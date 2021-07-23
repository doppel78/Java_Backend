package HomeWork_4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import HomeWork_4.dto.PostImageResponse;

import static io.restassured.RestAssured.given;
import static HomeWork_4.Endpoints.*;

class VideoTests extends BaseTest {

    @Test
    @DisplayName("Загрузка видео больше 200 МБ")
    void uploadFileVideoTests() {
        given(requestSpecUploadVideo)
                .post(UPLOAD).prettyPeek().then()
                .spec(negative413ResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка картинки в поле видео")
    void uploadFileImageToVideoTests() {
        given(requestSpecUploadVideoImage)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileVideoImageOrGIFResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка MP3 в поле видео")
    void uploadFileMP3ToVideoTests() {
        given(requestSpecUploadVideoMP3)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileVideoMP3ResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка документа в поле видео")
    void uploadFileDocToVideoTests() {
        given(requestSpecUploadVideoDoc)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileVideoDocResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    @DisplayName("Загрузка GIF в поле видео")
    void uploadFileGIFToVideoTests() {
        given(requestSpecUploadVideoGIF)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileVideoImageOrGIFResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }
}
