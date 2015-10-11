package com.bilalalp.patentsearcher.service.base;

import com.bilalalp.patentsearcher.entity.AbstractEntity;

import java.util.List;

public interface BaseService<E extends AbstractEntity> {

    void persist(E entity);

    List<E> findAll();

    E findById(Long id);

    void remove(Long id);

    void remove(E entity);

    void update (E entity);
}