package com.bilalalp.patentsearcher.dao.base;

import com.bilalalp.patentsearcher.entity.AbstractEntity;
import com.bilalalp.patentsearcher.entity.SearchInfo;

import java.util.List;

public interface Dao<E extends AbstractEntity> {

    void persist(E entity);

    List<E> findAll();

    E findById(Long id);

    void remove(Long id);

    void remove(E entity);

    void update(E entity);

    void flush();

    void refresh(E entity);
}
