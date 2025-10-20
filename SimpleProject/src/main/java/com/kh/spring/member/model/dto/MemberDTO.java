package com.kh.spring.member.model.dto;

import java.sql.Date;

public class MemberDTO {
	
	private String memberId;
	private String memberPwd;
	private String memberName;
	private String email;
	private Date enrollDate;
	
	// 기본생성자, 매개변수생성자, getter/setter 작업하다가 필드명 오타내는 경우가 있음
	// memberId -> userId로 바꾸고싶음, 단어나 대소문자 등의 필드명 수정이 필요한 경우에 이 필드명을 사용하는 생성자들과 메소드들 전부를 수정해줘야함
	// 자료형을 수정해야 할 수도 있겠지? 보편적으로 다 때려부수고 다시만드는게 빠르긴해...
	// 이번에는 이런 문제, 상황(실수해서 자료형, 필드명, 테이블 수정 등)
	// 테이블 수정하면 당연히 필드 수정해야함, 오늘 테이블 수정했으니까 미리 만들어뒀다면 다 수정해야겠지
	// 이런 상황에서 코드의 수정이 많이 일어나야하는데 이걸 알차게 할 수 있는 방법을 알아보자!
	
	public MemberDTO() {
		super();
	}
	
	public MemberDTO(String memberId, String memberPwd, String memberName, String email, Date enrollDate) {
		super();
		this.memberId = memberId;
		this.memberPwd = memberPwd;
		this.memberName = memberName;
		this.email = email;
		this.enrollDate = enrollDate;
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public String getMemberPwd() {
		return memberPwd;
	}
	
	public void setMemberPwd(String memberPwd) {
		this.memberPwd = memberPwd;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getEnrollDate() {
		return enrollDate;
	}
	
	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

}
