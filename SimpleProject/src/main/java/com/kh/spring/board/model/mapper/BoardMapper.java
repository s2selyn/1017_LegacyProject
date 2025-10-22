package com.kh.spring.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.dto.BoardDTO;

@Mapper
public interface BoardMapper {
	
	// 어제 한건 @Select 애노테이션 달아서 작업
	// 오늘은 이 인터페이스 쓸건데 board-mapper.xml도 같이 이용하게 해보자
	// 사용방법이 좀 다르다
	int selectTotalCount(); // 매퍼에 선언한 메소드명을 mapper의 태그 id값으로 만들어준다
	// 이제 sqlSession 직접 안한다
	
	List<BoardDTO> findAll(RowBounds rowBounds);
	// 매퍼는 이거 추상메소드로 끝, sqlSession, select 이런거 읎어!

}
