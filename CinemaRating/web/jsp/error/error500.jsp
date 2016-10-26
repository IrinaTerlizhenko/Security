<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />
<html>
    <head>
        <title><fmt:message key="title.error-page" bundle="${rb}"/></title>
        <link rel='stylesheet' href="${pageContext.request.contextPath}/css/bootstrap.min.css" type='text/css' media='all'>
        <link rel='stylesheet' href="${pageContext.request.contextPath}/css/error.css" type='text/css' media='all'>
    </head>
    <body class="container">
        <div class="col-sm-6">
            <img src="https://http.cat/500" alt="">
        </div>
        <div class="col-sm-6">
            <fmt:message key="label.request-from" bundle="${rb}"/> ${pageContext.errorData.requestURI} <fmt:message key="label.failed" bundle="${rb}"/>.
            <br/>
            <fmt:message key="label.servlet-name-or-type" bundle="${rb}"/>: ${pageContext.errorData.servletName}
            <br/>
            <fmt:message key="label.status-code" bundle="${rb}"/>: ${pageContext.errorData.statusCode}
            <br/>
            <fmt:message key="label.exception" bundle="${rb}"/>: ${pageContext.errorData.throwable}
            <br/>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/controller?command=redirect&nextPage=path.page.login" role="button">
                <fmt:message key="label.to_login" bundle="${rb}"/>
            </a>
        </div>
    </body>
</html>