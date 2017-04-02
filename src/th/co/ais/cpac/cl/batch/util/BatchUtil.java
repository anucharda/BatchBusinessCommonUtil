package th.co.ais.cpac.cl.batch.util;

import java.math.BigDecimal;

import th.co.ais.cpac.cl.batch.ConstantsBusinessUtil;

public class BatchUtil {

	
	public static  BigDecimal getBatchTypeId(String jobType) throws Exception{
		BigDecimal batchTypeId=new BigDecimal("-99");
		PropertiesReader reader = new PropertiesReader("th.co.ais.cpac.cl.batch.properties.resource","SystemConfigPath");
		if(ConstantsBusinessUtil.suspendJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("suspend.batchTypeID"));
		}else if(ConstantsBusinessUtil.terminateJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("terminate.batchTypeID"));
		}else if(ConstantsBusinessUtil.reconnectJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("reconnect.batchTypeID"));
		}else if(ConstantsBusinessUtil.waiveBatchJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("waiveBatch.batchTypeID"));
		}else if(ConstantsBusinessUtil.writeOffJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("writeOff.batchTypeID"));
		}else if(ConstantsBusinessUtil.blacklistJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("blacklist.batchTypeID"));
		}
		else if(ConstantsBusinessUtil.siebelActivityLogJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("sb.activity.log.batchTypeID"));
		}
		else if(ConstantsBusinessUtil.siebelExemptLogJobType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("sb.exempt.log.batchTypeID"));
		}
		else if(ConstantsBusinessUtil.ssfccBlDlLogType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("ssfcc.exmpt.bldl.log.batchTypeID"));
		}
		else if(ConstantsBusinessUtil.ssfccExemptCreditLogType.equals(jobType)){
			batchTypeId= new BigDecimal(reader.get("ssfcc.exempt.credit.limit.log.batchTypeID"));
		}
		if(batchTypeId==new BigDecimal("-99")){
			throw new Exception("Error Cannon Get Batch Type ID --> "+batchTypeId);
		}
		return batchTypeId;
	}
	public static int getEnvionment()  throws Exception{
		PropertiesReader reader = new PropertiesReader("th.co.ais.cpac.cl.batch.properties.resource","SystemConfigPath");
		return Integer.parseInt(reader.get("env"));
	}
}
