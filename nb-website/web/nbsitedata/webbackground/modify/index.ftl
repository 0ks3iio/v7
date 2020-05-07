<title>网站后台</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${webUrl}/static/ace/css/ui.jqgrid.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${webUrl}/static/sweetalert/sweetalert.css" />
<script src="${webUrl}/static/sweetalert/sweetalert.min.js"></script>

<div class="row">
	<#if manager?default(false) >
	<div class="col-xs-12">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs pull-left" id="tabId">

				 	<li <#if container?default('b') == 'b'>class="active" </#if> >
				 		<a data-toggle="tab" href="#b">实验区简介维护</a>
				 	</li>
				 	
				 </ul>
			</div>
			<div class="tab-content">
				
				<div id="b" class="tab-pane fade in <#if container?default('b') == 'b'>active</#if>">
					<@search  m=true divId="b" useSeach=true />
				</div>
				
			</div>
		</div>
	</div>
	<#else>
		<div class="col-xs-12" id="">
			<@search  m=false divId="" useSeach=true />
		</div>
	</#if>
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
	    <#-- 日历的一个插件 -->
		initCalendar(".row");
		var url =  "${request.contextPath}/sitedate/modify/12/list/page";
		<#if manager?default(false) && type?default('')!='11'>

			$("#b .listDiv").load(url+getParams("b")+"2");
		
			//搜索

		$("#b #btn-search").on("click",function(){
            if(!checkDate("b")){
                return ;
            }
			$("#b .listDiv").load(url+getParams("b")+"2");
		});
 

		
		
		$("#b #btn-add").unbind().bind("click",function(){
		
			location.href="#${request.contextPath}/sitedate/modify/12/addoredit/page?container=b";
		});
		
		<#else>
			$(".listDiv").load(url);
			$("#btn-search").on("click",function(){
                if(!checkDate("")){
                    return ;
                }
				$(".listDiv").load(url+getParams(""));
			});
		</#if>
      });
	
	function getIds(container){	
		var ids = new Array();
		var obj ; 
		if(container){
			obj = $("#"+container +" .cbx-td");
		}else{
		 
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
			params = params+"&page_index=1";
		}else{
		
			if("b"==divId){
				params = "?container=b&page_index=2";
			
			}
			
		}
		params = params + "&commitState=";
		return encodeURI(params);
	}	
	
	

</script>
<#macro search m=false divId="b" useSeach=true>
	<#if m?default(false)>
		<div class="filter " >
			<!--按钮-->
			<#if manager?default(false)&&divId=="b">
				<@btnItem btnId="btn-add" btnValue="新增" btnClass="plus"/>			
			</#if>
	        
		</div>
		<div class="widget-box ">
		<div class="widget-body">
			<div class="widget-main listDiv">
			</div>
		</div>
		</div>
	<#else>		
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
				<#if container?default('')!='e'>
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