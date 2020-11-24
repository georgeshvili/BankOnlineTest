package ru.bk252.kkso16.utils;

/**
 * Вычисление хэш-значения от пароля
 */
public interface PasswordHasher {
    /**
     * Вычисление значения хэш-функции от пароля
     *
     * \param[in] password строковое представление пароля
     * \return Возвращает хэш от пароля в виде строки шестрадцатеричных цифр
     */
    String computeHash(String password);
}