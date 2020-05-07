package net.zdsoft.newstusys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.dao.StusysStudentBakDao;
import net.zdsoft.newstusys.dao.StusysStudentBakJdbcDao;
import net.zdsoft.newstusys.entity.StusysStudentBak;
import net.zdsoft.newstusys.service.StusysStudentBakService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

/**
 * 
 * @author weixh
 * 2018年9月18日	
 */
@Service("stusysStudentBakService")
public class StusysStudentBakServiceImpl extends BaseServiceImpl<StusysStudentBak, String>
		implements StusysStudentBakService {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StusysStudentBakDao stusysStudentBakDao;
	@Autowired
	private StusysStudentBakJdbcDao stusysStudentBakJdbcDao;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
	private ClassRemoteService classRemoteService;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<StusysStudentBak, String> getJpaDao() {
		return stusysStudentBakDao;
	}

	@Override
	protected Class<StusysStudentBak> getEntityClass() {
		return StusysStudentBak.class;
	}

	public void saveStudentBak(String schId) {
		long ss = System.currentTimeMillis();
		System.out.println("=========学生数据["+schId+"]存档 start============================");
		try {
			String status = RedisUtils.lpop(StusysStudentBak.BAK_PREFIX+schId+StusysStudentBak.BAK_STATUS);
			if(StusysStudentBak.BAK_STATUS_ING.equals(status)) {
				return;
			}
			RedisUtils.lpush(StusysStudentBak.BAK_PREFIX + schId 
					+ StusysStudentBak.BAK_STATUS, StusysStudentBak.BAK_STATUS_ING);
			Semester nowSemester = Semester.dc(semesterRemoteService.getCurrentSemester(1));
			List<StusysStudentBak> his = stusysStudentBakJdbcDao.findSemesterBySchId(schId);
			int nowYear = NumberUtils.toInt(nowSemester.getAcadyear().substring(0, 4));
			Semester lastSemester = null;
			boolean onlyCurrent = false;// 是否仅存档本学期数据
			// 已统计过的学年学期不再存档，从最后一次统计的学年学期（不含）开始存档
			if(CollectionUtils.isNotEmpty(his)) {
				StusysStudentBak bak = his.get(0);
				int ly = NumberUtils.toInt(bak.getAcadyear().substring(0, 4));
				if(ly > nowYear) {
					onlyCurrent = true;
//					clearClsCache(schId, "本学期已存档数据");
//					return;
				} else {
					if(bak.getAcadyear().equals(nowSemester.getAcadyear()) 
							&& bak.getSemester() >= nowSemester.getSemester()) {
						onlyCurrent = true;
	//					clearClsCache(schId, "本学期已存档数据");
	//					return;
					}
				}
				if (!onlyCurrent) {
					lastSemester = new Semester();
					lastSemester.setAcadyear(bak.getAcadyear());
					lastSemester.setSemester(bak.getSemester());
				}
			}
			if(onlyCurrent) {
				stusysStudentBakDao.deleteBySchIdSemester(nowSemester.getAcadyear(), nowSemester.getSemester(), schId);
			}
			// 存档差的学期
			List<Clazz> clsList = Clazz.dt(classRemoteService.findByIdCurAcadyear(schId, nowSemester.getAcadyear()));
			if(CollectionUtils.isEmpty(clsList)) {
				clearClsCache(schId, "学校下没有班级");
				return;
			}
			List<Student> stuList = Student.dt(studentRemoteService.findBySchoolId(schId));
			if(CollectionUtils.isEmpty(stuList)) {
				clearClsCache(schId, "学校下没有学生");
				return;
			}
			Map<String, List<Student>> stuMap = EntityUtils.getListMap(stuList, Student::getClassId, Function.identity());
			boolean hasData = lastSemester != null;
			int ly = 0;
			if(!onlyCurrent && hasData) {
				ly = NumberUtils.toInt(lastSemester.getAcadyear().substring(0, 4));
				if(lastSemester.getSemester() == 2) {
					ly++;
				}
			}
			List<StusysStudentBak> baks = new ArrayList<>();
			for(Clazz cls : clsList) {
				List<Student> stus = stuMap.get(cls.getId());
				if(CollectionUtils.isEmpty(stus)) {
					continue;
				}
				int clsYear = NumberUtils.toInt(cls.getAcadyear().substring(0, 4));
				int startYear = 0;// 开始存档学年
				if(onlyCurrent) {
					startYear = nowYear;
				} else {
					if(hasData) {
						if(ly >= clsYear) {// 上次存档已开班
							startYear = ly;
						} else {
							startYear = clsYear;
						}
					} else {
						startYear = clsYear;// 从开班的那个学年开始存档
					}
				}
				while(startYear <= nowYear) {
					int[] semes = new int[] {1, 2};
					if(onlyCurrent) {
						semes = new int[] {nowSemester.getSemester()};
					} else {
						if(hasData && startYear == ly) {
							if(lastSemester.getSemester() == 1) {// 上次已存档了第一学期
								semes = new int[]{2};
							}
						}
						if(startYear == nowYear) {
							if(nowSemester.getSemester() == 1) {// 存档当前学年，当前学期是第一学期
								semes = new int[]{1};
							}
						}
					}
					for(int semester : semes) {
						for (Student stu : stus) {
							StusysStudentBak bak = new StusysStudentBak();
							EntityUtils.copyProperties(stu, bak);
							bak.setId(UuidUtils.generateUuid());
							bak.setStudentId(stu.getId());
							bak.setCreationTime(new Date());
							bak.setModifyTime(new Date());
							bak.setAcadyear(startYear+"-"+(startYear+1));
							bak.setSemester(semester);
							if (startYear == nowYear) {
								bak.setClassName(cls.getClassNameDynamic());
							} else {
								String gn = getGradeNameDyn(cls.getAcadyear(), cls.getSection(), cls.getSchoolingLength(), startYear);
								if(StringUtils.isEmpty(gn)) {
									bak.setClassName(cls.getClassNameDynamic());
								} else {
									bak.setClassName(gn+cls.getClassName());
								}
							}
							bak.setTeacherId(cls.getTeacherId());
							baks.add(bak);
						}
					}
					
					startYear++;
				}
			}
			if(baks.size() > 0) {
				this.saveAll(baks.toArray(new StusysStudentBak[0]));
			}
			clearClsCache(schId, "存档成功");
			// TODO
		} catch (Exception e) {
			log.error("学校["+schId+"]学生数据存档失败："+e.getMessage(), e);
			clearClsCache(schId, "存档失败，详情请联系技术人员查看");
		}
		
		long ee = System.currentTimeMillis();
		System.out.println("=========学生数据["+schId+"]存档 end，耗时："+(ee-ss)+"ms============================");
	}
	
	/**
	 * 根据入学学年、所属学段、学制、当前学年和命名规则，并且按照规则动态生成班级名称 说明：学制主要用于查看已经毕业的年级生成年级名称时使用
	 * @param enrollyear
	 *            入学学年(格式：2005-2006)
	 * @param section
	 *            学段，微代码（DM-RKXD）:０幼儿园，１小学，２初中，３高中
	 * @param schoolinglen
	 *            学制
	 * @param curAcadyear
	 *            当前学年的前4位
	 * 
	 * @return String
	 */
	public String getGradeNameDyn(String enrollyear, int section,
			int schoolinglen, int curAcadyear) {
		if (curAcadyear == 0) {
			return "";
		}

		int grade = 1 + curAcadyear
				- NumberUtils.toInt(StringUtils
						.substringBefore(enrollyear, "-"));
		if (grade <= 0) {
			return "";
		}

		String gradeNameDyn;
		// 从微代码中取出年级规则，有几个固定变量：enrollyear，表示入学学年
		Map<String, McodeDetail> detailMap = SUtils.dt(mcodeRemoteService
				.findMapByMcodeId("DM-RKXD-" + section), new TR<Map<String, McodeDetail>>(){});
		if (MapUtils.isEmpty(detailMap))
			return "";
		// 判断年级是否已超出规定学制
		if (schoolinglen > 0) {
			grade = (grade > schoolinglen ? schoolinglen : grade);
		}
		McodeDetail detail = detailMap.get("" + grade);
		if (detail == null)
			return "";
		gradeNameDyn = detail.getMcodeContent();
		gradeNameDyn = StringUtils.replace(gradeNameDyn, "{enrollyear}",
				StringUtils.substringBefore(enrollyear, "-"));
		return gradeNameDyn;
	}
	
	/**
	 * 去除班级同步状态缓存，添加同步结果说明
	 * @param clsId
	 * @param msg 
	 */
	private void clearClsCache(String schId, String msg) {
		RedisUtils.del(StusysStudentBak.BAK_PREFIX + schId 
				+ StusysStudentBak.BAK_STATUS);
		RedisUtils.del(StusysStudentBak.BAK_PREFIX + schId 
				+ StusysStudentBak.BAK_MSG);
		if (StringUtils.isNotEmpty(msg)) {
			RedisUtils.lpush(StusysStudentBak.BAK_PREFIX + schId + StusysStudentBak.BAK_MSG,
					"上一次操作结果："+msg+"("+DateUtils.date2StringByMinute(new Date())+")");
		}
	}

}
