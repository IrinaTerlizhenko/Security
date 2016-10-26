<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<!-- todo deprecated footer -->

<html>
    <head>
        <title><fmt:message key="title.rating_review" bundle="${rb}"/></title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <c:set var="page" value="path.page.rating_review" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <form role="form" name="rating-form" class="rating-form form-horizontal container" action="${pageContext.request.contextPath}/controller">
            <input type="hidden" name="command" value="rate"/>
            <input type="hidden" name="movie_id" value="${movie_id}"/>
            <h3>
                <fmt:message key="header.rating_review" bundle="${rb}"/>
            </h3>
            <div class="form-group">
                <label for="rating" class="control-label">
                    <fmt:message key="label.rating" bundle="${rb}"/>:
                </label>
                <input id="rating" name="rating" class="form-control" type="number" size="2" name="rating" min="0" max="10" value="${rating}" required>
            </div>
            <div class="form-group">
                <c:choose>
                    <c:when test="${banned == false}">
                        <label for="review" class="control-label">
                            <fmt:message key="label.review" bundle="${rb}"/>:
                        </label>
                        <textarea id="review" name="review" class="form-control" maxlength="1000"
                                  placeholder=<fmt:message key="placeholder.review" bundle="${rb}"/>>${review}</textarea>
                    </c:when>
                    <c:otherwise>
                        <label>
                            <fmt:message key="label.review-banned" bundle="${rb}"/>.
                        </label>
                    </c:otherwise>
                </c:choose>
            </div>
            <br/>
            <input class="btn btn-success" type="submit" value=<fmt:message key="button.rate" bundle="${rb}"/>>
            <a role="button" href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${movie_id}" class="btn btn-default">
                <fmt:message key="button.cancel" bundle="${rb}"/>
            </a>
        </form>
    </body>
</html>