package com.cgpr.mineur.service;

import java.util.List;

public interface GenericService<T, ID> {
    List<T> listAll();
    T getById(ID id);
    T save(T dto);
    void delete(ID id);
}
