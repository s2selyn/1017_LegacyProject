package com.kh.spring.member.model.service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.kh.spring.exception.AuthenticationException;
import com.kh.spring.exception.UserIdNotFoundException;
import com.kh.spring.member.model.dao.MemberMapper;
import com.kh.spring.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // 생성자 주입을 통해 필드들에 의존성 주입되는데 개발자가 할필요없고 롬복이 알아서
public class MemberServiceImpl implements MemberService {
	
	/*
	 * SRP(Single Responsibility Principle)
	 * 단 일 책 임 원 칙 위반
	 * 
	 * 하나의 클래스는(메소드) 하나의 책임만을 가져야함 == 얘가 수정되는 이유는 딱 하나여야함
	 * 
	 * 멤버 서비스는 DB에 있는 멤버 테이블에 CRUD 작업을 위한 클래스임, 얘가 수정이 되는 이유는 뭐여야하나?
	 * 테이블 관련 작업을 할 때 뭔가 수정이 생기면 이게 변해야하는데,
	 * 지금 필드로 passwordEncoder를 사용하고 있음, 이거 쓰다가 Argon2로 Encoder 바꾸자고 했다고 쳐
	 * 이게 바뀔 수 있는데, 이 일이 지금 Member랑 관련있는 일이 아님
	 * 평문을 암호문으로 바꾸고 그게 맞는지 체크해주는건 멤버랑 아무관련이 없음
	 * 문자열 암호문으로 만들어주는 객체일뿐
	 * 근데 알고리즘 바뀌었다고 여기가 바뀌면 멤버랑 관련없는 작업으로 멤버클래스가 수정되는 일이 발생
	 * 이게 단일책임원칙 위반인것
	 * 
	 * 책임 분리하면 끝
	 * 
	 * 서비스 입장에서 BCryptPasswordEncoder를 너무 잘 알고 있는 의존하고 있는 상태
	 * 이걸 의존시키지 않고 클래스를 새로 생성 -> PasswordEncoder
	 * 
	 * 이거 메소드에도 적용되는 원칙임! 서비스에 있는 메소드들을 보니 코드들의 상태가...?
	 * 하나의 메소드가 너무 많은 책임을 가지고 있음
	 * signUp의 목적(컨트롤러가 호출함) -> 사용자가 입력한 값으로 DB에 한 행 INSERT
	 * 사용자의 입력값을 사용하려고 갑자기 검증을 하고 있음
	 * 만약에 정책이 바뀌었어, DB가 20byte -> 15byte로 컬럼 크기가 바뀐다든지..
	 * insert 목적 메소드인데 컬럼 크기 바뀌었다고 여기서 유효성 검증 부분이 바뀌어야함
	 * 
	 */
	
	// @Autowired
	// private final SqlSessionTemplate sqlSession;
	// 스프링이 관리하는 Bean이므로 주입받아야함 -> 방법 3개정도 있음
	// 일단 쉬운거 -> @Autowired 추가
	// 옛날 아저씨들 쓰는 방식, getSqlSession도 안해도 되고 close도 얘가 알아서 해주니 안해줘도됨
	// 그러고나서 해야할일은 DAO호출인데 DAO가 없으니 생성ㄱㄱ
	
	// 서비스에서도 DAO를 주입받아서 써야하니까 필드로 둬야하고 간단한 작업으로 구현
	// @Autowired
	// private final MemberRepository memberRepository;
	
	// 암호화 주입받기 위한 필드선언
	// @Autowired
	// private final BCryptPasswordEncoder passwordEncoder;
	private final PasswordEncoder passwordEncoder; // 기존에 사용하던 해싱알고리즘 뭔지 몰라도 쓰게 여기만 수정, 아래쪽 수정안해도됨 -> 알고리즘 바뀌면 PasswordEncoder에서 수정하기만 하면됨
	// 어제 한거 필드, 세터, 생성자 주입 이렇게 세가지였음 -> 권장은 생성자였죠? 늘어날때마다 귀찮은데...? 처음에야 그냥 만들겠지만 나중에 계속 작업해야함
	// 그렇지만 이렇게 해야 객체의 불변성이 보장되니까 이게 권장사항임! 실무에서는 잘 지켜지지 않을 수 있음
	// 필드에 기존에 달려있던 @Autowired 애노테이션 지우고 final로 선언하고 생성자주입방식
	/*
	@Autowired
	public MemberServiceImpl(SqlSessionTemplate sqlSession,
							 MemberRepository memberRepository,
							 BCryptPasswordEncoder passwordEncoder) {
							 
		this.sqlSession = sqlSession;
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		
	}
	*/
	// 이거마저 귀찮으니까 롬복에서 만들어주는걸로 할 수 있음 -> 클래스에 애노테이션 추가 @RequiredArgsConstructor
	// 그럼 @Autowired 안달아도 생성자주입으로 권장방식 사용가능
	
