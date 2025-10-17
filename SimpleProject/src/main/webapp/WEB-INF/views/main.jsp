<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>환영합니다~</title>
</head>
<body>

	<%-- header.jsp 포함하기, main.jsp에서 header.jsp를 만나려면?
	include 폴더로 들어감, 들어간다는 뜻은 슬래시(/) --%>
	<jsp:include page="include/header.jsp" />
	
	<%-- 보기좋게 간격 만들어줄 div 추가 --%>
	<div style="width: 1200px; height: 600px;"></div>
	
	<%-- footer.jsp 포함하기 --%>
	<jsp:include page="include/footer.jsp" />

</body>
</html>