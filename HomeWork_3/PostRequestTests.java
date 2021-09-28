package HomeWork_3;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class PostRequestTests extends BaseTest {

    private final String PATH_TO_PIC_1 = "src/test/java/HomeWork_3/resources/Response_Check.jpg";
    private final String PATH_TO_PIC_2 = "src/test/java/HomeWork_3/resources/pic_PDF.pdf";
    private final String PATH_TO_PIC_3 = "src/test/java/HomeWork_3/resources/Main.java";
    private final String PATH_TO_PIC_4 = "src/test/java/HomeWork_3/resources/pic_02.jpg";
    private final String PATH_TO_PIC_5 = "src/test/java/HomeWork_3/resources/BIN.bin";
    private String Upload_ID;
    static String encodedFile;

    @Test
    void loadPictureAllFieldsExceptAlbumTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .params("type","Response.file")
                .params("name","Response.name")
                .params("title","Response.title")
                .params("description","Response.description")
                .multiPart("image", new File(PATH_TO_PIC_1))
                .expect()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.account_url", notNullValue())
                .body("data.name", equalTo("Response.name"))
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
    void loadPictureJustFieldAlbumTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .params("album","Response.album")
                .multiPart("image", new File(PATH_TO_PIC_1))
                .expect()
                .statusCode(417)
                .body("success", equalTo(false))
                .body("status", equalTo(417))
                .body("data.error", equalTo("Internal expectation failed"))
                .header("Connection", equalTo("close"))
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
    void loadPictureNoFileAttachedTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .multiPart("image","null")
                .expect()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("status", equalTo(400))
                .body("data.error", equalTo("Bad Request"))
                .header("Connection", equalTo("close"))
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
    void loadPicturePdfFormatTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .multiPart("image", new File(PATH_TO_PIC_2))
                .expect()
                .statusCode(417)
                .body("success", equalTo(false))
                .body("status", equalTo(417))
                .body("data.error", equalTo("Internal expectation failed"))
                .header("Connection", equalTo("close"))
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
    void loadPictureNotImageFormatTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .multiPart("image", new File(PATH_TO_PIC_3))
                .expect()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("status", equalTo(400))
                .body("data.error", equalTo("We don't support that file type!"))
                .header("Connection", equalTo("close"))
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
    void loadPictureBase64FormatTest() {
        byte[] byteArray = getFileContent(PATH_TO_PIC_4);
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
        Upload_ID = given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .multiPart("image", encodedFile)
                .params("type","base64")
                .expect()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.account_url", notNullValue())
                .body("data.size", notNullValue())
                .body("data.id", notNullValue())
                .header("Content-Length", notNullValue())
                .when()
                .post("https://api.imgur.com/3/upload")
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void loadPictureUrlFormatTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .multiPart("image", "https://proprikol.ru/wp-content/uploads/2020/04/kartinki-vikingi-1.jpg")
                .params("type","url")
                .expect()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.account_url", notNullValue())
                .body("data.size", notNullValue())
                .body("data.id", notNullValue())
                .body("data.link", notNullValue())
                .header("Content-Length", notNullValue())
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
    void loadPictureBinaryFormatTest() {
        Upload_ID = given()
                .header("Authorization", token)
                .multiPart("image",PATH_TO_PIC_5)
                .expect()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("status", equalTo(400))
                .body("data.error", equalTo("Bad Request"))
                .header("Connection", equalTo("close"))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @AfterEach
    void cleaningServiceTest() {
        if (Upload_ID == null) {
            System.out.println("************** NO NEED TO CLEAN ANYTHING THIS CASE....  *****************");
        }
        else {
            given()
                    .headers("Authorization", token)
                    .when()
                    .delete("https://api.imgur.com/3/image/{deleteID}", Upload_ID)
                    .prettyPeek()
                    .then()
                    .statusCode(200);}
    }

    private byte[] getFileContent(String path) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
