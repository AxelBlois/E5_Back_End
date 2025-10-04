<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
<div class="container">
    <h2>Welcome, ${sessionScope.user.firstname}!</h2>
    <p>You are successfully logged in.</p>

    <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit">Logout</button>
    </form>
</div>
</body>
</html>
