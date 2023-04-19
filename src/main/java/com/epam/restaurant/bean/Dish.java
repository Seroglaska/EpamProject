package com.epam.restaurant.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Dish implements Serializable {
    private static final long serialVersionUID = 8271867801723000721L;
    private int id;
    private BigDecimal price;
    private String name;
    private String description;
    private int categoryId;
    private String photoLink;

    public Dish() {
    }

    public Dish(int id, BigDecimal price, String name, String description, int category_id, String photo_link) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.categoryId = category_id;
        this.photoLink = photo_link;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Dish that = (Dish) o;
        return id == that.id
                && categoryId == that.categoryId
                && ((price == that.price) || (price != null && price.equals(that.price)))
                && ((name == that.name) || (name != null && name.equals(that.name)))
                && ((description == that.description) || (description != null && description.equals(that.description)))
                && ((photoLink == that.photoLink) || (photoLink != null && photoLink.equals(that.photoLink)));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = id + categoryId;
        result = prime * result + (price != null ? price.hashCode() : 0);
        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (description != null ? description.hashCode() : 0);
        result = prime * result + (photoLink != null ? photoLink.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category_id=" + categoryId +
                ", photo_link='" + photoLink + '\'' +
                '}';
    }
}
