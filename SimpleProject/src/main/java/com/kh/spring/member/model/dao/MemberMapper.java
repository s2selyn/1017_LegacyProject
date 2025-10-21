package com.kh.spring.member.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

}
