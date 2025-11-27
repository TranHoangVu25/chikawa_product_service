package com.example.product_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class SecurityConfig {

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
////        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedOrigin("http://localhost:5173");
//
//        // Dòng này cần thiết để hỗ trợ `credentials: 'include'`
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedHeader("*");
//
//
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }

}

