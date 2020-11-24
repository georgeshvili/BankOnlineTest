package ru.bk252.kkso16.dao;

import ru.bk252.kkso16.model.User;

/**
 * Хранилише информации пользователей.
 */
public interface UserDao {
    /**
     * Создание нового пользователя в хранилище.
     *
     * Успешное создание нового пользователя возможно, только если в
     * хранилище отсутсвует учётная запись с адресом \p email.
     *
     * \param[in] email адрес электронной почты нового пользователя.
     * \param[in] passwordHash хэш от пароля создаваемого пользователя.
     * \return объект нового пользователя.
     *         null, если такой пользователь уже есть в хранилище.
     *
     */
    User create(String email, String passwordHash);

    /**
     * Поиск пользователя по email.
     *
     * \param[in] email адрес электронной почты пользователя.
     * \return объект пользователя с адресом \p email.
     *         null если пользователь с адресом \p email не найден.
     */
    User findByEmail(String email);

    /**
     * Обновление информации о пользователе.
     *
     * \param[in] user объект пользователя с новыми данными.
     */
    void update(User user);

    /**
     * Удаление информации о пользователе \p user.
     *
     * \param[in] объкт удаляемого пользователя.
     */
    void delete(User user);
}