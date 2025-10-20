package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.dto.MemberDTO;

@Repository // DAO를 Bean으로 등록하기 위한 애노테이션
public class MemberRepository {
	
	public MemberDTO login(SqlSessionTemplate sqlSession, MemberDTO member) {
		
		// ver 0.1 간단하게 하고 회원가입 구현하고 고치자
		return sqlSession.selectOne("memberMapper.login", member);
		// SqlSessionTemplate는 SqlSession을 상속받으므로 똑같이 작성해도 된다
		// 원래 하던 방식으로 먼저 해봄
		
		// Repository는 저장소, 이 애노테이션은 DAO들을 Bean으로 등록하기 위한것
		
		// mapper 없으니 생성
		
	}

}
