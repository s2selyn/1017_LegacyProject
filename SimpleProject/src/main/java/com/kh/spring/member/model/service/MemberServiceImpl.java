package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.exception.TooLargeValueException;
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
		
		// 반환형 void이므로
		// 꼼꼼하게 검증
		// 유효값 검증
		
		// 일단 넘어온 member가 null(객체가 만들어지지않음)이면 넘어갈 필요도 없고 유효값 체크할 필요도 없음
		if(member == null) {
			
			// null이면 id값 체크고뭐고 아무것도 할필요가 없음
			return;
			
		}
		
		// null이 아니었다는것은 객체가 들어왔다는 뜻
		// 그럼 이제 필드값에 대한 검증이 있어야함
		// 일단 정규표현식 쓰면 좀 쉬워지니까 일부러 복잡하게 해보자
		// 예를들어서 id값이 20자가 넘으면 안된다고 가정
		// 하나씩 비교해보자(String으로 정규표현식 쓰면 쉬움)
		// member를 참조해서 getUserId를 또 참조해서 length호출한것으로 비교
		if(member.getUserId().length() > 20) {
			throw new TooLargeValueException("아이디 값이 너무 길어용");
			// throw 하고 우리가 만든 예외 클래스를 객체로 생성해줌
			// 사용자는 예외 일어난줄 몰라야함 -> 모든 것을 예외처리 해줘야하는데(try-catch) 더욱 스마트한 처리방법이 있음
		}
		
		// 길이만 보는게 아니라 테이블도 생각해야함, pk, not null
		// 무조건 id, pwd, name 컬럼에는 값이 있어야함
		if(member.getUserId() == null ||
		   member.getUserId().trim().isEmpty() ||
		   member.getUserPwd() == null ||
		   member.getUserPwd().trim().isEmpty()) {
			return;
		}
		
		// 지금 중복체크 구현안하고 있으니 사용자가 입력한 아이디가 DB에 존재하는지 아닌지도 비교해야함
		// 이런 식으로 작업하면 나중에 사용자에게 알려줄 수 있나? 아이디 없어, 입력안했어 이런건 리턴만 해서는 알려줄 수 없음
		// 이런걸 세분화해서 알려줄 수 있으면 좋겠다 -> 또 새로운거 배워보자
		// 지금은 조건 만족 못하면 그냥 리턴해버리는 식으로 작업함
		// 이제 리턴하지 말고 예외를 발생시켜보자 -> 내가 작성한 내용에 만족하지 못한다면 예외를 발생시켜서
		// id가 20자가 넘었다면? 20자가 넘을 때 발생할 예외를 발생시키자
		// 근데 그런 예외가 있나? 없는 것 같은데요... 우리가 직접 예외 클래스를 만들어보자! -> 만들고 와서 20자 넘는 곳 검증하는 곳에서 예외발생 코드작성

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
