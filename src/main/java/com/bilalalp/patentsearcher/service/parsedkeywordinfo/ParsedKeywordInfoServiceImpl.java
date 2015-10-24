package com.bilalalp.patentsearcher.service.parsedkeywordinfo;

import com.bilalalp.patentsearcher.dao.parsedkeywordinfo.ParsedKeywordInfoDao;
import com.bilalalp.patentsearcher.dto.KeywordCountDto;
import com.bilalalp.patentsearcher.entity.ContentType;
import com.bilalalp.patentsearcher.entity.ParsedKeywordInfo;
import com.bilalalp.patentsearcher.service.base.AbstractService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Setter
@Service
public class ParsedKeywordInfoServiceImpl extends AbstractService<ParsedKeywordInfo> implements ParsedKeywordInfoService {

    private ParsedKeywordInfoDao parsedKeywordInfoDao;

    @Autowired
    public ParsedKeywordInfoServiceImpl(ParsedKeywordInfoDao dao) {
        super(dao);
        setParsedKeywordInfoDao(dao);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeAllAbstractKeywordsWithNewTransaction(final Long patentInfoId) {
        parsedKeywordInfoDao.removeAllAbstractKeywords(patentInfoId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistWithNewTransaction(final List<ParsedKeywordInfo> parsedKeywordInfoList) {
        parsedKeywordInfoDao.persist(parsedKeywordInfoList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<KeywordCountDto> getKeywordCountDto(final List<Long> searchInfoIdList, final List<String> keywordInfoIdList, final List<ContentType> contentTypeList) {

        return parsedKeywordInfoDao.getKeywordCountDto(searchInfoIdList, keywordInfoIdList, contentTypeList);
    }
}