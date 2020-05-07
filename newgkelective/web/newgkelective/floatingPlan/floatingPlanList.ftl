<#if planType! == "A">
	<#assign batchstr = "选考">
	<#assign timeNum = 3>
<#else>
	<#assign batchstr = "学考">
	<#assign timeNum = allSubIds?size-3>
</#if>

<#if fix3List?? && fix3List?size gt 0>
<h4 class="form-title">
	<b class="mr20">3科组合走班安排</b>
</h4>
<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th rowspan="2">序号</th>
			<th rowspan="2">行政班</th>
			<th rowspan="2">所属组合班</th>
			<th rowspan="2">人数</th>
			<th colspan="2" class="text-center">选课组合</th>
			<th rowspan="2">跟随行政班上课科目</th>
		</tr>
		<tr>
			<th>组合</th>
			<th>人数</th>
		</tr>
	</thead>
	<tbody>
	<#list fix3List as cls>
		<tr>
			<td>${cls_index+1}</td>
			<td>${xzbNameMap[cls.id]!}</td>
			<td>${cls.className!}<input type="hidden" value="${cls.id!}" class="zhbId"></td>
			<td>${cls.studentCount!}</td>
			<#assign dto = cls.newDtoList[0]>
			<td>${dto.subShortNames!}</td>
			<td>${dto.sumNum!}</td>
			<td>${cls.subNames!}</td>
		</tr>
	</#list>
	</tbody>
</table>
</#if>

<#if fix2List?? && fix2List?size gt 0>
<h4 class="form-title">
	<b><#if planType! == "A">定2</#if>走1组合走班安排</b>
	<span class="font-14 color-yellow ml10"><i class="fa fa-exclamation-circle"></i> ${batchstr!}1、${batchstr!}2、${batchstr!}3均表示一个学生的不同<#if planType! == "A">选考<#else>学考</#if>科目上课的时间点。</span>
</h4>
<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th rowspan="2">序号</th>
			<th rowspan="2">行政班</th>
			<th rowspan="2">所属组合班</th>
			<th rowspan="2">人数</th>
			<th colspan="2" class="text-center">选课组合</th>
			<th rowspan="2">跟随行政班上课科目</th>
			<th rowspan="2">走班时间点</th>
			<th rowspan="2">走班科目</th>
		</tr>
		<tr>
			<th>组合</th>
			<th>人数</th>
		</tr>
	</thead>
	<tbody>
	<#list fix2List as cls>
		<#assign dtoList = cls.newDtoList!>
		<#if dtoList?? && dtoList?size gt 0>
		<#list dtoList as dto>
		
		<tr>
			<#if dto_index == 0>
			<td rowspan="${dtoList?size}">${cls_index+1}</td>
			<td rowspan="${dtoList?size}">${xzbNameMap[cls.id]!}</td>
			<td rowspan="${dtoList?size}">${cls.className!}
				<input type="hidden" value="${cls.id!}" class="zhbId">
			</td>
			<td rowspan="${dtoList?size}">${cls.studentCount!}</td>
			</#if>
			
			<td>${dto.subShortNames!}<input class="subGroup" value="${dto.subjectIdstr!}" type="hidden"/></td>
			<td>${dto.sumNum!}</td>
			<#if dto_index == 0>
			<td rowspan="${dtoList?size}">${cls.subNames!}</td>
			<td rowspan="${dtoList?size}">
				<select class="form-control" name="" id="" onchange="setClassTime(this);">
				<#list 1..timeNum as num>
	                <option value="${num}" <#if num?string == cls.batchs[0]>selected</#if>>${batchstr}${num}</option>
				</#list>
	            </select>
			</td>
			</#if>
			<td>${subNameMap[floatingGroupSubIdMap[cls.id+dto.subjectIdstr+cls.batchs[0]]]!}(${dto.sumNum!})</td>
		</tr>
		
		</#list>
		</#if>
	</#list>
	</tbody>
</table>
</#if>

<#if fix1List?? && fix1List?size gt 0>
<h4 class="form-title">
	<b>定1走2组合走班安排</b>
	<span class="font-14 color-yellow ml10"><i class="fa fa-exclamation-circle"></i> ${batchstr!}1、${batchstr!}2、${batchstr!}3均表示一个学生的不同<#if planType! == "A">选考<#else>学考</#if>科目上课的时间点。</span>
