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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;

@SuppressWarnings("serial")
@WebServlet("/q4")

/**
 * Handle q4 requests, return hashtags and their relevant tweet ids of a given data,
 * location and popularity range (m, n)
 */
public class Q4 extends HttpServlet {
	
	private String teamId = new String("sudoCloud");
    private String awsId1 = new String("7760-2621-3295");
    private String awsId2 = new String("2657-3003-6308");
    private String awsId3 = new String("2901-7055-0317");
    private HConnection connection = null;
    
    public void init() throws ServletException {
    	this.connection = (HConnection)getServletContext().getAttribute("Connection");
    }
    
    /**
	 * Handle q4 requests, return hashtags and their relevant tweet ids of a given data,
	 * location and popularity range (m, n)
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, 
    					HttpServletResponse response) 
    					throws ServletException, IOException{
    	
        response.setCharacterEncoding("UTF-8"); //encoding
        PrintWriter writer = response.getWriter();
        writer.println(teamId + "," + awsId1 + "," + awsId2 + "," + awsId3);
        
        String date = request.getParameter("date");
        String location = request.getParameter("location");
        int m = Integer.valueOf(request.getParameter("m"));
        int n = Integer.valueOf(request.getParameter("n"));
        String combine = date + location;
        
        HTableInterface table = this.connection.getTable("q4");
        List<Get> getList = new ArrayList<Get>(n-m+1);
        for (int i=m; i<=n; ++i) {
        	String key = combine + String.valueOf(i);
        	Get get = new Get(key.getBytes());
        	getList.add(get);
        }
        Result[] results = table.get(getList);
        for (Result result: results) {
        	if (!result.isEmpty())
        		writer.println(new String(result.raw()[0].getValue(), "UTF-8"));
        }
        table.close();
        
        writer.close();
    }
}