import api.*;
import api.request.Register;
import api.request.UserData;
import api.request.UserTime;
import api.response.FailReg;
import api.response.ResourceData;
import api.response.SuccessReg;
import api.response.UserTimeRes;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Owner("Парамонов Павел")
public class ReqresPOJOTest extends BaseAssertStep{
    private final static String url = "https://reqres.in/";

    @Test
    @Tag("POJO")
    @Tag("get")
    @DisplayName("Аватар содержит ID пользователя, почты всех пользователей заканчиваются на '@reqres.in'")
    public void checkAvatarIdTest() {
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        users.forEach(x -> assertTrue(x.getAvatar().contains(x.getId().toString()),
                "Аватар "+x.getAvatar()+" содержит Id клиента")); //проверка каждой записи
        assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")),
                "Почта заканчивается на @reqres.in"); //проверка всех записей

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
        for (int i = 0; i < avatars.size(); i++) {
            assertTrue(avatars.get(i).contains(ids.get(i)),
                    "Аватар "+avatars.get(i)+" содержит Id клиента (Проверка через массив)");
        }
    }

    @Test
    @Tag("POJO")
    @Tag("post")
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
        assertNotNull(successReg.getId(),"getId");
        assertNotNull(successReg.getToken(), "getId");
        assertEquals(id, successReg.getId());
        assertEquals(token, successReg.getToken());
    }

    @Test
    @Tag("POJO")
    @Tag("post")
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
        assertEquals("Missing password",failReg.getError());
    }

    @Test
    @Tag("POJO")
    @Tag("get")
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
        assertEquals(sortedYears,years);
    }

    @Test
    @Tag("POJO")
    @Tag("del")
    @DisplayName("Удаление пользователя")
    public void deleteUserTest(){
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecCode(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    @Test
    @Tag("POJO")
    @Tag("put")
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

        assertEquals(currentTime,userTimeRes.getUpdatedAt().replaceAll(regex, ""));

    }
}
