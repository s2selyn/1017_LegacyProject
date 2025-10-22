package com.kh.spring.board.model.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.BoardDTO;
import com.kh.spring.board.model.mapper.BoardMapper;
import com.kh.spring.exception.AuthenticationException;
import com.kh.spring.exception.InvalidArgumentsException;
import com.kh.spring.member.model.dto.MemberDTO;
import com.kh.spring.util.PageInfo;
import com.kh.spring.util.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // bean 등록
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper boardMapper;
	// 주입받는방법은 생성자주입, 세터주입, 필드주입 세가지 있다
	// 권장되는 방법은 생성자 만들어서 @Autowired 애노테이션 다는방법이 있다 근데 필드 늘어나면 생성자 수정해야하니 귀찮다
	// 생성자 주입법을 롬복 이용하도록 애노테이션 작성 --> 롬복에서 제공해준다
	// 생성자 주입 받을 필드니까 반드시 final 키워드 필요
	
	private final Pagination pagination;

//	@Override
//	public int selectListCount() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
	// findAll 내부로 옮기도록 인터페이스 수정했으므로 없애버림

	@Override
	public Map<String, Object> findAll(Long page) {
		// 여기도 원래 PageInfo pi로 받은것은 서비스에서 해야하는 일이었으니 Long page로 변경
		
		Map<String, Object> map = new HashMap();
		List<BoardDTO> boards = new ArrayList();
		
		// 뭐할까용?? ==> 여기서 뭐할까용?? 유 효 성 검 증
		// boardNo가 넘어왔는데, 넘어온게 어쩌라고 할 수 있지? 장난꾸러기가 있을 수 있다
		// url 수정해서 요청하면 잘못된 값 넘어오는 경우들 확인해줘야함
		if(page < 1) {
			
			// 1보다 작다면 장난을 쳤나보군, 이녀석 그러면 안된다 하고 알려줌
			// 억지로 강제로 예외를 발생시키면 좋겠다
			throw new InvalidArgumentsException("잘못된 접근입니다.");
			// 직접 이용해보고 따라해봐도 좋다. 아무것도 없는 상태에서는 여기 뭐넣을지 고민 엄청 해야겠지
			// 기존 사이트들을 많이 이용해보셔
			
		}
		
		// 페이징처리 객체 생성 해야하는데...
		// 일단 BoardMapper도 스프링에게 주입받아서 써야함
		// BoardMapper를 필드에 선언하러 위에 작성하고옴
		// 메소드는 아직 없고 정수로 받아올것임 -> BoardMapper ㄱㄱ
		int count = boardMapper.selectTotalCount();
		// 호출 잘 되는지 출력해서 확인
		log.info("총 게시글 개수 : {}", count);
		
		// Pagination Bean으로 등록했으니 필드 추가하고와야함
		PageInfo pi = pagination.getPageInfo(count, page.intValue(), 5, 5);
		// 페이징 처리용 PageInfo 객체 만든것!
		
		// 앞에서 offset, limit 계산해서 매퍼에 이거 전달하면서 OFFSET 문법 사용하고 맞는 페이지 게시글들만 조회했다
		// 해본거 또하면 노잼이니까 안해본거 또해보자
		// 마이바티스 공식문서 보면 RowBounds 객체 생성해서 앞에서 했던것처럼 offset, limit 전달하고, 실제 sql문 수행할 때 이 RowBounds 객체를 넘기기만 하면 따로 offset 문법 사용하지 않아도 알아서 그만큼만 조회해서 데리고온다
		// RowBounds 객체 만들어서 해볼건데,
		// 총 조회된 개수가 없을수도 있다! 게시글 전체 삭제했거나 아무것도 등록안했거나 -> 게시글이 없으니 굳이 가서 selectBoard 해볼 필요도 없고 RowBounds 만들 필요도 없고 매퍼 갈필요도 없음
		// 게시글 있을때만 RowBounds만들고 매퍼도 갈 작업 할거니까 조건 -> 있는지 없는지는 count로 판별
		if(count > 0) { // 0보다 크면 게시글이 있는거다! RowBounds 만들어보자
			
			RowBounds rb = new RowBounds((page.intValue() - 1) * 5, 5);
			// 마이바티스 객체 생성해서 offset, limit 값 전달
			
			// Mapper의 findAll 메소드 호출하면서 RowBounds 객체 전달
			boards = boardMapper.findAll(rb);
			// 돌아올거 받아주자
			
			// 앞단에서 화면에 뿌려야할거임, 페이징 버튼 만들어야하니까 pi도 들고가야함, 서비스에서 컨트롤러로 보내야할게 두개
			// pi랑 boards 두개 담아줘야하는 상황임, 리턴할때 하나밖에 못돌아가는데 두개돌려야함, 두개를 어디담을까? 선택의 영역
			// 지금 반환타입이 잘못된거죠? 인터페이스 생각해서 짜고 개발했는데 코드짜다보면 차마 이것까지 생각못했구나!
			// 처음 생각하더라도 개발하다보면 배우는게 많아지고 실력이 는다, 그럼 이렇게 해야겠다 싶으니 코드 수정해야함
			// 정해야한다 두개 돌아가야하니까 어딘가에 담아야한다 -->  보통 선택지가 두개, 지금의 특징은 두개의 값을 담아야하는데 타입이 다름
			// 하나는 둘다 담을 수 있는 무언가(DTO)를 만들기 -> 필드 두개를 놓고 pi, List<BoardDTO> 설정 / 다른 하나는 Map
			// 타입이 다른 여러개 값을 담으려면 보통 list, set, map 세개
			// 컬렉션은 각 친구들이 특징이 있다, list는 순서가 보장되어야 하는 상황, set은 중복을 제거해야할 때, map은 값을 효율적으로 관리하고싶을때(key가 있으니까)
			// 지금은 map이 제일 괜찮은 선택지, 뭘 써도 잘 동작(담아서 앞으로 반환가능) -> 이런 상황에서 개발자들이 선호하는것은 이름 붙여놓는거
			// 그러고나면 DTO / Map 중에 고민하는것, 정답은 Map이고 이유는 두가지(본질은 같음)
			// DTO는 생산효율적 측면에서 만드는데 시간이 오래걸린다는 단점, 나중에 코드 파악하는데 시간이 걸림(나중에 다른 개발자가 보면 DTO 공부해야함)
			// 그래서 만들어서 사용하기보다는 유지보수 측면에서 기존의 것을 사용하는게 효율적, 자바개발자라면 누구나 맵을 알것이다, 키밸류 하려고 썼구나~
			// 근데 DTO 쓰면 안되는건 아님, 좋은 선택지중에 하나 -> 아무튼 메소드 초입에 맵 생성하고 반환타입 수정
			
		}
		
		// 만들어둔 맵에 pi, boards 담을건데, list를 맵에 못담는게 문제
		map.put("pi", pi);
		
		// 지금 if 스코프 벗어난 지역변수라 못담으니 선언을 바깥에서 해준다
		map.put("boards", boards);
		
		return map;
		// -> 인터페이스 반환형 수정
		
	}
	
	private void validateUser(BoardDTO board, HttpSession session) {
		
		String boardWriter = board.getBoardWriter();
		MemberDTO loginMember = ((MemberDTO)session.getAttribute("loginMember"));
		if(loginMember == null || !boardWriter.equals(loginMember.getUserId())) {
			throw new AuthenticationException("권한 없는 접근입니다.");
		}
		
	}
	
	private void validateContent(BoardDTO board) {
		if(board.getBoardTitle().trim().isEmpty() ||
				   board.getBoardContent().trim().isEmpty()) {
					throw new InvalidArgumentsException("유효하지 않은 요청입니다.");
				}
		String boardTitle = board.getBoardTitle().replaceAll("<", "&lt;");
		String boardContent = board.getBoardContent().replaceAll("<", "&lt;");
		if(board.getBoardTitle().contains("이승철바보")) {
			boardTitle = board.getBoardTitle().replaceAll("이승철바보", "글쓴사람바보");
		}
		board.setBoardTitle(boardTitle);
		board.setBoardContent(boardContent);
		
	}

	@Override
	public int save(BoardDTO board, MultipartFile upfile, HttpSession session) {
		
		// 1번 권한검증
		// board에서 boardWriter 가져오기
		// String boardWriter = board.getBoardWriter();
		
		// session에서 loginMember 받아오기
		// MemberDTO loginMember = ((MemberDTO)session.getAttribute("loginMember"));
		
		/*
		if(loginMember == null) {
			throw new NullPointerException("로그인해");
		}
		// 이거 좀 합리적으로 할수없나? 자바할때 두번이상 한건디?
		// 자바 뿐만 아니라 다른것들은 숏서킷 연산을 한다 -> 두번 적을 필요가 없음
		
		// loginMember에서 userId 가져오기
		String userId = loginMember.getUserId();
		
		// 비교 -> boardWriter != userId 이렇게 하면 안됨 주소값 비교가 된다
		// loginMember가 null이면 NPE가 일어날텐데 어디서? loginMember. <- 여기서 난다, 널을 가리킴! 널일 수 있는건 loginMember, 널을 가리키는 곳은 참조하는부분이겠지
		// NPE는 UE임, loginMember 널인지 아닌지 먼저 판별해줘야함 -> loginMember 받아오는곳 다음에서?
		if(!boardWriter.equals(userId)) {
			throw new AuthenticationException("권한 없는 접근입니다.");
		}
		*/
		
		// Short - Circuit 연산
		// OR이든 AND든, OR인 경우는 둘중에 하나가 true면 true니까 앞이 true면 뒤를 연산 안하고
		// AND인 경우에는 하나라도 false면 false니까 앞이 false면 뒤를 연산 안하는거
		// 자바기본이잖아요 복습좀.. 변수, 자료형, 조건문, 반복문, 배열 다섯개는 기본이다, 자바가 아니더라도.. 연산자도 기본이야
		/*
		if(loginMember == null || !boardWriter.equals(loginMember.getUserId())) {
			throw new AuthenticationException("권한 없는 접근입니다.");
		}
		*/
		
		// 원래는 클래스로 분리, 지금은 시간없어서 메소드로 분리 -> 이 위에 validateUser 작성하고 있던거 주석처리
		validateUser(board, session);
		
		// 2번 값에 대한 유효성 검증
		// ??? 15:12 정상적으로 들어왔는지 체크해줘야함
//		if(board.getBoardTitle().trim().isEmpty() ||
//		   board.getBoardContent().trim().isEmpty()) {
//			throw new InvalidArgumentsException("유효하지 않은 요청입니다.");
//		}
		
		// 지금 공격에 취약한 상태
		// 악의적인 친구라서 회원가입 할 때 email에 스크립트 태그 같은거 써서 <script>alert('hi')</script> 가입요청
		// 이러면 원래 들어갔어야하는데 부트스트랩이 값을 막아줬다네요
		// 이건 크로스 사이트 스크립팅 공격, XSS공격이라고 한다
		// 게시글 내용에 <script>for이런식으로 해서 다른사람이 게시글 클릭했을 때 자바스크립트코드가 동작해서 망가지는 식
		// 이런걸 유효값 검증할때 체크해서 빼줘야한다 -> 태그가 태그로 인식되지 않도록 -> 꺾쇠 기호를 대체하는식
//		String boardTitle = board.getBoardTitle().replaceAll("<", "&lt;");
//		String boardContent = board.getBoardContent().replaceAll("<", "&lt;");
		
		// 아니면 이런것도 가능~ 욕설 필터링 이런거 -> 리스트나 배열 쓰면 좋겠지만 단적으로
//		if(board.getBoardTitle().contains("이승철바보")) {
//			boardTitle = board.getBoardTitle().replaceAll("이승철바보", "글쓴사람바보");
//		}
		// 이렇게 바꿔주는건 생각해보고 하는거겠지? 필요하다면 하는거임
		
//		board.setBoardTitle(boardTitle);
//		board.setBoardContent(boardContent);
		// DB가서 insert 하기 전에 해야하니까 이거 서비스단에서 하는거임
		// 근데 또 insert와는 크게 상관이 없고 필드값 검증이니까 빼낸다, 그리고 주석처리
		validateContent(board);
		
		// 3번 파일이 있을경우 업로드 ==> 빈으로 분리하기 숙제
		// 있을때만 올려야함 -> 조건, 파일이 있는지 없는지는 upfile에 있는 Name필드 -> 자바개발자라면 get으로 지었겠지? -> 빈문자열이 아니어야함 -> isEmpty()
		if(!upfile.getOriginalFilename().isEmpty()) {
			
			// 이름바꾸기
			// KH_시간+숫자+원본확장자
			// 생각해보니 문자열 더하기 해야하는데 메모리낭비 하기싫음 -> 두개있는데 스트링빌더
			StringBuilder sb = new StringBuilder();
			sb.append("KH_");
			String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			sb.append(currentTime);
			sb.append("_");
			int num = (int)(Math.random() * 900) + 100;
			sb.append(num);
			String ext = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));
			sb.append(ext);
			// 파일의 이름을 새로 만드는 과정
			// ??? 15:29
			
			// ----- 이 위아래도 메소드 두개로 나눠서 책임분리 해야함, 이름바꾸는거고 경로 ??? 15:39
			
			// 물리적인 경로를 이용하기 위해서 이용했던 스코프 -> applicationScope, 이건 타입이 뭐였더라? ServletContext
			ServletContext application = session.getServletContext();
			String savePath = application.getRealPath("/resources/files/");
			
			try {
				upfile.transferTo(new File(savePath + sb.toString()));
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			// 이거 끝나면 또 이렇게 안할거임 자바 복습(위에서 다섯개 한거에 ??? 추가)
			// 자바 API(JAPI) 중에서 제일 잘 쓰는건? String
			// 까먹는거 당연한데 중요한건 다시봐야함, 까먹은 상태로 그냥가면 안된다. 고급지식 이해하기 어려워진다.
			// 얘도 클래스로 빼고 bean으로 쓰는게 좋다, 파일첨부 하려면 똑같이 이런코드 써야함, 파일을 책임지는 클래스, 한명이 작업하고 다 사용하는게 효율적
			// 얘 책임분리하기 까지가 커트라인 ==> 총대가 여기까지 해줘
			// 팀프로젝트를 하면 총대가 root-, security-, servlet-context / web.xml, pom.xml / 공통모듈 분리까지 해주고 repo 올리는것
			// 괜히 나눠서 작업하면 사고날 확률이 크다
			// 세부내용은 뭐 얘기해서 고치고 수업자료 써도되고 총대는 뭐 발표나 보고서에서 빼준다든지 할일 줄여주는거지
			// 총대가 좋은점은 팀원들이 사용해야할 공통모듈 작업해서 팀원에게 제공해서 생산성을 향상시켰다 이런식으로 어필가능
			
			// 파일이 있을 때 업로드까지 끝나면, 파일에 대한 정보를 board에 넣어주자
			board.setOriginName(upfile.getOriginalFilename());
			
			// full 경로 써줘야한다
			board.setChangeName("/spring/resources/files/" + sb.toString());
			
		}
		
		int result = boardMapper.save(board);
		
		if(result != 1) {
			// 이럴 때 발생시킬 예외하나 깔끔하게 만들어보기
			throw new RuntimeException("이게 왜 이럴까요...?");
		}
		
		return result;
		
	}

	@Override
	public BoardDTO findByBoardNo(Long boardNo) {
		
		// 제일 처음에 뭐부터 할까? 장난꾸러기가 있을 수 있겠지? 게시글 번호가 슬래시 뒤에 붙어서 와야하는데 0, -500 이런식의 장난
		// 장난치지마라고 돌려보내자
		// 게시글 있는지 없는지 DB에 가야하는데, 있을 수 없는 번호라면 보낼 필요도 없다 -> boardNo가 장난꾸러기인가 아닌가를 검증, 이건 PK, SEQ로 만드니까 1부터 시작, 그보다 작을 수 없다
		if(boardNo < 1) {
			throw new InvalidArgumentsException("유효하지 않은 요청입니다.");
		}
		// 유효성 검증 했음
		
		// select, update 중에 뭐 먼저할지 정한다, DML 먼저하는것을 선호한다
		// UPDATE 먼저하고 SELECT 하는것을 선호한다
		// select 먼저하면 조회수 증가하기 전의 count가 들어있으니까 보편적인 상황에서 update 먼저해서 데이터 갱신시키고 최신 데이터를 조회(select)한다
		int result = boardMapper.increaseCount(boardNo);
		
		// 증가했을수도 안했을수도 있는데 안했다면 문제생긴거임 -> 보편적으로 쓰는것? BadRequestException은 못씀
		if(result != 1) {
			
			throw new InvalidArgumentsException("잘못된 요청입니다.");
			// 숙제로 만든 예외클래스로 바꾸면됨
			// 아무튼 insert 성공 못했으면 여기서 빠꾸당함
			
		}
		
		// insert 성공했다면 조회 해와야한다
		BoardDTO board = boardMapper.findByBoardNo(boardNo);
		// 조회한것을 반환받는다
		// 조회수 증가되고 조회하러 DB가는 사이에 게시글 삭제되었을 수 있음, 그럼 select 조건절에서 걸리니까 조회결과 없음
		// 지워진걸 들고가면 안된다! 그러니까 한번더 board가 조회가 안됐다면 null과 같을거니까 비교해야함
		if(board == null) {
			throw new InvalidArgumentsException("삭제된 게시글입니다.");
		}
		
		return board;
		
	}

	@Override
	public int deleteByBoardNo(Long boardNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(BoardDTO board) {
		// TODO Auto-generated method stub
		return 0;
	}

}
