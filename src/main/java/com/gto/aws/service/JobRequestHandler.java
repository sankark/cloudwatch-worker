package com.gto.aws.service;
 
import org.springframework.beans.factory.annotation.Autowired;

import com.gto.aws.model.JobConstants;
import com.gto.aws.model.JobRequest;

/**
 * This class implements org.springframework.amqp.core.MessageListener.
 *  It is tied to TUTORIAL_EXCHANGE and listing to an anonomous queue
 *  which picks up message in the  TUTORIAL_EXCHANGE with a routing pattern of
 *  my.routingkey.1  specified in rabbt-listener-contet.xml file.
 */
public class JobRequestHandler {

	@Autowired
	private CpuUtilizationJob cpuUtilizationJob;
   /* public JobRequest handleMessage(JobRequest request) {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Bucket jobRequestBucket=null;
    	try {
			jobRequestBucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_REQUESTS).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	JobRequestInstance req = new JobRequestInstance();
    	req.setJobRequestId(request.getJobRequestId());
    	try {
			req = jobRequestBucket.fetch(req).withConverter(ConvertorFactory.getJobRequestConvertor()).execute();
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println(req.getJob().getJobName());
    	cloudWatchService.processJob(req);
    	request.setResponse(JobConstants.COMPLETED);
    	return request;
    }*/
	public JobRequest handleMessage(JobRequest request) {
		try {
			cpuUtilizationJob.listMetrics(request);
		} catch (Exception e) {
			request.setResponse(JobConstants.FAILED);
			// TODO: handle exception
		}
		request.setResponse(JobConstants.COMPLETED);
		return request;
		
	}
	
}