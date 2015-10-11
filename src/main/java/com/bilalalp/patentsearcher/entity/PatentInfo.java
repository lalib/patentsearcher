package com.bilalalp.patentsearcher.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "C_PATENT_TITLE")
    private String patentTitle;

    @Column(name = "C_PATENT_LINK")
    private String patentLink;

    @Column(name = "C_ABSTRACT_CONTENT")
    private String abstractContent;

    @ManyToOne(targetEntity = SiteInfo.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = SiteInfo.JOIN_COLUMN)
    private SiteInfo siteInfo;

    @ManyToOne(targetEntity = SearchInfo.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = SearchInfo.JOIN_COLUMN)
    private SearchInfo searchInfo;

}