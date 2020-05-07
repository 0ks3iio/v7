package net.zdsoft.newstusys.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.newstusys.entity.StusysStudentBak;
import net.zdsoft.newstusys.service.StusysStudentBakService;

/**
 * 
 * @author weixh
 * 2018年9月25日	
 */
@Controller
@RequestMapping("/newstusys/bak")
public class NewStusysBakAction extends BaseAction {
	@Autowired
	private StusysStudentBakService stusysStudentBakService;
	
	@ResponseBody
	@RequestMapping("/schBakSave")
	public String schBak() {
		String unitId = getLoginInfo().getUnitId();
		String status = RedisUtils.get(StusysStudentBak.BAK_PREFIX+unitId+StusysStudentBak.BAK_STATUS);
		if(StusysStudentBak.BAK_STATUS_ING.equals(status)) {
			return success("数据已经在存档中...");
		}
		BakThread bt = new BakThread(unitId);
		bt.start();
		return success("数据已经在存档中...");
	}
	
	class BakThread extends Thread {
		private String schId;
		
		public BakThread(String schId) {
			super();
			this.schId = schId;
		}

		public String getSchId() {
			return schId;
		}

		public void setSchId(String schId) {
			this.schId = schId;
		}

		@Override
		public void run() {
			stusysStudentBakService.saveStudentBak(schId);
		}
		
	}

}
