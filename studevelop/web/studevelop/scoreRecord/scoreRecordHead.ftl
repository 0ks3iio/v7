<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>社团活动登记</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row" id="importDiv">
	<div class="col-xs-12">
	   <div class="box box-default">
	      <#--<div class="box-body">-->
		  <#--<!-- PAGE CONTENT BEGINS &ndash;&gt;-->
			<#---->
           <#--</div>-->

           <div class="row">
               <div class="col-sm-2">
                   <div class="box box-default" id="id1">
                       <div class="box-header">
                           <h3 class="box-title">班级菜单</h3>
                       </div>
				   <@studevelopTreemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick"/>
                   </div>
               </div>
	<div class="col-sm-10" >
	      <div class="box-body">
		<div class="filter clearfix"">
	
				<div class="filter-item">
					<span class="filter-name">学年：</span>
					<div class="filter-content">
						<select name="acadyear" id="acadyear" onchange="doSearch2();" class="form-control">
							<#if acadyearList?exists && acadyearList?size gt 0>
								<#list acadyearList as item >
									<option value="${item!}" <#if item==acadyear?default("")>selected="selected"</#if>>${item!}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">学期：</span>
					<div class="filter-content">
						<select name="semester" id="semester" onchange="doSearch2();" class="form-control">
							<option value="1" <#if "1"==semester?default("")>selected="selected"</#if>>第一学期</option>
							<option value="2" <#if "2"==semester?default("")>selected="selected"</#if>>第二学期</option>
						</select>
					</div>
				</div>

	           <input type="hidden" id="studentId">	           
		</div>
		<div id="showList">
	    </div>
	</div>
</div>
</div>
<script>
	$(function(){
		$('#id2').height($('#id1').height());
	})
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "student"){
			var id = treeNode.id;
			$("#studentId").val(id);
			doSearch(id);
		}
	}
	function doSearch(id){
	    var acadyear = $('#acadyear').val();
	    var semester = $('#semester').val();
	    var url = "${request.contextPath}/studevelop/scoreRecord/list?acadyear="+acadyear+"&semester="+semester+"&studentId="+id;
	    $('#showList').load(url);
	}
	function doSearch2(){
	    var acadyear = $('#acadyear').val();
	    var semester = $('#semester').val();
	    var studentId = $('#studentId').val();
	    if(studentId == ''){
	        return;
	    }
	    var url = "${request.contextPath}/studevelop/scoreRecord/list?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId;
	    $('#showList').load(url);
	}
</script>

