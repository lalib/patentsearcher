package com.bilalalp.patentsearcher.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ParsedKeywordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class ParsedKeywordInfo extends AbstractEntity {

    public static final String TABLE_NAME = "T_PARSED_KEYWORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "C_KEYWORD")
    private String keyword;

    @Column(name = "C_CONTENT_TYPE")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @ManyToOne(targetEntity = PatentInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = PatentInfo.JOIN_COLUMN)
    private PatentInfo patentInfo;

    public ParsedKeywordInfo(String kw, ContentType type, PatentInfo patentInfo) {
        setKeyword(kw);
        setContentType(type);
        setPatentInfo(patentInfo);
    }
}
