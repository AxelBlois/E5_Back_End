<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/css/style.css" var="styleUrl" />
<c:url value="/logout" var="logoutUrl" />
<c:url value="/person_browse" var="personsUrl" />

<html>
<head>
    <title>${person.firstname} ${person.lastname}</title>
    <link rel="stylesheet" href="${styleUrl}" />
</head>
<body>
<%@ include file="/WEB-INF/views/components/navbar.jsp" %>

<div class="container">
    <h2>${person.firstname} ${person.lastname}</h2>
    <p><strong>Email:</strong> ${person.mail}</p>
    <p><strong>Phone:</strong> ${person.mobilePhone}</p>
    <p><strong>Login:</strong> ${person.login}</p>

    <a href="${personsUrl}">
        <button type="button">Back to List</button>
    </a>
</div>

</body>
</html>
