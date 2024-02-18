package ru.javawebinar.topjava.util.datetimeformatter;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDateTimeFormat {
    DefaultValue defaultValue();

    public enum DefaultValue {
        DATE_MIN,
        DATE_MAX,
        TIME_MIN,
        TIME_MAX
    }
}
