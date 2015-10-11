package com.bilalalp.patentsearcher.service.keyword;

import com.bilalalp.patentsearcher.dao.keywordinfo.KeywordInfoDao;
import com.bilalalp.patentsearcher.dto.KeywordDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.service.base.AbstractService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void persist(final String keywordText) {

        final KeywordInfo keywordInfo = new KeywordInfo();
        keywordInfo.setKeyword(keywordText);
        super.persist(keywordInfo);
    }

    @Override
    @Transactional
    public void update(KeywordDto keywordDto) {

        final String text = keywordDto.getText();

        final KeywordInfo keywordInfo = findById(keywordDto.getId());
        keywordInfo.setKeyword(text);

        super.update(keywordInfo);
    }
}