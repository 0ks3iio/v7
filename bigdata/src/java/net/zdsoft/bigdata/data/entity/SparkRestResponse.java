package net.zdsoft.bigdata.data.entity;

/**
 * Created by wangdongdong on 2018/11/1 10:30.
 */
public class SparkRestResponse {

    private String serverSparkVersion;
    private String workerId;
    private String submissionId;
    private String success;
    private String action;
    private String driverState;
    private String message;

    public static SparkRestResponse error() {
        SparkRestResponse response = new SparkRestResponse();
        response.setSuccess("false");
        response.setDriverState("NULL");
        return response;
    }

    public String getServerSparkVersion() {
        return serverSparkVersion;
    }

    public void setServerSparkVersion(String serverSparkVersion) {
        this.serverSparkVersion = serverSparkVersion;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getDriverState() {
        return driverState;
    }

    public void setDriverState(String driverState) {
        this.driverState = driverState;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
