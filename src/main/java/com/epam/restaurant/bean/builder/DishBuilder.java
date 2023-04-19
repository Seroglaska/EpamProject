package com.epam.restaurant.bean.builder;

import com.epam.restaurant.bean.Dish;

import java.math.BigDecimal;

public class DishBuilder {
    private Dish dish = new Dish();

    public DishBuilder setId(int id) {
        dish.setId(id);
        return this;
    }

    public DishBuilder setPrice(BigDecimal price) {
        dish.setPrice(price);
        return this;
    }

    public DishBuilder setName(String name) {
        dish.setName(name);
        return this;
    }

    public DishBuilder setDescription(String description) {
        dish.setDescription(description);
        return this;
    }

    public DishBuilder setCategoryId(int categoryId) {
        dish.setCategoryId(categoryId);
        return this;
    }

    public DishBuilder setPhotoLink(String photoLink) {
        dish.setPhotoLink(photoLink);
        return this;
    }
    
    public Dish build() {
        return dish;
    }
}