	// 검증클래스 책임 분리하고 주입받기 위한 필드선언
	private final MemberValidator validator;
	
	// Mapper 인터페이스로 책임분리하고 필드 주석처리
	private final MemberMapper mapper; // 만들어둔 인터페이스로 주입받기 위한 필드

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
		
		// ver 0.1
		// return memberRepository.login(sqlSession, member);
		
		// 사용자는 평문을 입력하지만 실제 DB컬럼에는 암호문이 들어있기 때문에
		// 비밀번호를 비교하는 SELECT문은 사용할 수 없음 -> 매퍼 수정
		// MemberDTO loginMember = memberRepository.login(sqlSession, member);
		// 일단 아이디만으로 조회해오기
		// 매개변수로 받아온 member에는? 로그인할때 사용자가 입력한 비밀번호 평문이 들어있음
		// 1절 : ID만으로 조회, passwordEncoder로 비밀번호 검증할거임, 만약에 id 조회결과가 없다면?
		/*
		 * NPE 발생한다, loginMember.getUserPwd()는 null이 아닐때만 동작해야함
		 * -> Exception 클래스 새러 생성
		 * 
		 */
		MemberDTO loginMember = mapper.login(member);
		// 인터페이스로 수정하고 반영
		
		/*
		// -----
		if(loginMember == null) {
			
			// throw new UserIdNotFoundException("아이디가 존재하지 않습니다.");
			throw new UserIdNotFoundException("아이디 또는 비밀번호가 틀림");
			// 보안적 관점에서 메세지 수정
			
			// 비밀번호 잘못됐다는 메세지면 아이디 있다는거니까 모든 경우의 수 때려박는 식으로 브루트포스 어택(무차별 대입 공격, Brute-force attack)
			// 선택지를 줄여주는게 좋은게 아니다, 메세지는 애매하게 넣어줌
			// 모든 암호는 브루트포스 공격의 타겟이 될 수 있음, 그러므로 몇번 틀리면 안된다고 인증 다시하라, 시간동안 금지하고 막아버리는 식으로 구현
			// 레인보우 테이블 -> 해싱 알고리즘 돌리면 평문으로 돌렸을 때 무조건 같은 값이 나오니까 이걸 전부 표로 만들어놓음
			// a로 돌리면 뭐가나오고 b로 돌리면 뭐가나오는지 -> 이걸 레인보우테이블이라고 함, 알고리즘만 알면 이 표를 보고 평문 유추 가능
			// 그러므로 암호화 알고리즘도 아무거나 쓰는게 아니라 많이 쓰고 검증된걸로 써야함
			// 우리도 이런 보안적인 측면을 고려하기 위해서 사용자가 로그인을 할 때 비밀번호 다섯번 잘못적으면 막아야겠따! 메일 인증 다시 받게 해야겠다! 이런거 할수있겠지?
			// 이거 하려면 로그인 시도 했는데 몇번 틀렸는지 알아야함 -> 브라우저에 하면 안되니까 DB에 넣어야하는데 안틀리면 안생기는 값이니 별도의 테이블로 구현하는게 낫겠지
			// 반대로 조회할때도 로그인을 할때도 이걸 바로 id 조회하는게 아니라 시도실패횟수먼저 조회해서 되돌려보내는 식으로 구현
			
		}
		// -----
		
		// log.info("사용자가 입력한 비밀번호 평문 : {}", member.getUserPwd());
		// log.info("DB에 저장된 암호화된 암호문 : {}", loginMember.getUserPwd());
		// DB에서 저장된 암호문을 같이 까보자!
		// 암호문이 있으면 만들때 사용한 salt값 사용가능
		// 버전을 보면 어떤 버전의 알고리즘을 알수있고, 횟수도 알고리즘 몇번 돌렸는지 알수있음
		// 평문, 버전, 반복수, salt값이 있다면 똑같은 방식으로 salt값 더해서 가능
		// 해싱 알고리즘 특 -> 같은 값으로 같은 해싱알고리즘을 돌리면 똑같은 결과값이 나온다
		
		// 아이디만 가지고 조회를 하기 때문에
		// 비밀번호를 검증 후
		// 비밀번호가 유효하다면 ㅇㅋㅇㅋ~
		// 비밀번호가 유효하지 않다면 이상한데??
		
		// 새로운거 만들지 않고 기존 코드 고치는 방식으로 작업
		// select, insert 한번씩 해봤으니 스프링 다한거임, 퀄리티만 높여서 실무와 가까운 코드로 고쳐보자
		
		if(passwordEncoder.matches(member.getUserPwd(), loginMember.getUserPwd())) {
			// 암호문이 평문으로 만들어졌다면 true, 아니라면 false -> if문의 조건으로 사용
			
			// 들어왔다는건 비밀번호 맞다는거임
			return loginMember;
			
		}
		
		return null;
		// 원래대로라면 여기오는건 비밀번호가 잘못된거니까 리턴이 아니라 예외를 발생해야함
		// throw new PasswordNotMatchException 이런거 만들어서 했겠죠 -> 이거 별로 안좋음
		// 아이디가 틀리고 비밀번호가 틀리고 이렇게 알려주면 보안상 별로 좋은게 아님, 공격자가 메세지를 받고 있는 아이디구나, 해서 비밀번호만 바꿔서 공격하는 식으로 악용가능
		// 명확하게 알려주는건 별로 좋은 방법이 아니게 된다. 보안적인 측면을 잘 고려하면 아이디 또는 비밀번호가 잘못되었다고 알려줌, 뭐가 잘못된지 없는지 모르게 하기 위해서
		// 아이디 유무는 중복체크 보면 알 수 있으니 허점이 있긴 하지만... 아무튼 그런거 고려해서 메세지 수정
		*/
		// 메소드로 책임분리하고 주석처리
		