</h4>
<#list fix1List as cls>
<table class="table table-striped table-bordered table-hover class-group-table table-editable">
	<thead>
		<tr>
			<th rowspan="2">序号</th>
			<th rowspan="2">行政班</th>
			<th rowspan="2">所属组合班</th>
			<th rowspan="2">人数</th>
			<th colspan="2" class="text-center">选课组合</th>
			<th rowspan="2">跟随行政班上课科目</th>
			<#list 1..timeNum as num>
				<#assign bi = -1>
				<#list cls.batchs as batch>
					<#if batch == num?string><#assign bi = batch_index></#if>
				</#list>
				<th rowspan="2" width="100" <#if bi lt 0>class="disabled"</#if>>
					${batchstr}${num} 
					<#if bi gt -1>
					<span class="label label-warning">${(subNameMap[cls.floatingSubIds[bi]])!}</span>
					</#if>
				</th>
			</#list>
			<#-- 
			<th rowspan="2" width="100">${batchstr}${cls.batchs[0]!} <span class="label label-warning">${(subNameMap[cls.floatingSubIds[0]])!}</span></th>
			<th rowspan="2" width="100">${batchstr}${cls.batchs[1]!} <span class="label label-warning">${(subNameMap[cls.floatingSubIds[1]])!}</span></th>
			-->
			<th rowspan="2">操作</th>
		</tr>
		<tr>
			<th>组合</th>
			<th>人数</th>
		</tr>
	</thead>
	<tbody>
		<#assign dtoList = cls.newDtoList!>
		<#if dtoList?? && dtoList?size gt 0>
		<#list dtoList as dto>
		<tr>
			<#if dto_index == 0>
			<td rowspan="${dtoList?size}">${cls_index+1}</td>
			<td rowspan="${dtoList?size}">${xzbNameMap[cls.id]!}</td>
			<td rowspan="${dtoList?size}">${cls.className!}
				<input type="hidden" value="${cls.id!}" class="zhbId">
			</td>
			<td rowspan="${dtoList?size}">${cls.studentCount!}</td>
			</#if>
			
			<td>${dto.subShortNames!}<input class="subGroup" value="${dto.subjectIdstr!}" type="hidden"/></td>
			<td>${dto.sumNum!}</td>
			
			<#if dto_index == 0>
			<td rowspan="${dtoList?size!}">${cls.subNames!}</td>
			</#if>
			
			<#list 1..timeNum as num>
				<#assign bi = -1>
				<#list cls.batchs as batch>
					<#if batch == num?string><#assign bi = batch_index></#if>
				</#list>
				<td class="choose <#if bi lt 0>disabled</#if>" data-batch="${num!}">
				<#if bi gt -1>
					<a class="item <#if dto.beXzbSub>form-disable-x</#if>" href="javascript:void(0);">
						<span class="huan">换</span>
						<span class="num pink"></span>
						<input type="hidden" class="subjectId" name="subjectId2" value="${floatingGroupSubIdMap[cls.id+dto.subjectIdstr+cls.batchs[bi]]!}">
						${(subNameMap[floatingGroupSubIdMap[cls.id+dto.subjectIdstr+cls.batchs[bi]]!])!}
					</a>
				</#if>
				</td>
			</#list>
			
			<#if dto_index == 0>
			<td rowspan="${dtoList?size!}">
				<a class="table-btn color-blue js-move-course" data-batchs="${cls.batchs[0]+","+cls.batchs[1]!}" 
					data-oldChose="${cls.floatingSubIds[0]?default("")+","+cls.floatingSubIds[1]?default("")}" href="javascript:void(0)">设置教学点</a>
			</td>
			</#if>
		</tr>
		</#list>
		</#if>
	</tbody>
</table>
</#list>
</#if>

<#if mixedList?? && mixedList?size gt 0>
<h4 class="form-title">
	<b><#if planType! == "A">混合<#else>其他</#if>组合走班安排</b>
	<span class="font-14 color-yellow ml10"><i class="fa fa-exclamation-circle"></i> ${batchstr!}1、${batchstr!}2、${batchstr!}3均表示一个学生的不同<#if planType! == "A">选考<#else>学考</#if>科目上课的时间点。</span>
