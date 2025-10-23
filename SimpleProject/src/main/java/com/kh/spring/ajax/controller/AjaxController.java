package com.kh.spring.ajax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.spring.board.model.dto.ReplyDTO;
import com.kh.spring.board.model.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AjaxController {
	
	/*
	 * 응답할 데이터를 문자열로 반환
	 * ModelAndView의 viewName필드에 return한 문자열값이 대입(setter로 대입된다 View 객체에)
	 * 이 상태에서 => DispatcherServlet으로 전달되고
	 * 이건 다시 => ViewResolver로 보내버림
	 * 이건 우리가 원하는게 아니야! jsp를 찾아서 보내는게 아니라 순수하게 문자열 데이터만 응답하고싶다
	 * 
	 * 앞에서는 ajax 구현했을때는 서블릿에서 이 작업을 어떻게함? -> response.setContentType("text/html; charset=UTF-8")
	 * PrintWriter pw = response.getWriter();
	 * pw.print(보낼거);
	 * 컨텐트타입지정하고스트림얻어와서스트림으로보냄
	 * 여기는 지금 response가 없음, 디스패처 서블릿이 관리함, DS가 이런식으로 작업하도록 알려줘야함
	 * ??? 보낼때는 컨텐트 타입을 지정하고 보낼 방식을 정해야함
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
	 * 이거적으면 앞에 test가 구분이 안되니까 빨간줄 -> 속성적어주면 해결 -> value="test"
	 * 
	 */
	// 요청 받아서 처리해줄 RequestHandler 만들어줘잉, 아무도 요청처리기라고 안한다 리퀘스트 핸들러라고 하니까 그렇게 불러야함...
	@ResponseBody
	@GetMapping(value="test", produces="text/html; charset=UTF-8") // mapping값은 jsp에서 url에 작성한것과 동일하게
	public String ajaxReturn(@RequestParam(name="input") String value) {
		// 요청시 전달값 뽑아서 확인하기로 했으니까 매개변수 자리에 input key값으로 뽑아써야하니까 -> @RequestParam(name="input")
		
		log.info("잘넘어옴? {}", value);
		
		// DB에 잘 다녀왔다고 가정
		// 오늘 점심은 짬뽕이다! ==> 조회해옴
		String lunchMenu = "오늘 점심은 짬뽕이다!";
		
		return lunchMenu; // 이걸 응답해주고 싶음
		// 문자열을 반환하고 있다
		
		// return "ajax/ajax";
		
	}
	
	private final BoardService boardService;
	
	@Autowired
	public AjaxController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@PostMapping("replies")
	public String insertReply(ReplyDTO reply) {
		// 앞단에서 넘어온거 받을 매개변수?
		
		log.info("{}", reply);
		boardService.insertReply(reply); // <- ReplyDTO로 받아서 넘겨야한다
		
		return "";
		
	}
	
}
