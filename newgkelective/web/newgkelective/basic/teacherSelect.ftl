<!-- 复制到 -->
<div class="layer-copy">
	<div class="layer-content">
		<div class="gk-copy" style="border: 1px solid #eee;">
			<div class="box-body padding-5 clearfix">
				<b class="float-left mt3">各科教师</b>
				<div class="float-right input-group input-group-sm input-group-search">
			        <div class="pull-left">
			        	<input type="text" id="findTeacher2" class="form-control input-sm js-search" placeholder="输入教师姓名查询" value="">
			        </div>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default" onClick="findTeacher2();">
					    	<i class="fa fa-search"></i>
					    </button>
				    </div>
			    </div>
			</div>
			<table class="table no-margin">
				<tr>
					<th width="127">科目</th>
					<th><label><input type="checkbox" name="copyTeacherAll" class="wp"><span class="lbl"> 全选</span></label></th>
				</tr>
			</table>
			<div class="gk-copy-side" id="myscrollspy2">
				<ul class="nav gk-copy-nav" style="margin: 0 -1px 0 -1px; height:445px;">
					<#if subjectInfoMap?exists>
	                    <#list subjectInfoMap?keys as mkey>
	                    	<li id="course_${mkey!}" class="courseLi <#if mkey_index == 0>active</#if>"><a  href="#aab_${mkey!}" data-value="${mkey!}">${subjectInfoMap[mkey].subjectName!}
	                    	<span class="badge badge-default"></span></a></li>
	                    </#list>
	                </#if>
				</ul>
				</ul>
			</div>
			<div class="gk-copy-main2 copyteacherTab">
				<div data-spy="scroll" data-target="#myscrollspy2" id="scrollspyDivId2"style="position:relative;height:445px;overflow:auto;border-left: 1px solid #eee;">
					<#if subjectInfoMap?exists>
	                    <#list subjectInfoMap?keys as mkey>
	                    	<#assign subjectInfo = subjectInfoMap[mkey]!>
	                    	<div id="aab_${mkey!}"  data-value="${mkey!}" pid="${subjectInfo.subjectId!}" class="tg">
								<div class="form-title ml-15 mt10 mb10">${subjectInfo.subjectName!}<a class="color-blue ml10 font-12 js-clearChoose" href="javascript:">取消</a> <a class="color-blue ml10 font-12 js-allChoose" href="javascript:">全选</a> </div>
								<ul class="gk-copy-list">
			                    	<#assign tidAndState = subjectInfo.teacherIdAndState!>
									 <#if tidAndState?exists && tidAndState?keys?size gt 0>
	                               	     <#list tidAndState?keys as tt >
											<label class="mr20">
												<input type="checkbox" class="wp <#if tidAndState[tt] == "1">sel</#if>" name="copyTeacher" value="${tt!}" pid="${subjectInfo.subjectId!}" data-value="${teacherIdToTeacherName[tt]!}">
												<span class="lbl"> ${teacherIdToTeacherName[tt]!}</span>
											</label>
										 </#list>
									 </#if>
								</ul>
							</div>
	                    </#list>
	                </#if>
				</div>
			</div>
		</div>
		<div class="no-data-container" id="noDataId" style="display:none;">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
				<div class="no-data-body">
					<p class="no-data-txt">没有相关数据</p>
				</div>
			</div>
		</div>
		<div style="display:none;">
			<form id="myForm"></form>
		</div>
	</div>
</div>
<div class="layui-layer-btn">
	<a class="layui-layer-btn0" id="act-commit" onclick="saveTea();">确定</a>
	<a class="layui-layer-btn1" id="act-close">取消</a>
</div>
<#--
<div class="layer-footer">
	<button class="btn btn-lightblue" id="act-commit" onclick="saveTea();">确定</button>
	<button class="btn btn-grey" id="act-close">取消</button>
</div>
-->
<script>
var isCheckAll = false;  
function swapCheck() {
	if (isCheckAll) {  
        $("input[type='checkbox']").each(function() {  
            this.checked = false;  
        });  
        isCheckAll = false;  
    } else {  
        $("input[type='checkbox']").each(function() {  
            this.checked = true;  
        });  
        isCheckAll = true;  
    }  
}
function findTeacher2(){
	var teacherName=$('#findTeacher2').val().trim();
	if(teacherName!=""){
		$(".gk-copy-main2 .lbl").removeClass("color-blue");
		var first;
		$(".gk-copy-main2 input").each(function(){
			var objVal = $(this).attr("data-value");
			if (objVal.includes(teacherName)) {
				if(!first){
					first=$(this);
				}
				$(this).siblings().addClass("color-blue");
			}
		});
		if(first){
			//模仿锚点定位
			var divId=$(first).parents("div").attr("id");
			document.getElementById(divId).scrollIntoView();
			
			var buid = $(first).parents("div").attr("data-value");
			document.getElementById("course_"+buid).scrollIntoView();
			setTimeout(function(){
				$("#course_"+buid).siblings().removeClass("active");
				$("#course_"+buid).addClass("active");
			},50);
		}
	}else{
		$(".gk-copy-main2 .lbl").removeClass("color-blue");
	}
}
// 选择老师 界面 搜索 
$('#findTeacher2').bind('keypress',function(event){//监听回车事件
    if(event.keyCode == "13" || event.which == "13")    
    {  
        findTeacher2();
    }
});

