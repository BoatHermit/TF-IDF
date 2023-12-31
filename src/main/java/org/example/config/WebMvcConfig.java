package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC通用配置
 *
 * @author yinzihang
 * @since 0.1
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] ORIGINS =
            new String[] {
                    "http://localhost:8000",
                    "https://localhost:8000",
            };
    private static final String[] METHODS =
            new String[] { "GET", "POST", "PUT", "DELETE" };

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ORIGINS)
                .allowCredentials(true)
                .allowedMethods(METHODS)
                .maxAge(3600);
    }

}