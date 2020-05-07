package net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.solve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.ChooseStudent;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.GroupResult;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.RequiredParm;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.ThreeGroup;
import net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto.TwoGroup;
import net.zdsoft.newgkelective.data.optaplanner.util.CalculateSections;

import org.apache.commons.collections.CollectionUtils;

public class SolveTwoSubject {
	
	RequiredParm requiredParm;
//	int maxNum;
//	int minNum;//暂时先当做浮动数量
	
	/**
	 * 1：获得文理分开，行政班数量配置情况
	 * @param bestNum 班级最佳学生人数
	 * @param xzbNum 总共行政班数量
	 * @param groupList
	 */
	private void findClassNumForGroup(int bestNum,int xzbNum,List<ThreeGroup> groupList){
		Map<ThreeGroup,Integer> bestClassNumMap=new HashMap<ThreeGroup,Integer>();
		Map<ThreeGroup,Integer> stuNumMap=new HashMap<ThreeGroup,Integer>();//如果按照商来考虑
		Map<ThreeGroup,Integer> needNumMap=new HashMap<ThreeGroup,Integer>();
		int allClassNum=0;
		int allNeedClassNum=0;
		for (ThreeGroup threeGroup:groupList) {
			int needClassNum=0;
			
			TwoGroup ownTwoGroup = threeGroup.getTwoGroup();
			int allStuNum=0;
			if(ownTwoGroup==null || ownTwoGroup.getStudentNum()==0){
				
			}else{
				allStuNum=ownTwoGroup.getStudentNum();
			}
			List<TwoGroup> twoGroupList = threeGroup.getTwoGroupList();
			for(TwoGroup two:twoGroupList){
				if(CollectionUtils.isEmpty(two.getStudentList())){
					continue;
				}
				needClassNum=needClassNum+1;
				allStuNum=allStuNum+two.getStudentNum();
			}
			if(allStuNum==0){
				//没有需要安排的学生
				continue;
			}
			int cc = allStuNum/bestNum;//商
			if(cc<needClassNum){
				cc=needClassNum;
			}
			bestClassNumMap.put(threeGroup, cc);
			stuNumMap.put(threeGroup, allStuNum);
			allNeedClassNum=allNeedClassNum+needClassNum;
			allClassNum=allClassNum+cc;
			needNumMap.put(threeGroup, needClassNum);
		}
		if(allNeedClassNum>xzbNum){
			//至少班级数量一般为6大于xzbNum
			//不能再降
			for (ThreeGroup threeGroup:groupList) {
				threeGroup.setAllStuNum(stuNumMap.get(threeGroup));
				if(needNumMap.containsKey(threeGroup)){
					threeGroup.setAllArrangXzbNum(needNumMap.get(threeGroup));
				}
				
			}
			return;
		}
		while(true){
			
			if(allClassNum>xzbNum){
				//需要减少班级数量
				//找到最合适的ThreeGroup  -1
				ThreeGroup best=findBestThreeGroup(stuNumMap,bestClassNumMap,-1,bestNum,needNumMap);
				if(best==null){
					break;
				}
				int classNum=bestClassNumMap.get(best);
				if(classNum<=1){
					break;
				}
				classNum=classNum-1;
				bestClassNumMap.put(best, classNum);
				allClassNum=allClassNum-1;
			}else if(allClassNum<xzbNum){
				//需要增加班级数量
				//找到最合适的ThreeGroup +1
				ThreeGroup best= findBestThreeGroup(stuNumMap,bestClassNumMap,1,bestNum,needNumMap);
				if(best==null){
					break;
				}
				int classNum=bestClassNumMap.get(best);
				classNum=classNum+1;
				bestClassNumMap.put(best, classNum);
				allClassNum=allClassNum+1;
			}else{
				break;
			}
		}
		for (ThreeGroup threeGroup:groupList) {
			threeGroup.setAllStuNum(stuNumMap.get(threeGroup));
			if(bestClassNumMap.containsKey(threeGroup)){
				threeGroup.setAllArrangXzbNum(bestClassNumMap.get(threeGroup));
			}
			
		}
	}
	
