package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccLeaveWordDao;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccLeaveWord;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccLeaveWordService;
import net.zdsoft.eclasscard.data.utils.EccNeedServiceUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vdurmont.emoji.EmojiParser;
@Service("eccLeaveWordService")
public class EccLeaveWordServiceImpl extends BaseServiceImpl<EccLeaveWord, String> implements EccLeaveWordService{
	
	@Autowired
	private EccLeaveWordDao eccLeaveWordDao;
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private EccInfoService eccInfoService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	
	private static OpenApiOfficeService openApiOfficeService;
    public OpenApiOfficeService getOpenApiOfficeService() {
        if (openApiOfficeService == null) {
            openApiOfficeService = Evn.getBean("openApiOfficeService");
            if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
        }
        return openApiOfficeService;
    }
	@Override
	protected BaseJpaRepositoryDao<EccLeaveWord, String> getJpaDao() {
		return eccLeaveWordDao;
	}

	@Override
	protected Class<EccLeaveWord> getEntityClass() {
		return EccLeaveWord.class;
	}

	@Override
	public void saveLeaveWord(EccLeaveWord leaveWord,String type,String basePath) {
		String familyId = "";
		if(EccConstants.ECC_LEAVE_WORD_1.equals(type)){
			familyId = leaveWord.getReceiverId();
		}else{
			familyId = leaveWord.getSenderId();
		}
		Family family= SUtils.dc(familyRemoteService.findOneById(familyId),Family.class);
		User user = User.dc(userRemoteService.findByOwnerId(familyId));
		Set<String> ownerIds =Sets.newHashSet();
		ownerIds.add(familyId);
		if(family!=null && user != null){
			List<User> userList = null;
			if ( StringUtils.isNotBlank(family.getMobilePhone()) ) {
				userList = SUtils.dt(userRemoteService.findByMobilePhones(User.OWNER_TYPE_FAMILY, family.getMobilePhone()), User.class);
				userList = userList.stream().filter(u -> {
					return u.getUserState().equals(Integer.valueOf(1)) && PWD.decode(u.getPassword()).equals(PWD.decode(user.getPassword()));
				}).collect(Collectors.toList());
			}
			ownerIds = EntityUtils.getSet(userList,User::getOwnerId);
		}
		String key = "";
		List<EccLeaveWord> leaveWords = Lists.newArrayList();
		for(String ownerId:ownerIds){
			EccLeaveWord word = copyLeaveWord(leaveWord);
			if(EccConstants.ECC_LEAVE_WORD_1.equals(type)){
				//学生发送，给家长端发消息
				word.setReceiverId(ownerId);
				sendMsgToFamily(word,ownerId,basePath);
				key = word.getSenderId() + ownerId;
				RedisUtils.incrby(EccConstants.LEAVE_WORD_CACHE_HEAD + key+"num", 1);
			}else{
				word.setSenderId(ownerId);
				key = word.getReceiverId() + ownerId;
				//家长端发送，给班牌推消息
				sendMsgToCard(word);
				RedisUtils.incrby(EccConstants.LEAVE_WORD_CACHE_HEAD + ownerId +word.getReceiverId()+"num", 1);
			}
			RedisUtils.set(EccConstants.LEAVE_WORD_CACHE_HEAD+key,word.getContent());
			leaveWord.setContent(EmojiParser.parseToAliases(word.getContent()));
			leaveWords.add(word);
		}
		if(CollectionUtils.isNotEmpty(leaveWords)){
			saveAll(leaveWords.toArray(new EccLeaveWord[leaveWords.size()]));
		}
	}

	private EccLeaveWord copyLeaveWord(EccLeaveWord leaveWord) {
		EccLeaveWord word = new EccLeaveWord();
		word.setId(UuidUtils.generateUuid());
		word.setContent(leaveWord.getContent());
		word.setCreationTime(leaveWord.getCreationTime());
		word.setModifyTime(leaveWord.getModifyTime());
		word.setReceiverId(leaveWord.getReceiverId());
		word.setSenderId(leaveWord.getSenderId());
		word.setState(leaveWord.getState());
		word.setUnitId(leaveWord.getUnitId());
		return word;
	}
	
