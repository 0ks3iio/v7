<title>设置不排考或不统分</title>
<!-- ajax layout which only needs content area -->
<div class="jqGrid_wrapper">
    <table id="filterList"></table>
    <table id="filterPager"></table>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@filterList />
	});
</script>

<#macro filterList>
var url = "${request.contextPath}/scoremanage/examNum/filterlist?examId=${examId!}&classType=${classType!}&classIdSearch=${classIdSearch!}&arrType=${arrType!}&tabType=${tabType!}";
$("#filterList").jqGrid({
	url: url,
	datatype: "json",
	mtype:"GET",
	height:"auto",
	autowidth:true,
	shrinkToFit:true,
	viewrecords:true,
	hidegrid:false,
	multiselect: true,
	loadtext:'正在加载...',//当数据还没加载完或数据格式不正确时显示
	caption: "信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	colModel:[
	//修改了jqgrid的源码，使其支持表头th元素的样式控制(thclasses)
	{name:"student.id",width:90,sortable:false,hidden:true},
	{name:"student.studentName",label:"姓名",width:90,sortable:false},
	{name:"student.identityCard",label:"身份证号",width:120,sortable:false},
	{name:"className",label:"班级",width:90,sortable:false},
	{name:"student.studentCode",label:"学号",width:120,sortable:false}
],
gridComplete: function () {
  updatePagerIcons(this);
}, 
jsonReader: {
	id: "student.id"
	},
pager:"#filterPager"}
);
$("#filterPager_center").hide();
var shoename="";
<#if (tabType =='2')>
	shoename="不排考";
<#elseif (tabType =='3')>
	shoename="不统分";
</#if>
<#if (arrType =='1')>
$("#filterList").jqGrid('navGrid',"#filterPager",
	{ 
		add: false,
		addicon : 'ace-icon fa fa-plus-circle purple',
		edit: false,
		editicon : 'ace-icon fa fa-pencil blue',
		view: false,
		viewicon : 'ace-icon fa fa-search-plus grey',
		del: true,
		delicon : 'ace-icon fa fa-pencil blue',
		deltitle:'所选记录设置'+shoename,
		delfunc : function(ids){
			swal(
			{	title: shoename+"设置", 
				html: true, 
				text: "确认要设置"+shoename+"？",   
				type: "warning", 
				showCancelButton: true, 
				closeOnConfirm: false, 
				confirmButtonText: "是",
				cancelButtonText: "否",
				showLoaderOnConfirm: true,
				animation:false
			},
			function(){
				doDeleteById(ids);
			});
		},
		search: false,
		searchicon : 'ace-icon fa fa-search orange',
		refresh: true,
		refreshicon : 'ace-icon fa fa-refresh green',
		alerttext : '请选择行',
		alertcap : '警告'
	}
);
<#elseif (arrType =='2')>
$("#filterList").jqGrid('navGrid',"#filterPager",
	{ 
		add: false,
		addicon : 'ace-icon fa fa-plus-circle purple',
		edit: false,
		editicon : 'ace-icon fa fa-pencil blue',
		view: false,
		viewicon : 'ace-icon fa fa-search-plus grey',
		del: true,
		delicon : 'ace-icon fa fa-trash-o red',
		deltitle:'所选记录撤销'+shoename,
		delfunc : function(ids){
			swal(
			{	title: "撤销"+shoename+"设置", 
				html: true, 
				text: "确认要撤销不"+shoename+"？",   
				type: "warning", 
				showCancelButton: true, 
				closeOnConfirm: false, 
				confirmButtonText: "是",
				cancelButtonText: "否",
				showLoaderOnConfirm: true,
				animation:false
			},
			function(){
				doDeleteById(ids);
			});
		},
		search: false,
		searchicon : 'ace-icon fa fa-search orange',
		refresh: true,
		refreshicon : 'ace-icon fa fa-refresh green',
		alerttext : '请选择行',
		alertcap : '警告'
	}
);
</#if>
function doDeleteById(ids){
	var sendIds;
	if(ids instanceof Array){
		sendIds='';
		for(var i=0;i<ids.length;i++){
			sendIds+=","+ids[i];
		}
		sendIds=sendIds.substring(1,sendIds.length);
	}else{
		sendIds=ids;
	}
	$.ajax({
		url:'${request.contextPath}/scoremanage/examNum/filterlist/filterSet',
		data: {'ids':sendIds,'examId':'${examId!}','arrType':'${arrType!}','tabType':'${tabType!}'},
		type:'post',
		beforeSend:function(XMLHttpRequest){
			
		},  
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			swal({title: "操作成功!",
					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"},
					function(){
						$("#filterList").trigger("reloadGrid");
					}
				);
	 		}
	 		else{
				swal({title: "操作失败!",
					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
				);
			}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			var text = syncText(XMLHttpRequest);
 			swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
		}
	});
}
</#macro>
