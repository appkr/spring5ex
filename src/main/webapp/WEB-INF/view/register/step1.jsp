<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
    <h2>약관</h2>
    <p>약관 내용</p>
    <form action="step2" method="post">
        <label for="agree">
            <input id="agree" type="checkbox" name="agree" value="true">
            약관동의
        </label>
        <input type="submit" value="다음 단계">
    </form>
</body>
</html>
