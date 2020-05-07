<#--<a href="javascript:void(0);" class="page-back-btn" onclick="goback('${gradeId!}')"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="filter">
	<div class="filter-item">
	<#--<h3 class="box-title">分析结果</h3>-->
	<a href="javascript:void(0);" class="btn btn-blue pull-right" id="toCountDetail">查看统计明细</a>
	</div>
</div>
<div class="box box-default">
	<input type="hidden" value="${choiceId!}" id="choiceId">
	<div class="box-body">
			<div class="row">
				<div class="col-md-6">
					<!-- S 三科统计结果 -->
					<div>
						<div class="table-container">
							<div class="table-container-header clearfix">
								<h4 class="pull-left">三科统计结果（共${newConditionList3?size}种）</h4>
								<button class="btn btn-blue pull-right" id="change3">转为两科</button>
							</div>	
							<div class="table-container-body result3">
								<table class="table table-striped table-hover no-margin">
									<thead>
										<tr>
											<th>
												<label class="pos-rel">
												<input name="course-checkbox" type="checkbox" class="wp wpall wpall3">
												<span class="lbl"></span>
												</label>
											</th>
											<th>序号</th>
											<th>学科</th>
											<th>选课人数</th>
											<th>同化数</th>
										</tr>
									</thead>
									<tbody>
										<#if newConditionList3?exists && newConditionList3?size gt 0>
										<#list newConditionList3 as item>
											<tr>
												<td>
													<label class="pos-rel">
														<input name="course-checkbox" type="checkbox" class="wp wp3" value="${item.subjectIdstr!}">
														<span class="lbl"></span>
													</label>
												</td>
												<td>${item_index+1}</td>
												<td>
				                                  <span>${item.subNames[0]!}</span>、
			                        	          <span>${item.subNames[1]!}</span>、
			                        	          <span>${item.subNames[2]!}</span>
			                              		</td>
												<td>${item.sumNum!}</td>
												<td>
				                              		<a id="${item.subjectIdstr!?replace(',','')}_a" class="table-btn show-details-btn data2" href="javascript:void(0);" onclick="toChosenList(3,'${item.subjectIdstr!}');"></a>
				                              	</td>
											</tr>
										</#list>
										</#if>
									</tbody>
								</table>
							</div>	
						</div>	
					</div>
				</div><!-- E 三科统计结果 -->
				
				<div class="col-md-6">
					<!-- S 两科统计结果 -->
					<div>
						<div class="table-container">
							<div class="table-container-header clearfix">
								<h4 class="pull-left">两科统计结果（共${newConditionList2?size}种）</h4>
								<button class="btn btn-blue pull-right" id="change2">转为三科</button>
							</div>	
							<div class="table-container-body">
								<table class="table table-striped table-hover no-margin">
									<thead>
										<tr>
											<th>
												<label class="pos-rel">
												<input name="course-checkbox" type="checkbox" class="wp wpall wpall2">
												<span class="lbl"></span>
												</label>
											</th>
											<th>序号</th>
											<th>学科</th>
											<th>选课人数</th>
											<th>同化数</th>
										</tr>
									</thead>
									<tbody>
										<#if newConditionList2?exists && newConditionList2?size gt 0>
										<#list newConditionList2 as item>
											<tr>
												<td>
													<label class="pos-rel">
														<input name="course-checkbox" type="checkbox" class="wp wp2" value="${item.subjectIdstr!}">
														<span class="lbl"></span>
													</label>
												</td>
												<td>${item_index+1}</td>
												<td>
				                                  <span>${item.subNames[0]!}</span>、
			                        	          <span>${item.subNames[1]!}</span>
			                              		</td>
												<td>${item.sumNum!}</td>
				                              	<td>
				                              		<a id="${item.subjectIdstr!?replace(',','')}_a" class="table-btn show-details-btn data3" href="javascript:void(0);" onclick="toChosenList(2,'${item.subjectIdstr!}');"></a>
				                              	</td>
											</tr>
										</#list>
										</#if>
									</tbody>
								</table>
							</div>
						</div>	
					</div><!-- E 两科统计结果 -->
				</div>
			</div>
	</div>
