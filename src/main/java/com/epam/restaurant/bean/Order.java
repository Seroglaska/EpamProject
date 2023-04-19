package com.epam.restaurant.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private static final long serialVersionUID = -2174330731093191175L;
    private long id = 0;
    private Map<Dish, Integer> orderList = new HashMap<>(); // dish-amount
    private BigDecimal totalPrice;
    private Timestamp dateTime;
    private String methodOfReceiving;
    private String status;

    public Order() {

    }

    public Order(long id, Map<Dish, Integer> orderList, BigDecimal totalPrice, Timestamp dateTime, String methodOfReceiving, String status) {
        this.id = id;
        this.orderList = orderList;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.methodOfReceiving = methodOfReceiving;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMethodOfReceiving() {
        return methodOfReceiving;
    }

    public void setMethodOfReceiving(String methodOfReceiving) {
        this.methodOfReceiving = methodOfReceiving;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<Dish, Integer> getOrderList() {
        return orderList;
    }

    public void setOrderList(Map<Dish, Integer> orderList) {
        this.orderList = orderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order that = (Order) o;
        return id == that.id
                && ((orderList == that.orderList) || (orderList != null && orderList.equals(that.orderList)))
                && ((totalPrice == that.totalPrice) || (totalPrice != null && totalPrice.equals(that.totalPrice)))
                && ((dateTime == that.dateTime) || (dateTime != null && dateTime.equals(that.dateTime)))
                && ((methodOfReceiving == that.methodOfReceiving) || (methodOfReceiving != null && methodOfReceiving.equals(that.methodOfReceiving)))
                && ((status == that.status) || (status != null && status.equals(that.status)));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = (int) (id ^ (id >>> 32));
        result = prime * result + (orderList != null ? orderList.hashCode() : 0);
        result = prime * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        result = prime * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = prime * result + (methodOfReceiving != null ? methodOfReceiving.hashCode() : 0);
        result = prime * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "id=" + id +
                ", orderList=" + orderList +
                ", totalPrice=" + totalPrice +
                ", dateTime=" + dateTime +
                ", methodOfReceiving='" + methodOfReceiving + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
