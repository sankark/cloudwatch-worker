package com.gto.aws.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.query.MapReduceResult;
import com.basho.riak.client.query.filter.LessThanFilter;
import com.basho.riak.client.query.filter.StringToIntFilter;
import com.basho.riak.client.query.filter.TokenizeFilter;
import com.basho.riak.client.query.functions.NamedErlangFunction;
import com.basho.riak.client.query.functions.NamedJSFunction;
import com.gto.aws.dao.DAOFactory;
import com.gto.aws.model.Cleanup;
import com.gto.aws.model.JobConstants;

public class CleanupService {
public void clean(){
	Iterable<String> keys = DAOFactory.getCleanupDao().listKeys();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DATE, -45);
	long cleanupDate = calendar.getTimeInMillis();
	for (String key : keys) {
		Cleanup cleanupObj = DAOFactory.getCleanupDao().get(key);
		
		try {
			if(sdf.parse(cleanupObj.getLastModifiedDate()).getTime() < cleanupDate){
				if(cleanupObj.getBucket().equalsIgnoreCase(JobConstants.INSATNCE_HISTORY))
					DAOFactory.getInstanceHistoryDao().delete(cleanupObj.getKey());
				if(cleanupObj.getBucket().equalsIgnoreCase(JobConstants.INSTANCEUSAGEDAY))
					DAOFactory.getInstanceUsageDao().delete(cleanupObj.getKey());
				if(cleanupObj.getBucket().equalsIgnoreCase(JobConstants.JOB_REQUESTS))
					DAOFactory.getJobRequestDao().delete(cleanupObj.getKey());
				
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DAOFactory.getCleanupDao().delete(key);
			
	}
}
}
