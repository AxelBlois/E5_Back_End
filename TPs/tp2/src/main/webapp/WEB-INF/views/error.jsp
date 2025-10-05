<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/login" var="loginUrl"/>
<c:url value="/css/style.css" var="styleUrl"/>

<html>
<head>
    <title>Error ${statusCode}</title>
    <link rel="stylesheet" href="${styleUrl}"/>
</head>

<body>
<div class="container">
    <h2 style="color: #e84118;">Error ${statusCode}</h2>
    <p>${userMessage}</p>
    <p><small>Path: ${uri}</small></p>

    <c:if test="${not empty message}">
        <p style="color: gray;">${message}</p>
    </c:if>


    <form action="${loginUrl}" method="get">
        <button type="submit">Go to Login</button>
    </form>
</div>
</html>
</body>
