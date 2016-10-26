<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="customtags" prefix="ctg"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.pagecontent" var="rb" />
<html>
    <head>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <title><fmt:message key="title.profile" bundle="${rb}"/></title>
    </head>
    <body>
        <c:set var="page" value="path.page.profile" scope="session"/>
        <%@ include file="common/menu.jsp"%>

        <div class="wrapper container">
            <div class="content container">
                <div class="picture col-sm-3 col-sm-offset-2">
                    <a data-toggle="modal" data-target="#full-picture">
                        <img src="${pageContext.request.contextPath}/${user.photo}" alt="">
                    </a>

                    <div id="full-picture" class="modal fade" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
                                    <h4 class="modal-title"><c:out value="${user.login}"></c:out></h4>
                                </div>
                                <div class="modal-body">
                                    <img src="${pageContext.request.contextPath}/${user.photo}" alt="">
                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-default" type="button" data-dismiss="modal">
                                        <fmt:message key="button.close" bundle="${rb}"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${user_id eq user.id}">
                            <a class="btn btn-default btn-block" href="${pageContext.request.contextPath}/controller?command=init_edit_profile" role="button">
                                <div>
                                    <fmt:message key="profile.edit" bundle="${rb}"/>
                                </div>
                            </a>

                            <a class="btn btn-default btn-block" data-toggle="modal" data-target="#delete-profile">
                                <fmt:message key="profile.delete" bundle="${rb}"/>
                            </a>

                            <div id="delete-profile" class="modal fade">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
                                            <h4 class="modal-title"><fmt:message key="label.profile_deletion" bundle="${rb}"/></h4>
                                        </div>
                                        <div class="modal-body">
                                            <fmt:message key="label.profile_deletion.question" bundle="${rb}"/>? <fmt:message key="label.deletion.warning" bundle="${rb}"/>.
                                        </div>
                                        <div class="modal-footer">
                                            <a class="btn btn-danger" href="${pageContext.request.contextPath}/controller?command=delete_profile" role="button">
                                                <fmt:message key="button.delete" bundle="${rb}"/>
                                            </a>
                                            <button class="btn btn-default" type="button" data-dismiss="modal">
                                                <fmt:message key="button.close" bundle="${rb}"/>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <ctg:admin>
                                <a class="btn btn-default btn-block" href="${pageContext.request.contextPath}/controller?command=init_ban&id=${user.id}" role="button">
                                    <div>
                                        <fmt:message key="profile.ban" bundle="${rb}"/>
                                    </div>
                                </a>
                            </ctg:admin>
                            <a class="btn btn-default btn-block" href="${pageContext.request.contextPath}/controller?command=init_share_movies&id=${user.id}" role="button">
                                <div>
                                    Share movies
                                </div>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-sm-5 text-container">
                    <h2><c:out value="${user.login}"></c:out></h2>
                    <h4 class="sub-text">
                        <ctg:admin>
                            <a class="col-sm-1" onclick="activateEditStatus()">
                                <img class="control" src="${pageContext.request.contextPath}/img/edit_status.png" alt="edit">
                            </a>
                        </ctg:admin>
                        <div class="col-sm-2">
                            <fmt:message key="label.status" bundle="${rb}"/>:
                        </div>
                        <div id="status" class="col-sm-9">
                            <c:out value="${user.status}"></c:out>
                        </div>
                        <form id="edit-status" class="col-sm-9 unvisible form-inline" method="POST" action="${pageContext.request.contextPath}/controller">
                            <input type="hidden" name="command" value="edit_status">
                            <input type="hidden" name="user_id" value="${user.id}">
                            <div class="form-group">
                                <input class="form-control inline-control" type="number" name="status" min="0" max="10"
                                       value="<c:out value="${user.status}"></c:out>">
                            </div>
                            <input class="btn btn-success" type="submit" value=<fmt:message key="button.edit" bundle="${rb}"/>>
                            <a role="button" href="#" class="btn btn-default" onclick="deactivateEditStatus()">
                                <fmt:message key="button.cancel" bundle="${rb}"/>
                            </a>
                        </form>
                        <br class="skip"/>
                        <fmt:message key="label.num_rated" bundle="${rb}"/>: <c:out value="${user.numRated}"></c:out>
                    </h4>
                    <table class="info table-hover">
                        <tbody>
                            <tr>
                                <td class="type">
                                    <fmt:message key="profile.login" bundle="${rb}"/>
                                </td>
                                <td><c:out value="${user.login}"></c:out></td>
                            </tr>
                            <tr>
                                <td class="type">
                                    <fmt:message key="profile.name" bundle="${rb}"/>
                                </td>
                                <td><c:out value="${user.name}"></c:out></td>
                            </tr>
                            <tr>
                                <td class="type">
                                    <fmt:message key="profile.surname" bundle="${rb}"/>
                                </td>
                                <td><c:out value="${user.surname}"></c:out></td>
                            </tr>
                            <tr>
                                <td class="type">
                                    <fmt:message key="profile.email" bundle="${rb}"/>
                                </td>
                                <td><c:out value="${user.email}"></c:out></td>
                            </tr>
                            <tr>
                                <td class="type">
                                    <fmt:message key="profile.reg_date" bundle="${rb}"/>
                                </td>
                                <td><c:out value="${user.regDate}"></c:out></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <%@ include file="common/footer.jsp"%>
        </div>

        <script type="text/javascript" src="${pageContext.request.contextPath}/js/cinemarating.js"></script>
    </body>
</html>