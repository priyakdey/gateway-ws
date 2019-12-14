package com.photoapp.gateway.ws.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.photoapp.gateway.ws.security.filter.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class GatewayWsApplicationSecurityConfigurer extends WebSecurityConfigurerAdapter {

	private Environment env;
	
	@Autowired
	public GatewayWsApplicationSecurityConfigurer(Environment env) {
		this.env = env;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/users-ws/signup").permitAll()
			.antMatchers(HttpMethod.POST, "/users-ws/login").permitAll()
			.antMatchers("/users-ws/h2-console").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.addFilter(new AuthorizationFilter(authenticationManager(), env));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	

}
