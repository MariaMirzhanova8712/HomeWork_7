package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Before;

public class Steps {

    @Before
    public void before() {
    }

    @Then("^отправить запрос getToken и проверить что он вернул значение токена$")
    public void getTokenTest() {
        String token = getToken();
        Assert.assertNotNull("не удалось получить токен ", token);
    }

    @Then("^получить из метода getBooking код бронирования, проверить что он не null$")
    public void createBookingTest() {
        Integer id = createBooking();
        Assert.assertNotNull("не удалось получить id бронирования ", id);
    }

    @Then("^проверить метод updateBooking для даты (.*)$")
    public void updateBookingTest(String checkout) {
        Integer id = createBooking();
        String token = getToken();
        String checkoutActual = updateBookingCheckout(checkout, id, token, 200);
        Assert.assertEquals("не совпали значения checkout ",  checkout.replace("\"", ""),
                checkoutActual);
    }

    @Then("^удалить бронирование используя метод deleteBookingPositive и проверить что оно удалено$")
    public void deleteBookingPositiveTest(){
        Integer id = createBooking();
        String token = getToken();

        deleteBooking(id, token);
        int statusCode = getBooking(id);
        Assert.assertEquals("бронирование не удалено ", statusCode, 404);
    }
    //негативные проверки
    @Then("^отправить запрос getToken c неверным паролем \"([^\"]*)\" или логином \"([^\"]*)\"$")
    public void getTokenNegativeTest(String login, String wrongPassword) {
        String token = getToken(login, wrongPassword);
        Assert.assertNull("Токен возвращен для неправильного пароля ", token);
    }
    @Then("^создать бронирование с неверным телом \"([^\"]*)\", проверить статус код ответа (\\d+)$")
    public void createBookingWithWrongBodyTest(String wrongBody, int statusCodeExpected) {
        String body = createBooking(wrongBody, 400);
        Assert.assertEquals("Не совпал текст сообщения об ошибке ", "Bad Request", body);
    }

    @Then("^передать в метод getBooking несуществующий id - wrongId равный (\\d+)$")
    public void checkingBookingWithWrongIdTest(int wrongId) {
        int actualStatusCode = getBooking(wrongId);
        Assert.assertEquals("бронирование с таким id не существует ", 404, actualStatusCode);
    }

    @Then("^попытка удаления бронирования с несуществующим id - wrongId равным (\\d+) используя метод deleteBooking$")
    public void deleteBookingWithWrongIdTest(int wrongId) {
        String token = getToken();
        int actualStatusCode = deleteBooking(wrongId, token);
        Assert.assertEquals("Неожиданный код ответа на попытку удаления бронирования с несуществующим id ", 405, actualStatusCode);
    }
//_____________________________________________________________________________________________________________________
    public String getToken(String login, String password) {
        String response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .body("{\n" +
                        "    \"username\" : \"" + login + "\",\n" +
                        "    \"password\" : \"" + password + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .statusCode(200)
                .extract()
                .response().body().
                path("token");
        System.out.println("\u001B[32m" + "Запрос getToken вернул значение " + response + "\u001B[0m");
        return response;
    }

    public String getToken() {
        String response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .body("{\n" +
                        "    \"username\" : \"admin\",\n" +
                        "    \"password\" : \"password123\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .statusCode(200)
                .extract()
                .response().body().
                path("token");
        System.out.println("\u001B[32m" + "Запрос getToken вернул значение " + response + "\u001B[0m");
        return response;
    }

    //Создание и проверка  бронирования
    public Integer createBooking() {
        Integer response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .body("{\n" +
                        "    \"firstname\" : \"Maria\",\n" +
                        "    \"lastname\" : \"Mir\",\n" +
                        "    \"totalprice\" : 15000,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2022-10-01\",\n" +
                        "        \"checkout\" : \"2022-11-15\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .post("https://restful-booker.herokuapp.com/booking")
                .then()
                .statusCode(200)
                .extract()
                .response().body()
                .path("bookingid");
        System.out.println("Создали бронирование с id " + response);
        return response;
    }

    public String createBooking(String body, int statusCodeExpected) {
        String response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .body(body)
                .contentType(ContentType.JSON)
                .post("https://restful-booker.herokuapp.com/booking")
                .then()
                .statusCode(statusCodeExpected)
                .extract()
                .response()
                .getBody()
                .asString();
        System.out.println("Тело ответа '" + response + "'");
        return response;
    }

    //Создание, обновление и проверка  бронирования:
    public String updateBookingCheckout(String checkout, Integer bookingid, String token, Integer expectedStatusCode) {
        String response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/")
                .headers("Cookie", "token=" + token)
                .body("{\n" +
                        "    \"firstname\" : \"Maria\",\n" +
                        "    \"lastname\" : \"Mir\",\n" +
                        "    \"totalprice\" : 15000,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2022-10-01\",\n" +
                        "        \"checkout\" : " + checkout + "\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}").log().body()
                .contentType(ContentType.JSON)
                .put("https://restful-booker.herokuapp.com/booking/" + bookingid)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .response().body()
                .path("bookingdates.checkout");
        System.out.println("Изменили дату выезда на " + response + " для бронирования с id " + bookingid);
        return response;
    }


    //Создание, удаление, проверка что удалено бронирование:
    public int deleteBooking(Integer bookingid, String token) {
        int statusCode = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/")
                .headers("Cookie", "token=" + token)
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/" + bookingid)
                .then()
                .log()
                .body()
                .extract()
                .response()
                .statusCode();
        System.out.println("Попытка удаления для бронирования с id " + bookingid);
        return statusCode;
    }

    //Проверка бронирования с несуществующим id
    public Integer getBooking(Integer bookingid) {
        Integer response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/")
                .get("https://restful-booker.herokuapp.com/booking/" + bookingid)
                .then()
                .extract()
                .response()
                .statusCode();
        System.out.println("вернули код ответа для запроса getBooking " + response);
        return response;
    }



}
