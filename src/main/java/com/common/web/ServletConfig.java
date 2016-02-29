package com.common.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletConfig implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
    	System.out.println("ServletConfig init");
    	// Do stuff during webapp's startup.
    }

    public void contextDestroyed(ServletContextEvent event) {
        // Do stuff during webapp's shutdown.
    	System.out.println("ServletConfig deploying");

    }

}

