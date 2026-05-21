package com.RamonVale.notification_service.Config;

import com.RamonVale.notification_service.Domain.Notificador.EmailSender;
import com.RamonVale.notification_service.Domain.Notificador.SmsSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class NoticationConfig {

  @Bean
  public EmailSender emailSender(JavaMailSender mailSender){
    return new EmailSender(mailSender);
  }

  @Bean
  public SmsSender smsSender(
    @Value("${twilio.account-sid}") String accountSid,
    @Value("${twilio.auth-token}") String authToken,
    @Value("${twilio.phone-number}") String fromNumber)
    {
      return new SmsSender(accountSid, authToken, fromNumber);


  }
}
