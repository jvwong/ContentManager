package com.example.cm.cm_web.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@EnableTransactionManagement
@ComponentScan(
        basePackages = "com.example.cm",
        excludeFilters =
        @ComponentScan.Filter({Controller.class, ControllerAdvice.class})
)
@Import({ SecurityConfig.class })
public class RootContextConfiguration {

    @Bean
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(-1);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames(
                "/WEB-INF/i18n/messages"
        );
        return messageSource;
    }

    // // Explicitly declare the validation method
    // // Set the validation messages to the same as the internationalization
    // @Bean
    // public LocalValidatorFactoryBean localValidatorFactoryBean()
    // {
    //     LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    //     validator.setValidationMessageSource(this.messageSource());
    //     return validator;
    // }
    //
    // // Enable method validation
    // @Bean
    // public MethodValidationPostProcessor methodValidationPostProcessor()
    // {
    //     MethodValidationPostProcessor processor =
    //             new MethodValidationPostProcessor();
    //     processor.setValidator(this.localValidatorFactoryBean());
    //     return processor;
    // }

}
