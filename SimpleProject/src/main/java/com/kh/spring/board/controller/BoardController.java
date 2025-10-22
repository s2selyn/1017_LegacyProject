package com.kh.spring.board.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.board.model.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {
	
	// 서비스가 필드로 있어야하고 스프링에게 받아서 써야한다 -> 의존성 주입
	// 의존성 주입이란? 이러면 이것도 대답할 수 있어야겠죠? 빈칸채우기를 할 수 있으면 내용을 알 수 있는거니까 나머지 채우기도 가능
	private final BoardService boardService;
	
	// 이번에 매핑은 Get방식이므로
	@GetMapping("boards")
	public String findAll(@RequestParam(name="page", defaultValue="1") Long page
						, Model model) {
		// 앞에서 전달되는 값 뽑아서 사용하려면 매개변수 자리에 애노테이션 추가
		log.info("앞에서 넘어온 페이지 값 : {}", page);
		// 매개변수 안넘어왔을때 발생하는 예외 -> defaultValue로 추가, 문자열로 추가해줘도 Long으로 파싱해준다
		// 앞에서 넘어온 값이 없어도 사용할 수 있게 된다
		
		// 페이징처리
		// 게시글 몇개야
		// 한페이지에 몇개 보여주지?
		// 버튼은 몇개보여주지??
		// 앞에서는 BoardService에 구현하고 컨트롤러에서 selectListCount 하고 페이징처리 객체만들어서 돌려줬음
		// 그렇게 할 필요가 없다! 사실 컨트롤러가 해야할일이 아님, 실제로 하는 곳은 service(컨트롤러는 모델과 뷰 사이에서 중간다리를 해야함)
		Map<String, Object> map = boardService.findAll(page);
		// 반환받은 map을 화면 만들어서 보여주도록 어딘가에 담아서 보내줘야한다 -> requestScope에 담아야함, 화면에서만 쓸거니까
		// 스프링에서 RequestScope에 담기 위한 값을 담을 친구는? Model -> 매개변수 추가
		// Model에 추가하는 방법은 addAttribute (addObject는 ModelAndView일때)
		model.addAttribute("map", map);
		
		return "board/list";
		
	}
	
	@GetMapping("boards/form")
	public String toForm() {
		return "board/form";
	}

}
