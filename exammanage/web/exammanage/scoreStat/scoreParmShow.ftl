<#if emStatParm?exists>
<h4 class="box-title">考生来源范围</h4>
<div class="filter filter-f16 ml-15"> 
    <div class="filter-item" style="display:none;">
		<span class="filter-name">学生来源：</span>
		<div class="filter-content">
			<#assign sourseType=emStatParm.sourseType?default('')>
			<#if sourceMap?exists && (sourceMap?size gt 0)>
				<#assign ss=0>
				<#list sourceMap?keys as key>
				<#if ss==0>
						<label class="pos-rel labelchose-w">
							<input name="sourseType" type="checkbox" <#if (sourseType?index_of(key) gte 0)>checked</#if> value="${key!}" class="wp">
							<span class="lbl">${sourceMap[key]!}</span>
					    </label>
			    <#else>
			    	<label class="pos-rel labelchose-w">
					<input name="sourseType" type="checkbox" <#if (sourseType?index_of(key) gte 0)>checked</#if> value="${key!}" class="wp">
					<span class="lbl" style="margin-left:10px;">${sourceMap[key]!}</span>
			   		 </label>
			    </#if>
			    <#assign ss=ss+1>
			    </#list>
		    </#if>
		</div>
	</div>
</div>
<div class="filter filter-f16 ml-15"> 
    <div class="filter-item">
    <#if emStatParm.subjectId?default('')!='00000000000000000000000000000000' && emStatParm.subjectId?default('')!='99999999999999999999999999999999'>
		<span class="filter-name">包括考生：</span>
		<div class="filter-content">
			<label class="pos-rel labelchose-w">
				<input name="isCheat" type="checkbox" <#if emStatParm.isCheat?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl">作弊生</span>
		    </label>
		    <label class="pos-rel labelchose-w">
				<input name="isMiss" type="checkbox" <#if emStatParm.isMiss?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl" style="margin-left:10px;">缺考生</span>
		    </label>
		    <label class="pos-rel labelchose-w">
				<input name="isZero" type="checkbox" <#if emStatParm.isZero?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl" style="margin-left:10px;">0分考生</span>
		    </label>
		    <!--
		    <label class="pos-rel labelchose-w">
				<input name="isOnlystat" type="checkbox" <#if emStatParm.isOnlystat?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl" style="margin-left:10px;">不统分考生</span>
		    </label>-->
		</div>
		<#else>
		<span class="filter-name">包括考生：</span>
		<div class="filter-content">
			<label class="pos-rel labelchose-w">
				<input name="isCheat" type="checkbox" <#if emStatParm.isCheat?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl">单科存在作弊</span>
		    </label>
		    <label class="pos-rel labelchose-w">
				<input name="isMiss" type="checkbox" <#if emStatParm.isMiss?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl" style="margin-left:10px;">单科存在缺考</span>
		    </label>
		    <label class="pos-rel labelchose-w">
				<input name="isZero" type="checkbox" <#if emStatParm.isZero?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl" style="margin-left:10px;">单科存在零分</span>
		    </label>
		    <!--
		    <label class="pos-rel labelchose-w">
				<input name="isOnlystat" type="checkbox" <#if emStatParm.isOnlystat?default("")=="1">checked</#if> value="1" class="wp"  disabled="disabled">
				<span class="lbl" style="margin-left:10px;">不统分考生</span>
		    </label>-->
		</div>
		</#if>
	</div>
