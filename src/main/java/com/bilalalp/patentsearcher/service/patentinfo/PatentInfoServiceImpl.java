package com.bilalalp.patentsearcher.service.patentinfo;

import com.bilalalp.patentsearcher.dao.patentinfo.PatentInfoDao;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.service.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatentInfoServiceImpl extends AbstractService<PatentInfo> implements PatentInfoService {

    private PatentInfoDao patentInfoDao;

    @Autowired
    public PatentInfoServiceImpl(PatentInfoDao patentInfoDao) {
        super(patentInfoDao);
        setDao(patentInfoDao);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistWithNewTransaction(PatentInfo patentInfo) {
        persist(patentInfo);
        flush();
    }
}