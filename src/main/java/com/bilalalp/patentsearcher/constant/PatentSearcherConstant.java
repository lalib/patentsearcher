package com.bilalalp.patentsearcher.constant;

public class PatentSearcherConstant {

    //    public static final String PATENT_SCOPE_URL = "https://patentscope.wipo.int/search/en/result.jsf?query=cloud%20computing&sortOption=Pub%20Date%20Desc&viewOption=Simple&currentNavigationRow=";
    public static final String PATENT_SCOPE_URL = "https://patentscope.wipo.int/search/en/result.jsf?query=nanotechnology&office=&sortOption=Pub%20Date%20Desc&viewOption=Simple&currentNavigationRow=";

    public static final String USPTO_URL = "http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&TERM1=cloud+computing&FIELD1=&co1=OR&TERM2=&FIELD2=&d=PTXT&p=";

    public static final Integer TIMEOUT = 150000000;

    public static final Integer SLEEP_LENGTH = 60000;

    public static final Integer BREAK_COUNT = 100;

    private PatentSearcherConstant() {

    }
}