<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" onchange="changeList()">
						<#list acadyearList as ac>
							<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" onchange="changeList()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">行政班级：</span>
				<div class="filter-content">
					<select name="classId" id="classId" class="form-control" style="width:120px" onchange="changeList();">
						<option value="00000000000000000000000000000000">--请选择--</option>
						<#if clazzList?exists && clazzList?size gt 0>
							<#list clazzList as item> 
								<option value="${item.id!}">${item.classNameDynamic!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter">
				<div class="filter-item filter-item-right" >
					<button class="btn btn-blue" onclick="addStat()">新增</button>
				</div>
			</div>
		</div>
		<div class="table-container" id = "otherStatListDiv">
						
		</div>
	</div>
</div>
<script>
	$(function(){
		changeList();
	});

	function changeList() {
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		var classId = $("#classId").val();
		var str = "?classId="+classId+"&acadyear="+acadyear+"&semester="+semester;
		var url =  '${request.contextPath}/stuwork/otherStart/list/page'+str;
		$("#otherStatListDiv").load(url);
	}
	
	function addStat() {
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		var str = "?acadyear="+acadyear+"&semester="+semester;
		var url = "${request.contextPath}/stuwork/otherStart/edit/page"+str;
		indexDiv = layerDivUrl(url,{title: "新增",width:520,height:380});
	}
</script>