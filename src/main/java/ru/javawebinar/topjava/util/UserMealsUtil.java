package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
         List<UserMealWithExceed> mealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
        mealWithExceeds.stream().forEach(System.out::println);
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        //Grouping user meals with date
        Map<LocalDate,List<UserMeal>> userMealByDate = mealList.stream().collect(Collectors.groupingBy((p)->p.getDateTime().toLocalDate()));

        Map<LocalDate,Integer> caloriesPerDayMap = new HashMap<>();
        for (Map.Entry<LocalDate,List<UserMeal>> pair:userMealByDate.entrySet()) {
           int sum = pair.getValue().stream().collect(Collectors.summingInt((p) -> p.getCalories()));
           caloriesPerDayMap.put(pair.getKey(),sum);
        }
        mealList.stream().filter((p) ->
                (caloriesPerDayMap.containsKey(p.getDateTime().toLocalDate()) && caloriesPerDayMap.get(p.getDateTime().toLocalDate()).intValue() > caloriesPerDay &&
                        TimeUtil.isBetween(p.getDateTime().toLocalTime(),startTime,endTime))).forEach((p) -> {
            userMealWithExceeds.add(new UserMealWithExceed(p.getDateTime(),p.getDescription(),p.getCalories(),true));
        });
        return userMealWithExceeds;
    }
}