	/**
	 * 保证行政班数量一定，分配个组合的班级数量，移动判断  主要用于findClassNumForGroup
	 * @param stuNumMap
	 * @param bestClassNumMap
	 * @param avgNumMap
	 * @param i >0需要增加班级  <0 需要减少班级
	 * @param bestClassstuNum
	 * @param needNumMap 至少班级数量下降
	 * @return
	 */
	private ThreeGroup findBestThreeGroup(Map<ThreeGroup, Integer> stuNumMap,
			Map<ThreeGroup, Integer> bestClassNumMap, int i,int bestClassstuNum,Map<ThreeGroup, Integer> needNumMap) {
		ThreeGroup returnGroup=null;
		int cc=0;
		if(i<0){
			//找到最合适减少班级数量的ThreeGroup
			//一般情况 应该移动一个 所以暂时直接找 如果减少一个班级后 导致平均数偏离bestClassstuNum增加最少的  
			for(Entry<ThreeGroup, Integer> item:stuNumMap.entrySet()){
				ThreeGroup key = item.getKey();
				
				int classNum=bestClassNumMap.get(key);
				if(needNumMap.get(key)>=classNum){
					continue;
				}
				int allNum=item.getValue();
				
				if(classNum==1){
					continue;
				}
				int c1=allNum/(classNum-1)+(allNum%(classNum-1)==0?0:1);
				int cc1 = c1-bestClassstuNum;
				if(cc1<0){
					cc1=0-cc1;
				}
				if(returnGroup==null){
					returnGroup=key;
					cc=cc1;
				}else{
					if(cc1<cc){
						returnGroup=key;
						cc=cc1;
					}
				}
			}
			
		}else{
			//找到最合适增加班级数量的ThreeGroup
			//一般情况 应该移动一个 如果增加一个班级后 导致平均数偏离bestClassstuNum减少最少的
			for(Entry<ThreeGroup, Integer> item:stuNumMap.entrySet()){
				ThreeGroup key = item.getKey();
				int allNum=item.getValue();
				int classNum=bestClassNumMap.get(key);
				int c1=allNum/(classNum+1)+(allNum%(classNum+1)==0?0:1);
				int cc1 = bestClassstuNum-c1;
				if(cc1<0){
					cc1=0-cc1;
				}
				if(returnGroup==null){
					returnGroup=key;
					cc=cc1;
				}else{
					if(cc1<cc){
						returnGroup=key;
						cc=cc1;
					}
				}
			}
			
		}
		return returnGroup;
	}
	
	/**
	 * 保证行政班数量一定，分配个组合的班级数量，移动判断  主要用于makeResultAllTwoGroup
	 * @param twoGroupList
	 * @param i  >0需要增加班级  <0 需要减少班级
	 * @param avgClassStuNum
	 * @return
	 */
	private TwoGroup findBestTwoGroup(List<TwoGroup> twoGroupList, int i,
			int avgClassStuNum) {
		TwoGroup returnTwoGroup=null;
		int cc=0;
		if(i<0){
			//找到最合适减少班级数量的ThreeGroup
			//一般情况 应该移动一个 所以暂时直接找 如果减少一个班级后 导致平均数偏离bestClassstuNum增加最少的  
			for(TwoGroup item:twoGroupList){
				if(CollectionUtils.isEmpty(item.getStudentList())){
					continue;
				}
				int allNum=item.getStudentList().size();
				int classNum=item.getNeedClassNum();
				if(classNum==1){
					//不能减少
					continue;
				}
				int c1=allNum/(classNum-1)+(allNum%(classNum-1)==0?0:1);
				int cc1 = c1-avgClassStuNum;
				if(cc1<0){
					cc1=0-cc1;
				}
				if(returnTwoGroup==null){
					returnTwoGroup=item;
					cc=cc1;
				}else{
					if(cc1<cc){
						returnTwoGroup=item;
						cc=cc1;
					}
				}
			}
			
		}else{
			//找到最合适增加班级数量的ThreeGroup
			//一般情况 应该移动一个 如果增加一个班级后 导致平均数偏离bestClassstuNum减少最少的
			for(TwoGroup item:twoGroupList){
				if(CollectionUtils.isEmpty(item.getStudentList())){
					continue;
				}
				int allNum=item.getStudentList().size();
				int classNum=item.getNeedClassNum();
				int c1=allNum/(classNum+1)+(allNum%(classNum+1)==0?0:1);
				int cc1 = c1-avgClassStuNum;
				if(cc1<0){
					cc1=0-cc1;
				}
				if(returnTwoGroup==null){
					returnTwoGroup=item;
					cc=cc1;
				}else{
					if(cc1<cc){
						returnTwoGroup=item;
						cc=cc1;
					}
				}
			}
			
		}
		return returnTwoGroup;
	}
	
