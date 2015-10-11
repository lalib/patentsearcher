package com.bilalalp.patentsearcher.service.stopwordinfo;

import com.bilalalp.patentsearcher.dao.stopwordinfo.StopWordInfoDao;
import com.bilalalp.patentsearcher.dto.StopWordInfoDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.entity.StopWordInfo;
import com.bilalalp.patentsearcher.service.base.AbstractService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@Service
public class StopWordInfoServiceImpl extends AbstractService<StopWordInfo> implements StopWordInfoService {

    private StopWordInfoDao stopWordInfoDao;

    @Autowired
    public StopWordInfoServiceImpl(StopWordInfoDao dao) {
        super(dao);
        setStopWordInfoDao(dao);
    }

    @Override
    @Transactional
    public void update(final StopWordInfoDto stopWordInfoDto) {

        final String text = stopWordInfoDto.getText();

        final StopWordInfo stopWordInfo = findById(stopWordInfoDto.getId());
        stopWordInfo.setWord(text);

        super.update(stopWordInfo);
    }

    @Override
    @Transactional
    public void persist(final String wordText) {

        final StopWordInfo stopWordInfo = new StopWordInfo();
        stopWordInfo.setWord(wordText);
        super.persist(stopWordInfo);
    }
}
