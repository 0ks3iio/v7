package net.zdsoft.framework.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Lazy(true)
@Service
public class McodeSetting {
	
	private static McodeSetting mcodeSetting = new McodeSetting();

	public static McodeSetting newInstance() {
		if (mcodeSetting == null) {
			mcodeSetting = new McodeSetting();
		}
		return mcodeSetting;
	}

	private McodeSetting() {

	}

	/**
	 * 根据JqGrid组装的下拉格式
	 * 
	 * @param mcodeId
	 * @return
	 */
	public String getMcodeWithJqGrid(final String mcodeId) {
		List<McodeDetail> mcodes = SUtils.dt(getMcodeRemoteService().findByMcodeIds(mcodeId), new TR<List<McodeDetail>>(){});
		String s = "";
		for (McodeDetail mcode : mcodes) {
			if (StringUtils.isBlank(s)) {
				s = mcode.getThisId() + ":" + mcode.getMcodeContent();
			} else {
				s = s + ";" + mcode.getThisId() + ":" + mcode.getMcodeContent();
			}
		}
		return s;
	}

	/**
	 * 直接获取微代码内容
	 * 
	 * @param mcodeId
	 * @param thisId
	 * @return
	 */
	public String getMcode(final String mcodeId, final String thisId) {
		McodeDetail mcodeDetail = SUtils.dc(getMcodeRemoteService().findByMcodeAndThisId(mcodeId, thisId), McodeDetail.class);
		if (mcodeDetail == null)
			return "";
		else
			return mcodeDetail.getMcodeContent();
	}

	/**
	 * 根据微代码，组装成checkbox代码
	 * 
	 * @param mcodeId
	 * @param thisId
	 * @param name
	 * @return
	 */
	public String getMcodeCheckbox(final String mcodeId, final String thisId, final String name) {
		String[] ts = StringUtils.split(thisId, ",");
		List<McodeDetail> mcodeDetails = SUtils.dt(getMcodeRemoteService().findByMcodeIds(mcodeId), new TypeReference<List<McodeDetail>>(){});
		StringBuffer sb = new StringBuffer();
		for (McodeDetail mcode : mcodeDetails) {
			String content = mcode.getMcodeContent();
			String mcodeThisId = mcode.getThisId();
			sb.append("<label>");
			sb.append("<input type='checkbox' id='").append(name).append("_").append(mcodeThisId).append("' name='")
					.append(name).append("' value='").append(mcode.getThisId()).append("'");
			sb.append(" title='").append(content).append("'");
			if (ArrayUtils.contains(ts, mcodeThisId)) {
				sb.append(" checked");
			}
			sb.append(" class='ace' />");
			sb.append("<span class='lbl'> ").append(content).append("</span></label>");
		}
		return sb.toString();
	}

	/**
	 * 组装select的option数据
	 * 
	 * @param mcodeId
	 * @param thisId
	 * @param containNull
	 * @return
	 */
	public String getMcodeSelect(final String mcodeId, final String thisId, final String containNull) {
		if (StringUtils.isBlank(mcodeId)) {
			return "<option value=''>--- 请选择 ---</option>";
		}
		List<McodeDetail> mcodeDetails;
		if (StringUtils.equals("DM-REGION", mcodeId)) {
			mcodeDetails = new ArrayList<McodeDetail>();
//			List<Region> regions = SUtils.dl(getRegionService().findTop20(), Region.class);
//			for (Region region : regions) {
//				McodeDetail detail = new McodeDetail();
//				detail.setThisId(region.getFullCode());
//				detail.setMcodeContent(region.getFullName());
//				mcodeDetails.add(detail);
//			}
		} else {
			mcodeDetails = SUtils.dt(getMcodeRemoteService().findByMcodeIds(mcodeId), new TypeReference<List<McodeDetail>>(){});
		}

		StringBuffer sb = new StringBuffer();
		if (StringUtils.equals("1", containNull)) {
			sb.append("<option value=''>--- 请选择 ---</option>");
		}
		for (McodeDetail mcode : mcodeDetails) {
			String content = mcode.getMcodeContent();
			String mcodeThisId = mcode.getThisId();
			sb.append("<option value='").append(mcode.getThisId()).append("'");
			sb.append(" title='").append(content).append("'");
			// if (null != mcode.getIsFolder() &&
			// BooleanUtils.toBoolean(mcode.getIsFolder())) {
			// sb.append(" disabled='disabled' style='color:rgb(98, 117,
			// 255);'");
			// }
			if (StringUtils.equals(mcodeThisId, thisId))
				sb.append(" selected");
			sb.append(">").append(content).append("</option>");
		}
		return sb.toString();
	}