	private List<GroupResult> makeResultAllTwoGroup(
			List<TwoGroup> twoGroupList, int bestClassNum, int avgClassStuNum) {
		//bestClassNum 分配
		int needXzbNum=0;
		List<GroupResult> returnResult=new ArrayList<GroupResult>();
		for(TwoGroup gg:twoGroupList){
			List<ChooseStudent> stulist = gg.getStudentList();
			if(CollectionUtils.isEmpty(stulist)){
				continue;
			}
			List<Integer> allArrlist = CalculateSections.calculateSectioning2(stulist.size(), avgClassStuNum, 0);
			gg.setNeedClassNum(allArrlist.size());
//			gg.setAvgMax(stulist.size()/allArrlist.size()+(stulist.size()%allArrlist.size()==0?0:1));
			needXzbNum=needXzbNum+allArrlist.size();
		}
		while(true){
			if(bestClassNum<needXzbNum){
				//需要减少班级数量
				//找到最合适的TwoGroup  -1
				
				TwoGroup best=findBestTwoGroup(twoGroupList,-1,avgClassStuNum);
				if(best==null){
					break;
				}
				int classNum=best.getNeedClassNum();
				if(classNum<=1){
					//不能移动
					break;
				}
				classNum=classNum-1;
				best.setNeedClassNum(classNum);
				needXzbNum=needXzbNum-1;
				
			}else if(bestClassNum>needXzbNum){
				//需要增加班级数量
				//找到最合适的TwoGroup +1
				TwoGroup best=findBestTwoGroup(twoGroupList,1,avgClassStuNum);
				if(best==null){
					break;
				}
				int classNum=best.getNeedClassNum();
				if(classNum<=1){
					//不能移动
					break;
				}
				classNum=classNum-1;
				best.setNeedClassNum(classNum);
				needXzbNum=needXzbNum-1;
			}else{
				break;
			}
	
		}
		for(TwoGroup gg:twoGroupList){
			List<ChooseStudent> stulist = gg.getStudentList();
			int classNum = gg.getNeedClassNum();
			if(CollectionUtils.isEmpty(stulist) || classNum==0){
				continue;
			}
			//stulist 平均分配classNum 
			List<GroupResult> list = makeSingleResult(gg,gg.getSubjectIds(), stulist, classNum, NewGkElectiveConstant.SUBJTCT_TYPE_2);
			returnResult.addAll(list);
		}
		
		return returnResult;
	}
	
	
	
	
	
	
	public List<GroupResult> solve(List<ThreeGroup> groupList,RequiredParm requiredParm) throws Exception{
		List<GroupResult> result=new ArrayList<GroupResult>();
		this.requiredParm=requiredParm;
//		this.maxNum=requiredParm.getMinReguired();
//		this.minNum=requiredParm.getCanAddReguired();
		int singleClassNum = requiredParm.getSingleNum();
		int bestNum=requiredParm.getBestClassStuNum();//平均数
		int xzbNum=requiredParm.getXzbNum();//班级数量
		
		findClassNumForGroup(bestNum, xzbNum,groupList);

		for (ThreeGroup threeGroup:groupList) {
			int bestClassNum = threeGroup.getAllArrangXzbNum();//最佳班级数量
			//总人数
			int stuNum=threeGroup.getAllStuNum();
			if(bestClassNum==0 || stuNum==0){
				break;
			}
			//根据行政班数量配置获得班级数量，取得需要的最佳班级人数
			int avgClassStuNum=stuNum/bestClassNum+(stuNum%bestClassNum==0?0:1);//每班最佳班级人数
			
			
			
			TwoGroup ownTwoGroup = threeGroup.getTwoGroup();
			List<TwoGroup> twoGroupList = threeGroup.getTwoGroupList();
			if(ownTwoGroup==null || ownTwoGroup.getStudentNum()==0){
				//极端情况1 3科组合的学生数量为0  
				//无法移动学生数据，只能根据各组合人数自成班级
				/**
				 * 不能超过 bestClassNum
				 */
				if(CollectionUtils.isEmpty(threeGroup.getTwoGroupList())){
					continue;
				}
				List<GroupResult> rr=makeResultAllTwoGroup(twoGroupList,bestClassNum,avgClassStuNum);
				if(CollectionUtils.isNotEmpty(rr)){
					result.addAll(rr);
				}
//				for(TwoGroup two:twoGroupList){
//					if(CollectionUtils.isEmpty(two.getStudentList())){
//						continue;
//					}
//					
//					List<GroupResult> rr=makeResult(two.getSubjectIds(),two.getStudentList(),NewGkElectiveConstant.SUBJTCT_TYPE_2);
//					result.addAll(rr);
//				}
				continue;
			}
			
			
			int allStuNum=ownTwoGroup.getStudentNum();
			if(CollectionUtils.isEmpty(threeGroup.getTwoGroupList())){
				//极端情况2 某3科组合的两两组合中不存在
				//只要安排
				/***
				 * 不能超过 bestClassNum
				 */
				List<GroupResult> rr=makeSingleResult(null,ownTwoGroup.getSubjectIds(), ownTwoGroup.getStudentList(), bestClassNum, NewGkElectiveConstant.SUBJTCT_TYPE_3);
//				List<GroupResult> rr=makeResultAllThreeGroup(ownTwoGroup,bestClassNum);
//				List<GroupResult> rr=makeResult(ownTwoGroup.getSubjectIds(),ownTwoGroup.getStudentList(),NewGkElectiveConstant.SUBJTCT_TYPE_3);
				if(CollectionUtils.isNotEmpty(rr)){
					result.addAll(rr);
				}
				continue;
			}
			
			//最少班级数 --也就是至少两两组合每个组合一个班级
			int minClassNum=0;
			int twoNeedClassNum=0;//两两组合需要的班级数量
			for(TwoGroup two:twoGroupList){
				if(CollectionUtils.isEmpty(two.getStudentList())){
					continue;
				}
				minClassNum=minClassNum+1;//排除学生不存在的组合
				//calculateSectioning2 返回的结果按从大到小排
				List<Integer> arrlist = CalculateSections.calculateSectioning2(two.getStudentNum(), avgClassStuNum, 0);
				two.setNeedClassNum(arrlist.size());
//				two.setAvgMax(arrlist.get(0));
				twoNeedClassNum=twoNeedClassNum+two.getNeedClassNum();
				allStuNum=allStuNum+two.getStudentNum();
			}
			//最佳班级数allStuNum
//			List<Integer> allArrlist = CalculateSections.calculateSectioning2(allStuNum, maxNum, minNum);
//			int bestClassNum=allArrlist.size();//最佳班级数量
			if(bestClassNum>twoNeedClassNum){
				//说明可以完全满足allArrlist的数量去分配
				List<GroupResult> rr=makeAllResult(bestClassNum,avgClassStuNum, twoGroupList, ownTwoGroup);
				result.addAll(rr);
			}else if(bestClassNum==twoNeedClassNum){
				//极端情况3 (1)完全不够 在每个组合开设一个班级后 3科组合只能去平均分配，保证每个班级人数尽量平均 bestClassNum<minClassNum
				//(2)一样的处理方式不存在3科组合班级 最后一种
				//1:bestClassNum=twoNeedClassNum 
				List<GroupResult> rr = makeOnlyTwoAllResult(twoGroupList, ownTwoGroup);
				result.addAll(rr);
				
			}else{
//				2:bestClassNum<twoNeedClassNum
//				List<GroupResult> rr = makeOnlyTwoAllResult(twoGroupList, ownTwoGroup);
//				result.addAll(rr);
				//先twoNeedClassNum降到bestClassNum
				while(true){
					if(bestClassNum==twoNeedClassNum){
						break;
					}
					TwoGroup best = findBestTwoGroup(twoGroupList, -1, avgClassStuNum);
					if(best==null){
						break;
					}
					if(best.getNeedClassNum()<=1){
						break;
					}
					best.setNeedClassNum(best.getNeedClassNum()-1);
					twoNeedClassNum--;
				}
				List<GroupResult> rr = makeOnlyTwoAllResult(twoGroupList, ownTwoGroup);
				result.addAll(rr);
			}
			
		}
		int threeClassNum=0;
		for(GroupResult rr:result){
			if(NewGkElectiveConstant.SUBJTCT_TYPE_3.equals(rr.getSubjectType())){
				threeClassNum++;
			}
		}
		
		Map<String, Set<ChooseStudent>> singleOther = requiredParm.getOtherChooseStudentMap();
		//singleClassNum=requiredParm.getSingleNum()-3科组合班级
		singleClassNum=singleClassNum-threeClassNum;
		List<GroupResult> singleList = makeSingleClass(result,singleOther,singleClassNum);
		if(CollectionUtils.isNotEmpty(singleList)){
			result.addAll(singleList);
		}
		return result;
	}
	
	
	
	

