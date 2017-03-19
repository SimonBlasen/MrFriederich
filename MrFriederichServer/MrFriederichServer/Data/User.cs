using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MrFriederichServer.Data
{
    public class User
    {
        private int playerId;
        private string name;
        private byte[] lastHash;

        public User(int playerId)
        {
            this.playerId = playerId;
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

        public byte[] LastHash
        {
            get
            {
                return lastHash;
            }
        }

        public override string ToString()
        {
            return "Id=" + playerId + ", Name=" + name;
        }
    }
}
