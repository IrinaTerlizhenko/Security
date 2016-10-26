<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title><fmt:message key="title.ban" bundle="${rb}"/></title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-datetimepicker.css">
        <link rel='stylesheet' href="${pageContext.request.contextPath}/css/bootstrap-glyphicons.css" type='text/css' media='all'>
    </head>
    <body>
        <c:set var="page" value="path.page.ban" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <form role="form" class="content form-horizontal" method="POST" action="${pageContext.request.contextPath}/controller">
                <input type="hidden" name="command" value="ban"/>
                <input type="hidden" name="id" value="<c:out value="${user.id}"></c:out>"/>
                <div class="form-group">
                    <label for="user" class="col-sm-2 control-label">
                        <fmt:message key="ban.user" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <input id="user" type="text" class="form-control" maxlength="30" value="<c:out value="${user.login}"></c:out>" disabled/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="type" class="col-sm-2 control-label">
                        <fmt:message key="ban.type" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <select id="type" name="type" class="form-control">
                            <option id="ABUSE" value="ABUSE">ABUSE</option>
                            <option id="SPAM" value="SPAM">SPAM</option>
                        </select>
                    </div>
                </div>
               <div class="form-group">
                    <label for="time" class="col-sm-2 control-label">
                        <fmt:message key="ban.time" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <div class='input-group date' id='datetimepicker1'>
                            <input id="time" type='text' name="expiration" class="form-control">
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="reason" class="col-sm-2 control-label">
                        <fmt:message key="ban.reason" bundle="${rb}"/>
                    </label>
                    <div class="col-sm-10">
                        <textarea id="reason" name="reason" class="form-control" maxlength="300"></textarea>
                    </div>
                </div>
                <div class="col-sm-offset-3">
                    <input type="submit" class="btn btn-warning" value=<fmt:message key="button.ban" bundle="${rb}"/>>
                    <a class="btn btn-default" href="${pageContext.request.contextPath}/controller?command=profile&id=${user.id}" role="button">
                        <fmt:message key="button.cancel" bundle="${rb}"/>
                    </a>
                </div>
            </form>
            <%@ include file="common/footer.jsp"%>
        </div>

        <script type="text/javascript" src="${pageContext.request.contextPath}/js/moment.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/cinemarating.js"></script>
    </body>
</html>
