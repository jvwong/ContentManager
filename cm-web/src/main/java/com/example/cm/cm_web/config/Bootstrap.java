package com.example.cm.cm_web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class Bootstrap implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException
    {
    	/*
    	 * Root application context configuration
    	 */
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        /*
    	 * Web UI application context configuration
    	 */
        AnnotationConfigWebApplicationContext webContext =
                new AnnotationConfigWebApplicationContext();
        webContext.register(WebServletContextConfiguration.class);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "cmWebDispatcher", new DispatcherServlet(webContext)
        );
        // Needs to be set for @EnableAsync and @Async
        dispatcher.setAsyncSupported(true);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        /*
        * RESTful web service application context configuration
        */
        AnnotationConfigWebApplicationContext restContext
                = new AnnotationConfigWebApplicationContext();
        restContext.register(RestServletContextConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(restContext);
        // Recognize OPTIONS requests
        servlet.setDispatchOptionsRequest(true);
        dispatcher = servletContext.addServlet(
             "cmRestDispatcher", servlet
        );
        // Needs to be set for @EnableAsync and @Async
        dispatcher.setAsyncSupported(true);
        dispatcher.setLoadOnStartup(2);
        dispatcher.setMultipartConfig(
                new MultipartConfigElement(
                        "/var/www/tomcat/temp", // temp location only
                        2097152, // max size (bytes) file
                        4194304, // max size (bytes) total request
                        0)); // size threshold after which files will be written to disk
        dispatcher.addMapping("/services/*");
		
        // Activate the profile
        servletContext.setInitParameter("spring.profiles.active", "dev");

    }
}
