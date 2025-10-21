package com.kh.spring.member.model.service;

import org.springframework.stereotype.Component;

import com.kh.spring.exception.AuthenticationException;
import com.kh.spring.exception.InvalidArgumentsException;
import com.kh.spring.exception.TooLargeValueException;
import com.kh.spring.member.model.dao.MemberMapper;
import com.kh.spring.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@Component // 컨트롤러, 서비스, DAO 아니니까
@RequiredArgsConstructor
public class MemberValidator {
	
	// final선언은 생성자 주입 받겠다는거고 그건 방법이 두개
	private final MemberMapper memberMapper;
	
	/*
	// 필드 생성될때마다 이게 계속 추가되어야함 -> 더 편하게 할 방법은 생성자 직접선언 대신 클래스 선언부에 롬복 라이브러리가 제공해주는 애노테이션 추가
	// 이게 정석임! 롬복 안쓰는 스프링 쓰는 개발자들은 다 이렇게 한다
	@Autowired
	public MemberValidator(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
		// 스프링이 생성자 호출할 때 주입할수 있도록 작성하고 애노테이션 작성
	}
	*/
	
	private void checkNull(MemberDTO member) {
		
		if(member == null) {
			throw new NullPointerException("잘못된 접근입니다.");
		}
		
	}
	
	private void checkLength(MemberDTO member) {
		
		if(member.getUserId().length() > 20) {
			throw new TooLargeValueException("아이디 값이 너무 길어용");
		}
		
	}
	
	private void checkBlank(MemberDTO member) {
		
		if(member.getUserId() == null ||
				   member.getUserId().trim().isEmpty() ||
				   member.getUserPwd() == null ||
				   member.getUserPwd().trim().isEmpty()) {
					
					throw new InvalidArgumentsException("유효하지 않는 값입니다.");
					
				}
		
		// 이것도 개발자가 해야하는건데 부트가면 스타터에 내장되어있어서 딸깍하고 애노테이션 추가하면 쓸 수 있음
		
	}

	// 서비스에서 넘긴 값으로 유효성 검증할거니까 받아서 return해줘야함
	public void validatedMember(MemberDTO member) {
		
		// 서비스에 있던 검증코드 복사해오기 -> 예외발생하면 예외핸들러가 잡아서 처리해줄것이다!
		/*
		if(member == null) {
			throw new NullPointerException("잘못된 접근입니다.");
		}
		*/
		checkNull(member);
		
		/*
		if(member.getUserId().length() > 20) {
			throw new TooLargeValueException("아이디 값이 너무 길어용");
		}
		*/
		checkLength(member);
		
		/*
		if(member.getUserId() == null ||
		   member.getUserId().trim().isEmpty() ||
		   member.getUserPwd() == null ||
		   member.getUserPwd().trim().isEmpty()) {
			
			throw new InvalidArgumentsException("유효하지 않는 값입니다.");
			
		}
		*/
		checkBlank(member);
		
		// 비밀번호를 체크하는 뭔가가 필요하다면 이 내부에서 하는게 아니라, 클래스 내부에서 메소드를 생성하고
		// 이 내부에서는 메소드 호출만 한다
		// 그리고 나면 서비스 입장에서도 직접 호출하는게 아니라서 여기만 수정해도 서비스는 모름
		// 의존성, 결합도 낮추기 가능!
		// 클래스간의 아는 내용이 줄어들수록 유지보수가 좋아진다
		// 이렇게 해두면 재활용가능, update, delete 할때 불러서 사용가능
		
	}
	// 복사해놓고보니 작업도 세개나 하고있고 public이라 직접 접근해서 사용하고 있음
	// 검증 생기면 또 변동 생기겠지? 이것은 또 좋지않다, 하나의 메소드는 하나의 기능만 수행해야해!
	// 위쪽에 메소드로 분리
	
	public void validatedUpdateMember(MemberDTO member, MemberDTO sessionMember) {
		
		// 널체크 메소드 만들어뒀으니 재활용
		checkNull(member);
		checkNull(sessionMember);
		
		// member에 담긴 id와 현재 session에 담긴 id도 비교해야함
		// 딴데 뺄 필요가 없을듯? 걍 여기서 작업
		if(!member.getUserId().equals(sessionMember.getUserId())) {
			
			// 두개 비교해서 다르다면 -> ! -> 다른 친구가 들어왔군!
			// 우리가 의도한 상황이 아니므로 억지로 예외 발생시키기
			throw new AuthenticationException("권한없는 접근입니다.");
			// 만들러 다녀와야함
			// 인증이 있고 인가가 있음
			// 인증은 나라는거 인증, 로그인이 우리에게는 인증과정
			// 인가는 권한이 있나없나를 체크, 공지사항은 관리자만 쓸수있어야한다, 일반사용자의 공지사항 작성은 인가해주면 안되겠지? 그게 인가
			// 인증과 인가는 다른것, 영어로는 authentication / authorization
			
			// 잡아서 처리해야하니까 ExceptionHandlingController도 다녀오기
			
		}
		
		// 2를 생략하려고 했으나 로그인한 상태인데 그사이에 탈퇴시켜버렸다면 확인해야함 -> MemberValidator 작업
		// id만 있으면 select해서 있는지 없는지 확인하는 메소드가 있음 -> login 호출
		// 서비스 가는 방법도 있는데 컨트롤러에서 이미 쓰고있어서 또가긴 좀
		// 실질적으로 바로 가는 녀석이 있음 -> MemberMapper의 로그인 메소드 호출
		// 여기 매퍼 없엉, 스프링에게 매퍼 주입받아서 써야함 -> 주입받고싶으면 필드부에 final 붙여서 선언
		checkNull(memberMapper.login(member));
		// 길이체크도 하면 좋겠지만 if문쓰고 length 어쩌고 근데 그냥 넘어감
		// id를 체크하고 이 과정을 하니까 뭘 넘겨도 상관없음 -> 서비스로 돌아감
		
	}

}