</div>
<script>
$(function(){
	showBreadBack(gobackChoice,false,"分析结果");
	
	$('.wp').on('click',function(){
		var ob = this;
		if($(ob).hasClass('wpall')){
			checkAll(ob);
		} else {
			var cls = '.wpall2';
			var chcls = '.wp2'
			if($(ob).hasClass('wp3')){
				cls = '.wpall3';
				chcls = '.wp3'
			}
			var ck = $(ob).prop('checked'); 
			var tl = $(chcls).length;
			var ctl = $(chcls + ':checked').length;
			if(ck && tl == ctl){
				$(cls).prop("checked", "true");
			} else {
				$(cls).removeAttr("checked");
			}
		}
	});
	
	$('#change3').on('click',function(){
		var cls = '.wp3';
		toOther(3, cls);
	});
	
	$('#change2').on('click',function(){
		var cls = '.wp2';
		toOther(2, cls);
	});
	
	$("#toCountDetail").on("click",function(){
		var url ='${request.contextPath}/newgkelective/'+choiceId+'/choiceCountDetail/list/page';
		$("#showList").load(url);
	})
})

// 2科
var subIds2 = '';
// 3科
var subIds3 = '';
var choiceId = '${choiceId!}';

var hasClick=false;
function toOther(type, cls){
	if(hasClick){
		return;
	}
	
	hasClick=true;
	var sidArr = $(cls+':checked');
	if(!sidArr || sidArr.length == 0){
		hasClick=false;
		layerTipMsg(true,'提示','请先勾选要同化的科目组合！');
		return;
	}
	if(type == 2 && sidArr.length == 1){
		hasClick=false;
		layerTipMsg(true,'提示','操作转三科，请至少勾选2个科目组合！');
		return;
	}
	var sids = '';
	for(var i=0;i<sidArr.length;i++){
		if(sids!=''){
			sids+=';';
		}
		sids+=$(sidArr[i]).val();
	}
	clearDataDisplay(type);
	if(type==2){
		subIds2 = sids;
	} else {
		subIds3 = sids;
	}
	$.ajax({
		url:'${request.contextPath}/newgkelective/${choiceId!}/choiceAnalysis/sync',
		data: {'arrayType':type,'subjectIdstr':sids},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		hasClick=false;
	 		dealSyncData(type, jsonO);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

// 清除目标同化列的显示数据
function clearDataDisplay(type){
	$('.data'+type).each(function(){
		$(this).text('');
	});
}

function dealSyncData(type, datas){
	if(!datas || datas.length == 0){
		return;
	}
	for(var j=0;j<datas.length;j++){
		var dob = datas[j];
		var aid = '#'+dob.subIds+'_a';
		aid = aid.replaceAll(',',''); 
		$(aid).text(dob.stuNum);
	}
}

function checkAll(obj){
	var cls = '.wp2';
	if($(obj).hasClass('wpall3')){
		cls = '.wp3';
	}
    if($(obj).prop('checked')){
        $(cls).prop("checked", "true");
    }else{
        $(cls).removeAttr("checked");
    }
}

function toChosenList(type,subjectIds){
	var choiceId=$("#choiceId").val();
	var sids = subIds3;
	if(type == 3){
		sids = subIds2;
	}
	var url ='${request.contextPath}/newgkelective/'+choiceId+'/choiceAnalysis/sync/list/page?subjectIdstr='+subjectIds+'&arrayType='+type+'&subArrayIds='+sids;
	$("#showList").load(url);
}

function gobackChoice(){
	var url =  '${request.contextPath}/newgkelective/${gradeId!}/goChoice/index/page';
	$("#showList").load(url);
}
</script>
