package com.epam.restaurant.bean;

import java.io.Serializable;

public class RegistrationUserData implements Serializable {
    private static final long serialVersionUID = -3734494546116300742L;
    private String login;
    private transient char[] password;
    private String name;
    private String phoneNumber;
    private String email;
    private Integer roleId;
    private Integer id;

    public RegistrationUserData() {
    }

    public RegistrationUserData(String login, char[] password, String name, String phoneNumber, String email, int roleId) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.roleId = roleId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RegistrationUserData that = (RegistrationUserData) o;
        return roleId == that.roleId
                &&((login == that.login) || (login != null && login.equals(that.login)))
                && ((name == that.name) || (name != null && name.equals(that.name)))
                && ((phoneNumber == that.phoneNumber) || (phoneNumber != null && phoneNumber.equals(that.phoneNumber)))
                && ((email == that.email) || (email != null && email.equals(that.email)))
                && (password != null && new String(password).equals(new String(that.password)));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = roleId;
        result = prime * result + (login != null ? login.hashCode() : 0);
        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = prime * result + (email != null ? email.hashCode() : 0);
        if (password != null) {
            for (int i = 0; i < password.length; i++) {
                result = prime * result + (int) password[i];
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
