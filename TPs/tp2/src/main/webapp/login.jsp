<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>
<body>
<div class="container">
    <h2>User Authentication</h2>

    <%
        String error = (String) request.getAttribute("errorMessage");
        if (error != null) {
    %>
    <div class="error"><%=error%></div>
    <%} %>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="login">Username:</label>
        <input type="text" id="login" name="login" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Sign in</button>
    </form>


    <p class="info">
        Test credentials from the DataModel:<br>
        <strong>changc / 0000</strong> or <strong>rozanb / 1234</strong>
    </p>
    </div>

</body>
</html>
