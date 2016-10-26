<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="customtags" prefix="ctg"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />

<html>
    <head>
        <title>
            <fmt:message key="title.all_users" bundle="${rb}"/>
        </title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/cinemarating.js"></script>
    </head>
    <body>
        <c:set var="page" value="path.page.users" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <div class="content text-center">
                <div id="num_page" data-num_page="${num_page}"></div>

                <h1>
                    <fmt:message key="header.all_users" bundle="${rb}"/>
                </h1>

                <ctg:pagination upper="true" size="${fn:length(users)}"/>

                <c:forEach var="user" items="${users}" begin="${num_page * 10}" end="${num_page * 10 + 9}">
                    <%@ include file="common/short_user.jsp"%>
                </c:forEach>

                <ctg:pagination upper="false" size="${fn:length(users)}"/>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>
    </body>
</html>
