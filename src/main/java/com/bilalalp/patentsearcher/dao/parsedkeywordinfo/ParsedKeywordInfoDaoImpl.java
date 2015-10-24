package com.bilalalp.patentsearcher.dao.parsedkeywordinfo;

import com.bilalalp.patentsearcher.dao.base.AbstractDao;
import com.bilalalp.patentsearcher.dto.KeywordCountDto;
import com.bilalalp.patentsearcher.entity.ContentType;
import com.bilalalp.patentsearcher.entity.ParsedKeywordInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ParsedKeywordInfoDaoImpl extends AbstractDao<ParsedKeywordInfo> implements ParsedKeywordInfoDao {

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeAllAbstractKeywords(final Long patentInfoId) {

        getEntityManager()
                .createQuery("DELETE FROM ParsedKeywordInfo p WHERE p.contentType = :contentType AND p.patentInfo.id = :patentInfoId")
                .setParameter("contentType", ContentType.ABSTRACT)
                .setParameter("patentInfoId", patentInfoId)
                .executeUpdate();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<KeywordCountDto> getKeywordCountDto(final List<Long> searchInfoIdList, final List<String> stopWordInfoList, final List<ContentType> contentTypeList) {

        final String qlString = "SELECT NEW com.bilalalp.patentsearcher.dto.KeywordCountDto(COUNT(p.keyword),p.keyword) " +
                "FROM ParsedKeywordInfo p " +
                "WHERE p.keyword NOT IN :stopWordInfoList " +
                "AND p.contentType IN :contentTypeList " +
                "AND p.patentInfo.searchInfo.id IN :searchInfoIdList " +
                "GROUP BY p.keyword " +
                "ORDER BY COUNT(p.keyword) DESC";

        return getEntityManager()
                .createQuery(qlString)
                .setParameter("contentTypeList", contentTypeList)
                .setParameter("stopWordInfoList", stopWordInfoList)
                .setParameter("searchInfoIdList", searchInfoIdList)
                .setMaxResults(1000)
                .getResultList();
    }
}