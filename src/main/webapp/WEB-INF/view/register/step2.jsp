<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><spring:message code="member.register"/></title>
</head>
<body>
    <h2><spring:message code="member.info"/></h2>
    <form action="step3" method="post">
        <p>
            <label for="email"><spring:message code="email"/></label>
            <input id="email" type="text" name="email" value="${registerRequest.email}">
        </p>
        <p>
            <label for="name"><spring:message code="name"/></label>
            <input id="name" type="text" name="name" value="${registerRequest.name}">
        </p>
        <p>
            <label for="name"><spring:message code="password"/></label>
            <input id="password" type="password" name="password">
        </p>
        <p>
            <label for="name"><spring:message code="password.confirm"/></label>
            <input id="confirmPassword" type="password" name="confirmPassword">
        </p>
        <input type="submit" value="<spring:message code="register.btn"/>">
    </form>
</body>
</html>
