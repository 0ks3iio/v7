package net.zdsoft.bigdata.datav.vo;

import net.zdsoft.bigdata.datav.entity.ScreenGroup;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/14 上午9:46
 */
public class ScreenGroupVo extends ScreenGroup {

    private List<GroupScreenVo> groupScreenVos;


    public List<GroupScreenVo> getGroupScreenVos() {
        return groupScreenVos;
    }

    public void setGroupScreenVos(List<GroupScreenVo> groupScreenVos) {
        this.groupScreenVos = groupScreenVos;
    }

    public static class GroupScreenVo {
        private String id;
        private String unitId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }
    }
}
