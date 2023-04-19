package com.epam.restaurant.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {
    private static final long serialVersionUID = -1445447307917783855L;
    private List<Dish> dishes = new ArrayList<>();

    public Menu() {
    }

    public Menu(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void add(Dish dish) {
        dishes.add(dish);
    }

    public void remove(Dish dish) {
        dishes.remove(dish);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Menu that = (Menu) o;
        return (dishes == that.dishes) || (dishes != null && dishes.equals(that.dishes));
    }

    @Override
    public int hashCode() {
        return dishes != null ? dishes.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "dishes=" + dishes +
                '}';
    }
}
