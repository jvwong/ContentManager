package com.example.cm.cm_jcrrepository.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springmodules.jcr.JcrSessionFactory;
import org.springmodules.jcr.jackrabbit.LocalTransactionManager;
import org.springmodules.jcr.jackrabbit.RepositoryFactoryBean;

import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

@Configuration
@EnableTransactionManagement
@ComponentScan(
		basePackages="com.example.cm.cm_jcrrepository.jcr"
)
@PropertySource("classpath:environment.properties")
public class JCRDataConfig {

	@Autowired
	Environment env;

	// Transaction support
	// One of the strengths of the JCR module is the ability to use
	// the Spring transaction infrastructure (both declaratively and
	// programmatically) with Java Content Repository. JSR 170 treats
	// transactional support as an optional feature and does not impose
	// a standard way of exposing the transactional hooks so each implementation
	// can chose a different method. At the moment of writing, only
	// Jackrabbit is known to support transactions (in most of its
	// operations) and it does so by exposing an javax.transaction.XAResource
	// for each JcrSession. JCR module offers a LocalTransactionManager which
	// can be used for local transactions:
	@Bean
	LocalTransactionManager transactionManager(){
		return new LocalTransactionManager(sessionFactory());
	}

	// SessionFactory which unifies the Repository, Credentials and
	// Workspace interfaces and allows automatic registration of
	// listeners and custom namespaces.
	//
	// SessionFactory hides the authentication details inside the
	// implementation so that once configured, sessions with the same
	// credentials can be easily retrieved. To take advantage of the
	// implementation features (not covered by the spec), the interface
	// allows the retrieval of SessionHolder, a JCR module specific
	// class which is used for transaction and session management
	// with a default, generic implementation that works for every
	// JCR implementation but does not support optional features or
	// customized ones (such as JackrabbitSessionHolder which supports
	// Jackrabbit'stransaction infrastructure). JCR Module provides
	// an easy and transparent way of discovering SessionHolder
	// implementations (which I will discuss in more detail later on)
	// making it easy to plug in support for other JSR-170 compliant
	// libraries.
	// The default implementation of SessionFactory is
	// JcrSessionFactory which requires a repository to work against,
	// and the credentials.
	@Bean
	JcrSessionFactory sessionFactory(){
		JcrSessionFactory sessionFactory = new JcrSessionFactory();
		sessionFactory.setCredentials(credentials());
		try{
			Repository rep = (Repository) repository().getObject();
			sessionFactory.setRepository((rep));
		} catch (Exception e){}
		return sessionFactory;
	}

	@Bean
	public SimpleCredentials credentials(){
		String username = env.getProperty("username");
		String password = env.getProperty("password");
		SimpleCredentials credentials
				= new SimpleCredentials(username, password.toCharArray());
		return credentials;
	}

	// RepositoryFactoryBean configures, starts and stops the
	// repository instances. As the JSR-170 doesn't address the way
	// the repository should be configured, implementations vary in
	// this regard. The support contains predefined FactoryBeans for
	// Jackrabbit and Jeceira and an abstract base class which can
	// easily support other repositories.
	@Bean
	RepositoryFactoryBean repository(){
		RepositoryFactoryBean repositoryFactory
				= new RepositoryFactoryBean();
		repositoryFactory
				.setHomeDir(new FileSystemResource(env.getProperty("repository.dir")));
		repositoryFactory
				.setConfiguration(new ClassPathResource(env.getProperty("repository.conf")));
		return repositoryFactory;
	}

}
