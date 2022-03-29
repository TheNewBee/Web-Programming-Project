/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Raymond Chan
 */
public class DBConnection {

    private static final String dbServer = "localhost";
    private static final String dbUsername = "webpro";
    private static final String dbPassword = "JBfXV72fYhecw3MR";
    private static final String dbDatabase = "webpro";
    private static Connection conn;

    public DBConnection() {
        conn = null;
    }

    public Connection getConnection() {
        if (conn != null) {
            return conn;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + dbServer + "/" + dbDatabase + "?user=" + dbUsername + "&password=" + dbPassword);
            return conn;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException E) {
        }
        return null;
    }
}
