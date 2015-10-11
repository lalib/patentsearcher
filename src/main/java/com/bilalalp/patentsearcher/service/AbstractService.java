package com.bilalalp.patentsearcher.service;


import com.bilalalp.patentsearcher.dao.Dao;
import com.bilalalp.patentsearcher.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Setter
@Getter
@EnableTransactionManagement
public abstract class AbstractService<E extends AbstractEntity> implements IService<E> {

    private Dao<E> dao;

    public AbstractService(Dao<E> dao) {
        setDao(dao);
    }

    @Override
    @Transactional(transactionManager = "jpaTransactionManager", propagation = Propagation.REQUIRED)
    public void persist(E entity) {
        getDao().persist(entity);
    }
}