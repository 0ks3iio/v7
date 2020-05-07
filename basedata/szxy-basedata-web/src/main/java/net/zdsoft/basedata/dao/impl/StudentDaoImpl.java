package net.zdsoft.basedata.dao.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.dao.StudentJdbcDao;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class StudentDaoImpl extends BaseDao<Student> implements StudentJdbcDao {

	@Override
	public Student setField(ResultSet rs) throws SQLException {
		Student student = new Student();
		student.setId(rs.getString("id"));
		student.setSchoolId(rs.getString("school_id"));
		student.setClassId(rs.getString("class_id"));
		student.setStudentName(rs.getString("student_name"));
		student.setOldName(rs.getString("old_name"));
		student.setStudentCode(rs.getString("student_code"));
		student.setUnitiveCode(rs.getString("unitive_code"));
		student.setIdentityCard(rs.getString("identity_card"));
		student.setSex(rs.getInt("sex"));
		student.setBirthday(rs.getTimestamp("birthday"));
		student.setMobilePhone(rs.getString("mobile_phone"));
		student.setIsLeaveSchool(rs.getInt("is_leave_school"));
		student.setEnrollYear(rs.getString("enroll_year"));
		student.setClassInnerCode(rs.getString("class_inner_code"));
		student.setEconomyState(rs.getString("economy_state"));
		student.setCardNumber(rs.getString("card_number"));
		student.setIsSingleton(rs.getInt("is_singleton"));
		student.setStayin(rs.getInt("stayin"));
		student.setBoorish(rs.getInt("boorish"));
		student.setIsLocalSource(rs.getInt("is_local_source"));
		student.setStudentRecruitment(rs.getInt("student_recruitment"));
		student.setStudyMode(rs.getInt("study_mode"));
		student.setNativePlace(rs.getString("native_place"));
		student.setHomepage(rs.getString("homepage"));
		student.setEmail(rs.getString("email"));
		student.setLinkPhone(rs.getString("link_phone"));
		student.setLinkAddress(rs.getString("link_address"));
		student.setPostalcode(rs.getString("postalcode"));
		student.setRegionCode(rs.getString("region_code"));
		student.setCreationTime(rs.getTimestamp("creation_time"));
		student.setModifyTime(rs.getTimestamp("modify_time"));
		student.setIsDeleted(rs.getInt("is_deleted"));
		student.setEventSource(rs.getInt("event_source"));
		student.setIsFreshman(rs.getInt("is_freshman"));
		student.setPassword(rs.getString("password"));
		student.setNowState(rs.getString("now_state"));
		student.setSchoolDistrict(rs.getString("school_district"));
		student.setMeetexamCode(rs.getString("meetexam_code"));
		student.setSourcePlace(rs.getString("source_place"));
		student.setIsSpecialid(rs.getInt("is_specialid"));
		student.setDirId(rs.getString("dir_id"));
		student.setFilePath(rs.getString("file_path"));
		student.setHealth(rs.getString("health"));
		student.setNation(rs.getString("nation"));
		student.setBackground(rs.getString("background"));
		student.setPolityJoinDate(rs.getTimestamp("polity_join_date"));
		student.setHomeAddress(rs.getString("home_address"));
		student.setToSchoolDate(rs.getTimestamp("to_school_date"));
		student.setDorm(rs.getString("dorm"));
		student.setDormTel(rs.getString("dorm_tel"));
		student.setSpecId(rs.getString("spec_id"));
		student.setSpecpointId(rs.getString("specpoint_id"));
		student.setEnterScore(rs.getString("enter_score"));
		student.setBankCardNo(rs.getString("bank_card_no"));
		student.setOldSchoolName(rs.getString("old_school_name"));
		student.setOldSchoolDate(rs.getTimestamp("old_school_date"));
		student.setIdentitycardType(rs.getString("identitycard_type"));
		student.setPin(rs.getString("pin"));
		student.setDuplexClassGradeId(rs.getString("duplex_class_grade_id"));
		student.setStateCode(rs.getString("state_code"));
		student.setRecruitMode(rs.getInt("recruit_mode"));
		student.setLearnMode(rs.getInt("learn_mode"));
		student.setCooperateType(rs.getInt("cooperate_type"));
		student.setCooperateSchoolCode(rs.getString("cooperate_school_code"));
		student.setRegisterPlace(rs.getString("register_place"));
		student.setRegisterType(rs.getInt("register_type"));
		student.setIsLowAllowce(rs.getInt("is_low_allowce"));
		student.setIsEnjoyStateGrants(rs.getInt("is_enjoy_state_grants"));
		student.setGrantStandard(rs.getDouble("grant_standard"));
		student.setSourceType(rs.getInt("source_type"));
		student.setTeachingPointsName(rs.getString("teaching_points_name"));
		student.setStudentCategory(rs.getString("student_category"));
		student.setCompatriots(rs.getInt("compatriots"));
		student.setGraduateSchool(rs.getString("graduate_school"));
		student.setRemark(rs.getString("remark"));
		student.setRegisterCode(rs.getString("register_code"));
		student.setSpellName(rs.getString("spell_name"));
		student.setHomePlace(rs.getString("home_place"));
		student.setLastDegree(rs.getString("last_degree"));
		student.setRegisterAddress(rs.getString("register_address"));
		student.setBloodType(rs.getString("blood_type"));
		student.setExamCode(rs.getString("exam_code"));
		// student.setCredentialType(rs.getString("credential_type"));
		// student.setCredentialCode(rs.getString("credential_code"));
		student.setPhotoNo(rs.getString("photo_no"));
		student.setBenefitMoney(rs.getDouble("benefit_money"));
		student.setStudentMode(rs.getString("student_mode"));
		student.setRewardSpecId(rs.getString("reward_spec_id"));
		student.setSchLengthType(rs.getString("sch_length_type"));
		student.setIsFamilySide(rs.getInt("is_family_side"));
		student.setStudentQq(rs.getString("student_qq"));
		student.setOneCardNumber(rs.getString("one_card_number"));
		student.setSeatNumber(rs.getString("seat_number"));
		student.setEnglishName(rs.getString("english_name"));
		student.setSchoolinglen(rs.getString("schoolinglen"));
		student.setToschooltype(rs.getString("toschooltype"));
		student.setCountry(rs.getString("country"));
		student.setMarriage(rs.getString("marriage"));
		student.setNativeType(rs.getString("native_type"));
		student.setLocalPoliceStation(rs.getString("local_police_station"));
		student.setNowaddress(rs.getString("nowaddress"));
		student.setHomePostalcode(rs.getString("home_postalcode"));
		// student.setSource(rs.getString("source"));
		student.setCooperateForm(rs.getString("cooperate_form"));
		student.setTrainStation(rs.getString("train_station"));
		student.setOldStudentCode(rs.getString("old_student_code"));
		student.setFamilyMobile(rs.getString("family_mobile"));
		student.setReligion(rs.getString("religion"));
		// student.setReturnedchinese(rs.getInt("returnedchinese"));
		student.setFamilyOrigin(rs.getString("family_origin"));
		student.setCompulsoryedu(rs.getString("compulsoryedu"));
		student.setFlowingPeople(rs.getInt("flowing_people"));
		student.setPolityDate(rs.getTimestamp("polity_date"));
		student.setPolityIntroducer(rs.getString("polity_introducer"));
		student.setPartyDate(rs.getTimestamp("party_date"));
		student.setPartyIntroducer(rs.getString("party_introducer"));
		student.setZkCode(rs.getString("zk_code"));
		student.setZkResult(rs.getDouble("zk_result"));
		student.setEnglishLevel(rs.getString("english_level"));
		student.setComputerLevel(rs.getString("computer_level"));
		// student.setSpecialty(rs.getString("specialty"));
		student.setIsPreedu(rs.getInt("is_preedu"));
		student.setStrong(rs.getString("strong"));
		student.setRegisterInfo(rs.getInt("register_info"));
		student.setOldSchcode(rs.getString("old_schcode"));
		student.setWlClassIntention(rs.getString("wl_class_intention"));
		student.setFormerClassTeacher(rs.getString("former_class_teacher"));
		student.setFormerClassLeader(rs.getString("former_class_leader"));
		student.setZkArtResult(rs.getDouble("zk_art_result"));
		student.setZkMathResult(rs.getDouble("zk_math_result"));
		student.setZkEnglishResult(rs.getDouble("zk_english_result"));
		student.setZkIntegratedResult(rs.getDouble("zk_integrated_result"));
		student.setZkPeResult(rs.getDouble("zk_pe_result"));
		student.setZkExperimentResult(rs.getDouble("zk_experiment_result"));
		student.setZkComputerResult(rs.getDouble("zk_computer_result"));
		student.setZkExtro(rs.getString("zk_extro"));
		student.setZkStrong(rs.getString("zk_strong"));
		student.setRxArtResult(rs.getDouble("rx_art_result"));
		student.setRxMathResult(rs.getDouble("rx_math_result"));
		student.setRxEnglishResult(rs.getDouble("rx_english_result"));
		student.setRxIntegratedResult(rs.getDouble("rx_integrated_result"));
		student.setRxResult(rs.getDouble("rx_result"));
		student.setAccountAttribution(rs.getString("account_attribution"));
		student.setIsLocalSchoolEnrollment(rs
				.getString("is_local_school_enrollment"));
		student.setIsDuplexClass(rs.getString("is_duplex_class"));
		student.setIsRereading(rs.getString("is_rereading"));
		student.setRegularClass(rs.getString("regular_class"));
		student.setAge(rs.getInt("age"));
		student.setDegreePlace(rs.getString("degree_place"));
		student.setDisabilityType(rs.getString("disability_type"));
		student.setIsBoarding(rs.getString("is_boarding"));
		student.setIsMigration(rs.getString("is_migration"));
		student.setIsFallEnrollment(rs.getString("is_fall_enrollment"));
		student.setFingerprint(rs.getString("fingerprint"));
		student.setDistance(rs.getString("distance"));
		student.setTrafficWay(rs.getString("traffic_way"));
		student.setIsNeedBus(rs.getInt("is_need_bus"));
		student.setIsGovernmentBear(rs.getInt("is_government_bear"));
		student.setIsNeedAssistance(rs.getInt("is_need_assistance"));
		student.setIsOrphan(rs.getInt("is_orphan"));
		student.setIsEnjoyAssistance(rs.getInt("is_enjoy_assistance"));
		student.setIsMartyrChild(rs.getInt("is_martyr_child"));
		student.setIdentitycardValid(rs.getString("identitycard_valid"));
		student.setUrbanRegisterType(rs.getString("urban_register_type"));
		student.setPlanMode(rs.getString("plan_mode"));
		student.setSocialRecruitmentType(rs
				.getString("social_recruitment_type"));
		student.setNowChildNum(rs.getInt("now_child_num"));
		student.setBirthRank(rs.getInt("birth_rank"));
		student.setRxqk(rs.getString("rxqk"));
		student.setIsGetCertification(rs.getString("is_get_certification"));
		student.setIsFiveHigherVocational(rs
				.getString("is_five_higher_vocational"));
		student.setBelongContinents(rs.getString("belong_continents"));
		student.setForeignerStudyTime(rs.getString("foreigner_study_time"));
		student.setIsVocationalTechClass(rs
				.getString("is_vocational_tech_class"));
		student.setIsRegularClass(rs.getString("is_regular_class"));
		student.setFingerprint2(rs.getString("fingerprint2"));
		// student.setSequenceIntId(rs.getLong("sequence_int_id"));
		student.setStudentType(rs.getString("student_type"));
		student.setVerifyCode(rs.getString("verify_code"));
		return student;
	}

	@Override
	public Map<String, Integer> countMapByClassIds(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, String> getStudentNameMap(String[] studentIds) {
		String sql = "select id,student_name from base_student where is_deleted = 0 and id IN ";
		return queryForInSQL(sql, null, studentIds,
				new MapRowMapper<String, String>() {

					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString("id");
					}

					@Override
					public String mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString("student_name");
					}
				}, null);
	}

	@Override
	public List<Student> getStudentIsLeaveSchool(String studentName,
			String identityCard, String unitId, Pagination page) {
		StringBuffer sql = new StringBuffer(
				"select * from base_student where ((is_leave_school=1 and verify_code is null) or is_leave_school=0 and is_freshman !='-1')");

		List<String> args = Lists.newArrayList();
		if (StringUtils.isNotEmpty(studentName)) {
			args.add(studentName);
			sql.append(" and student_name = ? ");
		}
		if (StringUtils.isNotEmpty(identityCard)) {
			args.add(identityCard);
			sql.append(" and identity_card = ?");
		}
		if (StringUtils.isNotEmpty(unitId)) {
			args.add(unitId);
			sql.append(" and school_id = ?");
		}

		if (page != null) {
			return query(sql.toString(), args.toArray(new Object[0]),
					new MultiRow(), page);
		} else {
			return query(sql.toString(), args.toArray(new Object[0]),
					new MultiRow());
		}
	}

	// public void update(Student student){
	// String sql = "update base_student set class_id=? where id = ?";
	// update(sql,new Object[]{student.getClassId(),student.getId()});
	// }
	@Override
	public void updateIdCard(List<Student> studentList) {
		String sql = "update base_student set identity_card=?, identitycard_type=? ,modify_time = ? where id=?";
		List<Object[]> objList = new ArrayList<Object[]>();
		for (Student stu : studentList) {
			Object[] obj = new Object[] { stu.getIdentityCard(),
					stu.getIdentitycardType(), new Date(), stu.getId() };
			objList.add(obj);
		}
		batchUpdate(sql, objList, new int[] { Types.CHAR, Types.CHAR,
				Types.TIMESTAMP, Types.CHAR });
	}
	
	public void updateClaIds(List<Student> studentList) {
		String sql = "update base_student set class_id=? ,modify_time = ? where id=?";
		List<Object[]> objList = new ArrayList<Object[]>();
		for (Student stu : studentList) {
			Object[] obj = new Object[] {stu.getClassId(), new Date(), stu.getId()};
			objList.add(obj);
		}
		batchUpdate(sql, objList, new int[] {Types.CHAR, Types.TIMESTAMP, Types.CHAR });
	}

	@Override
	public int[] updateCardNumber(List<String[]> studentList) {
		String sql = "update base_student set card_number=?,modify_time = ? where identity_card=?";
		List<Object[]> objList = new ArrayList<Object[]>();
		for (String[] stu : studentList) {
			Object[] obj = new Object[] { stu[1], new Date(), stu[0] };
			objList.add(obj);
		}
		return batchUpdate(sql, objList, new int[] { Types.VARCHAR, Types.TIMESTAMP,
				Types.VARCHAR });
	}

	@Override
	public List<Student> findByClaIdsLikeStuCodeNames(String unitId,
			String gradeId, String[] classIds, Student searchStudent) {
		StringBuffer sql=new StringBuffer("select t1.id,t1.student_name,t1.class_id,t1.student_code,t1.sex,t2.class_name from base_student t1,base_class t2 ");
		sql.append(" where t2.school_id=? and t2.grade_id=? and t2.is_deleted=0 and t2.is_graduate=0 and t2.id=t1.class_id ");
		List<Object> objList = new ArrayList<Object>();
		objList.add(unitId);
		objList.add(gradeId);
		
		if (classIds != null && classIds.length > 0) {
			sql.append(" and t1.class_id in  ");
			sql.append(SQLUtils.toSQLInString(classIds));
		}
		if (StringUtils.isNotEmpty(searchStudent.getStudentName())) {
			sql.append(" and t1.student_name like ? ");
			objList.add(searchStudent.getStudentName());
		}
		if (StringUtils.isNotEmpty(searchStudent.getStudentCode())) {
			sql.append(" and t1.student_code like ? ");
			objList.add(searchStudent.getStudentCode());
		}
		if (searchStudent.getSex() != null) {
			sql.append(" and t1.sex = ? ");
			objList.add(searchStudent.getSex());
		}
		sql.append("and t1.is_deleted=0 and t1.is_leave_school=0 order by t1.class_id,t1.student_code");
		return query(sql.toString(), objList.toArray(new Object[0]),
				new MultiRowMapper<Student>(){
					@Override
					public Student mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Student ent= new Student();
						ent.setId(rs.getString("id"));
						ent.setStudentName(rs.getString("student_name"));
						ent.setClassId(rs.getString("class_id"));
						ent.setStudentCode(rs.getString("student_code"));
						ent.setSex(rs.getInt("sex"));
						ent.setClassName(rs.getString("class_name"));
						return ent;
					}
			
		});
	}

	@Override
	public List<Student> findPartStudByClaIds(String unitId, String[] classIds) {
		String sql = "select id,school_id,class_id,student_name,old_name,student_code,unitive_code,identitycard_type,identity_card,sex,birthday,mobile_phone,"
				+ "homepage,email,link_phone,link_address,dir_id,file_path,remark from base_student where school_id=? and class_id IN "+SQLUtils.toSQLInString(classIds)+" and is_deleted = 0 and is_leave_school=0";
		return query(sql,unitId,new MultiRowMapper<Student>() {

			@Override
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				Student student = new Student();
				student.setId(rs.getString("id"));
				student.setSchoolId(rs.getString("school_id"));
				student.setClassId(rs.getString("class_id"));
				student.setStudentName(rs.getString("student_name"));
				student.setOldName(rs.getString("old_name"));
				student.setStudentCode(rs.getString("student_code"));
				student.setUnitiveCode(rs.getString("unitive_code"));
				student.setIdentitycardType(rs.getString("identitycard_type"));
				student.setIdentityCard(rs.getString("identity_card"));
				student.setSex(rs.getInt("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setMobilePhone(rs.getString("mobile_phone"));
				student.setHomepage(rs.getString("homepage"));
				student.setEmail(rs.getString("email"));
				student.setLinkPhone(rs.getString("link_phone"));
				student.setLinkAddress(rs.getString("link_address"));
				student.setDirId(rs.getString("dir_id"));
				student.setFilePath(rs.getString("file_path"));
				student.setRemark(rs.getString("remark"));
				return student;
			}
		});
	}

	@Override
	public long CountStudByClaIds(String[] classIds) {
		String sql = "select count(*) from base_student where class_id in "+SQLUtils.toSQLInString(classIds)+" and is_deleted = 0 and is_leave_school=0";
		return this.queryForLong(sql);
	}

	@Override
	public List<Student> findPartStudByStuIds(String unitId, String[] studentIds) {
		String sql = "select id,school_id,class_id,student_name,old_name,student_code,unitive_code,identitycard_type,identity_card,sex,birthday,mobile_phone,"
				+ "homepage,email,link_phone,link_address,dir_id,file_path,remark from base_student where school_id=? and id IN "+SQLUtils.toSQLInString(studentIds)+" and is_deleted = 0 and is_leave_school=0";
		MultiRowMapper<Student> multiRowMapper = new MultiRowMapper<Student>() {

			@Override
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				Student student = new Student();
				student.setId(rs.getString("id"));
				student.setSchoolId(rs.getString("school_id"));
				student.setClassId(rs.getString("class_id"));
				student.setStudentName(rs.getString("student_name"));
				student.setOldName(rs.getString("old_name"));
				student.setStudentCode(rs.getString("student_code"));
				student.setUnitiveCode(rs.getString("unitive_code"));
				student.setIdentitycardType(rs.getString("identitycard_type"));
				student.setIdentityCard(rs.getString("identity_card"));
				student.setSex(rs.getInt("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setMobilePhone(rs.getString("mobile_phone"));
				student.setHomepage(rs.getString("homepage"));
				student.setEmail(rs.getString("email"));
				student.setLinkPhone(rs.getString("link_phone"));
				student.setLinkAddress(rs.getString("link_address"));
				student.setDirId(rs.getString("dir_id"));
				student.setFilePath(rs.getString("file_path"));
				student.setRemark(rs.getString("remark"));
				return student;
			}
		};
		final int MAX_SIZE = 1000;
		if(studentIds.length < MAX_SIZE) {
			return query(sql,unitId, multiRowMapper);
		}else {
			List<Student> studentList = new ArrayList<>();
			
			int st = 0;
			int end = 0;
			String[] copyOfRange=null;
			List<Student> partStus = null;
			while(st<studentIds.length) {
				end = st + MAX_SIZE;
				if(end > studentIds.length) {
					end = studentIds.length;
				}
				copyOfRange = Arrays.copyOfRange(studentIds, st, end);
				sql = "select id,school_id,class_id,student_name,old_name,student_code,unitive_code,identitycard_type,identity_card,sex,birthday,mobile_phone,"
						+ "homepage,email,link_phone,link_address,dir_id,file_path,remark from base_student where school_id=? and id IN "+SQLUtils.toSQLInString(copyOfRange)+" and is_deleted = 0 and is_leave_school=0";
				partStus = query(sql,unitId, multiRowMapper);
				studentList.addAll(partStus);
				st = end;
			}
			return studentList;
		}
	}

	@Override
	public List<Student> findPartAllStudentByStuIds(String[] studentIds) {
		String sql = "select id,school_id,class_id,student_name,student_code,sex from base_student where id IN ";
		return queryForInSQL(sql, null, studentIds, new MultiRowMapper<Student>(){

			@Override
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				Student ent= new Student();
				ent.setId(rs.getString("id"));
				ent.setSchoolId(rs.getString("school_id"));
				ent.setStudentName(rs.getString("student_name"));
				ent.setClassId(rs.getString("class_id"));
				ent.setStudentCode(rs.getString("student_code"));
				ent.setSex(rs.getInt("sex"));
				return ent;
			}
			
		});
	}


	@Override
	public List<Student> findBySchoolIdStudentNameStudentCode(String unitId, String studentName, String studentCode) {
		String sql = "select id,student_name,class_id from base_student where school_id=? ";
		List<Object> objList = new ArrayList<Object>();
		objList.add(unitId);
		if(StringUtils.isNotBlank(studentName)) {
			sql=sql+" and student_name=? ";
			objList.add(studentName);
		}
		if(StringUtils.isNotBlank(studentCode)) {
			sql=sql+" and student_code=? ";
			objList.add(studentCode);
		}
		
		return query(sql, objList.toArray(new Object[] {}), new MultiRowMapper<Student>(){

			@Override
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				Student ent= new Student();
				ent.setId(rs.getString("id"));
				ent.setClassId(rs.getString("class_id"));
				ent.setStudentName(rs.getString("student_name"));
				return ent;
			}
			
		});
		
	}

	@Override
	public List<Student> findListBlendClassIds(String[] classIds) {
		String sql = "select id,school_id,class_id,student_name,old_name,student_code,unitive_code,identitycard_type,identity_card,sex,birthday,mobile_phone,"
				+ "homepage,email,link_phone,link_address,dir_id,file_path,remark from base_student where  class_id IN "+SQLUtils.toSQLInString(classIds)+" and is_deleted = 0 and is_leave_school=0";
		List<Student> list = query(sql,new MultiRowMapper<Student>() {

			@Override
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				Student student = new Student();
				student.setId(rs.getString("id"));
				student.setSchoolId(rs.getString("school_id"));
				student.setClassId(rs.getString("class_id"));
				student.setStudentName(rs.getString("student_name"));
				student.setOldName(rs.getString("old_name"));
				student.setStudentCode(rs.getString("student_code"));
				student.setUnitiveCode(rs.getString("unitive_code"));
				student.setIdentitycardType(rs.getString("identitycard_type"));
				student.setIdentityCard(rs.getString("identity_card"));
				student.setSex(rs.getInt("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setMobilePhone(rs.getString("mobile_phone"));
				student.setHomepage(rs.getString("homepage"));
				student.setEmail(rs.getString("email"));
				student.setLinkPhone(rs.getString("link_phone"));
				student.setLinkAddress(rs.getString("link_address"));
				student.setDirId(rs.getString("dir_id"));
				student.setFilePath(rs.getString("file_path"));
				student.setRemark(rs.getString("remark"));
				return student;
			}
		});
		if(list==null){
			list =new ArrayList<>();
		}
		sql = "select bs.id,bs.school_id,ss.class_id,student_name,old_name,student_code,unitive_code,identitycard_type,identity_card,sex,birthday,mobile_phone,"
				+ "homepage,email,link_phone,link_address,dir_id,file_path,remark from base_student bs,base_teach_class_stu ss " +
				"where ss.class_id in "+SQLUtils.toSQLInString(classIds)+" and ss.student_id=bs.id and ss.is_deleted=0  and bs.is_deleted = 0 and bs.is_leave_school=0 ";
		list.addAll(query(sql,new MultiRowMapper<Student>() {
			@Override
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				Student student = new Student();
				student.setId(rs.getString("id"));
				student.setSchoolId(rs.getString("school_id"));
				student.setClassId(rs.getString("class_id"));
				student.setStudentName(rs.getString("student_name"));
				student.setOldName(rs.getString("old_name"));
				student.setStudentCode(rs.getString("student_code"));
				student.setUnitiveCode(rs.getString("unitive_code"));
				student.setIdentitycardType(rs.getString("identitycard_type"));
				student.setIdentityCard(rs.getString("identity_card"));
				student.setSex(rs.getInt("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setMobilePhone(rs.getString("mobile_phone"));
				student.setHomepage(rs.getString("homepage"));
				student.setEmail(rs.getString("email"));
				student.setLinkPhone(rs.getString("link_phone"));
				student.setLinkAddress(rs.getString("link_address"));
				student.setDirId(rs.getString("dir_id"));
				student.setFilePath(rs.getString("file_path"));
				student.setRemark(rs.getString("remark"));
				return student;
			}
		}));
		return list;
	}

	/**
	 * 根据 学校ids 查询各个学校人数
	 * @param schoolIds
	 * @return
	 */
	public Map<String,Integer> countListBySchoolIds(String[] schoolIds){
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	public Map<String, Integer> countMapByGradeIds(String[] gradeIds){
		String sql = "select  c.grade_id,count(c.grade_id) from base_student s inner join base_class c on  s.class_id=c.id and s.is_deleted = 0 and s.is_leave_school=0  and c.is_deleted=0 and c.is_graduate =0 and c.grade_id in";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(1);
					}
					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(2);
					}
				}, " Group By c.grade_id");
	}
}
