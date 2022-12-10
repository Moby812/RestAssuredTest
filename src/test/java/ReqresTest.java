import api.Specifications;
import api.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    private final static String url = "https://reqres.in/";

    @Test
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
    public void successRegTest() {
        Specifications.installSpec(Specifications.requestSpec(url), Specifications.respSpecOk200());
        
    }
}
