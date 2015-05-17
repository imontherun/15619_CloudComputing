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

import sudoCloud.front.data.Q6Data;

@SuppressWarnings("serial")
@WebServlet("/q6")

/**
 * Handle q6 requests, return the number of photos uploaded by users within a range of userids
 */
public class Q6 extends HttpServlet {
	
	private String teamId = new String("sudoCloud");
    private String awsId1 = new String("7760-2621-3295");
    private String awsId2 = new String("2657-3003-6308");
    private String awsId3 = new String("2901-7055-0317");
    private Q6Data[] q6Data = null;
    private int low = 0;
	private int high = 17533756;
    
	public void init() throws ServletException {
    	this.q6Data = (Q6Data[])getServletContext().getAttribute("q6Data");
    }
    
    /**
	 * Handle q6 requests, return the number of photos uploaded by users within a range of userids
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, 
    					HttpServletResponse response) 
    					throws ServletException, IOException{
    	
        response.setCharacterEncoding("UTF-8"); //encoding
        PrintWriter writer = response.getWriter();
        writer.println(teamId + "," + awsId1 + "," + awsId2 + "," + awsId3);
        
        long m = Long.valueOf(request.getParameter("m"));
        long n = Long.valueOf(request.getParameter("n"));
        
        if (m > n)
        	writer.println("0");
        else
        	writer.println(this.floorValue(this.low, this.high, n) 
        			- this.lowerValue(this.low, this.high, m));
        
        writer.close();
    }
    
    //find the greatest userId less than or equal to the given userId, return its sum photo
  	private int floorValue (int low, int high, long target) {
  		int mid = (low + high + 1) / 2; //make mid lean to large side
  		while (low < high)
  		{
  			if (this.q6Data[mid].userId == target)
  				break;
  			else if (this.q6Data[mid].userId < target)
  				low = mid;
  			else
  				high = mid - 1;

  			mid = (low + high + 1) / 2;
  		}

  		return this.q6Data[mid].photo;
  	}

  	//find the greatest userId less than the given userId, return its sum photo
  	private int lowerValue (int low, int high, long target) {
  		int mid = (low + high + 1) / 2; //make mid lean to large side
  		while (low < high)
  		{
  			if (this.q6Data[mid].userId < target)
  				low = mid;
  			else
  				high = mid - 1;

  			mid = (low + high + 1) / 2;
  		}

  		return this.q6Data[mid].photo;
  	}
}
