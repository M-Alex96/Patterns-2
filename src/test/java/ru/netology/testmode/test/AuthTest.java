package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test

    public void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $(withText("Продолжить")).click();
        $(".notification__content").shouldBe(visible.text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $(withText("Продолжить")).click();
        $(".notification__content").shouldBe(visible.text("Ошибка! Пользователь заблокирован")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(withText("Продолжить")).click();
        $(".notification__content").shouldBe(visible.text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $(withText("Продолжить")).click();
        $(".notification__content").shouldBe(visible.text("Ошибка! Неверно указан логин или пароль")).shouldBe(visible);
    }
}