</h4>
<#list mixedList as cls>
<table class="table table-striped table-bordered table-hover no-margin class-group-table table-editable">
	<thead>
		<tr>
			<th rowspan="2">序号</th>
			<th rowspan="2">行政班</th>
			<th rowspan="2">所属组合班</th>
			<th rowspan="2">人数</th>
			<th colspan="2" class="text-center">选课组合</th>
			<#if planType! == "B"><th rowspan="2">跟随行政班上课科目</th></#if>
			
			<#list 1..timeNum as num>
				<#assign bi = -1>
				<#list cls.batchs as batch>
					<#if batch == num?string><#assign bi = batch_index></#if>
				</#list>
				<th rowspan="2" width="100" class="chose <#if bi lt 0>disabled</#if>">
					${batchstr}${num} 
					<#if (bi gt -1) && cls.floatingSubIds[bi]?exists>
						<span class="label label-warning">${subNameMap[cls.floatingSubIds[bi]]!}</span>
					</#if>
				</th>
			</#list>
			
			<th rowspan="2" class="last">操作</th>
		</tr>
		<tr>
			<th>组合</th>
			<th>人数</th>
		</tr>
	</thead>
	<tbody>
	<#assign dtoList = cls.newDtoList!>
	<#if dtoList?? && dtoList?size gt 0>
	<#list dtoList as dto>
		<tr>
			<#if dto_index == 0>
			<td rowspan="${dtoList?size}">${cls_index+1}</td>
			<td rowspan="${dtoList?size}">${xzbNameMap[cls.id]!}</td>
			<td rowspan="${dtoList?size}">${cls.className!}<input type="hidden" value="${cls.id!}" class="zhbId"></td>
			<td rowspan="${dtoList?size}">${cls.studentCount!}</td>
			</#if>
			
			<td>${dto.subShortNames!} <input class="subGroup" value="${dto.subjectIdstr!}" type="hidden"/></td>
			<td>${dto.sumNum!}</td>
			
			<#if dto_index == 0 && planType! == "B">
			<td rowspan="${dtoList?size!}">${cls.subNames!}</td>
			</#if>
			
			<#list 1..timeNum as num>
				<#assign bi = -1>
				<#list cls.batchs as batch>
					<#if batch == num?string><#assign bi = batch_index></#if>
				</#list>
				<td class="choose <#if bi lt 0>disabled</#if>" data-batch="${num!}">
				<#if (bi gt -1)>
					<#assign bth = cls.batchs[bi]>
					<a class="item <#if dto.beXzbSub>form-disable-x</#if>" href="javascript:void(0);">
						<span class="huan">换</span>
						<span class="num"></span>
						<input type="hidden" class="subjectId" name="subjectId1"  value="${floatingGroupSubIdMap[cls.id+dto.subjectIdstr+bth]!}">
						${(subNameMap[floatingGroupSubIdMap[cls.id+dto.subjectIdstr+bth]])!}
					</a>
				</#if>
				</td>
			</#list>
			
			<#if dto_index == 0>
			<td rowspan="${dtoList?size!}">
				<a class="table-btn color-blue js-move-course" data-batchs="${cls.batchs?join(',')}" 
					data-oldChose="${cls.floatingSubIds?join(',')}" href="javascript:void(0)">设置教学点</a>
			</td>
			</#if>
		</tr>
	</#list>
	</#if>
	</tbody>
</table>
</#list>
</#if>


<div class="layer layer-fix-course">
	<div class="layer-content">
		<div class="explain explain-no-icon">
			本行政班所有学生都上该科目，才可设为行政班科目。
		</div>
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right">行政班：</label>
				<div class="col-sm-8 mt7">01班</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right">所属组合班：</label>
				<div class="col-sm-8 mt7">混合1班</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right">人数：</label>
				<div class="col-sm-8 mt7">50</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right">设为行政班科目：</label>
				<div class="col-sm-8">
					<label><input type="checkbox" class="wp" checked=""><span class="lbl"> 物理(50)</span></label>
					<label><input type="checkbox" class="wp"><span class="lbl"> 化学(50)</span></label>
					<label><input type="checkbox" class="wp"><span class="lbl"> 生物(50)</span></label>
					<label><input type="checkbox" class="wp"><span class="lbl"> 历史(50)</span></label>
					<label><input type="checkbox" class="wp"><span class="lbl"> 地理(50)</span></label>
					<label><input type="checkbox" class="wp"><span class="lbl"> 政治(50)</span></label>
				</div>
			</div>
		</div>
	</div>
