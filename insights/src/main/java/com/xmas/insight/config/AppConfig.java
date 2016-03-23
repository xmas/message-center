package com.xmas.insight.config;

import com.xmas.entity.EntityHelper;
import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.persistence.ValidationMode;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@ComponentScan(basePackages = "com.xmas")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.xmas.insight.dao")
@EnableSpringDataWebSupport
public class AppConfig {

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

    @Value("${questions.api.url}")
    String questionsApiUrl;

    @Autowired
    private ApplicationContext context;


    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }

    private Properties hibernateProperties(){
        Properties properties = new Properties();
        properties.put("hibernate.show_sql",showSql);
        properties.put("hibernate.dialect",dialect);
        properties.put("hibernate.hbm2ddl.auto",hbm2ddl);
        return properties;
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
        entityManagerFactoryBean.setPackagesToScan("com.xmas.insight.entity", "com.xmas.insight.dao");
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
        props.setLocations(new FileSystemResource(System.getProperty("user.home")+ "/.insights/properties/app.properties"),
                new FileSystemResource(System.getProperty("user.home")+ "/.insights/properties/jdbc.properties"),
                new FileSystemResource(System.getProperty("user.home")+ "/.insights/properties/hibernate.properties"));
        return props;
    }

    @Bean
    EntityHelper<Insight, InsightEvaluator> insightHelper(){
        return new EntityHelper<>(Insight.class, context);
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new ExtendedMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }

    private static class ExtendedMultipartResolver extends CommonsMultipartResolver {

        public static final Set<String> ALLOWED_MULTIPART_METHODS = new HashSet<String>() {{
            add("POST");
            add("PUT");
            add("PATCH");
        }};

        @Override
        public boolean isMultipart(HttpServletRequest request) {
            return  request != null &&
                    ALLOWED_MULTIPART_METHODS.contains(request.getMethod().toUpperCase()) &&
                    FileUploadBase.isMultipartContent(new ServletRequestContext(request));
        }
    }

    @Bean
    public ResourceProcessor<Resource<Insight>> personProcessor() {
        return new ResourceProcessor<Resource<Insight>>() {
            @Override
            public Resource<Insight> process(Resource<Insight> resource) {
                long qId = resource.getContent().getEvaluator().getQuestionId();
                List<Link> links = resource.getLinks().stream()
                        .filter(l -> l.getVariableNames().contains("insight"))
                        .collect(Collectors.toList());
                resource.removeLinks();
                resource.add(links);
                resource.add(new Link("" + questionsApiUrl + "/" + qId, "question"));
                return resource;
            }
        };
    }


}
