package com.kh.spring.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 실무가서 하지말라는거 하는사람들이 있다 -> @Data -> 이건 종합선물세트, 기본생성자 equals hashcode tostring 등 / 매개변수생성자만 안만들어줌
// 그러면 @Data, @AllArgsConstructor 할텐데 매개변수생성자 생성하면 기본생성자 없어짐, 근데 스프링은 객체 만들때 bean등록된거 기본생성자 호출해서 만든다, 문제생김
// 결국 @Data, @NoArgsConstructor, @AllArgsConstructor 이렇게 해야함, 별 차이도 없고
// @Data 이거써서 실수생기고 문제생긴다, 안 쓸 메소드도 다 만들어지니까 자원효율측면에서 낭비가 됨, 쓰는사람도 있는데 쓰지 말라는게 권장사항
// 이미 이걸로 만들어진게 많아서 없애버리면 기존거 작동안하니까 없애질 못한다, 이런 코드를 봐도 혹하면 안됨
// 전에 말씀하셨듯이 한줄에 애노테이션 다 달아버리는것도 하지마쇼, 가독성 떨어진다
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyDTO {
	
	private Long replyNo;
	private Long refBno;
	private String replyContent;
	private String replyWriter;
	private String createDate;
	private String status;

	// 이거 resultMap 써서 collection 태그 써서 같이 조회해갈것이다
	// reply를 어떻게 해야하나? Board 조회할때 한꺼번에 가져갈건데
	// board는 1, reply는 여러개, 1:N 관계, 같이 조회하면 댓글개수만큼의 행
	// 하나의 보드만 들고가고싶은데? 그러면 BoardDTO의 필드로 ReplyDTO 작성해야한다 -> BoardDTO가세요
	// 사진게시판 할때 해봄, 하나의 게시글에 여러 첨부 들고갈때
	
}
