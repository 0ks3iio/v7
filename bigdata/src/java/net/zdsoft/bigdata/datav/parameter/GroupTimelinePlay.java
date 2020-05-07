package net.zdsoft.bigdata.datav.parameter;

/**
 * @author shenke
 * @since 2018/11/14 下午7:07
 */
public class GroupTimelinePlay {

    private Boolean timelineRewind;
    private Integer timelinePlayInterval;

    public Boolean getTimelineRewind() {
        return timelineRewind;
    }

    public void setTimelineRewind(Boolean timelineRewind) {
        this.timelineRewind = timelineRewind;
    }

    public Integer getTimelinePlayInterval() {
        return timelinePlayInterval;
    }

    public void setTimelinePlayInterval(Integer timelinePlayInterval) {
        this.timelinePlayInterval = timelinePlayInterval;
    }
}
