/*
 * 15619 team project
 * Team: sudoCloud
 * Member1: Huacong Cai - hcai
 * Member2: Qinyu Tong - qtong
 * Member3: Hansi Mou - hmou
 * 
 */

package sudoCloud.front.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;

@SuppressWarnings("serial")
@WebServlet("/q2")

/**
 * Handle q2 requests, return the TweetID:SentimentScore:CensoredText
 * of a given userid in a given time
 */
public class Q2 extends HttpServlet {
	
	private String teamId = new String("sudoCloud");
    private String awsId1 = new String("7760-2621-3295");
    private String awsId2 = new String("2657-3003-6308");
    private String awsId3 = new String("2901-7055-0317");
    private HConnection connection = null;
    
    public void init() throws ServletException {
    	this.connection = (HConnection)getServletContext().getAttribute("Connection");
    }
    
    /**
	 * Handle q2 requests, return the TweetID:SentimentScore:CensoredText
     * of a given userid in a given time
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, 
    					HttpServletResponse response) 
    					throws ServletException, IOException{
    	
    	response.setCharacterEncoding("UTF-8"); //encoding
        PrintWriter writer = response.getWriter();
        writer.println(teamId + "," + awsId1 + "," + awsId2 + "," + awsId3);
        
        String userId = request.getParameter("userid");
        String timeStamp = request.getParameter("tweet_time");
        String key = userId + timeStamp;
        
        HTableInterface table = this.connection.getTable("q2");
        Get get = new Get(key.getBytes());
        get.setMaxVersions(20);
        Result result = table.get(get);
        KeyValue[] kvArray = result.raw();
        for (KeyValue kv : kvArray)
        	writer.println(new String(kv.getValue(), "UTF-8"));
        table.close();

        writer.close();
    }
}