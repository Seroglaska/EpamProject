package com.epam.restaurant.bean.builder;

import com.epam.restaurant.bean.RegistrationUserData;

public class RegistrationUserDataBuilder {
    private RegistrationUserData userData = new RegistrationUserData();

    public RegistrationUserDataBuilder setLogin(String login) {
        userData.setLogin(login);
        return this;
    }

    public RegistrationUserDataBuilder setPassword(char[] password) {
        userData.setPassword(password);
        return this;
    }

    public RegistrationUserDataBuilder setName(String name) {
        userData.setName(name);
        return this;
    }

    public RegistrationUserDataBuilder setPhoneNumber(String phoneNumber) {
        userData.setPhoneNumber(phoneNumber);
        return this;
    }

    public RegistrationUserDataBuilder setEmail(String email) {
        userData.setEmail(email);
        return this;
    }

    public RegistrationUserDataBuilder setRoleId(Integer roleId) {
        userData.setRoleId(roleId);
        return this;
    }

    public RegistrationUserDataBuilder setId(Integer id) {
        userData.setId(id);
        return this;
    }

    public RegistrationUserData build() {
        return userData;
    }
}
