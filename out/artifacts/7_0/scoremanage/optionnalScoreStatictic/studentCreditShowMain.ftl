<div id="studentAdminDiv">
            <#--<a id="backA" onclick="goBack();" style="display: none" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
            <div class="box box-default">
                <div class="box-body" id="studentContent">
                    <div class="filter" id="searchDiv">
                        <div class="filter-item">
                            <span class="filter-name">班级：</span>
                            <div class="filter-content">
                                <select name="" id="classIdSearch" class="form-control" id="classIdSearch" onChange="searchList()" style="width:168px;" >
                                <#if classList?exists && (classList?size>0)>
                                    <option value="">---请选择---</option>
                                    <#list classList as item>
                                        <option value="${item.id!}" <#if item.id == classId?default('') > selected</#if>>${item.classNameDynamic!}</option>
                                    </#list>
                                <#else>
                                    <option value="">---请选择---</option>
                                </#if>
                                </select>
                            </div>
                        </div>

                        <div class="filter-item filter-item-left">
                            <div class="filter-content">
                                <a class="btn btn-blue" onclick="searchList();" >查找</a>

                            </div>
                        </div>

                        <div class="filter-item filter-item-right">
                            <div class="filter-content">
                                <a class="btn btn-blue" href="javascript:void(0);" onclick="doExport()">导出</a>

                            </div>
                        </div>

                    </div>
                    <div id="showList" >
                    </div>
                </div>
            </div>
        </div><!-- /.page-content -->
</div><!-- /.main-container -->
<script type="text/javascript" >

    $(function(){
        //初始化单选控件
        var classIdSearch = $('#classIdSearch');
        $(classIdSearch).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
    });



    function　searchList(){
        var classId = $('#classIdSearch').val();
        if(classId == ""){
            layerTipMsgWarn("请选择一个班级！");
            return;
        }
        var url="${request.contextPath}/scoremanage/optionalScore/creditStatisticStuList.action?classId="+classId;
        $("#showList").load(url);
    }

    function doExport(){
        var classId = $('#classIdSearch').val();
        if(classId == ""){
            layerTipMsgWarn("提示","请选择一个班级！");
            return;
        }
        var url="${request.contextPath}/scoremanage/optionalScore/exportCreditStatisticStuList.action?classId="+classId;
        document.location.href = url;
    }
    

</script>