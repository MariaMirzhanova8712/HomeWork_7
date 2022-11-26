import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class NegativeTests {
    //Получение токена(неверные логин\пароль)
    public static String getToken(String login, String password, int statusCode) {
        String response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .body("{\n" +
                        "    \"username\" : \"" + login + "\",\n" +
                        "    \"password\" : \"" + password + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .post("https://restful-booker.herokuapp.com/auth")
                .then()
                .log().status()
                .statusCode(statusCode)
                .extract()
                .response().body().
                path("token");
        System.out.println("\u001B[32m" + "Запрос getToken вернул значение " + response + "\u001B[0m");
        return response;
    }

    //Создание бронирования(неверное тело)
    public static Integer createBooking() {
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

    //Проверка бронирования с несуществующим id
    public static String updateBookingCheckout(String checkout, Integer bookingid, String token, Integer expectedStatusCode) {
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
                        "        \"checkout\" : \"" + checkout + "\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
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

    //Удаление бронирования с несуществующим id
    public static void deleteBooking(Integer bookingid, int statusCode) {
        var rec = RestAssured.given()  //bookingid
                .baseUri("https://restful-booker.herokuapp.com/")
                .headers("Cookie", "token=" )
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/" + bookingid)
                .then()
                .log()
                .body()
                .log().status()
                .statusCode(statusCode)
                .extract()
                .response();
        System.out.println("Удалили бронирование с id " + bookingid);
    }

    public static Integer getBooking(Integer bookingid) {
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
