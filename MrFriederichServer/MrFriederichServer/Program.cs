using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security.Cryptography;
using UnityServer;
using System.IO;
using MrFriederichServer.Data;
using System.Timers;

namespace MrFriederichServer
{
    class Program
    {
        static string filePath = ".\\test.txt";
        static Server server;
        static Encoding encoding;

        static Timer timer;

        static List<User> users;

        static void Main(string[] args)
        {
            Console.Write("> ");
            ConsoleWriteline("Starting server...");

            users = new List<User>();

            server = new Server(24400);
            server.ClientConnect += Server_ClientConnect;
            server.ClientDisconnect += Server_ClientDisconnect;
            server.ReceiveMessage += Server_ReceiveMessage;
            ConsoleWriteline("Server running on port 24400");

            encoding = Encoding.UTF8;

            timer = new Timer();
            timer.Interval = 5000;
            timer.Elapsed += Timer_Elapsed;
            timer.Start();

            ConsoleWriteline("Used encoding: " + encoding.ToString());


            while (true)
            {
                string input = Console.ReadLine();
                if (input.Length > 0)
                {
                    string[] inputArgs = new string[input.Split(' ').Length];
                    for (int i = 0; i < inputArgs.Length; i++)
                    {
                        inputArgs[i] = input.Split(' ')[i].ToLower();
                    }
                    CommandExecute(inputArgs);
                }
            }
        }

        /**
         * This timer checks the UStates of the Users and re-requests information, if not received yet
         * */
        private static void Timer_Elapsed(object sender, ElapsedEventArgs e)
        {
            for (int i = 0; i < users.Count; i++)
            {
                if (users[i].State == UState.WAIT_FOR_FILEHASH)
                {
                    server.Send(users[i].Ip, users[i].Port, new byte[] { 0, 3, 0 });
                }
                else if (users[i].State == UState.WAIT_FOR_PLAYERID)
                {
                    server.Send(users[i].Ip, users[i].Port, new byte[] { 0, 1, 0 });
                }
                else if (users[i].State == UState.IDLE)
                {
                    //Send watchdog
                    server.Send(users[i].Ip, users[i].Port, new byte[] { 0, 5, 0 });
                }
            }
        }

        private static void Server_ReceiveMessage(string ip, int port, byte[] message)
        {
            ConsoleWriteline("Received TCP from \"" + ip + "\":\"" + port + "\". Length=" + message.Length);

            if (message.Length >= 2)
            {
                User user = null;
                if (message.Length >= 6 && (message[0] != 0 || message[1] != 2))
                {
                    int playerId = (message[2] << 24) | (message[3] << 16) | (message[4] << 8) | (message[5]);
                    user = getUserById(playerId);
                }
                else
                {
                    for (int i = 0; i < users.Count; i++)
                    {
                        if (users[i].Ip == ip && users[i].Port == port)
                        {
                            user = users[i];
                            break;
                        }
                    }
                }

                // Received a users name and id
                if (message[0] == 0 && message[1] == 2)
                {
                    int playerId = (message[2] << 24) | (message[3] << 16) | (message[4] << 8) | (message[5]);

                    string playerName = System.Text.Encoding.UTF8.GetString(message, 6, message.Length - 6);

                    /*bool isNew = true;
                    for (int i = 0; i < users.Count; i++)
                    {
                        if (users[i].Id == playerId)
                        {
                            isNew = false;
                            break;
                        }
                    }

                    if (isNew)
                    {
                        User newUser = new User(playerId, ip, port);
                        newUser.State = UState.WAIT_FOR_FILEHASH;
                        user = newUser;
                        users.Add(newUser);
                    }*/

                    user.Id = playerId;
                    user.Name = playerName;
                    user.State = UState.WAIT_FOR_FILEHASH;

                    ConsoleWriteline("Got playerName and id:");
                    ConsoleWriteline("Playername: " + user.Name);
                    ConsoleWriteline("Id: " + user.Id);

                    kickAllOtherUsersWithSameId(user);
                }

                // Received a filehash from an user
                else if (message[0] == 0 && message[1] == 4)
                {
                    if (user != null)
                    {
                        user.State = UState.IDLE;
                        //TODO save and compare hash
                    }
                }

                // Active disconnect
                else if (message[0] == 0 && message[1] == 6)
                {
                    user = null;
                    for (int i = 0; i < users.Count; i++)
                    {
                        if (users[i].Ip == ip && users[i].Port == port)
                        {
                            user = users[i];
                            break;
                        }
                    }

                    if (user != null)
                    {
                        users.Remove(user);
                    }
                    else
                    {
                        ConsoleWriteline("WARNING: TCP Connection removed itself. But no User instance was found, so none has been removed");
                    }
                    ConsoleWriteline("TCP Connection removed itself actively");

                    server.RemoveTCPConnection(ip, port);
                }

                // Test message
                else if (message[0] == 0 && message[1] == 7)
                {
                    ConsoleWriteline("Test message arrived");
                }

                // Test message
                else if (message[0] == 0 && message[1] == 8)
                {
                    ConsoleWriteline("Playerid: " + message[2].ToString() + message[3].ToString() + message[4].ToString() + message[5].ToString());
                }
            }
        }

