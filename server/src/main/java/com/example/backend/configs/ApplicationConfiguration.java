package com.example.backend.configs;

import com.cloudinary.Cloudinary;
import com.example.backend.commons.AppConstants;
import com.example.backend.modules.user.constant.UserConstants;
import com.example.backend.modules.user.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfiguration {
    private final UserService userService;

    public ApplicationConfiguration(UserService userService){
        this.userService = userService;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userService.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(UserConstants.USER_NOT_FOUND));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedMethod("*"); // Cấu hình cho phép tất cả các phương thức (GET, POST, PUT, DELETE, v.v.)
        config.addAllowedHeader("*"); // Cấu hình cho phép tất cả các tiêu đề
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public Cloudinary cloudinary(){
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name", AppConstants.CLOUD_NAME);
        config.put("api_key", AppConstants.API_KEY);
        config.put("api_secret", AppConstants.API_SECRET);

        return new Cloudinary(config);
    }
}
