package com.kh.spring.member.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter // -> import 하면 이 내부에는 안보이는데 패키지 익스플로러에서는 생성되어있음
@Setter // setter 추가
@NoArgsConstructor // 기본생성자 추가(매개변수가 없는것이 특징이므로)
@AllArgsConstructor // 모든 필드를 가진 매개변수 생성자 추가
@ToString // toString 메소드 추가
public class MemberDTO {
	
	private String userId;
	private String userPwd;
	private String userName;
	private String email;
	private Date enrollDate;
	
	// 기본생성자, 매개변수생성자, getter/setter 작업하다가 필드명 오타내는 경우가 있음
	// memberId -> userId로 바꾸고싶음, 단어나 대소문자 등의 필드명 수정이 필요한 경우에 이 필드명을 사용하는 생성자들과 메소드들 전부를 수정해줘야함
	// 자료형을 수정해야 할 수도 있겠지? 보편적으로 다 때려부수고 다시만드는게 빠르긴해...
	// 이번에는 이런 문제, 상황(실수해서 자료형, 필드명, 테이블 수정 등)
	// 테이블 수정하면 당연히 필드 수정해야함, 오늘 테이블 수정했으니까 미리 만들어뒀다면 다 수정해야겠지
	// 이런 상황에서 코드의 수정이 많이 일어나야하는데 이걸 알차게 할 수 있는 방법을 알아보자!
	
	// 라이브러리를 프로젝트에 추가 -> pom.xml 작업
	// 롬복 세팅하고 다시 돌아와서
	// 수정사항 발생 시 실수할 확률, 생산성 저하 -> 롬복 사용으로 간단히 해결가능
	// 클래스 선언부에 애노테이션 추가
	// 롬복을 이용하면 코드를 쓰는게 아니라 애노테이션을 달아서 추가가능
	// 애노테이션 작성에 비해 자동완성으로 다는게 빠르긴 하지만 자바개발자라면 전부 롬복을 쓴다
	// 필드 변동이 생길 수 있음 -> 필드 넣고 저장하면 롬복이 알아서 해당하는 setter/getter, 생성자, toString 등 변경해준다
	// 보일러플레이트 코드 작업을 간단히 수행가능, 코드 다이어트 라이브러리 라고 말하는 사람도 있음, 코드 자체는 안보이니까
	// DTO도 깔끔해졌다, 오타를 두려워하지 않아도 되는군
	// -> 다시 컨트롤러로 돌아감

}
