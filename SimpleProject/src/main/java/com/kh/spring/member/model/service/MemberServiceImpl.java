package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberRepository;
import com.kh.spring.member.model.dto.MemberDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	// 스프링이 관리하는 Bean이므로 주입받아야함 -> 방법 3개정도 있음
	// 일단 쉬운거 -> @Autowired 추가
	// 옛날 아저씨들 쓰는 방식, getSqlSession도 안해도 되고 close도 얘가 알아서 해주니 안해줘도됨
	// 그러고나서 해야할일은 DAO호출인데 DAO가 없으니 생성ㄱㄱ
	
	// 서비스에서도 DAO를 주입받아서 써야하니까 필드로 둬야하고 간단한 작업으로 구현
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public MemberDTO login(MemberDTO member) {
		// log.info("나 불렀어?");
		
		// 여기서 할일은 SqlSession생성, DAO에 SqlSession 넘겨주기
		// 값 받아서 컨트롤러에게 돌려주기
		/*
		 * 원래 하던거
		 * SqlSession session = Template.getSqlSession();
		 * MemberDTO loginMember = new MemberDAO().login(sqlSession, member);
		 * sqlSession.close();
		 * return loginMember;
		 * 
		 * 지금 SqlSession을 Bean에 등록해뒀다, root-context.xml에서 확인됨, 스프링이 관리하는 친구니까
		 * 스프링님 SqlSession을 받아서 쓰고싶사옵니다 하고 해둬야할 작업 -> 필드로 선언
		 *  
		 */
		
		return memberRepository.login(sqlSession, member);
		
	}

	@Override
	public void signUp(MemberDTO member) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MemberDTO member) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(MemberDTO member) {
		// TODO Auto-generated method stub

	}

}
