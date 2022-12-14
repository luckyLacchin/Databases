package com.lacchin.com;

import java.sql.*;
import java.util.*;

public class A3_227339_2 {

    static Connection connection;
    static ArrayList<Float> listFloat;
    static ArrayList<Integer> listInt;


    public static Connection getConnection () {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver"); //questo mi va <-> lo metto nella libreria!
            connection = DriverManager.getConnection("jdbc:postgresql://sci-didattica.unitn.it:5432/db_024","db_024","1324");
            if (connection != null) {
                //System.out.println("Connection OK!");
            } else {
                //System.out.println("Connection FAILED!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }

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

        Statement  st = connection.createStatement();
        String sql = "DROP TABLE if exists \"Course\";";
        executeStatement(sql,connection);
        sql = "DROP TABLE if exists \"Professor\";";
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

            tempFloat = random.nextFloat(5);
            startFloat = startFloat + tempFloat + 1F;
            if (startFloat != 1940)
                listFloat.add(startFloat);

        }
        Collections.shuffle(listFloat);
        while (listInt.size() < 1000000) {

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
        for (float t : listFloat) {

            statement.setInt(1,listInt.get(index));
            statement.setString(2,name_base+index);
            statement.setString(3,"Address"+(index));
            statement.setInt(4,index++);
            statement.setFloat(5,t);
            statement.addBatch();
            if (index % 100000 == 0) {
                statement.executeBatch();
            }
        }
        statement.setInt(1,listInt.get(index));
        statement.setString(2,name_base+index);
        statement.setString(3,"Address"+(index));
        statement.setInt(4,index);
        statement.setFloat(5,1940);
        statement.addBatch();
        statement.executeBatch();
    }

    public static void step4 () {

        listFloat = new ArrayList<>();
        Random random = new Random();
        float startFloat = 0;
        float tempFloat = 0;
        String command = "";
        startFloat = 0;

        while (listFloat.size() < 1000000) {
            tempFloat = random.nextFloat(5);
            startFloat = startFloat + tempFloat + 1F;
            listFloat.add(startFloat);
        }
        Collections.shuffle(listFloat);
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
                statement.setString(3,"Credits" + index);
                statement.setInt(4,listInt.get(index++));
                statement.addBatch();
                if ((index % 100000 == 0)  || (index == listFloat.size())) {
                    statement.executeBatch();
                }

            }

        }catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void step5 () throws SQLException {
        ResultSet results = null;
        String command = "";
        Statement stmt = null;

        command = "select p.id from \"Professor\" p";
        results = executeQuery(command,connection);
        while (results.next()) {
            int id = results.getInt(1);
            System.err.println(id);
        }
        stmt = results.getStatement();
        results.close();
        stmt.close();
    }

    public static void step6 () throws SQLException {

        String command = "";
        Statement stmt = null;
        ResultSet results = null;

        command = "UPDATE \"Professor\" " +
                "SET department = 1973 " +
                "WHERE department = 1940";
        stmt = connection.createStatement();
        int count = stmt.executeUpdate(command);


    }

    public static void step7 () throws SQLException {

        String command = "";
        ResultSet results = null;
        Statement stmt = null;

        command = "SELECT p.id, p.address FROM \"Professor\" p WHERE p.department = 1973";
        results = executeQuery(command,connection);
        while (results.next()) {
            int id = results.getInt(1);
            String address = results.getString(2);
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

    public static void step9() throws SQLException {
        ResultSet results = null;
        String command = "";
        Statement stmt = null;

        command = "select p.id from \"Professor\" p";
        results = executeQuery(command,connection);
        while (results.next()) {
            int id = results.getInt(1);
            System.err.println(id);
        }
        stmt = results.getStatement();
        results.close();
        stmt.close();

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
            String address = results.getString(2);
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
        ///////////////////////////////////////////////////////////////
        //1o quesito//////////////////////////////////////////////////
        step1();
        endTime = System.nanoTime();
        System.out.println("Step 1 needs " + (endTime - startTime) + " ns" );
        startTime = endTime;;

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

