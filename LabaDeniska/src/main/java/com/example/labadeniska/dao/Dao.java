package com.example.labadeniska.dao;

import java.util.Collection;
import java.util.List;

public interface Dao<T, ID> {
    T findByID(long id);
    List<T> findAll();
    T save(T entity);
    T update(T entity);
    void deleteById(long integer);
}
