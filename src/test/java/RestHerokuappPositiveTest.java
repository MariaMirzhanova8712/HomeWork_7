
import org.junit.Assert;
import org.junit.Test;

public class RestHerokuappPositiveTest {

    public static void main(String[] args) {
    }

    String login = "admin";
    String password = "password123";

    //Получение токена, вывод токена в консоль:
    @Test
    public void getToken() {
        String token = Tests.getToken();
        Assert.assertNotNull("не удалось получить токен ", token);

    }

    //Создание и проверка  бронирования:
    @Test
    public void createBooking() {
        Integer id = Tests.createBooking();
        Assert.assertNotNull("не удалось получить id бронирования ", id);
    }

    //Создание, обновление и проверка  бронирования:
    @Test
    public void UpdateBooking() {
        String checkout = "2022-10-11";
        Integer id = Tests.createBooking();
        String token = Tests.getToken();
        String checkoutActual = Tests.updateBookingCheckout(checkout, id, token, 200);
        Assert.assertEquals("не совпали значения checkout ", checkout, checkoutActual);
    }

    //Создание, удаление, проверка что удалено бронирование
    @Test
    public void deleteBookingPositive200() {
        Integer id = Tests.createBooking();
        String token = Tests.getToken();

        Tests.deleteBooking(id, token);
        int statusCode = Tests.getBooking(id);
        Assert.assertEquals("бронирование не удалено ", statusCode, 404);
    }
}
