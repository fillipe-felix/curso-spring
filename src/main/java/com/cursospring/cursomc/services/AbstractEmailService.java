package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;


public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    @Override
    public void sendOrderConfirmationEmail(Pedido obj) {
        SimpleMailMessage simpleMailMessage = prepareSimpleMailMessageFromPedido(obj);
        sendMail(simpleMailMessage);
    }

    protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(obj.getCliente().getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("Pedido confirmado! Código do pedido: " + obj.getId());
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(obj.toString());
        return simpleMailMessage;
    }
}