</div>

<#-- 设置教学点 -->
<div class="layer layer-move-course">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">上课时间点：</label>
				<div class="col-sm-10">
					<div class="publish-course" data-num="">
					<#list 1..timeNum as num>
						<span data-batch="${num}">${batchstr}${num}</span>
					</#list>
					</div>
				</div>
			</div>
		</div>
		<p><b>走班科目安排</b></p>
		<div class="form-horizontal">
		<#list 1..timeNum as num>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">${batchstr}${num}：</label>
				<div class="col-sm-4">
					<select name="batch-${num}" class="form-control selSub" data-batch="${num}">
						<option value="">无</option>
					</select>
				</div>
				<#-- 
				<div class="col-sm-2">
					<label class="mt7"><input type="checkbox" class="wp" checked=""><span class="lbl"> 锁定科目</span></label>
				</div>
				 -->
			</div>
		</#list>
		</div>
		<p class="mytip"><b>参考值</b></p>
		<table class="table table-striped table-bordered table-hover no-margin">
			<thead>
				<tr>
					<th>选考科目</th>
					<th>物理</th>
					<th>化学</th>
					<th>生物</th>
					<th>历史</th>
					<th>地理</th>
					<th>政治</th>
					<th>技术</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>人数</td>
					<td>10</td>
					<td>10</td>
					<td>10</td>
					<td>10</td>
					<td>10</td>
					<td>10</td>
					<td>10</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<!-- page specific plugin scripts 
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>--> 

<style>
#aa .form-disabled{
	cursor: not-allowed;
    background-color: #eee;
    opacity: 1;
}
</style>

