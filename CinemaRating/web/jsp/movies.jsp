<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="customtags" prefix="ctg"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title><fmt:message key="title.all_movies" bundle="${rb}"/></title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/cinemarating.js"></script>
    </head>
    <body>
        <c:set var="page" value="path.page.movies" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <div class="content text-center">
                <div id="num_page" data-num_page="${num_page}"></div>

                <h1>
                    <c:if test="${page_type eq 'all'}" var="type_all">
                        <fmt:message key="all_movies.caption" bundle="${rb}"/>
                    </c:if>
                    <c:if test="${page_type eq 'top'}" var="type_top">
                        <fmt:message key="top_movies.caption" bundle="${rb}"/>
                    </c:if>
                </h1>

                <ctg:pagination upper="true" size="${fn:length(movies)}"/>

                <c:forEach var="movie" items="${movies}" begin="${num_page * 10}" end="${num_page * 10 + 9}">
                    <%@ include file="common/short_movie.jsp"%>
                </c:forEach>

                <ctg:pagination upper="false" size="${fn:length(movies)}"/>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>
    </body>
</html>
