		<form id="paramForm" >
		<div class="filter">
			<div class="filter-item" style="width: 100%">
				<span class="filter-name">是否允许学生端查询学生个人综合素质表：</span>
                <div class="filter-content">
				<label>
					<input type="hidden" name="params[15].paramType" value="STUFAM.QUERY.SWITCH">
					<input type="checkbox" name="params[15].param" id="querySwitch" value="1" class="wp wp-switch" <#if (paramMap['STUFAM.QUERY.SWITCH'])?exists && (paramMap['STUFAM.QUERY.SWITCH'].param)?default(0)==1>checked="checked"</#if> onclick="changeOp(this)">
					<span class="lbl"></span>
				</label>
				</div>
			</div>
			<#assign i=16>
			<div style="float: left">
			<#if gradeList1?exists && gradeList1?size gt 0>
			<#list gradeList1 as grade>
				<div class="filter-item" style="float: none">
					<span class="filter-name">${grade.gradeName!}：</span>
					<div class="filter-content">
						<label>
							<input type="hidden" name="params[${i}].paramType" value="SHOW.JUNIOR.SWITCH">
							<input type="hidden" name="params[${i}].gradeId" value="${grade.id!}">
							<input type="checkbox" name="params[${i}].param" value="1" class="wp wp-switch" <#if (paramMap2[grade.id])?exists && (paramMap2[grade.id].param)?default(0)==1>checked="checked"</#if> onclick="changeOp(this)">
							<span class="lbl"></span>
						</label>
					</div>
				</div>
				<#assign i=i+1>
			</#list>
			</#if>
			</div>
			<div style="float: left">
			<#if gradeList2?exists && gradeList2?size gt 0>
				<#list gradeList2 as grade>
					<div class="filter-item" style="float: none">
						<span class="filter-name">${grade.gradeName!}：</span>
						<div class="filter-content">
							<label>
								<input type="hidden" name="params[${i}].paramType" value="SHOW.JUNIOR.SWITCH">
								<input type="hidden" name="params[${i}].gradeId" value="${grade.id!}">
								<input type="checkbox" name="params[${i}].param" value="1" class="wp wp-switch" <#if (paramMap2[grade.id])?exists && (paramMap2[grade.id].param)?default(0)==1>checked="checked"</#if> onclick="changeOp(this)">
								<span class="lbl"></span>
							</label>
						</div>
					</div>
					<#assign i=i+1>
				</#list>
			</#if>
			</div>
			<div class="filter-item filter-item-right">
				<a href="javascript:void(0)" onclick="saveParam()" class="btn btn-blue">保存</a>
			</div>
		</div>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>类别</th>
						<th>项目</th>
						<th>最高可得分</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td rowspan="4">文化成绩</td>
						<td>学科成绩</td>
						<td>
							<input type="hidden" name="params[0].paramType" value="XKCJ.MAX.NUMBER">
							<input type="text" name="params[0].param" class="form-control number" value="${paramMap['XKCJ.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>英语笔试成绩</td>
						<td>
							<input type="hidden" name="params[1].paramType" value="YYBS.MAX.NUMBER">
							<input type="text" name="params[1].param" class="form-control number" value="${paramMap['YYBS.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>英语口试成绩</td>
						<td>
							<input type="hidden" name="params[2].paramType" value="YYKS.MAX.NUMBER">
							<input type="text" name="params[2].param" class="form-control number" value="${paramMap['YYKS.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>学科竞赛</td>
						<td>
							<input type="hidden" name="params[3].paramType" value="XKJS.MAX.NUMBER">
							<input type="text" name="params[3].param" class="form-control number" value="${paramMap['XKJS.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td rowspan="9">德育成绩</td>
						<td>操行等第</td>
						<td>
							<input type="hidden" name="params[4].paramType" value="CXDD.MAX.NUMBER">
							<input type="text" name="params[4].param" class="form-control number" value="${paramMap['CXDD.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>学生干部</td>
						<td>
							<input type="hidden" name="params[5].paramType" value="XSGB.MAX.NUMBER">
							<input type="text" name="params[5].param" class="form-control number" value="${paramMap['XSGB.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>社团骨干</td>
						<td>
							<input type="hidden" name="params[6].paramType" value="STGG.MAX.NUMBER">
							<input type="text" name="params[6].param" class="form-control number" value="${paramMap['STGG.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>值周表现</td>
						<td>
							<input type="hidden" name="params[7].paramType" value="ZZBX.MAX.NUMBER">
							<input type="text" name="params[7].param" class="form-control number" value="${paramMap['ZZBX.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>学农</td>
						<td>
							<input type="hidden" name="params[14].paramType" value="XN.MAX.NUMBER">
							<input type="text" name="params[14].param" class="form-control number" value="${paramMap['XN.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>军训</td>
						<td>
							<input type="hidden" name="params[8].paramType" value="JX.MAX.NUMBER">
							<input type="text" name="params[8].param" class="form-control number" value="${paramMap['JX.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>评优、先进</td>
						<td>
							<input type="hidden" name="params[9].paramType" value="PYXJ.MAX.NUMBER">
							<input type="text" name="params[9].param" class="form-control number" value="${paramMap['PYXJ.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>突出贡献</td>
						<td>
							<input type="hidden" name="params[10].paramType" value="TCGX.MAX.NUMBER">
							<input type="text" name="params[10].param" class="form-control number" value="${paramMap['TCGX.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>体育成绩</td>
						<td>
							<input type="hidden" name="params[11].paramType" value="TYCJ.MAX.NUMBER">
							<input type="text" name="params[11].param" class="form-control number" value="${paramMap['TYCJ.MAX.NUMBER'].param!}">
						</td>
					</tr>
					<tr>
						<td>全校性活动</td>
						<td>全校性活动</td>
						<td>
							<input type="hidden" name="params[12].paramType" value="QXXHD.MAX.NUMBER">
							<input type="text" name="params[12].param" class="form-control number" value="${paramMap['QXXHD.MAX.NUMBER'].param!}">
						</td>
					</tr>
				</tbody>
			</table>
			
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>类别</th>
						<th>最高可得分</th>
						<th>每届最高可得分</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td rowspan="5">五大节日</td>
						<td>
							<input type="hidden" name="params[13].paramType" value="5FESTIVAL.MAX.NUMBER">
							<input type="text" name="params[13].param" class="form-control number" value="${paramMap['5FESTIVAL.MAX.NUMBER'].param!}">
						</td>
						<td>
							<input type="text" name="params[13].paramPer" class="form-control number" value="${paramMap['5FESTIVAL.MAX.NUMBER'].paramPer!}">
						</td>
					</tr>
				</tbody>
			</table>
		</form>

<script src="${request.contextPath}/static/js/validate.js"></script>
<script>
$(function(){

});

function changeOp(obj){
}

var isSubmit = false;
function saveParam(){
	if(isSubmit){
        return;
    }
	var pattern=/[^0-9]/;
	var isOk = true;
    $("#paramForm .form-control").each(function(){
	    var val=$(this).val();
	    if(trim(val)==''){
			promptMessage(this,"最大分值不能为空，请输入！");
			isOk = false;
			return;
		}
		if(pattern.test(val)){
			promptMessage(this,"最大分值只能输入非负整数！");
			isOk = false;
			return;
		}
	    if(val&&val.length>3){
		    promptMessage(this,"最大分值不超过3位数！");
			isOk = false;
	        return;
	    }
    });
    if(!isOk){
        return;
    }
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/quality/param/save",
			dataType : 'json',
			success : function(data){
				showListDiv();
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
	    			isSubmit = false;
		 		}else{
					layerTipMsg(data.success,"保存成功","");
    			}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$("#paramForm").ajaxSubmit(options);
}

function promptMessage(key,msg){
    layer.tips(msg, key, {
        tipsMore: true,
        tips:2,
        time:3000
    });
}
</script>