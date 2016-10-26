<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="customtags" prefix="ctg"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title><fmt:message key="title.my_review" bundle="${rb}"/></title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/cinemarating.js"></script>
    </head>
    <body>
        <c:set var="page" value="path.page.reviews" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <div class="content text-center">
                <div id="num_page" data-num_page="${num_page}"></div>

                <h1>
                    <fmt:message key="title.my_review" bundle="${rb}"/>
                </h1>

                <c:choose>
                    <c:when test="${fn:length(review_map) > 0}">
                        <ctg:pagination upper="true" size="${fn:length(review_map)}"/>

                        <c:forEach var="entry" items="${review_map}" begin="${num_page * 10}" end="${num_page * 10 + 9}">
                            <div class="container">
                                <div class="picture col-sm-2 col-sm-offset-2">
                                    <img src="${pageContext.request.contextPath}/${entry.key.ref}" alt="">
                                </div>
                                <div class="col-sm-6 text-container">
                                    <a href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${entry.key.id}">
                                        <h2><c:out value="${entry.key.name}"></c:out></h2>
                                    </a>
                                    <h4 class="sub-text">
                                        <fmt:message key="label.rating" bundle="${rb}"/>: <c:out value="${entry.key.rating}"></c:out>
                                    </h4>
                                    <div class="justify"><c:out value="${entry.key.description}"></c:out></div>
                                </div>
                            </div>
                            <c:set var="review" value="${entry.value}" scope="request"/>
                            <%@ include file="common/review.jsp"%>
                        </c:forEach>

                        <ctg:pagination upper="false" size="${fn:length(review_map)}"/>
                    </c:when>
                    <c:otherwise>
                        <h3>
                            <fmt:message key="label.no_reviews" bundle="${rb}"/>.
                        </h3>
                    </c:otherwise>
                </c:choose>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>
    </body>
</html>
