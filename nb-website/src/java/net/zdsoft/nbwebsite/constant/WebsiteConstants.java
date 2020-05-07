/*
* Project: v7
* Author : shenke
* @(#) WebsiteConstants.java Created on 2016-10-10
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.nbwebsite.constant;


/**
 * @description: 常量
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-10上午9:29:29
 */
public final class WebsiteConstants {

	/** 文章類型微代碼*/
	public static final String TYPE_MCODE_ID = "DM-WZWZLX";

	public static final String REGION_CODE = "3302";

	/** 审核员*/
	public static final String MANAGER_ROLE_CODE = "web_article_auditor";
	
	//public static final String TYPE_BULLETIN = "00";
	//public static final String TYPE_BULLETIN = "02";
	//public static final String TYPE_BULLETIN = "03";
	//public static final String TYPE_BULLETIN = "04";
	//public static final String TYPE_BULLETIN = "05";
	//public static final String TYPE_BULLETIN = "06";
	//public static final String TYPE_BULLETIN = "07";
	//public static final String TYPE_BULLETIN = "08";
	
	
	/** 未提交*/
	public static final String STATE_UNCOMMIT = "0";
	/** 已提交（未审核）*/
	public static final String STATE_COMMITED = "1";
	/** 已通过*/
	public static final String STATE_PASSED = "2";
	/** 修改通过(该字段未使用)*/
	@Deprecated
	public static final String STATE_EDIT_PASSED = "3";
	/** 未通过*/
	public static final String STATE_UNPASS = "4";
	/** 退回*/
	public static final String STATE_RETURN = "5";
    /** 撤回*/
	public static final String STATE_CANCEL = "6";

	/** 置顶*/
	public static final String TOP_TRUE = "1";
	/** 取消置顶*/
	public static final String TOP_FALSE = "0";

	public static final String SYSTEM_SYSTEM_ID = "99";
	public static final String SYSTEM_SITEDATA_ID = "7";
	public static final String SYSTEM_SYSTEM_CODE = "eis_basedata";
	
	/**
	 * 类型
	 * @author shenke
	 *
	 */
	public static enum Type{
		
		/** 试验区介绍*/
		IEAREA("00","试验区介绍"),
		/** 公告通知*/
		BULLETIN("01","公告通知"),
		/** 学校经验*/
		SCHOOL_EXPERIENCE("02","学校经验"),
		/** 成果展示*/
		RESULT_SHOW("03","成果展示"),
		/** 媒体聚焦*/
		MEDIA_FOCUS("04","媒体聚焦"),
		/** 友情链接*/
		FRENDLY_LINKS("05","友情链接"),
		/** 区域直通车*/
		REGIONAL_TRAIN("06","区域直通车"),
		/** 实验区动态*/
		DYNAMIC_EAREA("07","实验区动态"),
		/** 实验区特色项目*/
		EAREA_PROJECT("08","实验区特色项目"),
		/** 图文翻动*/
		PICTURE_FLIP("11","图文翻动"),
		/** 区域直通图片设置*/
		REGIONAL_PICTURE("10","区域直通图片设置"),
		/** 实验区简介维护 */
		TEST_MAINTAIN("12","实验区简介维护"),
		;
	
		
		private String thisId;
		private String content;

		private Type(String thisId, String content){
			this.thisId = thisId;
			this.content = content;
		}
		
		public String getThisId(){
			return thisId;
		}

		public String getContent(){
			return this.content;
		}

		public String getType(){
			return getThisId();
		}
		
		public static Type valueOfThisid(String thisId) throws Exception{
			if("00".equals(thisId)){
				return Type.IEAREA;
			}
			else if("01".equals(thisId)){
				return Type.BULLETIN;
			}
			else if("02".equals(thisId)){
				return Type.SCHOOL_EXPERIENCE;
			}
			else if("03".equals(thisId)){
				return Type.RESULT_SHOW;
			}
			else if("04".equals(thisId)){
				return Type.MEDIA_FOCUS;
			}
			else if("05".equals(thisId)){
				return Type.FRENDLY_LINKS;
			}
			else if("06".equals(thisId)){
				return Type.REGIONAL_TRAIN;
			}
			else if("07".equals(thisId)){
				return Type.DYNAMIC_EAREA;
			}
			else if("08".equals(thisId)){
				return Type.EAREA_PROJECT;
			}
			else if("09".equals(thisId)){
				return Type.PICTURE_FLIP;
			}
			else if("10".equals(thisId)){
				return Type.REGIONAL_PICTURE;
			}
			else if("12".equals(thisId)){
				return Type.TEST_MAINTAIN;
			}
			else{
				throw new IllegalArgumentException("不合法的thisId");
			}
		}
	}
}
