<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%-- index.jsp에 오면 main.jsp를 띄우고 싶음
	방법 2개
	첫번째는 스크립틀릿으로 서블릿으로 요청, location.href로 포워딩
	두번째는 태그써서 <jsp:forward page=””></jsp:forward>로 포워딩 --%>
	<jsp:forward page="WEB-INF/views/main.jsp" />
	<h1>하이하이</h1>

</body>
</html>