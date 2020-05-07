package net.zdsoft.newstusys.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.newstusys.dao.StusysStudentBakJdbcDao;
import net.zdsoft.newstusys.entity.StusysStudentBak;

/**
 * 
 * @author weixh
 * 2018年9月18日	
 */
@Repository
public class StusysStudentBakJdbcDaoImpl extends BaseDao<StusysStudentBak> implements StusysStudentBakJdbcDao {

	public StusysStudentBak setField(ResultSet rs) throws SQLException {
		StusysStudentBak student = new StusysStudentBak();
//		try {
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
//			student.setCredentialType(rs.getString("credential_type"));
//			student.setCredentialCode(rs.getString("credential_code"));
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
			student.setSource(rs.getString("source"));
			student.setCooperateForm(rs.getString("cooperate_form"));
			student.setTrainStation(rs.getString("train_station"));
			student.setOldStudentCode(rs.getString("old_student_code"));
			student.setFamilyMobile(rs.getString("family_mobile"));
			student.setReligion(rs.getString("religion"));
//			student.setReturnedchinese(rs.getInt("returnedchinese"));
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
//			student.setSpecialty(rs.getString("specialty"));
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
			student.setIsLocalSchoolEnrollment(rs.getString("is_local_school_enrollment"));
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
			student.setSocialRecruitmentType(rs.getString("social_recruitment_type"));
			student.setNowChildNum(rs.getInt("now_child_num"));
			student.setBirthRank(rs.getInt("birth_rank"));
			student.setRxqk(rs.getString("rxqk"));
			student.setIsGetCertification(rs.getString("is_get_certification"));
			student.setIsFiveHigherVocational(rs.getString("is_five_higher_vocational"));
			student.setBelongContinents(rs.getString("belong_continents"));
			student.setForeignerStudyTime(rs.getString("foreigner_study_time"));
			student.setIsVocationalTechClass(rs.getString("is_vocational_tech_class"));
			student.setIsRegularClass(rs.getString("is_regular_class"));
			student.setFingerprint2(rs.getString("fingerprint2"));
			student.setStudentType(rs.getString("student_type"));
			student.setVerifyCode(rs.getString("verify_code"));
			student.setRewardRemark(rs.getString("reward_remark"));
			student.setRegisterStreet(rs.getString("register_Street"));
			student.setAcadyear(rs.getString("acadyear"));
			student.setSemester(rs.getInt("semester"));
			student.setStudentId(rs.getString("student_id"));
			student.setTeacherId(rs.getString("teacher_id"));
			student.setClassName(rs.getString("class_name"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		先以7.0的学生字段为主
//		student.set(rs.getString("open_user_status"));
//		student.set(rs.getString("student_classify"));
//		student.set(rs.getString("is_new_grads"));
//		student.set(rs.getString("enrol_way"));
//		student.set(rs.getString("is_dgregister"));
//		student.set(rs.getString("year_dgregister"));
//		student.set(rs.getString("dgregister_aim"));
//		student.set(rs.getString("is_teacher_child"));
//		student.set(rs.getString("zk_care_result"));
//		student.set(rs.getString("old_school_place"));
//		student.set(rs.getString("zk_full_result"));
		return student;
	}
	
	public List<StusysStudentBak> findBySchIdParams(String schId, String stuName, String stuCode, 
			String idCard){
		StringBuilder sql = new StringBuilder("SELECT * FROM base_student_history WHERE school_id=?");
		List<Object> args = new ArrayList<>();
		args.add(schId);
		if(StringUtils.isNotEmpty(stuName)) {
			sql.append(" AND student_name like ?");
			args.add("%"+stuName+"%");
		}
		if(StringUtils.isNotEmpty(stuCode)) {
			sql.append(" AND student_code like ?");
			args.add("%"+stuCode+"%");
		}
		if(StringUtils.isNotEmpty(idCard)) {
			sql.append(" AND identity_card like ?");
			args.add("%"+idCard+"%");
		}
		sql.append(" ORDER BY class_id, acadyear desc, semester, student_code, student_name ");
		return query(sql.toString(), args.toArray(), new MultiRow());
	}
	
	public List<StusysStudentBak> findStuByStuIdSemester(String acadyear, String semester, String stuId){
		String sql = "SELECT * FROM base_student_history WHERE acadyear=? and semester=? and student_id=?";
		return query(sql, new Object[] {acadyear, semester, stuId}, new MultiRow());
	}
	
	public List<StusysStudentBak> findSemesterBySchId(String schId){
		String sql = "SELECT DISTINCT acadyear,semester FROM base_student_history WHERE school_id=? ORDER BY acadyear DESC, semester DESC";
		return query(sql, schId, new MultiRowMapper<StusysStudentBak>() {

			public StusysStudentBak mapRow(ResultSet rs, int rowNum) throws SQLException {
				StusysStudentBak bak = new StusysStudentBak();
				bak.setAcadyear(rs.getString("acadyear"));
				bak.setSemester(rs.getInt("semester"));
				return bak;
			}
		});
	}
	@Override
	public void deleteByStuIds(String acadyear,String semester,String[] studentIds){
		String sql = "delete FROM base_student_history WHERE acadyear=? and semester=? and student_id in ";
		updateForInSQL(sql, new Object[]{acadyear,semester},studentIds);
	}
}
