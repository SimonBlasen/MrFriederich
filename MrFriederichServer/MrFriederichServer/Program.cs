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
                if (message.Length >= 6)
                {
                    int playerId = (message[2] << 24) | (message[3] << 16) | (message[4] << 8) | (message[5]);
                    user = getUserById(playerId);
                }

                // Received a users name and id
                if (message[0] == 0 && message[1] == 2)
                {
                    int playerId = (message[2] << 24) | (message[3] << 16) | (message[4] << 8) | (message[5]);
                    //TODO parse name and set it to the user
                    //...
                    bool isNew = true;
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
                        users.Add(newUser);
                    }
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
            }
        }

        private static void Server_ClientDisconnect(string ip, int port)
        {
            ConsoleWriteline("Disconnection from \"" + ip + "\":\"" + port + "\"");
        }

        private static void Server_ClientConnect(string ip, int port)
        {
            ConsoleWriteline("Connection from \"" + ip + "\":\"" + port + "\"");
            
            server.Send(ip, port, new byte[] { 0, 1, 0 });
        }

        private static async void CommandExecute(string[] commands)
        {
            if (commands[0] == "help")
            {
                ConsoleWriteline("Available commands:\n"
                                + " cancel                     Cancels the starting process\n"
                                + " createportmaps             Creates the explicit Open.Nat port maps\n"
                                + " deleteportmaps             Deletes the opened port maps\n"
                                + " kill                       Instantaneously stopps and closes the server without terminating any thready or ports\n"
                                + " listnatdevices             Lists all open Open.Nat NatDevices\n"
                                + " listportmaps               Lists all open Open.Nat port maps\n"
                                + " playerlist                 Returns a list of all player and ids\n"
                                + " player kick [playerID]     Kicks the given player\n"
                                + " repair properties          Repairs the server.properties file\n"
                                + " select [number]            Selects the given NatDevice to use\n"
                                + " setmap [mapNumber]         Sets the map to the given map number\n"
                                + " setminplayers [amount]     Sets the amount of players needed, to start a race\n"
                                + " setstarttime [start time]  Sets the time in seconds to wait for other players\n"
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
    }
}
