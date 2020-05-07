<#--<a href="javascript:" class="page-back-btn gotoDivideClass"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-caption">${newGkDivide.divideName!}</h3>
	</div>
	<div class="box-body">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">选课结果：</label>
				<div class="col-sm-10"><#if newGkChoice?exists>${newGkChoice.choiceName!}</#if></div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">参考成绩：</label>
				<div class="col-sm-10"><#if referScore?exists>${referScore.name!}</#if></div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">开班模式：</label>
				<div class="col-sm-10">
					<#if newGkDivide.openType?default('')=='01'>
						全固定模式
						<#elseif newGkDivide.openType?default('')=='02'>
						半固定模式
						<#elseif newGkDivide.openType?default('')=='06'>
						全手动模式
						<#elseif newGkDivide.openType?default('')=='05'>
						全走单科分层模式
						<#elseif newGkDivide.openType?default('')=='03'>
						文理科分层教学模式 — 语数外独立分班
						<#elseif newGkDivide.openType?default('')=='04'>
						文理科分层教学模式 — 语数外跟随文理组合分班
						<#elseif newGkDivide.openType?default('')=='05'>
						全走班单科分层
						<#elseif newGkDivide.openType?default('')=='08'>
						智能组合分班
						<#elseif newGkDivide.openType?default('')=='09'>
							3+1+2单科分层（重组）
						<#elseif newGkDivide.openType?default('')=='10'>
							3+1+2组合固定（重组）
						<#elseif newGkDivide.openType?default('')=='11'>
						3+1+2单科分层（不重组）
						<#elseif newGkDivide.openType?default('')=='12'>
						3+1+2组合固定（不重组）
					</#if>
				</div>
			</div>
			<#--
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">每班正常容纳人数：</label>
				<div class="col-sm-10">${newGkDivide.galleryful!}±${newGkDivide.maxGalleryful!}人</div>
			</div>
			-->
			<hr style="border-width: 1px 0 0 0;border-style: dashed; border-color: #d9d9d9;">
			<#if newGkDivide.openType?default('')=='01' || newGkDivide.openType?default('')=='02' || newGkDivide.openType?default('')=='05' 
				|| newGkDivide.openType?default('')=='06' || newGkDivide.openType?default('')=='08' 
				|| newGkDivide.openType?default('')=='09' || newGkDivide.openType?default('')=='10'
				|| newGkDivide.openType?default('')=='11'
				|| newGkDivide.openType?default('')=='12'>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">选考科目：</label>
				<div class="col-sm-10">
				<#if courseAList?exists && courseAList?size gt 0>
					<#list courseAList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">学考科目：</label>
				<div class="col-sm-10">
				<#if courseBList?exists && courseBList?size gt 0>
					<#list courseBList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
				</div>
			</div>
			<#--
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding color-999">行政班上课科目：</label>
					<div class="col-sm-10">
					<#if courseOList?exists && courseOList?size gt 0>
						<#list courseOList as course>
						<#if course_index==0>
							${course!}
						<#else>
							,${course!}
						</#if>
						</#list>
					</#if>
					</div>
				</div>
			-->
			<#else>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999"></label>
				<div class="col-sm-10">理科班</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">理科组合：</label>
				<div class="col-sm-10">
				<#if courseLAList?exists && courseLAList?size gt 0>
					<#list courseLAList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">教学班组合：</label>
				<div class="col-sm-10">
				<#if courseLJList?exists && courseLJList?size gt 0>
					<#list courseLJList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
				</div>
			</div>
			<#--
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding color-999">行政班科目：</label>
					<div class="col-sm-10">
					<#if courseLOList?exists && courseLOList?size gt 0>
						<#list courseLOList as course>
						<#if course_index==0>
							${course!}
						<#else>
							,${course!}
						</#if>
						</#list>
					</#if>
					</div>
				</div>
			-->
			<hr style="border-width: 1px 0 0 0;border-style: dashed; border-color: #d9d9d9;">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999"></label>
				<div class="col-sm-10">文科班</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">文科组合：</label>
				<div class="col-sm-10">
				<#if courseWAList?exists && courseWAList?size gt 0>
					<#list courseWAList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>	
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999">教学班组合：</label>
				<div class="col-sm-10">
				<#if courseWJList?exists && courseWJList?size gt 0>
					<#list courseWJList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
				</div>
			</div>
			<#--
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding color-999">行政班科目：</label>
					<div class="col-sm-10">
					<#if courseWOList?exists && courseWOList?size gt 0>
						<#list courseWOList as course>
						<#if course_index==0>
							${course!}
						<#else>
							,${course!}
						</#if>
						</#list>
					</#if>
					</div>
				</div>
			-->
			</#if>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding color-999"></label>
				<#if (dtoList?exists && dtoList?size gt 0 )|| (aList?exists && aList?size gt 0) || (bList?exists && bList?size gt 0)>
					<div class="col-sm-10"><a href="javascript:" class="color-blue showTwoSet" data-value="1">查看详细设置</a></div>
				</#if>	
			</div>
		</div>
		<#--
		<p>选课结果：<#if newGkChoice?exists>${newGkChoice.choiceName!}</#if></p>
		<p>参考成绩：<#if referScore?exists>${referScore.name!}</#if></p>
		<p>开班模式：
			<#if newGkDivide.openType?default('')=='01'>
				全固定模式
				<#elseif newGkDivide.openType?default('')=='02'>
				半固定模式
				<#elseif newGkDivide.openType?default('')=='06'>
				全手动模式
				<#elseif newGkDivide.openType?default('')=='05'>
				全走单科分层模式
				<#elseif newGkDivide.openType?default('')=='03'>
				文理科分层教学模式 — 语数外独立分班
				<#elseif newGkDivide.openType?default('')=='04'>
				文理科分层教学模式 — 语数外跟随文理组合分班
				<#elseif newGkDivide.openType?default('')=='05'>
				全走班单科分层
		  </#if>
		<#if newGkDivide.openType?default('')=='01' || newGkDivide.openType?default('')=='02' || newGkDivide.openType?default('')=='05' || newGkDivide.openType?default('')=='06'>
		<p>选考科目：
			<#if courseAList?exists && courseAList?size gt 0>
				<#list courseAList as course>
				<#if course_index==0>
					${course!}
				<#else>
					,${course!}
				</#if>
				</#list>
			</#if>
		</p>
		<p>学考科目：
			<#if courseBList?exists && courseBList?size gt 0>
				<#list courseBList as course>
				<#if course_index==0>
					${course!}
				<#else>
					,${course!}
				</#if>
				</#list>
			</#if>
		</p>
		<p>行政班上课科目：
			<#if courseOList?exists && courseOList?size gt 0>
				<#list courseOList as course>
				<#if course_index==0>
					${course!}
				<#else>
					,${course!}
				</#if>
				</#list>
			</#if>
		</p>
		<#else>
			<h4>理科班</h4>
			<p>理科组合：
				<#if courseLAList?exists && courseLAList?size gt 0>
					<#list courseLAList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
			</p>
			<p>教学班科目：
				<#if courseLJList?exists && courseLJList?size gt 0>
					<#list courseLJList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
			</p>
			<p>行政班科目：
				<#if courseLOList?exists && courseLOList?size gt 0>
					<#list courseLOList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
			</p>
			<h4>文科班</h4>
			<p>文科科组合：
				<#if courseWAList?exists && courseWAList?size gt 0>
					<#list courseWAList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>	
			
			</p>
			<p>教学班科目：
				<#if courseWJList?exists && courseWJList?size gt 0>
					<#list courseWJList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
			</p>
			<p>行政班科目：
				<#if courseWOList?exists && courseWOList?size gt 0>
					<#list courseWOList as course>
					<#if course_index==0>
						${course!}
					<#else>
						,${course!}
					</#if>
					</#list>
				</#if>
			
			</p>
		</#if>
		<p>每班正常容纳人数：${newGkDivide.galleryful!}，±${newGkDivide.maxGalleryful!}人</p>	
		<#if (dtoList?exists && dtoList?size gt 0 )|| (aList?exists && aList?size gt 0) || (bList?exists && bList?size gt 0)>
			<a href="javascript:" class="showTwoSet" data-value="1">显示详细设置</a>
		</#if>	
		-->
		
		<#--
			<#if newGkDivide.openType?default('')=='03' || newGkDivide.openType?default('')=='04' >
				<#if dtoList?exists && dtoList?size gt 0>
				<#assign divtableNum=1>	
				<a href="javascript:" class="showTwoSet">详细设置</a>
				<div class="layer layer-setting-tip">
					<div class="layer-cotnent">
					<#list dtoList as dto>
						<h4>${dto.allgroupName!}组合</h4>
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>组合名称</th>
									<th>分层参考成绩</th>
									<th>班级数预设置</th>
								</tr>
							</thead>
							<tbody>
								<#assign exDtoList=dto.exList>
								<#if exDtoList?exists && exDtoList?size gt 0>
									<#list exDtoList as exdto>
										<tr>
											<td>${exdto.groupName!}</td>
											<td>
												<#if exdto.hierarchyScore?default('')=='1'>
													 单科成绩
												<#else>
													总成绩
												</#if>
				
											</td>
											<td>
												<#assign classSumNumMap=exdto.classSumNumMap>
												<#if classSumNumMap?exists && classSumNumMap?size gt 0>
													<#if classSumNumMap['A']?exists>
														<p><strong>A：</strong>&nbsp;&nbsp;${classSumNumMap['A'][0]?default(0)}个班，共${classSumNumMap['A'][1]?default(0)}名学生</p>
													</#if>
													<#if classSumNumMap['B']?exists>
														<p><strong>B：</strong>&nbsp;&nbsp;${classSumNumMap['B'][0]?default(0)}个班，共${classSumNumMap['B'][1]?default(0)}名学生</p>
													</#if>
													<#if classSumNumMap['C']?exists>
														<p><strong>C：</strong>&nbsp;&nbsp;${classSumNumMap['C'][0]?default(0)}个班，共${classSumNumMap['C'][1]?default(0)}名学生</p>
													</#if>
												<#else>
													暂无设置
												</#if>
											</td>
										</tr>
									</#list>
								</#if>
							</tbody>
						</table>
					</#list>
				</div>
			</div>
			</#if>
		<#else>
			<#if (aList?exists && aList?size gt 0) || (bList?exists && bList?size gt 0)>
				<a href="javascript:" class="showTwoSet">详细设置</a>
				<div class="layer layer-setting-tip" style="overflow:auto">
					<div class="layer-content">
					<#if aList?exists && aList?size gt 0>
					<div <#if bList?exists && bList?size gt 0>class="col-sm-6"</#if>>
						<h4>选考参数设置</h4>
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>科目</th>
									<th>班级数</th>
									<th>范围</th>
								</tr>
							</thead>
							<tbody>
								<#list aList as item>
									<tr>
										<td>${item.subjectName!}</td>
										<td>${item.classNum?default(0)}</td>
										<td>
										<#assign anum=-1>
										<#assign bnum=-1>
										<#if item.maximum?exists && item.leastNum?exists>
											<#assign anum=((item.maximum+item.leastNum)/2)>
											<#assign bnum=((item.maximum-item.leastNum)/2)>
										</#if>
											 ${anum?default(0)} ± ${bnum?default(0)}
										</td>
									</tr>
								</#list>
							</tbody>
						</table>
					</div>
					</#if>
					<#if bList?exists && bList?size gt 0>
						<div <#if aList?exists && aList?size gt 0>class="col-sm-6"</#if>>
						<h4>学考参数设置</h4>
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>科目</th>
									<th>班级数</th>
									<th>范围</th>
								</tr>
							</thead>
							<tbody>
								<#list bList as item>
									<tr>
										<td>${item.subjectName!}</td>
										<td>${item.classNum?default(0)}</td>
										<td>
											<#assign anum=-1>
											<#assign bnum=-1>
											<#if item.maximum?exists && item.leastNum?exists>
												<#assign anum=((item.maximum+item.leastNum)/2)>
												<#assign bnum=((item.maximum-item.leastNum)/2)>
											</#if>
											 ${anum?default(0)} ± ${bnum?default(0)}
										</td>
									</tr>
								</#list>
							</tbody>
						</table>
						</div>
					</#if>
					</div>
				</div>
			</#if>
		</#if>	
	-->		
	</div>
