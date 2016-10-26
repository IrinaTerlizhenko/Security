<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title>
            <fmt:message key="title.edit_movie" bundle="${rb}"/>
        </title>
        <link rel='stylesheet' href="${pageContext.request.contextPath}/css/style.css" type='text/css'>
    </head>
    <body>
        <c:set var="page" value="path.page.edit_movie" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <form role="form" class="content form-horizontal" method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/controller">
                <input type="hidden" name="command" value="edit_movie"/>
                <input type="hidden" name="movie_id" value="${movie.id}"/>
                <h3>
                    <fmt:message key="label.edit_movie" bundle="${rb}"/>
                </h3>
                <div class="form-group">
                    <label for="movie_name" class="col-sm-2 compulsory control-label">
                        <fmt:message key="label.movie_name" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <input id="movie_name" name="movie_name" class="form-control" type="text" required minlength="1" maxlength="50"
                               value="<c:out value="${movie.name}"></c:out>"
                               pattern=".{1,50}"
                               placeholder=<fmt:message key="label.movie_name" bundle="${rb}"/>>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 compulsory control-label">
                        <fmt:message key="label.description" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <textarea id="description" name="description" class="form-control" required maxlength="1000"
                                  placeholder=<fmt:message key="label.description" bundle="${rb}"/>><c:out value="${movie.description}"></c:out></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label for="year" class="col-sm-2 compulsory control-label">
                        <fmt:message key="label.year" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <input id="year" name="year" class="form-control" type="number" required min="1888"
                               value="<c:out value="${movie.year}"></c:out>"
                               pattern="[1-9]\d{3}"
                               placeholder=<fmt:message key="label.year" bundle="${rb}"/>>
                    </div>
                </div>
                <div class="form-group">
                    <label for="country" class="col-sm-2 compulsory control-label">
                        <fmt:message key="label.country" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <input id="country" name="country" class="form-control" type="text" required minlength="1" maxlength="100"
                               value="<c:out value="${movie.country}"></c:out>"
                               pattern="([A-Z][A-Za-z]*(, [A-Z][A-Za-z]*)*)|([А-Я][А-Яа-я]*(, [А-Я][А-Яа-я]*)*)"
                               placeholder=<fmt:message key="label.country" bundle="${rb}"/>>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-2">
                        <img src="${pageContext.request.contextPath}/${movie.ref}" alt="no picture">
                    </div>
                    <div class="col-sm-10">
                        <span class="btn btn-success btn-file">
                            <i class="fa fa-plus"> </i>
                            <span>
                                <fmt:message key="label.picture" bundle="${rb}"/>
                            </span>
                            <input id="picture" name="picture" type="file" accept="image/*,image/jpeg,image/png,image/bmp,image/jpg">
                        </span>
                    </div>
                </div>
                <div class="col-sm-offset-3">
                    <input type="submit" class="btn btn-success" value=<fmt:message key="button.edit_movie" bundle="${rb}"/>>
                    <a role="button" href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${movie.id}" class="btn btn-default">
                        <fmt:message key="button.cancel" bundle="${rb}"/>
                    </a>
                </div>
            </form>
            <%@ include file="common/footer.jsp"%>
        </div>
    </body>
</html>