</div>
<h4 class="box-title">学科分数段设置</h4>
<div class="filter ml-15"> 
	<div class="filter-item">
        <span class="filter-name">分数段类型：</span>
        <div class="filter-content">
        	<select name="statSpaceType" id="statSpaceType" class="form-control" onChange="findByStatSpaceType()"  disabled="disabled"> 
					<option value="" <#if emStatParm.statSpaceType?default('')=="">selected="selected"</#if>>不使用分数段</option>
			     	<option value="1" <#if emStatParm.statSpaceType?default('')=="1">selected="selected"</#if>>统一间距</option>
					<option value="2" <#if emStatParm.statSpaceType?default('')=="2">selected="selected"</#if>>非统一间距</option>
			</select>
        </div>
    </div>
	<div class="table-container">
	    <#if emStatParm.statSpaceType?default('')=="1">
	    <div class="table-container-body spaceType_1">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">分数上限</th>
						<th class="text-center">分数下限</th>
						<th class="text-center">间距</th>
					</tr>
				</thead>
				<tbody>
					<tr class="emSpaceItem0">
						<td class="text-center">
							${emStatParm.upScore?default(maxScore)}
						</td>
						<td class="text-center">${emStatParm.lowScore?default(0)}</td>
						<td class="text-center">${emStatParm.spaceScore?default(20)}</td>
					</tr>
				</tbody>
			</table>
		</div>
		</#if>
		<#if emStatParm.statSpaceType?default('')=="2">
		<div class="table-container-body spaceType_2">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">分段名称</th>
						<th class="text-center">分数上限</th>
						<th class="text-center">分数下限</th>
					</tr>
				</thead>
				<tbody>
					<#if emStatParm.emSpaceItemList1?exists && emStatParm.emSpaceItemList1?size gt 0>
						<#assign i1=emStatParm.emSpaceItemList1?size>
						<#list emStatParm.emSpaceItemList1 as item1>
						<tr class="emSpaceItem1">
							<td class="text-center">
								${item1.name!}
							</td>
							<td class="text-center">
							${item1.upScore!}
							</td>
							<td class="text-center">${item1.lowScore!}
						</tr>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
		</#if>
	</div>
	<div class="filter-item">
        <span class="filter-name">名次段统计：</span>
        <div class="filter-content">
        	<select name="needRankStat" id="needRankStat" class="form-control" disabled="disabled"> 
					<option value="0" <#if emStatParm.needRankStat?default('0')=="0">selected="selected"</#if>>不需要</option>
			     	<option value="1" <#if emStatParm.needRankStat?default('0')=="1">selected="selected"</#if>>需要</option>
			</select>
        </div>
    </div>
    <div class="table-container">
	    <#if emStatParm.needRankStat?default('')=="1">
		<div class="table-container-body spaceType_2">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">名次段名称</th>
						<th class="text-center">名次上限</th>
						<th class="text-center">名次下限</th>
					</tr>
				</thead>
				<tbody>
					<#if emStatParm.emSpaceItemList9?exists && emStatParm.emSpaceItemList9?size gt 0>
						<#list emStatParm.emSpaceItemList9 as item1>
						<tr class="emSpaceItem1">
							<td class="text-center">
								${item1.name!}
							</td>
							<td class="text-center">
							${item1.upScore!}
							</td>
							<td class="text-center">${item1.lowScore!}
						</tr>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
		</#if>
	</div>
	<div class="filter-item">
        <span class="filter-name">上线人数统计（按名次划分）：</span>
        <div class="filter-content">
        	<select name="needLineStat" id="needLineStat" class="form-control" disabled="disabled"> 
					<option value="0" <#if emStatParm.needLineStat?default('0')=="0">selected="selected"</#if>>不需要</option>
			     	<option value="1" <#if emStatParm.needLineStat?default('0')=="1">selected="selected"</#if>>需要</option>
			</select>
        </div>
    </div>
    <div class="table-container">
    <#if emStatParm.needLineStat?default('0')=="1">
    <div class="table-container-body needLineStat_1">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th class="text-center"></th>
					<th class="text-center">名次线</th>
				</tr>
			</thead>
			<tbody>
			<#if lines?exists && lines?size gt 0>
				<#list lines as line>
				<tr class="emLineItem1">
					<td class="text-center">
						线${line_index + 1}
					</td>	
					<td class="text-center">
						前&nbsp${line!}&nbsp名
					</td>
				</tr>
				</#list>
			</#if>
			</tbody>
		</table>
	</div>
	</#if>
	</div>
	<div class="filter-item">
        <span class="filter-name">优秀/不及格人数统计（按分数划分）：</span>
        <div class="filter-content">
        	<select name="needGoodLine" id="needGoodLine" class="form-control" disabled="disabled"> 
					<option value="0" <#if emStatParm.needGoodLine?default('0')=="0">selected="selected"</#if>>不需要</option>
			     	<option value="1" <#if emStatParm.needGoodLine?default('0')=="1">selected="selected"</#if>>需要</option>
			</select>
        </div>
    </div>
    <div class="table-container">
    <#if emStatParm.needGoodLine?default('0')=="1">
    <div class="table-container-body needLineStat_1">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th class="text-center">优秀分数线</th>
					<th class="text-center">及格分数线</th>
				</tr>
			</thead>
			<tbody>
				<tr class="">
					<td class="text-center">
						${emStatParm.goodLine?default(0)}
					</td>	
					<td class="text-center">
						${emStatParm.failedLine?default(0)}
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	</#if>
	</div>
    <#if emStatParm.subjectId?default('')!='00000000000000000000000000000000' && emStatParm.subjectId?default('')!='99999999999999999999999999999999'>
	<div class="filter-item">
        <span class="filter-name">是否参与本次的总分统计：</span>
        <div class="filter-content">
        	<select name="joinSum" id="joinSum" class="form-control" disabled="disabled"> 
					<option value="0" <#if emStatParm.joinSum?default('1')=="0">selected="selected"</#if>>不需要</option>
			     	<option value="1" <#if emStatParm.joinSum?default('1')=="1">selected="selected"</#if>>需要</option>
			</select>
        </div>
    </div>
	</#if>
