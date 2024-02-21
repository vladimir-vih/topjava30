package ru.javawebinar.topjava.web.datetimeformatter;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) {
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.toString();
    }
}
