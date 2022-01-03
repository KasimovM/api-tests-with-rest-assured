package gmail.mrkvktrvch;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

class DemoWebShopTests {

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }

    @Test
    void loginWithCookieTest() {
        step("Получение cookie через API и установка в браузер", () -> {
            String authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", "mrkvktrvch@gmail.com")
                            .formParam("Password", "trap777")
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Загрузка минимального контента для установки cookie в браузер", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Установка cookie в браузер", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });

        step("Открытие главной страницы", () ->
                open(""));

        step("Выход из аккаунта для проверки того, что только зарегистрированный пользователь может проголосовать " +
                "за качество сайта", () ->
                $(".ico-logout").click());

        step("Голосование за качество сайта", () ->
                $("#pollanswers-4")).click();
                $(".vote-poll-button").click();

        step("Проверка того, что только зарегистрированный пользователь может голосовать за качество сайта", () ->
                $(".poll-vote-error")).shouldHave(text("Only registered users can vote."));

    }
}