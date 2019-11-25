package com.soochow.udf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetTxDateUdf {
    public static Connection dbConn(String name, String pass) {
        String driverName="org.apache.hive.jdbc.HiveDriver";
        Connection c = null;
        try {
            Class.forName(driverName);
            c = DriverManager.getConnection("jdbc:hive2://192.22.107.99:10000/test", name, pass);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void main(String[] args) {
        System.out.println(dbConn("hive", "hive"));
    }
}
