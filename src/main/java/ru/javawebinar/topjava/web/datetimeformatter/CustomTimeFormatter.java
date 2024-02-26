package ru.javawebinar.topjava.web.datetimeformatter;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {

    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.toString();
    }
}