<script>
var classChoseMap = {
<#if classChoseMap?? && classChoseMap?size gt 0>
<#list classChoseMap?keys as cc>
<#if classChoseMap[cc]?? && classChoseMap[cc]?size gt 0>
<#if cc_index != 0>,</#if>
"${cc!}":[<#list classChoseMap[cc]! as sub>
	<#if sub_index != 0>,</#if>["${sub[0]!}",${sub[1]!},"${sub[2]!}"]
</#list>]
</#if>
</#list>
</#if>
};
var freeSubIds = [
<#if freeSubIds?? && freeSubIds?size gt 0>
<#list freeSubIds as subId>
<#if subId_index != 0>,</#if>"${subId!}"</#list>
</#if>
];


var isMove=false;
var isSubmit=false;
$(function(){
	(function(){
		// 颜色显示
		var colors = ['purple','pink','orange','lightblue','yellow','blue','green'];
		var subIds = [<#list allSubIds as subId><#if subId_index != 0>,</#if>'${subId}'</#list>];
		
		for(x in subIds){
			var subId = subIds[x];
			if(x >= colors.length){
				x = x - colors.length;
			}
			var color = colors[x];
			$(".subjectId[value='"+subId+"']").parents(".item").find("span.num").addClass(color);
		}
		
		<#if planType! == "B">
		// 宽度控制
		$(".class-group-table").each(function(i,obj){
			
			var num = $(obj).find(".chose").length;
			var w = $(obj).width()*0.36- 102* num;
			$(obj).find("thead .last").width(w);
		});
		</#if>
		
		if(!${canEdit!}){
			$("td.choose a.item,#aa tbody select").addClass("form-disabled").attr("disabled","disabled");
			$(".publish-course span").addClass("disabled");
		}
	})();
	
	// 设置行政班科目
	$('.js-fix-course').on('click', function(){
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '设置行政班科目',
			area: ['500px'],
			btn: ['确定', '取消'],
			content: $('.layer-fix-course')
		});
	});
	
	// 设置走班科目
	$('.js-move-course').on('click', function(){
		//debugger;
		var batchs = $(this).attr("data-batchs");
		var oldChose = $(this).attr("data-oldChose");
		var clsId = $(this).parents("tr").find(".zhbId").val();
		
		var choseArr = classChoseMap[clsId];
		makeLayerMoveCourse(batchs,oldChose,clsId);
	
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '设置走班科目',
			area: ['700px'],
			btn: ['确定', '取消'],
			content: $('.layer-move-course'),
			btn1:function(index){ // 确定
				
				if(isMove){
					return;
				}
				isMove=true;
				if(!${canEdit!}){
					isMove=false;
					return;
				}
				
				var need_length = $(".layer-move-course .publish-course").attr("data-num");
				var batchs = [];
				var active_length = $(".layer-move-course .publish-course .active").each(function(i,obj){
					batchs.push($(obj).attr("data-batch"));
				});
				if(batchs.length < need_length){
					layer.msg('至少选择个'+need_length+'时间点', {
						icon: 2,
						time: 1500,
						shade: 0.2
					});
					isMove=false;
					return;
				}
				var batchstr = batchs.join();
				
				var subIds = [];
				var f = false;
				var param = "";
				$(".layer-move-course .selSub:not([disabled])").each(function(i,obj){
					var v = $(obj).val();
					if(!v) v = '';
					if(subIds.indexOf(v) == -1){
						subIds.push(v);
					}else if(v){
						f = true;
					}
					param += "@"+$(obj).attr("name")+"="+v;
				});
				//检验 科目重复
				if(f){
					layer.msg('不能出现相同走班科目', {
						icon: 2,
						time: 1500,
						shade: 0.2
					});
					isMove=false;
					return;
				}
				
				if(isSubmit){
					return;
				}
				isSubmit = true;
				
				param = param.substr(1);
				
				$.ajax({
				    url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/setMoveCourse?planType=${planType!}',
		            data:{'batchSub':param,'zhbId':clsId,'batchParam':batchstr},
		            type:"post",
		            dataType:'json',
		            success:function(jsonO){
		            	isMove=false;
		                if (jsonO.success) {
		                	if(showRight) moveStuCount();
		                    layer.msg(jsonO.msg,{offset:'t',time:2000});
		                    floatingPlan();
							// 关闭
							layer.close(index);
		                }else{
		                	layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		                }
		                isSubmit = false;
		            }
				});
			},
			btn2:function(){
				// 取消
				isMove=false;
			}
		});
	});
	
	// 交换选课
	$(".class-group-table").on('click','.item:not(.form-disabled)',function(){
		if(!${canEdit!}){
			return;
		}
	
		var active = $(".class-group-table").find(".active")
		var len = active.length
		if (len === 0) {
			$(this).addClass("active")	
			$(this).parent().siblings(".choose").children().addClass("exchange")
		}else if (len === 1) {
			if ($(this).hasClass("active")) {  // 取消选择
				$(this).removeClass("active")
				$(this).parent().siblings(".choose").children().removeClass("exchange")
			} else {
				if ($(this).hasClass("exchange")) {
					var clsId = $(this).parents("tbody").find(".zhbId").val();
					
					swapGroupCourse(active,this,clsId);
				} else {  // 选中了 其他行的 科目
					$(".class-group-table").find(".active").removeClass("active")	
					$(".class-group-table").find(".exchange").removeClass("exchange")	
					$(this).addClass("active")	
					$(this).parent().siblings(".choose").children().addClass("exchange")
				}
			}
		}
	});
	$('.publish-course span').on('click', function(e){
		e.preventDefault();
		if($(this).hasClass('disabled')) return;

		var batch = $(this).attr("data-batch");
		if($(this).hasClass('active')){
			$(this).removeClass('active');
			$(".layer-move-course .selSub[data-batch='"+batch+"']").attr("disabled","disabled");
			$(".layer-move-course .selSub[data-batch='"+batch+"'] option").removeAttr("selected");
		}else{
			$(this).addClass('active');
			$(".layer-move-course .selSub[data-batch='"+batch+"']").removeAttr("disabled");
		}
		var num = $(".publish-course").attr("data-num");
		if($(".publish-course .active").length >= num){
			$(".publish-course span:not(.active)").addClass("disabled");
		}else{
			$(".publish-course span").removeClass("disabled");
		}
	});
	// 禁止用键盘改变值
	$("#aa table select").keydown(function(e){
		console.log(e.keyCode);
		if([33,34,35,36,38,40].indexOf(e.keyCode)>-1){
			e.preventDefault();
		}
		return;		
	});
});

