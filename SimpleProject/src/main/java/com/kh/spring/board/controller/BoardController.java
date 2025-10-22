package com.kh.spring.board.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.BoardDTO;
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
	
	@PostMapping("boards")
	public String save(BoardDTO board, MultipartFile upfile, HttpSession session) {
		// ??? 14:28 board 테이블에 한행 insert 하는건데 첨부파일이 파일데이터로 넘어옴 -> 매개변수 추가
		
		log.info("게시글 정보 : {}, 파일 정보 : {}", board, upfile);
		
		// MultipartFile이 무조건 생성되어서 전달된다 -> 하던대로는 구분이 불가함
		// 파일은 아무 의미가 없다, 내용에 데이터가 아무것도 없으면 크기가 없는 0
		// 그러므로 filename으로 구분해야한다, 이름이 빈문자열인 파일은 존재할 수 없으니까
		// 첨부파일의 존재유무
		// MultipartFile객체의 fileName필드값으로 확인해야함
		
		// 서비스가서 작업 네개정도 해야할것같은데 뭐해야할까? 용량 초과는 넘어오기 전에 컨트롤러 앞에서 짤림, 멀티파트로 제대로 안왔으면 얘가 받지를 못함, 매개변수로 들어오지를 않음
		// INSERT INTO BOARD
		// VALUES (#{boardTitle}, #{boardContent}, #{boardWriter}, #{originName}, #{changeName})
		// 들어갈 값에 대해 먼저 생각 -> 작성자가 권한이 있는가
		// 요청 시 전달값 고치는거 아무것도 아님 -> 개발자도구에서 고쳐서 보낼 수 있음
		
		// 1. 권한있는 요청인가
		// 2. 파일 존재유무 체크 => 이름 바꾸기 작업(파일 확장자 체크도 해주면 좋다, 앞에서 뒤에서 다 하는건데 부트 넘어가서 하자) => 파일 업로드
		// 3. 값들이 유효성 있는 값인가
		// 4. 바뀐이름을 changeName필드에 담아서 Mapper로 보내기
		
		// 1하려면 뭐가있어야해?(2,3은 board랑 upfile로 할수있음) -> session 필요 -> 매개변수 작성
		// 서비스에 세개 넘겨야함 -> 서비스 인터페이스 수정
		// 서비스 좀더 집중해서 작업해보자
		boardService.save(board, upfile, session);
		
		return "board/form";
		
	}

}
