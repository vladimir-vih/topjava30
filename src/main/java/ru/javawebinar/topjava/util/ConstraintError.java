package ru.javawebinar.topjava.util;

public enum ConstraintError {
    EMAIL_DUPLICATE("email", "user.email.duplicate"),
    DATE_TIME_DUPLICATE("datetime", "meal.dateTime.duplicate");

    private String fieldName;
    private String messageCode;

    ConstraintError(String name, String code) {
        this.fieldName = name;
        this.messageCode = code;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
