package com.kh.spring.board.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDTO {
	
	// pk 있어야하고 시퀀스로 만들거임
	private Long boardNo;
	
	private String boardTitle;
	private String boardContent;
	private String boardWriter;
	
	// 하나의 게시글에 하나의 첨부파일만 가능하도록 할거라 1:N 아니니까 그냥 여기 컬럼으로 넣음
	private String originName;
	private String changeName;
	
	// 상세조회 시 조회수 올릴 필드
	private int count;
	
	// 날짜 맨날 sql date 썼으니까 이번엔 다르게
	private String createDate;
	private String status;

}
