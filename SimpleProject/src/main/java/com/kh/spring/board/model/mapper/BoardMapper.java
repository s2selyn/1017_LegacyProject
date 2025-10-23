package com.kh.spring.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.dto.BoardDTO;
import com.kh.spring.board.model.dto.ReplyDTO;

@Mapper
public interface BoardMapper {
	
	// 어제 한건 @Select 애노테이션 달아서 SQL문 넣고 작업
	// 오늘은 이 인터페이스 쓸건데 SQL문은 board-mapper.xml에 쓰고, 이 친구를 호출하는 방법으로 해보자
	// 사용방법이 좀 다르다
	int selectTotalCount(); // 매퍼에 선언한 메소드 식별자를 실제 SQL문 작성할 mapper의 태그 id 속성값으로 만들어준다
	// 원래는 매퍼.xml 작업해놓고 sqlSession에 select해서 boardMapper.selectTotalCount이렇게 호출했음
	// 이제 sqlSession 직접 사용 안한다 -> 설정해줘야함, 매퍼.xml이 Mapper에서 부르는 것을 xml에 작성하겠다는 설정
	// 이건 다시 xml 수정
	
	List<BoardDTO> findAll(RowBounds rowBounds);
	// 매퍼는 이거 추상메소드로 끝, sqlSession, select머시기 이런거 읎어!
	
	int save(BoardDTO board);
	
	int increaseCount(Long boardNo);
	// 이건 성공실패 여부를 주고받아야한다
	
	BoardDTO findByBoardNo(Long boardNo);
	
	BoardDTO findBoardAndReply(Long boardNo);
	// 이것이 왜 마음에 안드는가? 이름도 네단어 조합인데 메소드가 기능을 두개하고있음, 하나씩 조회하느게 아니라 두개를 조회해온다
	// 서비스 구현체 보면 하나의 메소드가 하나의 작업만 수행할 수 있게 책임분리 해뒀다
	// 매퍼에서 두개 같이 가져가는게 재활용성 측면에서 별로다, 보드만 가져가야할일, 리플만 가져갈일이 생길 수 있으니 따로만들면 좋을텐데
	// 이렇게 생긴 메소드를 보면 하나의 메소드가 너무 많은 것을 하고있나? 생각, 재활용성측면에서는 별로, 자원사용측면에서는 이게좋을지도, 원래 두번 DB에 가야하는 논리적 물리적으로 멀리있는 곳에 연결시도하니 시간추가되고 레이턴시 발생
	// 연결하는 커넥션 객체 생성, 사용이 자원많이 소모, select 문 규칙도 세워야하니까(???) 자원소모 측면에서는 지금이 선호될수도, 팀장성향에 따라 달라질수도, 보편적으로는 재량에 맡긴다
	// SQL문은 신입개발자한테 짜라고 안한다, 기본적으로는 짜진걸 준다 돌려보라고
	// 스타트업 갈거 아니면 기존개발, 솔루션, sql문 등이 있을것이다, 이전에 회사나 팀이 개발했던 방식이 있을것이니 그걸 준다
	// 개발자가 만들지만 다 회사 소유지, 회사코드가 다 있음
	
	int insertReply(ReplyDTO reply); // 서비스 인터페이스

}
