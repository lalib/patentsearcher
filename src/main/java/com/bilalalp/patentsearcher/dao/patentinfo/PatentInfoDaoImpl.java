package com.bilalalp.patentsearcher.dao.patentinfo;

import com.bilalalp.patentsearcher.dao.base.AbstractDao;
import com.bilalalp.patentsearcher.entity.ContentSearchInfoStatusType;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PatentInfoDaoImpl extends AbstractDao<PatentInfo> implements PatentInfoDao {

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long getTotalLinkCount(final Long searchInfoId) {

        return (Long) getEntityManager()
                .createQuery("SELECT COUNT(p) FROM PatentInfo p WHERE p.searchInfo.id = :id")
                .setParameter("id", searchInfoId)
                .<Long>getSingleResult();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long getAnalysiedLinkCount(Long searchInfoId) {
        return getLinkCountByContentSearchInfoType(searchInfoId, ContentSearchInfoStatusType.ANALYSIED);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Long getNotAnalysiedLinkCount(Long searchId) {
        return getLinkCountByContentSearchInfoType(searchId, ContentSearchInfoStatusType.NOT_ANALYSIED);
    }

    @Override
    public List<PatentInfo> getPatentInfoListBySearchInfoId(final Long searchInfoId) {

        return getEntityManager()
                .createQuery("Select p from PatentInfo p where p.searchInfo.id=:id")
                .setParameter("id",searchInfoId)
                .getResultList();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    private Long getLinkCountByContentSearchInfoType(Long searchInfoId, ContentSearchInfoStatusType contentSearchInfoStatusType) {
        return (Long) getEntityManager()
                .createQuery("SELECT COUNT(p) FROM PatentInfo p WHERE p.searchInfo.id = :id AND p.contentSearchInfoStatusType = :contentSearchInfoStatusType")
                .setParameter("id", searchInfoId)
                .setParameter("contentSearchInfoStatusType", contentSearchInfoStatusType)
                .getSingleResult();
    }
}
