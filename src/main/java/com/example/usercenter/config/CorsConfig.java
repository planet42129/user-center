package com.example.usercenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/**
 * @author hyh
 * @date 2024/4/7
 */

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 创建配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 允许跨域的源，可以设置为具体的域名或 "*" 表示允许所有来源
        config.addAllowedOrigin("*");
        // 允许的请求方法
        config.addAllowedMethod("*");
        // 允许的头信息
        config.addAllowedHeader("*");
        // 允许携带凭证（例如 Cookie）
        config.setAllowCredentials(true);
        // 设置 URL 映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回 CorsFilter
        return new CorsFilter(source);
    }

}

