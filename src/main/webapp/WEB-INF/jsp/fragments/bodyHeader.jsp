<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<nav class="navbar navbar-dark bg-dark py-0">
    <div class="container">
        <a href="meals" class="navbar-brand"><img src="resources/images/icon-meal.png"> <spring:message code="app.title"/></a>
        <sec:authorize access="isAuthenticated()">
            <form:form class="form-inline my-2" action="logout" method="post">
                <sec:authorize access="hasRole('ADMIN')">
                    <a class="btn btn-info mr-1" href="users"><spring:message code="user.title"/></a>
                </sec:authorize>
                <a class="btn btn-info mr-1" href="profile">${userTo.name} <spring:message code="app.profile"/></a>
                <button class="btn btn-primary my-1" type="submit">
                    <span class="fa fa-sign-out"></span>
                </button>
            </form:form>
        </sec:authorize>
        <sec:authorize access="isAnonymous()">
            <form:form class="form-inline my-2" id="login_form" action="spring_security_check" method="post">
                <input class="form-control mr-1" type="text" placeholder="Email" name="username">
                <input class="form-control mr-1" type="password" placeholder="Password" name="password">
                <button class="btn btn-success" type="submit">
                    <span class="fa fa-sign-in"></span>
                </button>
            </form:form>
        </sec:authorize>
        <div class="nav-item dropdown">
            <a class="dropdown-toggle nav-link my-1 ml-2" data-toggle="dropdown">${empty param.lang ? "ru" : param.lang}</a>
            <div class="dropdown-menu">
                <c:set var="servletPath" value="${pageContext.request.getServletPath()}"/>
                <c:set var="servletName" value="${servletPath.substring(servletPath.lastIndexOf('/') + 1, servletPath.indexOf('.'))}"/>
                <a class="dropdown-item" href="${pageContext.request.contextPath}/${servletName}?lang=en">English</a>
                <a class="dropdown-item" href="${pageContext.request.contextPath}/${servletName}?lang=ru">Русский</a>
            </div>
        </div>
    </div>
</nav>
