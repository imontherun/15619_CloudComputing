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
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/q1")

/**
 * Handle q1 requests, compute the result of prime number division
 */
public class Q1 extends HttpServlet {
	
    private BigInteger pubKey = new BigInteger(
			"6876766832351765396496377534476050002970857483815262918450355869850085167053394672634315391224052153");
    private String teamId = new String("sudoCloud");
    private String awsId1 = new String("7760-2621-3295");
    private String awsId2 = new String("2657-3003-6308");
    private String awsId3 = new String("2901-7055-0317");
    
    /**
	 * Handle q1 requests, compute the result of prime number division
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, 
    					HttpServletResponse response) 
    					throws ServletException, IOException {
    	
        String inputKey = request.getParameter("key");
        PrintWriter writer = response.getWriter(); 
        
        //get the global cache
        ConcurrentHashMap<String, String> q1Map = 
        	(ConcurrentHashMap<String, String>) getServletContext().getAttribute("Q1_MAP");
        
        String result =	q1Map.get(inputKey);
        if (result == null) { //cache miss
	        BigInteger keyY = new BigInteger(inputKey);
			result = keyY.divide(pubKey).toString();
			writer.println(result);
	        q1Map.put(inputKey, result); //cache result
        }   
        
        writer.println(teamId + "," + awsId1 + ","
					+ awsId2 + "," + awsId3);
        
        Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        writer.println(simpleDateFormat.format(date));
        
        writer.close();
    }
}