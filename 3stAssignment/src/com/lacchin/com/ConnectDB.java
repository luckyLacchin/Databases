package com.lacchin.com;

import java.sql.*;
import java.util.*;

public class ConnectDB {

    static Connection connection;
    static ArrayList<Float> listFloat;
    static ArrayList<Integer> listInt;


    public static Connection getConnection () {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver"); //questo mi va <-> lo metto nella libreria!
            connection = DriverManager.getConnection("jdbc:postgresql://sci-didattica.unitn.it:5432/db_024","db_024","1324");
            //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/assign3", "postgres", "1324");
            /*so we use this connection in order to connect to our local db
            So like this we are writing host = localhost and dbName: PostgreSQL_15;
            username : postgres
            password : 1324
            */
            if (connection != null) {
                System.out.println("Connection OK!");
            } else {
                System.out.println("Connection FAILED!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }
    public static boolean tableExist(Connection conn, String tableName) throws SQLException {
        boolean tExists = false;
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    //System.out.println("ciao1");
                    break;
                }
            }
        }
        return tExists;
    }
    //execute(): This method can be use for any kind of SQL statements.
    public static void executeStatement (String q, Connection con) {
        try{
            Statement stmt = con.createStatement();
            stmt.execute(q);
            stmt.close();
        }catch(SQLException e){
            System.out.println("Stetement: \n" + q);
            e.printStackTrace();
            throw new RuntimeException("Query " + q);
        }
    }

    public static ResultSet executeQuery (String query, Connection con) {
        try{
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery(query);
            return results;
        }catch (Exception e) {
            e.printStackTrace();
            return null; //non so quando potrebbe dare eccezione sinceramente.
        }
    }
    public static void step1 () throws SQLException {
        /*
        boolean exists = false;
        boolean exists1 = false;

        try {
            exists = tableExist(connection, "\"Professor\"");
            exists1 = tableExist(connection, "\"Course\"");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("E' avvenuta un'eccezione nell'esecuzione dei metodi");
        }

        if (exists1) {
            command = "drop table \"Course\"";
            executeStatement(command,connection);
        }
        if (exists) {
            command = "drop table \"Professor\"";
            executeStatement(command,connection);
            //System.out.println("ciao");
        }
         */
        Statement  st = connection.createStatement();
        String sql = "DROP TABLE if exists \"Course\";";
        executeStatement(sql,connection);
        sql = "DROP TABLE if exists \"Course\";";
        executeStatement(sql,connection);
        st.close();
    }
    public static void step2() throws SQLException {
        String command = "";
        command = "create table \"Professor\" (id INTEGER NOT NULL, name CHAR(50) NOT NULL, address CHAR (50) NOT NULL, " +
                "age INTEGER NOT NULL, department FLOAT NOT NULL, PRIMARY KEY (id))";
        executeStatement(command,connection);
        command = "create table \"Course\" (cid CHAR(25) NOT NULL, cname CHAR(50) NOT NULL, credits CHAR (30) NOT NULL, " +
                "teacher INTEGER NOT NULL, PRIMARY KEY (cid), FOREIGN KEY(teacher) REFERENCES \"Professor\")";
        executeStatement(command,connection);
    }

