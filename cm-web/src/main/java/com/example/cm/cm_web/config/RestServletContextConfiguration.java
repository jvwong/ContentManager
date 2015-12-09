package com.example.cm.cm_web.config;

import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_web.config.annotation.RestEndpoint;
import com.example.cm.cm_web.config.annotation.RestEndpointAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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

        @Autowired
        SpringValidatorAdapter validator;

        @Bean
        Jaxb2Marshaller marshaller(){
                Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
                marshaller.setClassesToBeBound(Article.class);
                return marshaller;
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "HEAD", "POST", "DELETE", "PATCH", "GET", "OPTIONS")
                        .exposedHeaders(
                                "X-AUTH-TOKEN",
                                "Content-Type",
                                "Date",
                                "Expires")
                        .allowCredentials(false).maxAge(3600)
                ;
        }

        // Tell web MVC to use the validator in RootContextConfiguration
        @Override
        public Validator getValidator()
        {
                return this.validator;
        }
}
