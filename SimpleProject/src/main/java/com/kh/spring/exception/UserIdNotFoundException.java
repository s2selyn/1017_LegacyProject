package com.kh.spring.exception;

public class UserIdNotFoundException extends RuntimeException {
	
	// ctrl + space 하면 생성가능, 기본생성자 없이 매개변수생성자만으로 작업
	// 생각은 꼼꼼히, 쓰는건 편하게
	public UserIdNotFoundException(String msg) {
		super(msg);
	}

}