</div>

<#if newGkDivide.openType?default('')=='03' || newGkDivide.openType?default('')=='04' >
<div class="box box-default setDivide-parm" style="display:none;">
	<div class="box-header">
		<h3 class="box-caption">详细设置</h3>
	</div>
	<div class="box-body">
		<#if dtoList?exists && dtoList?size gt 0>
		<#assign divtableNum=1>	
			<#list dtoList as dto>
				<h4>${dto.allgroupName!}组合</h4>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>组合名称</th>
							<th>分层参考成绩</th>
							<th>班级数预设置</th>
						</tr>
					</thead>
					<tbody>
						<#assign exDtoList=dto.exList>
						<#if exDtoList?exists && exDtoList?size gt 0>
							<#list exDtoList as exdto>
								<tr>
									<td>${exdto.groupName!}</td>
									<td>
										<#if exdto.hierarchyScore?default('')=='1'>
											 单科成绩
										<#else>
											总成绩
										</#if>
		
									</td>
									<td>
										<#assign classSumNumMap=exdto.classSumNumMap>
										<#if classSumNumMap?exists && classSumNumMap?size gt 0>
											<#if classSumNumMap['A']?exists>
												<p><strong>A：</strong>&nbsp;&nbsp;${classSumNumMap['A'][0]?default(0)}个班，共${classSumNumMap['A'][1]?default(0)}名学生</p>
											</#if>
											<#if classSumNumMap['B']?exists>
												<p><strong>B：</strong>&nbsp;&nbsp;${classSumNumMap['B'][0]?default(0)}个班，共${classSumNumMap['B'][1]?default(0)}名学生</p>
											</#if>
											<#if classSumNumMap['C']?exists>
												<p><strong>C：</strong>&nbsp;&nbsp;${classSumNumMap['C'][0]?default(0)}个班，共${classSumNumMap['C'][1]?default(0)}名学生</p>
											</#if>
										<#else>
											暂无设置
										</#if>
									</td>
								</tr>
							</#list>
						</#if>
					</tbody>
				</table>
			</#list>
		</#if>
	</div>
