<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>结亲对象管理</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<#assign OBJECT_STATE_RELATIONED = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_RELATIONED") />
<#assign OBJECT_STATE_FLOZEN = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_FLOZEN") />
<#assign OBJECT_STATE_INITIA = stack.findValue("@net.zdsoft.familydear.common.FamDearConstant@OBJECT_STATE_INITIA") />
<div class="row" id="importDiv">
    <input type="hidden" id = "tabType" value="${tabType!}">
    <div class="col-xs-12">
        <div class="box box-default">

            <div class="row">
                <div class="col-sm-12" >
                    <div class="box-body">
                        <div class="filter ">
                            <div class="row">
                                <div class="filter-item">
                                    <span class="filter-name">结亲对象：</span>
                                    <div class="filter-content">
                                        <input type="text" class="form-control" id="objName" style="width:168px;" >
                                    </div>
                                </div>
                                <#if tabType == '1'>
                                        <#if isAdmin?default(false) >
                                            <div class="filter-item">
                                                <span class="filter-name">结亲干部：</span>
                                                <div class="filter-content">
                                                    <input type="text" class="form-control" id="cadreName" style="width:168px;" >
                                                </div>
                                            </div>

                                        </#if>

                                    <#else>
                                    <input type="hidden" id="cadreName"value="" />
                                </#if>
                                <input type="hidden" name="teacherId" id="teacherId" value="${teacherId!}" />
                                <div class="filter-item">
                                    <span class="filter-name">结亲村：</span>
                                    <div class="filter-content">
                                        <select name="villageName" id="villageName" class="form-control" notnull="false" onChange="searchList();" style="width:168px;">
                                        ${mcodeSetting.getMcodeSelect("DM-XJJQC", '', "1")}
                                        </select>
                                        <#--<input type="text" class="form-control" id="villageName" style="width:168px;" >-->
                                    </div>
                                </div>

                                <div class="filter-item">
                                    <span class="filter-name">干部部门：</span>
                                    <div class="filter-content">
                                        <select class="form-control" id="deptId" name="deptId" onChange="searchList()">
                                            <option value="">请选择</option>
                                            <#if deptList?exists && (deptList?size>0)>
                                                <#list deptList as item>
                                                     <option value="${item.id!}">${item.deptName!}</option>
                                                </#list>
                                            <#else>
                                                <option value="">未设置</option>
                                            </#if>
                                        </select>
                                        <#--<select name="villageName" id="villageName" class="form-control" notnull="false" onChange="searchList();" style="width:168px;">-->
                                        <#--${mcodeSetting.getMcodeSelect("DM-XJJQC", '', "1")}-->
                                        <#--</select>-->
                                    <#--<input type="text" class="form-control" id="villageName" style="width:168px;" >-->
                                    </div>
                                </div>
                                <#if tabType == '2'>
                                    <input type="hidden" name="label" value="${OBJECT_STATE_FLOZEN!}" />
                                    <div class="filter-item">
                                        <button class="btn btn-blue js-addTerm" onclick="searchList();">查询</button>
                                        <button class="btn btn-blue js-addTerm" onclick="flozenObject(${OBJECT_STATE_INITIA!});">解除冻结</button>
                                    </div>
                                </#if>
                             </div>
                            <#if tabType == '1'>
                                <div class="row">

                                    <div class="filter-item">
                                        <label for="" class="filter-name">结亲标记：</label>
                                        <div class="filter-content">
                                            <select name="label" id="label" class="form-control" notnull="false" onChange="searchList();" style="width:168px;">
                                                <option value="">全部</option>
                                                <option value="1">已结亲</option>
                                                <option value="0">未结亲</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="filter-item">
                                        <label for="" class="filter-name">类别：</label>
                                        <div class="filter-content">
                                            <select name="type" id="type" class="form-control" notnull="false" onChange="searchList();" style="width:168px;">
                                                ${mcodeSetting.getMcodeSelect("DM-JQLB", '', "1")}
                                            </select>
                                        </div>
                                    </div>
                                    <div class="filter-item">
                                        <button class="btn btn-blue js-addTerm" onclick="searchList();">查询</button>
                                        <#if isAdmin?default(false) >
                                            <button class="btn btn-blue js-addTerm" onclick="addObject('');">+新增</button>
                                            <button class="btn btn-blue js-addTerm" onclick="doObjectImport();">导入</button>
                                            <!--<a href="javascript:doObjectImport();" class="btn btn-blue btn-seach">导入</a>-->

                                            <button class="btn btn-blue js-addTerm" onclick="releaseObject();">解除结亲</button>
                                            <button class="btn btn-blue js-addTerm" onclick="flozenObject(${OBJECT_STATE_FLOZEN!});">冻结</button>
                                        </#if>

                                    </div>
                                </div>
                            <#else>
                                <input type="hidden" name="label" id="label"  value="${OBJECT_STATE_FLOZEN!}" />
                                <input type="hidden" name="type"  id="type" value="" />
                            </#if>

                            <div class="table-wrapper" id="showList">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                $(function(){
                   searchList();
                });


                function　searchList(){
                    var objName = $('#objName').val();
                    var cadreName = $('#cadreName').val();
                    var deptId = $("#deptId").val();
                    var options=$("#villageName option:selected");
                    var village = "";
                    if(options.val()) {
                        village = options.text();
                    }
                    var label = $('#label').val();
                    var type = $('#type').val();
                    var teacherId = $('#teacherId').val();

                    var str = "?tabType=${tabType!}&objName="+encodeURIComponent(encodeURIComponent(objName))+"&cadreName="+encodeURIComponent(encodeURIComponent(cadreName))+"&villageName="+village+"&label="+label + "&type="+type +"&teacherId="+teacherId+"&deptId="+deptId;
                    var url = "${request.contextPath}/familydear/cadresRelation/list"+str;

                    $("#showList").load(url);
                }
                function addObject(objId){

                    var url = "${request.contextPath}/familydear/cadresRelation/add?objId="+objId;
                    indexDiv = layerDivUrl(url,{title: "结亲对象信息",width:750,height:700});
                }
                function releaseObject(){
                    var objIds = "";
                    var selEle = $('#list [name="objectId"]:checkbox:checked');
                    if(selEle.size()<1){
                        layerTipMsg(false,"请选择一个结亲对象!","");
                        return;
                    }
                    for(var i=0;i<selEle.size();i++){
                        if (i == selEle.size() - 1) {
                            objIds +=selEle.eq(i).val();
                        }else{
                            objIds +=selEle.eq(i).val() +",";
                        }
                    }
                    loadReleaseDialog(objIds);
                }

                function flozenObject(state){
                    var objIds = "";
                    var selEle = $('#list [name="objectId"]:checkbox:checked');
                    if(selEle.size()<1){
                        layerTipMsg(false,"请选择一个结亲对象!","");
                        return;
                    }
                    for(var i=0;i<selEle.size();i++){
                        if (i == selEle.size() - 1) {
                            objIds +=selEle.eq(i).val();
                        }else{
                            objIds +=selEle.eq(i).val() +",";
                        }
                    }
                    showConfirmMsg('确认进行该操作？','提示',function(){
                        var options={
                            url:"${request.contextPath}/familydear/cadresRelation/objFlozen",
                            dataType:"json",
                            type:"post",
                            data:{"objIds":objIds,"state":state},
                            success:function(data){
                                var jsonO = data;
                                if(!jsonO.success){
                                    layerTipMsg(jsonO.success,"冻结失败",jsonO.msg);
                                    return;
                                }else{
                                    layer.closeAll();
                                    layer.msg(jsonO.msg, {
                                        offset: 't',
                                        time: 2000
                                    });
                                    searchRelationList();
                                }
                            },
                            clearForm:false,
                            resetForm:false,
                            error:function(XMLHttpRequest ,textStatus,errorThrown){}

                        };
                        $.ajax(options);
                    });
                }

                function doObjectImport(){

                    $("#showDiv").load("${request.contextPath}/familydear/object/import/main");
                }



            </script>
