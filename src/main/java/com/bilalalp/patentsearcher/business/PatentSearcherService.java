package com.bilalalp.patentsearcher.business;

import com.bilalalp.patentsearcher.entity.PatentInfo;

import java.io.IOException;
import java.util.List;

public interface PatentSearcherService {

    List<PatentInfo> getPatentInfoList(final String searchUrl) throws IOException;
}
