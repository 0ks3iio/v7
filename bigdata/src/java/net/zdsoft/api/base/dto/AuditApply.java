package net.zdsoft.api.base.dto;

/**
 * @author shenke
 * @since 2019/5/23 下午5:36
 */
public final class AuditApply {

    private String ticketKey;
    private Long count;

    public AuditApply(String ticketKey, Long count) {
        this.ticketKey = ticketKey;
        this.count = count;
    }

    public String getTicketKey() {
        return ticketKey;
    }

    public void setTicketKey(String ticketKey) {
        this.ticketKey = ticketKey;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
