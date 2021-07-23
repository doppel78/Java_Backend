package HomeWork_4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import HomeWork_4.dto.PostImageResponse;

import static io.restassured.RestAssured.given;
import static HomeWork_4.Endpoints.*;

class ImageTests extends BaseTest {

    @Test
    @DisplayName("Загрузка картинки 100 Кб")
    void uploadFileImage100Tests() {
        given(requestSpecUploadImage)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImage100ResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка картинки 1 Кб и размером 20x14")
    void uploadFileImageMicroTests() {
        given(requestSpecUploadImageMicro)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImageMicroResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка картинки GIF")
    void uploadFileGIFTests() {
        given(requestSpecUploadImageGIF)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImageGIFResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка видео в поле картинки")
    void uploadFileVideoToImageTests() {
        given(requestSpecUploadImageVideo)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImageVideoResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка документа в поле картинки")
    void uploadFileDocToImageTests() {
        given(requestSpecUploadImageDoc)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImageDocResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка MP3 в поле картинки")
    void uploadFileMP3ToImageTests() {
        given(requestSpecUploadImageMP3)
                .post(UPLOAD).prettyPeek().then()
                .spec(negative417ResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }
}
