package net.zdsoft.eclasscard.data.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Create by jhon.zh on 2018/6/20
 *
 * @author: jhon.zh
 * @date: Created 2018/6/20
 */

public class FaceGroupDTO implements Serializable {
    private static final long serialVersionUID = -8852471737100494517L;
    private String groupId;
    private List<FaceInfo> currentFaceInfos;
    private List<FaceInfo> failedFaceInfos;
    private Boolean isCleanAll;
    private long lastLowerTime;

    public class  FaceInfo{
        /**
         * faceId
         * 人脸Id
         */
        String faceId;
        /**
         * 学生名
         */
        String faceName;
        /**
         * 是否更新
         */
        Boolean isUpdate;

        /**
         * clientTag
         * 由clientId给这个材料所打的TAG。
         * 后续可以用来查询，删除，replace。
         * 可以为null
         * 如果TAG不为null， 那么照片的分类Category 相当于为 "face|<clientTAG>"
         * clientTag必须为ASCII字符，而且长度必须<= 6
         */
        String clientTag;

        /**
         * index
         * 相同clientTag下的多张图片的序号
         */
        Integer index;
        /**
         * faceUrl
         * 图片Url,有有效期
         */
        String faceUrl;
        /**
         * faceMd5
         * 图片的MD5值
         */
        String faceMd5;

        /**
         * userInfo 用户信息
         */
        String userInfo;

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

        public String getFaceName() {
			return faceName;
		}

		public void setFaceName(String faceName) {
			this.faceName = faceName;
		}

		public Boolean getIsUpdate() {
			return isUpdate;
		}

		public void setIsUpdate(Boolean isUpdate) {
			this.isUpdate = isUpdate;
		}

		public String getClientTag() {
            return clientTag;
        }

        public void setClientTag(String clientTag) {
            this.clientTag = clientTag;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }

        public String getFaceMd5() {
            return faceMd5;
        }

        public void setFaceMd5(String faceMd5) {
            this.faceMd5 = faceMd5;
        }

        public String generateFaceKey(){
            String faceKey = faceId + "_" + clientTag + "_" + index;
            return faceKey;
        }

        public String getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(String userInfo) {
			this.userInfo = userInfo;
		}

		@Override
        public String toString() {
            return "FaceInfo{" +
                    "faceId='" + faceId + '\'' +
                    ", clientTag='" + clientTag + '\'' +
                    ", index=" + index +
                    ", faceUrl='" + faceUrl + '\'' +
                    ", faceMd5='" + faceMd5 + '\'' +
                    '}';
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<FaceInfo> getCurrentFaceInfos() {
        return currentFaceInfos;
    }

    public void setCurrentFaceInfos(
            List<FaceInfo> currentFaceInfos) {
        this.currentFaceInfos = currentFaceInfos;
    }

    public List<FaceInfo> getFailedFaceInfos() {
        return failedFaceInfos;
    }

    public void setFailedFaceInfos(
            List<FaceInfo> failedFaceInfos) {
        this.failedFaceInfos = failedFaceInfos;
    }

    public Boolean getIsCleanAll() {
        return isCleanAll;
    }

    public void setIsCleanAll(Boolean isCleanAll) {
        this.isCleanAll = isCleanAll;
    }

    public void setLastLowerTime(long lastLowerTime) {
		this.lastLowerTime = lastLowerTime;
	}

	public long getLastLowerTime() {
		return lastLowerTime;
	}

	@Override
    public String toString() {
        return "FaceGroupDTO{" +
                "groupId='" + groupId + '\'' +
                ", currentFaceInfos=" + currentFaceInfos +
                ", isCleanAll=" + isCleanAll +
                '}';
    }
}

