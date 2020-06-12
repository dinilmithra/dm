<!DOCTYPE html>
<%@ page isThreadSafe="false"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="com.dm.tomcatwebappsample.webproject.model.connections.DBConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>HR Database</title>
    </head>
    <body>
        <%
            ResultSet rs = DBConnection.getConnection().createStatement().executeQuery("Select * from EMPLOYEES");
            
            while(rs.next()){            
            
            out.println(rs.getString("EMPLOYEE_ID"));
            out.println(" "+rs.getString("FIRST_NAME")+" "+rs.getString("FIRST_NAME") );
            out.println(" "+rs.getString("EMAIL"));
            out.println(rs.getString(3)+"<br>");
            
            }

        %>
    </body>
</html>