<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/persons" var="personsUrl"/>
<c:url value="/logout" var="logoutUrl"/>

<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>

<%@ include file="/WEB-INF/views/components/navbar.jsp" %>

<div class="container">
    <h2>Welcome, ${sessionScope.user.firstname}!</h2>
    <p>You are successfully logged in.</p>

    <form action="${personsUrl}" method="get">
        <button type="submit">Navigate to the Persons List</button>
    </form>

    <form action="${logoutUrl}" method="get">
        <button type="submit">Logout</button>
    </form>
</div>

</body>
</html>
