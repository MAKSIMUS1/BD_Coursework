using System;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Data;
using System.Data.SqlClient;
using System.Text.Json;
using System.Numerics;
using System.Xml.Linq;

namespace HelloApp
{
    class Json_duffer
    {
        public int id { get; set; }
        public string name { get; set; }
        public Json_duffer(string name,  int age)
        {
            this.name = name;
            id = age;
        }
    }
    public class Competition
    {
        public int id { get; set; }
        public int? area_id { get; set; }
        public string name { get; set; }
    }
    public class Team
    {
        public int? id { get; set; }
        public int? area_id { get; set; }
        public string name { get; set; }
        public string short_name { get; set; }
        public string tla { get; set; }
        public string address { get; set; }
        public string website { get; set; }
        public int? founded { get; set; }
        public string club_colors { get; set; }
        public override bool Equals(object? obj)
        {
            if (obj is int i) return id == i;
            return false;
        }
        public override int GetHashCode() => name.GetHashCode();
    }
    public class Player
    {
        public int id { get; set; }
        public string name { get; set; }
        public string position { get; set; }
        public string date_of_birth { get; set; }
        public string nationality { get; set; }
        public int team_id { get; set; }
    }

        class Program
    {
        public static List<Team> jsbf_teams = new List<Team>();
        static async Task Main(string[] args)
        {
            string connectionString = "Server=localhost;Database=CourseTest;Trusted_Connection=True;";
            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                await connection.OpenAsync();
                Console.WriteLine("Подключение открыто");
                // Вывод информации о подключении
                Console.WriteLine("Свойства подключения:");
                Console.WriteLine($"\tСтрока подключения: {connection.ConnectionString}");
                Console.WriteLine($"\tБаза данных: {connection.Database}");
                Console.WriteLine($"\tСервер: {connection.DataSource}");
                Console.WriteLine($"\tВерсия сервера: {connection.ServerVersion}");
                Console.WriteLine($"\tСостояние: {connection.State}");
                Console.WriteLine($"\tWorkstationld: {connection.WorkstationId}");
                int number = 0;
                Console.Write("number: ");
                number = Convert.ToInt32(Console.ReadLine());
                switch (number)
                {
                    case 1:
                        #region areas
                        {
                            string sqlExpression = "use CourseTest;SELECT * FROM areas;";
                            SqlCommand command = new SqlCommand(sqlExpression, connection);
                            SqlDataReader reader = await command.ExecuteReaderAsync();

                            if (reader.HasRows) // если есть данные
                            {
                                // выводим названия столбцов
                                string columnName1 = reader.GetName(0);
                                string columnName2 = reader.GetName(1);

                                Console.WriteLine($"{columnName1}\t{columnName2}");
                                List<Json_duffer> jsbf = new List<Json_duffer>();
                                while (await reader.ReadAsync()) // построчно считываем данные
                                {
                                    int id = (int)reader.GetValue(0);
                                    string name = (string)reader.GetValue(1);
                                    jsbf.Add(new Json_duffer(name, id));
                                    Console.WriteLine($"{id} \t{name} ");
                                }

                                // сохранение данных
                                using (FileStream fs = new FileStream("areas.json", FileMode.OpenOrCreate))
                                {
                                    await JsonSerializer.SerializeAsync<List<Json_duffer>>(fs, jsbf);
                                    Console.WriteLine("Data has been saved to file");
                                }
                            }

                            await reader.CloseAsync();
                        }
                        #endregion
                        break;
                    case 2:
                        #region competitions;
                        {
                            string sqlExpression = "use CourseTest;SELECT * FROM Competitions;";
                            SqlCommand command = new SqlCommand(sqlExpression, connection);
                            SqlDataReader reader = await command.ExecuteReaderAsync();

                            if (reader.HasRows) // если есть данные
                            {
                                // выводим названия столбцов
                                string columnName1 = reader.GetName(0);
                                string columnName2 = reader.GetName(1);
                                string columnName3 = reader.GetName(3);

                                List<Competition> jsbf = new List<Competition>();
                                while (await reader.ReadAsync()) // построчно считываем данные
                                {
                                    int id = (int)reader.GetValue(0);
                                    int areaid = (int)reader.GetValue(1);
                                    string name = (string)reader.GetValue(2);
                                    Competition cp = new Competition();
                                    cp.id = id;
                                    cp.area_id = 
                                        areaid;
                                    cp.name = name;
                                    jsbf.Add(cp);
                                    Console.WriteLine($"{id}\t{areaid} \t{name}\t");
                                }
                                // сохранение данных
                                using (FileStream fs = new FileStream("competitions.json", FileMode.OpenOrCreate))
                                {
                                    await JsonSerializer.SerializeAsync<List<Competition>>(fs, jsbf);
                                    Console.WriteLine("Data has been saved to file");
                                }
                            }

                            await reader.CloseAsync();
                        }
                        #endregion
                        break;
                    case 3:
                        #region teams;
                        {
                            string sqlExpression = "use CourseTest;SELECT * FROM Teams;";
                            SqlCommand command = new SqlCommand(sqlExpression, connection);
                            SqlDataReader reader = await command.ExecuteReaderAsync();

                            if (reader.HasRows) // если есть данные
                            {
                                List<Team> jsbf = new List<Team>();
                                while (await reader.ReadAsync()) // построчно считываем данные
                                {
                                    try 
                                
                                    {
                                        int id = (int)reader.GetValue(0);
                                        int areaid = (int)reader.GetValue(1);
                                        string name = (string)reader.GetValue(2);
                                        string shortname = (string)reader.GetValue(3);
                                        string tla = (string)reader.GetValue(4);
                                        string address = (string)reader.GetValue(5);
                                        string website = (string)reader.GetValue(7);
                                        int founded = (int)reader.GetValue(9);
                                        string clubcolors = (string)reader.GetValue(10);
                                        Team team = new Team();
                                        team.id = id;
                                        team.area_id = areaid;
                                        team.name = name;
                                        team.short_name = shortname;
                                        team.tla = tla;
                                        team.address = address;
                                        team.website = website;
                                        team.founded = founded;
                                        team.club_colors = clubcolors;
                                        jsbf.Add(team); 
                                    }
                                    catch(Exception e)
                                    {
                                        Console.WriteLine(e.Message);
                                    }
                                }
                                using (FileStream fs = new FileStream("teams.json", FileMode.OpenOrCreate))
                                {
                                    await JsonSerializer.SerializeAsync<List<Team>>(fs, jsbf);
                                    Console.WriteLine("Data has been saved to file");
                                }
                            }

                            await reader.CloseAsync();
                        }
                        #endregion
                        break;
                    case 4:
                        #region teams;
                        {
                            string sqlExpression = "use CourseTest;SELECT * FROM Teams;";
                            SqlCommand command = new SqlCommand(sqlExpression, connection);
                            SqlDataReader reader = await command.ExecuteReaderAsync();

                            if (reader.HasRows) // если есть данные
                            {
                                while (await reader.ReadAsync()) // построчно считываем данные
                                {
                                    try

                                    {
                                        int id = (int)reader.GetValue(0);
                                        int areaid = (int)reader.GetValue(1);
                                        string name = (string)reader.GetValue(2);
                                        string shortname = (string)reader.GetValue(3);
                                        string tla = (string)reader.GetValue(4);
                                        string address = (string)reader.GetValue(5);
                                        string website = (string)reader.GetValue(7);
                                        int founded = (int)reader.GetValue(9);
                                        string clubcolors = (string)reader.GetValue(10);
                                        Team team = new Team();
                                        team.id = id;
                                        team.area_id = areaid;
                                        team.name = name;
                                        team.short_name = shortname;
                                        team.tla = tla;
                                        team.address = address;
                                        team.website = website;
                                        team.founded = founded;
                                        team.club_colors = clubcolors;
                                        jsbf_teams.Add(team);
                                    }
                                    catch (Exception e)
                                    {
                                        Console.WriteLine(e.Message);
                                    }
                                }
                            }
                            await reader.CloseAsync();
                        }
                        #endregion

                        #region players;
                        {
                            string sqlExpression = "use CourseTest;SELECT * FROM Players;";
                            SqlCommand command = new SqlCommand(sqlExpression, connection);
                            SqlDataReader reader = await command.ExecuteReaderAsync();

                            if (reader.HasRows) // если есть данные
                            {
                                List<Player> jsbf = new List<Player>();
                                while (await reader.ReadAsync()) // построчно считываем данные
                                {
                                    try

                                    {
                                        int id = (int)reader.GetValue(0);
                                        string name = (string)reader.GetValue(1);
                                        string pos = (string)reader.GetValue(2);
                                        string date = Convert.ToString(((DateTime)reader.GetValue(4))).Substring(0, 10);
                                        string nation = (string)reader.GetValue(6);
                                        int team_id = (int)reader.GetValue(8);
                                        
                                        Player player = new Player();
                                        player.id = id;
                                        player.name = name;
                                        player.position = pos;
                                        player.date_of_birth = date;
                                        player.nationality = nation;
                                        player.team_id = team_id;
                                        if (IsTeamId(player.team_id))
                                        {
                                            if (player.id == 1903)
                                                Console.WriteLine("STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP STOP ");
                                            jsbf.Add(player);
                                        }
                                        else
                                        {
                                            Console.WriteLine($"Team ID {player.team_id} not exists Player: {player.name}");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        Console.WriteLine(e.Message);
                                    }
                                }
                                using (FileStream fs = new FileStream("players.json", FileMode.OpenOrCreate))
                                {
                                    await JsonSerializer.SerializeAsync<List<Player>>(fs, jsbf);
                                    Console.WriteLine("Data has been saved to file");
                                }
                            }

                            await reader.CloseAsync();
                        }
                        #endregion
                        break;
                    default:
                        Console.WriteLine("default");
                        break;
                }
            }
            Console.WriteLine("Подключение закрыто...");
            Console.WriteLine("Программа завершила работу.");
        }
        public static bool IsTeamId(int i)
        {
            foreach(Team t in jsbf_teams)
            {
                if (t.id == i)
                    return true;
            }
            return false;
        }
    }
}