package ru.yandex.praktikum.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public User random() {
        return new User(RandomStringUtils.randomNumeric(5) + "@user.com", "qwerty123", "User");
    }
}
