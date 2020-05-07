<title>网站后台</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${webUrl}/static/ace/css/ui.jqgrid.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${webUrl}/static/sweetalert/sweetalert.css" />
<script src="${webUrl}/static/sweetalert/sweetalert.min.js"></script>

<div class="row">
    <div class="col-xs-12">
        <div class="tab-container">
            <div class="tab-header clearfix">
                <ul class="nav nav-tabs pull-left" id="tabId">
                    <#if manager?default(false) && type?default('')!='11'>
                    <li <#if container?default('a') == 'a'>class="active" </#if> >
                        <a data-toggle="tab" href="#a">待审信息</a>
                    </li>
                    </#if>
                    <li <#if container?default('') == 'b'||(!manager?default(false)&&container?default('')=='')>class="active" </#if> >
                        <a data-toggle="tab" href="#b">最近发布</a>
                    </li>
                    <li <#if container?default('') == 'c'>class="active" </#if> >
                        <a data-toggle="tab" href="#c">草稿箱</a>
                    </li>
                </ul>
            </div>
            <div class="tab-content">
                <#if manager?default(false) && type?default('')!='11'>
                <div id="a" class="tab-pane fade in <#if container?default('a') == 'a'>active</#if>">
                    <@search  m=true divId="a" useSeach=true />
                </div>
                </#if>
                <div id="b" class="tab-pane fade in <#if container?default('') == 'b'||(!manager?default(false)&&container?default('')=='')>active</#if>">
                    <@search  m=true divId="b" useSeach=true />
                </div>
                <div id="c" class="tab-pane fade in <#if container?default('') == 'c'>active</#if>">
                    <@search  m=false divId="c" useSeach=true />
                </div>
            </div>
        </div>
    </div>

</div><!-- 条件筛选结束 -->


<!-- page specific plugin scripts -->
<script type="text/javascript">
    var indexDiv = 0;
    var scripts = [null,
        "${webUrl}/static/ace/components/moment/moment.js",
        "${webUrl}/static/ace/components/bootstrap-daterangepicker/daterangepicker.js",
        "${webUrl}/static/ace/components/chosen/chosen.jquery.js",null
    ];
    $('.page-content-area').ace_ajax('loadScripts', scripts, function() {
        initCalendar(".row");
        var url =  "${request.contextPath}/sitedata/workbench/list/page";
    <#if manager?default(false) && type?default('')!='11'>
        $("#a .listDiv").load(url+"?state=pending&container=a");
        $("#a #btn-pass").unbind().bind("click",function(){
            updateState("2","a");
        });
        $("#a #btn-unpass").unbind().bind("click",function(){
            updateState("4","a");
        });
        $("#a #btn-search").on("click",function(){
            if(!checkDate("a")){
                return ;
            }
            $("#a .listDiv").load(url+getParams("a")+"&state=pending");
        });
    </#if>
        $("#b .listDiv").load(url+"?state=recent&container=b");
        $("#c .listDiv").load(url+"?state=uncommit&container=c");
        //搜索

        $("#b #btn-search").on("click",function(){
            if(!checkDate("b")){
                return ;
            }
            $("#b .listDiv").load(url+getParams("b")+"2&state=recent");
        });
        $("#c #btn-search").on("click",function(){
            if(!checkDate("c")){
                return ;
            }
            $("#c .listDiv").load(url+getParams("c")+"0&state=uncommit");
        });
        //新增
        $("#btn-add").on("click",function(){
        <#if type?default('')=='11' && !isInsert?default(true)>
            alert("你最多可以新增六条数据！");
            return ;
        </#if>
          <#if manager?default(false)>         
            location.href="#${request.contextPath}/sitedata/workbench/addoredit/page?container=a";
          <#else>
            location.href="#${request.contextPath}/sitedata/workbench/addoredit/page?container=b";
          </#if>
        });
        


        //刪除
        $("#btn-remove").on("click",function(){
            var ids = getIds("c");
            if(ids.length){
                var isDelete = false;
                layer.confirm('确认删除所选数据?',{icon:3,title:'提示'} ,function(index){
                    layer.close(index);
                    $.ajax({
                        url:'${request.contextPath}/sitedata/${type!}/delete?ids='+JSON.stringify(ids),
                        type:'post',
                        cache:false,
                        contentType: "application/json",
                        success:function(data) {
                            var jsonO = JSON.parse(data);
                            if(!jsonO.success){
                                showMsgError(jsonO.msg,"操作失败!",function(index){
                                    layer.close(index);
                                    //$("btn-remove").removeClass("disabled");
                                });
                            }
                            else{
                                // 需要区分移动端和非移动端返回处理不一样
                                showMsgSuccess(jsonO.msg,"操作成功!",function(index){
                                    layer.close(index);
                                });
                                $("#c .listDiv").load(url+"?state=uncommit&container=c");
                                
                            }
                        }
                    });
                });
            }else{
                alert("请选择数据");
            }
        });



    });

    function getIds(container){
        var ids = new Array();
        var obj ;
        if(container){
            obj = $("#"+container+" .cbx-td");
        }
        else{
            obj = $(".cbx-td");
        }
        $(obj).each(function(){
            if($(this).find("span").attr("chk")=="true"){
                ids.push($(this).find("input").attr("id"));
            }
        });
        return ids;
    }

    function getParams(divId){
        var params = "";
        if(divId == ''){
            if("commitTime"==$("#option").val()){
                params = "?startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val();

            }else{
                params = "?title="+$("#titleInput").val();
            }
            params = params+"&container=a&page_index=1";
        }else{
            if("commitTime"==$("#"+divId+" #option").val()){
                params = "?startDate="+$("#"+divId+" #startDate").val()+"&endDate="+$("#"+divId+" #endDate").val();
            }else{
                params = "?title="+$("#"+divId+" #titleInput").val();
            }

            if("a"==divId){
                params = params+"&container=a&page_index=1";
            }
            else if("b"==divId){
                params = params+"&container=b&page_index=2";
            }
            else if("c"==divId){
                params = params+"&container=c&page_index=3";
            }
            else if("d"==divId){
                params = params+"&container=d&page_index=4";
            }
        }
        params = params + "&commitState=";
        return encodeURI(params);
    }

    function checkDate(divId){
        var isOk = true;
        if("" != divId){
            var startDate = $("#"+divId+" #startDate").val();
            var endDate = $("#"+divId+" #endDate").val();
            if(startDate!=null&&startDate!="" && endDate!=null&&endDate!="" && startDate > endDate){
                layer.tips('开始日期不能大于结束日期','#'+divId+' #startDate',{tips:3,tipsMore:true});
                isOk = false;
            }
        }else{
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            if(startDate!=null&&startDate!="" && endDate!=null&&endDate!="" && startDate > endDate){
                layer.tips('开始日期不能大于结束日期','#startDate',{tips:3,tipsMore:true});
                isOk = false;
            }
        }
        return isOk;
    }

    function optionChange(obj,container){
        if("commitTime"==$(obj).val()){
            $("#"+container+" #inputDiv").css("display","none");
            $("#"+container+" #timeDiv").show();
        }else{
            $("#"+container+" #timeDiv").css("display","none");
            $("#"+container+" #startDate").val("");
            $("#"+container+" #endDate").val("");
            $("#"+container+" #inputDiv").show();
        }
    }

    function updateState(state,container){
        var ids = getIds(container);
        if(!ids.length){
            alert("请选择数据！");
            return ;
        }
        $.ajax({
            url:'${request.contextPath}/sitedata/${type!}/commitoruncommit?commitState='+state+'&ids='+JSON.stringify(ids),
            type:'post',
            cache:false,
            contentType: "application/json",
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(!jsonO.success){
                    showMsgError(jsonO.msg,"操作失败!",function(index){
                        layer.close(index);
                    });
                }
                else{
                    // 需要区分移动端和非移动端返回处理不一样
                    showMsgSuccess(jsonO.msg,"操作成功!",function(index){
                        var url = "${request.contextPath}/sitedata/workbench/list/page";
                        $("#a .listDiv").load(url+"?state=pending&container=a");
                        $("#b .listDiv").load(url+"?state=recent&container=b");
                        $("#c .listDiv").load(url+"?state=uncommit&container=c");
                        layer.close(index);
                    });
                }
            }
        });
    }