		return validateLoginMember(loginMember, member.getUserPwd());
		
	}
	
	// 메소드 분리로 책임분리
	// 여기서만 부를거니까 바깥에 보일 필요가 없음
	private MemberDTO validateLoginMember(MemberDTO loginMember, String userPwd) {
		// 일단 loginMember가 있어야하고, getUserPwd 하기 위해서 member 객체도 필요하므로 매개변수 작성
		
		// 코드 복붙
		// select 로직과 검증 로직을 분리했음, 책임을 분리해서 유지보수가 용이해짐
		if(loginMember == null) {
			throw new UserIdNotFoundException("아이디 또는 비밀번호가 틀림");
		}
		
		// log.info("평문 : {}, 암호문 : {}", userPwd, loginMember.getUserPwd());
		
		if(passwordEncoder.matches(userPwd, loginMember.getUserPwd())) {
			
			// log.info("여기가 문제겠지?"); -> PasswordEncoder 클래스 수정
			return loginMember;
			
		}
		
		return null;
		
	}
	// 해놓고보니 서비스는 멤버 CRUD 해야하는건데 이 메소드는 또 여기 있으면 안된다, 책임이 또 생겼으니까(검증로직 수정하면 얘도 수정되어야함)
	// -> 이건 또 방빼서 나가야함, 헷갈리니까 signUp 검증 메소드 코드 분리하자
	// insert 하는건데 검증하고 있으니까 -> service 클래스 생성

	@Override
	public void signUp(MemberDTO member) {
		
		// 반환형 void이므로
		// 꼼꼼하게 검증
		// 유효값 검증
		
		// 일단 넘어온 member가 null(객체가 만들어지지않음)이면 넘어갈 필요도 없고 유효값 체크할 필요도 없음
//		if(member == null) {
//			
//			// null이면 id값 체크고뭐고 아무것도 할필요가 없음
//			// return;
//			
//			// 여기도 예외처리 추가
//			throw new NullPointerException("잘못된 접근입니다.");
//			
//		}
//		
//		// null이 아니었다는것은 객체가 들어왔다는 뜻
//		// 그럼 이제 필드값에 대한 검증이 있어야함
//		// 일단 정규표현식 쓰면 좀 쉬워지니까 일부러 복잡하게 해보자
//		// 예를들어서 id값이 20자가 넘으면 안된다고 가정
//		// 하나씩 비교해보자(String으로 정규표현식 쓰면 쉬움)
//		// member를 참조해서 getUserId를 또 참조해서 length호출한것으로 비교
//		if(member.getUserId().length() > 20) {
//			
//			throw new TooLargeValueException("아이디 값이 너무 길어용");
//			// throw 하고 우리가 만든 예외 클래스를 객체로 생성해줌
//			// 사용자는 예외 일어난줄 몰라야함 -> 모든 것을 예외처리 해줘야하는데(try-catch) 더욱 스마트한 처리방법이 있음
//			
//		}
//		
//		// 길이만 보는게 아니라 테이블도 생각해야함, pk, not null
//		// 무조건 id, pwd, name 컬럼에는 값이 있어야함
//		if(member.getUserId() == null ||
//		   member.getUserId().trim().isEmpty() ||
//		   member.getUserPwd() == null ||
//		   member.getUserPwd().trim().isEmpty()) {
//		
//			// 사용자정의 예외클래스 만들어서 잘 작동하는지 확인했다
//			// 하나더 만들어보자, 유효값 아닐 때 발생시킬 예외 -> exception 패키지에 생성
//			// 솔직히 유효하지 않은 값 발생시킬 예외는 InvalidParamegerException이 있긴함
//			// InvalidArgumentsException 클래스 생성하고 돌아옴
//			
//			// return;
//			
//			throw new InvalidArgumentsException("유효하지 않는 값입니다.");
//			// 공백(스페이스바)만 입력하면 예외발생 의도대로함, 우리는 이거 알아야하지만 사용자는 이런거 보면안됨, 사용자는 예외발생 알수없도록 화면은 사용자가 보여줄것으로 만들어줘야함
//			// -> 예외처리 try-catch 여기저기 해야해? 새로운거 해보자 -> 예외처리기 만들건데 노션확인
//			
//		}
// 클래스로 책임분리하고 주석처리
		// 롬복으로 주입받아서 사용해야하니까 필드선언은 위에 작성하고 여기에서 호출
		validator.validatedMember(member);
		
		// 지금 중복체크 구현안하고 있으니 사용자가 입력한 아이디가 DB에 존재하는지 아닌지도 비교해야함
		// 이런 식으로 작업하면 나중에 사용자에게 알려줄 수 있나? 아이디 없어, 입력안했어 이런건 리턴만 해서는 알려줄 수 없음
		// 이런걸 세분화해서 알려줄 수 있으면 좋겠다 -> 또 새로운거 배워보자
		// 지금은 조건 만족 못하면 그냥 리턴해버리는 식으로 작업함
		// 이제 리턴하지 말고 예외를 발생시켜보자 -> 내가 작성한 내용에 만족하지 못한다면 예외를 발생시켜서
		// id가 20자가 넘었다면? 20자가 넘을 때 발생할 예외를 발생시키자
		// 근데 그런 예외가 있나? 없는 것 같은데요... 우리가 직접 예외 클래스를 만들어보자! -> 만들고 와서 20자 넘는 곳 검증하는 곳에서 예외발생 코드작성
		
		// 아이디 중복체크 생략(이거도 select 한번 해오는거니까 뭐 직접 해보쇼)
		
		// 실제로는 뭐 이런 난관 넘기는거 정규표현식으로 하것지 "".matches() 이런식으로 메소드 호출해서
		// 다 통과했다고 치자! 이제 뭐함?
		// DAO로 가서 INSERT하기
		// 우리 이번에 회원가입 할 때 무슨작업 추가하기로 했더라...? 암호화! Password Encryption
		// 이거 하려면 암호화 알고리즘이 필요하다, 스프링 시큐리티에서 제공하는 암호화 알고리즘 모듈을 사용해보자
		// pom.xml에 dependency 태그 추가하고 오기
		
		// 암호화 알고리즘은 클래스만 바꿔주면 쓸 수 있음, 지금은 검증된 근본부터 써보자 -> Bcrypt
		// 그리고 bean 설정용 xml 파일도 직접 만들어보자(root-context.xml에서 작업해도 되긴함)
		// 만들어서 Bean 등록까지 하고왔음
		
		// 의존성 주입 받아서 써야함 -> 여기서 애노테이션 쓰려면 필드 선언 먼저 해야한다
		// 위에 가서 작성하고 오셔
		
		// DAO로 가서 INSERT하기전에 비밀번호 암호화하기
		// log.info("사용자가 입력한 비밀번호 평문 : {}", member.getUserPwd()); // 생 plaintext는 암호화전 그대로인거 -> getUserPwd
		
		// 암호화하기 == 인코더가지고 .encode()호출
		// log.info("암호화한 후 : {}", passwordEncoder.encode(member.getUserPwd()));
		// DB에는 암호화된것이 들어가있어야한다, 안그러면 금융치료당함.. 벌금으로(정보통신망법, 개인정보보호법)
		// 우리 DB에 들어가있는것들도 바꿔줄까? DBeaver 다녀오기
		// 이렇게 하고 MemberVO만들어서 하는데, 시간 오래걸려서 DTO가지고 할거임
		String encPwd = passwordEncoder.encode(member.getUserPwd());
		member.setUserPwd(encPwd); // 암호화한 값으로 다시 넣어주자
		
		// 그리고 나서 DAO 호출
		// memberRepository.signup(sqlSession, member);
		mapper.signup(member);
		// 인터페이스로 수정하고 반영
		
		// 이제 sqlSession을 넘길 필요도 없이 스프링이 넘겨준다
		// 이건 단순 SQL문에만 사용가능, resultMap 필요하거나 조인, 서브쿼리 이런거 복잡해서 애매함
		// 이렇게 하고 root-context.xml에서 설정 추가함

	}

	@Override
	public void update(MemberDTO member, HttpSession session) {
		
		// 여기부터 이제 본격적인 개발자의 영역
		// 내가 생각하는 update는 어떻게 동작해야할까?
		// sql문을 미리 써보고 member를 넘겼음
		// 앞에서 넘긴 값이랑 지금 로그인된 사용자가 일치한다는 보장이 없음
		// 브라우저에서 값을 수정해서 보내버릴 수 있음, readonly 이런게 의미가 없다
		// 나쁜맘 먹으면 얼마든지 수정가능
		
		// 해야 할 일들
		// 1. 앞단에서 넘어온 ID값과 현재 로그인된 사용자의 ID값이 일치하는가?
		
		// 2. 실제 DB에 ID값이 존재하는 회원인가?
		
		// 3. USERNAME 컬럼에 넣을 값이 USERNAME 컬럼 크기보다 크지 않은가?
		
		// 4. EMAIL 컬럼에 넣을 값이 EMAIL 컬럼 크기보다 크지않은가?
		
		// OPTIONAL : (EMAIL컬럼에 넣을 값이 실제 EMAIL형식과 일치하는가?) -> 이메일 형식 아니어도 넣어주는 사이트들이 있음
		
		// 5. DB가서 UPDATE
		
		// 6. 성공환 회원의 정보로 SessionScope에 존재하는 loginMember키값의 Member객체 필드값 갱신해주기
		
		// 실질적으로 개발자가 해야할 일들을 생각해서 작업하는 영역, 제약조건도 필요하다
		// 이렇게 해도 문제생기고 뚫고 들어와서 데이터 망가지고 함
		// 앞에까지는 방법에 중점이었다면 여기부터는 세심하게 생각하고 고려해서 코드를 짜야함, 이런것들이 중요
		// 솔직히 서비스도 interface부터 뜯어고쳐야함, 여기서 HttpSession이 필요함, 거기서 뽑아서 검증(1)하고 덮어쓰기(6)해서 보내줘야함
		// 여기서도 세션 주입받아서 사용가능, scope 메소드 써서 주입받아서 사용할수도 있고
		// 실무에서는 컨트롤러에서 받아서 넘겨주는 방식을 선호한다
		// 여기서 세션을 받으면 여러 사용자에 의한 충돌이 생길 수 있음
		// 각 요청에 의해 세션이 생기므로 컨트롤러에서 넘겨서 사용하는것을 권장함 -> 컨트롤러에서 매개변수 작성
		// 여기서 세션만드는건 또 DB 업데이트랑 상관없는 영역이니까, 비즈니스 로직은 아님 세션만들고 받아오는것이 그렇지, 그러므로 컨트롤러에서 하는걸 권장
		
		// 본격적으로 진짜 시작
		// 세션에서 객체 뽑기
		MemberDTO sessionMember = ((MemberDTO)session.getAttribute("loginMember"));
		// 이게 null일수있다, 매핑값만 있으면 요청할수있음
		// sessionMember, member 둘다 null인지 검증해줘야하는데 이건 업데이트랑 관련없는 작업
		// -> 이건 MemberValidator클래스에 코드를 분리했음
		// 다녀와잇!
		
		// 2를 생략하려고 했으나 로그인한 상태인데 그사이에 탈퇴시켜버렸다면 확인해야함 -> MemberValidator 작업
		// id만 있으면 select해서 있는지 없는지 확인하는 메소드가 있음 -> login 호출
		validator.validatedUpdateMember(member, sessionMember);
		// 여기가 유효성 검증, 올바른 값이었다는 뜻 -> 이제 DB가서 업데이트해야함
		// 서비스가 가는게 아니라 Mapper가 가는거니까 mapper로 호출, sqlSession도 넘길 필요가 없음
		int result = mapper.update(member);
		// 매퍼 다녀옴
		
		if(result != 1) {
			
			// 이건 업데이트가 안되었다는 뜻
			// 예외 발생시켜야함
			throw new AuthenticationException("문제가 발생했습니다. 관리자에게 문의하세요.");
			
			// 의식 못하고 쓰는 웹사이트의 다양한 예외들 -> 이거 개발자가 깔끔하게 해줘야하는것
			// 예외가 일어나지 않는, 문제가 생기지 않는 웹사이트와 프로그램은 없다
			// 문제가 일어나는것과 별개로 사용자가 프로그램을 온전히 이용할 수 있게끔 깔끔하게 처리해줘야한다
			
		}
		
		// 잘 하고 돌아왔다는 뜻이고 세션에는 값이 갱신되어있지 않으니 갱신해주기
		sessionMember.setUserName(member.getUserName());
		sessionMember.setEmail(member.getEmail());

	}

	@Override
	public void delete(String userPwd, HttpSession session) {
		
		// 제 1원칙 : 기능이 동작해야함 -> 이게 안되면 짧건 잘쓰건 상관없음
		// 한줄이건 천줄이건 일단 돌면 성공이다, 오늘처럼 돌아가면 고치는거야, 돌아가지않으면 논외임
		// 익숙해진다면 처음부터 분리하고 작업하겠지, 코드를 이쁘게 짤 생각이 아니라 일단 돌아가게 만들 생각
		// 회원가입이라면 입력받은 값으로 insert가 동작해야함, 그다음에 체크도하고 이것저것 하겠지
		// >> 본질을 잊지말자 <<
		// 세미/미니는 최대한 많은 경험
		// 구현하는 기능도 큰거 하나 하는거도 좋겠지만 여러 기능을 여러 방법으로 해보는게 좋지
		// 가장 처음 만드는 기능은 제일 단순한게 좋다, 제일 빨리 끝낼 수 있는것으로
		// 어려울 것 같아서 큰거잡으면 이거 안끝났을때 노답임, 하나 간단한거 먼저 해놓고 뻗어나가는겨
		// 처음 할 때 잘되기 쉽지않고 안되면 막혀서 아무것도 못하게된다, 제일 작은단위 간단한것부터 접근하기
		
		// 아무튼 이것부터 마무리하자~
		// 마이페이지에 모달로 달아뒀음 -> my_page.jsp
		
		MemberDTO sessionMember = ((MemberDTO)session.getAttribute("loginMember"));
		
		// 세션에서 뽑았는데 로그인 안됐으면 탈퇴요청 해줄필요가 없으므로 돌려보냄
		if(sessionMember == null) {
			throw new AuthenticationException("로그인부터해라~");
		}
		
		if(!passwordEncoder.matches(userPwd, sessionMember.getUserPwd())) {
			
			// true가 아니면 비밀번호 잘못쓴거니 논할가치가 없죠
			throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
			
		}
		
		// status 컬럼 없으니 삭제
		// DELETE FROM MEMBER WHERE USER_ID = 현재 로그인된 사용자의 아이디
		// 비밀번호 입력받았다고 조건으로 못쓴다 사용자가 입력한건 평문이고 db에 들어있는건 암호문
		int result = mapper.delete(sessionMember.getUserId());
		
		// void로 반환하니까 검증 꼼꼼히하쇼 근데 또 중복이네 하참내 -> member, notice 어디서 실패하든 책임분리한곳에서 다 써버리고 중복 없애기 가능
		if(result != 1) {
			throw new AuthenticationException("관리자에게 문의하세요.");
		}
		
		// 중복으로 빼기(리팩토링)는 나중임, 일단 삭제되어야함
		
		// 탈퇴 성공하면 세션스코프에서 날리기
		session.removeAttribute("loginMember");
		// 이것도 세션관리라 여기서하는게 아니고 컨트롤러에서 해야함
		
		// 기능 구현 작업이 다 끝났다면 리팩토링, 이건 혼자하는게 아니라 팀원들이랑한다
		// 리팩토링은 작동은 똑같이 해야하고 안에 알맹이만 바뀌는것, 밖에서 사용할때 똑같이 사용할 수 있어야함
		// 안에걸 뜯어고치는건데 나혼자 개발한거면 상관없는데 여러명이 같이 붙어서 작업했을 경우에 내거 수정하면 다른사람이 영향을 받을 수 있음
		// 누군가 내가 만들어둔 서비스를 갖다쓰고 있었다면? 내가 이걸 바꾸고싶어서 로직을 바꾼다면?
		// 갑자기 안될수있다, 나는 내거 안건드렸는데 다른사람이 코드 수정해서 작동불가능해버리게됨, 내가 하고싶다고 수정해버리면ㄴㄴ
		// 여러사람이 바뀌었을때 일어날 상황을 테스트하면서 고쳐야함
		// 개발에서도 리팩토링은 고급기술로 쳐준다, 이거 하려면 일단 결과물이 있어야함
		
		/*
		 * 스프링 배웠는데...
		 * 쉬운데 어려움(복잡함)
		 * 
		 * 세팅 수업시간에 했던거 다 가져다쓰세요(직접하는건 의미없음, 유지보수는 많이할건데 신규작업은 MVC로 하는 회사는 없을지도? 있을지도?, 요즘은 다 부트)
		 * 컨트롤러, 서비스 만들어서 작업하고 암호화도 해보고, DAO도 Mapper 클래스로 바꿔서 해보고, 예외클래스도 만들고 GlobalExceptionHandler도 써보고
		 * 세팅이 어려운데 요즘 안하니까 구현에 신경써서 해보자
		 * 
		 * 쉬워진것같은데 하려고보니 복잡함
		 * 복잡함의 영역 대다수를 차지하는건 xml 파일들임, root-context, servlet-context
		 * 그래도 이것들은 한번만 하면 끝남, 하다못해 수업자료 복붙해도 돌아감, 읽어봐도되고 복붙해도 됨, 직접 만드는게 제일 좋긴하지
		 * 
		 * 스프링을 이용한 기능 구현 ==> 숙제(주말까지) ==> 꼭!!!하기!!!!꼭꼭해보기!!!!꼭꼭꼭!!
		 * 
		 * DynamicWebProject 회원(KH_MEMBER)파트 ==> Spring버전으로 다시 만들기
		 * 
		 * 화면 다있음 테이블 다있음 SQL문 다있음 ==> Service단 및 예외처리를 신경써보자
		 * 
		 */
		
	}

}
