package th.co.ais.cpac.cl.batch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import th.co.ais.cpac.cl.batch.ConstantsBusinessUtil;

public class Utility {
	public static String getusername(String jobType) {
		if (ConstantsBusinessUtil.suspendJobType.equals(jobType)) {
			return ConstantsBusinessUtil.suspendUsername;
		} else if (ConstantsBusinessUtil.terminateJobType.equals(jobType)) {
			return ConstantsBusinessUtil.terminateUsername;
		} else if (ConstantsBusinessUtil.reconnectJobType.equals(jobType)) {
			return ConstantsBusinessUtil.reconnectUsername;
		} else if (ConstantsBusinessUtil.waiveBatchJobType.equals(jobType)) {
			return ConstantsBusinessUtil.waiveBatchUsername;
		} else if (ConstantsBusinessUtil.writeOffJobType.equals(jobType)) {
			return ConstantsBusinessUtil.writeOffUsername;
		} else if (ConstantsBusinessUtil.blacklistJobType.equals(jobType)) {
			return ConstantsBusinessUtil.blacklistUsername;
		} else {
			return "undefined";
		}
	}

	public static int getActionID(String jobType) {
		if (ConstantsBusinessUtil.suspendJobType.equals(jobType)) {
			return ConstantsBusinessUtil.suspendOrderActionID;
		} else if (ConstantsBusinessUtil.terminateJobType.equals(jobType)) {
			return ConstantsBusinessUtil.terminateOrderActionID;
		} else if (ConstantsBusinessUtil.reconnectJobType.equals(jobType)) {
			return ConstantsBusinessUtil.reconnectOrderID;
		} else if (ConstantsBusinessUtil.waiveBatchJobType.equals(jobType)) {
			return ConstantsBusinessUtil.waiveBatchOrderID;
		} else if (ConstantsBusinessUtil.writeOffJobType.equals(jobType)) {
			return ConstantsBusinessUtil.writeOffOrderID;
		} else {
			return 0;
		}
	}

	public static String getJobName(String jobType) {
		if (ConstantsBusinessUtil.suspendJobType.equals(jobType)) {
			return "Suspend Job";
		} else if (ConstantsBusinessUtil.terminateJobType.equals(jobType)) {
			return "Terminate Job";
		} else if (ConstantsBusinessUtil.reconnectJobType.equals(jobType)) {
			return "Reconnect Job";
		} else if (ConstantsBusinessUtil.waiveBatchJobType.equals(jobType)) {
			return "Waive Batch Job";
		} else if (ConstantsBusinessUtil.writeOffJobType.equals(jobType)) {
			return "Write Off Job";
		} else {
			return "Other Job Undefine";
		}
	}

	public static Date convertStringToDate(String dateStr) {
		Date date;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException("Failed to parse date: ", e);
		}
		return date;
	}

}
