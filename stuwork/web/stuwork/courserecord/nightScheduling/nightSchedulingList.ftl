<#import "/fw/macro/popupMacro.ftl" as popup />
<form id="subForm" method="post">
<div class="table-container">
	<div class="table-container-body" id="myDiv">
		<table class="table table-striped layout-fixed">
			<thead>
				<tr>
				    <th width="10%"><label><input type="checkbox" class="wp" id="check-all"  ><span class="lbl" > 全选</span></label></th>
					<th width="30%">行政班</th>
					<th>值班老师</th>
					<th width="20%">操作</th>
				</tr>
			</thead>
			<tbody>
			<#if listDNSD?exists && listDNSD?size gt 0>
			   <#list listDNSD as dnsd>
			       <tr>
					   <td class="cbx-td"><label><input type="checkbox" class="wp"><span class="lbl"> ${dnsd_index+1!}</span></label></td>
			           <input type="hidden" id="classId${dnsd_index+1}"  class="classId"  value="${dnsd.clazz.id!}">
					   <td>${dnsd.clazz.classNameDynamic!}</td>
					   <td>
							<input type="hidden" id="teaIds${dnsd_index+1}" value="${dnsd.teacherId!}">
							<input type="hidden" id="teaName${dnsd_index+1}" value="${dnsd.teacherName!}">
							<input type="text" id="teaName${dnsd_index+1}" class="form-control" value="${dnsd.teacherName!}" onclick="editTeaId('${dnsd_index+1}')">
					   </td>
					   <td><a href="javascript:void(0);" id="remove${dnsd_index+1}" onclick = "removeTea('${dnsd.clazz.id!}');">清空</a></td>
				   </tr>
			   </#list>
			</#if>				
			</tbody>
		</table>
	</div>
</div>
</form>
<div style="display: none;">
<@popup.selectOneTeacher clickId="teacherName" columnName="教师(单选)" dataUrl="${request.contextPath}/common/div/teacher/popupData" id="teacherId" name="teacherName" dataLevel="2" type="danxuan" recentDataUrl="${request.contextPath}/common/div/teacher/recentData" resourceUrl="${resourceUrl}" handler='saveNightSch()'>
    <input type="text" id="teacherName"  class="form-control"/>
    <input type="hidden" id="teacherId" name="teacherId" />
    <input type="hidden" id="classId" name="classId" />
</@popup.selectOneTeacher>
</div>

<script>
isSubmit = false;
var index="";
function editTeaId(number){
	index=number;
	var teacherId =$("#teaIds"+number).val();
	var teacherName = $("#teaName"+number).val();
	var classId = $("#classId"+number).val();
	$('#teacherId').val(teacherId);
	$('#teacherName').val(teacherName);
	$('#classId').val(classId);
	$('#teacherName').click();
}

function saveNightSch(){
	if(isSubmit){
        return;
    }
    
    var teacherId=$("#teacherId").val();
	var classId= $("#classId").val();
	var queryDate = $('#queryDate').val();
	
	//批量选择
	if(classId == ""){
	   saveNightSchs();
	   return;
	}
	
	if(!teacherId  || teacherId==""){
		$('#remove'+index).click();
		return;
	}
	var teacherName=$("#teacherName").val();
	$("#teaName"+index).val(teacherName);
	
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/stuwork/night/scheduling/savetea",
			data:{"teacherId":teacherId,"classId":classId,"queryDate":queryDate},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 		}else{
					layerTipMsg(data.success,data.msg,"");
    			}
				searchList();
    			isSubmit = false;
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}

//点击选择
$("#check-all").unbind("click").bind("click",function(){
    checkAll();
});
function checkAll(){
    var checked = $("#check-all").attr("checked")=="checked"?true:false;
    $("#check-all").attr("checked",!checked);
    $(".cbx-td").find("input").each(function(){
        if(!checked==true && $(this).attr("checked")=="checked"){
            return ;
        }
        if(!checked==false && $(this).attr("checked")!="checked"){
            return ;
        }
        $(this).click();
        $(this).attr("checked",!checked);
        if(!checked==true){
            $(this).parents("tr").find(".classId").attr("name","classId");
        }else{
            $(this).parents("tr").find(".classId").removeAttr("name");
        }
    });
}
$(".cbx-td").each(function(){
            $(this).find("input").unbind("click").bind("click",function(){
                var checked = $(this).attr("checked")=="checked"?true:false;
                if(checked){
                    $(this).attr("checked",false);
                    $(this).parents("tr").find(".classId").removeAttr("name");
                }else{
                    $(this).attr("checked",true);
                    $(this).parents("tr").find(".classId").attr("name","classId");
                }
            });
        });
//清空
function removeTea(clazzId){
  var queryDate = $("#queryDate").val();
  $.ajax({
	            url:"${request.contextPath}/stuwork/night/scheduling/deletetea?clazzId="+clazzId+"&queryDate="+queryDate,
	            clearForm : false,
	            resetForm : false,
	            dataType:'json',
	            contentType: "application/json",
	            type:'post',
	            success:function (data) {
	                if(data.success){
	                    showSuccessMsgWithCall(data.msg,searchList);
	                }else{
	                    showErrorMsg(data.msg);
	                }
	            }
	      })
}

//安排老师
function setDutyTea(){
  $('#teacherName').click();
  $('#classId').val("");
  
}

//点击确定
function saveNightSchs(){
    var teacherId=$("#teacherId").val();
	var queryDate = $('#queryDate').val();
    $.ajax({
            url:"${request.contextPath}/stuwork/night/scheduling/saveClazzs?teacherId="+teacherId+"&queryDate="+queryDate,
            data:dealDValue(".table-striped"),
            clearForm : false,
            resetForm : false,
            dataType:'json',
            contentType: "application/json",
            type:'post',
            success:function (data) {
                if(data.success){
                    showSuccessMsgWithCall(data.msg,searchList);
                }else{
                    showErrorMsg(data.msg);
                }
            }
      })
}

 //从desktop.js中引用的方法
function dealDValue(container){
    var tags = ["input","select","textarea"];
    var os ;
    var obj = new Object();
    for(var i=0; i<tags.length;i++) {
        if (typeof(container) == "string") {
            os = jQuery(container + " " + tags[i]);
        }
        else {
            return;
        }
        os.each(function () {
            $this = $(this);
            var value = $this.val();
            var type = $this.attr("type");
            if ((type == 'number' || type=='int') && value!='' && value!=null) {
                value = parseInt(value);
            }
            var name = $this.attr("name");
            name = name || $this.attr("id");
            var exclude = $this.attr("exclude");
            if (!exclude) {
                if (obj[name] && !(obj[name] instanceof Array)) {
                    var array = new Array();
                    array.push(obj[name]);
                    array.push(value);
                    obj[name] = array;
                } else if (obj[name] && obj[name] instanceof Array){
                    obj[name].push(value);
                }else{
                    obj[name] = value;
                }
            }
        });
    }
    return JSON.stringify(obj);
}
</script>