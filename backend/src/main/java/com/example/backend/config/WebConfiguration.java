package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/PostMedia/**")
                .addResourceLocations("file:///C:/Social-Media-Backend/media/post_media/")
                .setCacheControl(CacheControl.noCache());

        registry.addResourceHandler("/MessageMedia/**")
                .addResourceLocations("file:///C:/Social-Media-Backend/media/message_media/")
                .setCacheControl(CacheControl.noCache());

        registry.addResourceHandler("/CommentMedia/**")
                .addResourceLocations("file:///C:/Social-Media-Backend/media/comment_media/")
                .setCacheControl(CacheControl.noCache());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Fix issue with path variable trailing slash
        configurer.setUseTrailingSlashMatch(true);
    }
}
