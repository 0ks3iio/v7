<div  class="tab-pane active">
    <div class="filter">
        <div class="filter-item">
            <span class="filter-name">学年：</span>
            <div class="filter-content">
                <select class="form-control" id="acadyear" name="acadyear" onChange="showList()">
					<#if acadyearList?exists && (acadyearList?size>0)>
	                    <#list acadyearList as item>
		                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
	                    </#list>
                    <#else>
	                    <option value="">未设置</option>
                     </#if>
				</select>
            </div>
        </div>
        <div class="filter-item">
            <span class="filter-name">学期：</span>
            <div class="filter-content">
                <select class="form-control" id="semester" name="semester" onChange="showList()">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
				</select>
            </div>
        </div>
        <div class="filter-item">
            <span class="filter-name">年级：</span>
            <div class="filter-content">
                <select class="form-control" id="gradeId" onChange="showClass()">
					<#if gradeList?exists && gradeList?size gt 0>
						<#list gradeList as item>
						<option value="${item.id!}" <#if item.id?default("")==gradeId?default("")>selected="selected"</#if>>${item.gradeName!}</option>
						</#list>
					</#if>
				</select>
            </div>
        </div>
        <div class="filter-item">
            <span class="filter-name">班级：</span>
            <div class="filter-content">
                <select class="form-control" id="classId" onChange="showList()">
                	
				</select>
            </div>
        </div>
        <div class="filter-item filter-item-right">
			<a a href="javascript:void(0);" onclick="doExport()" class="btn btn-blue">导出Excel</a>
		</div>
    </div>
</div>
<input type="hidden" id="type" value="${type!}">
<input type="hidden" id="unitId" value="${unitId!}">
<input type="hidden" id="userId" value="${userId!}">
<div id="showListDiv">

</div>
<script type="text/javascript">
	$(function(){
       showClass();
	})
	function showClass(){
		var gradeId=$("#gradeId").val();
		var unitId=$("#unitId").val();
		var userId=$("#userId").val();
		var classIdClass=$("#classId"); 
		$.ajax({
			url:"${request.contextPath}/quality/common/getClassList",
			data:{gradeId: gradeId,userId: userId,unitId: unitId},
			dataType: "json",
			success: function(json){
				var array=json.array;
				debugger;
				classIdClass.html("");
				var classComHtml="";
				if(array && array.length>0){
					classComHtml+='<option value="">全部</option>';
					for(var i=0;i<array.length;i++){
						classComHtml+='<option value="'+array[i].id+'">';
						classComHtml+=array[i].className;
						classComHtml+='</option>';
					}
					classIdClass.append(classComHtml);
				}
				showList();
			}
		});
	}
	function showList(){
		var type=$("#type").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		if(!classId){
			classId='';
		}
		if(!type){
			type="1";
		}
		var str="?type="+type+"&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&classId="+classId;
		var url="";
		if(type=="1"){
			url="${request.contextPath}/quality/collect/List1/page"+str;
		}else if(type=="2"){
			url="${request.contextPath}/quality/collect/List2/page"+str;
		}else if(type=="3"){
			url="${request.contextPath}/quality/collect/List3/page"+str;
		}else{
			url="${request.contextPath}/quality/collect/List4/page"+str;
		}
		$("#showListDiv").load(url);
	}
	
	function doExport(){
		var type=$("#type").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeId=$("#gradeId").val();
		var classId=$("#classId").val();
		if(!classId){
			classId='';
		}
		if(!type){
			type="1";
		}
		var str="?type="+type+"&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&classId="+classId;
		var url="";
		if(type=="1"){
			url="${request.contextPath}/quality/collect/List1/export"+str;
		}else if(type=="2"){
			url="${request.contextPath}/quality/collect/List2/export"+str;
		}else if(type=="3"){
			url="${request.contextPath}/quality/collect/List3/export"+str;
		}else{
			url="${request.contextPath}/quality/collect/List4/export"+str;
		}
		document.location.href = url;
	}
</script>