package com.xmas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;


@Configuration
@ComponentScan(basePackages = "com.xmas")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.xmas.dao")
@Import({MVCConfiguration.class, SchedulerConfig.class})
public class AppContext {

    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.host}")
    private String host;
    @Value("${jdbc.user}")
    private String dbUser;
    @Value("${jdbc.password}")
    private String dbPassword;

    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddl;

    @Value("${mail.host}")
    private String mailServerHost;
    @Value("${mail.port}")
    private Integer mailServerPort;
    @Value("${mail.user}")
    private String mailServerUser;
    @Value("${mail.password}")
    private String mailServerPassword;

    private Properties hibernateProperties(){
        Properties properties = new Properties();
        properties.put("hibernate.show_sql",showSql);
        properties.put("hibernate.dialect",dialect);
        properties.put("hibernate.hbm2ddl.auto",hbm2ddl);
        return properties;
    }


    @Bean
    JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", "true");
        mailProperties.setProperty("mail.smtp.starttls.enable", "true");
        mailProperties.setProperty("mail.smtp.auth", "true");

        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setDefaultEncoding("utf8");
        mailSender.setHost(mailServerHost);
        mailSender.setPort(mailServerPort);
        mailSender.setUsername(mailServerUser);
        mailSender.setPassword(mailServerPassword);
        mailSender.setProtocol("smtp");

        return mailSender;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver());
        return templateEngine;
    }

    /**
     * THYMELEAF: Template Resolver for email templates.
     */
    private TemplateResolver emailTemplateResolver() {
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/email/");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setOrder(1);
        return templateResolver;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(host);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com/xmas/entity", "com/xmas/dao");
        entityManagerFactoryBean.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        entityManagerFactoryBean.setValidationMode(ValidationMode.NONE);
        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() throws IOException {
        PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new FileSystemResource(System.getProperty("user.home")+ "/.pushmessages/properties/app.properties"),
                           new FileSystemResource(System.getProperty("user.home")+ "/.pushmessages/properties/jdbc.properties"),
                           new FileSystemResource(System.getProperty("user.home")+ "/.pushmessages/properties/hibernate.properties"));
        return props;
    }

}
