package ru.bk252.kkso16.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import ru.bk252.kkso16.model.User;
import ru.bk252.kkso16.dao.UserDao;
import ru.bk252.kkso16.utils.PasswordHasher;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordHasher passwordHasher;

    private AuthService authService;

    @BeforeEach
    public void setUp() {
        authService = new AuthService(userDao, passwordHasher);
    }

    /**
     * Позитивный тест регистрации в системе пользователя petrov@email.ru с паролем pa$$w0rd1111
     */
    @Test
    public void RegisterNewUserShouldSuccess() {
        // сконфигурируем mock-объектам поведение, требуемое для выполнения позитивного теста.
        // Т.е. метод create объекта UserDao должен быть вызыван с параметром email, равным petrov@email.ru,
        // а параметру password после вызова должно быть присвоено значение хэш-функции от пароля pa$$w0rd1111.
        // Предположим, используется произвольная хэш-функция, которая для строки pa$$w0rd1111 дает значение
        // "12345678".

        when(passwordHasher.computeHash("pa$$w0rd1111"))
                .thenReturn("12345678");

        when(userDao.create("petrov@email.ru", "12345678"))
                .thenReturn(new User("petrov@email.ru", "petrov", "12345678", "USER"));

        User newUser = authService.Register("petrov@email.ru", "pa$$w0rd1111");
        assertThat(newUser.getEmail()).isEqualTo("petrov@email.ru");
    }

    @Test
    public void RegisterSameUserShouldCancell() {

        when(userDao.findByEmail("petrov@email.ru"))
                .thenReturn(new User(null, "petrov", "12345678", "USER"));

        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    User newUser = authService.Register(userDao.findByEmail("petrov@email.ru").getEmail(), "pa$$w0rd1111");
                }
        );

        assertThat(e.getMessage()).isEqualTo("email is null");
    }

    @Test
    public void LoginExistingUserShouldSuccess() {

        when(passwordHasher.computeHash("pa$$w0rd1111"))
                .thenReturn("12345678");

        when(userDao.findByEmail("petrov@email.ru"))
                .thenReturn(new User("petrov@email.ru", "petrov", "12345678", "USER"));


        User loginUser = authService.Login("petrov@email.ru", "pa$$w0rd1111");
        User existingUser = userDao.findByEmail("petrov@email.ru");


        assertThat(loginUser.getEmail().equals(existingUser.getEmail()));
    }

    @Test
    public void LoginNonExistingUserShouldCancel() {

        when(userDao.findByEmail("george@email.ru"))
                .thenReturn(new User(null, null, null, null));


        Exception e = assertThrows(
                NullPointerException.class,
                () -> {
                    User newUser = authService.Login("george@email.ru", null);
                }
        );

        assertThat(e.getMessage()).isEqualTo("Cannot invoke \"String.equals(Object)\" because \"passwordHash\" is null");
    }

    @Test
    public void RegisterWithEmptyEmailShouldThrowException() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    User newUser = authService.Register("", "pa$$w0rd1111");
                }
        );

        assertThat(e.getMessage()).isEqualTo("email is empty");
    }

    @Test
    public void RegisterWithNullPasswordShouldThrowException() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    User newUser = authService.Register("george@email.ru", null);
                }
        );

        assertThat(e.getMessage()).isEqualTo("password is null");
    }

    @Test
    public void RegisterWithShortPasswordShouldThrowException() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    User newUser = authService.Register("george@email.ru", "pa$$");
                }
        );

        assertThat(e.getMessage()).isEqualTo("password too short");
    }


}
