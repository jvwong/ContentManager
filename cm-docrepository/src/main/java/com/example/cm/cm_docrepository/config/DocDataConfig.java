package com.example.cm.cm_docrepository.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;


import java.io.File;


@Configuration
@EnableNeo4jRepositories(basePackages = "com.example.cm.cm_docrepository.repository")
public class DocDataConfig extends Neo4jConfiguration {

	public DocDataConfig() {
		setBasePackage("com.example.cm.cm_model.domain");
	}

	@Bean(destroyMethod="shutdown")
	public GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory()
				.newEmbeddedDatabase(new File("/tmp/graphdb"));
	}
}

//@Configuration
//@EnableNeo4jRepositories(basePackages = "com.example.cm.cm_docrepository.repository")
//@PropertySource("classpath:environment.properties")
//@EnableTransactionManagement
//public class DocDataConfig extends Neo4jConfiguration {
//
//	// InProcessServer is useful for test and development environments, but is not
//	// recommended for production use. This implementation will start a new instance
//	// of CommunityNeoServer running on an available local port and return the URL
//	// needed to connect to it. It also registers a shutdown hook so that the
//	// underlying Neo4j server exits cleanly when the JVM shuts down,
//	// via Control+C, for example.
//	@Bean
//	public Neo4jServer neo4jServer() {
////		return new RemoteServer("http://localhost:7474");
//		return new InProcessServerBuilder();
//	}
//
//	@Bean
//	public SessionFactory getSessionFactory() {
//		// with domain entity base package(s)
//		return new SessionFactory("com.example.cm.cm_model.domain");
//	}
//
//	// needed for session in view in web-applications
//	@Bean
//	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
//	public Session getSession() throws Exception {
//		return super.getSession();
//	}
//}
