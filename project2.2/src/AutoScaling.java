/*
 * 15-619 Cloud Computing
 * Project2.2 Auto Scaling
 * File: AutoScaling.java
 * Name: Huacong Cai
 * AndrewID: hcai
 * Date: Sep, 26, 2014 
 */

/*
 * Create load balancer, auto scaling group, scaling policy and alarm, then
 * run warm up data center instances and start test, finally clear resources.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest;
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest;
import com.amazonaws.services.autoscaling.model.DeleteAutoScalingGroupRequest;
import com.amazonaws.services.autoscaling.model.DeleteLaunchConfigurationRequest;
import com.amazonaws.services.autoscaling.model.InstanceMonitoring;
import com.amazonaws.services.autoscaling.model.PutScalingPolicyRequest;
import com.amazonaws.services.autoscaling.model.PutScalingPolicyResult;
import com.amazonaws.services.autoscaling.model.UpdateAutoScalingGroupRequest;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.PutMetricAlarmRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.ConfigureHealthCheckRequest;
import com.amazonaws.services.elasticloadbalancing.model.ConnectionDraining;
import com.amazonaws.services.elasticloadbalancing.model.CreateLoadBalancerRequest;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersRequest;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.HealthCheck;
import com.amazonaws.services.elasticloadbalancing.model.Listener;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerAttributes;
import com.amazonaws.services.elasticloadbalancing.model.ModifyLoadBalancerAttributesRequest;

public class AutoScaling {
	
	static AmazonEC2Client ec2;
	static AmazonElasticLoadBalancingClient elb;
	static AmazonAutoScalingClient as;
	static AmazonCloudWatchClient cw;
	
	static String loadBalancerDNS;
	static Instance loadGeneratorIns;
	static String loadGeneratorDNS;
	
	static final int warmupTimes = 1;
	
	static final String andrewID = new String("hcai");
	static final String testID = new String("AutoScaling");
	static final String http = new String("http://");
	static final String username = new String("/username?username="+andrewID);
	static final String warmupURL = new String("/warmup?testId=warmup&dns=");
	static final String beginTestURL = new String("/begin-phase-2?testId="+testID+"&dns=");
	static final String viewLogURL = new String("/view-logs?name=result_" + andrewID + "_" + testID + ".txt");
	static final String healthCheckPage = new String("HTTP:80/heartbeat?username=" + andrewID);
	
	static final String loadBalancerName = new String("MSBELB");
	static final String availablityZone = new String("us-east-1c");
	static final String securityGroups = new String("sg-d2ffd1b7");
	static final String tagName = new String("Project");
	static final String tagValue = new String("2.2");
	static final String keyName = new String("CHC_key");
	static final String launchConfigName = new String("MSBLaunchConfig");
	static final String ASGName = new String("MSBAutoScaling");
	
	public static void main(String[] args) throws Exception{
		//Load the Properties File with AWS Credentials
		Properties properties = new Properties();
		properties.load(AutoScaling.class.getResourceAsStream("/AwsCredentials.properties"));
		 
		BasicAWSCredentials bawsc = new BasicAWSCredentials(properties.getProperty("accessKey"), properties.getProperty("secretKey"));
		 
		//Create an ELB client
		elb = new AmazonElasticLoadBalancingClient(bawsc);
		
		// Create ELB
		CreateLoadBalancerRequest LBRequest = new CreateLoadBalancerRequest();
		LBRequest.withAvailabilityZones(availablityZone)
		.withListeners(new Listener().withInstancePort(80)
					.withInstanceProtocol("HTTP")
					.withLoadBalancerPort(80)
					.withProtocol("HTTP"))
		.withLoadBalancerName(loadBalancerName)
		.withSecurityGroups(securityGroups)
		.withTags(new com.amazonaws.services.elasticloadbalancing.model.Tag()
				.withKey(tagName).withValue(tagValue));
		
		elb.createLoadBalancer(LBRequest);// send request
		System.out.println("Create ELB");
		
		// Modify load balancer attributes, disable draining
		LoadBalancerAttributes LBAttr = new LoadBalancerAttributes();
		LBAttr.withConnectionDraining(new ConnectionDraining().withEnabled(false));
		
		ModifyLoadBalancerAttributesRequest modifyLBAttrRequest = new ModifyLoadBalancerAttributesRequest();
		modifyLBAttrRequest.withLoadBalancerAttributes(LBAttr)
		.withLoadBalancerName(loadBalancerName);
		
		elb.modifyLoadBalancerAttributes(modifyLBAttrRequest); // send request
		System.out.println("Disable draining");
		
		// Health check
		HealthCheck healthCheck = new HealthCheck();
		healthCheck.withHealthyThreshold(10)
		.withInterval(30)
		.withTarget(healthCheckPage)
		.withTimeout(5)
		.withUnhealthyThreshold(5);
		
		ConfigureHealthCheckRequest healthCheckRequest = new ConfigureHealthCheckRequest();
		healthCheckRequest.withHealthCheck(healthCheck)
		.withLoadBalancerName(loadBalancerName);
		
		elb.configureHealthCheck(healthCheckRequest); // send request
		System.out.println("Configure health check");
		
		// Get ELB DNS
		DescribeLoadBalancersRequest describeLBRequest = new DescribeLoadBalancersRequest();
		describeLBRequest.withLoadBalancerNames(loadBalancerName);
		
		DescribeLoadBalancersResult describeLBResult = elb.describeLoadBalancers(describeLBRequest);

		loadBalancerDNS = describeLBResult.getLoadBalancerDescriptions().get(0).getDNSName();
		System.out.println("Get ELB DNS " + loadBalancerDNS);
		
		// Create an auto scaling client
		as = new AmazonAutoScalingClient(bawsc);
		
		// Create launch configuration
		CreateLaunchConfigurationRequest launchConfigRequest = new CreateLaunchConfigurationRequest();
		launchConfigRequest.withImageId("ami-ec14ba84")
		.withInstanceMonitoring(new InstanceMonitoring().withEnabled(true))
		.withInstanceType("m3.medium")
		.withKeyName("CHC_key")
		.withLaunchConfigurationName(launchConfigName)
		.withSecurityGroups("HTTP_SSH");
		
		as.createLaunchConfiguration(launchConfigRequest); // send request
		System.out.println("Create launch configuration");
		
		// Create auto scale group
		CreateAutoScalingGroupRequest ASGRequest = new CreateAutoScalingGroupRequest();
		ASGRequest.withAutoScalingGroupName(ASGName)
		.withAvailabilityZones(availablityZone)
		.withDefaultCooldown(60)
		.withDesiredCapacity(2)
		.withHealthCheckGracePeriod(180)
		.withHealthCheckType("ELB")
		.withLaunchConfigurationName(launchConfigName)
		.withLoadBalancerNames(loadBalancerName)
		.withMaxSize(5)
		.withMinSize(2)
		.withTags(new com.amazonaws.services.autoscaling.model.Tag()
				.withKey(tagName).withValue(tagValue)
				.withPropagateAtLaunch(true).withResourceId(ASGName));
		
		as.createAutoScalingGroup(ASGRequest); // send request
		System.out.println("Create auto scale group");
		
		// Scale policies
		// Scale out
		PutScalingPolicyRequest scaleOutOne = new PutScalingPolicyRequest();
		scaleOutOne.withAdjustmentType("ChangeInCapacity")
		.withAutoScalingGroupName(ASGName)
		.withCooldown(120)
		.withPolicyName("scaleOutOne")
		.withScalingAdjustment(1);
		
		PutScalingPolicyResult scaleOutOneResult = as.putScalingPolicy(scaleOutOne); // send request
		System.out.println("Create scale out policy");
		
		// Scale in
		PutScalingPolicyRequest scaleInOne = new PutScalingPolicyRequest();
		scaleInOne.withAdjustmentType("ChangeInCapacity")
		.withAutoScalingGroupName(ASGName)
		.withCooldown(120)
		.withPolicyName("scaleInOne")
		.withScalingAdjustment(-1);
		
		PutScalingPolicyResult scaleInOneResult = as.putScalingPolicy(scaleInOne); // send request
		System.out.println("Create scale in policy");
		
		// Create a cloud watch client
		cw = new AmazonCloudWatchClient(bawsc);
		
		// Create CloudWatch alarm
		// Alarm that scale out one vm per time
		PutMetricAlarmRequest scaleOutOneAlarm = new PutMetricAlarmRequest();
		scaleOutOneAlarm.withActionsEnabled(true)
		.withAlarmActions(scaleOutOneResult.getPolicyARN())
		.withAlarmName("scaleOutOneAlarm")
		.withComparisonOperator("GreaterThanThreshold")
		.withDimensions(new Dimension().withName("AutoScalingGroupName")
						.withValue(ASGName))
		.withEvaluationPeriods(1)
		.withNamespace("AWS/EC2")
		.withMetricName("NetworkIn")
		.withPeriod(60) //avg on 60s
		.withStatistic("Average")
		.withThreshold(26000000d)
		.withUnit(StandardUnit.Bytes);
		
		cw.putMetricAlarm(scaleOutOneAlarm); // send request
		System.out.println("Create scale out alarm");
		
		// Alarm that scale in one vm per time
		PutMetricAlarmRequest scaleInOneAlarm = new PutMetricAlarmRequest();
		scaleInOneAlarm.withActionsEnabled(true)
		.withAlarmActions(scaleInOneResult.getPolicyARN())
		.withAlarmName("scaleInOneAlarm")
		.withComparisonOperator("LessThanThreshold")
		.withDimensions(new Dimension().withName("AutoScalingGroupName")
						.withValue(ASGName))
		.withEvaluationPeriods(1)
		.withNamespace("AWS/EC2")
		.withMetricName("NetworkIn")
		.withPeriod(60) //avg on 60s
		.withStatistic("Average")
		.withThreshold(20000000d)
		.withUnit(StandardUnit.Bytes);
		
		cw.putMetricAlarm(scaleInOneAlarm); // send request
		System.out.println("Create scale in alarm");
		
		// Create an Amazon EC2 Client 
		ec2 = new AmazonEC2Client(bawsc);
		
		// Create load generator
		loadGeneratorIns = AutoScaling.launchLoadGenerator();
		loadGeneratorDNS = loadGeneratorIns.getPublicDnsName();
		AutoScaling.activate(loadGeneratorDNS);
		System.out.println("Create load generator");
		//TimeUnit.SECONDS.sleep(330); // wait for desired vm in service
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		System.out.println("Enter your value:");
		str = br.readLine();
		System.out.println("your value is :"+str);

		// Warm up
		for (int i=0; i<warmupTimes; ++i) {
			AutoScaling.warmup();
			TimeUnit.SECONDS.sleep(330); // wait for warm up ends
		}
		System.out.println("Warm up");
		
		// Begin test
		System.out.println("Start test");
		AutoScaling.beginTest();
		
		// Wait until test ends
		TimeUnit.SECONDS.sleep(2520);
		System.out.println("Test end");
		
		// Print traffic token and result
		AutoScaling.printResult();
		
		// Terminate vm and delete resources
		UpdateAutoScalingGroupRequest updateASGRequest = new UpdateAutoScalingGroupRequest();
		updateASGRequest.withAutoScalingGroupName(ASGName)
		.withDesiredCapacity(0)
		.withMaxSize(0)
		.withMinSize(0);
		
		as.updateAutoScalingGroup(updateASGRequest); // send request
		System.out.println("Update ASG and set vm to 0");
		TimeUnit.SECONDS.sleep(60);
		
		// Delete auto scaling
		DeleteAutoScalingGroupRequest delASGRequest = new DeleteAutoScalingGroupRequest();
		delASGRequest.withAutoScalingGroupName(ASGName);
		
		as.deleteAutoScalingGroup(delASGRequest); // send request
		System.out.println("Delete ASG");
		
		// Delete launch configuration
		DeleteLaunchConfigurationRequest delLaunchConfigRequest = new DeleteLaunchConfigurationRequest();	
		delLaunchConfigRequest.withLaunchConfigurationName(launchConfigName);
		
	    as.deleteLaunchConfiguration(delLaunchConfigRequest); // send request
	    System.out.println("Delete launch configuration");
	}
	
	/*
	 * Launch a new instance of the given type
	 * @param 	type, the type of instance that will be launch
	 */
	private static Instance launchLoadGenerator() throws InterruptedException {
		//Create Instance Request
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
		
		//Set availability zone
		Placement place = new Placement();
        place.setAvailabilityZone(availablityZone);
        
        runInstancesRequest.setPlacement(place);
		runInstancesRequest.withImageId("ami-562d853e")
		.withInstanceType("m3.medium")
		.withMinCount(1)
		.withMaxCount(1)
		.withKeyName(keyName)
		.withSecurityGroups("HTTP_SSH");
		 
		//Launch Instance
		RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
		
		//Return the Object Reference of the Instance just Launched
		Instance instance = runInstancesResult.getReservation().getInstances().get(0);
		
		//Tag instance
		String instanceID = instance.getInstanceId();
		CreateTagsRequest createTagsRequest = new CreateTagsRequest();
		createTagsRequest.withResources(instanceID)
						 .withTags(new Tag(tagName, tagValue));	
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
	 * Warm up the ELB
	 */
	private static void warmup() throws Exception{
		System.out.println(http + loadGeneratorDNS + warmupURL + loadBalancerDNS);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		System.out.println("Enter your value:");
		str = br.readLine();
		System.out.println("your value is :"+str);
		submitForm(http + loadGeneratorDNS + warmupURL + loadBalancerDNS);
	}
	
	/*
	 * Begin the test
	 */
	private static void beginTest() throws Exception{
		submitForm(http + loadGeneratorDNS + beginTestURL + loadBalancerDNS);
	}
	
	/*
	 * Print the result of the test
	 */
	private static void printResult() throws Exception{
		URL activate = new URL(http + loadGeneratorDNS + viewLogURL);
		BufferedReader in = new BufferedReader(new InputStreamReader(activate.openStream()));
		
		String line = in.readLine();
		while (line != null && !line.startsWith("Test launched.")) {
			line = in.readLine();
		}
		System.out.println(line);
		
		while (line != null && !line.startsWith("Total Bonus")) {
				line = in.readLine();
		}
		System.out.println(line);
	}
}
