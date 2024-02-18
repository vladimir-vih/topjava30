package ru.javawebinar.topjava.util.datetimeformatter;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    private LocalTime defaultTime;

    public CustomTimeFormatter(LocalTime time) {
        this.defaultTime = time;
    }
    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.toString();
    }
}
