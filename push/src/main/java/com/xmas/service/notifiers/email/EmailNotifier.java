package com.xmas.service.notifiers.email;

import com.xmas.entity.Message;
import com.xmas.exceptions.NotificationSendingException;
import com.xmas.service.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public static final boolean FORMAT_HTML = true;
    public static final String DEFAULT_CHARSET = "utf8";


    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    @Value("${user.home}/.pushmessages/email/")
    private String templatesDir;



    @Override
    public void pushMessage(Message message, List<String> emails) {
        emails.forEach(email -> mailSender.send(prepareMessage(message, email)));
    }

    MimeMessage prepareMessage(Message message, String email) {
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, false, DEFAULT_CHARSET);
            helper.setText(prepareText(message), FORMAT_HTML);
            helper.setSubject(message.getTitle());
            helper.setTo(email);

            return mailMessage;
        } catch (MessagingException me) {
            throw new NotificationSendingException("Can't create email message.", me);
        }
    }

    private Context prepareContext(Message message){
        Context context = new Context();

        context.setVariable("title", message.getTitle());
        context.setVariable("subtitle", message.getSubTitle());
        context.setVariable("text", message.getMessage());
        context.setVariable("icon", message.getIcon());
        context.setVariable("redirectUrl", message.getNotificationAppURL());

        return context;
    }

    private String prepareText(Message message){
        try {
            return this.templateEngine.process(templatesDir + "template.html", prepareContext(message));
        }catch (Exception e){
            return this.templateEngine.process("template.html", prepareContext(message));
        }
    }
}
