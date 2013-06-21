package com.gto.aws.model;

import com.basho.riak.client.convert.RiakKey;

public class Group {
String groupName;
@RiakKey String accessKey;
public String getGroupName() {
	return groupName;
}
public void setGroupName(String groupName) {
	this.groupName = groupName;
}
public String getAccessKey() {
	return accessKey;
}
public void setAccessKey(String accessKey) {
	this.accessKey = accessKey;
}
public String getSecretKey() {
	return secretKey;
}
public void setSecretKey(String secretKey) {
	this.secretKey = secretKey;
}
String secretKey;
}
