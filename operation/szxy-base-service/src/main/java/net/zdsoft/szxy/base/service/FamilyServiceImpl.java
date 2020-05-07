package net.zdsoft.szxy.base.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.szxy.base.api.FamilyRemoteService;
import net.zdsoft.szxy.base.dao.FamilyDao;
import net.zdsoft.szxy.base.dao.UserDao;
import net.zdsoft.szxy.base.dto.UserUpdater;
import net.zdsoft.szxy.base.entity.Family;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.model.QFamily;
import net.zdsoft.szxy.base.model.QUser;
import net.zdsoft.szxy.base.query.FamilyQuery;
import net.zdsoft.szxy.dubbo.jpa.DubboPageImpl;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/3/21 下午3:15
 */
@Service("familyRemoteService")
public class FamilyServiceImpl implements FamilyRemoteService {

    private Logger logger = LoggerFactory.getLogger(FamilyRemoteService.class);

    @Resource
    private FamilyDao familyDao;
    @Resource
    private UserDao userDao;
    @Resource
    private PassportClient passportClient;

    @Override
    public Family getFamilyById(String id) {
        return familyDao.findById(id).orElse(null);
    }

    @Override
    public List<Family> getFamiliesById(String[] ids) {
        AssertUtils.hasElements(ids, "家长ID列表不能为空");
        return familyDao.getFamiliesById(ids);
    }

    @Override
    public List<Family> getFamiliesByStudentId(String[] studentIds) {
        AssertUtils.hasElements(studentIds, "家长ID列表不能为空");
        return familyDao.getFamiliesByStudentId(studentIds);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Record(type = RecordType.Call)
    @Override
    public void updateMobilePhone(String familyId, String mobilePhone) throws SzxyPassportException {
        AssertUtils.notNull(mobilePhone, "手机号不能为空");
        AssertUtils.notNull(familyId, "familyId 不能为空");

        familyDao.updateMobilePhoneById(mobilePhone, familyId);
        Optional<User> user = userDao.getUserByOwnerId(familyId);
        if (user.isPresent()) {
            Account account = new Account();
            account.setId(user.get().getAccountId());
            account.setPhone(mobilePhone);
            try {
                passportClient.modifyAccount(account, new String[]{"phone"});
            } catch (PassportException e) {
                logger.error("同步更新Passport手机号出错", e);
                throw new SzxyPassportException("同步更新Passport出错");
            }
            UserUpdater updater = new UserUpdater();
            updater.setId(user.get().getId());
            updater.setMobilePhone(mobilePhone);
            userDao.updateUser(updater);
        }
    }

    @Record(type = RecordType.Call)
    @Override
    public Page<Family> queryFamilies(FamilyQuery familyQuery, Pageable page) {
        return DubboPageImpl.of(familyDao.findAll((Specification<Family>) (root, query, criteriaBuilder) -> {

            List<Predicate> ps = new ArrayList<>(5);
            if (StringUtils.isNotBlank(familyQuery.getMobilePhone())) {
                ps.add(criteriaBuilder.like(root.get(QFamily.mobilePhone), familyQuery.getMobilePhone() + "%"));
            }
            if (StringUtils.isNotBlank(familyQuery.getRealName())) {
                ps.add(criteriaBuilder.like(root.get(QFamily.realName), familyQuery.getRealName() + "%"));
            }
            if (StringUtils.isNotBlank(familyQuery.getUnitId())) {
                ps.add(criteriaBuilder.equal(root.get(QFamily.schoolId), familyQuery.getUnitId()));
            }
            if (CollectionUtils.isNotEmpty(familyQuery.getRegions())) {
                Predicate[] regionPredicates = familyQuery.getRegions().stream().map(region->{
                    return criteriaBuilder.like(root.get(QFamily.regionCode), region + "%");
                }).toArray(Predicate[]::new);
                ps.add(criteriaBuilder.or(regionPredicates));
            }
            if (Objects.nonNull(familyQuery.getUsername())) {
                Join<Family, User> join = root.join(QFamily.users);
                ps.add(criteriaBuilder.like(join.get(QUser.username), familyQuery.getUsername() + "%"));
            }
            return query.where(ps.toArray(new Predicate[0])).getRestriction();
        }, page));
    }
}
