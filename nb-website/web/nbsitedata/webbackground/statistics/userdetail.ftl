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
            </div>
            <div class="tab-content">
            <tr>
             
			 
            </tr>
          
          <#--  <th style="width:14%;"> &nbsp;&nbsp;发布人： 
              <select id='select_id'  nullable="false" onChange="doUpdateType();">
                 <option name='commitUserId'  value ="" selected="selected">&lt;全部&gt;</option>
                 <option name='commitUserId'  value ="" ><全部></option>
                 <option name='commitUserId'  value ="" ><全部></option>
              </select> 
             
             </th> --> 
             
       
          <div id="a" class="tab-pane fade in <#if container?default('a') == 'a'>active</#if>">
                    <@search  m=true divId="a" useSeach=true />
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
        var url =  "${request.contextPath}/sitedata/statistics/userPosting/list/page";
  
        $("#a .listDiv").load(url+"?state=unit&container=a");
       
        $("#a #btn-search").on("click",function(){
            if(!checkDate("a")){
                return ;
            }
            $("#a .listDiv").load(url+getParams("a")+"&state=unit");
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
                params = "?title="+$("#title").val();
            }
            params = params+"&container=a&page_index=1";
        }else{
            params = "?startDate="+$("#"+divId+" #startDate").val()+"&endDate="+$("#"+divId+" #endDate").val()+"&title="+$("#title").val();
            var type=$("#typeSelect option:selected").val();
            if("a"==divId){
                params = params+"&container=a&page_index=1&type="+type;
            }    
        }
        var commitState=$("#commitStateSelect option:selected").val();
        var auditUserId='';
        params = params + "&commitState="+commitState+"&auditUserId="+auditUserId;
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

   
    
</script>



<#macro search m=false divId="a" useSeach=true>
    <#if m?default(false)>
    <div class="filter " >
        <!--按钮-->
          <th style="width:14%;  "> &nbsp;&nbsp;所属栏目： 
              <select id='typeSelect'  nullable="false" >
                <option name='type'  value ="" selected="selected">&lt;全部&gt;</option>
                <#if models?exists && models?size gt 0>
                  <#list models as model>
                  <option name='type'  value ="${model.thisId!}">${model.mcodeContent!}</option>
                  </#list>
                </#if>
              </select> 
              
             </th>
             <th style="width:14%;  "  > &nbsp;&nbsp;文章标题：&nbsp;<input id="title" type="text" style="width: 144px; Height: 30px;"   />
			    
			 </th>
          <th class="hidden-280"> &nbsp;&nbsp;审核状态：
             <select id='commitStateSelect'  nullable="false" style="width: 144px; hight: 30px;" >
                <option name='commitState'  value ="" selected="selected">&lt;全部&gt;</option>
                <option name='commitState'  value ="0" >未提交</option>
                <option name='commitState'  value ="1" >提交未审核</option>
                <option name='commitState'  value ="2" >已通过</option>
                <option name='commitState'  value ="4" >未通过</option>
                <option name='commitState'  value ="6" >撤回</option>
             </select> 			    
			 </th>         
      <#--       <th style="width:14%;"> &nbsp;&nbsp;发布部门： 
              <select id='select_id'  nullable="false" onChange="doUpdateType();">
                 <option name='commitUserId'  value ="" selected="selected">&lt;全部&gt;</option>
                 <option name='commitUserId'  value ="" ><全部></option>
                 <option name='commitUserId'  value ="" ><全部></option>
              </select> 
          </th> -->
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
       
      <option value="commitTime">提交时间</option>      
       
    </div>
    <div class="filter-content"  id="timeDiv">
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



