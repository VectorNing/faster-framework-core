package com.github.faster.framework.spring.boot.autoconfigure.auth;

import com.github.faster.framework.core.auth.JwtService;
import com.github.faster.framework.core.auth.app.interceptor.AppAuthInterceptor;
import com.github.faster.framework.spring.boot.autoconfigure.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({AuthProperties.class, ProjectProperties.class})
public class AuthAutoConfiguration {
    @Autowired
    private AuthProperties authProperties;
    @Autowired
    private ProjectProperties projectProperties;
    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public JwtService jwtService() {
        JwtService jwtService = new JwtService();
        jwtService.setBase64Security(this.projectProperties.getBase64Secret());
        jwtService.setMultipartTerminal(authProperties.isMultipartTerminal());
        jwtService.setEnv(env);
        return jwtService;
    }

    @Configuration
    @ConditionalOnProperty(prefix = "faster.auth", name = "mode", havingValue = "app", matchIfMissing = true)
    public static class AppAuthConfiguration {
        @Configuration
        public static class InterceptorConfiguration implements WebMvcConfigurer {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new AppAuthInterceptor()).addPathPatterns("/**");
            }
        }
    }
}
