package com.example.ddingjakyo_be.domain.member.service;

import com.example.ddingjakyo_be.domain.member.controller.dto.response.EmailConfirmResponse;
import com.example.ddingjakyo_be.domain.member.controller.dto.request.EmailConfirmRequest;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender emailSender;
  private final ResourceLoader resourceLoader;
  private final Map<String, String> authBoard = new ConcurrentHashMap<>();
  private static final String AUTH_CODE_PLACEHOLDER = "YOUR_AUTH_CODE";
  private static final String verificationCode = createKey();
  private static final String emailDelimiter = "@";
  private static final String univDomain = "mju.ac.kr";


  public EmailConfirmResponse sendEmail(String to) throws Exception {
    boolean success = hasValidUnivDomain(to);
    String message = "학교 이메일 형식에 맞지 않습니다.";

    if (success) {
      MimeMessage mail = createMailContent(to);

      try {
        emailSender.send(mail);
        message = "인증 코드가 성공적으로 전송되었습니다.";
      } catch (MailException e) {
        e.printStackTrace();
        success = false;
        message = "이메일 전송에 실패했습니다.";
      }
    }

    return EmailConfirmResponse.of(success, message);
  }

  public EmailConfirmResponse checkVerificationCode(EmailConfirmRequest emailConfirmRequest) {
    String email = emailConfirmRequest.getEmail();
    String memberAuthCode = emailConfirmRequest.getAuthCode();

    String authCode = authBoard.get(email);

    boolean success = Objects.equals(memberAuthCode, authCode);
    String message = null;

    if (!success) {
      message = "인증 코드를 다시 확인해주세요.";
    }

    return EmailConfirmResponse.of(success, message);
  }

  private MimeMessage createMailContent(String to)
      throws MessagingException, UnsupportedEncodingException {

    MimeMessage message = emailSender.createMimeMessage();

    message.addRecipients(RecipientType.TO, to);
    message.setSubject("띵작교 이메일 인증");

    String content = "";

    try {
      Resource resource = resourceLoader.getResource("classpath:static/email-certification.txt");
      content = Files.readString(Path.of(resource.getURI()));
    } catch (IOException e) {
      System.out.println("File Read Error");
    }

    String authCode = verificationCode;
    // 멤버의 이메일 정보와 인증 코드를 저장
    authBoard.put(to, authCode);
    // YOUR_AUTH_CODE 부분을 인증 코드로 변경
    String emailText = content.replace(AUTH_CODE_PLACEHOLDER, authCode);
    message.setText(emailText, "utf-8", "html");
    message.setFrom(new InternetAddress("gopremium0131@gmail.com", "띵작교"));

    return message;
  }

  private static String createKey() {
    StringBuilder key = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < 8; i++) {
      int index = random.nextInt(3); // 0~2 까지 랜덤

      switch (index) {
        case 0 -> key.append((char) ((random.nextInt(26)) + 97));
        //  a~z  (ex. 1+97=98 => (char)98 = 'b')
        case 1 -> key.append((char) ((random.nextInt(26)) + 65));
        //  A~Z
        case 2 -> key.append(random.nextInt(10));
        // 0~9
      }
    }

    return key.toString();
  }

  // 이메일의 대학명 체크
  private boolean hasValidUnivDomain(String email) {
    StringTokenizer st = new StringTokenizer(email, emailDelimiter);
    String local = st.nextToken();
    String domain = st.nextToken();

    return domain.equals(univDomain);
  }
}
