package com.example.cm.cm_jcrrepository.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.jcr.SimpleCredentials;


@Configuration
@ComponentScan(
	basePackages="com.example.cm.cm_jcrrepository.jcr"
)
@PropertySource("classpath:environment.properties")
@ImportResource("classpath:beans-jcr.xml")
public class JCRDataConfig {

	@Autowired
	Environment env;

	@Bean
	public SimpleCredentials credentials(){
		String username = env.getProperty("username");
		String password = env.getProperty("password");
		SimpleCredentials credentials
				= new SimpleCredentials(username, password.toCharArray());
		return credentials;
	}

////	@Bean
////	RepositoryFactoryBean repository(){
////		RepositoryFactoryBean repositoryFactoryBean
////				= new RepositoryFactoryBean();
////	}

}
