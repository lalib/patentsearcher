package com.bilalalp.patentsearcher.business.parser;

import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.dto.WaitingEnum;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.util.JSoupUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class PatentScopeContentParserServiceImpl implements PatentContentParserService {

    private UIInfoDto uiInfoDto;

    private WaitingEnum waitingEnum;


    private List<PatentInfo> getPatentContents(final List<PatentInfo> patentInfos) throws IOException {

        for (final PatentInfo patentInfo : patentInfos) {

            final String patentLink = patentInfo.getPatentLink();

            final Element body = JSoupUtil.getBody(patentLink, uiInfoDto, waitingEnum);

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
}
