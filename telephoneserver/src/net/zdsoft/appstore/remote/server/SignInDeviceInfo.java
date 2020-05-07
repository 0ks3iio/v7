package net.zdsoft.appstore.remote.server;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:33:54 $
 */
public class SignInDeviceInfo {

    public String deviceId;
    public String versionInfo;
    public int connectStateCount = 0;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public int getConnectStateCount() {
        return connectStateCount;
    }

    public void setConnectStateCount(int connectStateCount) {
        this.connectStateCount = connectStateCount;
    }

}
