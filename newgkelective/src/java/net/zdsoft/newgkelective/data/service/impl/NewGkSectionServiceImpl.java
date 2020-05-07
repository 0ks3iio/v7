package net.zdsoft.newgkelective.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.dao.NewGkSectionDao;
import net.zdsoft.newgkelective.data.entity.NewGkSection;
import net.zdsoft.newgkelective.data.entity.NewGkSectionBegin;
import net.zdsoft.newgkelective.data.entity.NewGkSectionEnd;
import net.zdsoft.newgkelective.data.entity.NewGkSectionResult;
import net.zdsoft.newgkelective.data.service.NewGkSectionBeginService;
import net.zdsoft.newgkelective.data.service.NewGkSectionEndService;
import net.zdsoft.newgkelective.data.service.NewGkSectionResultService;
import net.zdsoft.newgkelective.data.service.NewGkSectionService;

@Service("newGkSectionService")
public class NewGkSectionServiceImpl extends BaseServiceImpl<NewGkSection, String> implements NewGkSectionService {

	@Autowired
	private NewGkSectionDao newGkSectionDao;
	@Autowired
	private NewGkSectionBeginService newGkSectionBeginService;
	@Autowired
	private NewGkSectionResultService newGkSectionResultService;
	@Autowired
	private NewGkSectionEndService newGkSectionEndService;
	
	@Override
	protected BaseJpaRepositoryDao<NewGkSection, String> getJpaDao() {
		return newGkSectionDao;
	}

	@Override
	protected Class<NewGkSection> getEntityClass() {
		return NewGkSection.class;
	}

	@Override
	public void saveSection(NewGkSection newGkSection, List<NewGkSectionBegin> beginList,
			List<NewGkSectionResult> resultList) {
		newGkSectionDao.save(newGkSection);
		if(CollectionUtils.isNotEmpty(beginList)) {
			newGkSectionBeginService.saveAll(beginList.toArray(new NewGkSectionBegin[0]));
		}
		if(CollectionUtils.isNotEmpty(resultList)) {
			newGkSectionResultService.saveAll(resultList.toArray(new NewGkSectionResult[0]));
		}
	}

	@Override
	public void updateSection(NewGkSection newGkSection, String[] noJoinId) {
		newGkSectionBeginService.updateJoinBySectionId(noJoinId,newGkSection.getId());
		newGkSectionDao.save(newGkSection);
	}

	@Override
	public void deleteBySectionId(String sectionId) {
		newGkSectionResultService.deleteBySectionIdAndArrangeType(sectionId,null);
		newGkSectionEndService.deleteBySectionId(sectionId);
		newGkSectionBeginService.deleteBySectionId(sectionId);
		newGkSectionDao.deleteById(sectionId);
	}

	@Override
	public void deleteResultBySectionId(String sectionId) {
		newGkSectionResultService.deleteBySectionIdAndArrangeType(sectionId,"1");
		newGkSectionEndService.deleteBySectionId(sectionId);
		NewGkSection newGkSection = findOne(sectionId);
		if(newGkSection!=null) {
			newGkSection.setModifyTime(new Date());
			newGkSection.setRemark("");
			newGkSection.setStat("0");
			newGkSectionDao.save(newGkSection);
		}
		//修改安排人数
		List<NewGkSectionResult> list = newGkSectionResultService.findListBy(new String[] {"sectionId","arrangeType"}, new String[] {sectionId,"0"});
		//统计安排人数
		Map<String,Integer> sub3Map=new HashMap<>();
		Map<String,Integer> sub2Map=new HashMap<>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(NewGkSectionResult r:list) {
				String scource=r.getStudentScource();
				String[] arr = scource.split(",");
				for(String s:arr) {
					String[] arr2 = s.split("-");
					if(!sub3Map.containsKey(arr2[0])) {
						sub3Map.put(arr2[0], Integer.parseInt(arr2[1]));
					}else {
						sub3Map.put(arr2[0], sub3Map.get(arr2[0])+Integer.parseInt(arr2[1]));
					}
					//2科
					String[] arr33 = arr2[1].split("_");
					for(int i=0;i<arr33.length-1;i++) {
						for(int j=i+1;j<arr33.length;j++) {
							String s1=arr33[i]+"_"+arr33[j];
							if(!sub2Map.containsKey(s1)) {
								sub2Map.put(s1, Integer.parseInt(arr2[1]));
							}else {
								sub2Map.put(s1, sub3Map.get(s1)+Integer.parseInt(arr2[1]));
							}
						}
					}
				}
			}
		}
		List<NewGkSectionBegin> beginList = newGkSectionBeginService.findListBy("sectionId", sectionId);
		if(CollectionUtils.isNotEmpty(beginList)) {
			for(NewGkSectionBegin b:beginList) {
				if(sub3Map.containsKey(b.getSubjectIds())) {
					b.setArrangeNum(sub3Map.get(b.getSubjectIds()));
				}else if(sub2Map.containsKey(b.getSubjectIds())) {
					b.setArrangeNum(sub2Map.get(b.getSubjectIds()));
				}else {
					b.setArrangeNum(0);
				}
			}
			newGkSectionBeginService.saveAll(beginList.toArray(new NewGkSectionBegin[] {}));
		}
	}

	@Override
	public void updateSection(String[] delResultId, List<NewGkSectionBegin> beginList, List<NewGkSectionEnd> endList,
			List<NewGkSectionResult> resultList) {
		if(delResultId!=null && delResultId.length>0) {
			newGkSectionResultService.deleteById(delResultId);
		}
		if(CollectionUtils.isNotEmpty(beginList)) {
			newGkSectionBeginService.saveAll(beginList.toArray(new NewGkSectionBegin[] {}));
		}
		if(CollectionUtils.isNotEmpty(endList)) {
			newGkSectionEndService.saveAll(endList.toArray(new NewGkSectionEnd[] {}));
		}
		if(CollectionUtils.isNotEmpty(resultList)) {
			newGkSectionResultService.saveAll(resultList.toArray(new NewGkSectionResult[] {}));
		}
		
	}

	

}
