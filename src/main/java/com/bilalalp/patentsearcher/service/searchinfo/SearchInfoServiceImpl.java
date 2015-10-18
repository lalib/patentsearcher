package com.bilalalp.patentsearcher.service.searchinfo;

import com.bilalalp.patentsearcher.dao.base.Dao;
import com.bilalalp.patentsearcher.dao.searchinfo.SearchInfoDao;
import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.entity.SearchInfo;
import com.bilalalp.patentsearcher.service.base.AbstractService;
import com.bilalalp.patentsearcher.service.keywordinfo.KeywordInfoService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Service
public class SearchInfoServiceImpl extends AbstractService<SearchInfo> implements SearchInfoService {

    private SearchInfoDao searchInfoDao;

    @Autowired
    private KeywordInfoService keywordInfoService;

    @Autowired
    public SearchInfoServiceImpl(Dao<SearchInfo> dao) {
        super(dao);
        setSearchInfoDao(searchInfoDao);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SearchInfo persistWithNewTransaction(SearchingDto searchingDto) {

        final List<KeywordInfo> keywordInfoList = new ArrayList<>();
        final List<KeywordInfoDto> keywordInfoDtoList = searchingDto.getKeywordInfoList();

        for (final KeywordInfoDto keywordInfoDto : keywordInfoDtoList) {

            final KeywordInfo keywordInfo = keywordInfoService.findById(keywordInfoDto.getId());
            keywordInfoList.add(keywordInfo);
        }

        final SearchInfo searchInfo = new SearchInfo();
        searchInfo.setKeywordInfoList(keywordInfoList);
        persist(searchInfo);
        flush();

        return searchInfo;
    }
}