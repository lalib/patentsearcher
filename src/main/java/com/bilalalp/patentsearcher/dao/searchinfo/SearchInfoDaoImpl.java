package com.bilalalp.patentsearcher.dao.searchinfo;

import com.bilalalp.patentsearcher.dao.base.AbstractDao;
import com.bilalalp.patentsearcher.dto.SearchInfoDto;
import com.bilalalp.patentsearcher.entity.SearchInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class SearchInfoDaoImpl extends AbstractDao<SearchInfo> implements SearchInfoDao {

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<SearchInfoDto> findAllSearchInfos() {

        return getEntityManager().createQuery("SELECT NEW " +
                "com.bilalalp.patentsearcher.dto.SearchInfoDto(s.id, s.searchInfoStatusType, s.creationDate, COUNT(p.id), s.startTime, s.endTime) " +
                "FROM SearchInfo s " +
                "LEFT JOIN s.patentInfoList p " +
                "GROUP BY s.id " +
                "ORDER BY s.creationDate DESC ")
                .<SearchInfoDto>getResultList();
    }
}