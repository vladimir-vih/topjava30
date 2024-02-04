package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal get(int mealId, int userId) {
        log.info("get meal {} for user {}", mealId, userId);
        return service.get(mealId, userId);
    }

    public void delete(int mealId, int userId) {
        log.info("delete meal {} for user {}", mealId, userId);
        service.delete(mealId, userId);
    }

    public List<MealTo> getAll(int userId) {
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal create(Meal meal, int userId) {
        log.info("create {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int userId) {
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime, int userId) {
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}
