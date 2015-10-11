package com.bilalalp.patentsearcher.dao;

import com.bilalalp.patentsearcher.entity.AbstractEntity;

public interface Dao<E extends AbstractEntity> {

    void persist(E entity);
}
