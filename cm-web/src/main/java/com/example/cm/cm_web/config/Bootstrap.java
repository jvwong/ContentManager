package com.example.cm.cm_web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.ResourceBundle;

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
        dispatcher.setLoadOnStartup(2);
        dispatcher.setMultipartConfig(
                new MultipartConfigElement(
                        "/var/www/tomcat/uploads", // temp location
                        2097152, // max size (bytes) file
                        4194304, // max size (bytes) total request
                        0)); // size threshold after which files will be written to disk
        dispatcher.addMapping("/services/*");
		
        // Activate the profile
        servletContext.setInitParameter("spring.profiles.active", "dev");

    }
}
