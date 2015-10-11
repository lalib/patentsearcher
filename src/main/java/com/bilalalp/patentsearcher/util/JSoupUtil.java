package com.bilalalp.patentsearcher.util;

import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.dto.WaitingEnum;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class JSoupUtil {

    public static Element getBody(String patentLink, UIInfoDto uiInfoDto, WaitingEnum waitingEnum) {

        int faultCount = 0;
        while (true) {

            try {
                return Jsoup.connect(patentLink).timeout(PatentSearcherConstant.TIMEOUT).get().body();

            } catch (Exception e) {

                faultCount++;
                sleep(uiInfoDto, waitingEnum);
            }

            if (faultCount == PatentSearcherConstant.BREAK_COUNT) {
                break;
            }
        }

        return null;
    }


    public static void sleep(UIInfoDto uiInfoDto, WaitingEnum waitingEnum) {

        try {
            uiInfoDto.setTotalWaitTime(uiInfoDto.getTotalWaitTime() + waitingEnum.getTime());
            Thread.sleep(waitingEnum.getTime());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
