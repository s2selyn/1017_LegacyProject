package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.spring.exception.InvalidArgumentsException;
import com.kh.spring.exception.TooLargeValueException;
import com.kh.spring.member.model.dao.MemberRepository;
import com.kh.spring.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // 생성자 주입을 통해 필드들에 의존성 주입되는데 개발자가 할필요없고 롬복이 알아서
public class MemberServiceImpl implements MemberService {
	
	// @Autowired
	private final SqlSessionTemplate sqlSession;
	// 스프링이 관리하는 Bean이므로 주입받아야함 -> 방법 3개정도 있음
	// 일단 쉬운거 -> @Autowired 추가
	// 옛날 아저씨들 쓰는 방식, getSqlSession도 안해도 되고 close도 얘가 알아서 해주니 안해줘도됨
	// 그러고나서 해야할일은 DAO호출인데 DAO가 없으니 생성ㄱㄱ
	
	// 서비스에서도 DAO를 주입받아서 써야하니까 필드로 둬야하고 간단한 작업으로 구현
	// @Autowired
	private final MemberRepository memberRepository;
	
	// 암호화 주입받기 위한 필드선언
	// @Autowired
	private final BCryptPasswordEncoder passwordEncoder;
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
		
		return memberRepository.login(sqlSession, member);
		
	}

	@Override
	public void signUp(MemberDTO member) {
		
		// 반환형 void이므로
		// 꼼꼼하게 검증
		// 유효값 검증
		
		// 일단 넘어온 member가 null(객체가 만들어지지않음)이면 넘어갈 필요도 없고 유효값 체크할 필요도 없음
		if(member == null) {
			
			// null이면 id값 체크고뭐고 아무것도 할필요가 없음
			// return;
			
			// 여기도 예외처리 추가
			throw new NullPointerException("잘못된 접근입니다.");
			
		}
		
		// null이 아니었다는것은 객체가 들어왔다는 뜻
		// 그럼 이제 필드값에 대한 검증이 있어야함
		// 일단 정규표현식 쓰면 좀 쉬워지니까 일부러 복잡하게 해보자
		// 예를들어서 id값이 20자가 넘으면 안된다고 가정
		// 하나씩 비교해보자(String으로 정규표현식 쓰면 쉬움)
		// member를 참조해서 getUserId를 또 참조해서 length호출한것으로 비교
		if(member.getUserId().length() > 20) {
			
			throw new TooLargeValueException("아이디 값이 너무 길어용");
			// throw 하고 우리가 만든 예외 클래스를 객체로 생성해줌
			// 사용자는 예외 일어난줄 몰라야함 -> 모든 것을 예외처리 해줘야하는데(try-catch) 더욱 스마트한 처리방법이 있음
			
		}
		
		// 길이만 보는게 아니라 테이블도 생각해야함, pk, not null
		// 무조건 id, pwd, name 컬럼에는 값이 있어야함
		if(member.getUserId() == null ||
		   member.getUserId().trim().isEmpty() ||
		   member.getUserPwd() == null ||
		   member.getUserPwd().trim().isEmpty()) {
		
			// 사용자정의 예외클래스 만들어서 잘 작동하는지 확인했다
			// 하나더 만들어보자, 유효값 아닐 때 발생시킬 예외 -> exception 패키지에 생성
			// 솔직히 유효하지 않은 값 발생시킬 예외는 InvalidParamegerException이 있긴함
			// InvalidArgumentsException 클래스 생성하고 돌아옴
			
			// return;
			
			throw new InvalidArgumentsException("유효하지 않는 값입니다.");
			// 공백(스페이스바)만 입력하면 예외발생 의도대로함, 우리는 이거 알아야하지만 사용자는 이런거 보면안됨, 사용자는 예외발생 알수없도록 화면은 사용자가 보여줄것으로 만들어줘야함
			// -> 예외처리 try-catch 여기저기 해야해? 새로운거 해보자 -> 예외처리기 만들건데 노션확인
			
		}
		
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
		log.info("사용자가 입력한 비밀번호 평문 : {}", member.getUserPwd()); // 생 plaintext는 암호화전 그대로인거 -> getUserPwd
		
		// 암호화하기 == 인코더가지고 .encode()호출
		log.info("암호화한 후 : {}", passwordEncoder.encode(member.getUserPwd()));

	}

	@Override
	public void update(MemberDTO member) {

	}

	@Override
	public void delete(MemberDTO member) {

	}

}
