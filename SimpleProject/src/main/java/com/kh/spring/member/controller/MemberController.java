package com.kh.spring.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.member.model.dto.MemberDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j // loggin, log -> 기록 남기는것, 롬복 설치해야 사용 가능, 롬복이 제공함
@Controller
public class MemberController {
	
	/*
	public MemberController() {
		log.info("하이 난 빈임");
	}
	*/
	
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
	
	/*
	@RequestMapping("login")
	public void login(Member member) {
		
		// 1. 값 뽑기
		// 2. 데이터 가공
		System.out.println(member);
		// 이거 원래 우리가 직접 했는데 스프링이 해준다
		
	}
	*/
	
	/*
	// 어떤 매핑값으로 오는 요청을 처리할 건지 애노테이션 작성
	@RequestMapping("login")
	public String login(HttpServletRequest request) {
		
		// 방법 1. HttpServletRequest 객체를 사용하는 방법 -> request.getParameter();
		// 잘 사용되지 않음, 앞에것과 똑같이 할 수 있다고 알려주신것
		// 원래 서블릿 생성하고 매개변수자리에 이거 작성해서 doGet, doPost 메소드 작업 했음
		// 그렇게 앞에서 했던것처럼 가능
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		System.out.printf("id : %s , pw : %s", userId, userPwd);
		
		return "main";
		// 메소드 반환타입 void -> String으로 수정
		
		// 브라우저에서 localhost/spring/login으로 요청 보내면 콘솔에 출력된다
		// 앞에서 인자값 넘긴대로 잘 가져와짐
		
	}
	*/
	
	/*
	@RequestMapping("login")
	public String login(@RequestParam(value="userId", defaultValue="fffff") String id,
						@RequestParam(value="userPwd") String pwd) {
		
		// 방법 2. RequestParam 애노테이션 사용 -> request.getParameter(); 생각하기
		// 사용자가 입력한 값 두개를 받아서 써야함
		// 매개변수 자리에 받을 값으로 두개를 작성
		System.out.printf("이렇게 하면 될까요?? id : %s, pwd : %s,", id, pwd);
		
		return "main";
		// null, null이 나온다
		// userId -> id, userPwd -> pwd 이렇게 담고싶다
		// 변수 앞에 애노테이션을 달아준다(자료형 앞에 @RequestParam) -> 요청 파라미터 줄인걸로 생각(request.getParameter 줄인느낌)
		// 넘어올 값이 계속 늘어날 수 있는데? 어떤 값이 넘어오는지? 그러면 애노테이션 다음에 또 추가
		// Param은 key-value 세트로 넘어옴, 어떤 key값으로 넘어올지를 value 속성으로 추가
		
		// 좋은 점은 속성중에 defaultValue를 추가할 수 있음
		// 만약에 앞단에서 값이 넘어오는데 userId라는 key값이 안남어오는 경우가 있을 수 있음 앞단이 어떻게 만들어졌는지에 따라
		// 딱히 넘어온게 없는 경우, 빈문자열일 경우 -> 적어둔 디폴트값이 변수에 담긴다 -> 이걸 활용가능해서 편리하고 좋다
		
	}
	*/
	
	/*
	@RequestMapping("login")
	public String login(@RequestParam(value="userId") String userId,
						@RequestParam(value="userPwd") String userPwd) {
		
		// 방법 3. @RequestParam 생략 가능한 경우
		System.out.println("으흐흫ㅎㅎㅎ id : " + userId + ", pwd : " + userPwd);
		
		return "main";
		// 이번엔 앞단의 값이 잘 넘어온다
		// 요청처리기에 매개변수를 작성해뒀는데, 매개변수의 이름이 앞단의 key값과 일치함
		// 이러면 스프링이 앞에다가 자동으로 알아서 @RequestParam 애노테이션을 붙여준다 -> @RequestParam(value="userId")
		// 같은 이름으로 매개변수를 작성해주면 RequestParam을 생략할 수 있음
		
	}
	*/
	
	// 결국 앞단에서 넘어온 변수를 가공해야함, 마이바티스로 넘기려면 하나에 담아서 가야하니까
	// 그래서 좋은 방법이지만 지난주 금요일에 쓴걸 한번 해보자, 최종적으로 데이터 가공한 다음에 서비스 호출해서 넘겨야하니까
	// 방법 4.
	// DBeaver 수정작업, DTO 생성
	
	@RequestMapping("login")
	public String login(MemberDTO member) {
		
		// System.out.println("로그인 시 입력한 정보 : " + member);
		// 롬복에 의해 값이 추가되어서 toString 출력됨
		
		// 별로 마음에 안드는점? 초보개발자라서 콘솔에 뭘 출력해봐야한다, 확인하면서 개발하는게 좋아요
		// 근데 시스템 클래스를 이용해서 출력하는것이 자원을 많이 쓰는 작업임, 자바 자체 API로 불러내는데 자원을 많이 씀
		// 문자열로 출력해서 확인하니까 + 기호 써서 하는경우가 많은데 String + String은 불변이라 기존 스트링이 쓰이는게 아니라 다른 스트링이 새로 생성되니 메모리 낭비.. 마음에 안들어서 printf 썼지만 자원을 아낄 조금 더 좋은 방법이 있다
		// 결과, 값을 확인할 때 출력해보는데... 출력할때도 사용하는 라이브러리가 많다.
		// 스프링 레거시, mvc에는 logging 라이브러리가 들어있음, 이것도 객체로 만들어서 해봐야하는데, 롬복을 배우면 출력도 쉬워짐
		// 맨위로 올라가서 컨트롤러 애노테이션 위에 작업 -> @Slf4j 추가하고 import
		log.info("Member객체 필드값 확인 ~ {}", member);
		// printf 메소드처럼 구멍뚫기 -> {}
		// 어디에서 출력한건지도 같이 출력된다
		// INFO : com.kh.spring.member.controller.MemberController - Member객체 필드값 확인 ~ MemberDTO(userId=thisiskey, userPwd=thisispassword, userName=null, email=null, enrollDate=null)
		
		// 진짜진짜 가보자고
		/*
		 * 다양한 방법으로 앞단에서 넘어온 핸들러에서 값을 뽑음
		 * 옛날 다이나믹 프로젝트, getParam, DTO에서 아무것도 안적고 가져오는 방법
		 * 스프링은 어떻게 알고 이 값을 넣어주는거지? 뭐가필요한지 어떻게 알아?
		 * 
		 */
		
		return "main";
		
	}
	
}
