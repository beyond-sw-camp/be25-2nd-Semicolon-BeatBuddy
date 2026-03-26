package com.beyond.beatbuddy.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity /*,
												   JwtTokenProvider jwtTokenProvider */) throws Exception {
		httpSecurity
				// CSRF 비활성화
				.csrf(AbstractHttpConfigurer::disable)
				// CORS 설정 적용
				.cors(cors->cors.configurationSource(corsConfigurationSource()))
				// HTTP Basic 인증 안 씀
				.httpBasic(AbstractHttpConfigurer::disable)
				// form 로그인 안 씀
				.formLogin(AbstractHttpConfigurer::disable)
				// JWT 쓰니까 세션 안 씀
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/api/v1/auth/**",
								"/images/**",
								"/default-profile.png",
								"/default-group.png",
								"/swagger-ui/**",
								"/v3/api-docs/**"
						).permitAll()
						// 나머지는 인증 필요
						.anyRequest().authenticated()
				);
				// JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가하는 설정
				// JwtAuthenticationFilter에서 인증이 정상적으로 처리되면 UsernamePasswordAuthenticationFilter는 자동으로 통과한다.
//				.addFilterBefore(
//						new JwtAuthenticationFilter(jwtTokenProvider),
//						UsernamePasswordAuthenticationFilter.class
//				);

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		/* CORS = 다른 주소에서 오는 요청 허용/차단 설정 */
		CorsConfiguration config = new CorsConfiguration();
		// 어떤 주소에서 오는 요청 허용할지
		config.addAllowedOrigin("http://localhost:5173"); // Vue 기본 포트
		// 어떤 HTTP 메서드 허용할지
		config.addAllowedMethod("*");  // GET, POST, PUT, DELETE 전부
		// 어떤 헤더 허용할지
		config.addAllowedHeader("*"); // Authorization 등 전부
		// 쿠키/인증정보 허용할지
		config.setAllowCredentials(true); // HttpOnly Cookie 때문에 필수
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 모든 경로에 CORS 설정 적용
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
