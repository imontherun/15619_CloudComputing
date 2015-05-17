/*
 * 15-619 Cloud Computing
 * Project2.1 MSB Recruitment
 * File: MSBRecruitment.java
 * Name: Huacong Cai
 * AndrewID: hcai
 * Date: Sep, 20, 2014 
 */

/*
 * This is a EC2 auto scale program, launches a m3.medium load generator and 
 * a m3.medium data center instance. Then read the log from load generator to
 * see if the total requests handled are more than 3600, if not, launch a new 
 * data center instance to handle request.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;

public class MSBRecruitment{
	
	private enum InstanceType {loadGenerator, dataCenterInstance};
	
	static AmazonEC2Client ec2;
	static Instance loadGeneratorIns;
	static String loadGeneratorDNS;
	static ArrayList<Instance> dataCenterIns = new ArrayList<Instance> ();
	
	static final String andrewID = new String("hcai");
	static final String testID = new String("test");
	static final String http = new String("http://");
	static final String username = new String("/username?username="+andrewID);
	static final String iWantMore = new String("/part/one/i/want/more?testId="+testID+"&dns=");
	static final String viewLog = new String("/view-logs?name=result_" + andrewID + "_" + testID + ".txt");
	
	public static void main(String[] args) throws Exception {
		//Load the Properties File with AWS Credentials
		Properties properties = new Properties();
		properties.load(MSBRecruitment.class.getResourceAsStream("/AwsCredentials.properties"));
		 
		BasicAWSCredentials bawsc = new BasicAWSCredentials(properties.getProperty("accessKey"), properties.getProperty("secretKey"));
		 
		//Create an Amazon EC2 Client
		ec2 = new AmazonEC2Client(bawsc);
		 
		//Launch load generator, activate it
		loadGeneratorIns = launchInstance(InstanceType.loadGenerator);
		loadGeneratorDNS = loadGeneratorIns.getPublicDnsName();
		activate(loadGeneratorDNS);

		//Launch first data center instance, activate it and submit its DNS
		dataCenterIns.add(launchInstance(InstanceType.dataCenterInstance));
		String dcInstanceDNS = dataCenterIns.get(dataCenterIns.size()-1).getPublicDnsName();
		activate(dcInstanceDNS);
		submitDCInstance(dcInstanceDNS);
		
		//Parse log and launch more instance until total request handled > 3600
		TimeUnit.SECONDS.sleep(60); //Wait the first log
		double requestHandled = 0;
		while (requestHandled < 3600.00) {
			//get the last block of the log
			ArrayList<String> lastLog = getLastLog();
			
			//parse log
			requestHandled = 0;
			for (int i=0; i<lastLog.size(); ++i) {
				String[] slice = lastLog.get(i).split("Requests per second:");
				requestHandled += Double.parseDouble(slice[1].split("\\[")[0]);
			}
			
			//If requestHandled is not enough and new instance is worked
			//Launch new data center instance, activate it and submit its DNS
			if (requestHandled < 3600.00 && lastLog.size() == dataCenterIns.size()) {
				dataCenterIns.add(launchInstance(InstanceType.dataCenterInstance));
				dcInstanceDNS = dataCenterIns.get(dataCenterIns.size()-1).getPublicDnsName();
				activate(dcInstanceDNS);
				submitDCInstance(dcInstanceDNS);
			}
			
			TimeUnit.SECONDS.sleep(30);
		}
		
		System.out.println("Total data center instances: " + dataCenterIns.size());
		System.out.println(getResult());
		
	}
	
	/*
	 * Launch a new instance of the given type
	 * @param 	type, the type of instance that will be launch
	 */
	private static Instance launchInstance(InstanceType type) throws InterruptedException {
		//Create Instance Request
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
		
		//Set availability zone
		Placement place = new Placement();
        place.setAvailabilityZone("us-east-1c");
        
        runInstancesRequest.setPlacement(place);
		
		//Configure Instance Request
		switch(type){
		
		case loadGenerator:
			runInstancesRequest.withImageId("ami-1810b270");
			break;
			
		case dataCenterInstance:
			runInstancesRequest.withImageId("ami-324ae85a");
			break;
		}
		
		runInstancesRequest.withInstanceType("m3.medium")
		.withMinCount(1)
		.withMaxCount(1)
		.withKeyName("CHC_key")
		.withSecurityGroups("HTTP");
		 
		//Launch Instance
		RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
		
		//Return the Object Reference of the Instance just Launched
		Instance instance = runInstancesResult.getReservation().getInstances().get(0);
		
		//Tag instance
		String instanceID = instance.getInstanceId();
		CreateTagsRequest createTagsRequest = new CreateTagsRequest();
		createTagsRequest.withResources(instanceID)
						 .withTags(new Tag("Project", "2.1"));	
		ec2.createTags(createTagsRequest);
		
		DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
		describeInstancesRequest.withInstanceIds(instanceID);
		//Wait until running
		do {			
			DescribeInstancesResult describeInstanceResult = ec2.describeInstances(describeInstancesRequest);
			instance = describeInstanceResult.getReservations().get(0).getInstances().get(0);
			InstanceState instanceState = instance.getState();

			if (instanceState.getName().equals("pending"))
				TimeUnit.SECONDS.sleep(20); //Sleep 20 second
			else
				break;
		}
		while(true);
		
		//Sleep until the instance completely boot up
		TimeUnit.SECONDS.sleep(60);
		
		return instance;
	}
	
	/*
	 * Submit HTTP Form
	 * @param 	url, the url that needs to be open
	 */
	private static void submitForm(String url) throws Exception{
		URL activate = new URL(url);
		activate.openStream();
	}
	
	/*
	 * Activate the given instance
	 * @param 	dns, the dns address of the instance that needs to be activated
	 */
	private static void activate(String dns) throws Exception{
		submitForm(http + dns + username);
	}
	
	/*
	 * Submit the given data center instance to load generator
	 * @param 	dns, the dns address of the instance that needs to be submitted
	 */
	private static void submitDCInstance(String dns) throws Exception{
		submitForm(http + loadGeneratorDNS + iWantMore + dns);
	}
	
	/*
	 * Get the last rps lines in the log
	 * @return 	an ArrayList contains last rps lines in the log
	 */
	private static ArrayList<String> getLastLog() throws Exception{
		URL activate = new URL(http + loadGeneratorDNS + viewLog);
		BufferedReader in = new BufferedReader(new InputStreamReader(activate.openStream()));
		
		//Skip the first two lines
		in.readLine();
		in.readLine();
		
		String line = in.readLine();
		ArrayList<String> lastLog = new ArrayList<String> ();
		while (line != null && !line.startsWith("RESULT:")) {
			lastLog.clear();
			
			while(!line.startsWith("-")) {
				if (!line.startsWith("minute"))
					lastLog.add(line);
				line = in.readLine();
			}
			
			line = in.readLine();
		}
		
		return lastLog;
	}
	
	/*
	 * Get the result of the log
	 * @return 	the result string
	 */
	private static String getResult() throws Exception{
		URL activate = new URL(http + loadGeneratorDNS + viewLog);
		BufferedReader in = new BufferedReader(new InputStreamReader(activate.openStream()));
		
		String line = in.readLine();
		while (line != null && !line.startsWith("RESULT:")) {
			line = in.readLine();
		}
		
		String result = line;
		line = in.readLine();
		result += "\n" + line;
		return result;
	}
}