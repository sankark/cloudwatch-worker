package com.gto.aws.model;

import com.basho.riak.client.convert.RiakKey;

public class InstanceUsagePerDay {
@RiakKey String id;
String instanceId;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getInstanceId() {
	return instanceId;
}
public void setInstanceId(String instanceId) {
	this.instanceId = instanceId;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public long getUsageMinutes() {
	return usageInMinutes;
}
public void setUsageMinutes(long usageInMinutes2) {
	this.usageInMinutes = usageInMinutes2;
}
String date;
long usageInMinutes;
}
