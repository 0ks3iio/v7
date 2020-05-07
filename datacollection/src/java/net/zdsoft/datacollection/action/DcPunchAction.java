package net.zdsoft.datacollection.action;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.datacollection.entity.DcDataScore;
import net.zdsoft.datacollection.service.DcDataScoreService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

@RequestMapping("/dc/punch")
@Controller
public class DcPunchAction extends BaseAction {

	private static final String EXTEND_MINUTES = "dc_score_extend_minutes";

	@Autowired
	private DcDataScoreService dcDataScoreService;
	

	@Autowired
	private SystemIniRemoteService systemIniRemoteService;

	@ResponseBody
	@RequestMapping("/chekPass/{reportId}/{dataId}")
	public String checkPass(@PathVariable String reportId, @PathVariable String dataId, String state) {
		DcDataScore score = dcDataScoreService.findOne(dataId);
		if(score != null){
			score.setState(NumberUtils.toInt(state));
			dcDataScoreService.save(score);
			return "1";
		}
		else{
			return "0";
		}
	}

	@ResponseBody
	@RequestMapping("/applyPunchScore")
	public String applyPunchScore() {

		Date date = new Date();

		Date dateMin = new Date();
		dateMin = DateUtils.setHours(dateMin, 7);
		dateMin = DateUtils.setMinutes(dateMin, 0);
		dateMin = DateUtils.setSeconds(dateMin, 0);
		int score = 0;

		if (date.before(dateMin)) {
			score = 0;
			JSONObject json = new JSONObject();
			json.put("tip", "未到考勤时间（7:00），请确认当前是否属于正常上班时间。");
			json.put("score", score);
			return json.toJSONString();
		}

		int extendMinutes = NumberUtils.toInt(systemIniRemoteService.findValue(EXTEND_MINUTES));
		if (extendMinutes <= 0) {
			extendMinutes = 2;
		}
		
		extendMinutes = 0;

//		Date date1 = DateUtils.setHours(date, 9);
//		date1 = DateUtils.setMinutes(date1, extendMinutes);
//		date1 = DateUtils.setSeconds(date1, 0);
//		if (date.before(date1))
//			score = 5;
		Date date2 = DateUtils.setHours(date, 8);
		date2 = DateUtils.setMinutes(date2, 45 + extendMinutes);
		date2 = DateUtils.setSeconds(date2, 0);
		if (date.before(date2))
			score = 5;

		String[] ss = new String[] { "无论何事，若真的在乎且努力，结果总不会差。早安！", "偶尔的自我陶醉一番，人不用活得那么严肃的。早安！",
				"向日葵告诉我，只要面对着阳光努力向上，日子就会变得单纯而美好。早安！", "熬夜，是因为没有勇气结束这一天；赖床，是因为没有勇气开始这一天。祝你早安！",
				"在每天的清晨，我的祝福都会陪你慢慢醒来，慢慢实现，慢慢成功。祝你天天有个好心情！",
				"陶醉于清晨微微的清风，让自然的味道扑向你的心扉，用心感受，你会发现：希望在向你招手，梦想在向你奔跑，幸福在向你靠拢。早安！",
				"一天第一个问候送给你，让你有个好心情；第一个祝福送给你，祝你一天工作顺心；第一个愿望送给你，愿你永远幸福又温馨。早安！",
				"霞光万道，只为今朝绚烂；青草带露，娇脆欲滴美好；一声鸟鸣，新的一天拂晓。好的心情是快乐一天的开始，早安，我的朋友！",
				"晨曦的第一缕曙光照亮了你小窗，晨风的第一丝舒爽轻拂了你脸庞，我的祝福也如约来到你身旁，只为你带来美好祝愿。早上好！",
				"只要你的心是晴的，人生就没有雨天。就像好事情总是发生在那些微笑着的人身上,保持微笑。早安！",
				"清晨的风，驱逐烦躁的心情；清晨的雾，阻挡忧伤的脚步；清晨的露，谱写快乐的音符；清晨的祝福，指引幸福的旅途。早安！",
				"过不去的过去是跟自己过不去，放下包袱才能轻松前行。美好的明天在向我们招手，轻松的开始全新的一天吧，早安朋友！",
				"美好的一天开始了，每天给自己一个希望，试着不为明天而烦恼，不为昨天而叹息，只为今天更美好。早安，人们！", "我叫太阳每天把幸福的阳光洒在你身上，我叫月亮每天给你一个甜美的梦境，祝愿你事事如意！",
				"悄悄的打开快乐的大门，欢迎你进来，美好的一天从这一刻开始！早安，朋友！", "人应该要为梦想追逐，不管现实给了你多少黑眼圈。勇敢的追求自己的梦想吧！早上好！每一天都是新的开始！",
				"蓝天吻着海洋，海洋偎着蓝天，我把祝福写在蓝天碧波上。当太阳在海边升起，我的祝福就会来到你的身边，早安！",
				"睁开眼，缓一缓；快起床，伸懒腰；笑一笑，美好的一天又开始了。早安，祝你今天好心情，工作顺利，生活甜美！",
				"我的心意，和清晨的阳光一起，把快乐带到你的心底，让你和我一起，伴随清风吹起，把一天的快乐举起，祝早安！",
				"把昨天的疲惫让梦带走，让今天的激情与风交流，伸出你的手，握紧我真诚的问候，为你在新的一天加油！祝早安！",
				"一丝心的悸动，一缕情的关怀，在这寂寂的清晨，送去我点点的问候。愿你烦恼莫沾，幸福无边，亲爱的朋友，早安！",
				"每一个日出，总想带去希望的问候：让每天的空气都是清新、让每天的心情都是舒展、让每天的收获都是充实，早安！",
				"做一个平静的人，做一个善良的人，做一个微笑挂在嘴边，快乐放在心上的人。愿我小小的问候带给你快乐，早安，朋友！",
				"睁开明亮的双眼，除去睡意的干扰，舒展美丽的笑脸，拥抱快乐的一天。早安，朋友，愿你今天好心情，生活工作都舒心！",
				"早安！朋友，请接受我的祝福：让微笑爬上脸庞，让快乐充满心房，让好运陪在身旁，让成功依偎胸膛，让友情在你心中珍藏。",
				"清晨的风，驱逐烦躁的心情；清晨的雾，阻挡忧伤的脚步；清晨的露，谱写快乐的音符；清晨的祝福，指引幸福的旅途。早安！",
				"清晨曙光初现，幸福在你身边，中午艳阳高照，微笑在你心间，傍晚日落西山，欢乐随你一天，关心你的朋友，从早晨到夜晚！",
				"努力是人生的一种精神状态，往往最美的不是成功的那一刻，而是那段努力奋斗的过程。朋友，愿你努力后的明天更精彩，早安！",
				"阳光洒在心中，惊醒一帘幽梦；问候如沐清风，蓝了朗朗晴空；泛起甜甜笑容，心事一切随风。愿你开启快乐一天。朋友，早安！",
				"一天好心情不仅从清晨开始，而且从心晨开始。保持好状态，让心晨明朗阳光，给心城自由开放，还新晨如云舒卷。朋友，早安。",
				"不是每天都有阳光，不是每天都会凉爽，只要我们心中有阳光，人生总会是晴朗，只要我们心中有凉爽，每天都会充满希望。早安！",
				"生命就是一个绣花。我们从底下看，乱七八糟的走线，我们从上面看，则是一朵花。愿你这朵生命之花时刻绚烂夺目。早安，朋友！",
				"褪去疲劳，忘却烦躁，放眼今天，无限美好。好朋友的祝福总是很早，愿你每天微笑，心情大好，祝你健康幸福，万事顺意，早安！",
				"不求锁定结局，只求曾经努力；不求事事顺意，只求心情美丽；不求左右别人，只求善待自己；不求马到成功，只求坚定不移。早安！",
				"时光依旧美好，遥看鲜花绿草，清歌一曲逍遥，生活如意美妙，岁月点点滴滴，不忘你我情谊。记得让自己开心，我的朋友。早上好。",
				"走过一些路，才知道辛苦；登过一些山，才知道艰难；趟过一些河，才知道跋涉；道一声问候，才知道这就是幸福。早安，我的朋友！",
				"早安！当你睁开双眼，祝福已飞到你面前，带着快乐的旋律，愉悦的心态，甜蜜的浪漫和美妙的生活伴你度过美好的一天！",
				"春日清晨，祝福飘香；花语鸟鸣，溪水歌唱；烦恼消除，快乐飞翔；上班出行，赏尽春光；指动荧屏，问候情长；祝你今早，拥抱欢畅！",
				"你笑，全世界都跟着你笑；你哭，全世界只有你一人哭。不是环境决定心情，而是心情决定着环境，学会乐观，愿你笑容永远灿烂！早安！",
				"放下你的成熟，收起你的稳重，脱掉你的圆滑，穿上你的童真，释放你的压力，冲出你的焦虑，绽放你的活力！一句早安朋友，愿你好运久久！",
				"早安朋友，请接受我的祝福：让微笑爬上脸庞，让快乐充满心房，让好运陪在身旁，让成功依偎胸膛，让幸福尽情绽放，让友情在你心中珍藏。",
				"人生的每一次付出就像在山谷当中的喊声，你没有必要期望要谁听到，但那延绵悠远的回音，就是生活对你最好的回报，新的一天新的开始！早安。",
				"清晨的霞光无比灿烂，扫除你一切的忧烦，清晨的露珠无比晶莹，装点你美丽的心情，祝早安带笑意，心情快乐无比！",
				"新的一天，新的苏醒，新的太阳，新的暖风，新的空气，新的呼吸，新的快乐，新的心境，新的脚步，新的前行，新的一天，新的问候，早安！" };

		JSONObject json = new JSONObject();
		if (score > 0) {
			DcDataScore dataScore;
			String id = DigestUtils.md5Hex(DateFormatUtils.format(new Date(), "yyyyMMdd") + getLoginInfo().getUserId());
			dataScore = dcDataScoreService.findOne(id);
			if (dataScore == null) {
				dataScore = new DcDataScore();
				dataScore.setId(id);
				dataScore.setScore(score);
				dataScore.setCreateUserId(getLoginInfo().getUserId());
				dataScore.setCreationTime(new Date());
				dataScore.setDeptId(getLoginInfo().getDeptId());
				dataScore.setDescription(DateFormatUtils.format(new Date(), "yyyy/MM/dd") + " 考勤加分");
				dataScore.setFinalOperationUserId("");
				dataScore.setOperationUserId("");
				dataScore.setWoDate(DateFormatUtils.format(new Date(), "yyyy/MM/dd") );
				dataScore.setScoreType(5);
				dataScore.setState(3);
				dataScore.setTeacherId(getLoginInfo().getOwnerId());
				dataScore.setUnitId(getLoginInfo().getUnitId());
				dcDataScoreService.save(dataScore);
			} else {
				score = 0;
				json.put("tip", "今天已经加了" + dataScore.getScore() + "分，请明天继续加油！");
				json.put("score", score);
				return json.toJSONString();
			}
		} else {
			score = 0;
			json.put("tip", "今天已经过了考勤加分时间（8:45及以前+5），请明天继续加油！");
			json.put("score", score);
			return json.toJSONString();
		}

		json.put("score", score);
		json.put("tip", ss[RandomUtils.nextInt(0, ss.length)]);
		return json.toJSONString();
	}

	public static void main(String[] args) {

	}

}
