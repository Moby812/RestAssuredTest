package api.steps;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import java.util.List;


abstract public class BaseAssertStep {

    @Step("Проверка, что изречение верно: {text}")
    protected void assertTrue(Boolean bul, String text) {
        Assertions.assertTrue(bul);
    }

    @Step("Проверка доступности поля: {text}")
    protected void assertNotNull(Integer text, String string) {
        Assertions.assertNotNull(text);
    }

    @Step("Проверка доступности поля: {text}")
    protected void assertNotNull(String text, String string) {
        Assertions.assertNotNull(text);
    }

    @Step("Проверка ожидаемого текста: {expected}")
    protected void assertEquals(Integer expected, Integer actual) {
        Assertions.assertEquals(expected,actual);
    }

    @Step("Проверка ожидаемого текста: {expected}")
    protected void assertEquals(String expected, String actual) {
        Assertions.assertEquals(expected,actual);
    }

    @Step("Проверка ожидаемого текста: {expected}")
    protected void assertEquals(List<Integer> expected, List<Integer> actual) {
        Assertions.assertEquals(expected,actual);
    }
    
}
