<#--<div class="box box-default">-->
<#--<div class="box-header">-->
<#--<h4 class="box-title">成长手册</h4>-->
<#--</div>-->
<#--&lt;#&ndash;<div class="box-body">&ndash;&gt;-->
<#--&lt;#&ndash;<div class="no-data-container">&ndash;&gt;-->
<#--&lt;#&ndash;<div class="no-data">&ndash;&gt;-->
<#--&lt;#&ndash;<span class="no-data-img">&ndash;&gt;-->
<#--&lt;#&ndash;<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">&ndash;&gt;-->
<#--&lt;#&ndash;</span>&ndash;&gt;-->
<#--&lt;#&ndash;<div class="no-data-body">&ndash;&gt;-->
<#--&lt;#&ndash;<h3>成长手册制作中</h3>&ndash;&gt;-->
<#--&lt;#&ndash;</div>&ndash;&gt;-->
<#--&lt;#&ndash;</div>&ndash;&gt;-->
<#--&lt;#&ndash;</div>&ndash;&gt;-->
<#--&lt;#&ndash;</div>&ndash;&gt;-->
<#--</div>-->
<div class="main-content">

    <div class="main-content-inner">
        <div class="page-content">
            <div class="box box-default">
                <div class="box-header">
                    <h4 class="box-title">学生手册</h4>
                </div>
                <div class="box-body">
                    <div >
                        <div class="filter">
                            <div class="filter-item">
                                <span class="filter-name">学年：</span>
                                <div class="filter-content">
                                    <select name="acadyear" id="acadyear" class="form-control" onchange="doSearch();" >
                                    <#if acadyearList?exists && (acadyearList?size gt 0)>
                                        <#list acadyearList as item>
                                            <option value="${item!}" <#if acadyear == item?default('')> selected </#if> >${item!}</option>
                                        </#list>
                                    <#else>
                                        <option value="">暂无数据</option>
                                    </#if>
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <span class="filter-name">学期：</span>
                                <div class="filter-content">
                                    <select vtype="selectOne"  name="semester" id="semester" class="form-control" onchange="doSearch()">
                                     ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                                    </select>
                                </div>
                            </div>

                            <div class="filter-item">
                                <span class="filter-name">班级：</span>
                                <div class="filter-content">

                                    <select name="classId" id="classId" class="form-control" onchange="doSearch();" >
                                        <#if isAdmin?default(false) >
                                            <option value=""  >全部班级</option>
                                        </#if>
                                    <#if classList?exists && (classList?size gt 0) >
                                        <#list classList as class>
                                            <option value="${class.id!}" >${class.classNameDynamic!}</option>
                                        </#list>
                                    </#if>
                                    </select>
                                </div>
                            </div>
                            <div class="filter-item">
                                <label for="" class="filter-name">姓名：</label>
                                <div class="filter-content">
                                    <input type="text" class="form-control"   name="studentName" id="studentName"  />
                                </div>
                            </div>
                            <div class="filter-item">
                                <button type="button"  class="btn btn-blue" onclick="doSearch();">查找</button>
                            </div>
                        </div>
                    </div>
                    <div class="filter">
                        <div class="state-default">
                            <div class="filter-item">
                                <button type="button" class="btn btn-blue js-toManage">批量管理</button>
                            </div>
                        </div>
                        <div class="state-inManage hidden">
                            <div class="filter-item">
                                <button type="button" class="btn btn-blue" id="allSelect">全选</button>
                                <button type="button" class="btn btn-blue" style="display:none;" id="allCancel">取消全选</button>

                            </div>
                            <div class="filter-item">

                                <button type="button" class="btn btn-green export-btn">导出手册</button>
                                <button type="button" class="btn btn-lightblue release-btn" >将手册发布给家长</button>
                            </div>

                            <div class="filter-item"><button type="button" class="btn btn-blue js-confirm">确定</button></div>
                        </div>
                    </div>

                </div>
                </div>
            </div>

            <div class="card-list card-list-xs clearfix" id="studentListDiv">

            </div>
        </div><!-- /.page-content -->
    </div>
</div><!-- /.main-content -->
</div><!-- /.main-container -->
<script type="text/javascript">

    jQuery(document).ready(function () {
        doSearch();
    });
    function doSearch() {
        var classId = $("#classId").val();
        var acadyear = $("#acadyear").val();
        var semester = $("#semester").val();
        var studentName = $("#studentName").val();

        $("#studentListDiv").load(encodeURI(encodeURI("${request.contextPath}/studevelop/devdoc/studentList?classId=" + classId +"&acadyear=" + acadyear + "&semester=" + semester +"&studentName=" + studentName)));

    }
    function saveSmall(){
    	  var options = {
            url:"${request.contextPath}/studevelop/common/attachment/saveSmall?",
            type:"post",
            dataType:"json",
            success:function (data) {
                if(data.success){
                    layer.closeAll();
                    layerTipMsg(data.success,"处理成功",data.msg);
                    doSearch();
                }
                else{
                    layer.closeAll();
                    layerTipMsg(data.success,"处理失败",data.msg);
                }
            }
        }
        $.ajax(options);
    }
    function getClassList() {
        var acadyear = $("#acadyear").val();

        var options = {
            url:"${request.contextPath}/studevelop/devdoc/classList?acadyear=" + acadyear,
            type:"post",
            dataType:"json",
            success:function (data) {
                var len =data.length;
                var html = "<option value=\"\"  >全部班级</option> ";
                if(len == 0){
                    layerTipMsg(false,"提示","该学年下没有班级信息!");
                }
                for(var i=0;i<len;i++){
                    var id = data[i].id;
                    var name = data[i].classNameDynamic
                    html += "<option value=\"" +id+"\" >" +name+"</option>";
                }
                $("#classId").html(html);
            }
        }

        $.ajax(options);

    }

    $(function(){
        $('.js-toManage').on('click',function(e){
            //debugger;
            $('.state-default').addClass('hidden');
            $('.state-inManage').removeClass('hidden');
            $('.card-list').addClass('in-manage');
           // e.preventdefault();

        })
        $('.js-confirm').on('click',function(){
            //debugger;
            $('.state-default').removeClass('hidden');
            $('.state-inManage').addClass('hidden');
            $('.card-list').removeClass('in-manage');
        })
        
        $('.release-btn').on('click',function(){
        	batchRelease();
        });
        
        $('.export-btn').on('click',function(){
        	exportDoc();
        });
    });




</script>
