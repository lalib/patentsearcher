package com.bilalalp.patentsearcher.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.bilalalp.patentsearcher"})
@PropertySource(value = {"classpath:application.properties"})
public class PatentSearcherConfiguration {

    @Autowired
    private Environment environment;

    @Qualifier(value = "patentSearcherEntityManagerFactory")
    @Bean(name = "patentSearcherEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commonsEntityManagerFactory() throws PropertyVetoException {

        final LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(patentSearcherDataSource());
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.bilalalp.patentsearcher.entity");
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(getJpaPropertyMap());
        return localContainerEntityManagerFactoryBean;
    }

    @Qualifier(value = "jpaTransactionManager")
    @Bean(name = "jpaTransactionManager")
    public JpaTransactionManager jpaTransactionManager() throws PropertyVetoException {

        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(commonsEntityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    @Qualifier(value = "patentSearcherDataSource")
    @Bean(name = "patentSearcherDataSource")
    public DataSource patentSearcherDataSource() throws PropertyVetoException {

        final ComboPooledDataSource basicDataSource = new ComboPooledDataSource();
        basicDataSource.setJdbcUrl(environment.getProperty("patentSearcher.jdbc.url"));
        basicDataSource.setPassword(environment.getProperty("patentSearcher.jdbc.password"));
        basicDataSource.setUser(environment.getProperty("patentSearcher.jdbc.userName"));
        basicDataSource.setDriverClass(environment.getProperty("patentSearcher.jdbc.driverClassName"));
        basicDataSource.setMaxPoolSize(environment.getProperty("patentSearcher.jdbc.maxPoolSize", Integer.class));
        basicDataSource.setInitialPoolSize(environment.getProperty("patentSearcher.jdbc.initialPoolSize", Integer.class));
        basicDataSource.setAcquireIncrement(environment.getProperty("patentSearcher.jdbc.incrementSize", Integer.class));
        basicDataSource.setIdleConnectionTestPeriod(environment.getProperty("patentSearcher.jdbc.connectionTestPeriod", Integer.class));
        basicDataSource.setMaxIdleTime(environment.getProperty("patentSearcher.jdbc.maxIdleTime", Integer.class));
        basicDataSource.setUnreturnedConnectionTimeout(environment.getProperty("patentSearcher.jdbc.unreturnedTimeout", Integer.class));
        basicDataSource.setAutoCommitOnClose(environment.getProperty("patentSearcher.jdbc.autoCommit", Boolean.class));
        basicDataSource.setNumHelperThreads(environment.getProperty("patentSearcher.jdbc.numHelperThreads", Integer.class));
        basicDataSource.setMaxStatements(environment.getProperty("patentSearcher.jdbc.maxStatements", Integer.class));
        basicDataSource.setMaxStatementsPerConnection(environment.getProperty("patentSearcher.jdbc.maxStatementsPerConnection", Integer.class));
        basicDataSource.setDebugUnreturnedConnectionStackTraces(environment.getProperty("patentSearcher.jdbc.debugUnreturnedConnectionStackTraces", Boolean.class));
        basicDataSource.setMinPoolSize(environment.getProperty("patentSearcher.jdbc.minPoolSize", Integer.class));
        return basicDataSource;
    }

    private Map<String, Object> getJpaPropertyMap() {

        final Map<String, Object> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put("hibernate.implicit_naming_strategy", new ImplicitNamingStrategyJpaCompliantImpl());
        jpaPropertyMap.put("hibernate.physical_naming_strategy", new PhysicalNamingStrategyStandardImpl());
        jpaPropertyMap.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto", String.class));
        jpaPropertyMap.put("hibernate.showSql", environment.getProperty("hibernate.showSql", Boolean.class));
        jpaPropertyMap.put("hibernate.formatSql", environment.getProperty("hibernate.formatSql", Boolean.class));
        jpaPropertyMap.put("hibernate.generate_statistics", environment.getProperty("hibernate.generate_statistics", Boolean.class));
        jpaPropertyMap.put("hibernate.max_fetch_depth", environment.getProperty("hibernate.max_fetch_depth", Integer.class));
        jpaPropertyMap.put("hibernate.default_batch_fetch_size", environment.getProperty("hibernate.default_batch_fetch_size", Integer.class));
        jpaPropertyMap.put("hibernate.jdbc.batch_size", environment.getProperty("hibernate.jdbc.batch_size", Integer.class));
        jpaPropertyMap.put("hibernate.cache.region.factory_class", environment.getProperty("hibernate.cache.region.factory_class", String.class));
        jpaPropertyMap.put("hibernate.cache.use_query_cache", environment.getProperty("hibernate.cache.use_query_cache", Boolean.class));
        jpaPropertyMap.put("hibernate.cache.use_second_level_cache", environment.getProperty("hibernate.cache.use_second_level_cache", Boolean.class));
        return jpaPropertyMap;
    }
}
