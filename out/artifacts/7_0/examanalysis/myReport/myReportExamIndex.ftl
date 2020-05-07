<div class="box box-default">
	<div class="box-body">
        <ul class="nav nav-tabs nav-tabs-1">
        	<#if nameList?exists && nameList?size gt 0>
        	<#list nameList as item>
        		<#assign value=typeMap[item]>
	            <li <#if item_index==0>class="active"</#if>>
	                <a href="javascript:void(0)" onclick="showList('${value!}');" data-toggle="tab" data-value="${value!}">${item!}</a>
	            </li>
	        </#list>
            </#if>
        </ul>
        <div class="tab-content">
            <div  class="tab-pane active">
                <div class="filter">
                    <div class="filter-item">
                        <span class="filter-name">学年：</span>
                        <div class="filter-content">
                            <select class="form-control" id="acadyear" name="acadyear" onChange="showList()">
								<#if acadyearList?exists && (acadyearList?size>0)>
				                    <#list acadyearList as item>
					                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
				                    </#list>
			                    <#else>
				                    <option value="">未设置</option>
			                     </#if>
							</select>
                        </div>
                    </div>
                    <div class="filter-item">
                        <span class="filter-name">学期：</span>
                        <div class="filter-content">
                            <select class="form-control" id="semester" name="semester" onChange="showList()">
								${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
							</select>
                        </div>
                    </div>
                    <div class="filter-item">
                        <span class="filter-name">年级：</span>
                        <div class="filter-content">
                            <select class="form-control" id="gradeCode" onChange="showList()">
                            	<option value="">全部</option>
								<#if gradeList?exists && gradeList?size gt 0>
									<#list gradeList as item>
									<option value="${item.gradeCode!}" <#if item.gradeCode==gradeCode?default("")>selected="selected"</#if>>${item.gradeName!}</option>
									</#list>
								</#if>
							</select>
                        </div>
                    </div>
                </div>
            </div>
            <input type="hidden" id="type" value="${type!}">
            <div id="showListDiv">
            
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
	$(function(){
       showList('${type!}');
	})
	function showList(type){
		if(type){
			$("#type").val(type);
		}else{
			type=$("#type").val();
		}
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		$("#showListDiv").load("${request.contextPath}/examanalysis/emReport/examList/page?type="+type+"&acadyear="+acadyear+"&semester="+semester+"&gradeCode="+gradeCode);
	}
</script>
