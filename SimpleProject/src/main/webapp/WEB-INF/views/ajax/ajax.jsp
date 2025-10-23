<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h1>AJAX</h1>
	
	<jsp:include page="../include/header.jsp" />
	
	<pre>
		웹페이지 전체를 새로고침하지 않고
		서버와 비동기 통신을 하여 화면을 갱신할 수 있는 기술 패턴
		
		* 핵심!! => 비동기통신 => 부분갱신 => 사용자 경험 향상 => 트래픽줄어듬
		이거해서 뭘하고싶냐가 필요하지
		데이터를 받아와서 화면에 출력하는건데 JS를 이용하니까 DOM을 다루는데 요소 전부를 고치는게 아니라 특정 DOM요소만 갱신
		목적이 있어야 기술을 선택해서 사용
		트래픽은 html 전체 대신 바꿀부분만 주고받으니 줄어든다, 당연히 속도도 빨라짐
		
		--------------------------------------------------여기까진 필수
		
		전송 데이터 형식 => 과거에는 XML 사용 => 현재 JSON (JavaScriptObjectNotation, 자바스크립트 객체 표현법)
		문자열을 만드는데 자바스크립트 객체 모양으로 만든다, 문법 쓸 때 쌍따옴표 쓰기 조심, 속성과 값 둘 다 달고(숫자는 안달아도된다), 날짜형식이 없는거 조심
		날짜 다루고 싶으면 문자열로 다루어야한다
		
		AJAX 구현 API == XMLHttpRequest (이름만 봐도 XML 시절에 쓰던거구만~) => 현재는 modern Fetch API(이건 바닐라자바스크립트 쓸때이야기)
																	=> ajax() => jQuery
																	=> axios() => React
		실제로 네이버나 kh보면 ajax로 jQery쓰는게 많다, 오래 써서.. 스프링할때까지만 이렇게 하고
		리액트 넘어가서는 jQuery 버리고 순수자바스크립트(바닐라)로 할거임
		
		--------------------------------------------------
		전체적인 흐름
		
		1. 클라이언트가 요청 보냄(JS로 보냄, form 태그 이런거 아님)
		2. 서버는 요청 처리 후 데이터 응답(동기식 요청의 html 응답 아님, 문자열 => JSON형태로)
		아이디 중복조회, 댓글작성, 댓글조회 어떻게 했는지? 서버가 어떻게 데이터 응답함? 아이디 중복조회 NNNNN, NNNNY / 댓글조회 [{"replyNo" : 1, "replyWriter" : "홍길동", "replyWriter" : "이거 기억안나세요???"}]
		3. 클라이언트는 응답받은 데이터로 자바스크립트 DOM요소객체를 갱신(update)
	</pre>
	
	<jsp:include page="../include/footer.jsp" />

</body>
</html>