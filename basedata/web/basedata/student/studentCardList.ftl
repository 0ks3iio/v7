<#import "/fw/macro/webmacro.ftl" as w>
<title></title>
<!-- ajax layout which only needs content area -->
<font style="color:red;">注意：（1）双击行，回车进行单行保存（2）双击使其在编辑状态，之后左下角进行对编辑状态数据进行批量保存</font>
<div class="jqGrid_wrapper">
    <table id="cardStuList"></table>
    <div id="cardStuPager"></div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@cardStuList />
	});
</script>

<#macro cardStuList>
var url = encodeURI("${request.contextPath}/basedata/student/cardList?stuName=${stuName!}&sex=${sex!}");
$("#cardStuList").jqGrid({
	url: url,
	datatype: "json",
	mtype:"GET",
	height:"auto",
	autowidth:true,
	shrinkToFit:true,
	viewrecords:true,
	hidegrid:false,	
	multiselect: false,//复选框
	loadtext:'正在加载...',//当数据还没加载完或数据格式不正确时显示
	caption: "学生信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	colModel:[
		//修改了jqgrid的源码，使其支持表头th元素的样式控制(thclasses)
		{name:"id",sortable:false,hidden:true},
		{name:"studentName",label:"姓名",width:60,sortable:false},
		{name:"sex",label:"性别",width:60,sortable:false,formatter:"select",editable:false,edittype:"select",
			editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid('DM-XB')}"}},
		{name:"className",label:"班级",width:100,sortable:false},
		{name:"identitycardType",label:"身份证类型",width:120,sortable:false,formatter:"select",editable:true,edittype:"select",
		editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid('DM-SFZJLX')}"}},
		{name:"identityCard",label:"身份证件号码",width:120,sortable:false,editable: true}
	],
	ondblClickRow : function(id,iRow,iCol,e) {
           if(id){
              $('#cardStuList').jqGrid("editRow",id,{
                    keys : true,        //设置为true可以使用 [Enter]保存数据或者[Esc] 取消编辑
                    url: "${request.contextPath}/basedata/student/stuCardEditing",
                    mtype : "POST",  
                    restoreAfterError: true,  
                    extraparam: {  
                       "student.id": id,  
                       "student.identityCard": $("#"+id+"_identityCard").val(),  
                       "student.identitycardType": $("#"+id+"_identitycardType").val()
                    },  
                    oneditfunc: function(rowid){  
                    	//编辑时调用方法
                    },  
                    successfunc: function(response){ 
                    	var s=checksave(response);
                    	return s;
                    },  
                    errorfunc: function(rowid, res){  
                 	}
                    
                });
             }
        },

gridComplete: function () {
}, 

jsonReader: {
	id:"id"
},
pager:"#cardStuPager"}
);
$("#cardStuList").jqGrid('navGrid',"#cardStuPager",
	{ 	//navbar options
		add: false,
		addicon : 'ace-icon fa fa-plus-circle purple',
		edit: false,
		editicon : 'ace-icon fa fa-pencil blue',
		view: false,
		viewicon : 'ace-icon fa fa-search-plus grey',
		del: false,
		delicon : 'ace-icon fa fa-trash-o red',
		search: false,
		searchicon : 'ace-icon fa fa-search orange',
		refresh: true,
		refreshicon : 'ace-icon fa fa-refresh green',
		beforeRefresh:function(){
			lastsel="";
		},
		alerttext : '请选择行',
		alertcap : '警告'
	}
);
$("#cardStuPager_center").hide();
$("#refresh_cardStuList").parent().append('<td class="ui-pg-button ui-state-disabled" style="width:4px;"><span class="ui-separator"></span></td><td class="ui-pg-button ui-corner-all" title="批量保存编辑内容" id="mysaveall_cardStuList"><div class="ui-pg-div"><span class="ui-icon ui-icon-disk"></span></div></td>');
$("#mysaveall_cardStuList").unbind("click").bind("click",function(){
	saveAllEdit();
});
function checksave(result) {
    if (result.responseText == "") {
      return true;
    }else{
  		var jsonO = JSON.parse(result.responseText);
  		if(jsonO.success){
  			if(jsonO.msg=='不保存'){
  				return false;
  			}
  			return true;
  		}else{
  			swal({title: "操作失败!",
					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
				);
			return false;
  		}
    }
}
var isSubmit=false;
function saveAllEdit(){ 
	if(isSubmit){
		return false;
	}
	isSubmit=true;
	var data=[];      
	var rowData = $("#cardStuList").jqGrid("getRowData");  
	if (rowData.length < 1) { 
		swal({title: "提示!",
				text: "没有编辑数据需要保存",type: "error",showConfirmButton: true,confirmButtonText: "确定"}
			);
		isSubmit=false;
		return false;
	}
	var data1;
	for (var i = 0; i < rowData.length; i++) { 
		var id=rowData[i].id;
		var identitycardType=$("#"+id+"_identitycardType").val();
		//当identitycardType为undefined时就不是编辑状态的行
		if(typeof(identitycardType) != "undefined"){
			data1=rowData[i];
			var identitycardType=$("#"+id+"_identitycardType").val();
			data1.identitycardType=identitycardType;
			var identityCard=$("#"+id+"_identityCard").val();
			if(identityCard=null || identityCard.trim()==""){
				swal({title: "提示!",
				text: "身份证号不能为空",type: "error",showConfirmButton: true,confirmButtonText: "确定"}
				);
				isSubmit=false;
				return false;
			}
			data1.id=id;
			data1.identityCard=identityCard;
			data.push(data1);
		}
	}  
	if(data.length<=0){
		isSubmit=false;
		swal({title: "提示!",
				text: "没有编辑数据需要保存",type: "error",showConfirmButton: true,confirmButtonText: "确定"}
			);
		return false;
	}
	$.ajax({
	    url:"${request.contextPath}/basedata/student/stuCardSaveAll",
	    data: JSON.stringify(data),  
	    type:'post', 
	    dataType:"json", 
	    contentType:"application/json",     
	    success:function(data) {
	 		if(!data.success){
	 			swal({title: "操作失败!",
    			text: data.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
    				isSubmit=false;
    			});
	 		}
	 		else{
	 			swal({title: "操作成功!",
    			text: data.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"}, function(){
    				isSubmit=false;
    				$("#cardStuList").trigger('reloadGrid');
    			});			 			
			}
	    }
	});
			
}
</#macro>
