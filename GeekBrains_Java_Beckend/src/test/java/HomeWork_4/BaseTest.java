package HomeWork_4;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;

    static String username;

    public final static String PROPERTIES = "src/test/java/HomeWork_4/resources/application.properties";
    public final static String PATH_TO_IMAGE = "src/test/java/HomeWork_4/resources/JPEG-Image.jpg";
    public final static String PATH_TO_IMAGE_MICRO = "src/test/java/HomeWork_4/resources/PNG_Image_1_Kb.png";
    public final static String PATH_TO_GIF = "src/test/java/HomeWork_4/resources/GIF-Image.gif";
    public final static String PATH_TO_DOC = "src/test/java/HomeWork_4/resources/testDOCX.docx";
    public final static String PATH_TO_MP3 = "src/test/java/HomeWork_4/resources/music.mp3";
    public final static String PATH_TO_VIDEO = "src/test/java/HomeWork_4/resources/video_70_mb.mp4";
    public final static String PATH_TO_VIDEO200 = "src/test/java/HomeWork_4/resources/video_150_mb.mp4";

    static ResponseSpecification smallPositiveResponseSpecification;
    static ResponseSpecification positiveResponseSpecification;
    static ResponseSpecification positiveAccountResponseSpecification;
    static ResponseSpecification smallNegativeResponseSpecification;
    static ResponseSpecification negative400ResponseSpecification;
    static ResponseSpecification negative401ResponseSpecification;
    static ResponseSpecification negative413ResponseSpecification;
    static ResponseSpecification negative417ResponseSpecification;

    static RequestSpecification requestSpecificationWithAuth;
    static RequestSpecification requestSpecUploadImage;
    static ResponseSpecification uploadFileImage100ResponseSpecification;
    static RequestSpecification requestSpecUploadImageMicro;
    static ResponseSpecification uploadFileImageMicroResponseSpecification;
    static RequestSpecification requestSpecUploadImageGIF;
    static ResponseSpecification uploadFileImageGIFResponseSpecification;
    static RequestSpecification requestSpecUploadImageVideo;
    static ResponseSpecification uploadFileImageVideoResponseSpecification;
    static RequestSpecification requestSpecUploadImageDoc;
    static ResponseSpecification uploadFileImageDocResponseSpecification;
    static RequestSpecification requestSpecUploadImageMP3;
    static RequestSpecification requestSpecUploadVideo;
    static RequestSpecification requestSpecUploadVideoImage;
    static ResponseSpecification uploadFileVideoImageOrGIFResponseSpecification;
    static RequestSpecification requestSpecUploadVideoMP3;
    static ResponseSpecification uploadFileVideoMP3ResponseSpecification;
    static RequestSpecification requestSpecUploadVideoDoc;
    static ResponseSpecification uploadFileVideoDocResponseSpecification;
    static RequestSpecification requestSpecUploadVideoGIF;
    static RequestSpecification requestSpecUploadImageBase64;
    static ResponseSpecification uploadFileImageBase64ResponseSpecification;
    static RequestSpecification requestSpecDeleteImageBase64;
    static ResponseSpecification deleteFileImageBase64ResponseSpecification;

    static String encodedFile;
    static MultiPartSpecification base64MultiPartSpec;

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://api.imgur.com/3";
        getProperties();
        token = properties.getProperty("token");
        username = properties.getProperty("username");

        smallPositiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("data.id", is(notNullValue()))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        positiveResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(smallPositiveResponseSpecification)
                .expectBody("data.account_url", is(username))
                .expectBody("data.account_url", is(notNullValue()))
                .build();

        positiveAccountResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(smallPositiveResponseSpecification)
                .expectBody("data.url", is(username))
                .expectBody("data.url", is(notNullValue()))
                .build();

        smallNegativeResponseSpecification = new ResponseSpecBuilder()
                .expectBody("success", equalTo(false))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        negative400ResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(smallNegativeResponseSpecification)
                .expectBody("status", equalTo(400))
                .expectStatusCode(400)
                .build();

        negative401ResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(401))
                .expectBody("data.error", equalTo("Authentication required"))
                .addResponseSpecification(smallNegativeResponseSpecification)
                .expectStatusCode(401)
                .build();

        negative413ResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(413))
                .addResponseSpecification(smallNegativeResponseSpecification)
                .expectBody(("data.error"), equalTo("File is over the size limit"))
                .expectStatusCode(413)
                .build();

        negative417ResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(417))
                .addResponseSpecification(smallNegativeResponseSpecification)
                .expectBody(("data.error"), equalTo("Internal expectation failed"))
                .expectStatusCode(417)
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        requestSpecUploadImage = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR1")
                .addMultiPart("image", new File(PATH_TO_IMAGE))
                .build();

        uploadFileImage100ResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data.type", equalTo("image/jpeg"))
                .expectBody("data.size", equalTo(96590))
                .build();

        requestSpecUploadImageMicro = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR2")
                .addMultiPart("image", new File(PATH_TO_IMAGE_MICRO))
                .build();

        uploadFileImageMicroResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data.type", equalTo("image/jpeg"))
                .expectBody("data.width", equalTo(20))
                .expectBody("data.height", equalTo(14))
                .expectBody("data.size", equalTo(413))
                .build();

        requestSpecUploadImageGIF = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR3")
                .addMultiPart("image", new File(PATH_TO_GIF))
                .build();

        uploadFileImageGIFResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data.type", equalTo("image/gif"))
                .build();

        requestSpecUploadImageVideo = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR4")
                .addMultiPart("image", new File(PATH_TO_VIDEO))
                .build();

        uploadFileImageVideoResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(positiveResponseSpecification)
                .expectBody("data.type", equalTo("video/mp4"))
                .build();

        requestSpecUploadImageDoc = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR5")
                .addMultiPart("image", new File(PATH_TO_DOC))
                .build();

        uploadFileImageDocResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(negative400ResponseSpecification)
                .expectBody("data.error", equalTo("We don't support that file type!"))
                .build();

        requestSpecUploadImageMP3 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR6")
                .addMultiPart("image", new File(PATH_TO_MP3))
                .build();

        requestSpecUploadVideo = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR7")
                .addMultiPart("video", new File(PATH_TO_VIDEO200))
                .build();

        requestSpecUploadVideoImage = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR8")
                .addMultiPart("video", new File(PATH_TO_IMAGE))
                .build();

        uploadFileVideoImageOrGIFResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(negative400ResponseSpecification)
                .expectBody("data.error", equalTo("Unable to determine file duration"))
                .build();

        requestSpecUploadVideoMP3 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR9")
                .addMultiPart("video", new File(PATH_TO_MP3))
                .build();

        uploadFileVideoMP3ResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(negative400ResponseSpecification)
                .expectBody("data.error", equalTo("File exceeds max duration"))
                .build();

        requestSpecUploadVideoDoc = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR10")
                .addMultiPart("video", new File(PATH_TO_DOC))
                .build();

        uploadFileVideoDocResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(negative400ResponseSpecification)
                .expectBody("data.error", equalTo("We don't support that file type!"))
                .build();

        requestSpecUploadVideoGIF = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR11")
                .addMultiPart("video", new File(PATH_TO_GIF))
                .build();
    }

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        requestSpecUploadImageBase64 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "STR12")
                .addMultiPart(base64MultiPartSpec)
                .build();

        uploadFileImageBase64ResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(smallPositiveResponseSpecification)
                .expectBody("data.type", equalTo("image/jpeg"))
                .build();

        requestSpecDeleteImageBase64 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart(base64MultiPartSpec)
                .build();

        deleteFileImageBase64ResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectContentType(ContentType.JSON)
                .expectBody("data", equalTo(true))
                .expectBody("success", equalTo(true))
                .expectStatusCode(200)
                .build();
    }

    private static void getProperties() {
        try (InputStream output = new FileInputStream(PROPERTIES)) {
            properties.load(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