	/**
	 * 剩余X组成班级数量
	 * @param result
	 * @param singleOther
	 * @param singleClassNum
	 * @return
	 * @throws Exception 
	 */
	private List<GroupResult> makeSingleClass(List<GroupResult> result,Map<String, Set<ChooseStudent>> singleOther,int singleClassNum) throws Exception{
		List<GroupResult> singleResult=new ArrayList<GroupResult>();
		List<TwoGroup> oneSubjectList=new ArrayList<TwoGroup>();//subjectIds 只含单科
		List<TwoGroup> maxSubjectList=new ArrayList<TwoGroup>();//需要开设班级数量大于1，用于后面可以调整班级开设数量
		Map<String,TwoGroup> oneSubjectMap=new HashMap<String,TwoGroup>();//key:subjectId
		
		//遗留单科
		for(GroupResult rr:result){
			if(NewGkElectiveConstant.SUBJTCT_TYPE_2.equals(rr.getSubjectType())){
				Set<String> tarSubjectIds = rr.getSubjectIds();
				for(ChooseStudent stu:rr.getStuIdList()){
					Set<String> cc = stu.getChooseSubjectIds();
					cc.removeAll(tarSubjectIds);
					for(String c:cc){
						TwoGroup oneSubject = oneSubjectMap.get(c);
						if(oneSubject==null){
							oneSubject=new TwoGroup();
							oneSubject.setSubjectIds(new HashSet<String>());
							oneSubject.getSubjectIds().add(c);
							oneSubject.setStudentList(new ArrayList<ChooseStudent>());
							oneSubject.setStudentNum(0);
							oneSubjectList.add(oneSubject);
							oneSubjectMap.put(c, oneSubject);
						}
						oneSubject.getStudentList().add(stu);
						oneSubject.setStudentNum(oneSubject.getStudentNum()+1);
					}
				}
			}
		}
		if(singleOther!=null && singleOther.size()>0){
			for(Entry<String, Set<ChooseStudent>> single:singleOther.entrySet()){
				String c = single.getKey();
				TwoGroup oneSubject = oneSubjectMap.get(c);
				if(oneSubject==null){
					oneSubject=new TwoGroup();
					oneSubject.setSubjectIds(new HashSet<String>());
					oneSubject.getSubjectIds().add(c);
					oneSubject.setStudentList(new ArrayList<ChooseStudent>());
					oneSubject.setStudentNum(0);
					oneSubjectList.add(oneSubject);
					oneSubjectMap.put(c, oneSubject);
				}
				oneSubject.getStudentList().addAll(single.getValue());
				oneSubject.setStudentNum(oneSubject.getStudentNum()+single.getValue().size());
			}
			
		}
		//剩余总人数
		int allXnum=0;
		for(TwoGroup item: oneSubjectList){
			allXnum=allXnum+item.getStudentNum();
		}
		if(allXnum==0){
			//没有单科
			return new ArrayList<GroupResult>();
		}
		if(singleClassNum==0){
			//说明没有多余的班级数量 
			System.out.println("没有多余的班级数量");
			throw new Exception("没有多余的班级数量");
		}
		//班级平均数
		int bestStuNum = allXnum/singleClassNum+(allXnum%singleClassNum==0?0:1);
		//单科班级数量尽量<=行政班数量


		int allNum=0;
		for(TwoGroup item: oneSubjectList){
			int size1=item.getStudentNum();
			List<Integer> allArrlist = CalculateSections.calculateSectioning2(size1, bestStuNum, 0);
			int num=allArrlist.size();
			item.setNeedClassNum(num);
			allNum=allNum+num;
			int shang = size1/num;
			int yushu=size1%num;
			if(yushu>0){
				shang=shang+1;
			}
//			item.setAvgMax(shang);
			if(item.getNeedClassNum()>1){
				maxSubjectList.add(item);
			}
		}
		
		if(allNum<=singleClassNum){
			//行政班数量刚好--暂时先不考虑要完全刚好够singleClassNum 一般不会少
			
		}else{
			//超出行政班数
			//减少一个班级数量
			int left=allNum-singleClassNum;
			if(CollectionUtils.isNotEmpty(maxSubjectList)){
				while(true){
					if(left<=0){
						break;
					}
					if(CollectionUtils.isEmpty(maxSubjectList)){
						break;
					}
//					TwoGroup choose=findBest(maxSubjectList);
					TwoGroup choose=findBestTwoGroup(maxSubjectList, -1, bestStuNum);
					if(choose==null){
						break;
					}
					choose.setNeedClassNum(choose.getNeedClassNum()-1);
//					int size1=choose.getStudentNum();
					int num=choose.getNeedClassNum();
//					int shang = size1/num;
//					int yushu=size1%num;
//					if(yushu>0){
//						shang=shang+1;
//					}
//					choose.setAvgMax(shang);
					if(num<=1){
						maxSubjectList.remove(choose);//移除班级降为0
					}
					if(CollectionUtils.isEmpty(maxSubjectList)){
						//没有可以降的组合
						break;
					}
					left--;
				}
			}
			
		}
		//根据开设数量
		for(TwoGroup item: oneSubjectList){
			List<GroupResult> rr=makeSingleResult(null,item.getSubjectIds(),item.getStudentList(),item.getNeedClassNum(),null);
			singleResult.addAll(rr);
		}
		return singleResult;
	}
	
//	private TwoGroup findBest(List<TwoGroup> oneSubjectList) {
//		TwoGroup t=null;
//		int avg=0;
//		for(TwoGroup tt:oneSubjectList){
//			if(tt.getNeedClassNum()<=1){
//				continue;
//			}
//			//假设减去一个班级
//			int size1=tt.getStudentNum();
//			int num=tt.getNeedClassNum()-1;
//			int shang = size1/num;
//			int yushu=size1%num;
//			if(yushu>0){
//				shang=shang+1;
//			}
//			if(tt==null || shang<avg){
//				t=tt;
//				avg=shang;
//			}
//		}
//		return t;
//	}
	/**
	 * 将学生studentList平均分成needClassNum班
	 * @param twoGroup 2+x才传值
	 * @param subjectIds
	 * @param studentList
	 * @param needClassNum
	 * @param subjectType 为null 代表单科班
	 * @return
	 */
	private List<GroupResult> makeSingleResult(TwoGroup twoGroup,Set<String> subjectIds,
			List<ChooseStudent> studentList, int needClassNum,String subjectType) {
		List<GroupResult> reList=new ArrayList<GroupResult>();
		if(needClassNum==1){
			GroupResult groupResult = new GroupResult();
			if(twoGroup!=null){
				groupResult.setTwoGroup(twoGroup);
			}
			groupResult.setSubjectIds(subjectIds);
			groupResult.setStuIdList(studentList);
			groupResult.setSubjectType(subjectType);
			reList.add(groupResult);
		}else{
			int size1=studentList.size();
			int shang = size1/needClassNum;
			int yushu=size1%needClassNum;
			int max=0;
			for(int i=0;i<needClassNum;i++){
				GroupResult groupResult = new GroupResult();
				if(twoGroup!=null){
					groupResult.setTwoGroup(twoGroup);
				}
				groupResult.setSubjectIds(subjectIds);
				groupResult.setStuIdList(subList(studentList,max, max+shang));
				groupResult.setSubjectType(subjectType);
				max=max+shang;
				reList.add(groupResult);
			}
			if(yushu>0){
				List<ChooseStudent> left = subList(studentList,max, size1);
				for(int i=0;i<left.size();i++){
					reList.get(i).getStuIdList().add(left.get(i));
				}
			}
		}
		return reList;
	}

