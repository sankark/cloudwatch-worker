package com.gto.aws.model;

public class CpuHeatMap {
private String date;
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getAvg() {
	return avg;
}
public void setAvg(String avg) {
	this.avg = avg;
}
public String getHour() {
	return hour;
}
public void setHour(String hour) {
	this.hour = hour;
}
private String avg;
private String hour;
}
