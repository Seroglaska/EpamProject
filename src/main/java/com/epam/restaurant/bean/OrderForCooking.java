package com.epam.restaurant.bean;

import java.io.Serializable;

public class OrderForCooking implements Serializable {
    private static final long serialVersionUID = 2899503221119242930L;
    private long orderId;
    private String dishName;
    private int quantity;
    private String methodOfReceiving;

    public OrderForCooking() {
    }

    public OrderForCooking(long orderId, String dishName, int quantity, String methodOfReceiving) {
        this.orderId = orderId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.methodOfReceiving = methodOfReceiving;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMethodOfReceiving() {
        return methodOfReceiving;
    }

    public void setMethodOfReceiving(String methodOfReceiving) {
        this.methodOfReceiving = methodOfReceiving;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderForCooking that = (OrderForCooking) o;
        return orderId == that.orderId
                && quantity == that.quantity
                && ((dishName == that.dishName) || (dishName != null && dishName.equals(that.dishName)))
                && ((methodOfReceiving == that.methodOfReceiving) || (methodOfReceiving != null && methodOfReceiving.equals(that.methodOfReceiving)));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = (int) (orderId ^ (orderId >>> 32));
        result = prime * result + (dishName != null ? dishName.hashCode() : 0);
        result = prime * result + (methodOfReceiving != null ? methodOfReceiving.hashCode() : 0);
        result = prime * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "orderId=" + orderId +
                ", dishName='" + dishName + '\'' +
                ", quantity=" + quantity +
                ", methodOfReceiving='" + methodOfReceiving + '\'' +
                '}';
    }
}
