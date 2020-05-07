package net.zdsoft.bigdata.extend.data.biz;


import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.google.common.collect.Maps;

import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;

public class BgCalConsumer implements Runnable {
	private UserProfile up;
	private CountDownLatch cdl;
	private BgCalStorage bcs;
	
	public BgCalConsumer(UserProfile up,CountDownLatch cdl,BgCalStorage bcs) {
		super();
		this.up = up;
		this.cdl = cdl;
		this.bcs = bcs;
	}

	@Override
	public void run() {
		BgCalService bgCalService = (BgCalService) Evn
				.getBean("bgCalService");
		int index=0;
		Map<String,Json> jsonMap = Maps.newHashMap();
		try {
			while(cdl.getCount()==BgCalStorage.THREAD_MUNBER){
				try {
					Json j = bcs.pollTimeOut();
					if(j!=null){
						index++;
						jsonMap.put(j.getString(up.getPrimaryKey()), j);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(index==1000){
					bgCalService.dealUserTagCalDetail(up, jsonMap,bcs);
					index = 0;
					jsonMap.clear();
				}
			}
			if(index>0){
				bgCalService.dealUserTagCalDetail(up, jsonMap,bcs);
				index = 0;
				jsonMap.clear();
			}
			for(int i=0;i<1000;i++){
				Json j = bcs.poll();
				if(j==null)break;
				index++;
				jsonMap.put(j.getString(up.getPrimaryKey()), j);
			}
			if(index>0){
				bgCalService.dealUserTagCalDetail(up, jsonMap,bcs);
				index = 0;
				jsonMap.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cdl.countDown();
	}

}
