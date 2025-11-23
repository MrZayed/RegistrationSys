package com.example.RegistrationSys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail (String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("System OTP Code");
        message.setText("Your OTP is: " + otp + "\nPlease take care \n \n It will expire in 5 minutes.");
        mailSender.send(message);
    }
}