//点中数量
$(".copyteacherTab").off('click').on('click','input:checkbox[name=copyTeacher]',function(){
	var closeDiv=$(this).closest("div");
	var course_id=$(closeDiv).attr("data-value");
	var num=$("#course_"+course_id).find("span.badge").text();
	if(num.trim()==""){
		num=parseInt(0);
	}else{
		num=parseInt(num);
	}
	if($(this).is(":checked")){
		//+1
		num=num+1;
	}else{
		//-1
		num=num-1;
	}
	if(num>0){
		$("#course_"+course_id).find("span.badge").text(""+num);
		//用取消
		$(closeDiv).find(".js-allChoose").hide();
		$(closeDiv).find(".js-clearChoose").show();
	}else{
		$("#course_"+course_id).find("span.badge").text("");
		//用全选
		$(closeDiv).find(".js-allChoose").show();
		$(closeDiv).find(".js-clearChoose").hide();
	}
})
$('input:checkbox[name=copyTeacherAll]').on('change',function(){
	var actioveDiv=$(".copyteacherTab").find("div.active");
	if($(this).is(':checked')){
		$('input:checkbox[name=copyTeacher]').each(function(i){
			if(!$(this).is(':disabled')){
				$(this).prop('checked',true);
			}
		})
	}else{
		$('input:checkbox[name=copyTeacher]').each(function(i){
			$(this).prop('checked',false);
		})
	}
	//整体数量操作
	$(".courseLi").each(function(){
		var cid=$(this).find("a").attr("data-value");
		//计算数量
		var length=$("#aab_"+cid).find('input:checkbox[name=copyTeacher]:checked').length;
		if(length>0){
			$(this).find("span").text(""+length);
			$("#aab_"+cid).find(".js-allChoose").hide();
			$("#aab_"+cid).find(".js-clearChoose").show();
		}else{
			$(this).find("span").text("");
			$("#aab_"+cid).find(".js-allChoose").show();
			$("#aab_"+cid).find(".js-clearChoose").hide();
		}
	})
});

$(".js-allChoose").on('click',function(){
	var closeDiv=$(this).parent().parent();
	var cId=$(closeDiv).attr("data-value");
	var num=0;
	$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
		if(!$(this).is(':disabled')){
			$(this).prop('checked',true);
			num++;
		}
	})
	$("#course_"+cId).find("span").text(""+num);
	$(closeDiv).find(".js-allChoose").hide();
	$(closeDiv).find(".js-clearChoose").show();
})

$(".js-clearChoose").on('click',function(){
	var closeDiv=$(this).parent().parent();
	var cId=$(closeDiv).attr("data-value");
	$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
		$(this).prop('checked',false);
	})
	$("#course_"+cId).find("span").text("");
	$(closeDiv).find(".js-allChoose").show();
	$(closeDiv).find(".js-clearChoose").hide();
});

function clearSelTea2(){
	//1. 清除旧数据  2. 设置默认数据
	//数字清空
	$(".gk-copy-nav").find("span").each(function(){
		$(this).text("");
	});
	//去除之前的查询结果
	$('#findTeacher2').val('');
	//findTeacher2();
	
	//取消全选
	$('input:checkbox[name=copyTeacherAll]').prop("checked",false);
	
	//tab默认选中本科目
	//$(".courseLi").removeClass("active").eq(0).addClass("active");
	
	//取消所有选中老师与复制的条件
	$('.layer-selTea').find("input:checkbox[name=copyTeacher]").prop('checked',false);
	
	//取消所有不可选
	$('.layer-selTea').find("input:checkbox[name=copyTeacher]").prop('disabled',false);
	
	//全选显示 取隐藏
	$(".js-allChoose").show();
	$(".js-clearChoose").hide();

}
function makeRes(){
	var hs = "";
	var i=0;
	$("#scrollspyDivId2 .tg").each(function(){
		var subjectId = $(this).attr("pid");
		var $input = $(this).find("ul label :checked");
		if($input.length <= 0){
			return;
		}
		//TODO checkd
		hs += '<input type="hidden" name="teacherPlanList['+ i +'].subjectId" value="'+subjectId+'">';
		var j = 0;
		$input.each(function(){
			var tid = $(this).val();
			hs += '<input type="hidden" name="teacherPlanList['+ i +'].exTeacherIdList['+ j +']" value="'+tid+'">';
			j++;
		});
		i++;
	});
	$("#myForm").html(hs);
	if(i <= 0){
		return false;
	}
	//console.log(hs);
	return true;
}
var isSubmit=false;
function saveTea(){
	makeRes();
	
	//debugger;
	if(isSubmit){
		return;
	}
	isSubmit=true;
    var options = {
        url : '${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherSet/saveTeacher',
        data: {},
        dataType : 'json',
        success : function(data){
            if(data.success){
            	layer.closeAll();
				layer.msg(data.msg, {offset: 't',time: 2000});
               var url = '${request.contextPath}/newgkelective/${arrayItemId!}/goBasic/teacherSet/index/page?courseId=${courseId!}&useMaster=1';
               $("#gradeTableList").load(url);
            }
            else{
                layerTipMsg(data.success,"保存失败",data.msg);
            }
            isSubmit=false;
        },
        clearForm : false,
        resetForm : false,
        type : 'post',
        error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
    };
    
    $("#myForm").ajaxSubmit(options);
}

$("#myscrollspy2 ul li.courseLi").on("click",function(){
	var obj = this;
	setTimeout(function(){
		$(obj).siblings().removeClass("active");
		$(obj).addClass("active");
		
	},5);
});
$(function(){
	//debugger;
	clearSelTea2();
	$("input.sel").click().removeClass("sel");
	$('#scrollspyDivId2').scrollspy({ target: '#myscrollspy2' });
});
</script>
