package com.bilalalp.patentsearcher.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PatentInfo.class)
public abstract class PatentInfo_ extends com.bilalalp.patentsearcher.entity.AbstractEntity_ {

	public static volatile SingularAttribute<PatentInfo, String> searchLink;
	public static volatile SingularAttribute<PatentInfo, SearchInfo> searchInfo;
	public static volatile SingularAttribute<PatentInfo, String> patentNumber;
	public static volatile SingularAttribute<PatentInfo, String> abstractContent;
	public static volatile SingularAttribute<PatentInfo, Boolean> parsed;
	public static volatile SingularAttribute<PatentInfo, Long> id;
	public static volatile SingularAttribute<PatentInfo, String> patentTitle;
	public static volatile SingularAttribute<PatentInfo, String> patentLink;
	public static volatile SingularAttribute<PatentInfo, SiteInfo> siteInfo;

}

