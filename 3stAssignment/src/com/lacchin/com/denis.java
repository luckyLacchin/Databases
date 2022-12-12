package com.lacchin.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class denis {

    static ArrayList<Integer> ids;
    static ArrayList<String> cids;
    static Connection connection;

    public static void main(String[] args) {
        try {
      /*connection =
        DriverManager.getConnection(
          "jdbc:postgresql://127.0.0.1/postgres?user=postgres&password=L%40W78%21jp9VJ%245P%26&ssl=false"
        );*/

            connection =
                    DriverManager.getConnection(
                            "jdbc:postgresql://sci-didattica.unitn.it/db_027?user=db_027&password=pass_027&ssl=false"
                    );

            // Delete tables if already exist
            long timePre = System.nanoTime();
            point1();
            System.out.println(
                    "Step 1 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Tables creation
            timePre = System.nanoTime();
            point2();
            System.out.println(
                    "Step 2 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Insert 1'000'000 random tuples in professor
            timePre = System.nanoTime();
            point3();
            System.out.println(
                    "Step 3 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Insert 1'000'000 random tuples in coruse
            timePre = System.nanoTime();
            point4();
            System.out.println(
                    "Step 4 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Select all ids from professor and print to stderr
            timePre = System.nanoTime();
            point5();
            System.out.println(
                    "Step 5 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Update all departments 1940 to 1973
            timePre = System.nanoTime();
            point6();
            System.out.println(
                    "Step 6 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Print all ids and addresses to stderr of professors with department 1973
            timePre = System.nanoTime();
            point7();
            System.out.println(
                    "Step 7 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Creates B+Tree
            timePre = System.nanoTime();
            point8();
            System.out.println(
                    "Step 8 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Select all ids from professor and print to stderr
            timePre = System.nanoTime();
            point9();
            System.out.println(
                    "Step 9 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Update all departments 1973 to 1974
            timePre = System.nanoTime();
            point10();
            System.out.println(
                    "Step 10 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            // Print all ids and addresses to stderr of professors with department 1974
            timePre = System.nanoTime();
            point11();
            System.out.println(
                    "Step 11 needs: " + (System.nanoTime() - timePre) + " ns"
            );

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void point1() throws SQLException {
        // Punto uno (eliminazione condizionale delle tabelle)
        Statement cursor = connection.createStatement();
        cursor.executeUpdate(
                "DROP TABLE IF EXISTS course;\n" + "DROP TABLE IF EXISTS professor;"
        );
        cursor.close();
    }

    public static void point2() throws SQLException {
        // Punto due (inserimento nel DB delle due tabelle)
        Statement cursor = connection.createStatement();
        cursor.executeUpdate(
                "CREATE TABLE IF NOT EXISTS Professor(\n" +
                        "id INTEGER NOT NULL,\n" +
                        "name CHAR(50) NOT NULL,\n" +
                        "address CHAR(50) NOT NULL,\n" +
                        "age INTEGER NOT NULL,\n" +
                        "department FLOAT NOT NULL,\n" +
                        "CONSTRAINT \"Professor_pkey\" PRIMARY KEY (id)\n" +
                        ");\n" +
                        "CREATE TABLE IF NOT EXISTS Course(\n" +
                        "cid CHAR(25) NOT NULL,\n" +
                        "cname CHAR(50) NOT NULL,\n" +
                        "credits CHAR(30) NOT NULL,\n" +
                        "teacher INTEGER NOT NULL,\n" +
                        "CONSTRAINT \"Course_pkey\" PRIMARY KEY (cid),\n" +
                        "CONSTRAINT \"Course_teacher_fkey\" FOREIGN KEY (teacher) REFERENCES Professor(id) MATCH SIMPLE\n" +
                        "ON UPDATE NO ACTION\n" +
                        "ON DELETE NO ACTION\n" +
                        ");"
        );
        cursor.close();
    }

    public static void point3() throws SQLException {
        // Punto tre (1000000 tuple casuali per professor)
        Random random = new Random();

        String[] names = {"Liam", "Noah", "Oliver", "Elijah", "James", "William", "Benjamin", "Lucas", "Henry", "Theodore", "Jack", "Levi", "Alexander", "Jackson", "Mateo", "Daniel", "Michael", "Mason", "Sebastian", "Ethan", "Logan", "Owen", "Samuel", "Jacob", "Asher", "Aiden", "John", "Joseph", "Wyatt", "David", "Leo", "Luke", "Julian", "Hudson", "Grayson", "Matthew", "Ezra", "Gabriel", "Carter", "Isaac", "Jayden", "Luca", "Anthony", "Dylan", "Lincoln", "Thomas", "Maverick", "Elias", "Josiah", "Charles", "Caleb", "Christopher", "Ezekiel", "Miles", "Jaxon", "Isaiah", "Andrew", "Joshua", "Nathan", "Nolan", "Adrian", "Cameron", "Santiago", "Eli", "Aaron", "Ryan", "Angel", "Cooper", "Waylon", "Easton", "Kai", "Christian", "Landon", "Colton", "Roman", "Axel", "Brooks", "Jonathan", "Robert", "Jameson", "Ian", "Everett", "Greyson", "Wesley", "Jeremiah", "Hunter", "Leonardo", "Jordan", "Jose", "Bennett", "Silas", "Nicholas", "Parker", "Beau", "Weston", "Austin", "Connor", "Carson", "Dominic", "Xavier", "Jaxson", "Jace", "Emmett", "Adam", "Declan", "Rowan", "Micah", "Kayden", "Gael", "River", "Ryder", "Kingston", "Damian", "Sawyer", "Luka", "Evan", "Vincent", "Legend", "Myles", "Harrison", "August", "Bryson", "Amir", "Giovanni", "Chase", "Diego", "Milo", "Jasper", "Walker", "Jason", "Brayden", "Cole", "Nathaniel", "George", "Lorenzo", "Zion", "Luis", "Archer", "Enzo", "Jonah", "Thiago", "Theo", "Ayden", "Zachary", "Calvin", "Braxton", "Ashton", "Rhett", "Atlas", "Jude", "Bentley", "Carlos", "Ryker", "Adriel", "Arthur", "Ace", "Tyler", "Jayce", "Max", "Elliot", "Graham", "Kaiden", "Maxwell", "Juan", "Dean", "Matteo", "Malachi", "Ivan", "Elliott", "Jesus", "Emiliano", "Messiah", "Gavin", "Maddox", "Camden", "Hayden", "Leon", "Antonio", "Justin", "Tucker", "Brandon", "Kevin", "Judah", "Finn", "King", "Brody", "Xander", "Nicolas", "Charlie", "Arlo", "Emmanuel", "Barrett", "Felix", "Alex", "Miguel", "Abel", "Alan", "Beckett", "Amari", "Karter", "Timothy", "Abraham", "Jesse", "Zayden", "Blake", "Alejandro", "Dawson", "Tristan", "Victor", "Avery", "Joel", "Grant", "Eric", "Patrick", "Peter", "Richard", "Edward", "Andres", "Emilio", "Colt", "Knox", "Beckham", "Adonis", "Kyrie", "Matias", "Oscar", "Lukas", "Marcus", "Hayes", "Caden", "Remington", "Griffin", "Nash", "Israel", "Steven", "Holden", "Rafael", "Zane", "Jeremy", "Kash", "Preston", "Kyler", "Jax", "Jett", "Kaleb", "Riley", "Simon", "Phoenix", "Javier", "Bryce", "Louis", "Mark", "Cash", "Lennox", "Paxton", "Malakai", "Paul", "Kenneth", "Nico", "Kaden", "Lane", "Kairo", "Maximus", "Omar", "Finley", "Atticus", "Crew", "Brantley", "Colin", "Dallas", "Walter", "Brady", "Callum", "Ronan", "Hendrix", "Jorge", "Tobias", "Clayton", "Emerson", "Damien", "Zayn", "Malcolm", "Kayson", "Bodhi", "Bryan", "Aidan", "Cohen", "Brian", "Cayden", "Andre", "Niko", "Maximiliano", "Zander", "Khalil", "Rory", "Francisco", "Cruz", "Kobe", "Reid", "Daxton", "Derek", "Martin", "Jensen", "Karson", "Tate", "Muhammad", "Jaden", "Joaquin", "Josue", "Gideon", "Dante", "Cody", "Bradley", "Orion", "Spencer", "Angelo", "Erick", "Jaylen", "Julius", "Manuel", "Ellis", "Colson", "Cairo", "Gunner", "Wade", "Chance", "Odin", "Anderson", "Kane", "Raymond", "Cristian", "Aziel", "Prince", "Ezequiel", "Jake", "Otto", "Eduardo", "Rylan", "Ali", "Cade", "Stephen", "Ari", "Kameron", "Dakota", "Warren", "Ricardo", "Killian", "Mario", "Romeo", "Cyrus", "Ismael", "Russell", "Tyson", "Edwin", "Desmond", "Nasir", "Remy", "Tanner", "Fernando", "Hector", "Titus", "Lawson", "Sean", "Kyle", "Elian", "Corbin", "Bowen", "Wilder", "Armani", "Royal", "Stetson", "Briggs", "Sullivan", "Leonel", "Callan", "Finnegan", "Jay", "Zayne", "Marshall", "Kade", "Travis", "Sterling", "Raiden", "Sergio", "Tatum", "Cesar", "Zyaire", "Milan", "Devin", "Gianni", "Kamari", "Royce", "Malik", "Jared", "Franklin", "Clark", "Noel", "Marco", "Archie", "Apollo", "Pablo", "Garrett", "Oakley", "Memphis", "Quinn", "Onyx", "Alijah", "Baylor", "Edgar", "Nehemiah", "Winston", "Major", "Rhys", "Forrest", "Jaiden", "Reed", "Santino", "Troy", "Caiden", "Harvey", "Collin", "Solomon", "Donovan", "Damon", "Jeffrey", "Kason", "Sage", "Grady", "Kendrick", "Leland", "Luciano", "Pedro", "Hank", "Hugo", "Esteban", "Johnny", "Kashton", "Ronin", "Ford", "Mathias", "Porter", "Erik", "Johnathan", "Frank", "Tripp", "Casey", "Fabian", "Leonidas", "Baker", "Matthias", "Philip", "Jayceon", "Kian", "Saint", "Ibrahim", "Jaxton", "Augustus", "Callen", "Trevor", "Ruben", "Adan", "Conor", "Dax", "Braylen", "Kaison", "Francis", "Kyson", "Andy", "Lucca", "Mack", "Peyton", "Alexis", "Deacon", "Kasen", "Kamden", "Frederick", "Princeton", "Braylon", "Wells", "Nikolai", "Iker", "Bo", "Dominick", "Moshe", "Cassius", "Gregory", "Lewis", "Kieran", "Isaias", "Seth", "Marcos", "Omari", "Shane", "Keegan", "Jase", "Asa", "Sonny", "Uriel", "Pierce", "Jasiah", "Eden", "Rocco", "Banks", "Cannon", "Denver", "Zaiden", "Roberto", "Shawn", "Drew", "Emanuel", "Kolton", "Ayaan", "Ares", "Conner", "Jalen", "Alonzo", "Enrique", "Dalton", "Moses", "Koda", "Bodie", "Jamison", "Phillip", "Zaire", "Jonas", "Kylo", "Moises", "Shepherd", "Allen", "Kenzo", "Mohamed", "Keanu", "Dexter", "Conrad", "Bruce", "Sylas", "Soren", "Raphael", "Rowen", "Gunnar", "Sutton", "Quentin", "Jaziel", "Emmitt", "Makai", "Koa", "Maximilian", "Brixton", "Dariel", "Zachariah", "Roy", "Armando", "Corey", "Saul", "Izaiah", "Danny", "Davis", "Ridge", "Yusuf", "Ariel", "Valentino", "Jayson", "Ronald", "Albert", "Gerardo", "Ryland", "Dorian", "Drake", "Gage", "Rodrigo", "Hezekiah", "Kylan", "Boone", "Ledger", "Santana", "Jamari", "Jamir", "Lawrence", "Reece", "Kaysen", "Shiloh", "Arjun", "Marcelo", "Abram", "Benson", "Huxley", "Nikolas", "Zain", "Kohen", "Samson", "Miller", "Donald", "Finnley", "Kannon", "Lucian", "Watson", "Keith", "Westin", "Tadeo", "Sincere", "Boston", "Axton", "Amos", "Chandler", "Leandro", "Raul", "Scott", "Reign", "Alessandro", "Camilo", "Derrick", "Morgan", "Julio", "Clay", "Edison", "Jaime", "Augustine", "Julien", "Zeke", "Marvin", "Bellamy", "Landen", "Dustin", "Jamie", "Krew", "Kyree", "Colter", "Johan", "Houston", "Layton", "Quincy", "Case", "Atreus", "Cayson", "Aarav", "Darius", "Harlan", "Justice", "Abdiel", "Layne", "Raylan", "Arturo", "Taylor", "Anakin", "Ander", "Hamza", "Otis", "Azariah", "Leonard", "Colby", "Duke", "Flynn", "Trey", "Gustavo", "Fletcher", "Issac", "Sam", "Trenton", "Callahan", "Chris", "Mohammad", "Rayan", "Lionel", "Bruno", "Jaxxon", "Zaid", "Brycen", "Roland", "Dillon", "Lennon", "Ambrose", "Rio", "Mac", "Ahmed", "Samir", "Yosef", "Tru", "Creed", "Tony", "Alden", "Aden", "Alec", "Carmelo", "Dario", "Marcel", "Roger", "Ty", "Ahmad", "Emir", "Landyn", "Skyler", "Mohammed", "Dennis", "Kareem", "Nixon", "Rex", "Uriah", "Lee", "Louie", "Rayden", "Reese", "Alberto", "Cason", "Quinton", "Kingsley", "Chaim", "Alfredo", "Mauricio", "Caspian", "Legacy", "Ocean", "Ozzy", "Briar", "Wilson", "Forest", "Grey", "Joziah", "Salem", "Neil", "Remi", "Bridger", "Harry", "Jefferson", "Lachlan", "Nelson", "Casen", "Salvador", "Magnus", "Tommy", "Marcellus", "Maximo", "Jerry", "Clyde", "Aron", "Keaton", "Eliam", "Lian", "Trace", "Douglas", "Junior", "Titan", "Cullen", "Cillian", "Musa", "Mylo", "Hugh", "Tomas", "Vincenzo", "Westley", "Langston", "Byron", "Kiaan", "Loyal", "Orlando", "Kyro", "Amias", "Amiri", "Jimmy", "Vicente", "Khari", "Brendan", "Rey", "Ben", "Emery", "Zyair", "Bjorn", "Evander", "Ramon", "Alvin", "Ricky", "Jagger", "Brock", "Dakari", "Eddie", "Blaze", "Gatlin", "Alonso", "Curtis", "Kylian", "Nathanael", "Devon", "Wayne", "Zakai", "Mathew", "Rome", "Riggs", "Aryan", "Avi", "Hassan", "Lochlan", "Stanley", "Dash", "Kaiser", "Benicio", "Bryant", "Talon", "Rohan", "Wesson", "Joe", "Noe", "Melvin", "Vihaan", "Zayd", "Darren", "Enoch", "Mitchell", "Jedidiah", "Brodie", "Castiel", "Ira", "Lance", "Guillermo", "Thatcher", "Ermias", "Misael", "Jakari", "Emory", "Mccoy", "Rudy", "Thaddeus", "Valentin", "Yehuda", "Bode", "Madden", "Kase", "Bear", "Boden", "Jiraiya", "Maurice", "Alvaro", "Ameer", "Demetrius", "Eliseo", "Kabir", "Kellan", "Allan", "Azrael", "Calum", "Niklaus", "Ray", "Damari", "Elio", "Jon", "Leighton", "Axl", "Dane", "Eithan", "Eugene", "Kenji", "Jakob", "Colten", "Eliel", "Nova", "Santos", "Zahir", "Idris", "Ishaan", "Kole", "Korbin", "Seven", "Alaric", "Kellen", "Bronson", "Franco", "Wes", "Larry", "Mekhi", "Jamal", "Dilan", "Elisha", "Brennan", "Kace", "Van", "Felipe", "Fisher", "Cal", "Dior", "Judson", "Alfonso", "Deandre", "Rocky", "Henrik", "Reuben", "Anders", "Arian", "Damir", "Jacoby", "Khalid", "Kye", "Mustafa", "Jadiel", "Stefan", "Yousef", "Aydin", "Jericho", "Robin", "Wallace", "Alistair", "Davion", "Alfred", "Ernesto", "Kyng", "Everest", "Gary", "Leroy", "Yahir", "Braden", "Kelvin", "Kristian", "Adler", "Avyaan", "Brayan", "Jones", "Truett", "Aries", "Joey", "Randy", "Jaxx", "Jesiah", "Jovanni", "Azriel", "Brecken", "Harley", "Zechariah", "Gordon", "Jakai", "Carl", "Graysen", "Kylen", "Ayan", "Branson", "Crosby", "Dominik", "Jabari", "Jaxtyn", "Kristopher", "Ulises", "Zyon", "Fox", "Howard", "Salvatore", "Turner", "Vance", "Harlem", "Jair", "Jakobe", "Jeremias", "Osiris", "Azael", "Bowie", "Canaan", "Elon", "Granger", "Karsyn", "Zavier", "Cain", "Dangelo", "Heath", "Yisroel", "Gian", "Shepard", "Harold", "Kamdyn", "Rene", "Rodney", "Yaakov", "Adrien", "Kartier", "Cassian", "Coleson", "Ahmir", "Darian", "Genesis", "Kalel", "Agustin", "Wylder", "Yadiel", "Ephraim", "Kody", "Neo", "Ignacio", "Osman", "Aldo", "Abdullah", "Cory", "Blaine", "Dimitri", "Khai", "Landry", "Palmer", "Benedict", "Leif", "Koen", "Maxton", "Mordechai", "Zev", "Atharv", "Bishop", "Blaise", "Davian"};

        PreparedStatement cursor = connection.prepareStatement(
                "INSERT INTO professor(id, name, address, age, department) VALUES (?,?,?,?,?);"
        );

        int r;
        HashSet<Integer> tempIDs = new HashSet<>();
        while (tempIDs.size() < 1000000) {
            r = random.nextInt(Integer.MAX_VALUE);
            if (!tempIDs.contains(r)) {
                tempIDs.add(r);
            }
        }
        ids = new ArrayList<>(tempIDs);

        float rr;
        HashSet<Float> tempDepartments = new HashSet<Float>();
        while (tempDepartments.size() < 999999) {
            rr = random.nextFloat(1000000);
            if (rr != ((float) 1940.0) && !tempDepartments.contains(rr)) {
                tempDepartments.add(rr);
            }
        }
        tempDepartments.add((float) 1940.0);
        ArrayList<Float> departments = new ArrayList<>(tempDepartments);

        for(int i = 0; i < ids.size(); i++) {
            cursor.setInt(1, ids.get(i));
            String temp = names[random.nextInt(names.length - 1)] + " " + names[random.nextInt(names.length - 1)];
            cursor.setString(
                    2,
                    temp.substring(0, temp.length() <= 50 ? temp.length() : 50)
            );
            temp = Integer.toString(random.nextInt(1000)) + ", " + names[random.nextInt(names.length - 1)] + " " + names[random.nextInt(names.length - 1)];
            cursor.setString(
                    3,
                    temp.substring(0, temp.length() <= 50 ? temp.length() : 50)
            );
            cursor.setInt(4, random.nextInt(100));
            cursor.setFloat(5, departments.get(i));
            cursor.addBatch();
            if (i % 1000 == 0 || i == ids.size() - 1) {
                cursor.executeBatch();
            }
        }

        cursor.close();
    }

    public static void point4() throws SQLException {
        // Punto quattro (1000000 tuple casuali per course)
        Random random = new Random();

        String[] cnames = {"Math", "Physics", "Programming", "Latin", "Polish", "Italian", "German", "Computer Science", "Hystory", "English", "Networking", "French"};
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        PreparedStatement cursor = connection.prepareStatement(
                "INSERT INTO course(cid, cname, credits, teacher) VALUES (?,?,?,?);"
        );

        HashSet<String> tempCIDs = new HashSet<>();
        while (tempCIDs.size() < 1000000) {
            String r = "";

            for(int i = 0; i<24; i++){
                r += chars[random.nextInt(chars.length)];
            }

            if (!tempCIDs.contains(r)) {
                tempCIDs.add(r);
            }
        }
        cids = new ArrayList<>(tempCIDs);

        for(int i = 0; i<cids.size(); i++){
            cursor.setString(1, cids.get(i));

            String temp = cnames[random.nextInt(cnames.length - 1)] + " " + random.nextInt(999);
            cursor.setString(
                    2,
                    temp.substring(0, temp.length() <= 50 ? temp.length() : 50)
            );

            temp = Integer.toString(random.nextInt(50)) + " semester " + Integer.toString(random.nextInt(2) + 1) ;
            cursor.setString(
                    3,
                    temp.substring(0, temp.length() <= 30 ? temp.length() : 30)
            );

            cursor.setInt(4, ids.get(random.nextInt(ids.size())));

            cursor.addBatch();

            if (i % 1000 == 0 || i == cids.size()-1) {
                cursor.executeBatch();
            }
        }
        cursor.close();
    }

    public static void point5() throws SQLException {
        // Punto cinque (seleziona tutti gli id e stampa sul stderr)

        Statement cursor = connection.createStatement();
        ResultSet resultSet = cursor.executeQuery("SELECT id FROM professor;");

        while (resultSet.next()) {
            System.err.println(Integer.toString(resultSet.getInt("id")));
        }

        resultSet.close();
        cursor.close();
    }

    public static void point6() throws SQLException {
        // Punto sei (aggiorna tutte le tuple che hanno il valore 1940)
        PreparedStatement cursor = connection.prepareStatement(
                "UPDATE professor SET department = ? WHERE department = ?;"
        );

        cursor.setFloat(1, 1973f);
        cursor.setFloat(2, 1940f);
        cursor.executeUpdate();

        cursor.close();
    }

    public static void point7() throws SQLException {
        // Punto sette (stampa id e indirizzo dei professori con dipartimento 1973 sullo stderr)
        PreparedStatement cursor = connection.prepareStatement(
                "SELECT id, address FROM professor WHERE department = ?;"
        );
        cursor.setFloat(1, 1973f);
        ResultSet resultSet = cursor.executeQuery();

        while (resultSet.next()) {
            System.err.println(resultSet.getString("id") + " " + resultSet.getString("address"));
        }

        resultSet.close();
        cursor.close();
    }

    public static void point8() throws SQLException {
        // Punto otto (crea B+tree)
        Statement cursor = connection.createStatement();
        cursor.executeUpdate("CREATE INDEX ON professor USING btree(department);");
        cursor.close();
    }

    public static void point9() throws SQLException {
        // Punto nove (seleziona tutti gli id e stampa sul stderr)
        Statement cursor = connection.createStatement();
        ResultSet resultSet = cursor.executeQuery("SELECT id FROM professor;");

        while (resultSet.next()) {
            System.err.println(Integer.toString(resultSet.getInt("id")));
        }

        resultSet.close();
        cursor.close();
    }

    public static void point10() throws SQLException {
        // Punto dieci (aggiorna tutte le tuple che hanno il valore 1973)
        PreparedStatement cursor = connection.prepareStatement(
                "UPDATE professor SET department = ? WHERE department = ?;"
        );

        cursor.setFloat(1, 1973f);
        cursor.setFloat(2, 1940f);
        cursor.executeUpdate();

        cursor.close();
    }

    public static void point11() throws SQLException {
        // Punto undici (stampa id e indirizzo dei professori con dipartimento 1974 sullo stderr)
        PreparedStatement cursor = connection.prepareStatement(
                "SELECT id, address FROM professor WHERE department = ?;"
        );
        cursor.setFloat(1, 1973f);
        ResultSet resultSet = cursor.executeQuery();

        while (resultSet.next()) {
            System.err.println(Integer.toString(resultSet.getInt("id")) + " " + resultSet.getString("address"));
        }

        resultSet.close();
        cursor.close();
    }

}
