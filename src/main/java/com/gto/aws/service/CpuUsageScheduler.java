package com.gto.aws.service;
import java.util.Iterator;

import org.quartz.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.gto.aws.dao.DAOFactory;
import com.gto.aws.model.Group;

public class CpuUsageScheduler extends QuartzJobBean {
	
private int timeout;
  
  /**
   * Setter called after the ExampleJob is instantiated
   * with the value from the JobDetailBean (5)
   */ 
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

@Override
protected void executeInternal(JobExecutionContext arg0)
		throws JobExecutionException {
	
	CloudWatchService serv = new CloudWatchService();
	AmazonCloudWatchClient cloudWatchClient;
	AmazonEC2Client ec2Client;
	Iterable<String> keys = DAOFactory.getGroupDao().listKeys();
	for (String key : keys) {
		Group group = DAOFactory.getGroupDao().get(key);
		BasicAWSCredentials cred = new BasicAWSCredentials(group.getAccessKey(), group.getSecretKey());
		cloudWatchClient = new AmazonCloudWatchClient(cred);
		ec2Client =new AmazonEC2Client(cred);
		serv.setCloudWatchClient(cloudWatchClient);
		serv.setEc2Client(ec2Client);
		serv.run();
	}
	System.out.println("***************** Job COmpleted **************");
	
	// TODO Auto-generated method stub
	//jobService.instanceUsageDayRequests();
}
  
 
}