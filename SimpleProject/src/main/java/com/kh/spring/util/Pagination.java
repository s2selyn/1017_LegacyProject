package com.kh.spring.util;

import org.springframework.stereotype.Component;

// Bean으로 등록
@Component
public class Pagination {
	
	public PageInfo getPageInfo(int listCount
							  , int currentPage
							  , int boardLimit
							  , int pageLimit) {
		
		// 여긴 앞에서 한거 그대로임
		int maxPage = (int)Math.ceil((double)listCount / boardLimit);
		int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
		int endPage = startPage + pageLimit - 1;
		if(endPage > maxPage) endPage = maxPage;
		
		// PageInfo 객체를 만들어서 반환
		return new PageInfo(listCount, currentPage, boardLimit, pageLimit, maxPage, startPage, endPage);		
		
	}

}
