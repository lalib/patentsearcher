package com.bilalalp.patentsearcher.business;

import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.dto.WaitingEnum;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatentScopePatentSearcherServiceImpl implements PatentSearcherService {

    private WaitingEnum waitingEnum = WaitingEnum.ONE_SECOND;

    private UIInfoDto uiInfoDto;

    private static final String MAIN_URL = "https://patentscope.wipo.int/search/en/";

    @Override
    public List<PatentInfo> getPatentInfoList(String searchUrl, UIInfoDto uiInfoDto) throws IOException {

        return getPatentLinks(searchUrl, uiInfoDto);
//        return getPatentContents(patentLinks);
    }

    private List<PatentInfo> getPatentContents(final List<PatentInfo> patentInfos) throws IOException {

        for (final PatentInfo patentInfo : patentInfos) {

            final String patentLink = patentInfo.getPatentLink();

            final Element body = getBody(patentLink);

            if (body != null) {
                final String abstractContent = getAbstractContent(body);

                patentInfo.setAbstractContent(abstractContent);

            }
        }

        return patentInfos;
    }

    private String getAbstractContent(final Element body) {

        final Elements aClass = getaClass(body);

        if (aClass != null && aClass.size() != 0) {

            final Element spanElement = getSpanElement(aClass);
            if (spanElement != null) {
                return spanElement.text();
            }
        }

        return null;
    }

    private Elements getaClass(Element body) {
        final Elements aClass = body.getElementsByAttributeValue("class", "trans-section NPabstract");

        if (aClass != null && !aClass.isEmpty()) {
            return aClass;
        }

        final Elements aClass1 = body.getElementsByAttributeValue("class", "PCTabstract trans-section");

        if (aClass1 != null && !aClass1.isEmpty()) {
            return aClass1;
        }

        return aClass;
    }

    private Element getSpanElement(Elements spanElements) {

        for (Element element : spanElements) {
            if (element.attr("lang", "en") != null) {
                return element;
            }
        }
        return null;
    }

    private Element getBody(String patentLink) {

        int faultCount = 0;
        while (true) {

            try {
                return Jsoup.connect(patentLink).timeout(PatentSearcherConstant.TIMEOUT).get().body();

            } catch (Exception e) {

                faultCount++;
                sleep();
            }

            if (faultCount == PatentSearcherConstant.BREAK_COUNT) {
                break;
            }
        }

        return null;
    }


    private List<PatentInfo> getPatentLinks(String searchUrl, UIInfoDto uiInfoDto) {
        int pageNumber = 0;
        final List<PatentInfo> patentInfoList = new ArrayList<>();
        this.uiInfoDto = uiInfoDto;

        boolean eof = false;

        int tryCount = 0;

        while (!eof) {

            try {

                pageNumber++;
                final Document document = Jsoup.connect(searchUrl + pageNumber).timeout(PatentSearcherConstant.TIMEOUT).get();
                final Element body = document.body();

                final Integer pageCount = getPageCount(body);


                uiInfoDto.setTotalRecordCount(pageCount);

                if (pageCount <= pageNumber * 10) {
                    eof = true;
                }

                final Elements tables = body.getElementsByTag("table");

                for (final Element table : tables) {

                    final Element resultTable = table.getElementById("resultTable");

                    if (resultTable == null) {
                        continue;
                    }

                    final Elements trElements = table.getElementsByTag("tr");


                    for (final Element element : trElements) {

                        if (doesRowContainsUrl(element)) {
                            final Elements aClass = element.getElementsByAttributeValue("class", "trans-section");

                            final PatentInfo patentInfo = new PatentInfo();

                            if (aClass != null && !aClass.isEmpty()) {
                                final String text = aClass.get(0).text();
                                patentInfo.setPatentTitle(text);
                            } else {
                                continue;
                            }

                            final Elements aElements = element.getElementsByTag("a");

                            if (aElements != null && !aElements.isEmpty()) {

                                final String href = aElements.get(0).attr("href");
                                patentInfo.setPatentLink(MAIN_URL + href);
                            }

                            patentInfoList.add(patentInfo);
                        }
                    }
                }

                uiInfoDto.setCurrentRecordCount(patentInfoList.size());
                tryCount = 0;

            } catch (final Exception ex) {

                tryCount++;
                pageNumber--;
                uiInfoDto.setErrorCount(uiInfoDto.getErrorCount() + 1);

                if (tryCount == PatentSearcherConstant.BREAK_COUNT) {
                    break;
                }

                sleep();
            }
        }

        return patentInfoList;
    }

    private void sleep() {
        try {
//            uiInfoDto.setTotalWaitTime(uiInfoDto.getTotalWaitTime() + waitingEnum.getTime());
//            Thread.sleep(waitingEnum.getNext().getTime());
//            waitingEnum = waitingEnum.getNext();

            uiInfoDto.setTotalWaitTime(uiInfoDto.getTotalWaitTime() + waitingEnum.getTime());
            Thread.sleep(waitingEnum.getTime());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean doesRowContainsUrl(Element element) {

        final Elements td = element.getElementsByTag("a");
        return td != null && !td.isEmpty();
    }

    private Integer getPageCount(Element element) {

        final Elements elementsByAttributeValue = element.getElementsByAttributeValue("class", "topResultFormCol1");
        if (elementsByAttributeValue != null) {
            return Integer.valueOf(elementsByAttributeValue.get(0).getElementsByTag("b").get(1).text().replace(",", ""));
        }
        return 0;
    }
}