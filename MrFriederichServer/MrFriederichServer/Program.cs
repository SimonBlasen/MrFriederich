using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security.Cryptography;
using UnityServer;
using System.IO;

namespace MrFriederichServer
{
    class Program
    {
        static string filePath = ".\\test.txt";
        static Server server;
        static Encoding encoding;

        static void Main(string[] args)
        {
            Console.Write("> ");
            ConsoleWriteline("Starting server...");
            server = new Server(24400);
            server.ClientConnect += Server_ClientConnect;
            server.ClientDisconnect += Server_ClientDisconnect;
            server.ReceiveMessage += Server_ReceiveMessage;
            ConsoleWriteline("Server running on port 24400");

            encoding = Encoding.UTF8;

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

        private static void Server_ReceiveMessage(string ip, int port, byte[] message)
        {
            ConsoleWriteline("Received TCP from \"" + ip + "\":\"" + port + "\". Length=" + message.Length);
        }

        private static void Server_ClientDisconnect(string ip, int port)
        {
            ConsoleWriteline("Disconnection from \"" + ip + "\":\"" + port + "\"");
        }

        private static void Server_ClientConnect(string ip, int port)
        {
            ConsoleWriteline("Connection from \"" + ip + "\":\"" + port + "\"");
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
                //return md5.ComputeHash(File.ReadAllBytes(file));
                return md5.ComputeHash(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x07 });
            }
            else
            {
                return new byte[] { };
            }
        }
    }
}
