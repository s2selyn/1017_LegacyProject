package com.kh.spring.exception.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kh.spring.exception.TooLargeValueException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice // Global Exception Handler로 사용하기 위한 애노테이션, 예외처리 전문가로 만들어보자
public class ExceptionHandlingController {
	
	// 사용자 정의 예외 클래스 두개 만들었음
	// 첫번째는 너무 길어요 예외, 두번째는 InvalidArguments
	// 각각의 예외가 발생했을 때 이게 잘 잡는지 확인해보자
	@ExceptionHandler(TooLargeValueException.class)
	protected void largeValueError() {
		// 접근제한자 protected, 메소드명은 자유
		
		log.info("길이가 길면 정말 얘가 출동함??");
		
	}

}
