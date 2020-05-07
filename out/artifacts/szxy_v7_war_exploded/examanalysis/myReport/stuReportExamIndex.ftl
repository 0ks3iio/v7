<div class="box box-default">
	<div class="box-body">
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
            </div>
        </div>
        <div id="showListDiv">
        
        </div>
    </div>
</div>
<script type="text/javascript">
	$(function(){
       showList();
	})
	function showList(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		$("#showListDiv").load("${request.contextPath}/examanalysis/emReport/stuExamList/page?acadyear="+acadyear+"&semester="+semester);
	}
</script>
