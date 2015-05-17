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

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;

@SuppressWarnings("serial")
@WebServlet("/q5")

/**
 * Handle q5 requests, given two user id and return who is hotter
 */
public class Q5 extends HttpServlet {
	
	private String teamId = new String("sudoCloud");
    private String awsId1 = new String("7760-2621-3295");
    private String awsId2 = new String("2657-3003-6308");
    private String awsId3 = new String("2901-7055-0317");
    private HConnection connection = null;
    
    public void init() throws ServletException {
    	this.connection = (HConnection)getServletContext().getAttribute("Connection");
    }
    
    /**
	 * Handle q5 requests, given two user id and return who is hotter
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, 
    					HttpServletResponse response) 
    					throws ServletException, IOException{
    	
        response.setCharacterEncoding("UTF-8"); //encoding
        PrintWriter writer = response.getWriter();
        writer.println(teamId + "," + awsId1 + "," + awsId2 + "," + awsId3);
        
        String m = request.getParameter("m");
        String n = request.getParameter("n");
        writer.println(m + "\t" + n + "\t"+ "WINNER");
        
        HTableInterface table = this.connection.getTable("q5");
        List<Get> getList = new ArrayList<Get>();
        getList.add(new Get(m.getBytes()));
        getList.add(new Get(n.getBytes()));
        Result[] results = table.get(getList);
        if (results.length >= 2) {//valid m & n
	        KeyValue[] mKvArray = results[0].raw();
	        KeyValue[] nKvArray = results[1].raw();
	        
	        String[] mscore = new String(mKvArray[0].getValue(), "UTF-8").split(",");
	        String[] nscore = new String(nKvArray[0].getValue(), "UTF-8").split(",");
	        int mInt;
	        int nInt;
	        String winner;
	        
	        for (int i=0; i<=3; ++i) {
	        	mInt = Integer.valueOf(mscore[i]);
	        	nInt = Integer.valueOf(nscore[i]);
	        	if (mInt > nInt)
	        		winner = m;
	        	else if (mInt < nInt)
	        		winner = n;
	        	else
	        		winner = "X";
	        	writer.println(mscore[i] + "\t" + nscore[i] + "\t"+ winner);
	        }    
        }
        table.close();
        
        writer.close();
    }
}
