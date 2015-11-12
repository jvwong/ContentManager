package com.example.cm.cm_jcrrepository.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.cm.cm_jcrrepository.repository")
@PropertySource("classpath:environment.properties")
public class JCRDataConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "ArticlesDB";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}
}
