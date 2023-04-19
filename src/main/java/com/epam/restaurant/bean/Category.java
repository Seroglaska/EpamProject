package com.epam.restaurant.bean;

import java.io.Serializable;

public class Category implements Serializable {
    private static final long serialVersionUID = 5094551711860514907L;
    private int id;
    private String name;
    private Integer status;

    public Category() {
    }

    public Category(int id, String name, Integer status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category that = (Category) o;
        return id == that.id
                && ((name == that.name) || (name != null && name.equals(that.name)))
                && ((status == that.status) || (status != null && status.equals(that.status)));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = id;
        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
