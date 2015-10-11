package com.bilalalp.patentsearcher.service.keywordinfo;

import com.bilalalp.patentsearcher.dao.keywordinfo.KeywordInfoDao;
import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.service.base.AbstractService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void update(KeywordInfoDto keywordInfoDto) {

        final String text = keywordInfoDto.getText();

        final KeywordInfo keywordInfo = findById(keywordInfoDto.getId());
        keywordInfo.setKeyword(text);

        super.update(keywordInfo);
    }
}