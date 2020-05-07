package net.zdsoft.desktop.service;

import java.io.UnsupportedEncodingException;

public interface SyncAnhuidataService {

	void syncData(String ahUserId, String[] ahUnitIds) throws UnsupportedEncodingException;
}
