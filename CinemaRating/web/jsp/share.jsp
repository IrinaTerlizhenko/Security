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

        <div class="wrapper container">
            <form role="form" class="content form-horizontal" method="POST" action="${pageContext.request.contextPath}/controller">
                <input type="hidden" name="command" value="share_movies"/>
                <input type="hidden" name="id" value="${id}"/>
                <c:forEach var="movie" items="${movies}" varStatus="i">
                    <div class="container content-container">
                        <div class="picture col-sm-2 col-sm-offset-2">
                            <img src="${pageContext.request.contextPath}/${movie.ref}" alt="">
                        </div>
                        <div class="col-sm-4 text-container">
                            <a href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${movie.id}">
                                <h2><c:out value="${movie.name}"></c:out></h2>
                            </a>
                            <h4 class="sub-text">
                                <fmt:message key="label.rating" bundle="${rb}"/>: ${movie.rating}
                            </h4>
                            <div class="justify"><c:out value="${movie.description}"></c:out></div>
                        </div>
                        <div class="col-sm-2 text-container" style="text-align: left;">
                            <input type="radio" class="radio" name="${movie.id}" id="id1_ + ${movie.id}" value="NONE" checked>
                            <label for="id1_ + ${movie.id}">None</label>
                            <input type="radio" class="radio" name="${movie.id}" id="id2_ + ${movie.id}" value="READ">
                            <label for="id2_ + ${movie.id}">Look through</label>
                            <input type="radio" class="radio" name="${movie.id}" id="id3_ + ${movie.id}" value="READ_WRITE">
                            <label for="id3_ + ${movie.id}">Edit</label>
                        </div>
                    </div>
                </c:forEach>
                <input type="submit" class="btn btn-success" value="Share">
            </form>
        </div>

    </div>
    <%@ include file="common/footer.jsp"%>
</div>
</body>
</html>
