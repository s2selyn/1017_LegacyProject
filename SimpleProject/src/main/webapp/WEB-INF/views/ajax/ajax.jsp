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
	
	<div class="innerOuter">
	<pre>
		웹페이지 전체를 새로고침하지 않고
		서버와 비동기 통신을 하여 화면을 갱신할 수 있는 기술 패턴
		
		* 핵심!! => 비동기통신 => 부분갱신 => 사용자 경험 향상 => 트래픽줄어듬
		이거해서 뭘하고싶냐가 필요하지, 목적
		데이터를 받아와서 화면에 출력하는건데 JS를 이용하니까 DOM을 다루는데 요소 전부를 고치는게 아니라 특정 DOM요소만 갱신
		목적이 있어야 기술을 선택해서 사용
		트래픽은 html 전체 대신 바꿀부분 데이터만 주고받으니 줄어든다, 통신비용이 줄어들고 당연히 속도도 빨라짐
		
		-------------------------------------------------- 여기까진 필수
		
		전송 데이터 형식 => 과거에는 XML 사용 => 현재 JSON (JavaScriptObjectNotation, 자바스크립트 객체 표현법)
		문자열을 만드는데 자바스크립트 객체 모양으로 만든다, 진짜 객체 아닌거 조심
		문법 쓸 때 쌍따옴표 쓰기 조심, 속성과 값 둘 다 달고(숫자는 안달아도된다), 일반 문자열이니 날짜형식이 없는거 조심(날짜 다루고 싶으면 문자열로 다루어야한다)
		
		AJAX 구현 API == XMLHttpRequest (이름만 봐도 XML 시절에 쓰던거구만~) => 현재는 modern Fetch API(이건 바닐라자바스크립트 쓸때이야기)
																	=> ajax() => jQuery(jQuery 쓴다면 ajax 매소드 써야한다는 뜻)
																	=> axios() => React(axios라는 라이브러리를 쓰는것)
																	
		실제로 네이버나 kh보면 ajax 요청보내는거로 jQery쓰는게 많다, 오래 써서.. 스프링할때까지만 이렇게 하고
		리액트 넘어가서는 jQuery 버리고 순수자바스크립트(바닐라)로 할거임
		
		--------------------------------------------------
		
		전체적인 흐름
		
		1. 클라이언트가 요청 보냄(JS로 보냄, form 태그 이런거 아님)
		2. 서버는 요청 처리 후 데이터 응답(동기식 요청의 html 응답 아님, 문자열 => JSON형태로, 사실 html도 문자열이긴해)
		아이디 중복조회, 댓글작성, 댓글조회 어떻게 했는지? 서버가 어떻게 데이터 응답함? 문자열 => 아이디 중복조회 NNNNN, NNNNY / 댓글조회 [{"replyNo" : 1, "replyWriter" : "홍길동", "replyContent" : "기억안나세요???"}]
		3. 클라이언트는 응답받은 데이터로 자바스크립트 DOM요소객체를 갱신(update)
		
		- jQuery로 ajax요청 시 주요 속성
		
		- url : 요청할 URL(필수)
		- data : 요청 시 전달값({키 : 밸류})
		- type : 요청 전송방식(GET/POST/PUT/DELETE)
				 GET방식 : 조회요청(SELECT)
				 POST방식 : 데이터 생성 요청(INSERT)
				 PUT방식 : 데이터 갱신 요청(UPDATE)
				 DELETE방식 : 데이터 삭제 요청(DELETE)
		(method는 form 태그에서 쓰는 속성명임)
		(ajax로 네가지 요청방식 다 보낼 수 있으니까 어제 설명했던 get, post, put, delete mapping 다 사용할 수 있음)
		- success : AJAX 통신 성공 시 실행할 함수를 정의
		
		컨트롤러가 바뀔것이다
	</pre>
	
	<h3>1. 버튼 클릭 해서 GET방식으로 요청 보내서 데이터 받아서 화면에 출력!</h3>
	
	<div class="form-group">
		<div class="form-control">
			입력 : <input type="text" id="ajax-input">
		</div>
		<div class="form-control">
			<button class="btn btn-sm btn-success"
			id="ajax-btn" onclick="test1();">AJAX로 요청보내기</button>
		</div>
	</div>
	
	응답 : <label id="result">현재 응답 없음</label>
	
	<%-- 계획 :
		인풋요소에 아무거나 쓰고 요청보내기 버튼을 누르면
		AJAX요청을 보내서
		요청을 받아서 처리해주는 RequestHandler가 값을 받아서 응답해주고
		받은 응답데이터를 라벨요소 Content영역에 출력할 것
	--%>
	
	<script>
		// 버튼을 클릭하면 ajax 요청을 보내게 만들거니까 script 요소 작성해야함, 버튼에 onclick 속성도 추가해서 test1 호출하게 함
		function test1() {
			
			// jQuery로 ajax 요청 보내려면 달러사인 참조해서 ajax 메소드호출(콜)
			$.ajax({
			// 요청시 보낼값이 많으니 객체로 전달한다
			
				url : "test", // 꼭!	
				type : "get",
				data : {
					"input" : $("#ajax-input").val() // 입력값도 보내보자
				},
				success : function(response) { // 서버에서 보내주는 응답데이터 받아서 찍어볼거니까 매개변수 전달, 성공 시 실행된다
					// 자바스크립트는 변수명만 쓰면 됨
					
					console.log(response);
					// 브라우저 테스트하면 요청 url 우리가 적은 test로 가고, get 방식이니까 요청 시 전달값이 뒤에 쿼리스트링으로 붙어서 간다
					// 핸들러 없어서 404, 컨트롤러에 처리할 핸들러 추가해야하는데 아직 없으니 AjaxController 클래스생성하고 작업
					
					// 컨트롤러 작업했음, 응답을 컨텐트 영역에 출력해줄것이다 -> 요소에 접근 먼저(바닐라자바스크립트 사용)
					document.getElementById("result").innerHTML = response;
					
				}
				
			});
			
		}
	
	</script>
	
	<hr>
	
	<h3>2. 댓글 작성하기</h3>
	
	<%--
		계획 : 글번호를 입력받고, 댓글 내용을 입력받은 뒤
			  버튼을 클릭하면 AJAX요청을 보내 Reply테이블에 한 행 INSERT
			  이거 하려면 writer가 필요한데 여기서 안받고 세션으로 뒷단에서 검증하자
			  앞에서는 input요소만 두개해서 받아보자
		
		ajax이므로 폼태그가 필요없다
	--%>
	
	<div class="form-group">
		<div class="form-control">
			글 번호 : <input type="text" id="num" />
		</div>
		<div class="form-control">
			댓글 내용 : <input type="text" id="reply-content" />
		</div>
		<div class="form-control">
			<button onclick="insert();" class="btn btn-sm btn-info">댓글 작성하기</button>
		</div>
	</div>
	
	<script>
		function insert() {
			
			// 미리 value 뽑아내자
			const boardNo = document.getElementById("num").value;
			const replyContent = document.getElementById("reply-content").value;
			
			// ajax 요청 보내기
			$.ajax({
				
				url : "replies",
				type : "post", // insert 할거니까 POST방식 전송
				data : {
					
					refBno : boardNo,
					replyContent : replyContent
					// 이렇게 전달하려고 하는데 컨트롤러에서 요청시의 전달값을 빼서 써야한다
					// 폼태그로 했다면 어떻게 받았을까? reply에 한 행 insert 하는거니까? 이번엔 거꾸로 해볼까? board-mapper.xml에 작업
					// boardNo -> refBno로 필드명으로 변경
					
				},
				success : function(response) {
					console.log(response);
					
					if(response === "success") {
						alert("댓글 작성 성공 ~");
					} else {
						alert("댓글 작성 실패 메롱");
					}
					
					// 성공하든 실패하든 input요소를 비워주자
					document.getElementById("num").value = "";
					document.getElementById("reply-content").value = "";
					
				}
				
			});
			
		}
	</script>
	
	<hr>
	
	<h4>자 재밌는거 해보겠습니다. AJAX요청으로 게시글 상세조회 해보기</h4>
	
	<%-- 오전에 했던거 로직 짜놓은것과 동일 --%>
	<div>
		<h3>게시글 자세히보기</h3>
		
		제목 : <p id="title"></p>
		작성자 : <p id="writer"></p>
		내용 : <p id="content"></p>
		작성일 : <p id="date"></p>
		<hr>
		<img id="board-img" />
		<hr>
		<div id="reply-area">
		
		</div>
	</div>
	<br>
	게시글 번호를 입력하세요 : <input type="text" id="detail" />
	<button onclick="detail();">게시글 보여주세용~</button>
	
	<script>
		function detail() {
			
			const num = document.getElementById("detail").value;
			$.ajax({
				
				url : `board/\${num}`,
				type : "get",
				success : result => {
					
					console.log(result);
					// 응답 데이터를 화면에 출력, 이거 필드명으로 돌아온다
					document.querySelector("#title").innerText = result.boardTitle;
					document.querySelector("#writer").innerText = result.boardWriter;
					document.querySelector("#content").innerText = result.boardContent;
					document.querySelector("#date").innerText = result.createDate;
					
					// 첨부파일은 있을수도 있고 없을수도 있음
					const imgEl = document.querySelector("#board-img");
					imgEl.src = result.changeName != undefined ? result.changeName : "";
					
					// 댓글도 보여줄것임
					const replies = result.replies;
					
					const el = replies.map(e => {
						
						return (
								`<div>
									<label style="330px">댓글 작성자 : \${e.replyWriter}</label>
									<label style="400px">댓글 내용 : \${e.replyContent}</label>
									<label style="150px">작성일 : \${e.createDate}</label>
								</div>`
						)
						
					}).join("");
					
					document.querySelector("#reply-area").innerHTML = el;
					
				}
				
			});
			
		}
	</script>
	
	<hr>
	
	<button class="btn btn-lg btn-primary" onclick="btn1();">버튼1</button>
	<br>
	<button class="btn btn-lg btn-primary" onclick="btn2();">버튼2</button>
	<script>
		function btn1() {
			
			$.ajax({
				
				url : "revol/c",
				type : "post",
				success : res => {
					// alert(res.boardTitle);
					console.log(res);
				}
				
			})
			
		}
		
		function btn2() {
			
			$.ajax({
				
				url : "revol/d",
				success : res => {
					// alert(res.boardTitle);
					console.log(res);
				}
				
			})
			
		}
	</script>
	
	</div>
	
	<jsp:include page="../include/footer.jsp" />

</body>
</html>