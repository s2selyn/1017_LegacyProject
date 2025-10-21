package com.kh.spring.exception.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.exception.InvalidArgumentsException;
import com.kh.spring.exception.TooLargeValueException;
import com.kh.spring.exception.UserIdNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice // Global Exception Handler로 사용하기 위한 애노테이션, 예외처리 전문가로 만들어보자
public class ExceptionHandlingController {
	
	@ExceptionHandler(UserIdNotFoundException.class)
	protected ModelAndView idNotFoundError(UserIdNotFoundException e) {
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", e.getMessage()).setViewName("include/error_page");
		// 어차피 메세지는 받아오고, 응답페이지는 고정이니 반복임
		// 에러페이지 바뀌거나 파일명 바뀌면? 모든 핸들러 이부분 바뀌어야함, 중복코드 발생하면 유지보수 힘들다
		log.info("발생예외 : {}", e);
		return mv;
		
	}
	
	// 사용자 정의 예외 클래스 두개 만들었음
	// 첫번째는 너무 길어요 예외, 두번째는 InvalidArguments
	// 각각의 예외가 발생했을 때 이게 잘 잡는지 확인해보자
	@ExceptionHandler(TooLargeValueException.class)
	protected ModelAndView largeValueError(TooLargeValueException e) {
		// 접근제한자 protected, 메소드명은 자유
		
		// log.info("길이가 길면 정말 얘가 출동함??");
		// 이것도 잘 호출된다
		
		// log.info("{}", e.getMessage());
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", e.getMessage()).setViewName("include/error_page");
		return mv;
		
	}
	
	@ExceptionHandler(InvalidArgumentsException.class)
	protected ModelAndView invalidArgumentError(InvalidArgumentsException e) {
		
		// log.info("값이 없으면 정말 얘가 호출됨??");
		// 브라우저에서 억지로 예외 발생시켜보기(아이디 비밀번호에 공백문자 입력하고 요청) -> 얘가 호출된다!
		
		// log.info("{}", e.getMessage());
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", e.getMessage()).setViewName("include/error_page");
		return mv;
		
	}
	
	// 직접 try-catch 하나씩 작성하지 않고 여기서 처리할 수 있게 되었다!
	// 지금은 사용자가 예외가 일어났을 때 빈화면밖에 못본다, 서비스에서 throw 할 때 각 예외 발생 시 메세지를 넘기고 있는데?
	// 각 예외 핸들러에 예외타입의 매개변수 작성 -> e.getMessage 메소드를 통해서 전달된 값을 얻을 수 있음
	// 그럼 이제 이 핸들러들의 반환타입을 void -> ModelAndView로 변경
	// -> mv객체 생성하고 어제 만들어둔 에러 페이지, 메세지 스코프에 에러메세지를 넣어서 포워딩하게 만들어준다
	// ??? 10:18 컨트롤러에서 응답하는게 아니라 예외 클래스(여기)에서 응답 가능
	
	// 이러니까 Service단에서 반환타입이 void가 된다, controller로 1/0을 넘기지 않음
	// insert가 안되는 상황에서는 억지로 예외를 발생해서 해당 에러페이지로 보내면서 메세지 전달하므로
	// 컨트롤러는 오로지 성공시 응답화면으로만 포워딩 해주면된다
	
	// 스프링으로 넘어오면서 기존에 하던 작업들을 스프링이 많은 부분을 대신하게 된다
	// 개발자는 어떻게 예외처리를 해줄지, 올바른 값이 들어왔는지, 이걸로 insert 수행할 수 있는지 이런일들에 포커스

}
