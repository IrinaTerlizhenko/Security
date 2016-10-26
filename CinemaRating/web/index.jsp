<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="locale" value="en" scope="session"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title><fmt:message key="title.index" bundle="${rb}"/></title>
    </head>
    <body>
        <c:set var="size" value="100" scope="session"/>
        <c:set var="form" value="none" scope="session"/>
        <c:set var="num_page" value="0" scope="session"/>
        <jsp:forward page="/jsp/login.jsp"/>
    </body>
</html>