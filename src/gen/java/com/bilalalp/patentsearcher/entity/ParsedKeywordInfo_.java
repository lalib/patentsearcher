package com.bilalalp.patentsearcher.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ParsedKeywordInfo.class)
public abstract class ParsedKeywordInfo_ extends com.bilalalp.patentsearcher.entity.AbstractEntity_ {

	public static volatile SingularAttribute<ParsedKeywordInfo, PatentInfo> patentInfo;
	public static volatile SingularAttribute<ParsedKeywordInfo, Long> id;
	public static volatile SingularAttribute<ParsedKeywordInfo, String> keyword;
	public static volatile SingularAttribute<ParsedKeywordInfo, ContentType> contentType;

}

