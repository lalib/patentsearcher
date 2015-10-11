package com.bilalalp.patentsearcher.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = StopWordInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class StopWordInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_STOP_WORD_INFO_ID";
    public static final String TABLE_NAME = "T_STOP_WORD_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_STOP_WORD")
    private String word;
}