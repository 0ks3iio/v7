//package net.zdsoft.szxy.base.dao;
//
//import net.zdsoft.szxy.base.entity.EduInfo;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
///**
// * @author shenke
// * @since 2019/3/20 下午4:34
// */
//@Repository
//public interface EduInfoDao extends JpaRepository<EduInfo, String> {
//
//    @Modifying
//    @Query(value = "update EduInfo set isDeleted=1, modifyTime=:#{new java.util.Date()} where id=?1")
//    void deleteEduInfosByUnitId(String unitId);
//}
