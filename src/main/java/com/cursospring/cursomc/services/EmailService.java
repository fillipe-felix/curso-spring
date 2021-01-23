package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Cliente;
import com.cursospring.cursomc.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;


public interface EmailService {

    void sendOrderConfirmationEmail(Pedido obj);

    void sendMail(SimpleMailMessage msg);

    void sendOrderConfirmationHtmlEmail(Pedido obj);

    void sendHtmlEmail(MimeMessage msg);

    void sendNewPasswordEmail(Cliente cliente, String newPass);
}
