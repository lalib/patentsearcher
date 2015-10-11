package com.bilalalp.patentsearcher;

import com.bilalalp.patentsearcher.business.PatentScopePatentSearcherServiceImpl;
import com.bilalalp.patentsearcher.business.PatentSearcherService;
import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;

import java.io.IOException;
import java.util.List;

public class StartUp {

    public static void main(String[] args) throws IOException {

//        final PatentSearcherService usptoPatentSearcherServiceImpl = new USPTOPatentSearcherServiceImpl();
//        final List<PatentInfo> patentInfoList = usptoPatentSearcherServiceImpl.getPatentInfoList(PatentSearcherConstant.USPTO_URL);
//        System.out.println(patentInfoList);

        final long startTime = System.currentTimeMillis();
        final PatentSearcherService patentScopePatentSearcherService = new PatentScopePatentSearcherServiceImpl();
        final List<PatentInfo> patentInfoList = patentScopePatentSearcherService.getPatentInfoList(PatentSearcherConstant.PATENT_SCOPE_URL, new UIInfoDto());
        final long endTime = System.currentTimeMillis();

        System.out.println("Total Time : " + (endTime - startTime) / 1000);
        System.out.println(patentInfoList.size());
    }
}