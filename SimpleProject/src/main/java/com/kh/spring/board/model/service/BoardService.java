package com.kh.spring.board.model.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dto.BoardDTO;
import com.kh.spring.board.model.dto.ReplyDTO;

public interface BoardService {
	
	/*
	 * 빵꾸많음, 실제 개발할 때 안되는 상황이 많다
	 * 그러면 보통 사람이 여기에 끼워맞춰서 작업하려고 하는 경향이 있다네요, 처음 생각한게 이거니까
	 * 이걸 만들 당시의 내머리로는 최선의 결과물이라 이걸 바꿔야지 하는 생각을 하기 쉽지않다고..
	 * 실제 개발하다보면 전혀 그렇지 않다, 만들면서 수정하자!
	 * 
	 */
	
	// 여기 작업은 일단 선생님께서 준비해오신걸로 한거임
	
	// 게시글 목록 조회 + 페이징처리
	// 조회랑 페이징처리 같이할거니까 현재 게시글 개수 알아내는 메소드가 필요 -> 서비스단에서 처리해서 넘어가는게 낫다, 목록 조회에서 퉁칠것임
	// 어쨌든 페이징처리 객체 만들것이므로 여기 있어야겠다
	// 총 게시글 수 조회
	// int selectListCount(); 여기서 할 게 아니라 findAll에서 해야한다, 매개변수 수정(Long boardNo)하고 넘겨서 전부 서비스에서 처리하도록 -> 구현클래스 수정
	// 목록조회 -> BoardDTO가 여러개 오겠지? -> 나중에 서비스에서 반환형 수정하고 나서 여기도 수정함
	Map<String, Object> findAll(Long boardNo);
	// 
	/*
	 * 개발자들이 선호하는 메소드 작명형태가 있음 -> 보통 기술스택에 따라 정한다, 회사가서 어떤거 쓸 지 모르니 이것저것 해보기
	 * 회사 가면 개발팀에 네이밍컨벤션 매뉴얼이 있음, 진짜 이상한 회사 가지 않는 이상 웬만하면 있음
	 * 선생님께도 회사에서 쓰시던 네이밍컨벤션 매뉴얼 있는데 기밀유지서약 있어서 보여주시면 안된다네요 헤헤
	 * 
	 * 예를 들어서 Board에 있는걸 조회해오고싶다면
	 * selectBoardList();
	 * findAll();
	 * selectAll();
	 * 
	 */
	
	// 게시글 작성
	/*
	 * 게시글을 새롭게 insert, board에 insert
	 * insertBoard();
	 * save();
	 * 
	 */
	int save(BoardDTO board, MultipartFile upfile, HttpSession session);
	
	// 게시글 상세보기(조회수 증가), select 해서 가겠지? 돌아올것은 DTO 한개
	/*
	 * board를 select한다면
	 * selectBoard();
	 * findByXXXX();
	 * -> XXX에 id, pk역할을 하는 친구들이 들어감
	 * 
	 */
	BoardDTO findByBoardNo(Long boardNo);
	
	// 게시글 삭제하기
	/*
	 * deleteBoard();
	 * deleteByXXXX();
	 * -> 여기도 XXXX에 id, pk역할이 들어감
	 * 
	 */
	int deleteByBoardNo(Long boardNo);
	
	// 게시글 수정하기
	/*
	 * updateBoard();
	 * updateByXXXX();
	 * 
	 */
	int update(BoardDTO board);
	
	// ----- 이 아래는 댓글 관련 작업할건데 작업속도 보고 결정(수업 or 숙제)
	// 댓글 서비스
	
	int insertReply(ReplyDTO reply, HttpSession session); // 이거 추가하고 서비스 구현체 -> 매개변수 수정 -> 반환형 수정

}
