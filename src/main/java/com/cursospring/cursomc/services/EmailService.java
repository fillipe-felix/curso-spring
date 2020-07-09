package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;


public interface EmailService {

    void sendOrderConfirmationEmail(Pedido obj);

    void sendMail(SimpleMailMessage msg);
}
