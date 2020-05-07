<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="cc" class="tab-pane active" role="tabpanel">
<div class="filter">
    <div class="filter-item">
		<a class="btn btn-blue js-export" href="javascript:void(0);" onClick="exportTeacher();";>导出名单</a>
	</div>
	<div class="filter-item filter-item-right">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <select name="" id="type" class="form-control">
		        	<option value="1" <#if type=="1">selected="selected"</#if>>姓名</option>
		        	<option value="2" <#if type=="2">selected="selected"</#if>>身份证号</option>
		        </select>
		        <div class="pos-rel pull-left">
		        	<input type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" id="searchCon" value="${searchCon!}">
		        </div>
			    
			    <div class="input-group-btn">
			    	<button type="button" class="btn btn-default" onClick="searchAudit();">
				    	<i class="fa fa-search"></i>
				    </button>
			    </div>
		    </div><!-- /input-group -->
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">单位：</span>
		<div class="filter-content">
			<select vtype="selectOne" name="" id="schId" class="form-control" onChange="searchAudit();">
			<option value="">--请选择--</option>
			<#if unitList?exists && unitList?size gt 0>
			    <#list unitList as sch>
				<option value="${sch.id!}" <#if schId?exists && sch.id == schId>selected="selected"</#if>>${sch.unitName!}</option>
				</#list>
			</#if>
			</select>
		</div>
	</div>
</div>
<#if registerInfoList?exists && registerInfoList?size gt 0>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
				    <th>序号</th>
					<th>教师姓名</th>
					<th>性别</th>
					<th>民族</th>
					<th>考号</th>
					<th>身份证号</th>
					<th>单位</th>
					<th>报名科目</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<#if registerInfoList?exists && registerInfoList?size gt 0>
				    <#list registerInfoList as item>
				<tr>
				    <td>${item_index+1!}</td>
				    <td>${item.teacherName!}</td>
				    <td>${item.sex!}</td>
				    <td>${item.nation!}</td>
				    <td>${item.cardNo!}</td>
				    <td>${item.identityCard!}</td>
				    <td>${item.unitName!}</td>
				    <td width="20%" style="word-break:break-all;">${item.subName!}</td>
				    <td>
				        <a class="color-blue js-see" href="javascript:void(0);" onClick="examCardEdit('${item.examId!}','${item.teacherId!}')";>查看准考证</a>
                    </td>
				</tr>				
				</#list>
			<#else>
				<tr>
					<td colspan="9" align="center">暂无数据</td>
				</tr>
			</#if>
			</tbody>
		</table>
		<@htmlcom.pageToolBar container="#siteTabDiv" class="noprint">
	    </@htmlcom.pageToolBar>
	</div>
</div>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有相关数据</p>
		</div>
	</div>
</div>
</#if>
<div class="layer layer-see"></div>
<script>
$(function(){
    var classIdSearch = $('#schId');
	$(classIdSearch).chosen({
	width:'350px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
	});
})

function searchAudit(){
   var examId = '${examId!}';
   var schId = $('#schId').val();
   var searchCon = $('#searchCon').val();
   var type = $('#type').val();
   if(type == '1'){
       var url = "${request.contextPath}/teaexam/siteSet/query/examCardList?schId="+schId+"&examId="+examId+"&teacherName="+searchCon+"&type="+type;
       $('#siteTabDiv').load(url);
   }else{
       var url = "${request.contextPath}/teaexam/siteSet/query/examCardList?schId="+schId+"&examId="+examId+"&identityCard="+searchCon+"&type="+type;
       $('#siteTabDiv').load(url);
   }
}

function exportTeacher(){
   var examId = '${examId!}';
   var schId = $('#schId').val();
   var searchCon = $('#searchCon').val();
   var type = $('#type').val();
   var url = "";
   if(type == '1'){
       url = "${request.contextPath}/teaexam/siteSet/query/examCardExport?schId="+schId+"&examId="+examId+"&teacherName="+searchCon;
   }else{
       url = "${request.contextPath}/teaexam/siteSet/query/examCardExport?schId="+schId+"&examId="+examId+"&identityCard="+searchCon;
   }
   document.location.href = url;
}

function examCardEdit(examId, teacherId){
   var url = "${request.contextPath}/teaexam/siteSet/query/examCardEdit?examId="+examId+"&teacherId="+teacherId;
   $(".layer-see").load(url,function() {
		layerShow();
	});
}

function layerShow(){
    layer.open({
		type: 1,
		shadow: 0.5,
		title: false,
		area: '620px',
		//btn: ['打印准考证'],
		yes: function(index){
			//printExamCard();
		},
		content: $('.layer-see')
	});
}
</script>
</div>