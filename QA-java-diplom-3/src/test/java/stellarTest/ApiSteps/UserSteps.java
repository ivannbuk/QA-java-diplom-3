package stellarTest.ApiSteps;

import stellarTest.PojoObjects.RequestSign;
import stellarTest.PojoObjects.RequestUser;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static stellarTest.ConstantsSetting.ConstUrl.TEST_URL;
import static stellarTest.ConstantsSetting.ConstUser.*;
import static io.restassured.RestAssured.given;

public class UserSteps {
    public static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setBaseUri(TEST_URL + "/api")
                    .setBasePath(AUTH_URL)
                    .setContentType(ContentType.JSON)
                    .build();

    @Step("Создаём уникального юзера")
    public static Response createUniqueUser(RequestUser body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post(REGISTER_URL);
    }

    @Step("Удаляем пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .when()
                .delete(USER_URL);
    }

    @Step("Выполняем авторизацию с помощью тела запроса на авторизацию")
    public static Response signInWithSignInRequest(RequestSign signInRequest) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(signInRequest)
                .when()
                .post(LOGIN_URL);
    }
}
