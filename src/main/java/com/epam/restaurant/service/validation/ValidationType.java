package com.epam.restaurant.service.validation;

public enum ValidationType {
    LOGIN("[\\w]{1,10}", "The LOGIN must consist of the following characters:_A-z0-9, max len 15ch"),
    NAME("[A-zА-я]{1,15}", "The NAME must consist of the following characters:A-zА-я, max len 15ch"),
    PHONE_NUMBER("^\\+\\d{10,14}$", "PHONE NUMBER must consist of [0-9] dig, min len 10,  max len 14ch"),
    EMAIL("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$", "Not correct EMAIL"),
    ID("^(\\d*)", "The ID must consist of digits and be greater than 0"),
    PRICE("^[\\d]+\\.?[\\d]*$", "PRICE can only consist of floating point numbers"),
    STATUS("[\\d]+","STATUS can only consist of numbers");

    private final String regex;
    private final String errorMsg;

    ValidationType(String regex, String errorMsg) {
        this.regex = regex;
        this.errorMsg = errorMsg;
    }

    public String getRegex() {
        return regex;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
