package com.bilalalp.patentsearcher.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = SiteInfo.TABLE_NAME)
@Access(AccessType.FIELD)
@Getter
@Setter
public class SiteInfo extends AbstractEntity {

    public static final String JOIN_COLUMN = "C_SITE_INFO_ID";
    public static final String TABLE_NAME = "T_SITE_INFO";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "C_SITE_ADDRESS")
    private String siteAddres;

    @Column(name = "C_SITE_NAME")
    private String siteName;

    @Column(name = "C_SITE_KEY")
    private String siteKey;
}