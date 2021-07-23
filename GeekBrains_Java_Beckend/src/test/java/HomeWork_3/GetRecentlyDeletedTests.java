package HomeWork_3;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;


public class GetRecentlyDeletedTests extends BaseTest {

    private final String PATH_TO_PIC = "src/test/java/HomeWork_3/resources/pic_02.jpg";
    private String array[] = new String[2];

    @BeforeEach
    void loadPictureAndGetDataTest() {
        Response response = given()
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
                .prettyPeek();
        array[0] = response.jsonPath().get("data.id");
        array[1] = response.jsonPath().get("data.deletehash");
    }

    @Test
    void deleteAndTryingToGetMentionedPicTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/image/{deleteID}", array[0])
                .then()
                .body("success", equalTo(true))
                .body("data", equalTo(true))
                .header("Date", notNullValue());
    }

    @AfterEach
    void tryingToGetDeletedPicTest() {
        given()
                .header("Authorization", token)
                .expect()
                .when()
                .get("https://api.imgur.com/3/image/{deleteID}", array[0])
                .prettyPeek()
                .then()
                .statusCode(404);
    }
}
