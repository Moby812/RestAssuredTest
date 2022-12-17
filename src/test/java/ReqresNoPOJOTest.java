import api.BaseAssertStep;
import api.Specifications;
import io.qameta.allure.Owner;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Owner("Парамонов Павел")
public class ReqresNoPOJOTest extends BaseAssertStep {
    private final static String url = "https://reqres.in/";

    @Test
    @Tag("NoPOJO")
    @Tag("get")
    @DisplayName("Аватар содержит ID пользователя, почты всех пользователей заканчиваются на '@reqres.in'")
    public void checkAvatarIdNoPOJOTest() {

        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        Response response = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        List<Integer> ids = jsonPath.get("data.id");
        List<String> emails = jsonPath.get("data.email");
        List<String> avatars = jsonPath.get("data.avatar");

        for (int i = 0; i < avatars.size(); i++) {
            assertTrue(avatars.get(i).contains(ids.get(i).toString()),
                    "Аватар "+avatars.get(i)+" содержит Id клиента (Проверка через массив)");
        }
        assertTrue(emails.stream().allMatch(x -> x.endsWith("@reqres.in")),
                "Почта заканчивается на @reqres.in");
    }

    @Test
    @Tag("NoPOJO")
    @Tag("post")
    @DisplayName("Успешная регистрация пользователя")
    public void successRegNoPOJOTest() {
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");
        Response response = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Integer id = jsonPath.get("id");
        String token = jsonPath.get("token");

        assertEquals(4,id);
        assertEquals("QpwL5tke4Pnpja7X4",token);
    }

    @Test
    @Tag("NoPOJO")
    @Tag("post")
    @DisplayName("Неуспешная регистрация пользователя")
    public void failRegNoPOJOTest() {
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecError400());
        Map<String, String> user = new HashMap<>();
        user.put("email", "sydney@fife");
        //вариант без использования Response
        given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .body("error",equalTo("Missing password"));
    }
}