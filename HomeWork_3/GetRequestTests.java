package HomeWork_3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetRequestTests extends BaseTest {

    private final String PATH_TO_PIC = "src/test/java/HomeWork_3/resources/pic_02.jpg";
    private String Upload_ID;

    @BeforeEach
    void loadPictureAndGetDataTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .multiPart("image", new File(PATH_TO_PIC))
                .expect()
                .statusCode(200)
                .body("success", equalTo(true))
                .contentType(notNullValue())
                .body("data.account_url", notNullValue())
                .body("data.id", notNullValue())
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void getExistingPicWithTokenTest() {
        given()
                .header("Authorization", token)
                .expect()
                .statusCode(200)
                .body("data.id", notNullValue())
                .body("success", equalTo(true))
                .body("data.link", notNullValue())
                .header("Cache-Control", notNullValue())
                .when()
                .get("https://api.imgur.com/3/image/{Upload_ID}", Upload_ID)
                .prettyPeek();     /* логирование в ответе */
    }

    @Test
    void getExistingPicWithClientIdTest() {
        given()
                .header("Authorization", clientId)
                .expect()
                .statusCode(200)
                .body("data.id", notNullValue())
                .body("success", equalTo(true))
                .body("data.link", notNullValue())
                .header("Cache-Control", notNullValue())
                .when()
                .get("https://api.imgur.com/3/image/{Upload_ID}", Upload_ID)
                .prettyPeek();     /* логирование в ответе */
    }

    @Test
    void getExistingPicUnauthorizedTest() {
        given()
                .expect()
                .statusCode(401)
                .body("data.error", equalTo("Authentication required"))
                .body("success", equalTo(false))
                .body("data.request", notNullValue())
                .header("Content-Type", notNullValue())
                .when()
                .get("https://api.imgur.com/3/image/{Upload_ID}", Upload_ID)
                .prettyPeek();
    }

    @AfterEach
    void cleaningServiceTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/image/{deleteID}", Upload_ID)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
