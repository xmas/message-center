package com.xmas.notifiers.email;

import com.xmas.entity.Message;
import com.xmas.exceptions.NotificationSendingException;
import com.xmas.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
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
        try {
            final Context ctx = new Context();
            ctx.setVariable("title", message.getTitle());
            ctx.setVariable("subtitle", message.getSubTitle());
            ctx.setVariable("text", message.getMessage());
            ctx.setVariable("image", message.getIcon());

            final String htmlContent = this.templateEngine.process("template.html", ctx);

            MimeMessage mailMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, false, "utf8");

            helper.setText(htmlContent, true);
            helper.setSubject(message.getTitle());

            helper.setFrom("vasyl.danyliuk.1@gmail.com");
            helper.setTo(email);
            
            return mailMessage;
        } catch (MessagingException me) {
            throw new NotificationSendingException(me.getMessage());
        }
    }
}
