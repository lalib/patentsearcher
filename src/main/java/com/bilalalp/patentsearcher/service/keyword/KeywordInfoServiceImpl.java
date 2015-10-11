package com.bilalalp.patentsearcher.service.keyword;

import com.bilalalp.patentsearcher.dao.keywordinfo.KeywordInfoDao;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.service.AbstractService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
public class KeywordInfoServiceImpl extends AbstractService<KeywordInfo> implements KeywordInfoService {

    private KeywordInfoDao keywordInfoDao;

    @Autowired
    public KeywordInfoServiceImpl(final KeywordInfoDao dao) {
        super(dao);
        setKeywordInfoDao(dao);
    }
}