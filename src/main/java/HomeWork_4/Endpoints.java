package HomeWork_4;

public class Endpoints {
    public static final String UPLOAD = "/upload";
    public static final String UPLOAD_BASE64 = "/image";
    public static final String GET_ACCOUNT = "/account/{username}";
    public static final String GET_DELETE = GET_ACCOUNT + "/image/{deleteHash}";
}
