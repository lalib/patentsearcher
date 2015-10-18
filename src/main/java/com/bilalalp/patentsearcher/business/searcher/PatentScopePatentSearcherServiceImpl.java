package com.bilalalp.patentsearcher.business.searcher;

import com.bilalalp.patentsearcher.config.PatentSearcherInitialData;
import com.bilalalp.patentsearcher.constant.PatentSearcherConstant;
import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.dto.WaitingEnum;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.service.patentinfo.PatentInfoService;
import com.bilalalp.patentsearcher.service.siteinfo.SiteInfoService;
import com.bilalalp.patentsearcher.util.JSoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "patentScopePatentSearcherService")
public class PatentScopePatentSearcherServiceImpl implements PatentSearcherService {

    private WaitingEnum waitingEnum = WaitingEnum.ONE_SECOND;

    @Autowired
    private PatentInfoService patentInfoService;

    @Autowired
    private SiteInfoService siteInfoService;

    private SiteInfo siteInfo;

    @PostConstruct
    public void init() {
        siteInfo = siteInfoService.findBySiteKey(PatentSearcherInitialData.getPatentScopeSiteInfo().getSiteKey());
    }

    @Override
    @Transactional
    public List<PatentInfo> getPatentInfoList(SearchingDto searchingDto, UIInfoDto uiInfoDto) throws IOException {

        final SiteInfo siteInfo = this.siteInfo;
        final List<KeywordInfoDto> keywordInfoDtoList = searchingDto.getKeywordInfoList();

        int pageNumber = 0;
        final List<PatentInfo> patentInfoList = new ArrayList<>();
        boolean eof = false;

        int tryCount = 0;

        final String searchUrl = createSearchUrl(keywordInfoDtoList);
        uiInfoDto.setGeneratedLink(searchUrl);

        while (!eof) {

            try {

                pageNumber++;
                final String link = searchUrl + pageNumber;
                final Document document = Jsoup.connect(link).timeout(PatentSearcherConstant.TIMEOUT).get();
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

                            final PatentInfo patentInfo = getPatentInfo(element, aClass);
                            if (patentInfo == null) {
                                continue;
                            }

                            patentInfo.setSiteInfo(siteInfo);
                            patentInfo.setSearchLink(link);
                            patentInfo.setSearchInfo(searchingDto.getSearchInfo());

                            patentInfoService.persistWithNewTransaction(patentInfo);
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

                if (PatentSearcherConstant.BREAK_COUNT.equals(tryCount)) {
                    break;
                }

                JSoupUtil.sleep(uiInfoDto, waitingEnum);
            }
        }

        return patentInfoList;
    }

    @Override
    public String getSiteUrl() {
        return siteInfo.getSiteAddres();
    }

    @Override
    public String createSearchUrl(List<KeywordInfoDto> keywordInfoDtoList) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://patentscope.wipo.int/search/en/result.jsf?query=");

        int index = 0;

        for (final KeywordInfoDto keywordInfoDto : keywordInfoDtoList) {
            index++;
            final String replacedKeyword = keywordInfoDto.getText().replace(" ", "%20");
            stringBuilder
                    .append("FP:(").append(replacedKeyword)
                    .append(")%20OR%20").append("EN_AB:(")
                    .append(replacedKeyword).append(")%20OR%20")
                    .append("EN_CL:(").append(replacedKeyword)
                    .append(")%20OR%20").append("EN_DE:(")
                    .append(replacedKeyword).append(")%20OR%20")
                    .append("PA:(").append(replacedKeyword)
                    .append(")%20OR%20").append("EN_ALLTXT:(")
                    .append(replacedKeyword).append(")%20OR%20")
                    .append("EN_TI:(").append(replacedKeyword)
                    .append(")");

            if (keywordInfoDtoList.size() == index) {
                stringBuilder.append("%20&");
            }
        }

        return stringBuilder
                .append("sortOption=Relevance&viewOption=Simple&currentNavigationRow=")
                .toString();
    }

    private PatentInfo getPatentInfo(Element element, Elements aClass) {
        final PatentInfo patentInfo = new PatentInfo();

        if (aClass != null && !aClass.isEmpty()) {
            final String text = aClass.get(0).text();
            patentInfo.setPatentTitle(text);
        } else {
            return null;
        }

        final Elements aElements = element.getElementsByTag("a");

        if (aElements != null && !aElements.isEmpty()) {

            final String href = aElements.get(0).attr("href");
            patentInfo.setPatentLink(siteInfo.getSiteAddres() + href);
        } else {
            return null;
        }

        return patentInfo;
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