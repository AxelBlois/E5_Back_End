<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/login.jsp" var="loginUrl"/>
<c:url value="/css/style.css" var="styleUrl"/>

<html>
<head>
    <title>Access Denied</title>
    <link rel="stylesheet" href="${styleUrl}"/>
</head>

<body>
<div class="container">
    <h2 style="color: #e84118;">Unauthorized Access</h2>
    <p>You do not have permission to access this page.</p>

    <p>Please <a href="${loginUrl}">login</a> to continue.</p>

    <form action="${loginUrl}" method="get">
        <button type="submit">Go to Login</button>
    </form>
</div>
</html>
</body>
