package th.co.ais.cpac.cl.batch.db;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import th.co.ais.cpac.cl.batch.Constants;
import th.co.ais.cpac.cl.common.UtilityLogger;
import th.co.ais.cpac.cl.template.database.DBConnectionPools;
import th.co.ais.cpac.cl.template.database.DBTemplatesExecuteQuery;
import th.co.ais.cpac.cl.template.database.DBTemplatesResponse;
import th.co.ais.cpac.cl.template.database.DBTemplatesUpdate;

/**
 *
 * @author Sirirat
 */
public class CLOrder {

	protected final UtilityLogger logger;

	public CLOrder(UtilityLogger logger) {
		this.logger = logger;
	}

	public class ExecuteResponse extends DBTemplatesResponse<Boolean> {

		@Override
		protected Boolean createResponse() {
			return false;
		}

		@Override
		public void setResponse(Boolean boo) {
			response = boo;
		}
	}

	public class CLOrderTreatementInfo {
		protected CLOrderTreatementInfo() {
		}

		private BigDecimal treatementId;
		private BigDecimal orderId;
		private BigDecimal batchId;
		private String baNumber;
		private String mobileNumber;
		private int actStatus;

		public BigDecimal getTreatementId() {
			return treatementId;
		}

		public void setTreatementId(BigDecimal treatementId) {
			this.treatementId = treatementId;
		}

		public BigDecimal getOrderId() {
			return orderId;
		}

		public void setOrderId(BigDecimal orderId) {
			this.orderId = orderId;
		}

		public String getBaNumber() {
			return baNumber;
		}

		public void setBaNumber(String baNumber) {
			this.baNumber = baNumber;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}

		public BigDecimal getBatchId() {
			return batchId;
		}

		public void setBatchId(BigDecimal batchId) {
			this.batchId = batchId;
		}

		public int getActStatus() {
			return actStatus;
		}

		public void setActStatus(int actStatus) {
			this.actStatus = actStatus;
		}

	}

	public class CLOrderInfoResponse extends DBTemplatesResponse<ArrayList<CLOrderTreatementInfo>> {

		@Override
		protected ArrayList<CLOrderTreatementInfo> createResponse() {
			return new ArrayList<>();
		}

	}

	protected class GetOrderTreatementInfoByMobileAndAction
			extends DBTemplatesExecuteQuery<CLOrderInfoResponse, UtilityLogger, DBConnectionPools> {
		private String mobileNo;
		private int orderActionID;

		public GetOrderTreatementInfoByMobileAndAction(UtilityLogger logger) {
			super(logger);
		}

		@Override
		protected CLOrderInfoResponse createResponse() {
			return new CLOrderInfoResponse();
		}

		@Override
		protected StringBuilder createSqlProcess() {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT").append(Constants.END_LINE);
			sql.append(" a.ORDER_ID,BA_NO,MOBILE_NO,TREATMENT_ID,BATCH_ID ").append(Constants.END_LINE);
			sql.append(" FROM CL_ORDER_TREATMENT a ").append(Constants.END_LINE);
			sql.append(" INNER JOIN dbo.CL_ORDER b ").append(Constants.END_LINE);
			sql.append(" ON a.ORDER_ID=b.ORDER_ID ").append(Constants.END_LINE);
			sql.append(" WHERE MOBILE_NO = ('").append(mobileNo).append("') ").append(Constants.END_LINE);
			sql.append(" and ORDER_ACTION_ID = (").append(orderActionID).append(")").append(Constants.END_LINE);
			sql.append(" and ACTION_STATUS = (").append(Constants.suspendInprogressStatus).append(")")
					.append(Constants.END_LINE);
			return sql;
		}

		@Override
		protected void setReturnValue(ResultSet resultSet) throws SQLException {
			CLOrderTreatementInfo temp = new CLOrderTreatementInfo();
			temp.setBaNumber(resultSet.getString("BA_NO"));
			temp.setOrderId(resultSet.getBigDecimal("ORDER_ID"));
			temp.setMobileNumber(resultSet.getString("MOBILE_NO"));
			temp.setTreatementId(resultSet.getBigDecimal("TREATMENT_ID"));
			temp.setBatchId(resultSet.getBigDecimal("BATCH_ID"));
			response.getResponse().add(temp);
		}

		protected CLOrderInfoResponse execute(String mobileNo, int orderActionID) {
			this.mobileNo = mobileNo;
			this.orderActionID = orderActionID;
			return executeQuery(Constants.getDBConnectionPools(logger), true);
		}
	}

