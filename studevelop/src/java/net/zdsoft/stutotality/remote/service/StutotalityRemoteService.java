package net.zdsoft.stutotality.remote.service;

public interface StutotalityRemoteService {

    /**
     * 获取综合评价下排展示类别
     * @return
     */
    public String getStutotalitytypeList(String studentId);

    /**
     * 具体类别对应的数据
     * @param studentId
     * @param type
     * @return
     */
    public String getStutotalityItemResult(String studentId,String type);

    /**
     * 二维码扫描后首次调用
     * @param codeId
     * @return
     */
//    public String checkCode(String studentId,String codeId);

    /**
     * 直接检查并保存二维码的数据
     * @param studentId
     * @param codeId
     * @return
     */
    public String saveCode(String studentId,String codeId);

}
