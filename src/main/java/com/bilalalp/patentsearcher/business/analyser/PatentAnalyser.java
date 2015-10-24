package com.bilalalp.patentsearcher.business.analyser;

import com.bilalalp.patentsearcher.dto.ConfigDto;
import com.bilalalp.patentsearcher.dto.SearchInfoDto;
import com.bilalalp.patentsearcher.service.patentinfo.PatentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatentAnalyser {

    @Autowired
    private PatentInfoService patentInfoService;

    @Transactional
    public void analyse(final SearchInfoDto searchInfoDto, final ConfigDto configDto) {



    }
}