package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.datetimeformatter.CustomDateTimeFormat;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.datetimeformatter.CustomDateTimeFormat.DefaultValue.*;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/meals";

    @GetMapping("{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam int id) {
        super.delete(id);
    }

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @PostMapping
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @RequestParam int id) {
        super.update(meal, id);
    }

    @GetMapping("filter")
    public List<MealTo> getBetween(@Nullable @RequestParam @CustomDateTimeFormat(defaultValue = DATE_MIN) LocalDate startDate,
                                   @Nullable @RequestParam @CustomDateTimeFormat(defaultValue = TIME_MIN) LocalTime startTime,
                                   @Nullable @RequestParam @CustomDateTimeFormat(defaultValue = DATE_MAX) LocalDate endDate,
                                   @Nullable @RequestParam @CustomDateTimeFormat(defaultValue = TIME_MAX) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}