function swapGroupCourse(active,nowObj,clsId){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	
	//TODO 交换操作
	var subGroup = active.parents("tr").find(".subGroup").val();
	var subBatchParam = active.find(".subjectId").val()+"-"+$(nowObj).parent().attr("data-batch")
		+","+$(nowObj).find(".subjectId").val()+"-"+active.parent().attr("data-batch");

	$.ajax({
	    url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/swapCourse?planType=${planType!}',
        data:{'subGroup':subGroup,'zhbId':clsId,'subBatchParam':subBatchParam},
        type:"post",
        dataType:'json',
        success:function(jsonO){
            if (jsonO.success) {
            	if(showRight) moveStuCount();
                layer.msg(jsonO.msg,{offset:'t',time:2000});
                if(jsonO.code == '04'){
	                floatingPlan();
	                return;
                }
                
                // 交换 科目 批次点
				var one = $(active.parent().html());
				var another = $($(nowObj).parent().html());
				$(nowObj).parent().append(one);
				active.parent().append(another);
				$(nowObj).remove();
				active.remove();
				$(".class-group-table").find(".active").removeClass("active");
				$(".class-group-table").find(".exchange").removeClass("exchange");
            }else{
            	layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
            }
            
			isSubmit = false;
        }
	});
}

function makeLayerMoveCourse(batchs,oldChose,clsId){
	
	var batcharr = batchs.split(",");
	var choseSubIdarr = oldChose.split(",");
	var choseInfos = classChoseMap[clsId];
	
	$(".layer-move-course .publish-course span").each(function(i,obj){
		$(obj).removeClass("active").removeClass("disabled");
		var bth = $(obj).attr("data-batch");
		if(batcharr.indexOf(bth) > -1){
			$(obj).addClass("active");
			<#if !(canEdit?boolean)>
			$(obj).addClass("disabled");
			</#if>
		}else{
			$(obj).addClass("disabled");
		}
	});
	$(".layer-move-course .publish-course").attr("data-num",batcharr.length);
	
	$(".layer-move-course .selSub").removeAttr("disabled");
	$(".layer-move-course .selSub").each(function(i,obj){
		var batch = $(obj).attr("data-batch");
		var batch_index = batcharr.indexOf(batch);
		if(batch_index > -1){
			$(obj).html(makeOptionStr(choseSubIdarr[batch_index]));
		}else{
			$(obj).html(makeOptionStr());
			$(obj).attr("disabled","disabled");
		}
	});
	
	
	//$(".layer-move-course .form-horizontal").html(cnt);
	if(!${canEdit!}){
		$(".layer-move-course .form-horizontal select").attr("disabled","disabled");
	}
	
	var thstr = "";
	var tbodystr = "";
	var freeStr = "";
	for(var x in choseInfos){
		var info = choseInfos[x];
		thstr += "<th>"+info[2]+"</th>";
		tbodystr += "<td>"+info[1]+"</td>";
		if(freeSubIds.indexOf(info[0]) > -1){
			freeStr += ","+info[2];
		}
	}
	var tstr = `
		<thead>
			<tr>
				<th>${batchstr!}科目</th>
				`+thstr+`
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>人数</td>
				`+tbodystr+`
			</tr>
		</tbody>`;
	$(".layer-move-course table").html(tstr);
	
	// 不开班 科目 提示
	freeStr = freeStr.substr(1);
	if(freeStr){
		$(".layer-move-course .mytip").text(freeStr+" 是不开班科目");
	}
	
	
	function makeOptionStr(selVal){
		var h = "<option value=''>无</option>";
		for(x in choseInfos){
			var info = choseInfos[x];
			if(info[0] == selVal){
				h += '<option selected value="'+info[0]+'">'+info[2]+'</option>';
			}else{
				h += '<option value="'+info[0]+'">'+info[2]+'</option>';
			}
		}
		return h;
	}
}

function setClassTime(obj){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	debugger;
	
	var batchs = $(obj).val();
	
	var clsId = $(obj).parents("tr").find(".zhbId").val();
	
	$.ajax({
	    url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/setMoveTime?planType=${planType!}',
        data:{'batchParam':batchs,'zhbId':clsId},
        type:"post",
        dataType:'json',
        success:function(jsonO){
            if (jsonO.success) {
                layer.msg(jsonO.msg,{offset:'t',time:2000});
                if(showRight) moveStuCount();
                //floatingPlan();
				// 关闭
				
            }else{
            	layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
            }
            isSubmit = false;
        }
	});
}
</script>