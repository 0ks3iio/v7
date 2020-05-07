<div id="data-group-index" <#if type == 0>class="index" <#else>style="background-color: #EAEDF1"</#if>>
	<div class="box box-structure">
		<#if type == 0>
		<div class="box-header clearfix">
			<div class="float-right">
				<button class="btn btn-lightblue btn-fang js-add-data" onclick="componentSet('');"><i class="wpfont icon-plus"></i></button>
			</div>
		</div>
		</#if>
		<div class="box-body body-scroll-32">
			<div class="row no-padding kanban-wrap height-1of1 scrollBar4">
				<#if templates?exists&&templates?size gt 0>
				<#list templates as template>
					<div class="part-box ${template.width!}"  id="board-detail-${template.id!}"></div>
				</#list>
				</#if>
			</div>
		</div>
		<div class="layer layer-default-height add-data clearfix" id="boardComponentEditDiv"></div>
	</div>
</div>
<#if templates?exists&&templates?size gt 0>
<#else>
	<#if type == 0>
	<div class="no-data">
		<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data.png"/>
		<div>还未添加${userProfileName!}标签，点击右上角+添加标签</div>
	</div>
	</#if>
</#if>
<script type="text/javascript">
function componentSet(id){
	 libraryIndex =layer.open({
		type: 1,
        title: '添加(编辑)标签',
        area: ['750px', '600px'],
        btn: ['确定', '取消'],
        yes: function (index, layero) {
    		saveGroupTag();
    	},
        content: $('#boardComponentEditDiv')
	});
	$("#boardComponentEditDiv").empty().load("${request.contextPath}/bigdata/userTag/group/edit?profileCode=${profileCode}&id="+id);
	return false;
}

function deleteComponent(id){
	showConfirmTips('prompt',"提示","您确定要删除吗？",function(){
	 	$.ajax({
	            url:"${request.contextPath}/bigdata/userTag/group/delete/"+id,
	            data:{
	              
	            },
	            type:"post",
	            clearForm : false,
				resetForm : false,
	            dataType: "json",
	            success:function(data){
	            	layer.closeAll();
			 		if(!data.success){
			 			showLayerTips4Confirm('error',data.message);
			 		}else{
			 		    showLayerTips('success',data.message,'t');
     					$('.page-content').load('${request.contextPath}/bigdata/userTag/group/portrait?type=0&profileCode=${profileCode}'); 
	    			}
	          },
	          error:function(XMLHttpRequest, textStatus, errorThrown){}
	    });
	});
}
function showScaleDiv(data,obj){
	var datas = data.datas;
	html='<div class="text-center"><h3 class="color-blue font-20 bold">'+data.total+'</h3><span>${userProfileName!}总数</span></div>';
	$.each(datas, function (i, v) {
        var classBar = 'progress-bar-primary';
        if(i%2==1){
        	classBar = 'progress-bar-warning';
        }
		html+='<div class="app-percent"><p class="position-relative"><span>'+v.name+'</span>';
		html+='<span class="pos-right"><b>'+v.count+'</b></span></p>';
		html+='<div class="progress no-margin"><div class="progress-bar '+classBar+'" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width:'+v.proportion+'%;"></div></div></div>';
    });
	$(obj).html(html);
}
$(document).ready(function(){
	var tagArray = encodeURI('${tagArray!}');
	<#if templates?exists&&templates?size gt 0>
		<#list templates as template>
			  $("#board-detail-${template.id!}").load("${request.contextPath}/bigdata/userTag/group/detail/${template.id!}?type=${type}&tagArray="+tagArray);		
		</#list>
	<#else>
		<#if type != 0>
		var htmlstr = '<div class="no-data-common"><div class="text-center"><img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>';
			htmlstr+='<p class="color-999">无数据</p></div></div>'	;
			$("#data-group-index").html(htmlstr);
		</#if>
	</#if>
});
</script>