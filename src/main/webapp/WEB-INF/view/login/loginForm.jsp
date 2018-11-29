<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title><spring:message code="login.title" /></title>
</head>
<body>
    <form:form modelAttribute="loginCommand">
    <form:errors />
        <p>
            <label><spring:message code="email" /></label>
            <form:input path="email" />
            <form:errors path="email" />
        </p>
        <p>
            <label><spring:message code="password" /></label>
            <form:password path="password" />
            <form:errors path="password" />
        </p>
        <input type="submit" value="<spring:message code="login.btn" />">
    </form:form>
</body>
</html>
