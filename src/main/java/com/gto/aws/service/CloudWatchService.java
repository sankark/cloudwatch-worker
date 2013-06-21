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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.query.indexes.BinIndex;
import com.gto.aws.convertor.ConvertorFactory;
import com.gto.aws.dao.DAOFactory;
import com.gto.aws.model.Data;
import com.gto.aws.model.DataPoint;
import com.gto.aws.model.Ec2CpuUtilizationInstance;
import com.gto.aws.model.Group;
import com.gto.aws.model.InstanceHistory;
import com.gto.aws.model.JobConstants;
import com.gto.aws.model.TimeData;

public class CloudWatchService{

	private AmazonCloudWatchClient cloudWatchClient;

	public AmazonEC2Client getEc2Client() {
		return ec2Client;
	}

	public void setEc2Client(AmazonEC2Client ec2Client) {
		this.ec2Client = ec2Client;
	}

	private AmazonEC2Client ec2Client;
	
	public AmazonCloudWatchClient getCloudWatchClient() {
		return cloudWatchClient;
	}

	public void setCloudWatchClient(AmazonCloudWatchClient cloudWatchClient) {
		this.cloudWatchClient = cloudWatchClient;
	}

	/*public void processJob(JobRequestInstance req){
		 System.out.println("inside process");
		Ec2CpuUtilizationJob job = req.getJob();
		String date = req.getDate();
		Collection<String> instances = job.getInstances();
		for (Iterator iterator = instances.iterator(); iterator.hasNext();) {
			String instance = (String) iterator.next();
			listMetrics(instance,date,req.getJobRequestId());
		}
		
		
	}*/
	/*public void listMetrics(String instance,String date,String jobId) {
		
		Ec2CpuUtilizationInstance inst = new Ec2CpuUtilizationInstance();
		inst.setJobId(jobId);
		inst.setDate(date);
		inst.setInstanceId(instance);
	
    	RequestHandler requestHandler = null;
		ListMetricsRequest listMetricsRequest=new ListMetricsRequest();
		Collection<Dimension> dimensionsList = new ArrayList<Dimension>();
		Dimension dimensionFilter = new Dimension();
		dimensionFilter.setName("InstanceId");
		dimensionFilter.setValue(instance);
		dimensionsList.add(dimensionFilter);
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setMetricName("CPUUtilization");
		
		
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
		statistics.add("SampleCount");
		statistics.add("Minimum");
		statistics.add("Maximum");
		statistics.add("Sum");
	
		getMetricStatisticsRequest.setStatistics(statistics );
		getMetricStatisticsRequest.setPeriod(3600);
		getMetricStatisticsRequest.setDimensions(dimensionsList);
		GetMetricStatisticsResult result = cloudWatchClient.getMetricStatistics(getMetricStatisticsRequest );
		Data data = new Data();
		List<Datapoint> datapoints = result.getDatapoints();
		//sortDatabyTime(datapoints);
				for (Iterator<Datapoint> iterator =  datapoints.iterator(); iterator.hasNext();) {
				Datapoint datapoint =  iterator.next();
				data.getData().add(new DataPoint(datapoint));
		System.out.println(datapoint.getTimestamp());
			
		}
			
				java.util.Calendar c2 = java.util.Calendar.getInstance();
				c2.setTime(startTime);
				data.setStartTime(String.valueOf(c2.get(Calendar.HOUR_OF_DAY)));
				c2.setTime(endTime);
				data.setEndTime(String.valueOf(c2.get(Calendar.HOUR_OF_DAY)));
			inst.getHourlyData().add(data);
			
		//	System.out.println("sdffsdf " +inst.getHourlyData().iterator().next().getData().size());
		}	
	int i =0;
		for (Iterator iterator = inst.getHourlyData().iterator(); iterator.hasNext();) {
			i++;
			Data data = (Data) iterator.next();
			Collection<DataPoint> t1 = data.getData();
			for (Iterator iterator2 = t1.iterator(); iterator2.hasNext();) {
				DataPoint dataPoint = (DataPoint) iterator2.next();
				System.out.println("dataPoint"+i);
				System.out.println("dataPoint time"+dataPoint.getTimestamp());
			}
			System.out.println();
			
		}
		Collection<Data> test = inst.getHourlyData();
		for (Iterator iterator = test.iterator(); iterator.hasNext();) {
			Data data = (Data) iterator.next();
			System.out.println(data.getStartTime());
			
		}
		System.out.println("inserting size 8***"+inst.getHourlyData());
		Bucket jobInstanceBucket = null;
		populateInstanceHistory(inst);
		//sortDatabyTime(inst);
		try {
			 jobInstanceBucket = getInstanceBucket();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			jobInstanceBucket.store(inst).withConverter(ConvertorFactory.getJobInstanceConvertor()).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	
	public void run(){
		populateInstanceHistoryForAllInstances();
	}
	private void populateInstanceHistoryForAllInstances(){
		Set<Instance> instances = getEc2Instances();
		for (Iterator iterator = instances.iterator(); iterator.hasNext();) {
			Instance instance = (Instance) iterator.next();
			
			populateInstanceHistoryForLast30Days(instance.getInstanceId());
			
		}
	}
	
	private Set<Instance> getEc2Instances() {
		Set<Instance> instances = new HashSet<Instance>();
  
            DescribeInstancesResult describeInstancesRequest = ec2Client.describeInstances();
            List<Reservation> reservations = describeInstancesRequest.getReservations();
            

            for (Reservation reservation : reservations) {
                instances.addAll(reservation.getInstances());
            }
            return instances;
	}

	public void populateInstanceHistoryForLast30Days(String instanceID){
		Date today = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(today);
	//	cal.add(Calendar.DAY_OF_MONTH, -1);
		Date previouDay = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -18);
		Date today30 = cal.getTime();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
		String fromDate = sdf.format(today30);
		String toDate = sdf.format(previouDay);
		System.out.println(fromDate +":"+toDate);
		listMetricsBetween(fromDate,toDate,instanceID);
	}
	
public void listMetricsBetween(String fromDate,String toDate,String instance) {
		

	

		Collection<Dimension> dimensionsList = new ArrayList<Dimension>();
		Dimension dimensionFilter = new Dimension();
		dimensionFilter.setName("InstanceId");
		dimensionFilter.setValue(instance);
		dimensionsList.add(dimensionFilter);
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setMetricName("CPUUtilization");
		
		
		// Start date
		List<DataPoint> dataList = new ArrayList<DataPoint>();
		java.util.Calendar startDate = getCalenderForDateString(fromDate);
		java.util.Calendar endDate = getCalenderForDateString(toDate);
		endDate.add(Calendar.HOUR,  23);
		endDate.add(Calendar.MINUTE,  59);
		endDate.add(Calendar.SECOND,  59);
		while(startDate.getTimeInMillis() <= endDate.getTimeInMillis() )
		for(int i=0;i<=23;i++){
		Date startTime=startDate.getTime();
		startDate.add(Calendar.HOUR, 1);
		
		Date endTime=startDate.getTime();
		getMetricStatisticsRequest.setStartTime(startTime);
		getMetricStatisticsRequest.setEndTime(endTime);
		getMetricStatisticsRequest.setNamespace("AWS/EC2");
		Collection<String> statistics = new ArrayList<String>();
		statistics.add("Average");
		statistics.add("SampleCount");
		statistics.add("Minimum");
		statistics.add("Maximum");
		statistics.add("Sum");
	
		getMetricStatisticsRequest.setStatistics(statistics );
		getMetricStatisticsRequest.setPeriod(60);
		getMetricStatisticsRequest.setDimensions(dimensionsList);
		GetMetricStatisticsResult result = cloudWatchClient.getMetricStatistics(getMetricStatisticsRequest );
		Data data = new Data();
		List<Datapoint> datapoints = result.getDatapoints();
		List<DataPoint> temp = new ArrayList<DataPoint>();
 		for (Iterator iterator = datapoints.iterator(); iterator.hasNext();) {
			DataPoint datapoint = new DataPoint((Datapoint) iterator.next());
			temp.add(datapoint);
			
		}
		//sortDatabyTime(temp);

					dataList.addAll(temp);
			
	
			
	
		}	
		sortDatabyTime(dataList);
		InstanceHistory history = new InstanceHistory();
		history.setInstanceId(instance);
		getStartTime2(dataList, history);
		populateLastStartandStopTime(history);
		populateUsageHours(history);
		populateHourlyData(history,fromDate,toDate,instance);
		populateAverageCpu(dataList,history);
		store(history,dataList);
		System.out.println(new JSONObject(history));
		
	}

	private void populateHourlyData(InstanceHistory history, String fromDate,
		String toDate, String instance) {
	// TODO Auto-generated method stub
		
		Collection<Dimension> dimensionsList = new ArrayList<Dimension>();
		Dimension dimensionFilter = new Dimension();
		dimensionFilter.setName("InstanceId");
		dimensionFilter.setValue(instance);
		dimensionsList.add(dimensionFilter);
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setMetricName("CPUUtilization");
		
		
		// Start date
		List<DataPoint> dataList = new ArrayList<DataPoint>();
		java.util.Calendar startDate = getCalenderForDateString(fromDate);
		java.util.Calendar endDate = getCalenderForDateString(toDate);
	
		Date startTime=startDate.getTime();
		Date endTime=endDate.getTime();
		getMetricStatisticsRequest.setStartTime(startTime);
		getMetricStatisticsRequest.setEndTime(endTime);
		getMetricStatisticsRequest.setNamespace("AWS/EC2");
		Collection<String> statistics = new ArrayList<String>();
		statistics.add("Average");
		statistics.add("SampleCount");
		statistics.add("Minimum");
		statistics.add("Maximum");
		statistics.add("Sum");
	
		getMetricStatisticsRequest.setStatistics(statistics );
		getMetricStatisticsRequest.setPeriod(3600);
		getMetricStatisticsRequest.setDimensions(dimensionsList);
		GetMetricStatisticsResult result = cloudWatchClient.getMetricStatistics(getMetricStatisticsRequest );
		Data data = new Data();
		List<Datapoint> datapoints = result.getDatapoints();
		List<DataPoint> temp = new ArrayList<DataPoint>();
 		for (Iterator iterator = datapoints.iterator(); iterator.hasNext();) {
			DataPoint datapoint = new DataPoint((Datapoint) iterator.next());
			temp.add(datapoint);
			
		}
		sortDatabyTime(temp);

					dataList.addAll(temp);
		
					history.setHourlyDataPoints(dataList);
					
			
	
	
}

	private void store(InstanceHistory history, List<DataPoint> dataList) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
		String today = sdf.format(new Date());
		history.setDataPoints(dataList);
		history.setLastUpdateddate(today);
		DAOFactory.getInstanceHistoryDao().put(history);
	// TODO Auto-generated method stub
	
}

	private void populateAverageCpu(List<DataPoint> dataList,
		InstanceHistory history) {
		
		long avgCpu = 0;
		for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
			DataPoint datapoint = (DataPoint) iterator.next();
			avgCpu = avgCpu + datapoint.getAverage().longValue();
			
		}
		if(avgCpu > 0)
		avgCpu = avgCpu / dataList.size();
		history.setAvgCpuforLastMonth(avgCpu);
	// TODO Auto-generated method stub
	
}

	private void populateUsageHours(InstanceHistory history) {
	// TODO Auto-generated method stub
		ArrayList<TimeData> timeDataList = (ArrayList<TimeData>) history.getTimeData();
		long totalRunningTimeinMillis=0;
		for (Iterator iterator = timeDataList.iterator(); iterator.hasNext();) {
			TimeData timeData = (TimeData) iterator.next();
			totalRunningTimeinMillis= totalRunningTimeinMillis +(timeData.getEndTime().getTime() - timeData.getStartTime().getTime())+300000;
		}
		history.setUsageHoursForLastMonth(totalRunningTimeinMillis);
}

	private void populateLastStartandStopTime(InstanceHistory history) {
		ArrayList<TimeData> timeDataList = (ArrayList<TimeData>) history.getTimeData();
		TimeData timeData=null;
		try {
			timeData = timeDataList.get(timeDataList.size()-1);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			
		}
		history.setLastStartandStopTime(timeData);
	// TODO Auto-generated method stub
	
}

	private InstanceHistory getInstanceHistoryFromBucket(String instance) {
		InstanceHistory instanceHistory = DAOFactory.getInstanceHistoryDao().get(instance);
	// TODO Auto-generated method stub
	return instanceHistory;
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
	
	private java.util.Calendar getCalenderForDate(Date date) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
		java.util.Calendar c = java.util.Calendar.getInstance();
		try {
			c.setTime(sdf.parse(sdf.format(date)));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return c;
	}

	private Bucket getInstanceBucket() throws RiakRetryFailedException {
		return RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_INSTANCES).execute();
	}

	private void sortDatabyTime(List<DataPoint> dataPoints) {
		// TODO Auto-generated method stub
		Collections.sort(dataPoints, new Comparator<DataPoint>(){
		     public int compare(DataPoint o1, DataPoint o2){
		         return o1.getTimestamp().compareTo(o2.getTimestamp()) ;
		     }
		});
	}

	
	private void populateInstanceStartandStopTime(){
		Bucket jobInstanceBucket = null;
		try {
			 jobInstanceBucket = getInstanceBucket();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			List<String> indices = jobInstanceBucket.fetchIndex(BinIndex.named("date")).withValue("").execute();
			for (Iterator iterator = indices.iterator(); iterator.hasNext();) {
				String jobInstanceId = (String) iterator.next();
				Ec2CpuUtilizationInstance instance = new Ec2CpuUtilizationInstance();
				instance.setJobId(jobInstanceId);
				instance = jobInstanceBucket.fetch(instance).withConverter(ConvertorFactory.getJobInstanceConvertor()).execute();
				populateInstanceHistory(instance);
			}
			
		} catch (RiakException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void populateInstanceHistory(Ec2CpuUtilizationInstance instance) {
		// TODO Auto-generated method stub

		InstanceHistory instanceHistory = new InstanceHistory();
		Collection<Data> hourlyDatas = instance.getHourlyData();
			for (Iterator iterator = hourlyDatas.iterator(); iterator.hasNext();) {
				Data data = (Data) iterator.next();
				Collection<DataPoint> hourlyData = data.getData();
				
				
					
						getStartTime((List<DataPoint>) hourlyData,instanceHistory);
					
					// could be start and stop
				
					
				

	}
			System.out.println(new JSONObject(instanceHistory));
	}

	private Date getPreviousEndTime(Ec2CpuUtilizationInstance instance) {
		return null;
		// TODO Auto-generated method stub
		
	}

	private void getStartTime(List<DataPoint> hourlyData,InstanceHistory instanceHistory) {
		// TODO Auto-generated method stub
		
		ArrayList<TimeData> timeDataList = (ArrayList<TimeData>) instanceHistory.getTimeData();
		TimeData timeData=null;
		for (Iterator iterator = hourlyData.listIterator(); iterator.hasNext();) {
			try {
				timeData = timeDataList.get(timeDataList.size()-1);
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				
			}
			DataPoint dataPoint = (DataPoint) iterator.next();
			//System.out.println(timeData.getEndTime());
			if(timeData !=null && timeData.getEndTime().getTime()+ 300000 == dataPoint.getTimestamp().getTime() ){
				timeData.setEndTime(dataPoint.getTimestamp());
			}
			else{
				//update previos endtime as previos starttime or + previos starttime + 1 to 5
				TimeData timeDataNew = new TimeData();
				timeDataNew.setStartTime(dataPoint.getTimestamp());
				timeDataNew.setEndTime(dataPoint.getTimestamp());
				instanceHistory.getTimeData().add(timeDataNew);
	
					
			}
			
		}
		
		
		
	}
	
	private void getStartTime2(List<DataPoint> hourlyData,InstanceHistory instanceHistory) {
		// TODO Auto-generated method stub
		
		
		ArrayList<TimeData> timeDataList = (ArrayList<TimeData>) instanceHistory.getTimeData();
		TimeData timeData=null;
		for (Iterator iterator = hourlyData.listIterator(); iterator.hasNext();) {
			try {
				timeData = timeDataList.get(timeDataList.size()-1);
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				
			}
			DataPoint dataPoint = (DataPoint) iterator.next();
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
				instanceHistory.getTimeData().add(timeDataNew);
	
					
			}
			
		}
		
		
		
	}


	private Date getPreviosDayStartTime() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		
	/*	 ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/integration/spring-integration-context.xml");//loading beans
		 CloudWatchService serv = (CloudWatchService) context.getBean("cloudWatchService");
		// serv.listMetrics("i-a0be39c3", "06102013", "test");
		//serv.populateInstanceHistoryForLast30Days("i-c30fa5a4");
		 serv.populateInstanceHistoryForAllInstances();*/
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
	}
}
