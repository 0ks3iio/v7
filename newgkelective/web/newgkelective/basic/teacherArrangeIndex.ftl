<!-- 复制到 -->
<div class="layer layer-copy">
	<div class="layer-content">
		<table width="100%">
			<tr>
				<td width="100"><p class="mb10">当前教师：</p></td>
				<td>
					<p class="mb10" id="curTeacher"></p>
				</td>
			</tr>
			<tr>
				<td valign="top"><p class="mb10">复制内容：</p></td>
				<td>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="1" class="wp"><span class="lbl"> 每天课时分配</span></label>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="2" class="wp"><span class="lbl"> 周课时分布</span></label>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="3" class="wp"><span class="lbl"> 互斥设置</span></label>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="4" class="wp"><span class="lbl"> 禁排时间</span></label>
				</td>
			</tr>
		</table>
		<div class="gk-copy" style="border: 1px solid #eee;">
			<div class="box-body padding-5 clearfix">
				<b class="float-left mt3">各科教师</b>
				<div class="float-right input-group input-group-sm input-group-search">
			        <div class="pull-left">
			        	<input type="text" id="findTeacher" class="form-control input-sm js-search" placeholder="输入教师姓名查询" value="">
			        </div>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default" onClick="findTeacher();">
					    	<i class="fa fa-search"></i>
					    </button>
				    </div>
			    </div>
			</div>
			<table class="table no-margin">
				<tr>
					<th width="127">科目</th>
					<th><label><input type="checkbox" name="copyTeacherAll" class="wp"><span class="lbl"> 全选</span></label></th>
				</tr>
			</table>
			<div class="gk-copy-side" id="myscrollspy">
				<ul class="nav gk-copy-nav" style="margin: 0 -1px 0 -1px; height:384px;">
					<#if coumap?exists>
	                    <#list coumap?keys as mkey>
	                    	<li id="course_${mkey!}" class="courseLi"><a  href="#aaa_${mkey!}" data-value="${mkey!}">${coumap[mkey]!}
	                    	<span class="badge badge-default"></span></a></li>
	                    </#list>
	                </#if>
				</ul>
				</ul>
			</div>
			<div class="gk-copy-main copyteacherTab">
				<div data-spy="scroll" data-target="#myscrollspy" id="scrollspyDivId"style="position:relative;height:384px;overflow:auto;border-left: 1px solid #eee;">
					<#if coumap?exists>
	                    <#list coumap?keys as mkey>
	                    	<div id="aaa_${mkey!}"  data-value="${mkey!}">
								<div class="form-title ml-15 mt10 mb10">${coumap[mkey]!}<a class="color-blue ml10 font-12 js-clearChoose" href="javascript:">取消</a> <a class="color-blue ml10 font-12 js-allChoose" href="javascript:">全选</a> </div>
								<ul class="gk-copy-list">
									 <#if subjectTeacherPlanExMap?exists && subjectTeacherPlanExMap?size gt 0>
									 <#if subjectTeacherPlanExMap[mkey]?exists>
									 <#assign teachTeachers = subjectTeacherPlanExMap[mkey] />
									 <#if teachTeachers?exists && teachTeachers?size gt 0>
	                               	     <#list teachTeachers as tt >
											<label class="mr20"><input type="checkbox" class="wp" name="copyTeacher" value="${tt.id!}" data-value="${tt.teacherName!}"><span class="lbl"> ${tt.teacherName!}</span></label>
										 </#list>
									 </#if>
									 </#if>
									 </#if>
								</ul>
							</div>
	                    </#list>
	                </#if>
				</div>
			</div>
		</div>
		<div class="no-data-container" id="noDataId" style="display:none;">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
				<div class="no-data-body">
					<p class="no-data-txt">没有相关数据</p>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="showTeacherArrange"></div>
<script>
function canLoadOther(){
	return true;
}
function backClick(){
	//如果不执行切换
}

$(function(){
	$('#scrollspyDivId').scrollspy({ target: '#myscrollspy' })
	
	var url = '${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherSet/index/list?courseId=${courseId!}&useMaster=${useMaster?default(0)}';
	$("#showTeacherArrange").load(url);
})
</script>