package com.kh.spring.ajax.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.ajax.dto.AjaxResponse;
import com.kh.spring.board.model.dto.BoardDTO;
import com.kh.spring.board.model.dto.ReplyDTO;

// @RestController == @Controller + @ResponseBody : 비동기요청처리용 컨트롤러로 빼서 이렇게 작업하면 굳이 RB 붙일필요가 없다 -> 1절
// 그럼 안에 있는 핸들러가 전부 revol 관련 작업이니까 이걸 RequestMapping으로 추출할 수 있다
// produces 속성도 똑같네? 분리가능 -> RequestMapping에 작성
// ajax 처리 컨트롤러는 이렇게 달면 편하겠다... 그럼 궁금한데? 나머지는 전부 중복제거라 해봤던건데 @RestController의 Rest는 뭐지..?
@RestController // -> 반환이 무조건 ResponseBody로 돌아간다
@RequestMapping(value="revol", produces="application/json; charset=UTF-8")
public class RevolutionController {
	
	/*
	 * REST(REpresentational State Transfer)
	 * 웹에서 데이터 주고받는 아키텍처 스타일중의 하나(http처럼)
	 * 효율적인건..? REST라는 전송방식은 없음, 그건 구현되지 않는다, 이건 스타일중의 하나
	 * 스타일이라 실제 통신은 http로 함 == http로 통신할때의 스타일인것
	 * 
	 * HTTP프로토콜을 활용한 아키텍처 스타일 중 하나 ==> 제일 잘나감
	 * 
	 * 이거 만든 아저씨는 로이 필딩, 대학다니던 시절에 http 1.0 개발에 참여한 어마어마하게 개발 잘하는 사람이라는 뜻, REST라는 스타일을 만들어냄(솔직히 논문을쓴것)
	 * http만으로 통신은 비효율적이니까 스타일제안 -> 개발업계에 잘 퍼져서 너도나도 rest
	 * 근데 이아저씨가 몇년전에 한 말, 자기 논문처럼 개발하는 개발자 100명중에 1명도 찾기 힘들다고 함
	 * 그누구도 없다네요 만든사람이 마음에 드는게 없다고 함
	 * 너도나도 rest 써서 개발했다고 하는데 니들이한거 다 rest 아닌데? 이러고 있다네요
	 * 
	 * 아무튼 사람들이 물어봤더니 필딩이 말한대로 구현한사람은 없다고 하네요, 엄청 깐깐하다네요
	 * 그럼 웹개발 실무진이 생각하는 REST방식은 어떤건지 얘기하는거부터 시작
	 * 
	 * 자원(Resource)중심의 URL구조 + 상태없음(Stateless) 통신
	 * 서버가 클라이언트의 상태를 저장해놓지 말자는게 상태없음 통신
	 * 여태 우리는 Stateless 상태 구현못함
	 * 클라이언트가 로그인한 상태 세션에 계속 저장함, 내가만든거 내가 들어가보는건 지금 상관없는데
	 * 내가만든 사이트가 겁나 잘돼서 동접 3천만명 이래버리면? 그만큼의 사용자정보를 서버가 세션에 저장해서 유지시키고 가지고있어야함
	 * 말이안된다... 하나의 서버가 그만큼의 데이터 감당하는게 말이안된다 -> 서버가 분산되어야함 -> 앞에 당연히 로드밸런서 붙어있음, 로드밸런서(부하분산기) 개념
	 * 실제 물리장치(컴퓨터)로 구현하기도 하고 소프트웨어적으로도 구현하기도함
	 * 앞에서 로드밸런서가 먼저 요청을 받고 안바쁜 서버에게 요청을 전달함, 라운드로빙방식이 제일 대표적으로 설명됨
	 * 10개온다면 순서대로 요청을 보냄 1번은 1번에게 2번은 2번에게 이런식 -> 하나의 서버가 모든요청 처리보다는 부하가 줄어든다
	 * 이러면 생기는 문제 -> 사용자가 로그인 요청을 보내서 1번 서버가 로그인 정보를 세션에 가지고 있었음
	 * 근데 두번째 요청이 2번 서버로 로드밸런서가 넘겨버리면 로그인 안된상태, 세션에 없음, 모두가 알아야한다 로그인된상태라는것을
	 * 그럼 연동해야겠죠? 그럼 병렬로 해놓은 의미가 뭐임? 기껏 나눠놨더니
	 * 지금 방식은 작은 제품(소프트웨어) 프로젝트 소규모에 어울리는 방식
	 * 
	 * REST 방식 아키텍쳐를 만들때 제일먼저 이야기하는것은 아무튼 자원중심의 URL구조를 만들자!
	 * 이건 앞에서 board가지고 이야기한적있다.
	 * 그리고 앞에서 get/post/delete로 요청 보낼 수 있는 방법을 알아냈음
	 * 예를 들어서 board라고 이야기 -> 클라이언트가 서버에게 보내는 요청이 board에 대한 요청이라면? 자원이 게시글인거지
	 * GET방식은 조회요청이 된다 -> 단복수 상관없는데 권장사항은 복수형, 그래서 board 아니고 boards
	 * GET		/boards				==> 게시글 목록 조회
	 * GET		/boards/19			==> 게시글들 중 19번 게시글 조회(계층구조의 요청)
	 * 
	 * 우리 다이나믹 웹할때 게시판 하나로 일반/사진 이런식으로 한것처럼 하는 경우가 있다 -> 계층구조를 URL에 추가
	 * GET		/boards/photo/19	==> 게시글들 중 사진게시글들중 19번 게시글 조회
	 * 
	 * POST		/boards				==> 새 게시글 생성
	 * 어차피 전달값이 body영역에 들어간다 노출되지 않음 -> URL이 변하지 않으니 그대로 /boards
	 * 
	 * update는 종류가 두가지
	 * PUT		/boards/19			==> 19번 게시글 전체 수정(한 행 전체 갈아엎기)
	 * PATCH	/boards/19			==> 19번 게시글 부분 수정(회원정보 중에서 비밀번호만 수정)
	 * 근데 이건 귀에걸면 귀걸이 코에걸면 코걸이, 보편적으로는 PUT을 수정으로 많이 쓴다
	 * 
	 * DELETE	/boards/19			==> 19번 게시글 삭제
	 * 
	 * board 할 때 이렇게 한것같은데? 그럼 그게 REST 구현한건가? 아님, 한 30%뿐
	 * 
	 * 매우 중요한것 : 응답! 서버가 클라이언트에게 응답해주는것
	 * REST 방식 구현은 서버가 클라이언트에게 응답할 때 항상 상태코드를 응답해줘야함(우리가 맨날 보는 404 이런것들)
	 * 
	 * HTTP 상태 코드 활용(기본)
	 * GET/DELETE요청이 왔는데 서버입장에서 잘처리했다면 200 OK를 돌려줌
	 * 200 OK						==> 요청이 성공적으로 잘 이루어졌음(GET, DELETE)
	 * 201 Created					==> 요청에 의해 데이터가 잘 만들어짐(POST, PUT, PATCH 요청을 보낸대로 잘 했다)
	 * 
	 * 서비스단에서 참 많이한거있죠? 사용자가 이상한 값을 보내는 잘못을 저지름 -> 빈문자, 글자수제한위반 등
	 * 400 Bad Request				==> 잘못된 요청(4로 시작하면 클라이언트 잘못 느낌)
	 * 401 Unauthorized				==> 인증 실패(로그인해야만 이용, 관리자만 이용 등을 로그인안하고 요청하거나 관리자 아닌 일반사용자가 요청)
	 * 404 Not Found				==> 없음
	 * 
	 * 500 Internal Error			==> 서버 터짐
	 * 
	 * 응답할때 이런거 같이 보내서 앞에서는 상태코드 보고 잘됐는지 안됐는지 체크하고 적절한 메세지를 화면에 출력해줘야함
	 * 
	 * 이게 유행하게 된 계기는 통신장치의 다양화가 컸음
	 * 옛날에 웹, 메일, 파일 FTP 이용은 컴퓨터뿐이었음
	 * 이제 시대가 흐르면서 시작은 피처폰이겠지? 가운데 버튼이 하나 있죠? 보편적으로 SKT면 Nate 이런거 누르면 큰일나는 버튼
	 * 그때는 반응형 웹, 모바일용 화면 이런게 없었음, 이거 누르면 브라우저에서 나오는 사이트 화면이 그대로 나온다, 그거띄우려면 텍스트 이미지 광고영상 많으니까
	 * 자원들을 통신데이터로 가져오니까, 그당시에는 통신데이터 끌고오면 과금됨, 버튼 한번 눌렀는데 오백원, 놀래서 사람들이 끄겠지?
	 * HTTP는 무서워서 꺼버리면 프로세스 날아갈테니 나중에 다시 누르면 또 받아오겠죠? 차라리 안껐으면 캐싱한거 그대로 써서 돈 더 안냈을수도...? ㅎ
	 * 아무튼 그때 무서워서 못쓰다가 ,, PDA보다 더 이전세대 장치가 있는데
	 * 그전엔 워크맨, CD플레이어, mp3 player 이런거 쓰던... 그러다가 LG에서 내놓은 프라다폰? 이거 2007년 이게 브라우저같은걸 이용할수있게된 LG전자 회심의한수..
	 * 그이후에 또 아이폰이 나왔져, 우리나라만의 문제, 와이파이가 좀 늦게 들어왔다(통신사 엄살)
	 * 아이팟, 아이폰 나오면서 대 인터넷 세상이 열려버림, 스마트폰 게임같은거, 메신저(왓치앱같은거)
	 * 그전까지는 문자메세지 보낼때 돈들었죠? 스마트폰 쓰면 문자주고받는데 돈이안든대 와이파이만 있으면 무료로계속 문자메세지 주고받을수있다니까?
	 * 인터넷환경에서 이용할수있는 서비스가 무궁무진해짐, 그래서 아이폰 시작으로 스마트폰 죽죽 나오다가 너무작아요 태블릿 죽죽나오고
	 * 스마트폰으로만해야대? TV 냉장고 여기저기 달아서 서버에 요청 보내게 만들어버림
	 * 그러면 이렇게 개발하는거 너무 비효율적임.. 사용자가 무조건 브라우저를 통해서 이걸 본다? 아니거든요.. 어디서든 요청 보낼수있음
	 * 그거마다 브라우저용, 태블릿용, 스마트폰용, 자동차용, 티비용, 냉장고용 데이터형식 다 따로 만들면 서버/핸들러 미쳐버린 갯수
	 * 앞에서 요청보낼 장치가 너무 많아짐, 그걸 화면 전부 서버에서 만들기 비효율적이니까 서버는 사용자에게 데이터만 넘겨주자
	 * 세탁기는 C로 개발, 이건 C로 JSON 형태 다룰 수 있음, 화면만들기가능
	 * 게임개발은 C#이래, 그럼 스크립트로 보낸대, 서버에서는 또 JSON으로 응답해줘, C#도 JSON으로 온거 화면으로 만들 수 있음
	 * C++, JS 다 화면 만들수있음
	 * 클라이언트가 다양해져도 서버입장에서는 똑같은 데이터를 보내고 싶으니 REST방식이 유행
	 * 처음에는 XML로 보냈지만 오늘날의 표준은 JSON, 어느언어에서든지 파싱해서 쓸수있음(범용성이 좋다, 모든 언어에 JSON 파싱 라이브러리가 있음)
	 * 
	 * 오늘 AJAX쓰면서 데이터 응답해줬는데
	 * 어떤건 produces가 html이고 json이러면 못빼고 하나하나 달아야함 이게 맘에안든다
	 * --> 작업을 어떻게? ajax 패키지에 패키지 생성
	 * 
	 */
	
