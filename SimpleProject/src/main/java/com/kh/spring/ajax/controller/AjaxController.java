package com.kh.spring.ajax.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.kh.spring.board.model.dto.BoardDTO;
import com.kh.spring.board.model.dto.ReplyDTO;
import com.kh.spring.board.model.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AjaxController {
	
	/*
	 * 응답할 데이터를 문자열로 반환
	 * ModelAndView의 viewName필드에 return한 문자열값이 대입(View 객체에 setter로 대입된다)
	 * 이 상태에서 => DispatcherServlet으로 전달되고, 이건 다시 => ViewResolver로 보내버림
	 * 이건 우리가 원하는게 아니야! jsp를 찾아서 보내는게 아니라 순수하게 문자열 데이터만 응답하고싶다
	 * 
	 * 앞에서는 ajax 구현했을때는 서블릿에서 이 작업을 어떻게함? -> response.setContentType("text/html; charset=UTF-8");
	 * PrintWriter pw = response.getWriter();
	 * pw.print(보낼거);
	 * 컨텐트 타입 지정하고 스트림 얻어와서 스트림으로 보냄
	 * 여기는 지금 response가 없음, 디스패처 서블릿이 관리함, DS가 이런식으로 작업하도록 알려줘야함(컨텐트타입도 알려주고 프린트로 보낼거야 jsp 가는거아니야~)
	 * 보낼때는 컨텐트 타입을 지정하고 보낼 방식을 정해야함
	 * 
	 * 반환하는 String타입의 값이 View의 정보가 아닌 응답데이터라는 것을 명시해서
	 * => MessageConverter라는 빈으로 이동하게끔 해줘야한다
	 * 
	 * 그러려면 스프링은 이럴 때 보통 애노테이션을 쓴다
	 * 이번에 사용할 애노테이션
	 * @ResponseBody -> 응답 바디에 넣겠다는 뜻
	 * 
	 * 한글 깨져서 온다 setContentType은 아직 안했음 -> 이건 response가지고 한다
	 * 또 방법이 있음 -> 이건 GetMapping 괄호내부에 추가함 -> produces="text/html; charset=UTF-8"
	 * 이거적으면 앞에 test가 구분이 안되니까 빨간줄 -> 속성적어주면 해결(속성값 하나만 있을땐 자동으로 알아서 들어갔음, 지금은 produces 속성 추가해서 어디로 들어갈지 구분이 안되므로) -> value="test"
	 * 
	 */
	// 요청 받아서 처리해줄 RequestHandler 만들어줘잉, 아무도 요청처리기라고 안한다 리퀘스트 핸들러라고 하니까 그렇게 불러야함...
	@ResponseBody
	@GetMapping(value="test", produces="text/html; charset=UTF-8") // mapping값은 jsp에서 url에 작성한것과 동일하게
	public String ajaxReturn(@RequestParam(name="input") String value) {
		// 요청시 전달값 뽑아서 확인하기로 했으니까 매개변수 자리에 input key값으로 넘어온 value 뽑아써야하니까 -> @RequestParam(name="input")
		
		log.info("잘넘어옴? {}", value);
		// return "ajax/ajax"; 확인하고 주석처리
		// 앞단에 딱히 보낼 게 없어서 이렇게 했는데, 입력값은 잘 넘어오고 브라우저에서 받은것은 ajax.jsp
		// return "이게뭐냐고!"; 이걸로 보내면? -> 404
		
		// DB에 잘 다녀왔다고 가정
		// 오늘 점심은 짬뽕이다! ==> 조회해옴
		String lunchMenu = "오늘 점심은 짬뽕이다!";
		// 이걸 응답해주고 싶음 -> 이거 보내버리면? 입력 아무거나 해보고 나면 404가 돌아온다 -> 메세지 보면 JSP file Not Found
		return lunchMenu;
		// 지금 리퀘스트 핸들러에서 문자열을 반환하고 있다 -> 이러면 무슨일이 일어남? 이 메소드 위에 여러줄 주석 확인
		
		// @ResponseBody 추가하고 브라우저 확인하면 브라우저에서 404는 뜨지않고, STS 콘솔에 전달값 나온다
		// 근데 Response는 한글 깨지고 느낌표만 나옴, 응답할때 컨텐트 타입 지정안함 -> 다시 애노테이션(주석확인) -> 그리고 다시 브라우저에서 확인
		
	}
	
	private final BoardService boardService;
	
	@Autowired
	public AjaxController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@ResponseBody
	@PostMapping(value="replies", produces="text/html; charset=UTF-8")
	public String insertReply(ReplyDTO reply, HttpSession session) {
		// 앞단에서 넘어온거 받을 매개변수? -> 지금 하는거 댓글작성, reply 테이블에 insert하기위해서 세개필요 댓글내용 게시글번호 작성자아이디
		// 앞에서 지금 넘기는 값은 글번호, 댓글내용 / 작성자아이디는 세션에서 뽑을것이다 로그인한거
		// 뽑을거 2개니까 앞에서 넘길때 가공해서 넘긴다 → 나중에 reply로 가공해서 보낼거니까 매개변수자리에 DTO작성
		
		// log.info("{}", reply);
		int result = boardService.insertReply(reply, session); // <- ReplyDTO로 받아서 넘겨야한다
		// replyWriter가 없으니까 세션 넘겨줘야한다 로그인 검증도 해야함
		// ??? 15:15 성공하든 실패하든 데이터 돌려줘야한다 -> 애노테이션
		// @PostMapping 속성수정
		
		// return String.valueOf(result);
		// 성공하면 1, 실패하면 0 갈텐데... 별로 좋은거아님
		// 어제부터 얘기하는 앞단개발자 뒷단개발자 다른상황
		// 앞단에서 댓글작성하려고 보냈는데 뒷단에서 0, 1 보냈다면? 우리는 유추가능, DML이니까 성공1 실패0이겠네? 이럴수있지
		// 근데 앞단만 한 사람이 1/0 보면 애매함, 명확하게 성공실패여부 보내주는게 좋겠다
		// 문자열로 보내는걸로 수정
		return result > 0 ? "success" : "fail";
		// 0, 1보다는 훨씬 명확하다 그래도 마음에 들진 않음, 성공입니다 실패입니다 적어버릴수있지만 영어가 좋다, 메세지같은것도 영어로 해주는게 좋음
		// 왜? 다 하고 깃허브 올릴거아녀, 남들 보라고 올리는거임, 일단은 이게 어쨌든 영어가 공용어 느낌이니 맞춰주는게 좋을지도
		// 코드는 공공재이다 항상 생각해야함 누구나 보고 쓰게해야함 ChatGPT도 코드 다 공개되어있다
		// 누구나 보고 쓰고 할 수 있어야함, 그러니까 항상 코드를 쓸 때는 남한테 보여주는 일기다 라고 생각하고 써야함
		// 남한테 보여주는 일기장이라면 신경써서 써야하겠지?
		
	}
	
	@ResponseBody
	@GetMapping(value="board/{num}", produces="application/json; charset=UTF-8")
	public BoardDTO detail(@PathVariable(value="num") Long boardNo) {
		// 이번엔 url 경로에 달려있는 boardNo를 매개변수로 뽑아서 넘겨줘야함 -> PathVariable 애노테이션에 value속성추가(name도 가능)
		log.info("게시글 번호 잘 오나용 ㅎ : {}", boardNo);
		// 잘 넘어가면 다 끝남~ 우리 boardNo 있으면 board 조회해오는거 다 해놨음
		BoardDTO board = boardService.findByBoardNo(boardNo);
		log.info("혹시 모르니 찍어봄 : {}", board);
		// 잘 돌아온다, 이제 boardDTO를 보내야한다, 앞에선 그냥 문자열 보냈는데...
		// 이친구 들고가야하는데 JSON으로 어떻게 만들지?
		/* 아 편하다~
		 * {
		 * 	"boardNo" : 19,
		 * 	"boardTitle" : "첨부파일있이",
		 * 	...
		 * 	"replies" : [
		 * 		{
		 * 			"replyNo" : 5,
		 * 			"replyContent" : "ㅎㅎ"
		 * 			...
		 * 		},
		 * 		{
		 * 		}
		 * 	]
		 * }
		 * 
		 */
		// 이거 귀찮으니 json array 만들고 json object 가지고도 할 수 있는데 이것도 복잡하고 귀찮음 -> gson
		
		// return new Gson().toJson(board); 원래 이렇게 했는데 똑같이하는거 노잼임 이렇게 안할거임
		// Gson으로 돌려줄거니까 @GetMapping 수정 -> produces 속성값 별 다섯개였는데 다 잊었죠 ㅎ
		// 잘 나오긴 한다, board만 반환하는걸로 수정하고 반환형도 수정
		return board;
		// 스프링이 컨버터로 지가 알아서 함, responsebody로 가는데 json으로 가고싶다고 써놨으니 바꿔준다
		// 라이브러리 꼭 추가해줘야 이걸 알아서 해준다
		
	}
	
}
