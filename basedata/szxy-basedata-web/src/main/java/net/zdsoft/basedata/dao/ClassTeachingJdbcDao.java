package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.ClassTeaching;

import java.util.List;

public interface ClassTeachingJdbcDao {
    public List<ClassTeaching> findClassTeachingListByTeacherId(String unitId, String[] teaherIds);
    public List<ClassTeaching> findClassTeachingListByTeacherId(String[] teaherIds);
    public List<ClassTeaching> findByIsDeletedAndSubjectIdIn(int isDeletedFalse, String[] subjectIds);
    public List<ClassTeaching> findClassTeachingListHistoryByTeacherId(String unitId, String[] teaherIds);
}
