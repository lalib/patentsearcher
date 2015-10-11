package com.bilalalp.patentsearcher.dao;

import com.bilalalp.patentsearcher.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Getter
@Setter
@Repository
@EnableTransactionManagement
public abstract class AbstractDao<E extends AbstractEntity> {

    @PersistenceContext(unitName = "patentSearcherEntityManagerFactory")
    private EntityManager entityManager;

    @Transactional(value = "jpaTransactionManager", propagation = Propagation.REQUIRED)
    public void persist(final E entity) {
        getEntityManager().persist(entity);
    }
}