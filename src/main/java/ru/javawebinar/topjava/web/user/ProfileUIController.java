package ru.javawebinar.topjava.web.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.ConstraintError;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

import static ru.javawebinar.topjava.util.ValidationUtil.getConstraintsError;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        executeAndCheckConstraintErrors(result,
                () -> super.update(userTo, SecurityUtil.authUserId()));
        if (result.hasErrors()) {
            return "profile";
        } else {
            SecurityUtil.get().setTo(userTo);
            status.setComplete();
            return "redirect:/meals";
        }
    }

    private void executeAndCheckConstraintErrors(BindingResult result,
                                                 Runnable function) {
        if (result.hasErrors()) {
            return;
        }
        try {
            function.run();
        } catch (DataIntegrityViolationException ex) {
            ConstraintError constraintError = getConstraintsError(ex);
            if (constraintError != null) {
                result.rejectValue(constraintError.getFieldName(), constraintError.getMessageCode());
                return;
            }
            throw ex;
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        executeAndCheckConstraintErrors(result,
                () -> super.create(userTo));
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        }
        status.setComplete();
        return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
    }
}