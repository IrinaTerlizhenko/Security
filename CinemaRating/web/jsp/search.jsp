<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title><fmt:message key="title.search" bundle="${rb}"/></title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <c:set var="page" value="path.page.search" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <div class="content text-center">
                <h1>
                    <fmt:message key="title.search" bundle="${rb}"/>
                </h1>

                <c:choose>
                    <c:when test="${fn:length(movie_result) + fn:length(movie_result) > 0}">
                        <c:forEach var="movie" items="${movie_result}">
                            <%@ include file="common/short_movie.jsp"%>
                        </c:forEach>

                        <c:forEach var="user" items="${user_result}">
                            <%@ include file="common/short_user.jsp"%>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <h2>
                            <fmt:message key="search.not_found" bundle="${rb}"/>
                        </h2>
                    </c:otherwise>
                </c:choose>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>
    </body>
</html>
