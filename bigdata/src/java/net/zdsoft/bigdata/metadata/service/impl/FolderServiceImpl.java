package net.zdsoft.bigdata.metadata.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.dao.FolderDao;
import net.zdsoft.bigdata.metadata.entity.Folder;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.entity.FolderEx;
import net.zdsoft.bigdata.metadata.entity.FolderType;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.bigdata.metadata.service.FolderService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2019/1/7 14:16.
 */
@Service
public class FolderServiceImpl extends BaseServiceImpl<Folder, String> implements FolderService {


    @Resource
    private FolderDao folderDao;
    @Resource
    private FolderDetailService folderDetailService;

    @Override
    protected BaseJpaRepositoryDao<Folder, String> getJpaDao() {
        return folderDao;
    }

    @Override
    protected Class<Folder> getEntityClass() {
        return Folder.class;
    }

    @Override
    public Map<String, List<Folder>> findFolderMapByParentId(String parentId) {
        List<Folder> topFolders = folderDao.findAllByParentIdAndStatusOrderByOrderId(parentId,1);
        Map<String, List<Folder>> folderMap = Maps.newHashMap();
        topFolders.forEach(e-> folderMap.put(e.getId(), this.findListBy("parentId", e.getId())));
        return folderMap;
    }

    @Override
    public Map<String, List<Folder>> findAllFolderForDirectory() {
        List<Folder> folders = this.findListBy("folderType", FolderType.FOLDER.getValue().toString());
        return folders.stream().collect(Collectors.groupingBy(Folder::getParentId));
    }

    @Override
    public List<Folder> findAllFolderByParentId(String parentId, Integer folderType) {
        return folderDao.findAllByParentIdAndFolderTypeAndStatusOrderByOrderId(parentId, folderType,1);
    }

    @Override
    public void saveFolder(Folder folder) throws BigDataBusinessException {
        List<Folder> listForName = this.findListBy(new String[]{"folderName", "folderType", "parentId"},
                new String[]{folder.getFolderName(), folder.getFolderType().toString(), folder.getParentId()});
        folder.setModifyTime(new Date());
        if (StringUtils.isBlank(folder.getId())) {
            if (listForName.size() > 0) {
                throw new BigDataBusinessException("该名称已存在!");
            }
            folder.setId(UuidUtils.generateUuid());
            folder.setCreationTime(new Date());
            folderDao.save(folder);
            return;
        }

        // 验证名称和code是否存在
        if (listForName.size() > 0) {
            if (!listForName.get(0).getId().equals(folder.getId())) {
                throw new BigDataBusinessException("该名称已存在!");
            }
        }

        folderDao.update(folder, new String[]{"folderName", "usage", "status"
                , "orderId", "remark", "modifyTime"});
    }

    @Override
    public List<Folder> findFolderByUsage(Integer usage) {
        return folderDao.findAllByUsage(usage);
    }

    @Override
    public Map<String, List<FolderDetail>> findFolderDetailMap(String unitId, String userId, String[] folderIds) {
        List<FolderDetail> authorityFolderDetail = folderDetailService.findAllAuthorityFolderDetail(unitId, userId, folderIds);
        return authorityFolderDetail.stream().collect(Collectors.groupingBy(FolderDetail::getFolderId));
    }

    @Override
    public List<FolderEx> findFolderTree() {
        return findChildFolder("00000000000000000000000000000000");
    }

    @Override
    public List<FolderEx> findAuthFolderTree(String unitId, String userId) {
        List<FolderDetail> authorityFolderDetail = folderDetailService.findAllAuthorityFolderDetail(unitId, userId, null);
        Set<String> authFolder = new HashSet<>();
        authorityFolderDetail.forEach(e->{
            getAllFolderIds(authFolder, e.getFolderId());
        });
        return findAuthChildFolder("00000000000000000000000000000000", authFolder);
    }

    private void getAllFolderIds(Set<String> authFolder, String folderId) {
        Folder folder = this.findOne(folderId);
        if (!"00000000000000000000000000000000".equals(folder.getParentId())) {
            authFolder.add(folder.getParentId());
            getAllFolderIds(authFolder, folder.getParentId());
        }
    }

    @Override
    public void updateFolderName(Folder folder) throws BigDataBusinessException {
        Folder older = this.findOne(folder.getId());

        List<Folder> listForName = this.findListBy(new String[]{"folderName", "folderType", "parentId"},
                new String[]{folder.getFolderName(), older.getFolderType().toString(), older.getParentId()});

        // 验证名称和code是否存在
        if (listForName.size() > 0) {
            if (!listForName.get(0).getId().equals(folder.getId())) {
                throw new BigDataBusinessException("该名称已存在!");
            }
        }

        folderDao.update(folder, new String[]{"folderName"});
    }

    @Override
    public void deleteFolder(String folderId, Integer folderType) throws BigDataBusinessException {
        if (folderType == FolderType.DIRECTORY.getValue()) {
            // 目录下有目录或者文件夹的不能删除
            List<Folder> childFolder = folderDao.findAllByParentIdAndStatusOrderByOrderId(folderId, 1);
            if (childFolder.size() > 0) {
                throw new BigDataBusinessException("该目录下存在目录或者文件夹，您不能删除!");
            }
        } else {
            List<FolderDetail> details = folderDetailService.findListBy("folderId", folderId);
            if (details.size() > 0) {
                throw new BigDataBusinessException("该文件不是空的，您不能删除!");
            }
        }

        this.delete(folderId);
    }

    @Override
    public Integer getMaxOrderIdByParentId(String parentId) {
        Integer orderId = folderDao.getMaxOrderIdByParentId(parentId);
        return orderId == null ? 0 : orderId;
    }

    private List<FolderEx> findChildFolder(String parentId) {
        List<FolderEx> folderTree = Lists.newArrayList();
        List<Folder> firstFolders = this.findAllFolderByParentId(parentId, FolderType.DIRECTORY.getValue());
        for (Folder f : firstFolders) {
            if (f.getFolderType() == 2) {
                continue;
            }
            FolderEx folderEx = new FolderEx();
            folderEx.setId(f.getId());
            folderEx.setFolderName(f.getFolderName());
            folderEx.setFolderType(f.getFolderType());
            folderEx.setChildFolder(findChildFolder(f.getId()));
            folderTree.add(folderEx);
        }
        return folderTree;
    }

    private List<FolderEx> findAuthChildFolder(String parentId, Set<String> authFolder) {
        List<FolderEx> folderTree = Lists.newArrayList();
        List<Folder> firstFolders = this.findAllFolderByParentId(parentId, FolderType.DIRECTORY.getValue());
        for (Folder f : firstFolders) {
            if (f.getFolderType() == 2) {
                continue;
            }
            if (!authFolder.contains(f.getId())) {
                continue;
            }
            FolderEx folderEx = new FolderEx();
            folderEx.setId(f.getId());
            folderEx.setFolderName(f.getFolderName());
            folderEx.setFolderType(f.getFolderType());
            folderEx.setChildFolder(findAuthChildFolder(f.getId(), authFolder));
            folderTree.add(folderEx);
        }
        return folderTree;
    }
}