	private void sendMsgToCard(EccLeaveWord leaveWord) {
		Student student=SUtils.dc(studentRemoteService.findOneById(leaveWord.getReceiverId()),Student.class);
		if(student!=null){
			List<EccInfo> eccInfos = eccInfoService.findByClassIdIn(new String[]{student.getClassId()});
			Set<String> sids = EntityUtils.getSet(eccInfos, EccInfo::getId);
			EccNeedServiceUtils.postLeaveWord(sids,student.getId(),leaveWord.getSenderId(),leaveWord.getContent(),DateUtils.date2StringByMinute(leaveWord.getCreationTime()));
		}
	}

	private void sendMsgToFamily(EccLeaveWord leaveWord,String familyId,String basePath) {
		Student student=SUtils.dc(studentRemoteService.findOneById(leaveWord.getSenderId()),Student.class);
		Set<String> uIds = Sets.newHashSet();
		User user = User.dc(userRemoteService.findByOwnerId(familyId));
		JSONArray msgarr = new JSONArray();
		if(student!=null && user != null){
			uIds.add(user.getId());
			String title = "您收到一条"+student.getStudentName()+"的留言";
			String url = basePath+"/mobile/open/eclasscard/leavae/word/detail?studentId="+student.getId()+"&familyId="+familyId;
			String content = leaveWord.getContent();
			fillWeiKeJson(title, url, uIds, content, msgarr);
			try {
				if(getOpenApiOfficeService()!=null && msgarr.size()>0){
					getOpenApiOfficeService().pushWeikeMessage("LEAVE_WORD", msgarr.toJSONString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	private void fillWeiKeJson(String title,String url,Set<String> uIds,String content,JSONArray msgarr){
		// 往办公公众号推送消息
		JSONArray userIds = new JSONArray();
		String[] rowsContent = new String[] { content };
		for(String id:uIds){
			userIds.add(id);
		}
		JSONObject msg = new JSONObject();
		msg.put("userIdArray", userIds);

		msg.put("msgTitle", title);
		msg.put("jumpType", "0");
		msg.put("url", url);
		msg.put("rowsContent", rowsContent);

		msgarr.add(msg);
	}

	@Override
	public List<EccLeaveWord> findBySenderAndReceiverId(String receiverId,
			String senderId,Date lastTime,Pagination page) {
		List<EccLeaveWord> eccLeaveWords = Lists.newArrayList();
		Specification<EccLeaveWord> specification = new Specification<EccLeaveWord>() {
            @Override
            public Predicate toPredicate(Root<EccLeaveWord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                
                Predicate p = cb.and(cb.equal(root.get("receiverId").as(String.class), receiverId), cb.equal(root.get("senderId").as(String.class), senderId));
                Predicate p2 = cb.and(cb.equal(root.get("receiverId").as(String.class), senderId), cb.equal(root.get("senderId").as(String.class), receiverId));
                p = cb.or(p, p2); 
                p = cb.and(p,cb.and(cb.lessThanOrEqualTo(root.get("creationTime").as(Date.class), lastTime))); 
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("creationTime").as(Date.class)));

                cq.where(p).orderBy(orderList);
                return cq.getRestriction();
            }
		};
        if (page != null) {
        	Pageable pageable = Pagination.toPageable(page);
        	Page<EccLeaveWord> findAll = eccLeaveWordDao.findAll(specification, pageable);
        	page.setMaxRowCount((int) findAll.getTotalElements());
        	eccLeaveWords = findAll.getContent();
        }
        else {
        	eccLeaveWords = eccLeaveWordDao.findAll(specification);
        }
        for(EccLeaveWord leaveWord:eccLeaveWords){
        	leaveWord.setContent(EmojiParser.parseToUnicode(leaveWord.getContent()));
        }
        	
        return eccLeaveWords;
	}

	@Override
	public String findLastWordByCache(String key) {
		return RedisUtils.get(EccConstants.LEAVE_WORD_CACHE_HEAD+key);
	}

	@Override
	public void updateStatus(String receiverId, String senderId, Date time) {
		eccLeaveWordDao.updateStatus(receiverId,senderId,time);
	}

	@Override
	public List<EccLeaveWord> findByReceiverIdsNotRead(String[] stuIds) {
		return eccLeaveWordDao.findByReceiverIdsNotRead(stuIds);
	}

}
