package api;

import api.request.UserData;
import io.qameta.allure.Step;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static io.restassured.RestAssured.given;

abstract public class BaseAssertStep {

    @Step("Проверка, что изречение верно: {text}")
    public void assertTrue(Boolean bul, String text) {
        Assertions.assertTrue(bul);
    }

    @Step("Проверка доступности поля: {text}")
    public void assertNotNull(Integer text, String string) {
        Assertions.assertNotNull(text);
    }

    @Step("Проверка доступности поля: {text}")
    public void assertNotNull(String text, String string) {
        Assertions.assertNotNull(text);
    }

    @Step("Проверка ожидаемого текста: {expected}")
    public void assertEquals(Integer expected, Integer actual) {
        Assertions.assertEquals(expected,actual);
    }

    @Step("Проверка ожидаемого текста: {expected}")
    public void assertEquals(String expected, String actual) {
        Assertions.assertEquals(expected,actual);
    }

    @Step("Проверка ожидаемого текста: {expected}")
    public void assertEquals(List<Integer> expected, List<Integer> actual) {
        Assertions.assertEquals(expected,actual);
    }
    
}
