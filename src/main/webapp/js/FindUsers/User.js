class User {
    constructor(name, phoneNumber, email, role) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }

    setName(name) {
        this.name = name;
    }

    setPhoneNumber(phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    setEmail(email) {
        this.email = email;
    }

    setRole(role) {
        this.role = role;
    }

    getName() {
        return this.name;
    }

    getPhoneNumber() {
        return this.phoneNumber;
    }

    getEmail() {
        return this.email;
    }

    getRoleId() {
        return this.roleId;
    }
}