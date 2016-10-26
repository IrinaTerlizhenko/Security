<div class="container content-container">
    <div class="picture col-sm-2 col-sm-offset-2">
        <img src="${pageContext.request.contextPath}/${movie.ref}" alt="">
    </div>
    <div class="col-sm-6 text-container">
        <a href="${pageContext.request.contextPath}/controller?command=show_movie&movie_id=${movie.id}">
            <h2><c:out value="${movie.name}"></c:out></h2>
        </a>
        <h4 class="sub-text">
            <fmt:message key="label.rating" bundle="${rb}"/>: ${movie.rating}
        </h4>
        <div class="justify"><c:out value="${movie.description}"></c:out></div>
    </div>
</div>
