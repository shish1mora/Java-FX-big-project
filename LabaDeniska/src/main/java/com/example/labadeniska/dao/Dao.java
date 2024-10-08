package com.example.labadeniska.dao;

import java.util.Collection;
import java.util.List;

import java.util.List;

/**
 * Общий интерфейс для DAO (Data Access Object) операций.
 *
 * @param <T> Тип сущности.
 * @param <ID> Тип идентификатора сущности.
 */
public interface Dao<T, ID> {

    /**
     * Находит сущность по её идентификатору.
     *
     * @param id Идентификатор сущности.
     * @return Сущность с указанным идентификатором.
     */
    T findByID(long id);

    /**
     * Получает все сущности.
     *
     * @return Список всех сущностей.
     */
    List<T> findAll();

    /**
     * Сохраняет новую сущность.
     *
     * @param entity Сущность для сохранения.
     * @return Сохраненная сущность.
     */
    T save(T entity);

    /**
     * Обновляет данные сущности.
     *
     * @param entity Сущность с обновленными данными.
     * @return Обновленная сущность.
     */
    T update(T entity);

    /**
     * Удаляет сущность по её идентификатору.
     *
     * @param id Идентификатор сущности для удаления.
     */
    void deleteById(long id);
}
