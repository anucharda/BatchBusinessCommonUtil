/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.ais.cpac.cl.batch;

import th.co.ais.cpac.cl.batch.cnf.CNFDatabase;
import th.co.ais.cpac.cl.common.UtilityLogger;
import th.co.ais.cpac.cl.template.database.DBConnectionPools;

/**
 *
 * @author Sirirat
 */
public class ConstantsBusinessUtil {

	public static int suspendOrderActionID = 10;//dummy 
	public static int terminateOrderActionID = 4;
	public static int reconnectOrderID = 12;
	public static int waiveBatchOrderID = 8;
	public static int writeOffOrderID = 9;
	public static String END_LINE = "";
	
	public static String suspendJobType="SD";
	public static String terminateJobType="DT";
	public static String reconnectJobType="RC";
	public static String waiveBatchJobType="WA";
	public static String writeOffJobType ="WO";
	public static String blacklistJobType="BL";
	public static String siebelActivityLogJobType="SA";
	public static String siebelExemptLogJobType="SE";
	public static String ssfWriteOffLogJobType="SW";
	public static String ssfccBlDlLogType="DB";
	public static String ssfccExemptCreditLogType="EC";
	
	public static int batchInprogressStatus = 1;
	public static int batchReceiveStatus = 2;
	public static int batchCompleteStatus = 3;
	
	public static String suspendUsername = "CLSYS";
	public static String terminateUsername = "CLSYS";
	public static String reconnectUsername = "CLSYS";
	public static String waiveBatchUsername = "CLSYS";
	public static String writeOffUsername = "CLSYS";
	public static String blacklistUsername = "CLSYS";
	public static String siebelActivityLogUsername = "CLSYS";
	public static String siebelExemptLogUsername = "CLSYS";
	public static String ssfWriteOffLogUsername = "CLSYS";
	public static String ssfccBlDlLogUsername="CLSYS";
	public static String ssfccExemptCreditLogUsername="CLSYS";
	
	public static int actInprogressStatus = 3;
	public static int actSuccessStatus = 4;
	public static int actIncompleteStatus = 5;
	public static int actFailStatus = 6;
	public static int treatProgressStatus = 3;
	public static int treatSuccessStatus = 4;
	public static int treatIncompleteStatus = 5;
	public static int treatFailStatus = 6;
	public static String adjCompleteStatus = "CP";
	public static String adjFailStatus = "ER";
	public static String PIPE ="\\|";
	public static String sffOKExt =".ok";
	public static String sffErrExt =".err";
	public static String blacklistExt =".dat";
	public static String writeOffSuccess ="S";
	public static String writeOffFail ="E";
	public static String blacklistFailReason="Not Found Result in SFF_BLACKLIST";

	public static DBConnectionPools getDBConnectionPools(UtilityLogger logger) {
		CNFDatabase cnf = new CNFDatabase();
		return new DBConnectionPools<>(cnf, logger);
	}
	  public enum Environment {
		    PROD(1),
		    DEV(2),
		    SIT(3),
		    UnKnow(-9999);
		    private final int code;

		    private Environment(int code) {
		      this.code = code;
		    }

		    public int getCode() {
		      return code;
		    }
		  }

		  public static final Environment mapEnvironment(int code) {
		    if (Environment.PROD.getCode() == code) {
		      return Environment.PROD;
		    } else if (Environment.DEV.getCode() == code) {
		      return Environment.DEV;
		    } else if (Environment.SIT.getCode() == code) {
		      return Environment.SIT;
		    }
		    return Environment.UnKnow;
		  }
}
