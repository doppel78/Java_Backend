package HomeWork_6;

import com.github.javafaker.Faker;
import com.github.javafaker.Food;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import retrofit2.Response;
import retrofit2.Retrofit;
import HomeWork_6.db.dao.ProductsMapper;
import HomeWork_6.db.model.Products;
import HomeWork_6.db.model.ProductsExample;
import HomeWork_6.dto.Category;
import HomeWork_6.dto.Product;
import HomeWork_6.enums.CategoryType;
import HomeWork_6.service.CategoryService;
import HomeWork_6.service.ProductService;
import HomeWork_6.utils.DbUtils;
import HomeWork_6.utils.RetrofitUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j

public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    static ProductsMapper productsMapper;

    Faker faker = new Faker();
    Product productFoodCategory;
    Product productNonExistCategory;
    Product postProductWithBadPrice;
    Product productToChangeTitle;
    Product productWithId;
    Product putNewPriceAndTitle;
    Product putProductWithBadPrice;
    Product putProductWithBadTitle;

    private final int ZERO_PRICE = 0;
    private final int NEGATIVE_PRICE = -250;
    private final double DOUBLE_PRICE = 120.25;
    private final int BIG_PRICE = 1000000000;

    private final double NEW_PUTTED_PRICE = 5020.25;
    private final String EMPTY_TITLE = "";
    private final String DIGIT_TITLE = "12345";
    private final String SPECIAL_SYMBOL_TITLE = "##$$@@";
    private final String EMAIL_TITLE = "support@geekbrains.ru";
    private final int NUMBER_OF_PRODUCT_IN_CATEGORY_TO_PUT = 12;
    private final int NUMBER_OF_PRODUCT_IN_CATEGORY_TO_GET = 2;
    private final int NUMBER_OF_PRODUCT_IN_CATEGORY_TO_DELETE = 2;

    public ProductTests() throws IOException {
    }

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        categoryService = client.create(CategoryService.class);
        productService = client.create(ProductService.class);
        productsMapper = DbUtils.getProductsMapper();

    }

    Product productToPut = getProductToPut();

    private Product getProductToPut() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> productResponse = categoryService
                .getCategory(id)
                .execute();
        Product productToPut = productResponse.body().getProducts().get(NUMBER_OF_PRODUCT_IN_CATEGORY_TO_PUT);
        System.out.println("?????????????? Product to put: " + productResponse.body().getProducts().get(NUMBER_OF_PRODUCT_IN_CATEGORY_TO_PUT));
        return productToPut;
    }

    private String getFakerFoodTitle() {
        return faker.food().dish();
    }

    private String getFakerAncientTitle() {
        return faker.ancient().titan();
    }

    @DisplayName("(GET) ???????????????? ???????????? ??????????????????")
    @Test
    void getProductsTest() throws IOException {

        Response<ArrayList<Product>> response = productService.getProducts().execute();
        log.info(response.toString());
        assertThat(response.code(), equalTo(200));
    }

    @DisplayName("(POST) ?????????????????? ?????????????? ?????????????????? FOOD")
    @Test
    void postProductTest() throws IOException {

        final String fakerFoodTitle = getFakerFoodTitle();
        final int price = (int) ((Math.random() + 1) * 500);
        final CategoryType categoryType = CategoryType.FOOD;

        productFoodCategory = new Product()
                .withTitle(fakerFoodTitle)
                .withPrice(price)
                .withCategoryTitle(categoryType.getTitle());

        Response<Product> response = productService.createProduct(productFoodCategory).execute();

        log.info(response.body().toString());

        long postProductId = response.body().getId();

        assertThat(response.body().getTitle(), equalTo(productFoodCategory.getTitle()));
        assertThat(response.body().getPrice(), equalTo(productFoodCategory.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(productFoodCategory.getCategoryTitle()));
        assertThat(response.code(), equalTo(201));

        Products checkFoodProduct = productsMapper.selectByPrimaryKey(postProductId);

        log.info("???????????????? ?? ?????????????? (productFoodCategory.getTitle()): " + productFoodCategory.getTitle());
        log.info("???????????????? ???????????????? ?? ???????? (checkFoodProduct.getTitle()): " + checkFoodProduct.getTitle());
        assertThat(checkFoodProduct.getTitle(), equalTo(productFoodCategory.getTitle()));

        ProductsExample example = new ProductsExample();
        example.createCriteria()
                .andTitleEqualTo(fakerFoodTitle)
                .andPriceEqualTo(price)
                .andCategory_idEqualTo(categoryType.getId().longValue());
        final List<Products> products = productsMapper.selectByExample(example);

        assertThat(products, contains(hasProperty("id", equalTo(response.body().getId().longValue()))));
    }

    @DisplayName("(POST) ???????????????? ???????????????? ?? ?????????????? ?????????????????????? ????????????????????")
    @ParameterizedTest(name = "???????? ???{index}: ???????????????? {index} - {arguments}")
    @ValueSource(strings = {EMPTY_TITLE, DIGIT_TITLE, SPECIAL_SYMBOL_TITLE, EMAIL_TITLE})
    void postParamTitleProductTest(String postParamTitle) throws IOException {
        productToChangeTitle = new Product()
                .withTitle(postParamTitle)
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(productToChangeTitle).execute();
        log.info(response.toString());
        log.info(response.body().toString());
        assertThat(response.code(), equalTo(201));
        assertThat(productToChangeTitle.getTitle(), equalTo(postParamTitle));

        long postParamTitleProductId = response.body().getId();

        Products checkParamTitle = productsMapper.selectByPrimaryKey(postParamTitleProductId);

        log.info("postParamTitle: " + postParamTitle);
        log.info("???????????????? ?? ?????????????? (productFoodCategory.getTitle()): " + productToChangeTitle.getTitle());
        log.info("???????????????? ???????????????? ?? ???????? (checkFoodProduct.getTitle()): " + checkParamTitle.getTitle());

        assertThat(checkParamTitle.getTitle(), equalTo(postParamTitle));
    }

    @DisplayName("(POST) ???????????????? ???????????????? ?? ?????????????? ?????????????????????? ????????????")
    @ParameterizedTest(name = "???????? ???{index}: ???????? {index} - {arguments}")
    @ValueSource(ints = {ZERO_PRICE, NEGATIVE_PRICE, (int) DOUBLE_PRICE, BIG_PRICE})
    void postParamPriceProductTest(int postParamPrice) throws IOException {
        postProductWithBadPrice = new Product()
                .withTitle(getFakerAncientTitle())
                .withPrice(postParamPrice)
                .withCategoryTitle(CategoryType.FURNITURE.getTitle());

        Response<Product> response = productService.createProduct(postProductWithBadPrice).execute();
        log.info(response.toString());
        log.info(response.body().toString());
        assertThat(response.code(), equalTo(201));

        long postParamPriceProductId = response.body().getId();

        Products checkPostParamPriceProduct = productsMapper.selectByPrimaryKey(postParamPriceProductId);

        log.info("???????????????? ?? ?????????????? (productFoodCategory.getPrice()): " + postProductWithBadPrice.getPrice());
        log.info("???????????????? ???????????????? ?? ???????? (checkFoodProduct.getTitle()): " + checkPostParamPriceProduct.getPrice());
        assertThat(checkPostParamPriceProduct.getPrice(), equalTo(postProductWithBadPrice.getPrice()));
    }

    @DisplayName("(PUT) ???????????????? ?? ???????????????? ???????????????? ?? ????????")
    @Test
    void putChangeTitleAndPrice() throws IOException {

        putNewPriceAndTitle = new Product()
                .withId(productToPut.getId())
                .withTitle("SUPER-BONUS_new_version")
                .withPrice((int) NEW_PUTTED_PRICE)
                .withCategoryTitle("Furniture");

        Response<Product> response = productService.putProduct(putNewPriceAndTitle).execute();
        log.info(response.toString());
        log.info(response.body().toString());
        assertThat(response.code(), equalTo(200));
        assertThat(response.body().getPrice(), equalTo((int) NEW_PUTTED_PRICE));
        log.info(String.valueOf("NEW_PUTTED_PRICE from request: " + NEW_PUTTED_PRICE));
        log.info("NEW_PUTTED_PRICE from response: " + response.body().getPrice().toString());


        Products checkProductParam = productsMapper.selectByPrimaryKey(Long.valueOf(putNewPriceAndTitle.getId()));

        log.info("???????????????? ???? ???????????? (productToPut.getTitle()): " + productToPut.getTitle());
        log.info("?????????? ???????????????? ?? ?????????????? (putNewPriceAndTitle.getTitle()): " + putNewPriceAndTitle.getTitle());
        log.info("?????????? ???????????????? ???????????????? ?? ???????? (checkProduct.getTitle()): " + checkProductParam.getTitle());
        assertThat(checkProductParam.getTitle(), equalTo(putNewPriceAndTitle.getTitle()));
        log.info("???????? ???? ???????????? (productToPut.getPrice()): " + productToPut.getPrice());
        log.info("?????????? ???????? ?? ?????????????? (putNewPriceAndTitle.getPrice()): " + putNewPriceAndTitle.getPrice());
        log.info("?????????? ???????? ???????????????? ?? ???????? (checkProduct.getPrice()): " + checkProductParam.getPrice());
        assertThat(checkProductParam.getPrice(), equalTo(putNewPriceAndTitle.getPrice()));

    }


    @DisplayName("(PUT) ?????????????????? ???????? ???????????????? ???? ???????????? ???????????????????? ????????????????")
    @ParameterizedTest(name = "???????? ???{index}: ???????? {index} - {arguments}")
    @ValueSource(ints = {ZERO_PRICE, NEGATIVE_PRICE, (int) DOUBLE_PRICE, BIG_PRICE})
    void putParamPriceProductTest(int putParamPrice) throws IOException {
        putProductWithBadPrice = new Product()
                .withId(productToPut.getId())
                .withTitle(productToPut.getTitle())
                .withPrice(putParamPrice)
                .withCategoryTitle(productToPut.getCategoryTitle());

        System.out.println("?????????????????????? ???????? " + putParamPrice);
        System.out.println("?????????????? ???????? " + productToPut.getPrice());
        Response<Product> response = productService.putProduct(putProductWithBadPrice).execute();
        log.info(response.toString());
        log.info(String.valueOf(response.code()));
        assertThat(response.code(), equalTo(200));
        log.info(response.body().toString());

        log.info("NEW_PUTTED_PRICE form request: " + putParamPrice);
        log.info("NEW_PUTTED_PRICE from response: " + response.body().getPrice().toString());
    }

    @DisplayName("(PUT) ?????????????????? ???????????????? ???????????????? ???? ???????????? ???????????????????? ????????????????")
    @ParameterizedTest(name = "???????? ???{index}: ???????????????? {index} - {arguments}")
    @ValueSource(strings = {EMPTY_TITLE, DIGIT_TITLE, SPECIAL_SYMBOL_TITLE, EMAIL_TITLE})
    void putParamTitleProductTest(String putParamTitle) throws IOException {
        putProductWithBadTitle = new Product()
                .withId(productToPut.getId())
                .withTitle(putParamTitle)
                .withPrice(productToPut.getPrice())
                .withCategoryTitle(CategoryType.FURNITURE.getTitle());

        Response<Product> response = productService.putProduct(putProductWithBadTitle).execute();
        log.info(response.toString());
        log.info(response.body().toString());
        assertThat(response.code(), equalTo(200));
        log.info("NEW_PUTTED_TITLE from response: " + response.body().getTitle().toString());
        log.info("NEW_PUTTED_TITLE form request: " + putParamTitle);
    }

    @DisplayName("(POST) ?????????????????? ?????????????? c ???????????????????????? ID (????????????????????)")
    @Test
    void postWithIdProductTest() throws IOException {
        productWithId = new Product()
                .withId((int) ((Math.random() + 1) * 8000))
                .withTitle("My ID product")
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle("Furniture");

        Response<Product> response = productService.createProduct(productWithId).execute();
        log.info(response.toString());
        assertThat(response.code(), equalTo(400));
    }

    @DisplayName("(POST) ?????????????????? ?????????????? ???????????????????????????? ?????????????????? (????????????????????)")
    @Test
    void postNonExistCategoryProductTest() throws IOException {
        productNonExistCategory = new Product()
                .withTitle(getFakerFoodTitle())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle("Unknown category");

        Response<Product> response = productService.createProduct(productNonExistCategory).execute();
        log.info(response.toString());
        assertThat(response.code(), equalTo(500));
    }

    @DisplayName("(GET) ???????????????? ?????????????? ???? ?????? ?????????????????????? ???????????? ?? ??????????????????")
    @Test
    void getProductByNumberInCategoryTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> productsResponse = categoryService
                .getCategory(id)
                .execute();

        Product productToGet = productsResponse.body().getProducts().get(NUMBER_OF_PRODUCT_IN_CATEGORY_TO_GET);
        Response<Product> getProductResponse = productService
                .getProduct(productToGet.getId())
                .execute();

        log.info("Response.body = " + getProductResponse.body().toString());
        log.info("Product title: " + getProductResponse.body().getTitle());
        assertThat(getProductResponse.body().getTitle(), equalTo(productToGet.getTitle()));
        assertThat(getProductResponse.body().getId(), equalTo(productToGet.getId()));

        long getProductByNumberId = getProductResponse.body().getId();

        Products checkGetProductTitle = productsMapper.selectByPrimaryKey(getProductByNumberId);

        log.info("???????????????? ?? ?????????????? (productToGet.getTitle()): " + productToGet.getTitle());
        log.info("???????????????? ???????????????? ?? ???????? (checkFoodProduct.getTitle()): " + checkGetProductTitle.getTitle());
        assertThat(checkGetProductTitle.getTitle(), equalTo(productToGet.getTitle()));
        log.info("???????? ?? ?????????????? (productToGet.getPrice()): " + productToGet.getPrice());
        log.info("???????? ???????????????? ?? ???????? (checkFoodProduct.getPrice()): " + checkGetProductTitle.getPrice());
        assertThat(checkGetProductTitle.getPrice(), equalTo(productToGet.getPrice()));
    }


    @DisplayName("(DELETE) ?????????????? ??????????????")
    @Test
    void deleteProductToDeleteTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> productsToChooseToDeleteResponse = categoryService
                .getCategory(id)
                .execute();
        System.out.println("productsToChooseToDeleteResponse" + productsToChooseToDeleteResponse);

        Product productToDelete = productsToChooseToDeleteResponse.body().getProducts().get(NUMBER_OF_PRODUCT_IN_CATEGORY_TO_DELETE);
        System.out.println("?????????????? ?????? ????????????????: " + productToDelete);
        System.out.println("Id ???????????????? ?????? ????????????????: " + productToDelete.getId());

        int countProductsBefore = DbUtils.countProducts(productsMapper);
        log.info("countProductsBefore " + countProductsBefore);
        Response<Void> response = productService
                .deleteProduct(productToDelete.getId())
                .execute();

        assertThat(response.isSuccessful(), is(true));
        int countProdactsAfter = DbUtils.countProducts(productsMapper);
        log.info("countProdactsAfter " + countProdactsAfter);
    }
}
