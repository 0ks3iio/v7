package net.zdsoft.bigdata.extend.data.biz;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Maps;

import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.TagRuleSymbol;
import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.framework.entity.Json;

public class BgCalStorage {
	
	
	public static Map<String,Boolean> statusMap = Maps.newConcurrentMap();
	public  AtomicLong count = new AtomicLong(0);
	public static final int THREAD_MUNBER = 30;
	public  Map<String,MetadataTableColumn> metaColMap;
	public  Map<String,UserTag> tagMap;
	public  Map<String,Metadata> metaMap;
	public  Map<String, List<TagRuleRelation>> ruleRelationMap;
	public  Map<String, TagRuleSymbol> tagSymbolMap;
	private  BlockingQueue<Json> queues = new LinkedBlockingQueue<>(2000);
	private  ExecutorService pool = Executors.newFixedThreadPool(20);

	/**
	 * 生产
	 * 
	 * @param p
	 * @throws InterruptedException
	 */
	public void push(Json json) throws InterruptedException {
		queues.put(json);
	}

	/**
	 * 消费
	 * 1.分钟拿不到则返回null
	 * @throws InterruptedException
	 */
	public  Json poll() {
		return queues.poll();
	}
	
	public  Json pollTimeOut() throws InterruptedException{
		return queues.poll(30,TimeUnit.SECONDS);
	}
	
	public void submit(Runnable task) {
		pool.submit(task);
	}

	public  boolean chekMap() {
		if(metaColMap==null||metaColMap.size()==0){
			return false;
		}
		if(tagMap==null||tagMap.size()==0){
			return false;
		}
		if(metaMap==null||metaMap.size()==0){
			return false;
		}
		if(ruleRelationMap==null||ruleRelationMap.size()==0){
			return false;
		}
		if(tagSymbolMap==null||tagSymbolMap.size()==0){
			return false;
		}
		return true;
	}
	
	public  void clearMap() {
		if(metaColMap!=null){
			metaColMap.clear();
		}
		if(tagMap!=null){
			tagMap.clear();
		}
		if(metaMap!=null){
			metaMap.clear();
		}
		if(ruleRelationMap!=null){
			ruleRelationMap.clear();
		}
		if(tagSymbolMap!=null){
			tagSymbolMap.clear();
		}
	}
}
