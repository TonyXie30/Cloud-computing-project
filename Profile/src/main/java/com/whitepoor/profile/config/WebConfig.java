package com.whitepoor.profile.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String allowedIp = "http://localhost:9527";
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9527","http://localhost:9528",
                        "http://192.168.56.1:9527","http://192.168.56.1:9528",
                        "http://124.71.57.244:9527","http://124.71.57.244:9528",
                        "https://124.71.57.244:443","https://124.71.57.244:80",
                        "https://localhost:443","https://localhost:80",
                        "https://localhost:9527","https://localhost:9528","http://localhost","https://localhost","https://124.71.57.244","http://124.71.57.244")

                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
