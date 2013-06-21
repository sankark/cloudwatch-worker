package com.gto.aws.model;

import com.basho.riak.client.convert.RiakKey;

public class CostJobRequest {
String accessKey;
String secretKey;
String response;
public String getResponse() {
	return response;
}
public void setResponse(String response) {
	this.response = response;
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
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
@RiakKey String id;
}
