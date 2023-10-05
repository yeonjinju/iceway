package com.delivery.iceway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // HttpSecurity 객체가 메소드의 인자로 주입되며 이 객체를 이용해 보안 설정을 진행합니다.
        SecurityFilterChain chain = httpSecurity
                // 기본적인 HTTP 인증을 사용하지 않도록 설정
                .httpBasic(basic -> basic.disable())
                // CSRF 보호를 비활성화 (상황에 따라서는 활성화 고려)
                .csrf(csrf -> csrf.disable())
                // HTTP 요청에 대한 접근 제어 설정을 시작
                .authorizeHttpRequests(request -> request
                        // "/users/**" 패턴의 URL은 "users" 역할을 가진 사용자만 접근 가능
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        // 루트 URL, 로그인 폼, 로그인 요청은 모든 사용자에게 허용
                        .antMatchers("/users/loginform", "/users/login").permitAll()
                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated())
                // 폼 기반 로그인 설정
                .formLogin(formLogin -> formLogin
                        // 로그인 폼의 URL
                        .loginPage("/users/loginform")
                        // 로그인 성공 시 리다이렉트 될 기본 URL
                        .defaultSuccessUrl("/admin/status", true))
                // 로그아웃 설정
                .logout(logout -> logout
                        // 로그아웃 요청을 처리할 URL
                        .logoutUrl("/users/logout")
                        // 로그아웃 성공 시 리다이렉트 될 URL
                        .logoutSuccessUrl("/users/loginform"))
                .build(); // SecurityFilterChain 객체 생성
        return chain; // 생성된 SecurityFilterChain 객체 반환
    }

    // 비밀번호를 암호화 해주는 객체를 bean 으로 만든다.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 세션이벤트 관련객체를 bean 으로 만든다.
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // 인증 메니저 객체를 bean 으로 만든다.
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }
}
