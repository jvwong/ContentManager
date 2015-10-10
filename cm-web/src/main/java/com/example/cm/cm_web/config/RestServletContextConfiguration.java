package com.example.cm.cm_web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_web.config.annotation.RestEndpointAdvice;

@Configuration
@EnableWebMvc 
@ComponentScan(
        basePackages = {"com.example.cm.cm_web.rest"},
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter({
        	RestEndpoint.class,
        	RestEndpointAdvice.class})
)
public class RestServletContextConfiguration extends WebMvcConfigurerAdapter {
}
