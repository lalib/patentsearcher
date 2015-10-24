package com.bilalalp.patentsearcher.service.base;


import com.bilalalp.patentsearcher.dao.base.Dao;
import com.bilalalp.patentsearcher.entity.AbstractEntity;
import com.bilalalp.patentsearcher.entity.SearchInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Setter
@Getter
@EnableTransactionManagement
public abstract class AbstractService<E extends AbstractEntity> implements BaseService<E> {

    private Dao<E> dao;

    public AbstractService(Dao<E> dao) {
        setDao(dao);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void persist(E entity) {
        getDao().persist(entity);
    }

    @Override
    public void persist(List<E> entityList) {
        getDao().persist(entityList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<E> findAll() {
        return getDao().findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(E entity) {
        getDao().remove(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(Long id) {
        getDao().remove(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public E findById(Long id) {
        return getDao().findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(E entity) {
        getDao().update(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void flush() {
        getDao().flush();
    }

    @Override
    public void refresh(E entity) {
        getDao().refresh(entity);
    }
}