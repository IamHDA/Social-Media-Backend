package com.example.backend.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class Format {
    public static String formatTimeAgo(LocalDateTime sentTime) {
        LocalDateTime now = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(sentTime, now);
        if (years > 0) return years + " năm trước";

        long months = ChronoUnit.MONTHS.between(sentTime, now);
        if (months > 0) return months + " tháng trước";

        long days = ChronoUnit.DAYS.between(sentTime, now);
        if (days > 0) return days + " ngày trước";

        long hours = ChronoUnit.HOURS.between(sentTime, now);
        if (hours > 0) return hours + " giờ trước";

        long minutes = ChronoUnit.MINUTES.between(sentTime, now);
        if (minutes > 0) return minutes + " phút trước";

        return "Vừa xong";
    }
}
