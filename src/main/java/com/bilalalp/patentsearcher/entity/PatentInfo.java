package com.bilalalp.patentsearcher.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = PatentInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class PatentInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_PATENT_INFO_ID";
    public static final String TABLE_NAME = "T_PATENT_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_PATENT_NUMBER")
    private String patentNumber;

    @Lob
    @Column(name = "C_PATENT_TITLE")
    private String patentTitle;

    @Lob
    @Column(name = "C_PATENT_LINK")
    private String patentLink;

    @Lob
    @Column(name = "C_ABSTRACT_CONTENT")
    private String abstractContent;

    @Column(name = "C_PARSED")
    private Boolean parsed = Boolean.FALSE;

    @Lob
    @Column(name = "C_SEARCH_LINK")
    private String searchLink;

    @Column(name = "C_CONTENT_STATUS_TYPE")
    @Enumerated(EnumType.STRING)
    private ContentSearchInfoStatusType contentSearchInfoStatusType = ContentSearchInfoStatusType.NOT_ANALYSIED;

    @ManyToOne(targetEntity = SiteInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = SiteInfo.JOIN_COLUMN)
    private SiteInfo siteInfo;

    @ManyToOne(targetEntity = SearchInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = SearchInfo.JOIN_COLUMN)
    private SearchInfo searchInfo;

    @ElementCollection
    private List<String> abstractContentWordList;

    @ElementCollection
    private List<String> claimContentWordList;

}