package tunequest.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("http://localhost:3000", "https://tunequestapigateway.azure-api.net", "https://purple-smoke-0c75a6403.5.azurestaticapps.net/") // Allow requests from frontend, api gateway and deployed frontend
                        .allowedMethods("GET", "POST") // Allow HTTP methods
                        .allowedHeaders("*"); // Allow all headers
            }
        };
    }
}
