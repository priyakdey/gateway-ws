package com.photoapp.gateway.ws.security.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	private Environment env;
	
	@Autowired
	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment env) {
		super(authenticationManager);
		this.env = env;
	}

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String authHeader = request.getHeader(this.env.getProperty("authorization.token.header.name"));
		
		if (authHeader == null || !authHeader.startsWith(this.env.getProperty("authorization.token.header.prefix"))) {
			chain.doFilter(request, response);
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}


	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String authHeader = request.getHeader(this.env.getProperty("authorization.token.header.name"));
		
		if(authHeader == null) {
			return null;
		}
		
		String token = authHeader.split(" ")[1];
		
		String userName = Jwts.parser()
							  .setSigningKey(this.env.getProperty("token.secret.key"))
							  .parseClaimsJws(token)
							  .getBody()
							  .getSubject();
		
		if (userName == null) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(userName, null, new ArrayList<>());
							  
	}

	
}
