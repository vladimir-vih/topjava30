package ru.javawebinar.topjava.util.datetimeformatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<CustomDateTimeFormat> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    private Formatter<?> getFormatter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        switch (annotation.defaultValue()) {
            case DATE_MIN -> {
                return new CustomDateFormatter(LocalDate.MIN);
            }
            case DATE_MAX -> {
                return new CustomDateFormatter(LocalDate.MAX);
            }
            case TIME_MIN -> {
                return new CustomTimeFormatter(LocalTime.MIN);
            }
            case TIME_MAX -> {
                return new CustomTimeFormatter(LocalTime.MAX);
            }
        }
        return null;
    }
}