	/**
	 * 根据注解中的vsql，组装成select格式
	 * 
	 * @param vsql
	 * @param thisId
	 * @param containNull
	 * @param obj
	 * @return
	 */
	public String getVsqlSelect(String vsql, final String thisId, final String containNull, Map<String, Object> obj) {
		if (StringUtils.isBlank(vsql)) {
			return "<option value=''>--- 请选择 ---</option>";
		}

		List<Object> params = new ArrayList<Object>();
		while (StringUtils.contains(vsql, "{") && StringUtils.contains(vsql, "}")) {
			String p = StringUtils.substringBetween(vsql, "{", "}");
			vsql = StringUtils.replace(vsql, "{" + p + "}", "?");
			Object o = obj.get(p);
			params.add(o == null ? "0" : o);
		}

		//TODO
		/**
		 * 改造过程中，现将这个给去掉， by linqz@20171209
		 */
//		List<String[]> os = SUtils.dl(getMcodeDetailService().findBySql(vsql, params.toArray(new Object[0]));
		List<McodeDetail> mcodeDetails = new ArrayList<McodeDetail>();
//		for (String[] o : os) {
//			McodeDetail detail = new McodeDetail();
//			detail.setThisId(o[0]);
//			detail.setMcodeContent(o[1]);
//			mcodeDetails.add(detail);
//		}

		StringBuffer sb = new StringBuffer();
		if (StringUtils.equals("1", containNull)) {
			sb.append("<option value=''>--- 请选择 ---</option>");
		}
		for (McodeDetail mcode : mcodeDetails) {
			String content = mcode.getMcodeContent();
			String mcodeThisId = mcode.getThisId();
			sb.append("<option value='").append(mcode.getThisId()).append("'");
			sb.append(" title='").append(content).append("'");
			// if (null != mcode.getIsFolder() &&
			// BooleanUtils.toBoolean(mcode.getIsFolder())) {
			// sb.append(" disabled='disabled' style='color:rgb(98, 117,
			// 255);'");
			// }
			if (StringUtils.equals(mcodeThisId, thisId))
				sb.append(" selected");
			sb.append(">").append(content).append("</option>");
		}
		return sb.toString();
	}

	/**
	 * 组装成radio样式
	 * 
	 * @param mcodeId
	 * @param thisId
	 * @param name
	 * @return
	 */
	public String getMcodeRadio(final String mcodeId, final String thisId, final String name) {
		List<McodeDetail> mcodeDetails = SUtils.dt(getMcodeRemoteService().findByMcodeIds(mcodeId), new TypeReference<List<McodeDetail>>(){});
		StringBuffer sb = new StringBuffer();
		for (McodeDetail mcode : mcodeDetails) {
			String content = mcode.getMcodeContent();
			String mcodeThisId = mcode.getThisId();
			sb.append("<label>");
			sb.append("<input type='radio' id='").append(name).append("_").append(mcodeThisId).append("' name='")
					.append(name).append("' value='").append(mcode.getThisId()).append("'");
			sb.append(" title='").append(content).append("'");
			if (StringUtils.equals(mcodeThisId, thisId))
				sb.append(" checked");
			sb.append(" class='ace' />");
			sb.append("<span class='lbl'> ").append(content).append("</span></label>");
		}
		return sb.toString();
	}

	public RegionRemoteService getRegionService() {
		return Evn.getBean("regionRemoteService");
	}

	public McodeRemoteService getMcodeRemoteService() {
		return Evn.getBean("mcodeRemoteService");
	}
}
