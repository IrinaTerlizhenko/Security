<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/cinemarating.js"></script>
<div class="container">
    <div class="row">
        <div class="col-sm-1 col-sm-offset-2">
            <div class="thumbnail">
                <a href="${pageContext.request.contextPath}/controller?command=profile&id=${review.user.id}">
                    <img class="img-responsive user-photo" src="${pageContext.request.contextPath}/${review.user.photo}" alt="">
                </a>
            </div>
        </div>
        <div class="col-sm-7 review-main-info">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <a href="${pageContext.request.contextPath}/controller?command=profile&id=${review.user.id}">
                        <strong><c:out value="${review.user.login}"></c:out></strong>
                    </a>
                    <span class="text-muted"><fmt:message key="label.commented" bundle="${rb}"/> <c:out value="${review.time}"></c:out></span>
                    <c:if test="${user_id eq review.user.id or role eq 2}">
                        <a class="review-icon" data-toggle="modal" data-target="#delete-review-${review.user.id}" href="#">
                            <img src="${pageContext.request.contextPath}/img/delete.png" alt="delete">
                        </a>

                        <div id="delete-review-${review.user.id}" class="modal fade">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
                                        <h4 class="modal-title"><fmt:message key="label.review_deletion" bundle="${rb}"/></h4>
                                    </div>
                                    <div class="modal-body">
                                        <fmt:message key="label.review_deletion.question" bundle="${rb}"/>? <fmt:message key="label.deletion.warning" bundle="${rb}"/>.
                                    </div>
                                    <div class="modal-footer">
                                        <a class="btn btn-danger" href="${pageContext.request.contextPath}/controller?command=delete_review&movie_id=${review.mid}&user_id=${review.user.id}" role="button">
                                            <fmt:message key="button.delete" bundle="${rb}"/>
                                        </a>
                                        <button class="btn btn-default" type="button" data-dismiss="modal">
                                            <fmt:message key="button.close" bundle="${rb}"/>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <a class="review-icon" onclick="activateEdit(${review.user.id})" role="button">
                            <img src="${pageContext.request.contextPath}/img/edit.png" alt="edit">
                        </a>
                    </c:if>
                </div>
                <div class="panel-body">
                    <div id="review-text-${review.user.id}">
                        <c:out value="${review.text}"></c:out>
                    </div>
                    <form class="unvisible" id="review-edit-${review.user.id}" method="POST" action="${pageContext.request.contextPath}/controller">
                        <input type="hidden" name="command" value="edit_review">
                        <input type="hidden" name="user_id" value="${review.user.id}">
                        <input type="hidden" name="movie_id" value="${review.mid}">
                        <textarea name="review" class="form-control" maxlength="1000">${review.text}</textarea>
                        <input type="submit" class="btn btn-success" value=<fmt:message key="button.edit" bundle="${rb}"/>>
                        <a class="btn btn-default" onclick="deactivateEdit(${review.user.id})" role="button">
                            <fmt:message key="button.cancel" bundle="${rb}"/>
                        </a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
