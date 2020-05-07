package net.zdsoft.framework.pagedata.dao;

import net.zdsoft.framework.pagedata.entity.FwDataGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FwDataGroupJpaDao extends JpaRepository<FwDataGroup, String> {

    FwDataGroup findByCode(String code);

}
