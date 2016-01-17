package com.xmas.config;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebMvc
public class MVCConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/R/data/**").addResourceLocations("classpath:/R/data/");
        registry.addResourceHandler("/questions/data/**").addResourceLocations("classpath:/questions/");
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new ExtendedMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {

    }

    private static class ExtendedMultipartResolver extends CommonsMultipartResolver {

        public static final Set<String> ALLOWED_MULTIPART_METHODS = new HashSet<String>() {{
            add("POST");
            add("PUT");
            add("PATCH");
        }};

        @Override
        public boolean isMultipart(HttpServletRequest request) {
            if (request == null) return false;
            return ALLOWED_MULTIPART_METHODS.contains(request.getMethod().toUpperCase()) &&
                    FileUploadBase.isMultipartContent(new ServletRequestContext(request));
        }
    }

}