	private List<GroupResult> makeOnlyTwoAllResult(List<TwoGroup> twoGroupList,TwoGroup threeGroup){
		List<GroupResult> result=new ArrayList<GroupResult>();
		GroupResult groupResult;
		List<ChooseStudent> canMove=threeGroup.getStudentList();
		int moveLeft=canMove.size();
		//先考虑用掉小的班级数
		for(TwoGroup t:twoGroupList){
			if(t.getNeedClassNum()==0){
				continue;
			}
			List<ChooseStudent> stuList = t.getStudentList();
			int stuNum = t.getStudentNum();
			int needClassNum=t.getNeedClassNum();
			List<Integer> list1 = makeBestList(stuNum, needClassNum);
			//先不考虑添加人
			int max=0;
			for(int i:list1){
				groupResult=new GroupResult();
				groupResult.setTwoGroup(t);
				groupResult.setSubjectIds(t.getSubjectIds());
				groupResult.setStuIdList(subList(stuList,max, max+i));
				groupResult.setStuNum(i);
				groupResult.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_2);
				max=max+i;
				result.add(groupResult);
			}
		}
		int mm=0;
		while(moveLeft>0){
			//moveLeft分到
			GroupResult g = findMin(result,null);
			if(g==null){
				break;
			}
			//移动一个学生
			g.getStuIdList().addAll(subList(canMove,mm, mm+1));
			g.setStuNum(g.getStuNum()+1);
			mm=mm+1;
			moveLeft=moveLeft-1;
			if(moveLeft==0){
				break;
			}
		}
		return result;
	}
	
	
	private List<GroupResult> makeAllResult(int bestClassNum,int avgStuNum,List<TwoGroup> twoGroupList,TwoGroup threeGroup){
		List<GroupResult> result=new ArrayList<GroupResult>();
		

		GroupResult groupResult;
		List<ChooseStudent> canMove=threeGroup.getStudentList();
		int moveLeft=canMove.size();
		//剩余班级数中班级人数最少的班级在arrList最后一个值
		int minClassStuNum=avgStuNum-1;
		int maxClassStuNum=avgStuNum;
		//例子arrList：52,52,51 其中一个组合是104个学生 如果很不好的情况下变成51,52 那么就需要3个班 与现实2个班不符，如果多的都是这样情况，可能就没有办法达到目标
		//先考虑用掉小的班级数
		//一般是由于两两组合班级人数不够 从三科拿，所以三科剩余班级数量 应该班级学生人数在平均数值-1
		for(TwoGroup t:twoGroupList){
			if(t.getNeedClassNum()==0){
				continue;
			}
			List<ChooseStudent> stuList = t.getStudentList();
			int stuNum = t.getStudentNum();
			int needClassNum=t.getNeedClassNum();
			List<Integer> list1 = makeBestList(stuNum, needClassNum);
			//先不考虑添加人
			int max=0;
			for(int i:list1){
				groupResult=new GroupResult();
				groupResult.setTwoGroup(t);
				groupResult.setSubjectIds(t.getSubjectIds());
				groupResult.setStuIdList(subList(stuList,max, max+i));
				groupResult.setStuNum(i);
				groupResult.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_2);
				max=max+i;
				result.add(groupResult);
			}
		}
		List<GroupResult> threeResult=new ArrayList<GroupResult>();
		int leftClassNum=bestClassNum-result.size();
		int mm=0;//canMove剩余
		for(int i=0;i<leftClassNum;i++){
			groupResult=new GroupResult();
			groupResult.setSubjectIds(threeGroup.getSubjectIds());
			//当还有可以开设一个班 但是这边人数不够最小值
			if(mm+minClassStuNum>canMove.size()){
				groupResult.setStuIdList(subList(canMove,mm, canMove.size()));
				mm=canMove.size();
				groupResult.setStuNum(groupResult.getStuIdList().size());
			}else{
				groupResult.setStuIdList(subList(canMove,mm, mm+minClassStuNum));
				mm=mm+minClassStuNum;
				groupResult.setStuNum(minClassStuNum);
			}
			groupResult.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_3);
			moveLeft=moveLeft-minClassStuNum;
			threeResult.add(groupResult);
			result.add(groupResult);
		}
		//剩余
		List<ChooseStudent> canMove1 = subList(canMove,mm, canMove.size());
		moveLeft=canMove1.size();
		int m=0;//开始序号
		while(moveLeft>0){
			//moveLeft分到
			GroupResult g = findMin(result,maxClassStuNum);
			if(g==null){
				break;
			}
			//移动一个学生
			g.getStuIdList().addAll(subList(canMove1,m, m+1));
			g.setStuNum(g.getStuNum()+1);
			m=m+1;
			moveLeft=moveLeft-1;
			if(moveLeft==0){
				break;
			}
		}
		canMove1 = subList(canMove1,m, canMove1.size());
		moveLeft=canMove1.size();
		m=0;
		if(moveLeft>0){
			//只能放在3科组合
			while(moveLeft>0){
				//moveLeft分到
				GroupResult g = findMin(threeResult,null);
				if(g==null){
					break;
				}
				//移动一个学生
				g.getStuIdList().addAll(subList(canMove1,m, m+1));
				g.setStuNum(g.getStuNum()+1);
				m=m+1;
				moveLeft=moveLeft-1;
				if(moveLeft==0){
					break;
				}
			}
			
		}
