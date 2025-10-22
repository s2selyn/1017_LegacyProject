package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper boardMapper;
	// 필드 늘어나면 생성자 수정해야하니 생성자 주입법을 롬복 이용하도록 애노테이션 작성
	// 생성자 주입 받을 필드니까 final 키워드 필요
	
	private final Pagination pagination;

//	@Override
//	public int selectListCount() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public Map<String, Object> findAll(Long page) {
		
		Map<String, Object> map = new HashMap();
		List<BoardDTO> boards = new ArrayList();
		
		// 뭐할까용?? ==> 여기서 뭐할까용?? 유 효 성 검 증
		// url 요청 수정해서 잘못된 값 넘어오는 경우들 확인해줘야함
		if(page < 1) {
			
			// 장난을 쳤나보군, 이녀석 그러면 안된다 하고 알려줌
			// 억지로 예외를 발생시키면 좋겠다
			throw new InvalidArgumentsException("잘못된 접근입니다.");
			// 직접 이용해보고 따라해봐도 좋다. 아무것도 없는 상태에서는 여기 뭐넣을지 고민 엄청 해야겠지
			// 기존 사이트들을 많이 이용해보셔
			
		}
		
		// 페이징처리 객체 생성 -> BoardMapper도 스프링에게 주입받아서 써야함
		// BoardMapper를 필드에 선언하러 위에 작성
		// 메소드는 아직 없고 정수로 받아올것임 -> BoardMapper ㄱㄱ
		int count = boardMapper.selectTotalCount();
		log.info("총 게시글 개수 : {}", count);
		
		// Pagination Bean으로 등록했으니 필드 추가
		PageInfo pi = pagination.getPageInfo(count, page.intValue(), 5, 5);
		
		// 앞에서 offset, limit 계산해서 매퍼에 이거 전달하면서 OFFSET 문법 사용하고 맞는 페이지 게시글들만 조회했다
		// 해본거 또하면 노잼이니까 안해본거 또해보자
		// 마이바티스 공식문서 보면 RowBounds 객체 생성해서 offset, limit 전달하기
		// 총 조회된 개수가 없을수도 있다! 게시글 전체 삭제했거나 아무것도 등록안했거나 -> 굳이 가서 select 할필요도 없고 offset 어쩌고 할필요도 없음, 매퍼 갈필요도 없음
		// 게시글 있을때만 작업할거니까 조건 -> 있는지 없는지는 count로 판별
		if(count > 0) {
			
			RowBounds rb = new RowBounds((page.intValue() - 1) * 5, 5);
			// 마이바티스 객체 생성해서 offset, limit 값 전달
			
			// Mapper의 메소드 호출하면서 RowBounds 객체 전달
			boards = boardMapper.findAll(rb);
			// 돌아올거 받아주자
			
			// pi랑 boards 두개 담아줘야하는 상황임, 지금 반환타입이 잘못된거죠? 인터페이스 생각해서 짜고 개발했는데 사고가 난거죠
			// 처음 생각한거랑 달라지면 또 코드수정해야하고 그러다보면 배우고 는다!
			// 보통 선택지가 두개, 지금의 특징은 두개의 값을 담아야하는데 타입이 다름
			// 하나는 둘다 담을 수 있는 무언가(DTO)를 만들기 -> 필드 두개를 놓고 pi, List<BoardDTO> 설정 / 다른 하나는 Map
			// 타입이 다른 여러개 값을 담으려면 보통 list, set, map 세개
			// 컬렉션은 각 친구들이 특징이 있다, list는 순서가 보장되어야 하는 상황, set은 중복을 제거해야할 때, map은 값을 효율적으로 관리하고싶을때(key가 있으니까)
			// 지금은 map이 제일 괜찮은 선택지, 뭘 써도 잘 동작(담아서 앞으로 반환가능) -> 이런 상황에서 개발자들이 선호하는것은 이름 붙여놓는거
			// 그러고나면 DTO / Map 중에 고민하는것, 정답은 Map이고 이유는 두가지(본질은 같음)
			// DTO는 생산효율적 측면에서 시간이 오래걸린다는 단점, 나중에 코드 파악하는데 시간이 걸림(DTO 공부해야함)
			// 그래서 만들어서 사용하기보다는 기존의 것을 사용하는게 효율적, 자바개발자라면 누구나 맵을 알것이다, 키밸류 하려고 썼구나~
			// 근데 DTO 쓰면 안되는건 아님 -> 아무튼 메소드 초입에 맵 생성
			
		}
		
		// 만들어둔 맵에 pi, boards 담을건데, list가 맵에 못담는게 문제
		map.put("pi", pi);
		
		// 지금 if 스코프 벗어난 지역변수라 못담으니 선언을 바깥에서 해준다
		map.put("boards", boards);
		
		return map;
		// -> 인터페이스 수정
		
	}

	@Override
	public int save(BoardDTO board, MultipartFile upfile, HttpSession session) {
		
		// 1번 권한검증
		// board에서 boardWriter 가져오기
		String boardWriter = board.getBoardWriter();
		
		// session에서 loginMember 받아오기
		MemberDTO loginMember = ((MemberDTO)session.getAttribute("loginMember"));
		if(loginMember == null) {
			throw new NullPointerException("로그인해");
		}
		// 이거 좀 합리적으로 할수없나? 자바할때 두번이상 한건디?
		
		// loginMember에서 userId 가져오기
		String userId = loginMember.getUserId();
		
		// 비교 -> boardWriter != userId 이렇게 하면 안됨 주소값 비교가 된다
		// loginMember가 null이면 NPE가 일어날텐데 어디서? loginMember. <- 여기서 난다, 널을 가리킴! 널일 수 있는건 loginMember, 널을 가리키는 곳은 참조하는부분이겠지
		// NPE는 UE임, loginMember 널인지 아닌지 먼저 판별해줘야함 -> loginMember 받아오는곳 다음에서?
		if(!boardWriter.equals(userId)) {
			throw new AuthenticationException("권한 없는 접근입니다.");
		}
		
		return 0;
	}

	@Override
	public BoardDTO findByBoardNo(Long boardNo) {
		// TODO Auto-generated method stub
		return null;
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