</div>	
<#else>
<div class="box box-default setDivide-parm" style="display:none;">
	<div class="box-header">
		<h3 class="box-caption">详细设置</h3>
	</div>
	<div class="box-body">
		<div class="row">
		<#if (aList?exists && aList?size gt 0) || (bList?exists && bList?size gt 0)>
			<#if aList?exists && aList?size gt 0>
				<div <#if bList?exists && bList?size gt 0>class="col-sm-6"</#if>>
					<h4>选考参数设置</h4>
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>科目</th>
								<th>班级数</th>
								<th>范围</th>
							</tr>
						</thead>
						<tbody>
							<#list aList as item>
								<tr>
									<td>${item.subjectName!}</td>
									<td>${item.classNum?default(0)}</td>
									<td>
									<#assign anum=-1>
									<#assign bnum=-1>
									<#if item.maximum?exists && item.leastNum?exists>
										<#assign anum=((item.maximum+item.leastNum)/2)>
										<#assign bnum=((item.maximum-item.leastNum)/2)>
									</#if>
										 ${anum?default(0)} ± ${bnum?default(0)}
									</td>
								</tr>
							</#list>
						</tbody>
					</table>
				</div>
			</#if>
			<#if bList?exists && bList?size gt 0>
				<div <#if aList?exists && aList?size gt 0>class="col-sm-6"</#if>>
					<h4>学考参数设置</h4>
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>科目</th>
								<th>班级数</th>
								<th>范围</th>
							</tr>
						</thead>
						<tbody>
							<#list bList as item>
								<tr>
									<td>${item.subjectName!}</td>
									<td>${item.classNum?default(0)}</td>
									<td>
										<#assign anum=-1>
										<#assign bnum=-1>
										<#if item.maximum?exists && item.leastNum?exists>
											<#assign anum=((item.maximum+item.leastNum)/2)>
											<#assign bnum=((item.maximum-item.leastNum)/2)>
										</#if>
										 ${anum?default(0)} ± ${bnum?default(0)}
									</td>
								</tr>
							</#list>
						</tbody>
					</table>
				</div>
			</#if>
		</#if>
		</div>
	</div>
</div>
</#if>			
<script>
function goDivideIndex(){
	var url =  '${request.contextPath}/newgkelective/${newGkDivide.gradeId!}/goDivide/index/page';
	$("#showList").load(url);
}
$(function(){
	showBreadBack(goDivideIndex,false,"分班设置");
	<#if (dtoList?exists && dtoList?size gt 0 )|| (aList?exists && aList?size gt 0) || (bList?exists && bList?size gt 0)>
			<#--$('.showTwoSet').on('click', function(e){
				e.preventDefault();
				layer.open({
					type: 1,
					shadow: 0.5,
					title: '详细设置',
					area: '520px',
					btn:'知道了',
					content: $('.layer-setting-tip')
				})
			});-->
			$('.showTwoSet').on('click', function(e){
				var showId=$(this).attr("data-value");
				if(showId=="1"){
					$(".setDivide-parm").show();
					$(this).html("隐藏详细设置");
					$(this).attr("data-value","0");
				}else{
					$(".setDivide-parm").hide();
					$(this).html("显示详细设置");
					$(this).attr("data-value","1");
				}
				
			})
			
	</#if>
})
</script>