    public static void step3() throws SQLException {

        listFloat = new ArrayList<Float>();
        listInt = new ArrayList<Integer>();
        Random random = new Random();
        float startFloat = 0;
        int startInt = 0;
        int tempInt = 0;
        float tempFloat = 0;
        String command = "";

        while (listFloat.size() < 999999) {
            //System.out.println(listFloat.size());
            tempFloat = random.nextFloat(5);
            startFloat = startFloat + tempFloat + 1;
            if (startFloat != 1940)
                listFloat.add(startFloat);

        }
        while (listInt.size() < 1000000) {
            //System.out.println(listInt.size());
            tempInt = random.nextInt(5);
            startInt = startInt + tempInt + 1;
            listInt.add(startInt);
        }
        Collections.shuffle(listInt);

        command= "INSERT INTO \"Professor\"(id, name, address, age, department) " +
                " VALUES (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(command);
        int index = 0;
        String name_base = "John";
        Calendar cal = Calendar.getInstance();
        for (float t : listFloat) {
            //System.out.println(index);
            statement.setInt(1,listInt.get(index)); //ne prendo a caso, però ho dovuto mettere index come integer
            //String timeInMillis = Float.toString(cal.getTimeInMillis());
            //statement.setString(2,name_base+timeInMillis.substring(timeInMillis.length()-5,timeInMillis.length()-1));
            statement.setString(2,name_base+index);
            statement.setString(3,"Address"+(index));
            statement.setInt(4,index++);
            statement.setFloat(5,t);
            // solo il nome lo sto facendo con numeri a caso, il resto index per address e age
            statement.addBatch();
            if (index % 100000 == 0) { // lo faccio giusto per essere sicuro che me lo abbia fatto per tutti, forse sarebbe meglio fare meno di 10 mila
                statement.executeBatch();
            }
        }
        statement.setInt(1,listInt.get(index)); //ne prendo a caso, però ho dovuto mettere index come integer
        statement.setString(2,name_base+index);
        statement.setString(3,"Address"+(index));
        statement.setInt(4,index);
        statement.setFloat(5,1940);
        statement.addBatch();
        statement.executeBatch();
    }

