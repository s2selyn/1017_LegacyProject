package com.kh.spring.member.model.service;

import org.springframework.stereotype.Component;

import com.kh.spring.exception.InvalidArgumentsException;
import com.kh.spring.exception.TooLargeValueException;
import com.kh.spring.member.model.dto.MemberDTO;

@Component // 컨트롤러, 서비스, DAO 아니니까
public class MemberValidator {
	
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

}
