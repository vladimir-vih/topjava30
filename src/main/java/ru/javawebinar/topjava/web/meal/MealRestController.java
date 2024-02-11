package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController extends AbstractMealController{

    @Override
    public Meal create(Meal meal) {
        checkNew(meal);
        return super.create(meal);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        super.update(meal);
    }
}