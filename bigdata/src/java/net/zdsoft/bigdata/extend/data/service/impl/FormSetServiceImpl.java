package net.zdsoft.bigdata.extend.data.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.FormSetDao;
import net.zdsoft.bigdata.extend.data.entity.FormSet;
import net.zdsoft.bigdata.extend.data.service.FormSetService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("formSetService")
public class FormSetServiceImpl extends BaseServiceImpl<FormSet, String> implements
		FormSetService {
	@Autowired
	private FormSetDao formSetDao;
	@Autowired
	private MetadataService metadataService;
	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<FormSet, String> getJpaDao() {
		return formSetDao;
	}

	@Override
	protected Class<FormSet> getEntityClass() {
		return FormSet.class;
	}

	@Override
	public List<FormSet> findListByOrder(String formName,Pagination page) {
		List<FormSet> forms = Lists.newArrayList();
		Specification<FormSet> specification = new Specification<FormSet>() {
            @Override
            public Predicate toPredicate(Root<FormSet> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<>();
                if(StringUtils.isNotBlank(formName)){
                	ps.add(cb.like(root.get("name").as(String.class), "%" +formName+ "%"));
                }
                List<Order> orderList = new ArrayList<>();
                orderList.add(cb.asc(root.get("orderId").as(Integer.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
		};
	    if (page != null) {
	    	Pageable pageable = Pagination.toPageable(page);
	    	Page<FormSet> findAll = formSetDao.findAll(specification, pageable);
	    	page.setMaxRowCount((int) findAll.getTotalElements());
	    	forms =  findAll.getContent();
	    }
	    else {
	    	forms = formSetDao.findAll(specification);
	    }
		if(CollectionUtils.isNotEmpty(forms))fillMetaName(forms);
		return forms;
	}

	private void fillMetaName(List<FormSet> forms) {
		List<Metadata> metas = metadataService.findPropertyMetadata();
		Map<String,String> metaNameMap = EntityUtils.getMap(metas, Metadata::getId, Metadata::getName);
		forms.forEach(f ->{if(metaNameMap.containsKey(f.getMdId()))f.setMetaName(metaNameMap.get(f.getMdId()));});
	}

	@Override
	public void saveOrUpdate(FormSet form,String content) {
    	if(StringUtils.isNotEmpty(form.getId())){
    		FormSet oldForm = findOne(form.getId());
    		form.setCreationTime(oldForm.getCreationTime());
    	}else{
    		form.setId(UuidUtils.generateUuid());
    		form.setCreationTime(new Date());
    	}
    	form.setModifyTime(new Date());
        save(form);
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        File targetDirectory = new File(filePath+FormSet.HTML_PATH);
        if (!targetDirectory.exists()) {
        	targetDirectory.mkdirs();
        }
        String htmlPath = filePath+form.getHtmlPath();
        FileUtils.writeString(htmlPath, content, false);
	}

	@Override
	public String findHtmlByMdId(String mdId) {
		FormSet form = findByMdId(mdId);
		if(form==null){
			return "";
		}
		String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
		String htmlPath = filePath+form.getHtmlPath();
		String htmlStr = FileUtils.readString(htmlPath);
		if(StringUtils.isBlank(htmlStr)){
			htmlStr = "";
		}
		return htmlStr;
	}

	@Override
	public FormSet findByMdId(String mdId) {
		return formSetDao.findByMdId(mdId);
	}


}
