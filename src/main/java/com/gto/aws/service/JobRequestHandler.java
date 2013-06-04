package com.gto.aws.service;
 
import java.util.Iterator;
import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import com.gto.aws.convertor.ConvertorFactory;
import com.gto.aws.model.JobConstants;
import com.gto.aws.model.JobRequest;
import com.gto.aws.model.JobRequestInstance;

/**
 * This class implements org.springframework.amqp.core.MessageListener.
 *  It is tied to TUTORIAL_EXCHANGE and listing to an anonomous queue
 *  which picks up message in the  TUTORIAL_EXCHANGE with a routing pattern of
 *  my.routingkey.1  specified in rabbt-listener-contet.xml file.
 */
public class JobRequestHandler {

	@Autowired
	private CloudWatchService cloudWatchService;
    public JobRequest handleMessage(JobRequest request) {
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
    }
}