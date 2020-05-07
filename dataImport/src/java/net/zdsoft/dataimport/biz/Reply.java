package net.zdsoft.dataimport.biz;

import net.zdsoft.dataimport.config.ImportApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

/**
 * @author shenke
 * @since 2017.08.03
 */
public class Reply<QT extends QImportEntity> {

    private String id;
    private String globeError;
    private Long creationTime;
    private ImportState importState;

    private SimpMessagingTemplate simpMessagingTemplate;

    private TaskReply<QT> taskReply;

    public Reply() {
        this.creationTime = System.currentTimeMillis();
        this.importState = ImportState.WAIT;
        taskReply = bulidTaskReply();
    }

    public static Reply buildGlobeErrorReply(String globeError) {
        Reply reply = new Reply();
        reply.setGlobeError(globeError);
        reply.taskReply = bulidTaskReply();
        return reply;
    }

    public static Reply.TaskReply bulidTaskReply() {
        return new TaskReply();
    }

    public void setJavaObjects(List<QT> javaObjects) {
        this.taskReply.<QT>setJavaObjects(javaObjects);
    }

    public List<QT> getJavaObjects() {
        return this.taskReply.getJavaObjects();
    }

    public Reply<QT> setErrorObjects(List<QT> errorObjects) {
        this.taskReply.<QT>setErrorObjects(errorObjects);
        return this;
    }

    public List<QT> getErrorObjects() {
        return this.taskReply.getErrorObjects();
    }

    public Reply setHeaders(List<String> headers) {
        this.taskReply.setHeaders(headers);
        return this;
    }

    public void clearGlobeError() {
        this.globeError = "";
    }

    public List<String> getHeaders() {
        return this.taskReply.getHeaders();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlobeError() {
        return globeError;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public ImportState getImportState() {
        return importState;
    }

    public void setImportState(ImportState importState) {
        this.importState = importState;
    }

    /**
     * 该方法内部回实时通知客户端
     * @param globeError
     * @return
     */
    public Reply setGlobeError(String globeError) {
        this.globeError = globeError;
        this.importState = ImportState.ERROR;
        notifyClient(ImportState.ERROR.getCode(), globeError);
        return this;
    }

    /**
     * 通知客户端解析状态
     */
    public void notifyClient(int importStateCode , String msg) {
        JSONResponse response = new JSONResponse()
                .setImportStateCode(importStateCode)
                .setBusinessValue(this.id)
                .setMsg(msg);
        getSimpMessagingTemplate().convertAndSend("/import/status", response);
    }

    private SimpMessagingTemplate getSimpMessagingTemplate() {
        if ( simpMessagingTemplate == null ) {
            this.simpMessagingTemplate = ImportApplicationContext.getApplication().getBean(SimpMessagingTemplate.class);
        }
        return simpMessagingTemplate;
    }

    public static class TaskReply<QT> {

        private TaskReply() {

        }

        private List<QT> javaObjects;
        private List<QT> errorObjects;
        private List<String> headers;

        public List<QT> getJavaObjects() {
            return javaObjects;
        }

        public void setJavaObjects(List<QT> javaObjects) {
            this.javaObjects = javaObjects;
        }

        public List<QT> getErrorObjects() {
            return errorObjects;
        }

        public void setErrorObjects(List<QT> errorObjects) {
            this.errorObjects = errorObjects;
        }

        public List<String> getHeaders() {
            return headers;
        }

        public void setHeaders(List<String> headers) {
            this.headers = headers;
        }
    }
}