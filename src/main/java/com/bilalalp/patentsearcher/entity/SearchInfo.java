package com.bilalalp.patentsearcher.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = SearchInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class SearchInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_SEARCH_INFO_ID";
    public static final String TABLE_NAME = "T_SEARCH_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(targetEntity = KeywordInfo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "searchInfo")
    private List<KeywordInfo> keywordInfoList;

    @Column(name = "C_SEARCH_STATUS")
    @Enumerated(EnumType.STRING)
    private SearchInfoStatusType searchInfoStatusType = SearchInfoStatusType.NOT_FINISHED;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "C_START_TIME")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "C_END_TIME")
    private Date endTime;
}