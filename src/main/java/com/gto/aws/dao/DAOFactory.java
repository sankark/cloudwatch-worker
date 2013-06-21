package com.gto.aws.dao;

import java.util.Date;

import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import com.gto.aws.model.Cleanup;
import com.gto.aws.model.Data;
import com.gto.aws.model.Ec2CpuUtilizationInstance;
import com.gto.aws.model.Ec2CpuUtilizationJob;
import com.gto.aws.model.Group;
import com.gto.aws.model.HourlyData;
import com.gto.aws.model.InstanceHistory;
import com.gto.aws.model.InstanceUsagePerDay;
import com.gto.aws.model.JobConstants;
import com.gto.aws.model.JobRequestInstance;
import static com.basho.riak.client.convert.KeyUtil.getKey;



public class DAOFactory {
 
	
	public interface IJobDetailsDAO extends IGenericDAO<Ec2CpuUtilizationJob> {}
	public interface ICleanupDAO extends IGenericDAO<Cleanup> {}
	public interface IGroupDAO extends IGenericDAO<Group> {}
	public interface IJobRequestDAO extends IGenericDAO<JobRequestInstance> {

		JobRequestInstance updateStatus(String id,String status);}
	public interface IDataDAO extends IGenericDAO<Data> {}
	public interface IInstanceUsagePerDayDAO extends IGenericDAO<InstanceUsagePerDay> {}
	public interface IHourlyDataDAO extends IGenericDAO<HourlyData> {}
	public interface IEc2CpuUtilizationInstanceDAO extends IGenericDAO<Ec2CpuUtilizationInstance> {}
	public interface IEc2CpuUtilizationJobDAO extends IGenericDAO<Ec2CpuUtilizationJob> {}
	public interface IInstanceHistoryDAO extends IGenericDAO<InstanceHistory> {}
	
	public static class GroupDAO extends GenericDAO<Group> implements IGroupDAO {
		public GroupDAO(){
			super(JobConstants.GROUP_BUCKET);
		}

		@Override
		protected void updateCleanupBucket(Group object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
	}
	
	public static class CleanupDAO extends GenericDAO<Cleanup> implements ICleanupDAO {
		public CleanupDAO(){
			super(JobConstants.CLEANUP_BUCKET);
		}

		@Override
		protected void updateCleanupBucket(Cleanup object) {
		}
	}
		
	
	// The use those interfaces as we declare entity-specific DAOs


	
	public static class JobRequestDAO extends GenericDAO<JobRequestInstance>  implements IJobRequestDAO {
		public JobRequestDAO(){
			super(JobConstants.JOB_REQUESTS);
		}

		@Override
		public JobRequestInstance updateStatus(String id, String status) {
			// TODO Auto-generated method stub
			JobRequestInstance obj = this.get(id);
			obj.setStatus(status);
			return obj;
		}
		
		@Override
		protected void updateCleanupBucket(JobRequestInstance object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static class DataDAO extends GenericDAO<Data> implements IDataDAO {
		public DataDAO(){
			super(JobConstants.DATA_POINTS);
		}
		
		@Override
		protected void updateCleanupBucket(Data object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class InstanceUsagePerDayDAO extends GenericDAO<InstanceUsagePerDay> implements IInstanceUsagePerDayDAO {
		public InstanceUsagePerDayDAO(){
			super(JobConstants.INSTANE_USAGE_DAY);
		}
		@Override
		public InstanceUsagePerDay put(InstanceUsagePerDay object){
			InstanceUsagePerDay myObject=null;
			try {
				populateKey(object);
				myObject = bucket.store(object).execute();
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
			return myObject;
		}
		private void populateKey(InstanceUsagePerDay object) {
			object.setId(object.getInstanceId()+'-'+object.getDate());
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void updateCleanupBucket(InstanceUsagePerDay object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			cleanup.populateKey();
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class InstanceHistoryDAO extends GenericDAO<InstanceHistory> implements IInstanceHistoryDAO {
		public InstanceHistoryDAO(){
			super(JobConstants.INSATNCE_HISTORY);
		}
		
		@Override
		protected void updateCleanupBucket(InstanceHistory object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			cleanup.populateKey();
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
	}
	
	public static class HourlyDataDAO extends GenericDAO<HourlyData> implements IHourlyDataDAO {
		public HourlyDataDAO(){
			super(JobConstants.HOURLY_DATA);
		}
		
		@Override
		protected void updateCleanupBucket(HourlyData object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			cleanup.populateKey();
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
	}
	
	
	public static class Ec2CpuUtilizationInstanceDAO extends GenericDAO<Ec2CpuUtilizationInstance> implements IEc2CpuUtilizationInstanceDAO {
		public Ec2CpuUtilizationInstanceDAO(){
			super(JobConstants.JOB_INSTANCES);
		}
		
		@Override
		protected void updateCleanupBucket(Ec2CpuUtilizationInstance object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class Ec2CpuUtilizationJobDAO extends GenericDAO<Ec2CpuUtilizationJob> implements IEc2CpuUtilizationJobDAO {
		public Ec2CpuUtilizationJobDAO(){
			super(JobConstants.JOB_DEFINITION);
		}
		
		@Override
		protected void updateCleanupBucket(Ec2CpuUtilizationJob object) {
			Cleanup cleanup = new Cleanup();
			cleanup.setBucket(bucket.getName());
			cleanup.setKey(getKey(object));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
			cleanup.setLastModifiedDate(sdf.format(new Date()));
			try {
				cleanupBucket.store(cleanup).execute();
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
			// TODO Auto-generated method stub
			
		}
	}
	
	
 
	
	// Static-only usage pattern
	protected DAOFactory() {}
	

	public static IGroupDAO getGroupDao() {
		return new GroupDAO();
	}
	
	public static IInstanceUsagePerDayDAO getInstanceUsageDao() {
		return new InstanceUsagePerDayDAO();
	}
	
	public static ICleanupDAO getCleanupDao() {
		return new CleanupDAO();
	}
		
	public static IJobRequestDAO getJobRequestDao() {
		return new JobRequestDAO();
	}
	
	public static InstanceHistoryDAO getInstanceHistoryDao() {
		return new InstanceHistoryDAO();
	}
	
	public static IDataDAO getDataDao() {
		return new DataDAO();
	}
	
	
	public static IHourlyDataDAO getHourlyDataDao() {
		return new HourlyDataDAO();
	}
	public static IEc2CpuUtilizationInstanceDAO getEc2CpuUtilizationInstanceDao() {
		return new Ec2CpuUtilizationInstanceDAO();
	}
	
	
	

}