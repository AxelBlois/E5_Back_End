<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/home" var="homeUrl" />
<c:url value="/persons" var="personsUrl" />
<c:url value="/logout" var="logoutUrl" />


<nav class="navbar">
    <div class="navbar-left">
        <a href="${homeUrl}" class="navbar-brand">MyApp Users</a>
        <a href="${personsUrl}" class="nav-link">Persons</a>
    </div>
    <div class="navbar-right">
        <form action="${logoutUrl}" method="get" style="display:inline;">
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
</nav>
