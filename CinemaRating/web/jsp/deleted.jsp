<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
<head>
    <title>
        <fmt:message key="title.deleted" bundle="${rb}"/>
    </title>
    <link rel='stylesheet' href="${pageContext.request.contextPath}/css/style.css" type='text/css'>
</head>
    <body class="deleted">
        <div class="wrapper container">
            <div class="content">
                <c:choose>
                    <c:when test="${deleted == 'movie'}">
                        <c:set var="page" value="path.page.deleted" scope="session"/>
                        <%@ include file="common/menu.jsp"%>
                        <h2>
                            <fmt:message key="label.movie_deleted" bundle="${rb}"/>.
                        </h2>
                    </c:when>
                    <c:when test="${deleted == 'profile'}">
                        <h2>
                            <fmt:message key="label.profile_deleted" bundle="${rb}"/>.
                        </h2>
                        <a href="${pageContext.request.contextPath}/controller?command=redirect&nextPage=path.page.login">
                            <fmt:message key="label.to_login" bundle="${rb}"/>
                        </a>
                    </c:when>
                </c:choose>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>
    </body>
</html>
