package net.zdsoft.datacollection.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.datacollection.JiangBei.entity.JiangBeiDataReport;
import net.zdsoft.datacollection.JiangBei.service.JiangBeiDataReportService;
import net.zdsoft.datacollection.entity.DcProject;
import net.zdsoft.datacollection.entity.DcProjectColumn;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.ExcelStat;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.MathUtils;
import net.zdsoft.framework.utils.POIUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

@Controller
@RequestMapping("/dc")
public class DataCollectionAction extends BaseAction {

	// @Autowired
	// private DcProjectService dcProjectService;
	//
	// @Autowired
	// private DcProjectColumnService dcProjectColumnService;

	@Autowired
	private UnitRemoteService unitRemoteService;

	@Autowired
	private TeacherRemoteService teacherRemoteService;

	@Autowired
	private UserRemoteService userRemoteService;

	@Autowired
	private JiangBeiDataReportService jiangBeiDataReportService;

	@Autowired
	private SystemIniRemoteService systemIniRemoteService;

	private static float round(String value) {
		double f = NumberUtils.toFloat(value);
		long l = Math.round(f * 10);
		return l * 0.1f;
	}

	private float round(float f) {
		long l = Math.round(f * 10);
		return l * 0.1f;
	}

	public static void main(String[] args) throws Exception {
		// ----------------------解析并保存模板-----------------------
		String rootPath = "c:\\doc\\2017";
		File root = new File(rootPath);
		for (File f : root.listFiles()) {
			String filePath = f.getPath();
			try {
				String htmlStr = POIUtils.excelToHtml(filePath, null);
				String head = StringUtils.substring(htmlStr, 0, htmlStr.indexOf("<h2>"));
				List<String> sheetNames = Arrays.asList("幼儿园信息", "教师信息", "享受补助", "缴纳社保补助", "后勤人员补助");
				int sheetIndex = 0;
				Map<String, String> map = new HashMap<String, String>();
				Map<String, String> registerMap = new HashMap<String, String>();
				for (String sheetName : sheetNames) {
					sheetIndex++;
					String outFileName = sheetName + ".html";
					String id = DigestUtils.md5Hex("2016浙江宁波江北区上半年幼儿园-" + sheetName);

					// DcProject project = dcProjectService.findOne(id);
					DcProject project = null;
					if (project == null) {
						project = new DcProject();
						project.setId(id);
						project.setDataStartIndex(4);
						project.setTitleIndex(3);
						project.setTemplatePath("");
						project.setProjectName("2016浙江宁波江北区上半年幼儿园-" + sheetName);
						project.setDataEndIndex(4);
					}
					syncExcel(registerMap, map, project, htmlStr, head, outFileName, sheetIndex);
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				System.out.println(filePath);
			}
		}
	}

	@RequestMapping("/initStatExcel")
	public void initStatExcel() {

		try {
			String filePath = "c:\\doc\\hz.xls";
			String htmlStr = POIUtils.excelToHtml(filePath, null);
			String head = StringUtils.substring(htmlStr, 0, htmlStr.indexOf("<h2>"));

			JSONArray jsons = new JSONArray();
			List<JiangBeiDataReport> jbdrs = jiangBeiDataReportService.findAll();
			jbdrs.addAll(jbdrs.subList(0, jbdrs.size()));
			for (JiangBeiDataReport jbdr : jbdrs) {
				if (StringUtils.equals(BaseConstants.ZERO_GUID, jbdr.getUnitId()))
					continue;

				JSONObject json = new JSONObject();
				String id = DigestUtils.md5Hex("2016浙江宁波江北区上半年幼儿园-幼儿园信息");
				List<DcProjectColumn> projectColumns = RedisUtils.getObject(
						"jbreport.dcprojectcolumn." + id + "_" + jbdr.getUnitId(),
						new TypeReference<List<DcProjectColumn>>() {
						});
				// List<DcProjectColumn> projectColumns = dcProjectColumnService
				// .findListBy(new String[] { "projectId", "unitId" }, new
				// String[] { id, jbdr.getUnitId() });
				SortUtils.ASC(projectColumns, "rowIndex");
				for (DcProjectColumn dpc : projectColumns) {
					Integer isData = dpc.getIsDataRow();
					if (isData == 1) {
						json.put("column3", dpc.getColumn3());
						json.put("column4", dpc.getColumn7());
						json.put("column5", dpc.getColumn8());
						json.put("column6", dpc.getColumn9());
					}
				}

				id = DigestUtils.md5Hex("2016浙江宁波江北区上半年幼儿园-享受补助");
				projectColumns = RedisUtils.getObject("jbreport.dcprojectcolumn." + id + "_" + jbdr.getUnitId(),
						new TypeReference<List<DcProjectColumn>>() {
						});
				// projectColumns = dcProjectColumnService.findListBy(new
				// String[] { "projectId", "unitId" },
				// new String[] { id, jbdr.getUnitId() });
				SortUtils.ASC(projectColumns, "rowIndex");
				int gwCount = 0, zcCount = 0, qlCount = 0;
				float gwSum = 0, zcSum = 0, qlSum = 0;
				for (DcProjectColumn p : projectColumns) {
					float gw = round(p.getColumn12());
					if (gw > 0) {
						gwCount++;
						gwSum += gw * round(p.getColumn13());
					}
					float zc = round(p.getColumn14());
					if (zc > 0) {
						zcCount++;
						zcSum += zc * round(p.getColumn15());
					}
					float ql = round(p.getColumn16());
					if (ql > 0) {
						qlCount++;
						qlSum += ql * round(p.getColumn17());
					}
				}
				json.put("column7", gwCount);
				json.put("column8", round(gwSum));
				json.put("column9", zcCount);
				json.put("column10", round(zcSum));
				json.put("column11", qlCount);
				json.put("column12", round(qlSum));

				id = DigestUtils.md5Hex("2016浙江宁波江北区上半年幼儿园-缴纳社保补助");
				// projectColumns = dcProjectColumnService.findListBy(new
				// String[] { "projectId", "unitId" },
				// new String[] { id, jbdr.getUnitId() });
				projectColumns = RedisUtils.getObject("jbreport.dcprojectcolumn." + id + "_" + jbdr.getUnitId(),
						new TypeReference<List<DcProjectColumn>>() {
						});

				SortUtils.ASC(projectColumns, "rowIndex");
				int sbCount = 0;
				float sbSum = 0;
				for (DcProjectColumn p : projectColumns) {
					float sb = round(p.getColumn11());
					if (sb > 0) {
						sbCount++;
						sbSum += sb;
					}
				}
				json.put("column13", sbCount);
				json.put("column14", round(sbSum));

				id = DigestUtils.md5Hex("2016浙江宁波江北区上半年幼儿园-后勤人员补助");
				// projectColumns = dcProjectColumnService.findListBy(new
				// String[] { "projectId", "unitId" },
				// new String[] { id, jbdr.getUnitId() });
				projectColumns = RedisUtils.getObject("jbreport.dcprojectcolumn." + id + "_" + jbdr.getUnitId(),
						new TypeReference<List<DcProjectColumn>>() {
						});

				SortUtils.ASC(projectColumns, "rowIndex");
				int hqCount = 0;
				float hqSum = 0;
				for (DcProjectColumn p : projectColumns) {
					float hq = round(p.getColumn10());
					if (hq > 0) {
						hqCount++;
						hqSum += hq;
					}
				}
				json.put("column15", hqCount);
				json.put("column16", hqSum);

				json.put("column17",
						round(json.getString("column8")) + round(json.getString("column10"))
								+ round(json.getString("column12")) + round(json.getString("column14"))
								+ round(json.getString("column16")));

				System.out.println(json.toJSONString());

				jsons.add(json);
			}
			DcProject project = new DcProject();
			project.setDataEndIndex(2 + jsons.size());
			project.setDataStartIndex(3);
			project.setTitleIndex(2);
			syncStatExcel(jsons, project, htmlStr, head, "hz.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/initExcel")
	public void initExcel() {
		String pathPrefix = systemIniRemoteService.findValue(SystemIni.FILE_PATH) + "/store/";
		try {
			List<JiangBeiDataReport> jbdrs = jiangBeiDataReportService.findAll();
			for (JiangBeiDataReport jbdr : jbdrs) {
				if (StringUtils.equals(BaseConstants.ZERO_GUID, jbdr.getUnitId()))
					continue;
				// ----------------------解析并保存模板-----------------------
				String filePath = pathPrefix + jbdr.getFilePath();
				filePath = "c:\\doc\\zq.xls";
				String htmlStr = POIUtils.excelToHtml(filePath, null);
				String head = StringUtils.substring(htmlStr, 0, htmlStr.indexOf("<h2>"));
				List<String> sheetNames = Arrays.asList("幼儿园信息", "教师信息", "享受补助", "缴纳社保补助", "后勤人员补助");
				int sheetIndex = 0;
				Map<String, String> map = new HashMap<String, String>();
				Map<String, String> registerMap = new HashMap<String, String>();
				map.put("unitId", jbdr.getUnitId());
				map.put("userId", jbdr.getUserId());
				for (String sheetName : sheetNames) {
					sheetIndex++;
					String outFileName = sheetName + ".html";
					String id = DigestUtils.md5Hex("2016浙江宁波江北区上半年幼儿园-" + sheetName);

					// DcProject project = dcProjectService.findOne(id);
					DcProject project = null;
					if (project == null) {
						project = new DcProject();
						project.setId(id);
						project.setDataStartIndex(4);
						project.setTitleIndex(3);
						project.setTemplatePath("");
						project.setProjectName("2016浙江宁波江北区上半年幼儿园-" + sheetName);
						project.setDataEndIndex(4);
					}
					// dcProjectColumnService.delete(project.getId(),
					// jbdr.getUnitId());
					syncExcel(registerMap, map, project, htmlStr, head, outFileName, sheetIndex);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> syncExcel(Map<String, String> registerValueMap, Map<String, String> map,
			DcProject project, String htmlStr, String head, String outFileName, int sheetIndex)
			throws IOException, UnsupportedEncodingException {

		Unit unit = null;
		User user = null;
		Teacher teacher = null;

		for (int i = 0; i < sheetIndex; i++) {
			htmlStr = StringUtils.substringAfter(htmlStr, "</h2>");
		}
		htmlStr = head + StringUtils.substringBefore(htmlStr, "<h2>");
		org.jsoup.nodes.Document doc = Jsoup.parse(htmlStr);
		Elements trs = doc.select("tbody tr");
		List<DcProjectColumn> dctrs = new ArrayList<DcProjectColumn>();

		int columnSize = 0;
		int rowIndex = 0;
		for (Element tr : trs) {
			rowIndex++;
			DcProjectColumn dctr = new DcProjectColumn();
			dctr.setIsTemplate(1);
			dctr.setId(UuidUtils.generateUuid());
			if (rowIndex >= project.getDataStartIndex() && rowIndex <= project.getDataEndIndex())
				dctr.setIsDataRow(1);
			dctr.setUnitId(map.get("unitId"));
			dctr.setUserId(map.get("userId"));
			dctr.setProjectId(project.getId());
			dctr.setRowIndex(tr.elementSiblingIndex());
			if (tr.childNodeSize() > 1 && tr.childNode(1).childNodeSize() > 0) {
				String s = tr.childNode(1).childNode(0).outerHtml();
				if (StringUtils.isNotBlank(s) && NumberUtils.isNumber(s)) {
					dctr.setIsDataRow(1);
					if (project.getDataEndIndex() < rowIndex) {
						project.setDataEndIndex(rowIndex);
					}
				}
			}
			for (Node td : tr.childNodes()) {
				if (td.siblingIndex() > columnSize)
					columnSize = td.siblingIndex();
				if (td.childNodeSize() > 0) {
					String s = td.childNode(0).outerHtml();
					if (rowIndex == project.getTitleIndex()) {
						map.put(sheetIndex + "-" + s, td.siblingIndex() + 1 + "");
					}
					EntityUtils.setValue(dctr, "column" + (td.siblingIndex() + 1), s);
				}
			}
			dctrs.add(dctr);
		}
		project.setColumnSize(columnSize);
		// dcProjectService.save(project);
		// ------------------------------模板保存完毕----------------------

		// 读取有权限填报的项目
		org.jsoup.nodes.Document doc2 = Jsoup.parse(htmlStr);
		List<DcProjectColumn> colss = dctrs;
		Element firstTR = doc2.select("tbody tr:eq(0)").get(0);
		Elements es = doc2.select(
				"tbody tr:lt(" + project.getDataStartIndex() + "),tr:gt(" + (project.getDataEndIndex() - 1) + ")");
		doc2.select("tbody tr:gt(0)").remove();

		int index = 1;
		ExcelStat sm = new ExcelStat();

		for (DcProjectColumn cols : colss) {
			if (index < project.getDataStartIndex() || index > project.getDataEndIndex()) {
				Element tr;
				if (index == 1)
					tr = firstTR;
				else {
					int indexES = index < project.getDataStartIndex() ? index
							: index - project.getDataEndIndex() + project.getDataStartIndex();
					tr = es.get(indexES - 1).clone();
				}
				int colspanTotal = 0;
				for (int i = 0; i < project.getColumnSize(); i++) {
					String v = EntityUtils.getValue(cols, "column" + (i + 1));
					if (v != null && tr.childNodeSize() > i) {
						int colspan = NumberUtils.toInt(tr.childNode(i).attr("colspan"));
						if (colspan == 0) {
							colspan = 1;
						}
						colspanTotal += colspan;
						if (Arrays.asList("{求和}", "｛求和｝", "{sum}", "｛sum｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(sm.getSum("column_" + (colspanTotal)));
							EntityUtils.setValue(cols, "column" + (i + 1), sm.getSum("column_" + (colspanTotal)));
						} else if (Arrays.asList("{计数}", "｛计数｝", "{count}", "｛count｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(sm.getCount("column_" + (colspanTotal)));
							EntityUtils.setValue(cols, "column" + (i + 1), sm.getCount("column_" + (colspanTotal)));
						} else if (Arrays.asList("{教师姓名}", "｛教师姓名｝", "{teacherName}", "｛teacherName｝").contains(v)) {
							if (teacher == null) {
							}
							if (teacher != null) {
								Node node = tr.childNode(i);
								((Element) node).html(teacher.getTeacherName());
							}
						} else if (Arrays.asList("{时间}", "｛时间｝", "{time}", "｛time｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
						} else if (Arrays.asList("{日期}", "｛日期｝", "{date}", "｛date｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
						} else if (Arrays.asList("{单位}", "｛单位｝", "{unitName}", "｛unitName｝").contains(v)) {
							Node node = tr.childNode(i);
							if (unit != null)
								((Element) node).html(unit.getUnitName());
						} else {
							Node node = tr.childNode(i);
							try {
								((Element) node).html(v);
							} catch (Exception e) {
								((TextNode) node).text(v);
							}
						}
					}
				}
				if (index > 1) {
					firstTR.parent().append(tr.toString());
				}
			} else if (index <= project.getDataEndIndex()) {
				Element tr = es.get(project.getDataStartIndex() - 1).clone();
				for (int ii = 0; ii < project.getColumnSize(); ii++) {
					int i = ii;
					String v = EntityUtils.getValue(cols, "column" + (i + 1));

					if (sheetIndex == 1) {
						int sIndex = NumberUtils.toInt(map.get(1 + "-" + "办园性质（自填）")) - 1;
						if (i == sIndex) {
							registerValueMap.put("map.yeyxz", v);
						}
					}

					if (sheetIndex == 5) {
						if (i == 7) {
							String gender = EntityUtils.getValue(cols, "column4");
							String age = EntityUtils.getValue(cols, "column5");
							if ((StringUtils.equals("女", gender) && NumberUtils.toInt(age) > 55)
									|| StringUtils.equals("男", gender) && NumberUtils.toInt(age) > 60) {
								v = "0";
								String v2 = EntityUtils.getValue(cols, "column" + (i + 1));
								EntityUtils.setValue(cols, "column8", v);
								if (!StringUtils.equals("0", v2) && !StringUtils.equals("&nbsp;", v2)
										&& StringUtils.isNotBlank(v2) && !StringUtils.equals(v, v2)) {
									v = "<font color='red'>" + v + "(原:" + v2 + ")</font>";
								}
							}
						} else if (i == 9) {
							String v1 = EntityUtils.getValue(cols, "column8");
							String v2 = EntityUtils.getValue(cols, "column9");
							v = "" + NumberUtils.toDouble(v1) * NumberUtils.toDouble(v2);
						}
					}

					if (sheetIndex == 4) {
						if (i == 9) {
							int nameIndex = NumberUtils.toInt(map.get(sheetIndex + "-" + "教师姓名(自动生成)"));
							if (nameIndex <= 0)
								nameIndex = 2;
							String gwbz = registerValueMap
									.get("gwbz." + EntityUtils.getValue(cols, "column" + nameIndex));
							if (StringUtils.isBlank(gwbz) || "0".equals(gwbz)) {
								v = "0";
							} else {

								double f = round("" + EntityUtils.getValue(cols, "column7"))
										* round("" + EntityUtils.getValue(cols, "column9"));
								f = MathUtils.round(f, 2);
								v = "" + f;
							}
							String v2 = EntityUtils.getValue(cols, "column" + (i + 1));
							if (!StringUtils.equals("&nbsp;", v2) && StringUtils.isNotBlank(v2)
									&& !StringUtils.equals(v, v2)) {
								v = "<font color='red'>" + v + "(原:" + v2 + ")</font>";
							}
						} else if (i == 10) {
							int nameIndex = NumberUtils.toInt(map.get(sheetIndex + "-" + "教师姓名(自动生成)"));
							if (nameIndex <= 0)
								nameIndex = 2;
							String gwbz = registerValueMap
									.get("gwbz." + EntityUtils.getValue(cols, "column" + nameIndex));
							if (StringUtils.isBlank(gwbz) || "0".equals(gwbz)) {
								v = "0";
							} else {

								double f = round("" + EntityUtils.getValue(cols, "column7"))
										* round("" + EntityUtils.getValue(cols, "column9")) / 2;
								f = MathUtils.round(f, 2);
								v = "" + f;
							}
							String v2 = EntityUtils.getValue(cols, "column" + (i + 1));
							if (!StringUtils.equals("0", v2) && !StringUtils.equals("&nbsp;", v2)
									&& StringUtils.isNotBlank(v2) && !StringUtils.equals(v, v2)) {
								v = "<font color='red'>" + v + "(原:" + v2 + ")</font>";
							}
						}
					}

					if (sheetIndex == 2) {
						int sIndex = NumberUtils.toInt(map.get(2 + "-" + "江北区注册时间（自填）")) - 1;
						if (sIndex == -1)
							sIndex = 9;
						if (i == sIndex) {
							int sNameIndex = NumberUtils.toInt(map.get(2 + "-" + "姓名（自填）"));
							if (sNameIndex == 0)
								sNameIndex = 2;
							registerValueMap.put("" + EntityUtils.getValue(cols, "column" + sNameIndex), v);
						}
					}

					if (sheetIndex == 3 || sheetIndex == 2) {
						int czbt = NumberUtils.toInt(map.get(sheetIndex + "-" + "职业技术职称(自动生成)"));
						if (czbt <= 0)
							czbt = 11;
						int dyxlIndex = NumberUtils.toInt(map.get(sheetIndex + "-" + "第一学历（全日制）(自动生成)"));
						if (dyxlIndex <= 0)
							dyxlIndex = 13;

						int dexlIndex = NumberUtils.toInt(map.get(sheetIndex + "-" + "进修学历(自动生成)"));
						if (dexlIndex <= 0)
							dexlIndex = 16;

						int gwbz = NumberUtils.toInt(map.get(sheetIndex + "-" + "每月岗位补助(自动生成)")) - 1;
						if (gwbz < 0)
							gwbz = 19;

						int zcbz = NumberUtils.toInt(map.get(sheetIndex + "-" + "每月职称补助(自动生成)")) - 1;
						if (zcbz < 0)
							zcbz = 20;

						int qlbz = NumberUtils.toInt(map.get(sheetIndex + "-" + "每月区龄补助(自动生成)")) - 1;
						if (qlbz < 0)
							qlbz = 18;

						int hjbz = NumberUtils.toInt(
								map.get(sheetIndex + "-" + "合计补助（自动计算：每月岗位补助*补助月数+每月职称补助*职称补助月数+每月区龄补助*区龄补助月数）")) - 1;

						if (sheetIndex == 2) {
							if (qlbz < gwbz) {
								if (i == qlbz) {
									i = i + 1;
								} else if (i == gwbz) {
									i = i - 1;
								}
							}
						}

						// 职称补贴
						if (i == zcbz) {
							if ("选择性民办".equals(registerValueMap.get("map.yeyxz"))) {
								v = "0";
							} else {
								int nameIndex = NumberUtils.toInt(map.get(sheetIndex + "-" + "姓名(自动生成)"));
								if (nameIndex <= 0) {
									nameIndex = 3;
								}
								String gwbz2 = registerValueMap
										.get("gwbz." + EntityUtils.getValue(cols, "column" + nameIndex));
								if (StringUtils.isBlank(gwbz2) || "0".equals(gwbz2)) {
									v = "0";
								} else {
									String s = EntityUtils.getValue(cols, "column" + czbt);
									if (StringUtils.equals("中级", s)) {
										v = "500";
									} else if (StringUtils.equals("初级", s)) {
										v = "100";
									} else {
										v = "0";
									}
								}
							}
							String v2 = EntityUtils.getValue(cols, "column" + (i + 1));
							if (!StringUtils.equals("0", v2) && !StringUtils.equals("&nbsp;", v2)
									&& StringUtils.isNotBlank(v2) && !StringUtils.equals(v, v2)) {
								v = "<font color='red'>" + v + "(原:" + v2 + ")</font>";
							}
							// 岗位补贴
						} else if (i == gwbz) {
							if ("选择性民办".equals(registerValueMap.get("map.yeyxz"))) {
								v = "0";
							} else {
								String dyxl = EntityUtils.getValue(cols, "column" + dyxlIndex);
								String dyzy = EntityUtils.getValue(cols,
										"column" + (dyxlIndex + (sheetIndex == 2 ? 2 : 1)));
								String dexl = EntityUtils.getValue(cols, "column" + dexlIndex);
								String dezy = EntityUtils.getValue(cols,
										"column" + (dexlIndex + (sheetIndex == 2 ? 2 : 1)));
								String zc = EntityUtils.getValue(cols, "column" + czbt);
								if (StringUtils.equals("中专或高中", dyxl)) {
									if (Arrays.asList("学前教育", "师范教育类", "医护类").contains(dyzy)) {
										// 1.第一学历中专、学前教育、师范类、医护类、没有第二学历，无职称200。初级职称300。
										if (StringUtils.isBlank(dexl) || StringUtils.equals(dexl, "0")) {
											if (StringUtils.equals("未定级", zc)) {
												v = "200";
											} else if (StringUtils.equals("初级", zc)) {
												v = "300";
											}
										}
										// 2.第一学历中专、学前教育，师范类、医护类，第二学历大专、学前、师范、艺术类、医护类、无职称、初级职称300，
										// 最高学历本科，学前、师范、艺术类、医护类，无职称、初级职称500，中级800。
										else if (StringUtils.equals("大专", dexl)) {
											if (Arrays.asList("学前教育", "师范教育类", "医护类", "艺术院校类").contains(dezy)) {
												if (Arrays.asList("未定级", "初级").contains(zc)) {
													v = "300";
												} else if (StringUtils.equals(zc, "中级")) {
													v = "800";
												}
											} else if ("其他".equals(dezy)) {
												if (Arrays.asList("未定级").contains(zc)) {
													v = "200";
												} else if (Arrays.asList("初级").contains(zc)) {
													v = "300";
												} else if (StringUtils.equals(zc, "中级")) {
													v = "800";
												}
											}
										} else if (StringUtils.equals("本科", dexl)) {
											if (Arrays.asList("学前教育", "师范教育类", "医护类", "艺术院校类").contains(dezy)) {
												if (Arrays.asList("未定级", "初级").contains(zc)) {
													v = "500";
												} else if (StringUtils.equals("中级", zc))
													v = "800";
											}
										}
									} else if (StringUtils.equals("其他", dyzy)) {
										if (StringUtils.equals("未定级", zc)) {
											v = "0";
										} else if (StringUtils.equals("初级", zc)) {
											v = "300";
										} else if (StringUtils.equals("中级", zc)) {
											v = "800";
										}
									}
								}

								else if (StringUtils.equals("大专", dyxl)) {
									//
									if (!Arrays.asList("其他", "艺术院校类").contains(dyzy)) {
										// 第一学历大专除其他专业外，第二学历本科，除其他专业外，无职称、初级职称500，中级职称800。
										if (StringUtils.equals("本科", dexl)) {
											if (Arrays.asList("未定级", "初级").contains(zc)) {
												v = "500";
											} else if (StringUtils.equals("中级", zc))
												v = "800";
										}
										// 第一学历大专，除其他专业外、无职称、初级职称500.中级职称800。
										else {
											if (Arrays.asList("未定级", "初级").contains(zc)) {
												v = "500";
											} else if (StringUtils.equals("中级", zc))
												v = "800";
										}
									}
									// 第一学历大专，其他专业，无职称200。初级职称300。中级职称800。
									else {
										if (StringUtils.equals("未定级", zc)) {
											v = "200";
										} else if (StringUtils.equals("初级", zc))
											v = "300";
										else if (StringUtils.equals("中级", zc))
											v = "800";

										// 第一学历大专，其他专业，第二学历本科、学前、医护类专业，初级职称500，中级职称800.
										if (StringUtils.equals("本科", dexl)
												&& Arrays.asList("学前教育", "医护类").contains(dezy)) {
											if (StringUtils.equals("初级", zc))
												v = "500";
											else if (StringUtils.equals("中级", zc))
												v = "800";
											else if (StringUtils.equals("未定级", zc))
												v = "300";
										}
									}
								}

								else if (StringUtils.equals("本科", dyxl)) {
									// 第一学历本科，除其他专业外800。无职称、初级、中级职称800。第二学历研究生，学前、医护类、艺术类，有无职称都800
									if (!StringUtils.equals("其他", dyzy)) {
										v = "800";
									} else {
										// 第一学历本科，其他专业，无职称、初级职称500，中级职称800。第二学历研究生，学前、医护类，有无职称都800
										if (StringUtils.equals("未定级", zc)) {
											v = "500";
										} else if (StringUtils.equals("初级", zc))
											v = "500";
										else if (StringUtils.equals("中级", zc))
											v = "800";

										if (StringUtils.equals("研究生", dexl)
												&& Arrays.asList("学前教育", "医护类").contains(dezy)) {
											v = "800";
										}
									}
								}
								// 第一学历研究生，除其他专业外1000，有无职称都1000。
								else if (StringUtils.equals("研究生", dyxl)) {
									if (!StringUtils.equals("其他", dyzy)) {
										v = "800";
									} else {
										v = "1000";
									}
								}
							}
							registerValueMap.put("gwbz." + EntityUtils.getValue(cols, "column3"), v);
							String v2 = EntityUtils.getValue(cols, "column" + (i + 1));
							if (!StringUtils.equals("0", v2) && !StringUtils.equals("&nbsp;", v2)
									&& StringUtils.isNotBlank(v2) && !StringUtils.equals(v, v2)) {
								v = "<font color='red'>" + v + "(原:" + v2 + ")</font>";
							}
						}
						// 区龄补贴
						else if (i == qlbz) {
							if ("选择性民办".equals(registerValueMap.get("map.yeyxz"))) {
								v = "0";

								String dyxl = EntityUtils.getValue(cols, "column" + dyxlIndex);
								String dyzy = EntityUtils.getValue(cols,
										"column" + (dyxlIndex + (sheetIndex == 2 ? 2 : 1)));
								String zc = EntityUtils.getValue(cols, "column" + czbt);
								boolean pass = false;
								if (StringUtils.equals("中专或高中", dyxl)) {
									if (Arrays.asList("学前教育", "师范教育类", "医护类").contains(dyzy)) {
										pass = true;
									}
								} else {
									pass = true;
								}
								if (!"未定级".equals(zc))
									pass = true;

								if (!pass) {
									v = "0";
								} else {
									String registerTime = registerValueMap.get(EntityUtils.getValue(cols, "column3"));

									int year = Calendar.getInstance().get(Calendar.YEAR) - 2;
									if (StringUtils.equals(year + ".10之后", registerTime))
										v = "0";
									else {
										for (int k = 0; k < 20; k++) {
											if (StringUtils.equals((year - k) + ".10之前", registerTime)) {
												int qlbt = 50 * (k + 1);
												if (qlbt > 200)
													qlbt = 200;
												v = qlbt + "";
												break;
											}
										}
									}
								}
							} else {
								String gwbz2 = registerValueMap.get("gwbz." + EntityUtils.getValue(cols, "column3"));
								if (StringUtils.isBlank(gwbz2) || "0".equals(gwbz2)) {
									v = "0";
								} else {
									String registerTime = registerValueMap.get(EntityUtils.getValue(cols, "column3"));

									int year = Calendar.getInstance().get(Calendar.YEAR) - 2;
									if (StringUtils.equals(year + ".10之后", registerTime))
										v = "0";
									else {
										for (int k = 0; k < 20; k++) {
											if (StringUtils.equals((year - k) + ".10之前", registerTime)) {
												int qlbt = 50 * (k + 1);
												if (qlbt > 200)
													qlbt = 200;
												v = qlbt + "";
												break;
											}
										}
									}
								}

							}
							String v2 = EntityUtils.getValue(cols, "column" + (i + 1));
							if (!StringUtils.equals("0", v2) && !StringUtils.equals("&nbsp;", v2)
									&& StringUtils.isNotBlank(v2) && !StringUtils.equals(v, v2)) {
								v = "<font color='red'>" + v + "(原:" + v2 + ")</font>";
							}
						} else if (i == hjbz) {
							int s1Index = NumberUtils.toInt(map.get(sheetIndex + "-" + "每月岗位补助(自动生成)"));
							int s11Index = NumberUtils.toInt(map.get(sheetIndex + "-" + "岗位补助月数（自填）"));
							int s2Index = NumberUtils.toInt(map.get(sheetIndex + "-" + "每月职称补助(自动生成)"));
							int s21Index = NumberUtils.toInt(map.get(sheetIndex + "-" + "职称补助月数（自填）"));
							int s3Index = NumberUtils.toInt(map.get(sheetIndex + "-" + "每月区龄补助(自动生成)"));
							int s31Index = NumberUtils.toInt(map.get(sheetIndex + "-" + "区龄补助月数（自填）"));

							float s1 = round("" + EntityUtils.getValue(cols, "column" + s1Index));
							float s11 = round("" + EntityUtils.getValue(cols, "column" + s11Index));
							float s2 = round("" + EntityUtils.getValue(cols, "column" + s2Index));
							float s21 = round("" + EntityUtils.getValue(cols, "column" + s21Index));
							float s3 = round("" + EntityUtils.getValue(cols, "column" + s3Index));
							float s31 = round("" + EntityUtils.getValue(cols, "column" + s31Index));
							v = s1 * s11 + s2 * s21 + s3 * s31 + "";
							String v2 = EntityUtils.getValue(cols, "column" + (i + 1));
							if (!StringUtils.equals("0", v2) && !StringUtils.equals("&nbsp;", v2)
									&& StringUtils.isNotBlank(v2) && !StringUtils.equals(v, v2)) {
								v = "<font color='red'>" + v + "(原:" + v2 + ")</font>";
							}
						}
					}
					if (v != null) {
						sm.sum("column_" + i, v);
						sm.count("column_" + i);
						if (tr.childNodeSize() <= i)
							continue;
						Node node = tr.childNode(i);
						EntityUtils.setValue(cols, "column" + (i + 1), v);
						((Element) node).html(v);
					}
				}
				firstTR.parent().append(tr.toString());
			}
			index++;
		}

		DcProjectColumn[] dcs = colss.toArray(new DcProjectColumn[0]);
		// dcProjectColumnService.saveAll(dcs);

		org.apache.commons.io.FileUtils.write(new File("c:\\doc\\" + outFileName),
				new String(doc2.toString().getBytes("UTF-8"), "UTF-8"));

		return map;
	}

	private Map<String, String> syncStatExcel(JSONArray jsons, DcProject project, String htmlStr, String head,
			String outFileName) throws IOException, UnsupportedEncodingException {

		htmlStr = StringUtils.substringAfter(htmlStr, "</h2>");
		htmlStr = head + StringUtils.substringBefore(htmlStr, "<h2>");
		org.jsoup.nodes.Document doc = Jsoup.parse(htmlStr);
		Elements trs = doc.select("tbody tr");
		List<DcProjectColumn> dctrs = new ArrayList<DcProjectColumn>();

		int columnSize = 0;
		int rowIndex = 0;
		for (Element tr : trs) {
			rowIndex++;
			DcProjectColumn dctr = new DcProjectColumn();
			dctr.setIsTemplate(1);
			dctr.setId(UuidUtils.generateUuid());
			if (rowIndex >= project.getDataStartIndex() && rowIndex <= project.getDataEndIndex())
				dctr.setIsDataRow(1);
			dctr.setProjectId(project.getId());
			dctr.setRowIndex(tr.elementSiblingIndex());
			if (tr.childNodeSize() > 1 && tr.childNode(1).childNodeSize() > 0) {
				String s = tr.childNode(1).childNode(0).outerHtml();
				if (StringUtils.isNotBlank(s) && NumberUtils.isNumber(s)) {
					dctr.setIsDataRow(1);
					if (project.getDataEndIndex() < rowIndex) {
						project.setDataEndIndex(rowIndex);
					}
				}
			}
			boolean allBlank = true;
			for (Node td : tr.childNodes()) {
				if (td.siblingIndex() > columnSize)
					columnSize = td.siblingIndex();
				if (td.childNodeSize() > 0) {
					String s = td.childNode(0).outerHtml();
					if (StringUtils.isNotBlank(s) && !"&nbsp;".equals(s))
						allBlank = false;
					EntityUtils.setValue(dctr, "column" + (td.siblingIndex() + 1), s);
				}
			}
			if (!allBlank)
				dctrs.add(dctr);
		}
		project.setColumnSize(columnSize);
		// dcProjectService.save(project);
		// ------------------------------模板保存完毕----------------------

		// 读取有权限填报的项目
		org.jsoup.nodes.Document doc2 = Jsoup.parse(htmlStr);
		List<DcProjectColumn> colss = dctrs;
		Element firstTR = doc2.select("tbody tr:eq(0)").get(0);
		Elements es = doc2.select("tbody tr:lt(" + project.getDataStartIndex() + ")");
		doc2.select("tbody tr:gt(0)").remove();

		int index = 1;
		ExcelStat sm = new ExcelStat();

		for (int i = 0; i < jsons.size(); i++) {
			JSONObject json = jsons.getJSONObject(i);
			DcProjectColumn column = JSONObject.parseObject(json.toJSONString(), DcProjectColumn.class);
			column.setColumn2(i + 1 + "");
			colss.add(colss.size() - 1, column);
		}

		for (DcProjectColumn cols : colss) {
			if (index < project.getDataStartIndex() || index > project.getDataEndIndex()) {
				Element tr;
				if (index == 1)
					tr = firstTR;
				else {
					int indexES = index < project.getDataStartIndex() ? index
							: index - project.getDataEndIndex() + project.getDataStartIndex() - 1;
					tr = es.get(indexES - 1).clone();
				}
				int colspanTotal = 0;
				for (int i = 0; i < project.getColumnSize(); i++) {
					String v = EntityUtils.getValue(cols, "column" + (i + 1));
					if (v != null && tr.childNodeSize() > i) {
						int colspan = NumberUtils.toInt(tr.childNode(i).attr("colspan"));
						if (colspan == 0) {
							colspan = 1;
						}
						colspanTotal += colspan;
						if (Arrays.asList("{求和}", "｛求和｝", "{sum}", "｛sum｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(sm.getSum("column_" + (colspanTotal)));
							EntityUtils.setValue(cols, "column" + (i + 1), sm.getSum("column_" + (colspanTotal)));
						} else if (Arrays.asList("{sumInt}", "｛sumInt｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node)
									.html("" + Math.round(NumberUtils.toDouble(sm.getSum("column_" + (colspanTotal)))));
							EntityUtils.setValue(cols, "column" + (i + 1),
									Math.round(NumberUtils.toDouble(sm.getSum("column_" + (colspanTotal)))));
						} else if (Arrays.asList("{计数}", "｛计数｝", "{count}", "｛count｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(sm.getCount("column_" + (colspanTotal)));
							EntityUtils.setValue(cols, "column" + (i + 1), sm.getCount("column_" + (colspanTotal)));
						} else if (Arrays.asList("{教师姓名}", "｛教师姓名｝", "{teacherName}", "｛teacherName｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html("XXXX 教师");
						} else if (Arrays.asList("{时间}", "｛时间｝", "{time}", "｛time｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
						} else if (Arrays.asList("{日期}", "｛日期｝", "{date}", "｛date｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
						} else if (Arrays.asList("{单位}", "｛单位｝", "{unitName}", "｛unitName｝").contains(v)) {
							Node node = tr.childNode(i);
							((Element) node).html("XXXX单位");
						} else {
							Node node = tr.childNode(i);
							try {
								((Element) node).html(v);
							} catch (Exception e) {
								((TextNode) node).text(v);
							}
						}
					}
				}
				if (index > 1) {
					firstTR.parent().append(tr.toString());
				}
			} else if (index <= project.getDataEndIndex()) {
				Element tr = es.get(project.getDataStartIndex() - 1).clone();
				for (int ii = 0; ii < project.getColumnSize(); ii++) {
					int i = ii;
					String v = EntityUtils.getValue(cols, "column" + (i + 1));
					if (v != null) {
						sm.sum("column_" + i, v);
						sm.count("column_" + i);
						if (tr.childNodeSize() <= i)
							continue;
						Node node = tr.childNode(i);
						EntityUtils.setValue(cols, "column" + (i + 1), v);
						if (node instanceof Element)
							((Element) node).html(v);
						else if (node instanceof TextNode)
							((TextNode) node).text(v);
					}
				}
				firstTR.parent().append(tr.toString());
			}
			index++;
		}

		// DcProjectColumn[] dcs = colss.toArray(new DcProjectColumn[0]);
		// dcProjectColumnService.saveAll(dcs);

		org.apache.commons.io.FileUtils.write(new File("c:\\doc\\" + outFileName),
				new String(doc2.toString().getBytes("UTF-8"), "UTF-8"));

		return null;
	}

}
