package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.dto.MemberDTO;
import com.kh.spring.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j // loggin, log -> 기록 남기는것, 롬복 설치해야 사용 가능, 롬복이 제공함
@Controller
// @RequestMapping("/member")
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
	// 방법 4. 커맨드 객체 방식
	// DBeaver 수정작업, DTO 생성
	
	/*
	 * HandlerAdapter의 판단 방법 :
	 * 
	 * 1. 매개변수 자리에 기본타입(int, boolean, String, Date...)이 있거나
	 * 	  RequestParam애노테이션이 존재하는 경우 == RequestParam으로 인식
	 * 
	 * 2. 매개변수 자리에 사용자 정의 클래스(MemberDTO, Board, Reply..)이 있거나
	 *    @ModelAttribute애노테이션이 존재하는 경우 == 커맨드 객체 방식으로 인식
	 * 
	 * 커맨드 객체 방식
	 * 
	 * 스프링에서 해당 객체를 기본생성자를 이용해서 생성한 후 내부적으로 setter메서드를 찾아서
	 * 요청 시 전달값을 해당 필드에 대입해줌
	 * 
	 * 1. 매개변수 자료형에 반드시 기본생성자가 존재할 것
	 * 2. 전달되는 키 값과 객체의 필드명이 동일할 것
	 * 3. setter메서드가 반드시 존재할 것
	 * 
	 * 지금은 로그인 매핑으로 요청이 들어온다
	 * DS -> HM -> Bean으로 등록해두었으니 RequestMapping으로 login 호출해야한다고 판단 -> 직접 호출하지 않고 HA를 거쳐서 HA가 판별하도록 함 -> 매개변수 판별 후 key-value를 보고, 기본생성자 호출, setter 호출 -> 각 필드에 값을 넣어준다
	 * ??? 11:15
	 * 
	 */
	/*
	 * 원래 하던건 private MemberService memberService = new MemberService();
	 * 
	 * -> 이게 무슨 작업? 변수를 선언하고 객체를 생성하는 과정
	 * 근데 이거 이제 스프링한테 위임하고 스프링이 제어하기로 했음
	 * private MemberService memberService 이까진 우리가 하더라도 = new MemberService(); 이건 우리가 안할듯
	 */
	
	/*
	dependency injection 방식 1
	@Autowired == 필드 인젝션 field injection, 스프링이 알아서 주입, 옛날 개발자들이 편하게 사용하던 방식
	private MemberService memberService;
	작업속도는 빠르다
	*/
	
	// DI 방식 2
	/*
	@Autowired == 세터 인젝션
	public void setMemberService() {
		this.memberService = memberService;
	}
	1, 2 둘다안씀
	*/
	
	private final MemberService memberService;
	
	// 권장방식은 생성자를 통한것
	@Autowired /* ☆ 권장 방법 ★ */
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	// ??? 12:23 DI 방식 두개의 문제점
	// ??? final이 붙은 변수는 선언과 동시에 초기화를 진행해야함
	// 객체의 불변성도 지키고 중간에 다른 객체로 변환될 걱정도 없어짐
	
	/*
	@RequestMapping("login")
	public String login(MemberDTO member,
						// HttpServletRequest request
						HttpSession session,
						Model model) {
		// (@ModelAttribute MemberDTO member)
		
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
		 *
		 * 다양한 방법으로 앞단에서 넘어온 핸들러에서 값을 뽑음
		 * 옛날 다이나믹 프로젝트, getParam, DTO에서 아무것도 안적고 가져오는 방법
		 * 스프링은 어떻게 알고 이 값을 넣어주는거지? 뭐가필요한지 어떻게 알아? -> 노션확인
		 * 
		 * 커맨드 객체 방식 -> @ModelAttribute라고 작성
		 * (아까 @RequestParam 생략한것처럼 @ModelAttribute를 생략할 수 있음)
		 * 
		 * 핸들러의 매개변수 타입은 개발자가 작성하니까 어떻게 될 지 알 수 없음
		 * 값을 가공할 때 보통 MemberDTO member 이런 식으로 적는 경우가 많겠지?
		 * 
		 * -> HandlerAdaptor의 판단 방법 주석 확인
		 * 
		 *
		
		// 우리는 이제 member 객체를 가지고 뭘 해야함? 로그인이니까 DB 서버 가서 사용자가 입력한 ID와 같은 아이디 값이 있는지 memberId 컬럼 보고, 사용자가 입력한 pwd와 같은 pwd값이 있는지 memberpwd 컬럼 보고
		// 일치하는게 있다면 조회 결과를 가져와서 컨트롤러까지 들고온 다음 로그인된 사용자 정보를 세션스코프에 setAttribute 등으로 추가
		// 일치하는게 없다면 알려줘야지 뭐 이상하다든지 무슨 방법을 통해서...
		// 일단 그걸 하려면 뭘 적어야하나요? 여기서 직접 하지않으니까 요청처리 하기위해서 Service 불러서 뭐 했었음
		
		// MemberService 호출하면 됨
		// memberService.login(member);
		
		// 나중에 요청을 처리할 서비스가 바뀐다면? 애노테이션 삭제해서 Bean에서 빼버리면 가능
		// 원래라면 서비스가 삭제되고 다른 서비스가 생겼으니 컨트롤러 코드를 바뀌어야함
		// Bean으로 구현하면 서비스가 바뀌었다고 컨트롤러가 바뀌지 않게 된다, 의존성을 낮추고 붙어있던걸 쪼개버림
		// 1. 컨트롤러 입장에서 실제로 호출할 친구를 구체적으로 만들어서 사용하지 않음
		// 두 종류의 클래스(기존 서비스, 새 서비스)를 묶을 수 있는 자료형(서비스가 구현할 인터페이스)만 사용
		// 두 클래스는 interface의 추상메소드를 이용해서 구현
		// 2. 실제 객체를 우리가 만들지 않고 Bean으로 등록하고 스프링이 알아서 등록하도록 애노테이션 추가작업만 했음(@Autowired)
		// wire는 줄, 이어주는것, 알아서 wiring하는 과정이라서 Autowiring
		// Bean으로 등록해놨으니 스프링이 관리할거고, MemberService가 필요한 시점에 자기가 가진 Bean들 중에서 이 타입에 맞는 객체를 대입해준것 = 이걸 와이어링한다고 표현, 그걸 위해 필요한 애노테이션이 @Autowired
		// -> 이걸 Dependency Injection, 의존성 주입이라고 표현
		// 실제로 서비스를 의존하고 있었던 기존작업코드, 직접 객체를 생성해서 의존, 타입도 쓰고 객체도 만들었다
		// 스프링에서는 실제로 사용할 서비스의 타입도 숨기고, 객체 생성도 스프링에게 위임함
		// 필요한 객체는 스프링에게 받아서 사용, 스프링이 알아서 주입해줌
		// 이렇게 컨트롤러가 서비스에 의존하고 있던 의존도가 낮아짐, 이게 의존성 주입
		// 실제 사용할 객체를 개발자가 생성하는것이 아니라 스프링으로부터 주입받아서 사용하는 개념
		
		// 서비스에서 정해둔 자료형으로 돌려받을것이 정해져있음
		MemberDTO loginMember = memberService.login(member);
		
		
		if(loginMember != null) {
			
			log.info("로그인 성공");
			// 앞(header.jsp)에서 넘기는 name 속성값을 가공할 타입의 필드명과 똑같이 쓰면 request handler에서 매개변수에 넣을 때 setter로 알아서 스프링에서 넣어준다
			// request.getParameter 해서 뽑고 객체 만들고 setter로 값 넣는 작업을 안해도 되니 그거 날아감
			// 서비스에서도 마찬가지로 Template 안만들고 알아서 만들고 알아서 반납하라고 스프링한테 맡김
			// 그리고 받아와서 메소드 호출만 하고 끝남
			// DAO도 마찬가지로 맨날 하던 거 똑같이 함, 자원반납 알아서 하니 그냥 반환했으니 다녀오기만 하면 끝
			
			// 받았으니까 응답화면 지정해줘야하는데? 안했는데? 하고나면 그냥 무슨 화면이 나옴
			// 이거하려면 원래 RequestDispatcher 객체 생성해서 포워딩 해야하는데
			// 그거안하고 그냥 return "main"; 했는데 우리 메인화면 뜸...!
			// 얘가 알아서 메인을 띄울까? 아닌데용ㅎ -> 노션 확인
			
		} else {
			log.info("실패");
		}
		
		
		// 성공하면 우리는 로그인된 사용자의 정보를 세션 스코프에 담는다
		if(loginMember != null) { // 로그인에 성공
			
			// sessionScope에 로그인된 사용자의 정보를 담아줌
			// HttpSession 타입 객체가 필요함, 브라우저마다 하나씩 생기니까 HttpServletRequest 타입 객체로 getSession 메소드 호출해서 받음
			// 지금 Request 객체가 없음, 어떻게 담아유? 어차피 넘기는건 DispatcherServlet이 넘기는데
			// 요청 할때마다 request, response 생김, DS한테 get하면 알아서 adaptor에서 만들어서 넘겨줌, 오전에 해본것임
			// 필요하면 매개변수에 HttpServletRequest 작성하면 됨 -> 메소드 시그니처에 추가
			
			// HttpSession session = request.getSession(); 해야하는데 이렇게 안하고
			// 만능은 아님, 143개 정도 된다, session 필요하면 필요하다고 매개변수 자료형에 적어주면 받아올 수 있음
			// 그리고 그냥 담아
			session.setAttribute("loginMember", loginMember);
			// 포워딩 할 수 있는데 그러면 요청 url이 그대로 남는다, localhost/spring/login 이렇게
			// 사용자가 실수로 새로고침 누르면? 로그인요청이 다시 가게된다
			// 지금처럼 세션에 데이터를 담고 나서 메인을 보여주고싶다면? 포워딩 해줄수도있는데("main" 리턴하면 가까처럼 접두접미 붙여서 전달)
			// 하나 더 배웠따
			// 포워딩 방식 보다는 -> sendRedirect
			// localhost/spring으로 보내면 똑같은 메인화면을 볼 수 있음
			// 사용자가 /만 보내서 요청을 보낼 수 있도록 하면 되니까 redirection해주는게 좋은데
			// response 객체가 필요하지만 그거 필요없고
			return "redirect:/"; // redirect: 을 적고 보내고 싶은 경로 적으면 끝
			
		} else { // 뭔가 잘못됐다는 뜻
			
			// 실패할 수 있지, 비밀번호 잘못 썼든지 잘못 적었든지
			// 어떻게 할 지 정해줘야한다
			// 지정해줄 페이지가 딱히 없음, include에 실패했을 때 보낼 페이지 하나 만들자 -> error_page.jsp
			// 포워딩은 확정인데 그 이전에 msg를 requestScope에 담아야함
			
			// error_page -> 포워딩
			// reqeustScope에 msg라는 키값으로 로그인 실패입니다 ~~ 담아서 포워딩
			// requestScope에 setAttribute 하려면 HttpServletRequest 타입 객체가 있어야 거기 담음
			// 근데 없지롱! 없으니까 매개변수로 받아와야지~ 핸들러 매개변수에 필요한거 달라고 추가
			// 나중에 핸들러 어댑터로 반환될 때(RequestAdaptor에서 HandlerAdaptor로 돌아갈때 어떤 형태로 가야함?)
			// -> ModelAndView 타입으로 돌아가야함
			// 실질적으로 return하고 붙어서 가는것은 view에 들어가는 내용이고, 우리가 requestScope에 담는 값(데이터)은 model에 들어가야함
			// 그래서 model and view인거임, 반환할 데이터와 화면을 합쳐둔것
			// 스프링에서 requestScope에 값을 담으려면 HttpServletRequest를 쓰는게 아니라 Model을 씀 -> Model model 추가(얘가 requestScope랑 영역이 같음)
			// Spring에서는 Model타입을 이용해서 RequestScope에 값을 담음
			model.addAttribute("msg", "로그인 실패 까비~");
			
			// 응답 jsp에 담을 값을 담았으니 포워딩 해야함
			// 포워딩 할 때 dispatcher servlet에서 view resolver에 간다, 거기에 가면 앞에 /WEB-INF/views 이게 붙음
			// 뒤에는 .jsp가 붙음
			// Forwarding
			// /WEB-INF/views/
			// .jsp
			
			// 우리는 include 밑에 error_page.jsp로 전달할건데
			// 얘 경로는 /WEB-INF/views/include/error_page.jsp 이것이다
			// 그럼 우리는 return 해서 전체 경로 중에 앞에 붙을 부분 떼고, 뒤에 확장자 붙을 부분 떼고
			// include/error_page
			return "include/error_page";
			
		}
		
		// return "main";
		
	}
	*/
	// 다시 구현하려고 전체 주석처리
	
	// 반환타입을 바꿔서 다시 구현해보자
	// 어댑터로 갈 때 무슨 타입으로 돌아가야함?
	// 두 번째 방법 : 반환타입 ModelAndView타입으로 반환
	// model은 단독사용 가능, view는 무조건 붙여서 사용
	
	// 핸들러에 어떤 요청이 오면 받을 지 애노테이션 달아야함 -> 이것도 보통 여기 메소드 라인에 다는게 아니라 클래스에 달아줌
	@PostMapping("/login")
	public ModelAndView login(MemberDTO member,
							  HttpSession session,
							  ModelAndView mv) {
		// ??? 15:43 옛날처럼 어쩌고저쩌고 안하고
		// 매개변수 자리에 내가 가공하자고 하는, 서비스단에 넘길 때의 타입의 변수를 선언
		// 이 객체의 필드명과 앞단에서 넘기는 전달값의 key값이 동일해야함
		// 그래야 DTO를 기본생성자로 객체를 생성하고 key값과 동일한 필드명을 찾아서 세터 메소드를 호출해서 대입해줌
		// 그러므로 DTO는 기본생성자와 setter가 무조건 들어가야함
		// VO는 setter가 없으므로(있으면 안되니까) 여기(Spring)부터는 무조건 받는게 DTO가 된다
		
		// ModelAndView mv = new ModelAndView();
		
		MemberDTO loginMember = memberService.login(member);
		
		if(loginMember != null) {
			
			// 성공했으면 사용자의 정보를 세션스코프에 담을건데 세션없음
			// 그럼 매개변수 자리에 세션 필요하다고 써두면 된다~
			session.setAttribute("loginMember", loginMember);
			
			// ModelAndView로 돌아가야하는데 이 메소드 안에 없음.. 만들어도 됨
			// 그렇지만 매개변수 작성해서 받아올 수 있음
			
			// 유지보수 관점에서 쓰는 실무스킬있는데 지금말고 나중에
			
			// 아까 반환할 때 return "main"; 이런거 했는데 여기서 viewName 넣어줌
			mv.setViewName("redirect:/");
			
		} else {
			
			// 실패는 request객체에 메세지 넣어서 화면지정했는데 이제는 이것도 model and view로 처리
			// 이거 좋은점 : 메소드 체이닝 가능, 나중에 담을 값 늘어나면 .()으로 추가가능
			// addObject로 front에 msg 값 전달 가능
			mv.addObject("msg", "로그인실패!")
			  .setViewName("include/error_page");
			
		}
		
		return mv;
		
	}
	// 기본적으로 우리가 구현할것은 CRUD
	// INSERT		--> POST ------> /member 요청이 Post로 왔을 때 처리하도록 메소드에 @PostMapping으로 작성
	// SELECT		--> GET ------> /member 요청이 Get으로 왔을 때 처리하도록 구체화해서 작성할 수 있다
	// 클래스 레벨에 @RequestMapping("/member") 달면 member 요청이 여기로 온다, 그리고 메소드 매핑을 PostMapping으로 작성함
	
	// UPDATE
	// DELETE
	
	@GetMapping("logout") // a태그로 요청이 오니까 Get 방식임
	public String logout(HttpSession session) {
		
		// sessionScope에 있는 값을 지우면 그게 로그아웃이겠지
		// 값을 지우려면? sessionScope의 attribute를 지우려면 HttpSession이 필요 -> 매개변수에 작성
		session.removeAttribute("loginMember");
		return "redirect:/";
		// 객체 지향 프로그램에서 기능을 수행하려면 객체가 있어야함
		// 기능을 수행하려면 메소드를 호출해야함, 메소드를 호출하려면 객체가 가지고 있으니까
		// 자바의 베이스는 객체, 그걸 다룰 때 제일 중요한건 타입, 값은 어차피 주소
		
	}
	
	@GetMapping("join")
	public String joinForm() {
		// 회원가입 a태그 누르면 이걸로 오게 작업했다
		
		// 이 핸들러는 signup.jsp로 포워딩 해줘야함
		// 포워딩 하려면 포워딩할 JSP파일의 논리적인 경로가 필요함
		// 이건 슬래시로 시작 /WEB-INF/views/member/signup.jsp <- webapp부터 시작하는 논리적인 경로
		// 이 경로에서, view resolver에 의해 앞뒤에 붙여주는거 제외하고
		// member/signup만 작성
		return "member/signup";
		
	}
	
}
