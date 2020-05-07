
	      <div class="box-body">
		  <!-- PAGE CONTENT BEGINS -->
			<div class="filter clearfix" style="padding-left: 20px;">
        	<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" name="acadyear" id="acadyear" onChange="searchList();">
					<#if acadyearList?? && (acadyearList?size>0)>
						<#list acadyearList as item>
							<option value="${item}" <#if item==acadyear?default('')>selected</#if>>${item!}</option>
						</#list>
					<#else>
						<option value="">暂无数据</option>
					</#if>
				</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="semester" name="semester" onChange="searchList();">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
					</select>
				</div>
			</div>		
            <div class="filter-item">
				<label for="" class="filter-name">年级：</label>
				<div class="filter-content">
	                <select name="" id="gradeId" class="form-control" onChange="searchList();">
					<#if gradeList?exists && gradeList?size gt 0>
					    <#list gradeList as item>
					        <option value="${item.id!}">${item.gradeName!}</option>
					    </#list>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
			    <#--a href="javascript:void(0);"  class="btn btn-blue" onclick = "copySubject();">复制</a-->
		        <a href="javascript:void(0);"  class="btn btn-blue js-edit">新增学科</a>
			</div>
    </div>
    <div class="table table-striped" id="tableList">
	</div>
	<div id="subjectEdit" class="layer layer-add">
    </div>
</div>
<script>
$(function(){
	searchList();
	// 新增
	$('.js-edit').on('click',function(e){
    	e.preventDefault();
    	var acadyear = $('#acadyear').val();
    	var semester = $('#semester').val();
    	var gradeId = $('#gradeId').val();
    	var that = $(this);
    	var id = that.closest('tr').find('.tid').val();
    	if(id != undefined){
    	    var url =  "${request.contextPath}/studevelop/subject/edit?id="+id;
    	}else{
    	    var url =  "${request.contextPath}/studevelop/subject/edit?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
    	}
		$("#subjectEdit").load(url,function() {
			layerShow('新增');
		});
    });
});

function layerShow(title){
   layer.open({
	    type: 1,
	    shade: 0.5,
	    title: title,
	    area: ['520px', '400px'],
	    btn: ['确定','取消'],
	    yes: function(index){
			saveSubject();
		},
	   content: $('.layer-add')
	});
}

function searchList(){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    if(acadyear == '${acadyear!}' && semester=='${semester!}'){
        $('#copyButton').show();
    }else{
        $('#copyButton').hide();
    }
    var gradeId = $('#gradeId').val();
    var url = "${request.contextPath}/studevelop/subject/list?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
    $("#tableList").load(url);
}

function copySubject(){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var gradeId = $('#gradeId').val();
    var url = "${request.contextPath}/studevelop/subject/copy?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
    $("#subjectEdit").load(url,function() {
		 layer.open({
	         type: 1,
	         shade: 0.5,
	         title: '复制到',
	         area: ['520px', '400px'],
	         btn: ['确定','取消'],
	         yes: function(index){
			      doCopy();
		     },
	         content: $('.layer-add')
	    });
	});
}
</script>