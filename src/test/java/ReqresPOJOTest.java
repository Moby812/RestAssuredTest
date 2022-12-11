import api.*;
import api.request.Register;
import api.request.UserData;
import api.request.UserTime;
import api.response.FailReg;
import api.response.ResourceData;
import api.response.SuccessReg;
import api.response.UserTimeRes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresPOJOTest {
    private final static String url = "https://reqres.in/";

    @Test
    @DisplayName("Аватар содержит ID пользователя, почты всех пользователей заканчиваются на '@reqres.in'")
    public void checkAvatarIdTest() {
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString()))); //проверка каждой записи
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in"))); //проверка всех записей

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
        for (int i = 0; i < avatars.size(); i++) {
            Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void successRegTest() {
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Register user = new Register("eve.holt@reqres.in","pistol");
        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);
        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());
        Assertions.assertEquals(id, successReg.getId());
        Assertions.assertEquals(token, successReg.getToken());
    }

    @Test
    @DisplayName("Неуспешная регистрация пользователя")
    public void failRegTest() {
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecError400());
        Register user = new Register("sydney@fife","");
        FailReg failReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(FailReg.class);
        Assertions.assertEquals("Missing password",failReg.getError());
    }

    @Test
    @DisplayName("Года отсортированы в порядке возрастания")
    public void sortedYearsTest(){
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        List<ResourceData> resource = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ResourceData.class);
        List<Integer> years = resource.stream().map(ResourceData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assertions.assertEquals(sortedYears,years);
    }

    @Test
    @DisplayName("Удаление пользователя")
    public void deleteUserTest(){
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecCode(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    @Test
    @DisplayName("Сверка времени запроса")
    public void timeTest(){
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        UserTime user = new UserTime("morpheus","zion resident");
        UserTimeRes userTimeRes = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(UserTimeRes.class);
        String regex = "\\..*$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex, "");

        Assertions.assertEquals(currentTime,userTimeRes.getUpdatedAt().replaceAll(regex, ""));

    }
}
