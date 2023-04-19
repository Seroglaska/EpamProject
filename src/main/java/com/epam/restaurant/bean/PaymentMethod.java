package com.epam.restaurant.bean;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    private static final long serialVersionUID = -3518737505016629342L;
    private int id;
    private String method;

    public PaymentMethod() {
    }

    public PaymentMethod(int id, String method) {
        this.id = id;
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentMethod that = (PaymentMethod) o;
        return id == that.id
                && ((method == that.method) || (method != null && method.equals(that.method)));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = id;
        result = prime * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "id=" + id +
                ", method='" + method + '\'' +
                '}';
    }
}
