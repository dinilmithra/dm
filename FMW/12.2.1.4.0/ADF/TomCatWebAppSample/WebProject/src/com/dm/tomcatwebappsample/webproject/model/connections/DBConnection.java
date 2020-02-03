package com.dm.tomcatwebappsample.webproject.model.connections;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;


public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException, NamingException {

        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/HRDS");
        Connection connection = ds.getConnection();

        return connection;
    }
}
