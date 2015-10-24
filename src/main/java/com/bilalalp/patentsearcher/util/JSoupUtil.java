package com.bilalalp.patentsearcher.util;

import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.CrawlingDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.dto.WaitingEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JSoupUtil {

    public static Element getBody(String patentLink, WaitingEnum waitingEnum, CrawlingDto crawlingDto) {

        int faultCount = 0;
        while (true) {

            try {
                final Document document = Jsoup.connect(patentLink).timeout(PatentSearcherConstant.TIMEOUT).get();
                return document.body();

            } catch (Exception e) {

                faultCount++;
                crawlingDto.setErrorCount(faultCount);
                sleep(crawlingDto, waitingEnum);
            }

            if (faultCount == PatentSearcherConstant.BREAK_COUNT) {
                break;
            }
        }

        return null;
    }

    public static void sleep(WaitingEnum waitingEnum) {

        try {
            Thread.sleep(waitingEnum.getTime());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(UIInfoDto uiInfoDto, WaitingEnum waitingEnum) {

        try {
            uiInfoDto.setTotalWaitTime(uiInfoDto.getTotalWaitTime() + waitingEnum.getTime());
            Thread.sleep(waitingEnum.getTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(CrawlingDto uiInfoDto, WaitingEnum waitingEnum) {

        try {
            uiInfoDto.setTotalWaitTime(uiInfoDto.getTotalWaitTime() + waitingEnum.getTime());
            Thread.sleep(waitingEnum.getTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}