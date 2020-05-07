<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
    <table class="table table-striped table-hover table-layout-fixed no-margin">
      <thead>
          <tr>
                <th style="width:20%" align="center">寝室楼</th>
                <th style="width:15%" align="center">寝室类别</th>
                <th style="width:15%" align="center">寝室属性</th>
                <th style="width:15%" align="center">寝室号</th>
                <th style="width:10%" align="center">容纳人数</th>
                <th style="width:15%" align="center">楼层</th>
				<th style="width:10%">操作</th>
          </tr>
      </thead>
      <tbody>
      	  <#if roomList?exists && roomList?size gt 0>
      	  	  <#list roomList as room>
      	  	  	 <tr>
      	  	  	 	<td>${room.buildName!}</td>
      	  	  	 	<td><#if room.roomType?default("1")=="1">男寝室<#else>女寝室</#if></td>
      	  	  	 	<td><#if room.roomProperty?default("1")=="1">学生寝室<#else>老师寝室</#if></td>
      	  	  	 	<td>${room.roomName!}</td>
      	  	  	 	<td>${room.capacity!}</td>
      	  	  	 	<td>${room.floor!}</td>
      	  	  	 	<td>
      	  	  	 		<a href="javascript:" class="color-lightblue"  onclick="editRoom('${room.id!}')">修改</a>
      	  	  	 		<a href="javascript:" class="color-blue"  onclick="deleteRoom('${room.id!}')">删除</a>
      	  	  	 	</td>
      	  	  	 </tr>
      	  	  </#list>
      	  <#else>
	          <tr >
	          	<td colspan="7" align="center">
	          		暂无数据
	          	</td>
	          <tr>
          </#if>
      </tbody>
  </table>
<@htmlcom.pageToolBar container="#tabDiv" class="noprint">
</@htmlcom.pageToolBar>