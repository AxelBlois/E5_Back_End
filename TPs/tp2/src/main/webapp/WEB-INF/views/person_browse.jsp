<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/css/style.css" var="styleUrl"/>
<c:url value="/person_manage" var="personManageUrl"/>


<html>
<head>
    <title>Persons List</title>
    <link rel="stylesheet" href="${styleUrl}">
</head>
<body>
<%@ include file="/WEB-INF/views/components/navbar.jsp" %>

    <div class="container">
        <h2>Lists of Registered Users</h2>

        <table border="1" width="100%" cellpadding="8" style="border-collapse: collapse;">
            <thead style="background-color: #273c75; color: white">
                <tr>
                    <th>View</th>
                    <th>ID</th>
                    <th>Firstname</th>
                    <th>Lastname</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Login</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${persons}">
                    <tr>
                        <td><a href="${personManageUrl}?id=${p.id}" class="btn-link">View</a></td>
                        <td>${p.id}</td>
                        <td>${p.firstname}</td>
                        <td>${p.lastname}</td>
                        <td>${p.mail}</td>
                        <td>${p.mobilePhone}</td>
                        <td>${p.login}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
