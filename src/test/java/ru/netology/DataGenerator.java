package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

   //Спецификация
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    //Вносим юзера в систему
    private static void request(RegistrationDto user) {
        given ()
                .spec(requestSpec)
                .body(user)
        .when()
                .post("/api/system/users")
        .then()
                .statusCode(200);
    }

    //Что лежит внутри учетки юзера
    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

    //Установка локали для фэйкера
    private static Faker faker = new Faker(new Locale("en"));

    //генерация логина
    public static String generateRandomUsername(){
        String username = faker.name().username();
        return username;
    }

    //генерация пароля
    public static String generateRandomPassword(){
        String password = faker.internet().password();
        return password;
    }

    //Создание учетки
    public static class Registration {
        private Registration() {
        }
        //Создание зарегистрированного юзера, статус устанавлиается в тестах
        public static RegistrationDto generateRegisteredUser(String status){
            var registeredUser = new RegistrationDto(generateRandomUsername(), generateRandomPassword(), status);
            request(registeredUser);
            return registeredUser;
        }
    }
}
