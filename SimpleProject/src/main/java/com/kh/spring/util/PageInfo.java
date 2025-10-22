package com.kh.spring.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//애노테이션 한줄쓰기 가능한데 가독성 떨어지니 안함, 하지말라고 한다
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageInfo {
	
	private int listCount;
	private int currentPate;
	private int boardLimit;
	private int pageLimit;
	
	private int maxPage;
	private int startPage;
	private int endPage;
	
	//위에 네개 값으로 세개값 만들고 페이지 인포 객체를 만들어서 반환해주는 클래스 생성

}
