package com.photoapp.gateway.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class GatewayWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayWsApplication.class, args);
	}
	
	@Bean
	public HttpTraceRepository getHttpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}

}
