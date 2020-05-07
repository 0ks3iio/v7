<div class="tab-pane active">
<form id="parmForm">
<#assign i1=-1>
<#assign i2=-1>
<#assign i3=-1>
<#assign i4=-1>
<#assign i9=-1>
	<input type="hidden" name="id" value="${emStatParm.id!}"/>
	<input type="hidden" name="examId" value="${emStatParm.examId!}"/>
	<input type="hidden" name="subjectId" id="subjectId" value="${emStatParm.subjectId!}"/>
	<input type="hidden" name="maxScore" id="maxScore" value="${maxScore?default(100)}"/>
	<div class="form-horizontal" role="form">
		<div class="form-group">
			<label class="col-sm-2 control-title no-padding-right"><span class="form-title">考生来源范围</span></label>
		</div>
		<div class="form-group" style="display:none;">
			<label class="col-sm-2 control-label no-padding-right">学生来源：</label>
			<div class="col-sm-6">
				<p>
					<#assign sourseType=emStatParm.sourseType?default('')>
					<#if sourceMap?exists && (sourceMap?size gt 0)>
						<#assign ss=0>
						<#list sourceMap?keys as key>
						<#if ss==0>
							<label class="inline">
								<input name="sourseType" type="checkbox" <#if (sourseType?index_of(key) gte 0)>checked</#if> value="${key!}" class="wp"/>
								<span class="lbl">${sourceMap[key]!}</span>
						    </label>
					    <#else>
					    	<label class="inline">
								<input name="sourseType" type="checkbox" <#if (sourseType?index_of(key) gte 0)>checked</#if> value="${key!}" class="wp"/>
								<span class="lbl" style="margin-left:10px;">${sourceMap[key]!}</span>
					   		 </label>
					    </#if>
					    <#assign ss=ss+1>
					    </#list>
				    </#if>
				</p>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">包括学生：</label>
			<div class="col-sm-6">
				<p>
				<#if emStatParm.subjectId?default('')!='00000000000000000000000000000000' && emStatParm.subjectId?default('')!='99999999999999999999999999999999'>
					<label class="inline">
						<input name="isCheat" type="checkbox" <#if emStatParm.isCheat?default("")=="1">checked</#if> value="1" class="wp"/>
						<span class="lbl"> 作弊生</span>
					</label>
					<label class="inline">
						<input name="isMiss" type="checkbox" <#if emStatParm.isMiss?default("")=="1">checked</#if> value="1" class="wp"/>
						<span class="lbl"> 缺考生</span>
					</label>
					<label class="inline">
						<input name="isZero" type="checkbox" <#if emStatParm.isZero?default("")=="1">checked</#if> value="1" class="wp"/>
						<span class="lbl"> 零分考生</span>
					</label>
					<!--<label class="inline">
						<input name="isOnlystat" type="checkbox" <#if emStatParm.isOnlystat?default("")=="1">checked</#if> value="1" class="wp"/>
						<span class="lbl"> 不统分考生</span>
					</label>-->
					<#else>
						<label class="inline">
							<input name="isCheat" type="checkbox" <#if emStatParm.isCheat?default("")=="1">checked</#if> value="1" class="wp"/>
							<span class="lbl"> 单科存在作弊</span>
						</label>
						<label class="inline">
							<input name="isMiss" type="checkbox" <#if emStatParm.isMiss?default("")=="1">checked</#if> value="1" class="wp"/>
							<span class="lbl"> 单科存在缺考</span>
						</label>
						<label class="inline">
							<input name="isZero" type="checkbox" <#if emStatParm.isZero?default("")=="1">checked</#if> value="1" class="wp"/>
							<span class="lbl"> 单科存在零分</span>
						</label>
					</#if>
				</p>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-title no-padding-right"><span class="form-title">学科分数段设置</span></label>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">分数段类型：</label>
			<div class="col-sm-6">
				<p class="js-explain">
					<label class="inline">
						<input type="radio" name="statSpaceType" value="" class="wp" <#if emStatParm.statSpaceType?default('')=="">checked="checked"</#if>/>
						<span class="lbl">不使用分数段</span>
					</label>
					<label class="inline">
						<input type="radio" name="statSpaceType" value="1" class="wp" <#if emStatParm.statSpaceType?default('')=="1">checked="checked"</#if> />
						<span class="lbl"> 统一间距</span>
					</label>
					<label class="inline">
						<input type="radio" name="statSpaceType" value="2" class="wp" <#if emStatParm.statSpaceType?default('')=="2">checked="checked"</#if>/>
						<span class="lbl"> 非统一间距</span>
					</label>
				</p>
				<div class="promptContainer spaceType_1" style="padding-left: 15px;padding-right: 15px;<#if emStatParm.statSpaceType?default('')!="1">display:none;</#if>"  >
					<table class="table table-bordered table-striped table-hover no-margin" style="background: #fff;">
                        <thead>
							<tr>
							    <th>分数上限</th>
								<th>分数下限</th>
								<th>间距</th>
							</tr>
						</thead>
						<tbody>	
							<#if emStatParm.statSpaceType?default('')=="1">
								<tr class="emSpaceItem0">
									<td>
										<input type="text"  class="form-control upScore_class" name="upScore" id="upScore"  vtype = "number" nullable="false"  value="${emStatParm.upScore?default(maxScore)}" maxLength="6">
									</td>
									<td><input type="text"  class="form-control lowScore_class" name="lowScore" id="lowScore"  vtype = "number" nullable="false"  value="${emStatParm.lowScore?default(0)}" maxLength="6"></td>
									<td><input type="text"  class="form-control spaceScore_class" name="spaceScore" id="spaceScore"  vtype = "number" nullable="false"  value="${emStatParm.spaceScore?default(20)}" maxLength="6"></td>
								</tr>
							</#if>
						</tbody>
					</table>
				</div>
				<div class="promptContainer spaceType_2" style="padding-left: 15px;padding-right: 15px;<#if emStatParm.statSpaceType?default('')!="2">display:none;</#if>"  >
					<table class="table table-bordered table-striped table-hover no-margin" style="background: #fff;">
                        <thead>
							<tr>
							    <th>分数上限</th>
								<th>分数下限</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>	
							<#if emStatParm.emSpaceItemList1?exists && emStatParm.emSpaceItemList1?size gt 0>
								<#assign i1=emStatParm.emSpaceItemList1?size>
								<#list emStatParm.emSpaceItemList1 as item1>
								<tr class="emSpaceItem1">
									<td>
									<input type="text" class="form-control upScore1" name="emSpaceItemList1[${item1_index}].upScore"  vtype = "number" value="${item1.upScore!}" maxLength="6">
									</td>
									<td>
										<input type="text"  class="form-control lowScore1" name="emSpaceItemList1[${item1_index}].lowScore"  vtype = "number" value="${item1.lowScore!}" maxLength="6">
									</td>
									<td>
										<a class="color-blue js-deleRow1" href="javascript:;" onclick="deleRow(this)">删除</a>
									</td>	
								</tr>
								</#list>
							</#if>
							<tr>
							    <td class="text-center" colspan="3">
							    	<a class="color-blue js-addRow1" href="javascript:;" onclick="addRow1(this)">+新增</a>
							    </td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">名次段统计：</label>
			<div class="col-sm-6">
				<p class="js-explain9">
					<label class="inline">
						<input type="radio" name="needRankStat" value="0" class="wp" <#if emStatParm.needRankStat?default('0')=="0">checked="checked"</#if>/>
						<span class="lbl">不需要</span>
					</label>
					<label class="inline">
						<input type="radio" name="needRankStat" value="1" class="wp" <#if emStatParm.needRankStat?default('0')=="1">checked="checked"</#if> />
						<span class="lbl"> 需要</span>
					</label>
				</p>
				<div class="promptContainer spaceType_9" style="padding-left: 15px;padding-right: 15px;<#if emStatParm.needRankStat?default('')!="1">display:none;</#if>"  >
					<table class="table table-bordered table-striped table-hover no-margin" style="background: #fff;">
                        <thead>
							<tr>
							    <th>名次上限</th>
								<th>名次下限</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>	
							<#if emStatParm.emSpaceItemList9?exists && emStatParm.emSpaceItemList9?size gt 0>
								<#assign i9=emStatParm.emSpaceItemList9?size>
								<#list emStatParm.emSpaceItemList9 as item1>
								<tr class="emRankItem1">
									<td>
									<input type="text" class="form-control upScore9" id='emSpace${item1_index}upScore' name="emSpaceItemList9[${item1_index}].upScore"   value="${item1.upScore!}" maxLength="6" nullable="false" vtype="int" min="1" max="9999">
									</td>
									<td>
										<input type="text"  class="form-control lowScore9" id='emSpace${item1_index}lowScore' name="emSpaceItemList9[${item1_index}].lowScore"  value="${item1.lowScore!}" maxLength="6" nullable="false" vtype="int" min="1" max="9999">
									</td>
									<td>
										<a class="color-blue js-deleRow1" href="javascript:;" onclick="deleRow(this)">删除</a>
									</td>	
								</tr>
								</#list>
							</#if>
							<tr>
							    <td class="text-center" colspan="3">
							    	<a class="color-blue js-addRow1" href="javascript:;" onclick="addRow9(this)">+新增</a>
							    </td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">上线人数统计（按名次划分）：</label>
			<div class="col-sm-6">
			<form id="parmForm1">
				<p class="js-explain2">
					<label class="inline">
						<input type="radio" name="needLineStat" value="0" class="wp" <#if emStatParm.needLineStat?default('0')=="0">checked="checked"</#if>/>
						<span class="lbl">不需要</span>
					</label>
					<label class="inline">
						<input type="radio" name="needLineStat" value="1" class="wp" <#if emStatParm.needLineStat?default('0')=="1">checked="checked"</#if> />
						<span class="lbl"> 需要</span>
					</label>
				</p>
				<div class="promptContainer lineType_1" style="padding-left: 15px;padding-right: 15px;<#if emStatParm.needLineStat?default('0')!="1">display:none;</#if>"  >
					<table class="table table-bordered table-striped table-hover no-margin" style="background: #fff;">
                        <thead>
							<tr>
							    <th>名次线</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>	
							<#if lines?exists && lines?size gt 0>
							<#assign i4=lines?size>
								<#list lines as line>
								<tr class="emLineItem1">
									<td>
										前&nbsp<input type="text" class="form-control inline-block line" id="lines${line_index}" name="lines"  value="${line!}" style="width:100px"  nullable="false" vtype="int" min="1" max="9999">&nbsp名
									</td>
									<td>
										<a class="color-blue js-deleRow1" href="javascript:;" onclick="deleRow(this)">删除</a>
									</td>	
								</tr>
								</#list>
							</#if>
							<tr>
							    <td class="text-center" colspan="2">
							    	<a class="color-blue js-addRow1" href="javascript:;" onclick="addRow4(this)">+新增</a>
							    </td>
							</tr>
						</tbody>
					</table>
				</div>
					</form>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>
		<#if emStatParm.subjectId?default('')!='00000000000000000000000000000000' && emStatParm.subjectId?default('')!='99999999999999999999999999999999'>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">是否参与本次的总分统计：</label>
			<div class="col-sm-6">
				<p class="js-explain3">
					<label class="inline">
						<input type="radio" name="joinSum" value="0" class="wp" <#if emStatParm.joinSum?default('1')=="0">checked="checked"</#if>/>
						<span class="lbl">不需要</span>
					</label>
					<label class="inline">
						<input type="radio" name="joinSum" value="1" class="wp" <#if emStatParm.joinSum?default('1')=="1">checked="checked"</#if> />
						<span class="lbl"> 需要</span>
					</label>
				</p>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>
		</#if>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">优秀/不及格人数统计（按分数划分）：</label>
			<div class="col-sm-6">
				<p class="js-explain4">
					<label class="inline">
						<input type="radio" name="needGoodLine" value="0" class="wp" <#if emStatParm.needGoodLine?default('0')=="0">checked="checked"</#if>/>
						<span class="lbl">不需要</span>
					</label>
					<label class="inline">
						<input type="radio" name="needGoodLine" value="1" class="wp" <#if emStatParm.needGoodLine?default('0')=="1">checked="checked"</#if> />
						<span class="lbl"> 需要</span>
					</label>
				</p>
				<div class="promptContainer lineType_4" style="padding-left: 15px;padding-right: 15px;<#if emStatParm.needGoodLine?default('0')!="1">display:none;</#if>"  >
					<table class="table table-bordered table-striped table-hover no-margin" style="background: #fff;">
                        <thead>
							<tr>
							    <th>优秀分数线</th>
								<th>及格分数线</th>
							</tr>
						</thead>
						<tbody>	
							<tr class="">
								<td>
									<input type="text"  class="form-control" name="goodLine" id="goodLine"  vtype = "number" nullable="false"  value="${emStatParm.goodLine?default(maxScore)}" maxLength="6" min="1" max="9999">
								</td>
								<td><input type="text"  class="form-control" name="failedLine" id="failedLine"  vtype = "number" nullable="false"  value="${emStatParm.failedLine?default(0)}" maxLength="6" min="1" max="9999"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"></label>
			<div class="col-sm-6">
				<a class="btn btn-blue" id="btn-saveParm" onclick="save()">确定</a>
			</div>
			<div class="col-sm-4 control-tips"></div>
		</div>													
	</div>
