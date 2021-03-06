package HomeWork_6.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import HomeWork_6.dto.Category;

public interface CategoryService {
    @GET("categories/{id}")
    Call<Category> getCategory(@Path("id") Integer id);
}
