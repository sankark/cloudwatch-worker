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

public class CleanupScheduler extends QuartzJobBean {
	
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
	
	CleanupService cleanup = new CleanupService();
	cleanup.clean();
	System.out.println("***************** Cleanup Job COmpleted **************");
	
	// TODO Auto-generated method stub
	//jobService.instanceUsageDayRequests();
}
  
 
}