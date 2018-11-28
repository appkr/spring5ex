<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
    <h2>회원 정보 입력</h2>
    <form action="step3" method="post">
        <p>
            <label for="email">이메일</label>
            <input id="email" type="text" name="email" value="${registerRequest.email}">
        </p>
        <p>
            <label for="name">이름</label>
            <input id="name" type="text" name="name" value="${registerRequest.name}">
        </p>
        <p>
            <label for="name">비밀번호</label>
            <input id="password" type="password" name="password">
        </p>
        <p>
            <label for="name">비밀번호 확인</label>
            <input id="confirmPassword" type="password" name="confirmPassword">
        </p>
        <input type="submit" value="다음 단계">
    </form>
</body>
</html>
