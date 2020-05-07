package net.zdsoft.syncdata.custom.zy.action;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.zy.entity.ZyCard;
import net.zdsoft.syncdata.custom.zy.service.ZyCardService;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class ZyCardSyncDataJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(">>>>一卡通同步任务开始！");
		ZyCardService zyCardService = (ZyCardService) Evn.getBean("zyCardService");
		TeacherRemoteService teacherRemoteService = (TeacherRemoteService) Evn.getBean("teacherRemoteService");
		StudentRemoteService studentRemoteService = (StudentRemoteService) Evn.getBean("studentRemoteService");
		try {
			List<ZyCard> cardList = zyCardService.getAllCards();
			List<String[]> studentList = new ArrayList<String[]>();
			List<String[]> techerList = new ArrayList<String[]>();
			for (ZyCard card : cardList) {
				String[] cardInfo = new String[2];
				cardInfo[0] = card.getIdentityCard();
				cardInfo[1] = card.getCardNumber();
				System.out.println(cardInfo[0]+"-----"+cardInfo[1]);
				if ("学生".equals(card.getType())) {
					studentList.add(cardInfo);
				} else {
					techerList.add(cardInfo);
				}
			}
			teacherRemoteService.updateCardNumber(techerList);
			studentRemoteService.updateCardNumber(studentList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(">>>>一卡通同步任务结束！");
	}

}
