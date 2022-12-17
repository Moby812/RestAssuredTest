package api.steps;

import api.request.Register;
import api.request.UserData;
import api.request.UserTime;
import api.response.FailReg;
import api.response.ResourceData;
import api.response.SuccessReg;
import api.response.UserTimeRes;
import io.qameta.allure.Step;

import java.util.List;

import static io.restassured.RestAssured.given;

public class PojoSteps extends BaseAssertStep{

    @Step("Выполнение get запроса: {url} и извлечение данных из параметра {path}")
    public List<UserData> getUserData(String url, String path) {
        return given()
                .when()
                .get(url)
                .then().log().all()
                .extract().body().jsonPath().getList(path, UserData.class);
    }

    @Step("Выполнение post запроса: {url}")
    public SuccessReg postSuccessReg(Register body, String url) {
        return given()
                .body(body)
                .when()
                .post(url)
                .then().log().all()
                .extract().as(SuccessReg.class);
    }

    @Step("Выполнение post запроса: {url}")
    public FailReg postFailReg(Register body, String url) {
        return given()
                .body(body)
                .when()
                .post(url)
                .then().log().all()
                .extract().as(FailReg.class);
    }

    @Step("Выполнение delete запроса: {url}")
    public void deleteUserId(String url) {
        given()
                .when()
                .delete(url)
                .then().log().all();
    }

    @Step("Выполнение get запроса: {url} и извлечение данных из параметра {path}")
    public List<ResourceData> getResourceData(String url, String path) {
        return given()
                .when()
                .get(url)
                .then().log().all()
                .extract().body().jsonPath().getList(path, ResourceData.class);
    }

    @Step("Выполнение put запроса: {url}")
    public UserTimeRes putUserTimeRes(UserTime body, String url) {
        return  given()
                .body(body)
                .when()
                .put(url)
                .then().log().all()
                .extract().as(UserTimeRes.class);
    }
}
