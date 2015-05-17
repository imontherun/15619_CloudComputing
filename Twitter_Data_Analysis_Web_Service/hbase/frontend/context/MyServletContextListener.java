/*
 * 15619 team project
 * Team: sudoCloud
 * Member1: Huacong Cai - hcai
 * Member2: Qinyu Tong - qtong
 * Member3: Hansi Mou - hmou
 * 
 */

package sudoCloud.front.context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;

import javax.servlet.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.util.StringUtils;

import sudoCloud.front.data.Q6Data;

/**
 * Context listener for the current application
 * Used for set up application context (global variables)
 * @author chc
 *
 */
public class MyServletContextListener implements ServletContextListener {
	private ServletContext context = null;
	private Configuration config = null;
	private HConnection connection = null;
	private String MASTER_DNS = "ec2-54-173-191-43.compute-1.amazonaws.com";
	private String q6Path = "/home/hadoop/q6.txt";
	private Q6Data[] q6Data = null;
	
	/**
	 * Used for setting global variables, which are scoped to the current application
	 * Will be called when the application is deployed
	 */
	@Override
	public void contextInitialized(ServletContextEvent event){
		this.context = event.getServletContext();
		config = HBaseConfiguration.create();
		config.set("hbase.rootdir", "hdfs://" + MASTER_DNS + ":20001/hbase");
		config.set("hbase.master", MASTER_DNS + ":60000");
		config.set("hbase.zookeeper.quorum", MASTER_DNS);

		//load q6 data
		this.q6Data = new Q6Data[17533757];
		try {
			BufferedReader fis = new BufferedReader(new FileReader(this.q6Path));
			String data = null;
			int i = 0;
			while ((data = fis.readLine()) != null) {
				String[] dataSplit = data.split(" ");
				this.q6Data[i] = new Q6Data(Long.valueOf(dataSplit[0]), Integer.valueOf(dataSplit[1]));
				++i;
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.context.setAttribute("q6Data", this.q6Data);

		//ExecutorService pool = new ThreadPoolExecutor(200, Integer.MAX_VALUE,
		//		300, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		try {
			this.connection = HConnectionManager.createConnection(config);
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
		this.context.setAttribute("Connection", this.connection);

		//warm up
		HTableInterface table = null;
		try {
			table = this.connection.getTable("q2");
			table.close();
			table = this.connection.getTable("q3");
			table.close();
			table = this.connection.getTable("q4");
			table.close();
			table = this.connection.getTable("q5");
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Release global variables created at initialization
	 * Will be called when the application is undeployed
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		//releases
		this.context = null;
		this.config = null;
		this.q6Data = null;
		
		try {
			this.connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}