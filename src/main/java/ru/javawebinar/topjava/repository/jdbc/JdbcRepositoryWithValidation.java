package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class JdbcRepositoryWithValidation {
    private static final Logger log = LoggerFactory.getLogger(JdbcRepositoryWithValidation.class);
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected static  <T> void validateObject(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                log.error(violation.getMessage());
            }
            throw new ConstraintViolationException(violations);
        }
    }
}
