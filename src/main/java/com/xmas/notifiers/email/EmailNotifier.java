package com.xmas.notifiers.email;

import com.xmas.entity.Message;
import com.xmas.exceptions.NotificationSendingException;
import com.xmas.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailNotifier implements Notifier {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void pushMessage(Message message, List<String> emails) {
        emails.forEach(email -> mailSender.send(prepareMessage(message, email)));
    }

    MimeMessage prepareMessage(Message message, String email) {
/*        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        final String htmlContent = this.templateEngine.process("email-inlineimage.html", ctx);*/
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            mailMessage.setSubject(message.getTitle());
            mailMessage.setText(message.getMessage());
            mailMessage.setFrom(new InternetAddress("vasyl.danyliuk.1@gmail.com"));
            mailMessage.setRecipient(RecipientType.TO, new InternetAddress(email));
            return mailMessage;
        }catch (MessagingException me){
            throw new NotificationSendingException(me.getMessage());
        }
    }
}
