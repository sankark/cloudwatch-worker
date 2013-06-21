package com.gto.aws.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;


public final class RiakFactory {
	private static RiakFactory instance = new RiakFactory();
	private static IRiakClient riakClient;
	
	private RiakFactory(){
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/spring/riak.properties");
		Properties prop = new Properties();
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String riakHost = prop.getProperty("riak.host");
		int riakPort = Integer.parseInt(prop.getProperty("riak.port"));
		try {
			riakClient = com.basho.riak.client.RiakFactory.pbcClient(riakHost, riakPort);
		} catch (RiakException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static IRiakClient getRiakClient(){
		if(riakClient == null)
			instance = new RiakFactory();
		return riakClient;
	}

}