</div>


<!--
	<h4 class="box-title">学科前百分比设置</h4>
	<div class="filter ml-15"> 
		<div class="filter-item">
	        <span class="filter-name">是否统计前百分比：</span>
	        <div class="filter-content">
	        	<select name="rankStatFront" id="rankStatFront" class="form-control" onChange="findByRankStatFront()" disabled="disabled">
						<option value="0">否</option>
				     	<option value="1" <#if emStatParm.rankStatFront?default('1')=="1">selected="selected"</#if>>是</option>
				</select>
	        </div>
	    </div>
	<div class="table-container">
		<#if emStatParm.rankStatFront?default('')=="1">
		<div class="table-container-body rankStatFront_class">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">分段名称</th>
						<th class="text-center">前百分比上限(%)</th>
						<th class="text-center">前百分比下限(%)</th>
					</tr>
				</thead>
				<tbody>
					<#if emStatParm.emSpaceItemList2?exists && emStatParm.emSpaceItemList2?size gt 0>
						<#list emStatParm.emSpaceItemList2 as item2>
						<tr class="emSpaceItem2">
							<td class="text-center">${item2.name!}</td>
							<td class="text-center">
							${item2.upScore!}
							</td>
							<td class="text-center">${item2.lowScore!}</td>
							
						</tr>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
		</#if>
	</div>
	</div>
-->
<!--
	<h4 class="box-title">学科后百分比设置</h4>
	<div class="filter ml-15"> 
		<div class="filter-item">
	        <span class="filter-name">是否统计后百分比：</span>
	        <div class="filter-content">
	        	<select name="rankStatBack" id="rankStatBack" class="form-control" onChange="findByRankStatBack()" disabled="disabled">
						<option value="0">否</option>
				     	<option value="1" <#if emStatParm.rankStatBack?default('1')=="1">selected="selected"</#if>>是</option>
				</select>
	        </div>
	    </div>
	<div class="table-container">
	<#if emStatParm.rankStatBack?default('')=="1">
		<div class="table-container-body rankStatBack_class" >
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center">分段名称</th>
						<th class="text-center">后百分比上限(%)</th>
						<th class="text-center">后百分比下限(%)</th>
						
					</tr>
				</thead>
				<tbody>
					<#if emStatParm.emSpaceItemList3?exists && emStatParm.emSpaceItemList3?size gt 0>
						<#list emStatParm.emSpaceItemList3 as item3>
						<tr class="emSpaceItem3">
							<td class="text-center">${item3.name!}</td>
							<td class="text-center">
							${item3.upScore!}
							</td>
							<td class="text-center">${item3.lowScore!}</td>
							
						</tr>
						</#list>
					</#if>
					
				</tbody>
			</table>
		</div>
		</#if>
	</div>
-->
</div>
<#else>
<h4 class="box-title">还没有设置该参数！</h4>
</#if>


