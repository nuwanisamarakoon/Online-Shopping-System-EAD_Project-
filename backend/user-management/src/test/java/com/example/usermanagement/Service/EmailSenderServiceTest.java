package com.example.usermanagement.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@Disabled
public class EmailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    }

    @Test
    void testSendEmailSuccessfully() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String content = "This is a test content.";

        assertDoesNotThrow(() -> {
            emailSenderService.sendEmail(to, subject, content);
        });

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailFailure() throws MessagingException, UnsupportedEncodingException {

        String to = "test@example.com";
        String subject = "Test Subject";
        String content = "This is a test content.";

        doThrow(new MessagingException("Mocked MessagingException"))
                .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> {
            emailSenderService.sendEmail(to, subject, content);
        });

        verify(mailSender, times(1)).send(mimeMessage);
    }


}
