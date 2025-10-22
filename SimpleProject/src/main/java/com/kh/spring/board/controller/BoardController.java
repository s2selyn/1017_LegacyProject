package com.kh.spring.board.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.BoardDTO;
import com.kh.spring.board.model.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("boards")
@RequiredArgsConstructor
public class BoardController {
	
	// 서비스가 필드로 있어야하고 스프링에게 받아서 써야한다 -> 의존성 주입
	// 의존성 주입이란? 이러면 이것도 대답할 수 있어야겠죠? 빈칸채우기를 할 수 있으면 내용을 알 수 있는거니까 나머지 채우기도 가능
	private final BoardService boardService;
	
	// 이번에 매핑은 Get방식이므로
	@GetMapping
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
	
	@GetMapping("/form")
	public String toForm() {
		return "board/form";
	}
	
	@PostMapping
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
		
		// save 했다면 게시글 목록 보도록 해줌, 상세보기 했다면 거기로 보냈겠다
		return "redirect:boards";
		
	}
	
	// 게시글 번호가 가변적으로 들어오는데, 이걸 어떻게 뽑아서 활용하고 애노테이션을 중복없이 달수있을까? -> 중괄호 -> 내부에 작성하고 싶은 키값, 지금 pk이니까 보통 이런 경우에 id로 많이 쓴다
	@GetMapping("/{id}") // 몇번 게시글이느냐에 따라 id에 들어올건데, 이걸 매개변수에 받아보자
	public String toDetail(@PathVariable(name="id") Long boardNo) { // boardNo로 사용할거니까 매개변수 선언, url 경로에서 가변적인 값을 뽑아내야한다, 앞에 애노테이션 추가 -> name 속성값은 메소드 상단의 애노테이션에 적은 키값을 여기에 적어준다
		// 잘 넘어오는지 출력해서 확인해보기
		log.info("게시글번호 : {}", boardNo);
		
		// 예를들면 카테고리가 있을 수 있음 -> @GetMapping("boards/{category}/{id}") 이런식
		// @PathVariable(name="category") String category 이렇게 매개변수 추가
		// 그리고 뽑아서 사용 가능
		
		/*
		 * 중요하닷
		 * Board는 매핑값을 선생님 마음대로 하기로 약속했다. 통일해서 준비해오셨음
		 * Board로 조회하고 수정하고 생성하는건데 전부 url을 boards로 시작하게 만들었음
		 * 
		 * mapping
		 * 
		 * 전체조회 			== 		boards				== GET
		 * 상세조회(단일조회) 	== 		boards/{boardNo}
		 * 작성 				== 		boards				== POST
		 * 
		 * 포워딩해주는거 맘에안드시는데 억지로 맞춰두심
		 * 
		 * 개발회사에 취업했다고 가정, 서비스기업(비투씨?)이라면 파트가 잘 나뉘어있음, 뒷단작업자와 앞단작업자가 다르다는 뜻
		 * jsp 작업하는 사람이랑, controller부터 그 이후 작업하는 사람이랑 사람부서팀 다다른거지
		 * 문제가 생긴다. 결국 앞사람이 매핑값 써서 뒷단사람에게 넘겨줘야함
		 * 근데 우리 테이블 네다섯개 작업도 이렇게 많이 필요한데 테이블 200개 300개 이래버리면 CRUD 작업 전부 필요할 때 매핑값 엄청많아진다
		 * 나혼자 작업하면 내가하니까 앞뒤보고 작업하면 되지만, 나뉘어있고 마주칠일도 없을수도있음, 따로작업해도 맞춰서 작업하도록 해야함
		 * 어느정도 규약이 정해져 있다면 생각을 통일시킬 수 있으니 굳이 소통하지 않아도 작업이 수월해지겠지
		 * 그중의 하나의 방법, url에 무슨 자원을 요청하는지 쓰는것
		 * 요청 매핑 경로(값)에 앞에서 보내는 요청이 무슨 자원을 요청하는건지 적는다
		 * 
		 * 지금은 board와 관련된 자원을 여기서 처리하니까 매핑값을 boards로 통일해버림
		 * 그럼 앞서 배운 내용과 충돌, 매핑값이 같으면 서버가 안올라간다, 충돌이나니까
		 * 이건 너무 당연한걸...? 매핑값 똑같이 해버리면 서버 실행부터 빵터짐
		 * 
		 * 선생님이 만들어두신건 방식을 차이뒀다
		 * 똑같은 요청매핑이지만 방식이 다르면 구분이 가능해짐
		 * 이렇게 하면 앞단 개발자는 편해, 요청자원을 적으면 됨
		 * 뒷단개발자도 편함, 어차피 모든 요청에 boards가 들어가야한다면 이걸 Controller에서 뽑을 수 있음
		 * -> @RequestMapping("boards") 이걸로 뽑아버리고
		 * -> 메소드에 붙은 boards 부분을 다 날릴 수 있음
		 * 그러면 이 컨트롤러는 boards로 시작하는 요청을 모두 처리할 수 있게 된다
		 * 
		 * 문제가 생김
		 * SELECT == GET / INSERT == POST
		 * select 전체조회라면 아무것도 안붙이고, pk로 조회하면 boards에 /pk로 붙여
		 * ??? 16:37 insert의 경우에는?
		 * 
		 * 이러고나면 UPDATE, DELETE가 남는다
		 * @PutMapping 은 업데이트(또는 @FetchMapping)
		 * 
		 * @DeleteMapping 은 딜리트
		 * 
		 * 어떤 자원을 요청하는지는 url에 적고
		 * CRUD는 메소드로 구분하자
		 * 
		 * 모두가 똑같이 이렇게 생각한다면 이것때문에 고민할 필요도 없고 앞뒷단 이슈 생길일이 없다
		 * 다음주부터는 이렇게 할건데, 아직 앞단에서 update, delete 요청을 보내는 방법을 모른다
		 * GET / POST로 보내는 방식은 안다
		 * ??? 16:39 a태그 등을 사용
		 * 
		 * 아무튼 @DeleteMapping, @PutMapping은 배워야할것들임
		 * 
		 * ??? 16:45 인터파크 구경
		 * 리액트 배우고 앞단은 리액트로 만들고 뒷단은 스프링부트로 만들것임 <- 최종형태
		 * 
		 * ??? 16:46 앞에 상대경로 방식으로 해놔서 절대경로로 바꾸는 작업도 추가해야한다
		 * 일단 이러고 작업하자
		 * 
		 */
		
		// 조회수 증가
		// 조회수 증가에 성공했다면 SELECT로 조회
		// 만약에 없는 게시글 번호라면 예외발생
		// 여기서 뭐 할거없음, 클릭해서 조회라 권한체크도 필요없으니 바로 서비스단 넘어가자
		boardService.findByBoardNo(boardNo);
		
		return "board/detail";
		
	}

}