//		if(CollectionUtils.isNotEmpty(result)){
//			result = reArrangeTwo(result);
//		}
		return result;
	}
	
	
	private List<GroupResult> reArrangeTwo(List<GroupResult> result) {
		List<GroupResult> threeResultList=new ArrayList<GroupResult>();
		Map<TwoGroup, List<GroupResult>> resultByTwo=new HashMap<TwoGroup, List<GroupResult>>();
		for(GroupResult g:result){
			if(g.getSubjectType().equals(NewGkElectiveConstant.SUBJTCT_TYPE_3)){
				threeResultList.add(g);
			}else{
				//2+x
				if(!resultByTwo.containsKey(g.getTwoGroup())){
					resultByTwo.put(g.getTwoGroup(),new ArrayList<GroupResult>());
				}
				resultByTwo.get(g.getTwoGroup()).add(g);
			}
		}
		
		
		if(resultByTwo.size()<=0){
			return result;
		}

		// 2+x重新考虑 以学生选课条件
		List<GroupResult> twoResultList = new ArrayList<GroupResult>();
		GroupResult groupResult ;
		for (Entry<TwoGroup, List<GroupResult>> itemGroup : resultByTwo
				.entrySet()) {
			TwoGroup key = itemGroup.getKey();
			Set<String> sameSubjectIds = key.getSubjectIds();
			List<GroupResult> ll = itemGroup.getValue();
			int allStuNum = ll.size();
			List<ChooseStudent> stuByTwoGroupList = new ArrayList<ChooseStudent>();
			for (GroupResult g : ll) {
				List<ChooseStudent> stu = g.getStuIdList();
				stuByTwoGroupList.addAll(stu);
			}
			int allstuSize = stuByTwoGroupList.size();
			int avgStuNumByOne = allstuSize / allStuNum;
			int yuShu = allstuSize % allStuNum;

			// 根据学生选课数据剩余x
			Map<String, List<ChooseStudent>> stuByX = new HashMap<String, List<ChooseStudent>>();
			// 不考虑数据错误 每个人只留下一个X
			for (ChooseStudent ss : stuByTwoGroupList) {
				for (String cc : ss.getChooseSubjectIds()) {
					if (!sameSubjectIds.contains(cc)) {
						if (!stuByX.containsKey(cc)) {
							stuByX.put(cc, new ArrayList<ChooseStudent>());
						}
						stuByX.get(cc).add(ss);
						break;
					}
				}
			}

			int three = 0;// 2+x中抽出3+0

			// 一般情况下 不会存在班级很多 暂时都以最小值班级数考虑
			Map<String, List<ChooseStudent>> leftstuByX = new HashMap<String, List<ChooseStudent>>();
			Map<String, List<GroupResult>> resultByX = new HashMap<String, List<GroupResult>>();
			for (Entry<String, List<ChooseStudent>> xx : stuByX.entrySet()) {
				List<ChooseStudent> stuByOneX = xx.getValue();
				String xSub = xx.getKey();
				int stuNum = stuByOneX.size();
				if (stuNum < avgStuNumByOne) {
					// 不能成3+0
					leftstuByX.put(xx.getKey(), new ArrayList<ChooseStudent>());
					leftstuByX.get(xx.getKey()).addAll(stuByOneX);
				} else {
					// 可以成3+0
					int classNum = stuNum / avgStuNumByOne;
					int maxIndex = 0;
					for (int i = 0; i < classNum; i++) {
						groupResult = new GroupResult();
						groupResult.setSubjectIds(new HashSet<String>());
						groupResult.getSubjectIds().addAll(sameSubjectIds);
						groupResult.getSubjectIds().add(xSub);
						groupResult.setStuIdList(subList(stuByOneX, maxIndex,
								maxIndex + avgStuNumByOne));
						groupResult.setStuNum(avgStuNumByOne);
						groupResult
								.setSubjectType(NewGkElectiveConstant.SUBJTCT_TYPE_3);
						maxIndex = maxIndex + avgStuNumByOne;
						if (!resultByX.containsKey(xSub)) {
							resultByX.put(xSub, new ArrayList<GroupResult>());
						}
						resultByX.get(xSub).add(groupResult);
						twoResultList.add(groupResult);
						three = three + 1;
					}
					if (maxIndex < stuByOneX.size()) {
						leftstuByX.put(xSub,
								subList(stuByOneX, maxIndex, stuByOneX.size()));
					}

				}

			}
			while (true) {
				if (yuShu == 0) {
					break;
				}
				boolean f = false;
				for (Entry<String, List<GroupResult>> kList : resultByX
						.entrySet()) {
					String kListKey = kList.getKey();
					List<GroupResult> keyListValue = kList.getValue();
					if (resultByX.containsKey(kListKey)) {
						List<ChooseStudent> leftStu = leftstuByX.get(kListKey);
						if (CollectionUtils.isNotEmpty(leftStu)) {
							// leftStu.size() yuShu keyListValue.size() 最小值
							int minMoveNum = leftStu.size();
							if (minMoveNum > yuShu) {
								minMoveNum = yuShu;
							}
							if (minMoveNum > keyListValue.size()) {
								minMoveNum = keyListValue.size();
							}
							if (minMoveNum == 0) {
								break;
							}
							f = true;
							for (int ii = 0; ii < minMoveNum; ii++) {
								keyListValue.get(ii).getStuIdList()
										.add(leftStu.get(ii));
								keyListValue.get(ii).setStuNum(
										keyListValue.get(ii).getStuNum() + 1);
								yuShu--;
							}
							leftstuByX
									.put(kListKey,
											subList(leftStu, minMoveNum,
													leftStu.size()));
							break;
						}
					}
				}
				if (!f) {
					break;
				}
			}
			// allStuNum>=three
			// 不会存在allStuNum<three
			List<ChooseStudent> leftStu = new ArrayList<ChooseStudent>();
			for (Entry<String, List<ChooseStudent>> dd : leftstuByX.entrySet()) {
				leftStu.addAll(dd.getValue());

			}
			if (allStuNum == three) {
				if (CollectionUtils.isNotEmpty(leftStu)) {
					int iii = 0;
					// leftstuByX要安排到不满的
					// 只能twoResultList 从3+0变为2+x
					for (GroupResult twoRr : twoResultList) {
						if (twoRr.getStuNum() == avgStuNumByOne) {
							twoRr.getStuIdList().add(leftStu.get(iii));
							iii++;
							twoRr.setStuNum(twoRr.getStuNum() + 1);
						}
					}
				}

			} else {
				// 直接剩余人数分班
				List<GroupResult> gglist = makeSingleResult(key,sameSubjectIds,
						leftStu, allStuNum - three,
						NewGkElectiveConstant.SUBJTCT_TYPE_2);
				if (CollectionUtils.isNotEmpty(gglist)) {
					twoResultList.addAll(gglist);
				}
			}

		}
		
		List<GroupResult> result2 = new ArrayList<GroupResult>();
		if(CollectionUtils.isNotEmpty(threeResultList)){
			result2.addAll(threeResultList);
		}
		if(CollectionUtils.isNotEmpty(twoResultList)){
			result2.addAll(twoResultList);
		}

		return result2;
	}
	

	/**
	 * 
	 * @param list
	 * @param startIndex
	 *            inclusive
	 * @param endIndex
	 *            exclusive
	 * @return
	 */
	private static <T> List<T> subList(List<T> list, int startIndex, int endIndex) {
		if (list == null) {
			throw new RuntimeException("list cannot be null!");
		}
		if (startIndex > list.size()) {
			throw new IndexOutOfBoundsException("start index out of size!");
		}
		if (endIndex > list.size()) {
			throw new IndexOutOfBoundsException("end index out of size!");
		}
		List<T> newList = new ArrayList<T>();
		for (int i = startIndex; i < endIndex; i++) {
			newList.add(list.get(i));
		}
		return newList;
	}
	
	private GroupResult findMin(List<GroupResult> result,Integer max){
		GroupResult g=null;
		for(GroupResult rr:result){
			if(max!=null){
				if(rr.getStuNum()>=max){
					continue;
				}
			}
			if(g==null){
				g=rr;
			}else{
				if(g.getStuNum()>rr.getStuNum()){
					g=rr;
				}
			}
		}
		return g;
	}
	//平均
	private List<Integer> makeBestList(int allNum,int classNum){
		List<Integer> rr=new ArrayList<Integer>();
		int a1 = allNum/classNum;
		for(int i=0;i<classNum;i++){
			rr.add(a1);
		}
		int remain = allNum%classNum;
		if (remain > 0) {
			 for (int i = 0; i < remain; i ++){
				 rr.set(i, rr.get(i) + 1); 
			 }
		}
		return rr;
	}
	
//	private List<GroupResult> makeResult(Set<String> subjectIds,List<ChooseStudent> studentIdList,String subjectType) {
//		int size=studentIdList.size();
//		List<Integer> arrlist = CalculateSections.calculateSectioning2(size, maxNum, minNum);
//		List<GroupResult> result=new ArrayList<GroupResult>();
//		GroupResult groupResult;
//		int max=0;
//		for(int ii:arrlist){
//			groupResult=new GroupResult();
//			groupResult.setSubjectIds(subjectIds);
//			groupResult.setStuIdList(subList(studentIdList,max, max+ii));
//			groupResult.setSubjectType(subjectType);
//			max=max+ii;
//			result.add(groupResult);
//		}
//		return result;
//	}
}