    public static void step4 () {

        listFloat = new ArrayList<>(); //potrei usare la stessa lista di prima, ma credo che anche così vada bene
        Random random = new Random();
        float startFloat = 0;
        int startInt = 0;
        int tempInt = 0;
        float tempFloat = 0;
        String command = "";
        startFloat = 0;

        while (listFloat.size() < 1000000) {
            tempFloat = random.nextFloat(5);
            startFloat = startFloat + tempFloat + 1F;
            listFloat.add(startFloat);
        }
        //credo che tutti questi controlli potrei anche toglierli ma per sicurezza li lascio

        command = "INSERT INTO \"Course\"(cid, cname, credits, teacher) " +
                " VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(command);
            int index = 0;
            String name_base = "Name";
            Calendar cal = Calendar.getInstance();
            for (float t : listFloat) {
                statement.setString(1,"Course"+ t);
                statement.setString(2,name_base+index);
                statement.setString(3,"Credits" + index); //anche se arriverà ad un numero senza senso
                statement.setInt(4,listInt.get(index++));
                //dovrei aver gestito bene anche la FK!
                statement.addBatch();
                if ((index % 100000 == 0)  || (index == listFloat.size())) { // lo faccio giusto per essere sicuro che me lo abbia fatto per tutti
                    statement.executeBatch();
                }

            }

        }catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void step5 () {
        ResultSet results = null;
        String command = "";
        Statement stmt = null;

        command = "select p.id from \"Professor\" p";
        results = executeQuery(command,connection);
        try {
            while (results.next()) {
                int id = results.getInt(1);
                System.err.println(id);
            }
            stmt = results.getStatement();
            results.close();
            stmt.close();
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void step6 () {

        String command = "";
        Statement stmt = null;
        ResultSet results = null;

        command = "UPDATE \"Professor\" " +
                "SET department = 1973 " +
                "WHERE department = 1940";
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(command);
            //System.out.println("Sono state aggiornate " + count + " tuple");
        }catch(SQLException e) {
            System.out.println("Errore nella creazione dello statement");
        }
    }

    public static void step7 () throws SQLException {

        String command = "";
        ResultSet results = null;
        Statement stmt = null;

        command = "SELECT p.id, p.address FROM \"Professor\" p WHERE p.department = 1973";
        results = executeQuery(command,connection);
        while (results.next()) {
            int id = results.getInt(1);
            String address = results.getString(3);
            System.err.println(id + ", " + address);
        }
        stmt = results.getStatement();
        results.close();
        stmt.close();

    }

    public static void step8 () throws SQLException {
        Statement stmt = null;
        String command = "";

        stmt = connection.createStatement();
        command = "CREATE INDEX ON \"Professor\" USING btree(department)";
        stmt.executeUpdate(command);
        stmt.close();
    }

    public static void step9() {
        ResultSet results = null;
        String command = "";
        Statement stmt = null;

        command = "select p.id from \"Professor\" p";
        results = executeQuery(command,connection);
        try {
            while (results.next()) {
                int id = results.getInt(1);
                System.err.println(id);
            }
            stmt = results.getStatement();
            results.close();
            stmt.close();
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void step10 () {
        String command = "";
        Statement stmt = null;
        ResultSet results = null;

        command = "UPDATE \"Professor\" " +
                "SET department = 1974 " +
                "WHERE department = 1973";
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(command);
            //System.out.println("Sono state aggiornate " + count + " tuple");
        }catch(SQLException e) {
            System.out.println("Errore nella creazione dello statement");
        }
    }

    public static void step11 () throws SQLException {
        String command = "";
        ResultSet results = null;
        Statement stmt = null;

        command = "SELECT p.id, p.address FROM \"Professor\" p WHERE p.department = 1974";
        results = executeQuery(command,connection);
        while (results.next()) {
            int id = results.getInt(1);
            String address = results.getString(3);
            System.err.println(id + ", " + address);
        }
        stmt = results.getStatement();
        results.close();
        stmt.close();

    }


    public static void main(String[] args) throws SQLException {

        connection = getConnection();
        long startTime = System.nanoTime();
        long endTime = -1;
        Statement stmt = null;
        int tempInt;
        float tempFloat;
        ///////////////////////////////////////////////////////////////
        //1o quesito//////////////////////////////////////////////////
        step1();
        endTime = System.nanoTime();
        System.out.println("Step 1 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;;
        //oK! funziona l'unica cosa è che devo mettere professor e course minuscoli --> case sensitive, il problema è che mi vengono salvati nel db minuscoli....DA CHIEDERE
        ////////////////////////////////////////////////////////////
        ////////////////////2o quesito////////////////////////////
        step2();
        endTime = System.nanoTime();
        System.out.println("Step 2 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        ////////////////3o quesito//////////////////////////////
        step3();
        endTime = System.nanoTime();
        System.out.println("Step 3 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;

        //////////////////////////////////////////////////////////////////////////
        //////////////////////4o quesito/////////////////////////////////////////
        step4();
        endTime = System.nanoTime();
        System.out.println("Step 4 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //non so perché me lo mette double precision e non FLOAT department!!!
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 5//////////////////////////////
        step5();
        endTime = System.nanoTime();
        System.out.println("Step 5 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;

        ////////////////////verifica///////////////////////
        /*
        command = "select p.id from professor p where p.department = 1940";
        results = executeQuery(command,connection);
        try {
            while (results.next()) {
                int id = results.getInt(1);
                System.err.println("ciao" + id);
            }
            stmt = results.getStatement();
            results.close();
            stmt.close();
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

         */
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 6//////////////////////////////

        step6();
        endTime = System.nanoTime();
        System.out.println("Step 6 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 7//////////////////////////////

        step7();
        endTime = System.nanoTime();
        System.out.println("Step 7 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;


        //////////////////////////////////////////////////////////
        //////////////////QUESITO 8//////////////////////////////

        step8();
        endTime = System.nanoTime();
        System.out.println("Step 8 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;


        //////////////////////////////////////////////////////////
        //////////////////QUESITO 9//////////////////////////////
        step9();
        endTime = System.nanoTime();
        System.out.println("Step 9 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 10//////////////////////////////
        step10();
        endTime = System.nanoTime();
        System.out.println("Step 10 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 11//////////////////////////////

        step11();
        endTime = System.nanoTime();
        System.out.println("Step 11 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;



        connection.close();
    }



}

/*
1a question un po' semplificata!
DatabaseMetaData dbm = con.getMetaData();
// check if "employee" table is there
ResultSet tables = dbm.getTables(null, null, "employee", null);
if (tables.next()) {
  // Table exists
}
else {
  // Table does not exist
}
 */
/*
1a question un po' più complessa ma magari meglio
public static boolean tableExist(Connection conn, String tableName) throws SQLException {
    boolean tExists = false;
    try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
        while (rs.next()) {
            String tName = rs.getString("TABLE_NAME");
            if (tName != null && tName.equals(tableName)) {
                tExists = true;
                break;
            }
        }
    }
    return tExists;
}*/
