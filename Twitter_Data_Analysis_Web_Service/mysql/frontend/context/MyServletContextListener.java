/*
 * 15619 team project
 * Team: sudoCloud
 * Member1: Huacong Cai - hcai
 * Member2: Qinyu Tong - qtong
 * Member3: Hansi Mou - hmou
 * 
 */

package sudoCloud.front.context;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.*;

/**
 * Context listener for the current application
 * Used for set up application context (global variables)
 * @author chc
 *
 */
public class MyServletContextListener implements ServletContextListener {
	private ServletContext context = null; 
	private ConcurrentHashMap<String, String> q1Map = null;  
	
	/**
	 * Used for setting global variables, which are scoped to the current application
	 * Will be called when the application is deployed
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		this.context = event.getServletContext();
		this.q1Map = new ConcurrentHashMap<String, String>();
		
		this.context.setAttribute("Q1_MAP", q1Map);
	}
	
	/**
	 * Release global variables created at initialization
	 * Will be called when the application is undeployed
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		//release cache
		this.context = null;
		this.q1Map = null;
	}
}