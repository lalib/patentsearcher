package com.bilalalp.patentsearcher.service;

import com.bilalalp.patentsearcher.entity.AbstractEntity;

public interface IService<E extends AbstractEntity> {

    void persist(E entity);
}
