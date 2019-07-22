package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    //@Autowired
    private MealService service;

    @Autowired
    public JspMealController(MealService service) {
        this.service = service;
    }

    //@RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @PostMapping("/meals/create/{id}")
    public String create(@ModelAttribute("mealForm") Meal meal){
        log.debug("create {}", meal);
        int userId = SecurityUtil.authUserId();
        service.create(meal, userId);
        return "redirect:/meals";
    }

    //@RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @PostMapping("/meals/update/{id}")
    public String update(@ModelAttribute("mealForm") Meal meal){
        log.debug("update {}", meal);
        int userId = SecurityUtil.authUserId();
        service.update(meal, userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/delete/{id}")
    public String delete(@PathVariable("id") int id){
        int userId = SecurityUtil.authUserId();
        service.delete(id,userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/get/{id}")
    public String get(Model model, @PathVariable("id") int id){
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meal", service.get(id, userId));
        return "meals/{id}";
    }

    @GetMapping("/meals")
    public String getAll(Model model){
        int userId = SecurityUtil.authUserId();
        log.debug("get All for user {}", userId);
        model.addAttribute("meals", MealsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "/meals";}

    @GetMapping("/meals/filter")
    public String getBetween(Model model,
                             //@PathVariable("startDate")LocalDate startDate,
                             //@PathVariable("endDate")LocalDate endDate,
                             @ModelAttribute("startTime")LocalTime startTime,
                             @ModelAttribute("endTime") LocalTime endTime){
        int userId = SecurityUtil.authUserId();
        log.debug("get All for user {} between dates", userId);
        model.addAttribute("meals", MealsUtil.getFilteredWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay(),startTime,endTime));
        return "/meals";
    }
}
