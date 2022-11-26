
import org.junit.Assert;
import org.junit.Test;

public class RestHerokuappNegativeTest {

    public static void main(String[] args) {

    }

    String login = "admin";
    String password = "password123";

    String wrongLogin = "wrongLogin";
    String wrongPassword = "wrongPassword";
    String body = "{\n" +
            "    \"firstname\" : \"Maria\",\n" +
            "    \"lastname\" : \"Mir\",\n" +
            "    \"totalprice\" : 15000,\n" +
            "    \"depositpaid\" : true,\n" +
            "    \"bookingdates\" : {\n" +
            "        \"checkin\" : \"2022-10-01\",\n" +
            "        \"checkout\" : \"2022-11-15\"\n" +
            "    },\n" +
            "    \"additionalneeds\" : \"Breakfast\"\n" +
            "}";
    String wrongBody = "{firstname}";
    String wrongBody2 = "{\n" +
            "    \"firstname\" : \"Maria\"" +
            "}";
    Integer wrongId = 0;


    //Негативная проверка на получение токена(неверные логин\пароль)
    @Test
    public void getTokenSendWrongPassword() {
        String token = Tests.getToken(login, wrongPassword);
        Assert.assertNull("Токен возвращен для неправильного пароля ", token);
    }

    //негативная проверка на создание бронирования(неверное тело) если задан только ключ (без значения)
    @Test
    public void createBookingWithWrongBody400() {
        String body = Tests.createBooking(wrongBody, 400);
        Assert.assertEquals("Не совпал текст сообщения об ошибке ", "Bad Request", body);
    }

    //Создание бронирования(неверное тело) если в тело запроса передано только одно ключ-значение, а требуется больше
    @Test
    public void createBookingWithWrongBody500() {
        String body = Tests.createBooking(wrongBody2, 500);
        Assert.assertEquals("Не совпал текст сообщения об ошибке ", "Internal Server Error", body);
    }

    //Проверка бронирования с несуществующим id
    @Test
    public void CheckingBookingWithWrongId() {
        int actualStatusCode = Tests.getBooking(wrongId);
        Assert.assertEquals("бронирование с таким id не существует ", 404, actualStatusCode);
    }

    // Удаление бронирования с несуществующим id
    @Test
    public void deleteBookingWithWrongId() {
        String token = Tests.getToken();
        int actualStatusCode = Tests.deleteBooking(wrongId, token);
        Assert.assertEquals("Удаление бронирования с несуществующим id ", 405, actualStatusCode);
    }
}
