<#if dshrMap??&& dshrMap?size gt 0>
  <#list dshrMap?keys as key>
      <div class="health-checkup">
        <h2>${key!}体检项目单</h2>
      <#if dshrMap[key]?exists && dshrMap[key]?size gt 0>
        <div class="filter" style="margin-left: 21%;">
			<div class="filter-item" style="width:150px;">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<p>${dshrMap[key][0].studentName!}</p>
				</div>
			</div>
			<div class="filter-item" style="width:130px;">
				<span class="filter-name">性别：</span>
				<div class="filter-content">
					<p>${mcodeSetting.getMcode("DM-XB","${dshrMap[key][0].sex!}")}</p>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<p>${dshrMap[key][0].className!}</p>
				</div>
			</div>
		</div>
		<table class="table" style="table-layout: fixed; ">
			<thead>
				<tr>
					<th width="30%">体检项</th>
					<th width="30%">单位</th>
					<th width="40%">检查结果</th>
				</tr>
			</thead>
			<tbody>                                 
            <#list dshrMap[key] as dshr>
		        <tr>
					<td>${dshr.itemName!}</td>
					<td><#if dshr.itemUnit?default("")=="">/<#else>${dshr.itemUnit!}</#if></td>
					<td>${dshr.itemResult!}</td>
				</tr>
            </#list>
            </tbody>
		</table>
	  </div>
    </#if>
  </#list>
<#else>
  <h2>暂无数据</h2>

</#if>