package com.cursospring.cursomc.config;

import com.cursospring.cursomc.services.DBService;
import com.cursospring.cursomc.services.EmailService;
import com.cursospring.cursomc.services.MockEmailService;
import com.cursospring.cursomc.services.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDatabase() throws ParseException {

        dbService.instantiateDatabase();
        return true;
    }

    /*@Bean
    public EmailService emailService(){
        return new MockEmailService();
    }*/

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }
}
