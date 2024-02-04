package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String get(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        Map<String, String[]> paramsMap = request.getParameterMap();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime, userId));
        } else {
            model.addAttribute("meals", getAll(userId));
        }
        return "meals";
    }

    @GetMapping("delete")
    public String delete(HttpServletRequest request) throws IOException {
        int userId = SecurityUtil.authUserId();
        delete(getId(request), userId);
        return "redirect:/meals";
    }

    @GetMapping("update")
    public String createOrUpdate(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        String id = request.getParameter("id");
        Meal meal;
        if (StringUtils.hasLength(id)) {
            meal = get(Integer.parseInt(id), userId);
        } else {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        }
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String save(HttpServletRequest request)
            throws IOException {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");
        if (StringUtils.hasLength(id)) {
            meal.setId(Integer.parseInt(id));
            update(meal, userId);
        } else {
            create(meal, userId);
        }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
