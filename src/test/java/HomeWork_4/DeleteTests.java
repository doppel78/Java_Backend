package HomeWork_4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import HomeWork_4.dto.PostImageResponse;
import HomeWork_4.dto.PostImageResponseBase;

import static io.restassured.RestAssured.given;
import static HomeWork_4.Endpoints.*;

class DeleteTests extends BaseTest {
    String uploadedImageId;

    @Test
    @DisplayName("Загрузка картинки c помощью Base")
    void uploadFileBase64Test() {
        uploadedImageId = given(requestSpecUploadImageBase64)
                .post(UPLOAD_BASE64).prettyPeek().then()
                .spec(uploadFileImageBase64ResponseSpecification)
                .extract().body().as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @AfterEach
    @DisplayName("Удаление картинки загруженной c помощью Base")
    void tearDown() {
        given(requestSpecDeleteImageBase64)
                .delete(GET_DELETE, username, uploadedImageId).prettyPeek().then()
                .spec(deleteFileImageBase64ResponseSpecification)
                .extract().body().as(PostImageResponseBase.class);
    }
}
