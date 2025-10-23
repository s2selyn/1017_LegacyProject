package com.kh.spring.ajax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Bean으로 등록
public class AjaxForwardController {
	
	@GetMapping("page") // 포워딩
	public String toAjax() {
		return "ajax/ajax";
	}
	
}
