<div class="tab-content" id="resultListDiv">
	<div id="cc" class="tab-pane active" role="tabpanel">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">行政班：</span>
				<div class="filter-content">
					<select name="classId" id="classId" class="form-control" onchange="itemShowList3()">
					<#if clazzList?exists && clazzList?size gt 0 >
						<#list clazzList as clazz>
							<option value="${clazz.id!}" <#if classId == clazz.id>selected="selected"</#if>>${clazz.classNameDynamic!}</option>
						</#list>
					</#if>
					 </select> 
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班主任：</span>
				<div class="filter-content">
					<p>${teacherName!}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">总人数：</span>
				<div class="filter-content">
					<p>${stuNum!}</p>
				</div>
			</div>
		</div>
		<div class="detail-div ">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th  style="width:110px">学号</th>
						<th  style="width:77px">姓名</th>
						<th  style="width:50px">性别</th>
						<th  style="width:60px">选课</th>
						<#if (tabNum>0)>
						<#list courseList as item>
							<th style="width:100px">${item.subjectName!}</th>
						</#list>
						</#if>
					</tr>
				</thead>
				<tbody>
				<#if dtolist?exists && dtolist?size gt 0>
					<#list dtolist as dtoA>
					<tr>
						<td>${dtoA.stuCode!}</td>
						<td>${dtoA.stuName!}</td>
						<td>${mcodeSetting.getMcode("DM-XB","${dtoA.stuSex!}")}</td>
						<td>${dtoA.stuChosenSubNames!}</td>
						<#if (tabNum>0)>
						<#assign i = 0>
						<#if dtoA.dtolist?exists && dtoA.dtolist?size gt 0>
							<#list dtoA.dtolist as dto>
								<#if i lt tabNum>
									<td>
									${dto.className!}
									<#if dto.place?default('') != ''>
									<br>${dto.place!}
									</#if>
									</td>
								</#if>
								<#assign i = i +1>
							</#list>
						</#if>
						<#if i lt 2>
							<#list i..2 as b>
								<td></td>
							</#list>
						</#if>
						</#if>
					</tr>
					</#list>
				<#else>
					<tr>
	                  	<td  colspan="88" align="center">
	                  		暂无数据
	                  	</td>
	                  <tr>
				</#if>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
function itemShowList3(){
	var classId = $("#classId").val();
	var url =  '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/class/page?classId='+classId;
	$("#itemShowDivId").load(url);
}
</script>