</script>



<#macro search m=false divId="a" useSeach=true>
    <#if m?default(false)>
    <div class="filter " >
        <!--按钮-->
        <#if divId=="a">
            <@btnItem btnId="btn-pass" btnValue="通过" btnClass=""/>
            <@btnItem btnId="btn-add" btnValue="新增" btnClass="plus"/>
        <#elseif !manager?default(false)&&divId=="b">
           <@btnItem btnId="btn-add" btnValue="新增" btnClass="plus"/> 
        </#if>
        <#if useSeach>
            <@timeItem container=divId/>
        </#if>
    </div>
    <div class="widget-box ">
        <div class="widget-body">
            <div class="widget-main listDiv">
            </div>
        </div>
    </div>
    <#else>
    <div class="filter " >
        <!--按钮-->
        <#if divId?default('')!='c'>
        <@btnItem btnId="btn-add" btnValue="新增" btnClass="fa-plus"/>
        </#if>
        <@btnItem btnId="btn-remove" btnValue="刪除" btnClass="fa-remove"/>
        <div class="filter-item ">
        </div>
        <#if useSeach>
            <@timeItem container=divId/>
        </#if>
    </div>
    <div class="row listDiv">
    </div>
    </#if>
</#macro>

<#macro btnItem btnValue divClass="pull-left" btnClass="" btnId="">
<div class="filter-item  ${divClass!}">
    <@w.btn btnId="${btnId!}" btnValue="${btnValue!}" btnClass="${btnClass!}" />
</div>
</#macro>

<#macro timeItem container>
<div class="filter-item pull-right">
    <div class="filter-content" >
        <select style="width:100px;" id="option" data-placeholder="请选择" class="form-control" onChange="optionChange(this,'${container!}');">
            <option value="title">标题</option>
            <#if container?default('')!='c'>
                <option value="commitTime">提交时间</option>
            </#if>
        </select>
    </div>
    <div class="filter-content" id="inputDiv">
        <input type="text" class="form-control" id="titleInput" />
    </div>
    <div class="filter-content" style="display:none;" id="timeDiv">
        <div class="input-daterange input-group" >
            <@w.dateDivWithNoColumn nullable="false" id="startDate" displayName="" value="" inputClass="input-sm form-control"/>
            <span class="input-group-addon">
				<i class="fa fa-exchange"></i>
				</span>
            <@w.dateDivWithNoColumn nullable="false" id="endDate" displayName="" value="" inputClass="input-sm form-control"/>
        </div>
    </div>
    <@w.btn btnId="btn-search" btnValue="查找" btnClass="fa-align-center common-search" />
</div>
</#macro>
