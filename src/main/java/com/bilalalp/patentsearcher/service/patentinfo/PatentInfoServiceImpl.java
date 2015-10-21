package com.bilalalp.patentsearcher.service.patentinfo;

import com.bilalalp.patentsearcher.dao.patentinfo.PatentInfoDao;
import com.bilalalp.patentsearcher.dto.ContentSearchDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
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
public class PatentInfoServiceImpl extends AbstractService<PatentInfo> implements PatentInfoService {

    private PatentInfoDao patentInfoDao;

    @Autowired
    public PatentInfoServiceImpl(PatentInfoDao patentInfoDao) {
        super(patentInfoDao);
        setPatentInfoDao(patentInfoDao);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistWithNewTransaction(PatentInfo patentInfo) {
        persist(patentInfo);
        flush();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ContentSearchDto getContentSearchDto(final Long searchInfoId) {

        final Long analysiedLinkCount = patentInfoDao.getAnalysiedLinkCount(searchInfoId);
        final Long notAnalysiedLinkCount = patentInfoDao.getNotAnalysiedLinkCount(searchInfoId);
        final Long totalLinkCount = patentInfoDao.getTotalLinkCount(searchInfoId);

        final ContentSearchDto contentSearchDto = new ContentSearchDto();
        contentSearchDto.setAnalysiedLinkCount(analysiedLinkCount);
        contentSearchDto.setNotAnalysiedLinkCount(notAnalysiedLinkCount);
        contentSearchDto.setTotalLinkCount(totalLinkCount);

        return contentSearchDto;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<PatentInfo> getPatentInfoListBySearchInfoId(Long searchInfoId) {
        return patentInfoDao.getPatentInfoListBySearchInfoId(searchInfoId);
    }
}