<%@ taglib uri="customtags" prefix="ctg"%>
<%--<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js" charset="UTF-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<link rel='stylesheet' href="${pageContext.request.contextPath}/css/bootstrap.min.css" type='text/css' media='all'>
<link rel='stylesheet' href="${pageContext.request.contextPath}/css/style.css" type='text/css' media='all'>
<link rel='stylesheet' href="${pageContext.request.contextPath}/css/menu.css" type='text/css' media='all'>

<nav class="navbar navbar-default container">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only"><!--Toggle navigation--></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/controller?command=redirect&nextPage=path.page.main">
                <img class="brand-image" src="${pageContext.request.contextPath}/img/CR.png" alt=<fmt:message key="menu.home" bundle="${rb}"/>>
            </a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li href="#" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="menu.movies" bundle="${rb}"/>
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/controller?command=all_movies">
                            <fmt:message key="menu.movies.all" bundle="${rb}"/>
                        </a></li>
                        <li><a href="${pageContext.request.contextPath}/controller?command=top_movies">
                            <fmt:message key="menu.movies.top" bundle="${rb}"/>
                        </a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${pageContext.request.contextPath}/controller?command=redirect&nextPage=path.page.add_movie">
                            <fmt:message key="menu.movies.add_movie" bundle="${rb}"/>
                        </a></li>
                    </ul>
                </li>
                <ctg:admin>
                    <li><a href="${pageContext.request.contextPath}/controller?command=all_users">
                        <fmt:message key="menu.users" bundle="${rb}"/>
                    </a></li>
                </ctg:admin>
            </ul>
            <form class="navbar-form navbar-left" role="search">
                <input type="hidden" name="command" value="search"/>
                <div class="form-group">
                    <input name="query" type="text" class="form-control" placeholder=<fmt:message key="placeholder.search" bundle="${rb}"/>>
                </div>
                <button type="submit" class="btn btn-default"><fmt:message key="button.submit" bundle="${rb}"/></button>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="menu.profile" bundle="${rb}"/>
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/controller?command=profile&id=${user_id}">
                            <fmt:message key="menu.my_profile" bundle="${rb}"/>
                        </a></li>
                        <li><a href="${pageContext.request.contextPath}/controller?command=ratings">
                            <fmt:message key="menu.my_ratings" bundle="${rb}"/>
                        </a></li>
                        <li><a href="${pageContext.request.contextPath}/controller?command=reviews">
                            <fmt:message key="menu.my_reviews" bundle="${rb}"/>
                        </a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="menu.language" bundle="${rb}"/>
                        <span class="caret"></span>
                    </a>
                    <fmt:setBundle basename="resources.language" var="lang"/>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/controller?command=change_language&language=en">
                            <fmt:message key="language.english" bundle="${lang}"/>
                        </a></li>
                        <li><a href="${pageContext.request.contextPath}/controller?command=change_language&language=ru">
                            <fmt:message key="language.russian" bundle="${lang}"/>
                        </a></li>
                    </ul>
                    <fmt:setBundle basename="resources.pagecontent" var="rb"/>
                </li>
                <li><a href="${pageContext.request.contextPath}/controller?command=logout">
                    <fmt:message key="menu.logout" bundle="${rb}"/>
                </a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>