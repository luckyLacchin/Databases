package com.lacchin.com;

import java.sql.*;
import java.util.*;

public class ConnectDB {


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


    public static void main(String[] args) throws SQLException {

        Connection connection = null;
        String command = "";
        connection = getConnection();
        long startTime = System.nanoTime();
        long endTime = -1;
        ResultSet results = null;
        Statement stmt = null;
        int tempInt;
        float tempFloat;
        ///////////////////////////////////////////////////////////////
        //1o quesito//////////////////////////////////////////////////
        boolean exists = false;
        boolean exists1 = false;
        try {
            exists = tableExist(connection, "professor");
            exists1 = tableExist(connection, "course");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("E' avvenuta un'eccezione nell'esecuzione dei metodi");
        }

        if (exists1) {
            command = "drop table Course";
            executeStatement(command,connection);
        }
        if (exists) {
            command = "drop table Professor";
            executeStatement(command,connection);
            //System.out.println("ciao");
        }
        endTime = System.nanoTime();
        System.out.println("Step 1 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //oK! funziona l'unica cosa è che devo mettere professor e course minuscoli --> case sensitive, il problema è che mi vengono salvati nel db minuscoli....DA CHIEDERE
        ////////////////////////////////////////////////////////////
        ////////////////////2o quesito////////////////////////////

        command = "create table Professor (id INTEGER not NULL, name CHAR(50) not NULL, address CHAR (50) not NULL, " +
                "age INTEGER not NULL, department FLOAT not NULL, PRIMARY KEY (id))";
        executeStatement(command,connection);
        command = "create table Course (cid CHAR(25) not NULL, cname CHAR(50) not NULL, credits CHAR (30) not NULL, " +
                "teacher INTEGER not NULL, PRIMARY KEY (cid), FOREIGN KEY(teacher) REFERENCES professor)";
        executeStatement(command,connection);
        endTime = System.nanoTime();
        System.out.println("Step 2 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        ////////////////3o quesito//////////////////////////////

        ArrayList<Float> listFloat = new ArrayList<Float>();
        ArrayList<Integer> listInt = new ArrayList<Integer>();
        Random random = new Random();
        float startFloat = 0;
        int startInt = 0;
        /*
        while (listFloat.size() < 1000000 || listInt.size() < 1000000) {
            System.out.println("listFloatSize = " + listFloat.size());
            if (listFloat.size() < 999999) {
                startFloat = startFloat + random.nextFloat();
                if (startFloat != 1940F)
                    listFloat.add(startFloat);
            }
            if (listInt.size() < 1000000) {
                startInt = startInt + random.nextInt(1000);
                if (!listInt.contains(startInt))
                    listInt.add(startInt);
            }
            //credo che tutti questi controlli potrei anche toglierli ma per sicurezza li lascio
        }

         */
        while (listFloat.size() < 999999) {
            System.out.println(listFloat.size());
            tempFloat = random.nextFloat(100);
            if (tempFloat != 0) {
                startFloat = startFloat + tempFloat;
                if (startFloat != 1940F)
                    listFloat.add(startFloat);
            }

        }
        listFloat.add(1940F);
        while (listInt.size() < 1000000) {
            System.out.println(listInt.size());
            tempInt = random.nextInt(100);
            if (tempInt != 0) {
                startInt = startInt + tempInt;
                listInt.add(startInt);
            }
        }
        Collections.shuffle(listInt);

        command= "INSERT INTO professor(id, name, address, age, department) " +
                " VALUES (?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(command);
            int index = 0;
            String name_base = "John";
            Calendar cal = Calendar.getInstance();
            for (float t : listFloat) {
                System.out.println(index);
                statement.setInt(1,listInt.get(index)); //ne prendo a caso, però ho dovuto mettere index come integer
                String timeInMillis = Float.toString(cal.getTimeInMillis());
                //statement.setString(2,name_base+timeInMillis.substring(timeInMillis.length()-5,timeInMillis.length()-1));
                statement.setString(2,name_base+index);
                statement.setString(3,"Address"+(index));
                statement.setInt(4,index++);
                statement.setFloat(5,t);
                // solo il nome lo sto facendo con numeri a caso, il resto index per address e age
                statement.addBatch();
                if ((index % 100000 == 0)  || (index == listFloat.size())) { // lo faccio giusto per essere sicuro che me lo abbia fatto per tutti, forse sarebbe meglio fare meno di 10 mila
                    statement.executeBatch();
                }

            }

        }catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        endTime = System.nanoTime();
        System.out.println("Step 3 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;

        //////////////////////////////////////////////////////////////////////////
        //////////////////////4o quesito/////////////////////////////////////////
        /*
        listFloat = new ArrayList<>();
        //random = new Random(); non credo abbia fare una nuova istanza
        startFloat = 0;
        while (listFloat.size() < 1000000) {
            tempFloat = random.nextFloat(100);
            if (tempFloat != 0) {
                startFloat = startFloat + tempFloat;
                listFloat.add(startFloat);
            }
        }
            //credo che tutti questi controlli potrei anche toglierli ma per sicurezza li lascio

        command = "INSERT INTO course(cid, cname, credits, teacher) " +
                " VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(command);
            int index = 0;
            String name_base = "Course";
            Calendar cal = Calendar.getInstance();
            for (float t : listFloat) {
                statement.setString(1,"Course"+ t);
                statement.setString(2,name_base+index);
                statement.setString(3,"Credits" + index); //anche se arriverà ad un numero senza senso
                statement.setInt(4,listInt.get(index++));
                //dovrei aver gestito bene anche la FK!
                statement.addBatch();
                if ((index % 10000 == 0)  || (index == listFloat.size())) { // lo faccio giusto per essere sicuro che me lo abbia fatto per tutti
                    statement.executeBatch();
                }

            }

        }catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        endTime = System.nanoTime();
        System.out.println("Step 4 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //non so perché me lo mette double precision e non FLOAT department!!!
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 5//////////////////////////////
        command = "select p.id from professor p";
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
        endTime = System.nanoTime();
        System.out.println("Step 5 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;

         */
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
        /*
        command = "UPDATE professor " +
                "SET department = 1973 " +
                "WHERE department = 1940";
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(command);
            //System.out.println("Sono state aggiornate " + count + " tuple");
        }catch(SQLException e) {
            System.out.println("Errore nella creazione dello statement");
        }
        endTime = System.nanoTime();
        System.out.println("Step 6 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 7//////////////////////////////

        command = "SELECT p.id, p.address FROM professor p WHERE p.department = 1973";
        results = executeQuery(command,connection);
        try {
            while (results.next()) {
                int id = results.getInt(1);
                String address = results.getString(2);
                System.err.println(id + " " + address);
            }
            stmt = results.getStatement();
            results.close();
            stmt.close();
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        endTime = System.nanoTime();
        System.out.println("Step 7 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;

         */
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 8//////////////////////////////
        /*
        stmt = connection.createStatement();
        command = "CREATE INDEX ON professor USING btree(department)";
        stmt.executeUpdate(command);
        stmt.close();
        endTime = System.nanoTime();
        System.out.println("Step 8 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;

         */
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 9//////////////////////////////
        /*
        endTime = System.nanoTime();
        System.out.println("Step 9 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 10//////////////////////////////
        endTime = System.nanoTime();
        System.out.println("Step 10 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;
        //////////////////////////////////////////////////////////
        //////////////////QUESITO 11//////////////////////////////
        endTime = System.nanoTime();
        System.out.println("Step 11 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;

         */


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
