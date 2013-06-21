package com.gto.aws.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.basho.riak.client.convert.RiakKey;

public class InstanceHistory {

@RiakKey private String instanceId;
private String lastUpdateddate;
private long avgCpuforLast30Days;
private long networkOutforLast30Days;
private long usageHoursForLast30Days;

private Collection<DataPoint> minutesDataPoints;
private Collection<DataPoint> hourlyDataPoints;
public Collection<DataPoint> getHourlyDataPoints() {
	return hourlyDataPoints;
}
public void setHourlyDataPoints(Collection<DataPoint> hourlyDataPoints) {
	this.hourlyDataPoints = hourlyDataPoints;
}
public Collection<DataPoint> getDataPoints() {
	return minutesDataPoints;
}
public void setDataPoints(Collection<DataPoint> dataPoints) {
	this.minutesDataPoints = dataPoints;
}
public long getUsageHoursForLastMonth() {
	return usageHoursForLast30Days;
}
public void setUsageHoursForLastMonth(long usageHoursForLastMonth) {
	this.usageHoursForLast30Days = usageHoursForLastMonth;
}
public String getLastUpdateddate() {
	return lastUpdateddate;
}
public void setLastUpdateddate(String lastUpdateddate) {
	this.lastUpdateddate = lastUpdateddate;
}
public long getAvgCpuforLastMonth() {
	return avgCpuforLast30Days;
}
public void setAvgCpuforLastMonth(long avgCpuforLastMonth) {
	this.avgCpuforLast30Days = avgCpuforLastMonth;
}
public long getNetworkOutforLastMonth() {
	return networkOutforLast30Days;
}
public void setNetworkOutforLastMonth(long networkOutforLastMonth) {
	this.networkOutforLast30Days = networkOutforLastMonth;
}
public TimeData getLastStartandStopTime() {
	return lastStartandStopTime;
}
public void setLastStartandStopTime(TimeData lastStartandStopTime) {
	this.lastStartandStopTime = lastStartandStopTime;
}
private TimeData lastStartandStopTime;


private Collection<TimeData> timeData = new ArrayList<TimeData>();
public String getInstanceId() {
	return instanceId;
}
public void setInstanceId(String instanceId) {
	this.instanceId = instanceId;
}
public Collection<TimeData> getTimeData() {
	return timeData;
}
public void setTimeData(Collection<TimeData> timeData) {
	this.timeData = timeData;
}
public void setDataPoints(List<DataPoint> dataList) {
	// TODO Auto-generated method stub
	minutesDataPoints = dataList;
	
}


}
