package com.gto.aws.model;

public class JobRequest {
	
	String jobRequestId;
	String instanceId;
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
	String date;
	public String getJobRequestId() {
		return jobRequestId;
	}
	public void setJobRequestId(String jobRequestId) {
		this.jobRequestId = jobRequestId;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	String response;

}
