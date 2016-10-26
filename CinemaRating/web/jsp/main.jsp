<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="customtags" prefix="ctg"%>

<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />
<html>
    <head>
        <title><fmt:message key="title.welcome" bundle="${rb}"/></title>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/slider.js"></script>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/slider.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <c:set var="page" value="path.page.main" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <div class="content container">
                <img src="${pageContext.request.contextPath}/img/CinemaRating_large.png">

                <div class="col-sm-4">
                    <h2><fmt:message key="header.latest5movies" bundle="${rb}"/></h2>
                    <div class="slider">
                        <ul>
                            <c:forEach var="movie" items="${latest_movies}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${movie.id}">
                                        <img src="${pageContext.request.contextPath}/${movie.ref}" alt="">
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <div class="col-sm-4">
                    <h2><fmt:message key="header.top5movies" bundle="${rb}"/></h2>
                    <div class="slider">
                        <ul>
                            <c:forEach var="movie" items="${top_movies}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${movie.id}">
                                        <img src="${pageContext.request.contextPath}/${movie.ref}" alt="">
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <div class="col-sm-4">
                    <h2><fmt:message key="header.latest3reviews" bundle="${rb}"/></h2>
                    <c:forEach var="review" items="${reviews}" varStatus="status">
                        <div class="maxwidth">
                            <div class="row">
                                <div class="col-sm-3">
                                    <div class="thumbnail">
                                        <a href="${pageContext.request.contextPath}/controller?command=profile&id=${review.user.id}">
                                            <img class="img-responsive user-photo" src="${pageContext.request.contextPath}/${review.user.photo}" alt="">
                                        </a>
                                    </div>
                                </div>
                                <div class="col-sm-9 review-main-info">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <a href="${pageContext.request.contextPath}/controller?command=profile&id=${review.user.id}">
                                                <strong><c:out value="${review.user.login}"></c:out></strong>
                                            </a>
                                            <span class="text-muted"><fmt:message key="label.commented_on_movie" bundle="${rb}"/>
                                                <a href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${review.mid}">
                                                    <strong><c:out value="${movies[status.index]}"></c:out></strong>
                                                </a> <c:out value="${review.time}"></c:out>
                                            </span>
                                        </div>
                                        <div class="panel-body">
                                            <div id="review-text-${review.user.id}">
                                                <c:out value="${review.text}"></c:out>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>
    </body>
</html>