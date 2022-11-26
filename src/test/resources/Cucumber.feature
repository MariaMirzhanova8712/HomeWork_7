#language=en

@all

Feature: Проверка методов с сайта herokuapp.com

  @getToken_positive
  Scenario: Проверить запрос getToken - позитивная проверка
    When отправить запрос getToken и проверить что он вернул значение токена

  @getBooking_positive
  Scenario: проверить метод getBooking - позитивная проверка
    When получить из метода getBooking код бронирования, проверить что он не null

  @updateBooking_positive
    Scenario: проверить метод updateBooking - позитивная проверка
      When проверить метод updateBooking для даты "2022-10-11"

  @deleteBooking_positive
    Scenario: проверить метод deleteBookingPositive200 - позитивная проверка
      When удалить бронирование используя метод deleteBookingPositive и проверить что оно удалено

  @getToken_negative
    Scenario: проверить запрос getToken - негативная проверка (неверные логин\пароль)
    When отправить запрос getToken c неверным паролем "wrongPassword" или логином "wrongLogin"

  @getBooking_negative
  Scenario: проверить метод getBooking при создании бронирования с неверным телом - негативная проверка
    When создать бронирование с неверным телом "firstname", проверить статус код ответа 400

  @CheckingBookingWithWrongId_negative
  Scenario: Проверка бронирования с несуществующим id - негативная проверка
    When передать в метод getBooking несуществующий id - wrongId равный 0

    @deleteBookingWithWrongId_negative
    Scenario: Удаление бронирования с несуществующим id - негативная проверка
      When попытка удаления бронирования с несуществующим id - wrongId равным 0 используя метод deleteBooking

