package net.zdsoft.eclasscard.data.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dao.EccPhotoAlbumDao;
import net.zdsoft.eclasscard.data.dto.FileDto;
import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.eclasscard.data.entity.EccPhotoAlbum;
import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.service.EccPhotoAlbumService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.eclasscard.data.utils.PushPotoUtils;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
@Service("eccPhotoAlbumService")
public class EccPhotoAlbumServiceImpl extends BaseServiceImpl<EccPhotoAlbum, String> implements
		EccPhotoAlbumService {

	@Autowired
	private EccPhotoAlbumDao eccPhotoAlbumDao;
	@Autowired
	private EccInfoService eccInfoService;
	@Override
	protected BaseJpaRepositoryDao<EccPhotoAlbum, String> getJpaDao() {
		return eccPhotoAlbumDao;
	}

	@Override
	protected Class<EccPhotoAlbum> getEntityClass() {
		return EccPhotoAlbum.class;
	}

	@Override
	public List<EccPhotoAlbum> findByShowEccInfo(String eccInfoId) {
		List<EccPhotoAlbum> albums =  eccPhotoAlbumDao.findByShowEccInfo(eccInfoId);
		for(EccPhotoAlbum album:albums){
			album.setPictrueName(EccUtils.getFileNameNoExt(album.getPictrueName()));
		}
		return albums;
	}

	@Override
	public List<EccPhotoAlbum> findListByObjectIdPage(final String objectId,Pagination page) {
		Specification<EccPhotoAlbum> specification = new Specification<EccPhotoAlbum>() {

            @Override
            public Predicate toPredicate(Root<EccPhotoAlbum> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("objectId").as(String.class), objectId));
                List<Order> orderList = new ArrayList<Order>();
                Calendar calendar = Calendar.getInstance();
                calendar.set(2018, 1, 1);//创建时间为空的排在后面
                Expression<Object> queryCase = cb.selectCase().when(cb.isNull(root.get("createTime")), calendar.getTime()).otherwise(root.get("createTime"));
                orderList.add(cb.desc(queryCase.as(Date.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EccPhotoAlbum> findAll = eccPhotoAlbumDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        }
        else {
        	return eccPhotoAlbumDao.findAll(specification);
        }
	}
	
	@Override
	public Set<String> findPictrueDirpathMore(final String[] pictrueDirpaths) {
		Set<String> pictrueDirpathMores = Sets.newHashSet();
		if(pictrueDirpaths==null||pictrueDirpaths.length<=0){
			return pictrueDirpathMores;
		}
		return eccPhotoAlbumDao.findPictrueDirpathMore(pictrueDirpaths);
	}

	@Override
	public void deleteByObjId(String id) {
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		List<EccPhotoAlbum> albums =  eccPhotoAlbumDao.findByShowEccInfo(id);
		if(albums.size()>0){
			deleteAll(albums.toArray(new EccPhotoAlbum[albums.size()]));
		}
		for(EccPhotoAlbum album:albums){
			if(StringUtils.isNotBlank(album.getPictrueDirpath())){
				File file = new File(fileSystemPath+ File.separator+album.getPictrueDirpath());
				file.delete();
			}
		}
	}


	@Override
	public List<EccPhotoAlbum> findByObjectIdIn(String[] array) {
		return eccPhotoAlbumDao.findByObjectIdIn(array);
	}

@Override
	public List<EccPhotoAlbum> findByObjIdsTopOne(String[] objectIds) {
		List<EccPhotoAlbum> topAlbums = Lists.newArrayList();
		Set<String> topAlbumObjIds = Sets.newHashSet();
		List<EccPhotoAlbum> albums = eccPhotoAlbumDao.findByObjIds(objectIds);
		for(EccPhotoAlbum album:albums){
			if(topAlbumObjIds.contains(album.getObjectId())){
				continue;
			}
			topAlbumObjIds.add(album.getObjectId());
			topAlbums.add(album);
		}
		return topAlbums;
	}

	@Override
	public void saveToMore(String unitId,String sendType, String[] eccInfoIds,List<FileDto> fileDtos) {
		if(eccInfoIds==null||eccInfoIds.length==0&&StringUtils.isNotBlank(sendType)){
			List<EccInfo> eccInfos = Lists.newArrayList();
			if("1".equals(sendType)){
				eccInfos = eccInfoService.findListByUnitAndType(unitId,new String[]{EccConstants.ECC_MCODE_BPYT_1,EccConstants.ECC_MCODE_BPYT_2,
						EccConstants.ECC_MCODE_BPYT_3,EccConstants.ECC_MCODE_BPYT_4,EccConstants.ECC_MCODE_BPYT_5,EccConstants.ECC_MCODE_BPYT_6});
			}else if("2".equals(sendType)){
				eccInfos = eccInfoService.findListByUnitAndType(unitId,new String[]{EccConstants.ECC_MCODE_BPYT_1});
			}else if("3".equals(sendType)){
				eccInfos = eccInfoService.findListByUnitAndType(unitId,new String[]{EccConstants.ECC_MCODE_BPYT_2});
			}else if("4".equals(sendType)){
				eccInfos = eccInfoService.findListByUnitAndType(unitId,new String[]{EccConstants.ECC_MCODE_BPYT_3});
			}else if("5".equals(sendType)){
				eccInfos = eccInfoService.findListByUnitAndType(unitId,new String[]{EccConstants.ECC_MCODE_BPYT_4,EccConstants.ECC_MCODE_BPYT_5});
			}else if("6".equals(sendType)){
				eccInfos = eccInfoService.findListByUnitAndType(unitId,new String[]{EccConstants.ECC_MCODE_BPYT_6});
			}
			Set<String> infoIds = EntityUtils.getSet(eccInfos, "id");
			eccInfoIds = infoIds.toArray(new String[infoIds.size()]);
		}
		List<EccPhotoAlbum> albums = Lists.newArrayList();
		for(String eccInfoId:eccInfoIds){
			for(FileDto fileDto:fileDtos){
				EccPhotoAlbum album = new EccPhotoAlbum();
				album.setId(UuidUtils.generateUuid());
				album.setCreateTime(new Date());
				album.setObjectId(eccInfoId);
				album.setPictrueDirpath(fileDto.getFilePath());
				album.setPictrueName(fileDto.getFileName());
				albums.add(album);
			}
		}
		if(!albums.isEmpty()){
			saveAll(albums.toArray(new EccPhotoAlbum[albums.size()]));
			pushPhoto(eccInfoIds, unitId);
		}
	}
	
	private void pushPhoto(String[] infoIds,String unitId){
		Set<String> cardNumbers = Sets.newHashSet();
		for(String cardId:infoIds){
			cardNumbers.add(cardId);
		}
		if(CollectionUtils.isNotEmpty(cardNumbers)){
			PushPotoUtils.addPushCards(cardNumbers, unitId);
		}
	}

}
