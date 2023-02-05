package smilegate.plop.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import smilegate.plop.auth.domain.UserEntity;
import smilegate.plop.auth.domain.UserRepository;
import smilegate.plop.auth.dto.request.RequestEmailVerification;
import smilegate.plop.auth.dto.response.ErrorResponseDto;
import smilegate.plop.auth.dto.response.ResponseDto;
import smilegate.plop.auth.exception.ErrorCode;
import smilegate.plop.auth.exception.WithdrawalUserException;
import smilegate.plop.auth.security.JwtTokenProvider;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import static java.awt.Color.blue;

@Service
@Slf4j
public class MailService {

    private JavaMailSender mailSender;

    private UserRepository userRepository;
    private RedisService redisService;

    @Autowired
    public MailService(UserRepository userRepository, RedisService redisService, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.mailSender = mailSender;
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        String authNum = "";
        for (int i=0; i<6; i++) {
            String num = Integer.toString(random.nextInt(10));

            buffer.insert(buffer.length(), num);
        }
        authNum = buffer.toString();
        log.error("key: " + authNum);
        return authNum;
    }

    public boolean checkInfo(RequestEmailVerification info){
        UserEntity user = userRepository.findByUserId(info.getUserId());
        if (user.getEmail().equals(info.getEmail()) )
            return true;
        return false;
    }
    @Async
    public void send(RequestEmailVerification info, String subject) {
        if (!checkInfo(info))
            throw new UsernameNotFoundException("정보가 일치하지 않습니다.");
        String authNum = createCode();
        MimeMessage message = mailHelper(info,subject, authNum);

        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(info.getEmail());
        smm.setFrom("jhl81094@gmail.com");
        smm.setSubject(subject);
        smm.setText(message.toString());

        mailSender.send(message);

        log.error("send ok");
    }

    public MimeMessage mailHelper(RequestEmailVerification info, String subject,String authNum) {
        MimeMessage message = this.mailSender.createMimeMessage();
        try {
            message.setSubject(subject,"UTF-8");
            String htmlStr = "<h1 >" + subject  + "</h1><br>"
                    +"<h2 style=\"color:blue\"> 인증 코드: " + authNum + "</h2>";
            message.setText(htmlStr, "UTF-8", "html");
            message.addRecipients(Message.RecipientType.TO, info.getEmail());

        } catch (MessagingException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        return message;
    }


}
