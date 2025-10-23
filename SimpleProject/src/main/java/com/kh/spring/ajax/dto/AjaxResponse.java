package com.kh.spring.ajax.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjaxResponse {
	// ajax 요청왔을때 응답할 객체
	
	private String code;
	private String message;
	private Object data;
	
	// 보통 응답용 객체 만들때 code, message, data 꼭 들어간다
	// 성공여부 알려주려고 success(boolean) 넣는 경우 있고 응답시간 알려주려고 시간 넣는 경우도 있음
	
}
