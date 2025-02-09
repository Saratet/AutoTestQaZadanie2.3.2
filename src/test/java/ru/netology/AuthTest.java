package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.generateRegisteredUser;
public class AuthTest {

    @BeforeEach
    void setUp (){
        open ("http://localhost:9999");
    }

    @Test
    //Юзер зарегистрирован и активен, логин и пароль верные. Должен произойти вход в ЛК
    void shouldLogInRegisteredActiveUserWithValidData(){
        var registeredUser = generateRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button").click();
        $("h2").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(15));
    }

    @Test
    //Юзер зарегистрирован и активен, логин верный, пароль неверный. Должно появиться уведомление, что логин или пароль неверные
    void shouldNotLogInRegisteredActiveUserWithWrongPassword(){
        var registeredUser = generateRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(DataGenerator.generateRandomPassword());
        $("button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.partialText("логин или пароль"));
    }

    @Test
    //Юзер зарегистрирован и заблокирован, логин и пароль верные. Должно появиться уведомление, что юзер заблокирован
    void shouldNotLogInBlockedUserWithValidData(){
        var registeredUser = generateRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.partialText("Пользователь заблокирован"));
    }

    @Test
    //Юзер зарегистирован и заблокирован, верный логин, неверный пароль. Должно появиться уведомление, что логин или пароль неверные, а не уведомление о блоикровке юзера
    void shouldNotLogInBlockedUserWithWrongPassword(){
        var registeredUser = generateRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(DataGenerator.generateRandomPassword());
        $("button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.partialText("логин или пароль"));
    }

    @Test
    //Юзер незарегистрирован. Должно появиться уведомление, что логин или пароль неверные
    void shouldNotLogInNotUnregisteredUser(){
        $("[data-test-id='login'] input").setValue(DataGenerator.generateRandomUsername());
        $("[data-test-id='password'] input").setValue(DataGenerator.generateRandomPassword());
        $("button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.partialText("логин или пароль"));
    }
}
