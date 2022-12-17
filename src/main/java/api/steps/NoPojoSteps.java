package api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class NoPojoSteps extends BaseAssertStep {

    @Step("Выполнение post запроса: {url}")
    public Response postResponse(Map<String, String> body, String url) {
        return given()
                .body(body)
                .when()
                .post(url)
                .then().log().all()
                .extract().response();
    }
}
