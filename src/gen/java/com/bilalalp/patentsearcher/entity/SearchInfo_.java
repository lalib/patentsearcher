package com.bilalalp.patentsearcher.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SearchInfo.class)
public abstract class SearchInfo_ extends com.bilalalp.patentsearcher.entity.AbstractEntity_ {

	public static volatile SingularAttribute<SearchInfo, SearchInfoStatusType> searchInfoStatusType;
	public static volatile SingularAttribute<SearchInfo, Date> startTime;
	public static volatile SingularAttribute<SearchInfo, Long> id;
	public static volatile SingularAttribute<SearchInfo, Date> endTime;

}

