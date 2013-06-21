package com.gto.aws.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.query.indexes.BinIndex;
import com.gto.aws.convertor.ConvertorFactory;
import com.gto.aws.dao.DAOFactory;
import com.gto.aws.model.CpuHeatMap;
import com.gto.aws.model.Data;
import com.gto.aws.model.DataPoint;
import com.gto.aws.model.Ec2CpuUtilizationInstance;
import com.gto.aws.model.Ec2CpuUtilizationJob;
import com.gto.aws.model.InstanceHistory;
import com.gto.aws.model.InstanceUsagePerDay;
import com.gto.aws.model.JobConstants;
import com.gto.aws.model.JobRequest;
import com.gto.aws.model.JobRequestInstance;
import com.gto.aws.model.TimeData;

public class CpuUtilizationJob {
	@Autowired
	private AmazonCloudWatchAsyncClient cloudWatchClient;
	@Autowired
	private AmazonEC2Client ec2Client;
	
	public AmazonCloudWatchAsyncClient getCloudWatchClient() {
		return cloudWatchClient;
	}

	public void setCloudWatchClient(AmazonCloudWatchAsyncClient cloudWatchClient) {
		this.cloudWatchClient = cloudWatchClient;
	}

	public void processJob(JobRequestInstance req){
		 System.out.println("inside process");
		Ec2CpuUtilizationJob job = req.getJob();
		String date = req.getDate();
		Collection<String> instances = job.getInstances();
		for (Iterator iterator = instances.iterator(); iterator.hasNext();) {
			String instance = (String) iterator.next();
			buildFor30Days(instance,date,req.getJobRequestId());
			listMetrics(instance,date,req.getJobRequestId());
		}
		
		
	}
	private void buildFor30Days(String instance,String date,String jobId) {
		// TODO Auto-generated method stub
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
		java.util.Calendar c = java.util.Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<30;i++){
		c.add(Calendar.DATE, -1);
		String newDate = sdf.format(c.getTime());
		InstanceUsagePerDay usage = DAOFactory.getInstanceUsageDao().get(instance+"-"+newDate);
		if(usage == null){
			listMetrics(instance,newDate,jobId);
		}
		}
	}

	public void listMetrics(String instance,String date,String jobId) {
		

		Collection<Dimension> dimensionsList = new ArrayList<Dimension>();
		Dimension dimensionFilter = new Dimension();
		dimensionFilter.setName("InstanceId");
		dimensionFilter.setValue(instance);
		dimensionsList.add(dimensionFilter);
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setMetricName("CPUUtilization");
		
		List<Datapoint> datapoints=new ArrayList<Datapoint>();
		// Start date
		java.util.Calendar c = getCalenderForDateString(date);
		for(int i=0;i<=23;i++){
		Date startTime=c.getTime();
		c.add(Calendar.HOUR, 1);
		Date endTime=c.getTime();
		getMetricStatisticsRequest.setStartTime(startTime);
		getMetricStatisticsRequest.setEndTime(endTime);
		getMetricStatisticsRequest.setNamespace("AWS/EC2");
		Collection<String> statistics = new ArrayList<String>();
		statistics.add("Average");
		getMetricStatisticsRequest.setStatistics(statistics );
		getMetricStatisticsRequest.setPeriod(60);
		getMetricStatisticsRequest.setDimensions(dimensionsList);
		GetMetricStatisticsResult result = cloudWatchClient.getMetricStatistics(getMetricStatisticsRequest );
		new Data();
		if(result.getDatapoints()!=null)
		datapoints.addAll(result.getDatapoints());
		
		}
		sortDatabyTime(datapoints);
		
		ArrayList<TimeData> timeDataList = new ArrayList<TimeData>();
		TimeData timeData=null;
		for (Iterator<Datapoint> iterator = datapoints.listIterator(); iterator.hasNext();) {
			try {
				timeData = timeDataList.get(timeDataList.size()-1);
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				
			}
			Datapoint dataPoint = (Datapoint) iterator.next();
			System.out.println(dataPoint.getTimestamp());
			//System.out.println(timeData.getEndTime());
			if(timeData !=null && timeData.getEndTime().getTime()+ 300000 == dataPoint.getTimestamp().getTime() ){
				timeData.setEndTime(dataPoint.getTimestamp());
			}
			else{
				//update previos endtime as previos starttime or + previos starttime + 1 to 5
				TimeData timeDataNew = new TimeData();
				timeDataNew.setStartTime(dataPoint.getTimestamp());
				timeDataNew.setEndTime(dataPoint.getTimestamp());
				timeDataList.add(timeDataNew);
	
					
			}
		}
		
		
		long usageInMillis = usagePerDay(timeDataList);
		long usageInMinutes = ((usageInMillis / 1000) / 60);
		InstanceUsagePerDay instanceUsagePerDay = new InstanceUsagePerDay();
		instanceUsagePerDay.setInstanceId(instance);
		instanceUsagePerDay.setUsageMinutes(usageInMinutes);
		instanceUsagePerDay.setDate(date);
		System.out.println(usageInMillis);
		DAOFactory.getInstanceUsageDao().put(instanceUsagePerDay);
		
			
	}

	private long usagePerDay(ArrayList<TimeData> timeDataList) {
		long totalRunningTimeinMillis=0;
		for (Iterator iterator = timeDataList.iterator(); iterator.hasNext();) {
			TimeData timeData = (TimeData) iterator.next();
			totalRunningTimeinMillis= totalRunningTimeinMillis +(timeData.getEndTime().getTime() - timeData.getStartTime().getTime())+300000;
		}
		return totalRunningTimeinMillis;
	}
	
	
	
	private java.util.Calendar getCalenderForDateString(String date) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
		java.util.Calendar c = java.util.Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return c;
	}
	
	
	private void sortDatabyTime(List<Datapoint> datapoints) {
		// TODO Auto-generated method stub
		Collections.sort(datapoints, new Comparator<Datapoint>(){
		     @Override
			public int compare(Datapoint o1, Datapoint o2){
		         return o1.getTimestamp().compareTo(o2.getTimestamp()) ;
		     }
		});
	}

	



	public static void main(String[] args) {
		
		 ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/integration/spring-integration-context.xml");//loading beans
		 CpuUtilizationJob serv = (CpuUtilizationJob) context.getBean("cpuUtilizationJob");
		// serv.listMetrics("i-a0be39c3", "06102013", "test");
		//serv.populateInstanceHistoryForLast30Days("i-c30fa5a4");
		// serv.populateInstanceHistoryForAllInstances();
		 serv.listMetrics("i-88b704f6", "06172013", "test");
	}

	public void listMetrics(JobRequest request) {
		buildFor30Days(request.getInstanceId(),request.getDate(),request.getJobRequestId());
		listMetrics(request.getInstanceId(),request.getDate(),request.getJobRequestId());
		// TODO Auto-generated method stub
		
	}
}
