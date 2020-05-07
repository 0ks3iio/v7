package net.zdsoft.syncdata.custom.guangyuan.service;

public interface GySyncService {

	void saveSchool(String url);

	void saveTeacher(String url);

	void saveEdu(String url);

	void saveUserToPassport();

	void pushUnit(String url);

	void pushUser(String url);

}
