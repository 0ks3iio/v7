<div class="box box-default">
	<div class="box-body">
		<div class="filter" id="searchType">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" onchange="doSearch()" class="form-control">
						<#if acadyearList?exists && acadyearList?size gt 0>
							<#list acadyearList as item >
								<option value="${item!}" <#if item==dormDto.acadyear?default("")>selected="selected"</#if>>${item!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semesterStr" id="semesterStr" onchange="doSearch()" class="form-control" style="width:120px;">
						<option value="1" <#if "1"==dormDto.semesterStr?default("")>selected="selected"</#if>>第一学期</option>
						<option value="2" <#if "2"==dormDto.semesterStr?default("")>selected="selected"</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">周次：</span>
				<div class="filter-content">
					<select name="week" id="week" onchange="doSearch()" class="form-control" style="width:80px;">
						<#if weekList?exists && weekList?size gt 0>
							<#list weekList as item >
								<option value="${item!}" <#if item==dormDto.week?default(0)>selected="selected"</#if>>${item!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">星期：</span>
				<div class="filter-content">
					<select name="weekDay" id="weekDay" class="form-control" onchange="doSearch()" style="width:100px;">
						<option value="1" <#if 1==dormDto.weekDay?default(0)>selected="selected"</#if>>星期一</option>
						<option value="2" <#if 2==dormDto.weekDay?default(0)>selected="selected"</#if>>星期二</option>
						<option value="3" <#if 3==dormDto.weekDay?default(0)>selected="selected"</#if>>星期三</option>
						<option value="4" <#if 4==dormDto.weekDay?default(0)>selected="selected"</#if>>星期四</option>
						<option value="5" <#if 5==dormDto.weekDay?default(0)>selected="selected"</#if>>星期五</option>
						<option value="6" <#if 6==dormDto.weekDay?default(0)>selected="selected"</#if>>星期六</option>
						<option value="7" <#if 7==dormDto.weekDay?default(0)>selected="selected"</#if>>星期天</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学段：</span>
				<div class="filter-content">
					<select name="section" id="section" class="form-control" onchange="doSearch()"  style="width:100px;">
						<#if sectionList?exists && sectionList?size gt 0>
							<#list sectionList as item >
								<option value="${item!}" <#if item==dormDto.section?default(0)>selected="selected"</#if>><#if item==0>幼儿园<#elseif item==1>小学<#elseif item==2>初中<#elseif item==3>高中<#else>剑桥高中</#if></option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
		</div>
		
		<div class="table-container">
			<div class="table-container-header text-right">
				<button class="btn btn-white" onclick="doExport();">导出</button>
			</div>
			<form class="print">
			<div class="table-container-body">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th class="text-center" width="10%">班级</th>
							<th class="text-center" width="15%">表扬</th>
							<th  width="50%">批评</th>
							<th >其他情况</th>
						</tr>
					</thead>
					<tbody id="dayTbodyId">
						<#--<#if formList?exists && formList?size gt 0>
						<#list formList as item>
							<tr>
							<td class="text-center">${item.className!}</td>
							<#if item.formList?exists && item.formList?size gt 0>
							<td><#list item.formList as inForm>
								<#if inForm.excellent?default(false) && inForm.checkRemind?default("")=="">
									${inForm.roomName!}&nbsp&nbsp
								</#if>
								</#list>
							</td>
							<#assign inIndex=0>
							<td><#list item.formList as inForm>
									<#if !inForm.excellent?default(false) && inForm.checkResult?default("")!="">
									<#if inIndex!=0>;<#else>扣分:</#if>
									${inForm.roomName!}(${inForm.checkResult!})
									<#assign inIndex=inIndex+1></#if>
								</#list>
								<#if inIndex!=0>。&nbsp&nbsp</#if>
								<#assign inIndex=0>
								<#list item.formList as inForm>
								<#if inForm.checkRemind?default("")!="">
									<#if inIndex!=0>;<#else>提醒:</#if>
									${inForm.roomName!}(${inForm.checkRemind!})
									<#assign inIndex=inIndex+1>
								</#if>
								</#list>
								<#if inIndex!=0>。</#if>
							</td>
							<td><#list item.formList as inForm>
								<#if inForm.otherInfo?default("")!="">
								${inForm.roomName!}(${inForm.otherInfo!})&nbsp&nbsp
								</#if>
								</#list></td>
							<#else>
								<td></td><td></td><td></td>
							</#if>
							</tr>
							-- <tr>
								<#if item.formList?exists && item.formList?size gt 0>
								<td class="text-center" rowspan="${item.formList?size}">${item.className!}</td>
									<#list item.formList as inForm>
									<#if !(inForm_index==0)>
										<tr>
									</#if>
									   <td class="text-center">${inForm.roomName!}</td>
									   <td><#if inForm.excellent?default(false)><img src="${request.contextPath}/static/images/classCard/icon-flower.png" alt="">
									   	   <#else>${inForm.checkResult!}
									   	   </#if> 
									   </td>
									   <td>${inForm.checkRemind!}</td>
									   <#if !(inForm_index==0)>
										</tr>
										</#if>
									</#list>
								</#if>
							</tr>--
						</#list>
						<#else>
						<tr >
				          	<td colspan="4" align="center">
				          		暂无数据
				          	</td>
			         	 </tr>
						</#if>-->
					</tbody>
				</table>
			</div>
			</form>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
	$(function(){
		var itemTr="";
		<#if formList?exists && formList?size gt 0>
		<#list formList as item>
			itemTr+='<tr><td class="text-center">${item.className!}</td>';
			<#if item.formList?exists && item.formList?size gt 0>
				itemTr+='<td>';
				<#list item.formList as inForm>
					<#if inForm.excellent?default(false) && inForm.checkRemind?default("")=="">
						itemTr+='${inForm.roomName!}&nbsp&nbsp';
					</#if>
				</#list>
				itemTr+='</td>';
				<#assign inIndex=0>
				itemTr+='<td>';
				<#list item.formList as inForm>
					<#if !inForm.excellent?default(false) && inForm.checkResult?default("")!="">
						<#if inIndex!=0>itemTr+=';';<#else>itemTr+='扣分:';</#if>
						itemTr+='${inForm.roomName!}(${inForm.checkResult!})';
						<#assign inIndex=inIndex+1>
					</#if>
				</#list>
				<#if inIndex!=0>itemTr+='。&nbsp&nbsp';</#if>
				<#assign inIndex=0>
				<#list item.formList as inForm>
					<#if inForm.checkRemind?default("")!="">
						<#if inIndex!=0>itemTr+=';';<#else>itemTr+='提醒:';</#if>
						itemTr+='${inForm.roomName!}(${inForm.checkRemind!})';
						<#assign inIndex=inIndex+1>
					</#if>
				</#list>
				<#if inIndex!=0>itemTr+='。';</#if>
				itemTr+='</td>';
				itemTr+='<td>';
				<#list item.formList as inForm>
					<#if inForm.otherInfo?default("")!="">
						itemTr+='${inForm.roomName!}(${inForm.otherInfo!})&nbsp&nbsp';
					</#if>
				</#list>
				itemTr+='</td>';
			<#else>
				itemTr+='<td></td><td></td><td></td>';
			</#if>
		</#list>
		<#else>
			itemTr+='<tr><td colspan="4" align="center">暂无数据</td></tr>';
		</#if>
		$("#dayTbodyId").append(itemTr);
	});
	function doSearch(){
		var url="${request.contextPath}/stuwork/dorm/stat/dayList/page?"+searchUrlValue("#searchType");
		$("#itemShowDivId").load(url);
	}
	function doExport(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
		LODOP.SAVE_TO_FILE(getConditions()+"日查反馈表"+".xls");
	}
	function getConditions(){
		var acadyear=$("#acadyear").val();
		var semesterStr=$("#semesterStr").val();
		var week=$("#week").val();
		var weekDay=$("#weekDay").val();
		var section=$("#section").val();
		if(section==0){
			section="幼儿园";
		}else if(section==1){
			section="小学";
		}else if(section==2){
			section="初中";
		}else if(section==3){
			section="高中";
		}else if(section==4){
			section="剑桥高中";
		}
		if(semesterStr=="1"){
			semesterStr="一";
		}else if(semesterStr=="2"){
			semesterStr="二";
		}
		return acadyear+"学年第"+semesterStr+"学期第"+week+"周星期"+weekDay+section;
	}
</script>
