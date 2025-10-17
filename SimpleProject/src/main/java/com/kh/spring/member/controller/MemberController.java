package com.kh.spring.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.member.model.vo.Member;

@Controller
public class MemberController {
	
	// 서블릿을 더이상 만들지 않지만, 서블릿이 사라진것은 아님
	// 서버 구동하면 web.xml을 읽는다
	// console에 서버 구동하면 보이는 DispatcherServlet <- 얘가 본부장, 커맨드센터, 진두지휘하는녀석
	// 이건 어디서 튀어나왔나? web.xml을 읽는다 -> 가서 보면됨
	// listener를 올림
	// processes application requests 주석 아래에 servlet 태그가 있음
	// 이거보면 org.springframework.web.servlet.DispatcherServlet 풀클래스명이니 이게 클래스인거 알수있음
	// try-catch, if-else 등등 뿐 객체 올리면서 필드값 넣음
	// 궁극적으로 서블릿임, DispatcherServlet은 HttpServlet을 상속받아 시작됨, 다이나믹 웹프로젝트랑 똑같음
	// doGet, doPost 다 들고있다, 메소드가 좀 많긴함 delete, put, patch?fetch? 이런건 부트가서 얘기할거지만 아무튼 궁극적으로 서블릿
	// 경고 뜨지말라는 애노테이션도 있다
	/*
	 * 크롬(브라우저)에서 요청을 보내면 누가 받나요? 톰캣이 제일 먼저
	 * 톰캣 안에 스프링 애플리케이션이 있다
	 * 스프링 프레임워크는 많은 모듈의 집합 어마어마하게 매우 많은 모듈의 집합
	 * 작동할 때 실질적으로 안에서 와랄랄라 많은 녀석들이 돌아감
	 * (DB관련작업, jdbc, mybatis 담당하는애들 / web관련된 mvc 요청받고 뭐시기하는애들 / bean으로 등록한것들은 core container안의 bean container안에 들어감)
	 * 
	 * 웹으로만 이야기하자, 요청이 오면 스프링 애플리케이션으로 왔다고 치자, 서블릿이 요청을 받을것임
	 * 서블릿은 원래 우리가 만들 때 어떤 매핑값으로 온 요청을 받을지 서블릿에 작성했음
	 * 웹 서블릿 애노테이션으로 작성
	 * 
	 * 스프링은 무슨 요청이 들어오든지 반드시 제일 먼저 DispatcherServlet이라는 녀석이 모든 요청을 받음(어떤 매핑값, 어떤 요청이든 상관없이 무조건!)
	 * 왜 얘부터 요청을 받느냐? 프로젝트 설정하는 web.xml에서 만들어낸다, 이것도 매핑값이 있어야하는데 이건 아래에 태그(servlet-mapping)에 적혀있음
	 * / <- 라고 적혀있다, appServlet이 DipatcherServlet 객체를 의미함, /로 시작하는 모든 요청처리를 진행하도록 되어있는거죠
	 * startup 태그에 1 적혀있음, 보편적으로 추가하지 않는다, 제일먼저 하겠다는 의미로 1이 부여된것
	 * 
	 */
	
	// MemberController를 서블릿이 부르도록 해야하는데 언제 불려서 언제 메모리에 올라갈지 모름, 이것도 스프링이 관리하게 해야함
	// 스프링이 관리하려면 얘도 Bean으로 등록해야함, bean 태그 작성해야겠지? 근데 구닥다리방식
	// 다른방법으로 해보자 보편적으로 컨트롤러를 빈으로 등록하기 위해서 사용하는 방식 -> 애노테이션, 클래스 선언부 위에 작성하러감(import도 해야함)
	// 이러면 Spring Explorer에 등록되어버림 확인가능, 컨트롤러로 사용할 객체라고 구체화시켜서 등록됨
	
	// 컨트롤러는 무슨 작업? 하는 일? 값뽑기 데이터 가공하기 요청처리하기 응답화면지정하기
	// 서블릿 안하고 여기서 메소드 늘려서 작업하기로 했음
	// 요청을 DispatcherServlet이 처리하는데 어떤 메소드가 기능을 처리할 지 구분해야함, 그래야 호출해서 요청처리
	// 그걸 위해 각 메소드 위에 또 애노테이션 작성
	/*
	@RequestMapping("login")
	public void test1() {
		System.out.println("login");
	}
	
	@RequestMapping("insert")
	public void test2() {
		System.out.println("insert");
	}
	
	@RequestMapping("delete")
	public void test3() {
		System.out.println("delete");
	}
	*/
	
	// RequestMapping도 결국 찾을 수 있도록 등록하는 과정임
	// 브라우저에서 매핑값으로 url로 요청 보내면 이 메소드 호출돼서 콘솔에 출력성공
	
	/*
	 * 스프링 애플리케이션에서 제일ㅇ 처음 요청받는건 DispatcherServlet -> 각각의 메소드를 요청처리기(RequestHandler)라고 표현
	 * DispatcherServlet 입장에서는 각 메소드 호출해서 요청을 넘겨줘야함
	 * 그건 요청 처리하는건지 아는지 뭘보고 컨트롤러인지 메소드인지 아는거지?
	 * 이걸 이해하려면 다시 web.xml로 돌아가야함
	 * 
	 */
	
	@RequestMapping("login")
	public void login(Member member) {
		
		// 1. 값 뽑기
		// 2. 데이터 가공
		System.out.println(member);
		// 이거 원래 우리가 직접 했는데 스프링이 해준다
		
	}

}
