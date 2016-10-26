<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <link rel='stylesheet' href="${pageContext.request.contextPath}/css/bootstrap.min.css" type='text/css' media='all'>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <title><fmt:message key="title.register" bundle="${rb}"/></title>
    </head>
    <body>
        <form method="POST" action="${pageContext.request.contextPath}/loginController" class="form-horizontal login-form">
            <input type="hidden" name="command" value="register"/>
            <div class="form-group">
                <label for="login" class="control-label compulsory">
                    <fmt:message key="label.login" bundle="${rb}"/>
                </label>
                <input id="login" type="text" name="login" class="form-control" required minlength="1" maxlength="30"
                       value="<c:out value="${login}"></c:out>"
                       pattern="(\w|\d){1,30}"
                       placeholder=<fmt:message key="placeholder.login" bundle="${rb}"/>>
            </div>
            <div class="form-group">
                <label for="password" class="control-label compulsory">
                    <fmt:message key="label.password" bundle="${rb}"/>
                </label>
                <input id="password" type="password" name="password" class="form-control" required minlength="1" maxlength="20"
                       pattern="(\w|\d){3,20}">
            </div>
            <div class="form-group">
                <label for="repeat-password" class="control-label compulsory">
                    <fmt:message key="label.repeat-password" bundle="${rb}"/>
                </label>
                <input id="repeat-password" type="password" name="repeat_password" class="form-control" required minlength="1" maxlength="20"
                       pattern="(\w|\d){3,20}">
            </div>
            <div class="form-group">
                <label for="email" class="control-label compulsory">
                    <fmt:message key="label.email" bundle="${rb}"/>
                </label>
                <input id="email" type="email" name="email" class="form-control" required minlength="5" maxlength="40"
                       value="<c:out value="${email}"></c:out>"
                       pattern="(\w|\d)+@\w+\.\w+"
                       placeholder=<fmt:message key="placeholder.email" bundle="${rb}"/>> <!-- todo pattern=".{5,40}" -->
            </div>
            <div class="form-group">
                <label for="pin" class="control-label compulsory">
                    PIN
                </label>
                <input id="pin" type="password" name="pin" class="form-control" required>
            </div>
            <span><c:out value="${errorLoginPassMessage}"></c:out></span>
            <br/>
            <span><c:out value="${wrongAction}"></c:out></span>
            <br/>
            <span><c:out value="${nullPage}"></c:out></span>
            <br/>
            <input class="btn btn-primary btn-block" type="submit" value=<fmt:message key="button.register" bundle="${rb}"/>>
            <a class="btn btn-default btn-block" href="${pageContext.request.contextPath}/controller?command=redirect&nextPage=path.page.login" role="button>">
                <fmt:message key="label.login" bundle="${rb}"/>
            </a>
        </form>
    </body>
</html>