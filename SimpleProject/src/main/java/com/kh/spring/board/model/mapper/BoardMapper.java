package com.kh.spring.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.dto.BoardDTO;

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

}
