package HomeWork_4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import HomeWork_4.dto.PostImageResponse;

import static io.restassured.RestAssured.given;
import static HomeWork_4.Endpoints.*;

class AccountTests extends BaseTest {

    @Test
    @DisplayName("Проверка аккаунта")
    void getAccountInfoTest() {
        given(requestSpecificationWithAuth)
                .get(GET_ACCOUNT, username).prettyPeek().then()
                .spec(positiveAccountResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Аккаунт без токена")
    void getAccountInfoWithoutTokenTest() {
        given()
                .get(GET_ACCOUNT, username).prettyPeek().then()
                .spec(negative401ResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }
}
