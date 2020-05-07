<div class="row">
	<div class="col-sm-12">
		<#if (dtoList?exists && dtoList?size>0)>
			<#assign allCount=(dtoList?size)>
			<#list dtoList as item>
				
				<#if item_index==0>
					<div class="box box-default">
						<div class="box-body clearfix">
							  <h2 class="text-center">${emPlace.examPlaceCode!}考场座位号</h2>
				<#elseif (item_index)%6==0>
					<div class="box box-default">
						<div class="box-body clearfix">
				</#if>
				
				 <div class="col-sm-4" > 
                   <div class="box-boder exaRoom-border">
				         <h1 class="exaRoom-fontSize text-center">${item.seatNum!}</h1>
				         <table class="table table-bordered table-striped table-condensed table-hover exaRoom-table">
							<tbody>
								<tr>
									<td class="text-center">姓名：</td>
									<td>${item.student.studentName!}</td>
								</tr>
								<tr>
									<td class="text-center">考号：</td>
									<td>${item.examNumber!}</td>
								</tr>
								<tr>
									<td class="text-center">班级：</td>
									<td>${item.className!}</td>
								</tr>
								<tr>
									<td class="text-center">考场：</td>
									<td>${emPlace.examPlaceCode!}</td>
								</tr>
							</tbody>
						 </table>
				   </div>
			   </div>
				<#if (item_index+1)%6==0 || (item_index+1)==allCount>
						</div>
					</div>
				</#if>
			</#list>
		<#else>
		<#if emPlace?exists>
		<div class="box box-default">
			<div class="box-body clearfix">
				  <h2 class="text-center">${emPlace.examPlaceCode!}考场座位号</h2>
			</div>
		</div>
		</#if>
		</#if>
	</div>
</div>


		<div class="box box-default" style="display:none">
			<div class="box-body clearfix print">
			<div class="row" >	
		<#if (dtoList?exists && dtoList?size>0)>
			
			<#list dtoList as item>
				
				 <div class="col-xs-4"  <#if (item_index%15==0) && (item_index>0)>style="page-break-before:always"</#if>> 
                   <div class="box-boder exaRoom-border">
				         <h1 class="exaRoom-fontSize text-center">${item.seatNum!}</h1>
				         <table class="table table-bordered table-striped table-condensed table-hover exaRoom-table">
							<tbody>
								<tr>
									<td class="text-center">姓名：</td>
									<td>${item.student.studentName!}</td>
								</tr>
								<tr>
									<td class="text-center">考号：</td>
									<td>${item.examNumber!}</td>
								</tr>
								<tr>
									<td class="text-center">班级：</td>
									<td>${item.className!}</td>
								</tr>
								<tr>
									<td class="text-center">考场：</td>
									<td>${emPlace.examPlaceCode!}</td>
								</tr>
							</tbody>
						 </table>
				   </div>
			   </div>
			</#list>
		</#if>
			</div>
		</div>
</div>

