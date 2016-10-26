<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title><fmt:message key="title.profile_edit" bundle="${rb}"/></title>
        <link rel='stylesheet' href="${pageContext.request.contextPath}/css/bootstrap.min.css" type='text/css' media='all'>
        <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <c:set var="page" value="path.page.profile_edit" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <div class="content">
                <form role="form" class="form-horizontal" method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" name="command" value="edit_profile"/>
                    <h3>
                        <fmt:message key="header.edit_profile" bundle="${rb}"/>
                    </h3>
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">
                            <fmt:message key="profile.name" bundle="${rb}"/>
                        </label>
                        <div class="col-sm-10">
                            <input id="name" type="text" name="name" class="form-control" minlength="1" maxlength="30"
                                   value="<c:out value="${user.name}"></c:out>"
                                   pattern="([A-Z][A-Za-z]*([ -][A-Z][A-Za-z]*)*)|([А-Я][А-Яа-я]*([ -][А-Я][А-Яа-я]*)*)"
                                   placeholder=<fmt:message key="placeholder.username" bundle="${rb}"/>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="surname" class="col-sm-2 control-label">
                            <fmt:message key="profile.surname" bundle="${rb}"/>
                        </label>
                        <div class="col-sm-10">
                            <input id="surname" type="text" name="surname" class="form-control" minlength="1" maxlength="30"
                                   value="<c:out value="${user.surname}"></c:out>"
                                   pattern="([A-Za-z]+([ -'][A-Za-z]+)*)|([А-Яа-я]+([ -'][А-Яа-я]+)*)"
                                   placeholder=<fmt:message key="placeholder.surname" bundle="${rb}"/>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">
                            <fmt:message key="profile.email" bundle="${rb}"/>
                        </label>
                        <div class="col-sm-10">
                            <input id="email" type="text" name="email" class="form-control" minlength="5" maxlength="40"
                                   value="<c:out value="${user.email}"></c:out>"
                                   pattern="(\w|\d)+@\w+\.\w+"
                                   placeholder=<fmt:message key="placeholder.email" bundle="${rb}"/>>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-2">
                            <img src="${pageContext.request.contextPath}/${user.photo}" alt="">
                        </div>
                        <div class="col-sm-10">
                            <span class="btn btn-success btn-file">
                                <i class="fa fa-plus"> </i>
                                <span>
                                    <fmt:message key="label.picture" bundle="${rb}"/>
                                </span>
                                <input id="picture" name="picture" type="file" accept="image/*,image/jpeg,image/png">
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="previous_password" class="col-sm-2 control-label">
                            <fmt:message key="profile.previous_password" bundle="${rb}"/>
                        </label>
                        <div class="col-sm-10">
                            <input id="previous_password" type="password" name="previous_password" class="form-control" minlength="3" maxlength="20"
                                   pattern="(\w|\d){3,20}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">
                            <fmt:message key="profile.new_password" bundle="${rb}"/>
                        </label>
                        <div class="col-sm-10">
                            <input id="password" type="password" name="password" class="form-control" minlength="3" maxlength="20"
                                   pattern="(\w|\d){3,20}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="repeat_password" class="col-sm-2 control-label">
                            <fmt:message key="profile.repeat_new_password" bundle="${rb}"/>
                        </label>
                        <div class="col-sm-10">
                            <input id="repeat_password" type="password" name="repeat_password" class="form-control" minlength="3" maxlength="20"
                                   pattern="(\w|\d){3,20}">
                        </div>
                    </div>
                    <div class="col-sm-offset-3">
                        <input class="btn btn-success" type="submit" value=<fmt:message key="button.edit_profile" bundle="${rb}"/>>
                        <a role="button" href="${pageContext.request.contextPath}/controller?command=profile&id=${user.id}" class="btn btn-default">
                            <fmt:message key="button.cancel" bundle="${rb}"/>
                        </a>
                    </div>
                </form>

                <div class="error">
                    <h3>
                        <c:if test="${error eq 'name_incorrect'}">
                            <fmt:message key="error.name_incorrect" bundle="${rb}"/>.
                            <br/>
                        </c:if>
                        <c:if test="${error eq 'surname_incorrect'}">
                            <fmt:message key="error.surname_incorrect" bundle="${rb}"/>.
                            <br/>
                        </c:if>
                        <c:if test="${error eq 'email_incorrect'}">
                            <fmt:message key="error.email_incorrect" bundle="${rb}"/>.
                            <br/>
                        </c:if>
                        <c:if test="${error eq 'pass_not_match'}">
                            <fmt:message key="error.pass_not_match" bundle="${rb}"/>.
                            <br/>
                        </c:if>
                    </h3>
                </div>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>

        <script type="text/javascript" src="${pageContext.request.contextPath}/js/cinemarating.js"></script>
    </body>
</html>
