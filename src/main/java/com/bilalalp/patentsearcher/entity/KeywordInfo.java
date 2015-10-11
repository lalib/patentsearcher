package com.bilalalp.patentsearcher.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = KeywordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class KeywordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_KEYWORD_INFO_ID";
    public static final String TABLE_NAME = "T_KEYWORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_KEYWORD")
    private String keyword;
}