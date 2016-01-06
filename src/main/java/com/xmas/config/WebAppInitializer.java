package com.xmas.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter",
                new CharacterEncodingFilter());
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");

        FilterRegistration.Dynamic entityManagerInView = servletContext.addFilter("entityManagerInViewFilter",
                new OpenEntityManagerInViewFilter());
        entityManagerInView.setInitParameter("entityManagerFactoryBeanName", "entityManagerFactory");
        entityManagerInView.setInitParameter("flushMode", "AUTO");
        entityManagerInView.addMappingForUrlPatterns(null, true, "/*");

        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File(System.getProperty("user.home")+ "/.pushmessages/properties/log4j2.xml");
        if(file.exists())
            context.setConfigLocation(file.toURI());
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {AppContext.class, MVCConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    private void initLogging(){

    }

}