        private static void Server_ClientDisconnect(string ip, int port)
        {
            ConsoleWriteline("Disconnection from \"" + ip + "\":\"" + port + "\"");
        }

        private static void Server_ClientConnect(string ip, int port)
        {
            ConsoleWriteline("Connection from \"" + ip + "\":\"" + port + "\"");

            User newUser = new User(ip, port);
            newUser.State = UState.WAIT_FOR_PLAYERID;
            users.Add(newUser);

            server.Send(ip, port, new byte[] { 0, 1, 0 });
        }

        private static async void CommandExecute(string[] commands)
        {
            if (commands[0] == "help")
            {
                ConsoleWriteline("Available commands:\n"
                                + " close                      Calls the CloseServer() method on the server\n"
                                + " gethash                    Hashs the file in filepath and shows the hash\n"
                                + " kill                       Instantaneously stopps and closes the server without terminating any thready or ports\n"
                                + " listusers                  Lists all User instances currently\n"
                                + " send [args0] [arg1] ...    Sends all given arguments to all connections\n"
                                + " stop                       Stops the server properly");
            }

            else if (commands.Length == 1 && (commands[0] == "kill"))
            {
                Environment.Exit(0);
            }

            else if (commands.Length >= 1 && commands[0] == "send")
            {
                string concated = commands[1];
                for (int i = 0; i < commands.Length - 2; i++)
                {
                    concated += " " + commands[i + 2];
                }
                server.SendAll(encoding.GetBytes(concated));
                ConsoleWriteline("Sent message to all");
            }

            else if (commands.Length >= 1 && commands[0] == "gethash")
            {
                byte[] hash = getFileHash(filePath);
                string hashString = "";
                for (int i = 0; i < hash.Length; i++)
                {
                    hashString += Convert.ToString(hash[i]) + ",";
                }
                hashString = hashString.Substring(0, hashString.Length - 1);
                ConsoleWriteline("Calculated hash: " + hashString);
            }

            else if (commands.Length >= 1 && commands[0] == "listusers")
            {
                for (int i = 0; i < users.Count; i++)
                {
                    ConsoleWriteline("users[" + i + "]: " + users[i].ToString());
                }
            }

            else if (commands.Length >= 1 && commands[0] == "close")
            {
                server.CloseServer();
            }

            else if (commands.Length >= 1 && commands[0] == "stop")
            {
                server.CloseServer();
                Environment.Exit(0);
            }
        }



        private static void ConsoleWriteline(string text)
        {
            Console.WriteLine(text);
            Console.Write("> ");
        }

        private static byte[] getFileHash(string file)
        {
            if (File.Exists(file))
            {
                MD5 md5 = new MD5CryptoServiceProvider();
                return md5.ComputeHash(File.ReadAllBytes(file));
                //return md5.ComputeHash(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x07 });
            }
            else
            {
                return new byte[] { };
            }
        }

        private static User getUserById(int id)
        {
            for (int i = 0; i < users.Count; i++)
            {
                if (users[i].Id == id)
                {
                    return users[i];
                }
            }

            return null;
        }

        private static void kickAllOtherUsersWithSameId(User realUser)
        {
            for (int i = 0; i < users.Count; i++)
            {
                if (users[i].Id == realUser.Id && (users[i].Ip != realUser.Ip || users[i].Port != realUser.Port))
                {
                    ConsoleWriteline("Kicked an user");
                    users.RemoveAt(i);
                    i--;
                }
            }
            
            for (int i = 0; i < users.Count; i++)
            {
                if (users[i].Id == realUser.Id)
                {
                    ConsoleWriteline("Still exists: " + users[i].Name + ", With ip=" + users[i].Ip + " and port=" + users[i].Port);
                }
            }
        }
    }
}
