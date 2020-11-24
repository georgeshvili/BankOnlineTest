package ru.bk252.kkso16.services;

import ru.bk252.kkso16.dao.UserDao;
import ru.bk252.kkso16.model.User;
import ru.bk252.kkso16.utils.PasswordHasher;

/**
 * Служба идентификации, аутентификации и авторизации.
 */
public class AuthService {
    /**< Хранилище информации о пользователях */
    private final UserDao userDao;
    /**< Объект хэширования паролей */
    private final PasswordHasher passwordHasher;

    /**
     * \param   userDao объект постоянного хранения информации о пользователях
     * \param   passwordHasher объект, выполняющий хэширование паролей в процессе
     *          регистрации/логина пользователей
     */
    public AuthService(UserDao userDao, PasswordHasher passwordHasher) {
        this.userDao = userDao;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Регистрация новой учетной записи пользователя в системе банка.
     *
     * \param[in]   email имя нового пользователя.
     * \param[in]   password пароль нового пользователя.
     *              Должен удовлетворять определенным требованиям см. []
     * \return      объект нового пользователя, если регистарация прошла устешно,
     *              null если пользователь с таким \email уже есть в хранилище.
     */
    public User Register(String email, String password) {
        if (email == null) {
            throw new IllegalArgumentException("email is null");
        }

        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("email is empty");
        }

        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }

        if (password.length() < 12) {
            throw new IllegalArgumentException("password too short");
        }

        String passwordHash = this.passwordHasher.computeHash(password);

        return this.userDao.create(email, passwordHash);
    }

    /**
     * Идентификация и аутентификация пользователя в системе.
     *
     * Вход в систему возможен только для ранее зарегестрированного пользователя
     *
     * \param[in]   email адрес электронной почты пользователя.
     * \param[in]   password пароль пользователя.
     * \return      объект существующего пользователя, если логин выполнен устешно,
     *              null если пользователя с таким \email нет в хранилище или
     *              введён неправильный пароль.
     */
    public User Login(String email, String password) {
        User user = this.userDao.findByEmail(email);
        if (user == null) {
            return null;
        }

        String passwordHash = this.passwordHasher.computeHash(password);
        if (!passwordHash.equals(user.getPasswordHash())) {
            return null;
        }

        return user;
    }
}