	@GetMapping("/a")
	public BoardDTO a() {
		
		BoardDTO a = new BoardDTO();
		a.setBoardTitle("b임");
		return a;
		
	}
	
	@GetMapping("/b")
	public BoardDTO b() {
		
		BoardDTO b = new BoardDTO();
		b.setBoardTitle("b임");
		return b;
		
	}
	
	@PostMapping("/c")
	public AjaxResponse c() {
		
		String str = "어허엏엏ㅇ";
		AjaxResponse ar = new AjaxResponse();
		
		// post방식이니 잘됐으면 201
		ar.setCode("201");
		
		// 실제 응답할 데이터는 str
		ar.setData(str);
		
		// 앞단에 전달할 추가적인 메세지
		ar.setMessage("데이터 생성에 성공했습니다.");
		
		return ar;
		
	}
	
	@GetMapping("/d")
	public AjaxResponse d() {
		
		ReplyDTO reply = new ReplyDTO();
		AjaxResponse ar = new AjaxResponse();
		
		ar.setCode("200");
		ar.setData(reply);
		ar.setMessage("조회에 성공했습니다.");
		
		return ar;
		
	}

	// c, d는 responsebody가 똑같은데 반환이 다름, 이부분을 AjaxResponse를 이용해서 통일시켜준다 -> 반환형으로 작업
	
}
