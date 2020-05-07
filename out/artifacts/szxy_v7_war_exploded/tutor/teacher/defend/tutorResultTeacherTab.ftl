
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">姓名：</span>
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
		        	<input type="text" id="teacherName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead">
		        </div>
			    
			    <div class="input-group-btn">
			    	<a href="javascript:void(0);" onclick="showTeaList()" type="button" class="btn btn-default">
				    	<i class="fa fa-search"></i>
				    </a>
			    </div>
		    </div>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">轮次名称：</span>
		<div class="filter-content">
			<select name="" id="tutorName" class="form-control" onChange="showTeaList()" style="width:180px">
				<option value="">---请选择---</option>
				<#if tutorList?exists && tutorList?size gt 0>
                  	<#list tutorList as tutor>
					 <option value="${tutor.id!}">${tutor.roundName!}</option>
              	    </#list>
                </#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">

		<a href="javascript:void(0);" onclick="maxNumEdit()" class="btn btn-blue js-setNumber" >可带人数设置</a>
		<a href="javascript:void(0);" onclick="showImport()" class="btn btn-blue">导入</a>
		<a href="javascript:void(0);" onclick="doTeaExport()" class="btn btn-blue">导出Excel</a>
	</div>
</div>

<div id="showTeaList" class="table-container">
</div>

<div id="maxNumEditLayer" class="layer layer-setNumber">
	
</div>
<script>
$(function(){
	showTeaList();
});
function showTeaList(){
	var teacherName = $("#teacherName").val().trim();
	var tutorId = $("#tutorName").val();
	var url =  '${request.contextPath}/tutor/result/teacher/list?teacherName='+encodeURIComponent(teacherName)+'&tutorId='+tutorId;
	$("#showTeaList").load(url);

}
function showImport(){
	var url =  '${request.contextPath}/tutor/import/main';
	$("#tabList").load(url);
}

function doTeaExport(){
	var teacherName = $("#teacherName").val().trim();
	var tutorId = $("#tutorName").val();
	document.location.href = "${request.contextPath}/tutor/result/teacher/export?teacherName="+encodeURIComponent(teacherName)+'&tutorId='+tutorId;
}

function maxNumEdit(){
	var url =  '${request.contextPath}/tutor/result/teacher/param/edit';
	$("#maxNumEditLayer").load(url,function() {
		  maxNumEditLayer();
		});
}
function maxNumEditLayer(){
	layer.open({
			type: 1,
			shade: .5,
			title: '可带人数设置',
			area: '320px',
			btn: ['确定', '取消'],
			yes: function(index){
			    saveMaxNum(index);
			  },
			content: $('.layer-setNumber')
		})
}
</script>