	public CLOrderTreatementInfo getOrderTreatementInfo(String mobileNo, int orderActionID) {
		CLOrderTreatementInfo orderInfo = null;
		CLOrderInfoResponse response = new GetOrderTreatementInfoByMobileAndAction(logger).execute(mobileNo,
				orderActionID);
		if (response.getResponse() != null && response.getResponse().size() > 0) {
			orderInfo = response.getResponse().get(0);
		}
		return orderInfo;
	}

	protected class UpdateOrderStatus extends DBTemplatesUpdate<ExecuteResponse, UtilityLogger, DBConnectionPools> {
		BigDecimal orderID;
		int actionStatus;
		String sffOrderNo;
		String failReason;
		String updateBy;

		public UpdateOrderStatus(UtilityLogger logger) {
			super(logger);
		}

		@Override
		protected StringBuilder createSqlProcess() {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE dbo.CL_ORDER ").append(Constants.END_LINE);
			sql.append("SET LAST_UPD= getdate() , LAST_UPD_BY='").append(updateBy).append("'")
					.append(Constants.END_LINE);
			sql.append(",ACTION_STATUS = ").append(actionStatus).append(Constants.END_LINE);
			sql.append(", ACTION_STATUS_DTM = getdate() ").append(Constants.END_LINE);
			sql.append(",SFF_ORDER_NO = '").append(sffOrderNo).append("'").append(Constants.END_LINE);
			if (failReason != null) {
				sql.append(", ACTION_REMARK = '").append(failReason).append("'").append(Constants.END_LINE);
			}
			sql.append(" WHERE ORDER_ID = ").append(orderID).append(Constants.END_LINE);
			return sql;
		}

		@Override
		protected ExecuteResponse createResponse() {
			return new ExecuteResponse();
		}

		protected ExecuteResponse execute(BigDecimal orderID, int actionStatus, String sffOrderNo, String failReason,
				String updateBy) {
			this.orderID = orderID;
			this.actionStatus = actionStatus;
			this.sffOrderNo = sffOrderNo;
			this.updateBy = updateBy;
			return executeUpdate(Constants.getDBConnectionPools(logger), true); // case
																				// adjust
																				// false;
		}
	}

	public ExecuteResponse updateOrderStatus(BigDecimal orderID, int actionStatus, String sffOrderNo, String failReason,
			String updateBy) {
		return new UpdateOrderStatus(logger).execute(orderID, actionStatus, sffOrderNo, failReason, updateBy);
	}

	protected class GetOrderTreatementInfoByTreatment
			extends DBTemplatesExecuteQuery<CLOrderInfoResponse, UtilityLogger, DBConnectionPools> {
	
		private BigDecimal treatmentID;

		public GetOrderTreatementInfoByTreatment(UtilityLogger logger) {
			super(logger);
		}

		@Override
		protected CLOrderInfoResponse createResponse() {
			return new CLOrderInfoResponse();
		}

		@Override
		protected StringBuilder createSqlProcess() {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT").append(Constants.END_LINE);
			sql.append(" TREATMENT_ID,MOBILE_NO,BA_NO,BATCH_ID ").append(Constants.END_LINE);
			sql.append(" FROM CL_ORDER_TREATMENT a ").append(Constants.END_LINE);
			sql.append(" INNER JOIN dbo.CL_ORDER b ").append(Constants.END_LINE);
			sql.append(" ON a.ORDER_ID=b.ORDER_ID ").append(Constants.END_LINE);
			sql.append(" WHERE ORDER_ACTION_ID = (").append(treatmentID).append(")").append(Constants.END_LINE);
			return sql;
		}

		@Override
		protected void setReturnValue(ResultSet resultSet) throws SQLException {
			CLOrderTreatementInfo temp = new CLOrderTreatementInfo();
			temp.setTreatementId(resultSet.getBigDecimal("TREATMENT_ID"));
			temp.setMobileNumber(resultSet.getString("MOBILE_NO"));
			temp.setBaNumber(resultSet.getString("BA_NO"));
			temp.setBatchId(resultSet.getBigDecimal("BATCH_ID"));
			response.getResponse().add(temp);
		}

		protected CLOrderInfoResponse execute(BigDecimal treatmentID) {
			this.treatmentID = treatmentID;
			return executeQuery(Constants.getDBConnectionPools(logger), true);
		}
	}
	
	public CLOrderInfoResponse getOrderTreatementInfoByTreatmentID(BigDecimal treatmentID) {
		CLOrderInfoResponse orderList = null;
		orderList = new GetOrderTreatementInfoByTreatment(logger).execute(treatmentID);
		return orderList;
	}

}