</form>
</div>



<script type="text/javascript">
var i1=${i1};
var i2=${i2};
var i3=${i3};
var i4=${i4};
var i9=${i9};
$(function(){
	$('.js-explain label').on('click',function(){
		var statSpaceType=$("input[name='statSpaceType']:radio:checked").val();
		if(statSpaceType=="1"){
			$(".spaceType_2").hide();
			$(".spaceType_2").find('.emSpaceItem1').remove();
			i1=-1;
			$(".spaceType_1").show();
			addRow0();
		}else if(statSpaceType=="2"){
			$(".spaceType_2").show();
			$(".spaceType_1").find('.emSpaceItem0').remove();
			$(".spaceType_1").hide();
		}else{
			$(".spaceType_2").find('.emSpaceItem1').remove();
			i1=-1;
			$(".spaceType_1").find('.emSpaceItem0').remove();
			$(".spaceType_2").hide();
			$(".spaceType_1").hide();
		}
	});
	$('.js-explain9 label').on('click',function(){
		var needRankStat=$("input[name='needRankStat']:radio:checked").val();
		if(needRankStat=="1"){
			$(".spaceType_9").show();
		}else{
			$(".spaceType_9").find('.emRankItem1').remove();
			i9=-1;
			$(".spaceType_9").hide();
		}
	});
	$('.js-explain2 label').on('click',function(){
		var needLineStat=$("input[name='needLineStat']:radio:checked").val();
		if(needLineStat=="1"){
			$(".lineType_1").show();
		}else{
			$(".lineType_1").find('.emLineItem1').remove();
			i4=-1;
			$(".lineType_1").hide();
		}
	});
	
	$('.js-explain4 label').on('click',function(){
		var needGoodLine=$("input[name='needGoodLine']:radio:checked").val();
		if(needGoodLine=="1"){
			$(".lineType_4").show();
		}else{
			$("#failedLine").val('0');
			$("#goodLine").val('0');
			$(".lineType_4").hide();
		}
	});
})
	
	function addRow0(){
		$tbody=$(".spaceType_1").find("table").find("tbody");
		var maxScore=$("#maxScore").val();
		var $html='<tr class="emSpaceItem0"><td><input type="text"  class="table-input upScore_class" name="upScore" id="upScore"  vtype = "number" nullable="false"  value="'+maxScore+'" maxLength="6"></td>'
					+'<td><input type="text"  class="table-input lowScore_class" name="lowScore" id="lowScore"  vtype = "number" nullable="false"  value="0" maxLength="6"></td>'
					+'<td><input type="text"  class="table-input spaceScore_class" name="spaceScore" id="spaceScore"  vtype = "number" nullable="false"  value="20" maxLength="6"></td></tr>';
					
		$tbody.append($html);
	}
	
	
	
	function findByRankStatFront(){
		var rankStatFront=$("#rankStatFront").val();
		if(rankStatFront=="1"){
			$(".rankStatFront_class").show();
		}else{
			$(".rankStatFront_class").hide();
			$(".rankStatFront_class").find('.emSpaceItem2').remove();
			i2=-1;
		}
	}
	function findByRankStatBack(){
		var rankStatBack=$("#rankStatBack").val();
		if(rankStatBack=="1"){
			$(".rankStatBack_class").show();
		}else{
			$(".rankStatBack_class").hide();
			$(".rankStatBack_class").find('.emSpaceItem3').remove();
			i3=-1;
		}
	}
	
	
	function addRow1(obj){
		$tr=$(obj).parent().parent();
		if(i1<0){
			i1=0;
		}
		var $html='<tr class="emSpaceItem1"><td><input type="text" class="table-input upScore1"+ name="emSpaceItemList1['+i1+'].upScore"  vtype = "number"  maxLength="6"></td>'
					+'<td><input type="text"  class="table-input lowScore1" name="emSpaceItemList1['+i1+'].lowScore"  vtype = "number"  maxLength="6"></td>'
					+'<td><a class="color-blue js-deleRow1" href="javascript:;" onclick="deleRow(this)">删除</a></td></tr>';	
		$tr.before($html);
		i1++;
	}
	
	
	function addRow2(obj){
		$tr=$(obj).parent().parent();
		if(i2<0){
			i2=0;
		}
		var $html='<tr class="emSpaceItem2"><td class="text-center"><input type="text" class="table-input upScore2"+ name="emSpaceItemList2['+i2+'].upScore"  vtype = "number"  maxLength="6"></td>'
					+'<td class="text-center"><input type="text"  class="table-input lowScore2" name="emSpaceItemList2['+i2+'].lowScore"  vtype = "number"  maxLength="6"></td>'
					+'<td class="text-center"><a class="table-btn color-red js-deleRow2" href="javascript:;" onclick="deleRow(this)">删除</a></td></tr>';	
		$tr.before($html);
		i2++;
	}
	
	
	function addRow3(obj){
		$tr=$(obj).parent().parent();
		if(i3<0){
			i3=0;
		}
		var $html='<tr class="emSpaceItem3"><td class="text-center"><input type="text" class="table-input upScore3"+ name="emSpaceItemList3['+i3+'].upScore"  vtype = "number"  maxLength="6"></td>'
					+'<td class="text-center"><input type="text"  class="table-input lowScore3" name="emSpaceItemList3['+i3+'].lowScore"  vtype = "number"  maxLength="6"></td>'
					+'<td class="text-center"><a class="table-btn color-red js-deleRow1" href="javascript:;" onclick="deleRow(this)">删除</a></td></tr>';	
		$tr.before($html);
		i3++;
	}
	function addRow9(obj){
		$tr=$(obj).parent().parent();
		if(i9<0){
			i9=0;
		}
		var $html='<tr class="emRankItem1"><td class="text-center"><input type="text" class="table-input upScore9" id="emSpace'+i9+'upScore" name="emSpaceItemList9['+i9+'].upScore" nullable="false" vtype="int" min="1" max="9999"></td>'
					+'<td class="text-center"><input type="text"  class="table-input lowScore9" id="emSpace'+i9+'lowScore" name="emSpaceItemList9['+i9+'].lowScore"  nullable="false" vtype="int" min="1" max="9999"></td>'
					+'<td class="text-center"><a class="table-btn color-red js-deleRow1" href="javascript:;" onclick="deleRow(this)">删除</a></td></tr>';	
		$tr.before($html);
		i9++;
	}
	
	function addRow4(obj){
		$tr=$(obj).parent().parent();
		if(i4<0){
			i4=0;
		}
		var $html = '<tr class="emLineItem1"><td>前&nbsp<input type="text" class="form-control inline-block line" id="lines'+i4+'" name="lines" style="width:100px"  nullable="false" vtype="int" min="1" max="9999">&nbsp名</td>'
					+'<td><a class="color-blue js-deleRow1" href="javascript:;" onclick="deleRow(this)">删除</a></td></tr>';
		$tr.before($html);
		i4++
	}
	
	function deleRow(obj){
		$tr=$(obj).parent().parent();
		$tr.remove();
	}
	function checkParmNeedRankStat(){
		var needRankStat=$("input[name='needRankStat']:radio:checked").val();
		if(needRankStat=="1"){
			var f=false;
			if(!$(".spaceType_9").find('.emRankItem1') || $(".spaceType_9").find('.emRankItem1').length==0){
				layer.tips('至少维护一个名次段!',$(".spaceType_9"), {
						tipsMore: true,
						tips: 3
					});
				return true;
			}
			var check = checkValue('#parmForm');
			if(!check){
				return true;
			}
			var upArr={};
			var lowArr={};
			var ii=0;
			$(".spaceType_9").find('.emRankItem1').each(function(){
				var up=$(this).find(".upScore9").val();
				var low=$(this).find(".lowScore9").val();
				<#--var up=$(this).find(".upScore9").val();
				if(checkParmToInt(up,$(this).find(".upScore9"))){
					f=true;
					return;
				}
				var low=$(this).find(".lowScore9").val();
				if(checkParmToInt(low,$(this).find(".lowScore9"))){
					f=true;
					return;
				}-->
				var u=parseInt(up);
				var l=parseInt(low);
				if(u>=l){
					layer.tips('名次上限不能大于或等于下限!',$(this).find(".lowScore9"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				if(ii=0){
					upArr[ii]=u;
					lowArr[ii]=l;
				}else{
					<#---是否有交集  从大到小---->
					if(lowArr[ii-1]<u){
						layer.tips('前下限不能小于后上限!',$(this).find(".upScore9"), {
							tipsMore: true,
							tips: 3
						});
						f=true;
						return;
					}
				}
				ii++;
			});
			if(f){
				return true;
			}
		}
		return false;
	}
	function checkParmToSpaceType(){
		var statSpaceType=$("input[name='statSpaceType']:radio:checked").val();
		if(statSpaceType=="1"){
			var upScore=$("#upScore").val();
			if(checkParmToNum(upScore,$("#upScore"))){
				return true;
			}
			var lowScore=$("#lowScore").val();
			if(checkParmToNum(lowScore,$("#lowScore"))){
				return true;
			}
			var u=parseFloat(upScore);
			var l=parseFloat(lowScore);
			if(u<=l){
				layer.tips('下限不能大于或等于上限!',$("#lowScore"), {
					tipsMore: true,
					tips: 3
				});
			}
			
			var spaceScore=$("#spaceScore").val();
			if(checkParmToNum(spaceScore,$("#spaceScore"))){
				return true;
			}
			var space=parseFloat(spaceScore);
			var c=u-l;
			if(c<space){
				layer.tips('不能大于上下限之差!',$("#spaceScore"), {
						tipsMore: true,
						tips: 3
					});
				return true;
			}
			var m=c/space;
			var m1=parseInt(m);
			if(!space*m1==c){
				layer.tips('间距不能整除上下限之差!',$("#spaceScore"), {
						tipsMore: true,
						tips: 3
					});
				return true;
			}
		}else if(statSpaceType=="2"){
			var f=false;
			if(!$(".spaceType_2").find('.emSpaceItem1') || $(".spaceType_2").find('.emSpaceItem1').length==0){
				layer.tips('至少维护一个分数段!',$(".spaceType_2"), {
						tipsMore: true,
						tips: 3
					});
			}
			<#----不能有交集----->
			var upArr={};
			var lowArr={};
			var ii=0;
			$(".spaceType_2").find('.emSpaceItem1').each(function(){
				var up=$(this).find(".upScore1").val();
				if(checkParmToNum(up,$(this).find(".upScore1"))){
					f=true;
					return;
				}
				var low=$(this).find(".lowScore1").val();
				if(checkParmToNum(low,$(this).find(".lowScore1"))){
					f=true;
					return;
				}
				var u=parseFloat(up);
				var l=parseFloat(low);
				if(u<=l){
					layer.tips('分数下限不能大于或等于上限!',$(this).find(".lowScore1"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				if(ii=0){
					upArr[ii]=u;
					lowArr[ii]=l;
				}else{
					<#---是否有交集  从大到小---->
					if(lowArr[ii-1]<u){
						layer.tips('前下限不能小于后上限!',$(this).find(".upScore1"), {
							tipsMore: true,
							tips: 3
						});
						f=true;
						return;
					}
				}
				ii++;
			});
			if(f){
				return true;
			}
		}else{
			
		}
		return false;
	}
	function checkParmToRankStatFront(){
		var rankStatFront=$("#rankStatFront").val();
		if(rankStatFront=="1"){
			if(!$(".rankStatFront_class").find('.emSpaceItem2') || $(".rankStatFront_class").find('.emSpaceItem2').length==0){
				layer.tips('至少维护一个分数段!',$(".rankStatFront_class"), {
						tipsMore: true,
						tips: 3
					});
			}
			var ii=0;
			var f=false;
			var upArr={};
			var lowArr={};
			$(".rankStatFront_class").find('.emSpaceItem2').each(function(){
				var up=$(this).find(".upScore2").val();
				if(checkParmToNum(up,$(this).find(".upScore2"))){
					f=true;
					return;
				}
				var low=$(this).find(".lowScore2").val();
				if(checkParmToNum(low,$(this).find(".lowScore2"))){
					f=true;
					return;
				}
				var u=parseFloat(up);
				if(u>100 || u<0){
					layer.tips('百分比范围0-100!',$(this).find(".upScore2"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				var l=parseFloat(low);
				if(l>100 || l<0){
					layer.tips('百分比范围0-100!',$(this).find(".lowScore2"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				if(u<=l){
					layer.tips('下限不能大于或等于上限!',$(this).find(".lowScore2"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				if(ii=0){
					upArr[ii]=u;
					lowArr[ii]=l;
				}else{
					<#---不能相同---->
					for(var b=0;b<ii;b++){
						if(upArr[bb]==u && lowArr[bb]==l){
							layer.tips('分数段不能重复!',$(this).find(".upScore2"), {
								tipsMore: true,
								tips: 3
							});
							f=true;
							break;
						}
					}
					if(f){
						return;
					}
					upArr[ii]=u;
					lowArr[ii]=l;
					
				}
				ii++;
			});
		}
		return false;
	}
	
	function checkParmToRankStatBack(){
		var rankStatBack=$("#rankStatBack").val();
		if(rankStatBack=="1"){
			if(!$(".rankStatBack_class").find('.emSpaceItem3') || $(".rankStatBack_class").find('.emSpaceItem3').length==0){
				layer.tips('至少维护一个分数段!',$(".rankStatBack_class"), {
						tipsMore: true,
						tips: 3
					});
				return true;
			}
			var ii=0;
			var f=false;
			var upArr={};
			var lowArr={};
			$(".rankStatBack_class").find('.emSpaceItem3').each(function(){
				var up=$(this).find(".upScore3").val();
				if(checkParmToNum(up,$(this).find(".upScore3"))){
					f=true;
					return;
				}
				var low=$(this).find(".lowScore3").val();
				if(checkParmToNum(low,$(this).find(".lowScore3"))){
					f=true;
					return;
				}
				var u=parseFloat(up);
				if(u>100 || u<0){
					layer.tips('百分比范围0-100!',$(this).find(".upScore3"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				var l=parseFloat(low);
				if(l>100 || l<0){
					layer.tips('百分比范围0-100!',$(this).find(".lowScore3"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				if(u<=l){
					layer.tips('下限不能大于或等于上限!',$(this).find(".lowScore3"), {
						tipsMore: true,
						tips: 3
					});
					f=true;
					return;
				}
				
				if(ii=0){
					upArr[ii]=u;
					lowArr[ii]=l;
				}else{
					<#---不能相同---->
					for(var b=0;b<ii;b++){
						if(upArr[bb]==u && lowArr[bb]==l){
							layer.tips('分数段不能重复!',$(this).find(".upScore3"), {
								tipsMore: true,
								tips: 3
							});
							f=true;
							break;
						}
					}
					if(f){
						return;
					}
					upArr[ii]=u;
					lowArr[ii]=l;
				}
				ii++;
			});
			if(f){
				return true;
			}
		}
		return false;
	}
	
	function checkParmForm(){
		<#if emStatParm.subjectId?default('')!='00000000000000000000000000000000' && emStatParm.subjectId?default('')!='99999999999999999999999999999999'>
			var sourseTypes=$("input[name='sourseType']:checked");
			if(sourseTypes.length==0){
				layerTipMsg(false,"失败","学生来源至少选择一个！");
				return true;
			}
		</#if>
		if(checkParmToSpaceType()){
			return true;
		}
		if(checkParmToRankStatFront()){
			return true;
		}
		if(checkParmToRankStatBack()){
			return true;
		}
		
		return false;
	}
	function checkParmForm1(){
		<#if emStatParm.subjectId?default('')!='00000000000000000000000000000000' && emStatParm.subjectId?default('')!='99999999999999999999999999999999'>
			var sourseTypes=$("input[name='sourseType']:checked");
			if(sourseTypes.length==0){
				layerTipMsg(false,"失败","学生来源至少选择一个！");
				return true;
			}
		</#if>
		if(checkParmToSpaceType()){
			return true;
		}
		if(checkParmNeedRankStat()){
			return true;
		}
		return false;
	}
	
	
	function checkParmToNum(data1,obj){
		data1=jQuery.trim(data1);
		if(data1==""){
			layer.tips('不能为空!',obj, {
					tipsMore: true,
					tips: 3
				});
			return true;
		}
		if(!checkParseFloat(data1, 1)){
			layer.tips('只能输入超过1位小数的数值!',obj, {
					tipsMore: true,
					tips: 3
				});
			return true;
		}
		return false;
	}
	var isSaveSubmit=false;
	
	function save(){
		if(isSaveSubmit){
			return;
		}
		
		isSaveSubmit = true;
		$("#btn-saveParm").addClass("disabled");
		if(checkParmForm1()){
			isSaveSubmit = false;
			$("#btn-saveParm").removeClass("disabled");
			return;
		}
		var needLineStat=$("input[name='needLineStat']:radio:checked").val();
		if(needLineStat=="1"){
			var check = checkValue('#parmForm');
			if(!check){
				isSaveSubmit = false;
				$("#btn-saveParm").removeClass("disabled");
				return;
			}
			if(!$(".lineType_1").find('.emLineItem1') || $(".lineType_1").find('.emLineItem1').length==0){
				layer.tips('至少维护一个上线人数!',$(".lineType_1"), {
					tipsMore: true,
					tips: 3
				});
				isSaveSubmit = false;
				$("#btn-saveParm").removeClass("disabled");
				return;
			}
		}
		var subjectId=$("#subjectId").val();
		var options = {
			url : "${request.contextPath}/exammanage/scoreStat/saveStatParm",
			data : {},
			dataType : 'json',
			success : function(data){
	 			if(!data.success){
	 				layerTipMsg(data.success,"失败",data.msg);
	 			}else{
	 				layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
					setSubjectParm(subjectId);
	 			}
				isSaveSubmit = false;
				$("#btn-saveParm").removeClass("disabled");
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){} 
		};
		$("#parmForm").ajaxSubmit(options);
		
	}
	

</script>