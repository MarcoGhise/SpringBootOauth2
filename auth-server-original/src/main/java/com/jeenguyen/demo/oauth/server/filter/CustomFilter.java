package com.jeenguyen.demo.oauth.server.filter;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.jeenguyen.demo.oauth.bean.User;

public class CustomFilter extends GenericFilterBean {

	private AuthenticationManager authenticationManager;
	
	private static final String COOKIE_NAME = "authentication";
	
	public CustomFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse httpReponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		User user = getCookieCredential(httpRequest);
		
		if (user != null) {
			
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					user.getPrincipal(), user.getCredential(),
					AuthorityUtils.createAuthorityList("ROLE_USER"));

			Authentication authResult = this.authenticationManager
					.authenticate(authRequest);
			/*
			 * User Authenticate
			 */
			SecurityContextHolder.getContext().setAuthentication(authResult);
		}
		else
		{
			Cookie cookie = new Cookie(COOKIE_NAME, null); 
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
			httpReponse.addCookie(cookie);
		}

		chain.doFilter(httpRequest, httpReponse);

	}

	private User getCookieCredential(HttpServletRequest httpRequest) {
		
		Cookie[] cookies = httpRequest.getCookies();

		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(COOKIE_NAME)) {
					byte[] decodedBytes = Base64.getDecoder().decode(
							cookie.getValue());
					String decodedString = new String(decodedBytes);
					String[] credentialSplitted = decodedString.split(":");
					return new User(credentialSplitted[0], credentialSplitted[1]);
				}
			}
		
		return null;
	}

}
