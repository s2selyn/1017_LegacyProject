package com.kh.spring.member.model.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordEncoder {
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	public String encode(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
	
	public boolean matches(String rawPassword, String encodePassword) {
		return passwordEncoder.matches(rawPassword, encodePassword); // 여기 매개변수 잘못 전달해서 문제 생겼음
	}
	
	// 암호화 알고리즘에 대한 책임을 이곳에서 담당하고 있음
	// 서비스 입장에서는 암호화에 대한 책임이 분리되었으므로 어떤 알고리즘을 쓰는지 모름
	// 직접적으로 무슨 타입인지 알고있으면 알고리즘이 바뀔때마다 서비스 내부 코드가 바뀌어야했음
	// 알고리즘이 바뀌면 여기만 수정되고, 서비스는 뭐 여기가 바뀌든말든 모르고 상관없게됨

}
