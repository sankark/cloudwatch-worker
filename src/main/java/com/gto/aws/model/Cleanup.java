package com.gto.aws.model;

import com.basho.riak.client.convert.RiakKey;

public class Cleanup {
@RiakKey private String id;
public String getId() {
	return bucket+"-"+key;
}
public void setId(String id) {
	this.id = id;
}
private String bucket;
public String getBucket() {
	return bucket;
}
public void setBucket(String bucket) {
	this.bucket = bucket;
}
public String getLastModifiedDate() {
	return lastModifiedDate;
}
public void setLastModifiedDate(String lastModifiedDate) {
	this.lastModifiedDate = lastModifiedDate;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}

public void populateKey() {
	setId(bucket+"-"+key);
}
private String lastModifiedDate;
private String key;
}
