package com.bilalalp.patentsearcher.dao.siteinfo;

import com.bilalalp.patentsearcher.dao.base.AbstractDao;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.entity.SiteInfo_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class SiteInfoDaoImpl extends AbstractDao<SiteInfo> implements SiteInfoDao {

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public SiteInfo getSiteInfoBySiteKey(final String siteKey) {

        final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<SiteInfo> query = criteriaBuilder.createQuery(SiteInfo.class);
        final Root<SiteInfo> from = query.from(SiteInfo.class);
        query.where(criteriaBuilder.equal(from.get(SiteInfo_.siteKey), siteKey));
        try {

            return getEntityManager().createQuery(query).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}