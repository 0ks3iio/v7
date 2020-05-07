package net.zdsoft.eclasscard.data.dto;

public class RrpcMsg {
	public static final String QUERY_SYNC_PIC_SCHEDULE = "thing.service.QuerySyncPicSchedule";
	public static final String SYNC_FACE_PICTURES = "thing.service.SyncFacePictures";
	public static final String QUERY_ADDED_USER_INFO = "thing.service.QueryAddedUserInfo";
	private String id;
	private String method;
	private Params params;
	private String version;
	
	public class Params{
		private String GroupID;
		private String FacePicURL;
		public String getGroupID() {
			return GroupID;
		}
		public void setGroupID(String groupID) {
			GroupID = groupID;
		}
		public String getFacePicURL() {
			return FacePicURL;
		}
		public void setFacePicURL(String facePicURL) {
			FacePicURL = facePicURL;
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
