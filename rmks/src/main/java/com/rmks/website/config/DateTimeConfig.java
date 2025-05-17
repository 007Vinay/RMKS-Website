package com.rmks.website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeConfig {

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (source == null || source.isEmpty()) {
                    return null;
                }
                try {
                    return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (Exception e) {
                    try {
                        // Try parsing without seconds
                        return LocalDateTime.parse(source + ":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (Exception ex) {
                        System.out.println("Error parsing date: " + source);
                        ex.printStackTrace();
                        return null;
                    }
                }
            }
        };
    }
} 