package com.bilalalp.patentsearcher.dao.base;

import com.bilalalp.patentsearcher.entity.AbstractEntity;
import com.bilalalp.patentsearcher.util.TypeUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Getter
@Setter
@Repository
@EnableTransactionManagement
public abstract class AbstractDao<E extends AbstractEntity> implements Dao<E> {

    @PersistenceContext(unitName = "patentSearcherEntityManagerFactory")
    private EntityManager entityManager;

    private Class<E> clazz;

    public AbstractDao() {
        clazz = TypeUtil.getParametrizedType(this.getClass());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void persist(final E entity) {
        getEntityManager().persist(entity);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<E> findAll() {

        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        final Root<E> from = query.from(clazz);
        final CriteriaQuery<E> all = query.select(from);
        final TypedQuery<E> allQuery = getEntityManager().createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(Long id) {
        getEntityManager().remove(findById(id));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public E findById(Long id) {

        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        final Root<E> from = query.from(clazz);
        ParameterExpression<Long> p = criteriaBuilder.parameter(Long.class, "id");
        query.where(criteriaBuilder.equal(from.get("id"), p));
        return entityManager.createQuery(query).setParameter("id", id).getSingleResult();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(E entity) {
        getEntityManager().remove(entity);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void update(E entity) {
        getEntityManager().merge(entity);
    }
}