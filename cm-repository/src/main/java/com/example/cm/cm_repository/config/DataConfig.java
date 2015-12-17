package com.example.cm.cm_repository.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages="com.example.cm.cm_repository.repository",
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@Import({ AMPQConfig.class })
@ImportResource({ "classpath:awscloud-config.xml" })
@PropertySource({ "classpath:application.properties" })
public class DataConfig {

	@Value( "${mysql.username}" )
	private String mysqlUsername = "cmsUser";
//	private String mysqlUsername;

	@Value( "${mysql.password}" )
	private String mysqlPassword = "cmsPassword";
//	private String mysqlPassword;

	private static final Logger logger
			= LoggerFactory.getLogger(DataConfig.class);

//	@Bean
//	@Profile("dev")
//	public DataSource dataSource()
//	{
//		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
//		return lookup.getDataSource("jdbc/cms");
//	}

	@Bean
	@Profile("dev")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/cms");
		dataSource.setUsername(mysqlUsername);
		dataSource.setPassword(mysqlPassword);
		return dataSource;
	}
	
//	@Bean
//	@Profile("dev")
//	public DataSource embeddedDataSource() {
//		return new EmbeddedDatabaseBuilder()
//	            .setType(EmbeddedDatabaseType.H2)
//	            .addScript("schema_H2.sql")
//	            .addScript("data_H2.sql")
//	            .build();
//	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
		LocalContainerEntityManagerFactoryBean factoryBean =
				new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		factoryBean.setDataSource(dataSource());
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setPackagesToScan("com.example.cm.cm_model.domain");
		return factoryBean;
	}
	
	@Bean 
	public JpaVendorAdapter jpaVendorAdapter(){
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//		adapter.setDatabase(Database.H2);

		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(false);
		adapter.setGenerateDdl(true);

//		adapter.setDatabasePlatform("org.hiberate.dialect.MySQLInnoDBDialect");
		return adapter;
	}
	
	@Bean 
	public PersistenceAnnotationBeanPostProcessor paPostProcessor(){
		return new PersistenceAnnotationBeanPostProcessor();
	}
	
	@Bean
	public PersistenceExceptionTranslator persistenceExceptionTranslator(){
			return new HibernateExceptionTranslator();
	}
	
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

	// application.properties bean
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}

