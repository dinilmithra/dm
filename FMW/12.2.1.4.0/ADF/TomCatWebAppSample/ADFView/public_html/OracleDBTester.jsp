<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" contentType="text/html"%>
<%-- These libraries are required for the <c> and <sql> tags--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html>
    <head>
        <sql:query var="democusts" dataSource="jdbc/HRDS">SELECT * FROM EMPLOYEES</sql:query>
    </head>
    <body>
        <h1>Testing JDBC JINDI Oracle</h1>
        <table width='500' border='1'>
            <tr>
                <th align='left'>Id</th>
                <th align='left'>Name</th>
                <th align='left'>Phone Number</th>
            </tr>
             
            <c:forEach var="democusts" items="${democusts.rows}">
                <tr>
                    <td>
                        ${democusts.EMPLOYEE_ID}
                    </td>
                    <td>
                        ${democusts.FIRST_NAME}
                         
                        ${democusts.LAST_NAME}
                    </td>
                    <td>
                        ${democusts.PHONE_NUMBER}
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>