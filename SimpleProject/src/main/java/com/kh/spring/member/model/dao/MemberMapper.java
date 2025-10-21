package com.kh.spring.member.model.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.spring.member.model.dto.MemberDTO;

/*
 * 컨트롤러, 서비스 애노테이션이 달려있었음
 * 여기도 DAO 애노테이션이 필요하겠지? -> @Mapper로 작성한다
 * 
 * 지금의 member-mapper.xml은 단순한 상태, resultMap 만들 필요도 없고 select 태그로 중복 뺄 필요도 없고
 * 이 이후로도 크게 복잡하지 않을 update, delete
 * 그런 경우에 이런식으로 작업하면 편함
 * 
 * 인터페이스니까 안에 들어갈 수 있는것은 추상메소드뿐
 * 
 */
@Mapper
public interface MemberMapper {
	
	@Select("SELECT USER_ID userId, USER_PWD userPwd, USER_NAME userName, EMAIL, ENROLL_DATE enrollDate FROM MEMBER WHERE USER_ID = #{userId}")
	MemberDTO login(MemberDTO member);
	// 이렇게 작성하고 SELECT 문 실행할것이므로 애노테이션 추가
	
	@Insert("INSERT INTO MEMBER VALUES (#{userId}, #{userPwd}, #{userName}, #{email}, SYSDATE)")
	int signup(MemberDTO member);
	
	// resultSet 필요하고 복잡하면 매퍼파일 xml로 만들어야한다, 이런 간단한것만 하는게 좋아
	// 가독성 떨어지긴 하는데 string 블록 만들어서 해도 되고
	@Update("UPDATE MEMBER SET USER_NAME = #{userName}, EMAIL = #{email} WHERE USER_ID = #{userId}")
	int update(MemberDTO member);
	
	// 마이바티스 써서 DAO 간단간단, xml 파일이 없는것도 가능해짐
	// 마이바티스 공식문서 가면 매퍼 애노테이션 사용하면 겁나 죠습니다 하고 써있음
	// 이건 마이바티스 3버전부터 제공되는 애노테이션이라 2버전 이하 쓰면 이것도 못쓰긴함
	// 구닥다리는 좋지않은표현일수도 있지, 회사전체의 생산성을 생각하면 효율성이 좋을 수 있다, 구닥다리가 안좋다는게 아니고 오래된 기술이라는 단순한 의미
	// 나쁜건 없어 기술 선택에는 항상 이유가 있어
	
	@Delete("DELETE FROM MEMBER WHERE USER_ID = #{userId}")
	int delete(String userId);

}
