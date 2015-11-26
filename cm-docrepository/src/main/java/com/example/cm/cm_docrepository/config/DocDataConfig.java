package com.example.cm.cm_docrepository.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.cm.cm_docrepository.repository")
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
@PropertySource("classpath:environment.properties")
public class DocDataConfig extends AbstractMongoConfiguration {

	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}


	@Override
	protected String getDatabaseName() {
		return "ArticlesDB";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}

//	@Bean
//	public AuditorAware<String> myAuditorProvider() {
//		return new SpringSecurityAuditorAware();
//	}
}
