package ru.javawebinar.topjava.util;


import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ConstraintError.DATE_TIME_DUPLICATE;
import static ru.javawebinar.topjava.util.ConstraintError.EMAIL_DUPLICATE;

public class ValidationUtil {

    private static final Validator validator;
    private static final Map<String, ConstraintError> CONSTRAINS_I18N_MAP = Map.of(
            "users_unique_email_idx", EMAIL_DUPLICATE,
            "meal_unique_user_datetime_idx", DATE_TIME_DUPLICATE);

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();
    }

    private ValidationUtil() {
    }

    public static <T> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static List<String> getErrorDetails(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    public static String getConstraintsErrorText(ConstraintError constraintError,
                                                 MessageSourceAccessor messageSourceAccessor) {
        return messageSourceAccessor.getMessage(constraintError.getMessageCode());
    }

    public static ConstraintError getConstraintsError(Exception e) {
        String lowerCaseMsg = e.getMessage().toLowerCase();
        for (String constraintText : CONSTRAINS_I18N_MAP.keySet()) {
            if (lowerCaseMsg.contains(constraintText)) {
                return CONSTRAINS_I18N_MAP.get(constraintText);
            }
        }
        return null;
    }
}