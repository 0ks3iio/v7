package net.zdsoft.szxy.operation.servermanage.dao;

import net.zdsoft.szxy.base.entity.Server;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/6 下午1:22
 */
@Repository
public interface ServerManageDao extends JpaRepository<Server, Integer> {

    /**
     * 获取所有可以授权的系统
     * serverClass=1 status=1 isDeleted=0
     * @see EnableServerDto
     * @return
     */
    @Query(
            value = "select new net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto(id, code, name) " +
                    "from Server where serverClass=1 and isDeleted=0 and status=1"
    )
    List<EnableServerDto> getAllEnableServers();
}
