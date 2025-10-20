package com.kh.spring.exception;

// 예외 클래스 만들 때 중요한것 -> Checked or Unchecked
// 값이 뭐가 들어올 지 알 수 있나? 이건 값이 뭔지에 따라 일어날수도있고 안일어날수도 있는 예외임
// 실행중에 발생하는 예외임 -> Unchecked Exception, 돌려봐야, 값을 입력해봐야 안다
// 예외를 기존 예외클래스 상속받아서 구현해야하는데 Unchecked는 전부 RuntimeException의 자식(후손)임
public class TooLargeValueException extends RuntimeException {
	// The serializable class TooLargeValueException does not declare a static final serialVersionUID field of type long
	// 버전관리 하라는 뜻인데 안할거임
	
	public TooLargeValueException(String message) {
		// 사용자가 20자 넘는 값을 입력했다면 이 예외를 발생시킬 것이다
		// 예외발생은 어떻게 하는지? -> 다시 서비스로 감
		super(message);
	}

}
