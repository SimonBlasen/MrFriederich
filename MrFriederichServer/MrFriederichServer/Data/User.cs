using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MrFriederichServer.Data
{
    public enum UState
    {
        IDLE, WAIT_FOR_PLAYERID, WAIT_FOR_FILEHASH
    }

    public class User
    {
        private string ip;
        private int port;

        private int playerId;
        private string name;
        private byte[] lastHash;
        private UState state;

        public User(int playerId, string ip, int port)
        {
            this.ip = ip;
            this.port = port;
            this.playerId = playerId;
            this.name = "None";
            lastHash = new byte[] { };
        }

        public User(string ip, int port)
        {
            this.ip = ip;
            this.port = port;
            this.playerId = -1;
            this.name = "None";
            lastHash = new byte[] { };
        }

        public string Name
        {
            get
            {
                return name;
            }
            set
            {
                name = value;
            }
        }

        public int Id
        {
            get
            {
                return playerId;
            }
            set
            {
                playerId = value;
            }
        }

        public string Ip
        {
            get
            {
                return ip;
            }
        }

        public int Port
        {
            get
            {
                return port;
            }
        }

        public byte[] LastHash
        {
            get
            {
                return lastHash;
            }
        }

        public UState State
        {
            get
            {
                return state;
            }
            set
            {
                state = value;
            }
        }

        public override string ToString()
        {
            return "Id=" + playerId + ", Name=" + name;
        }
    }
}
