<#if teacherList?exists && teacherList?size gt 0>
<table>
<#list teacherList as teacher>
	<tr data-teacher="${teacher.id!}">
        <td class="name" style="width:100px;">${teacher.teacherName!}</td>
		<td>${teacher.teacherCode!}</td>
		<#if type?default('1')=='1'>
        <td class="opt">
       		 <a class="js-replace" href="javascript:"  data-tname="${teacher.teacherName!}" data-tId="${teacher.id!}" onClick="saveTipsay('${teacher.id!}')">代课</a>		
        </td>
        <#else>
        <td class="opt"><a class="js-replace" href="javascript:" data-tname="${teacher.teacherName!}" data-tId="${teacher.id!}" onClick="saveTipsay('${teacher.id!}')">管课</a></td>
		</#if>
	</tr>
	</#list>
</table>

<script>
	var chooseTeacherId="";
	$(function(){
		//隔行颜色变化
		$('.tab-wrap .tab-item').each(function(){
	        $(this).find('table tr:even').addClass('even');
	    });
	    
	    //滑动
	    $('.tab-item table tr').hover(function(){
			$(this).addClass('current').siblings('tr').removeClass('current');
			var teacherId=$(this).attr("data-teacher");
			var teacherName=$(this).find(".name").html();
			chooseTeacherId=teacherId;
				
				//延迟一秒
				setTimeout(function(){
					if(teacherId==chooseTeacherId){
						//获取教师课表
						$("#moveTeacherName").html(teacherName);
						loadTeacherByWeek(teacherId,"teacherTable1");
					}else{
						
					}
				},1000);
			
		},function(){
			$(this).removeClass('current');
		});
		
	})
	

	var isSave=false;
	function saveTipsay(teacherId){
		//form提交
		if(isSave){
			return ;
		}
		isSave=true;
		var tipsayId=$("#tipsayId").val();
		if(!tipsayId){
			tipsayId="";
		}
		
		$.ajax({
			url : "${request.contextPath}/basedata/tipsay/saveTipsay2",
			data:{"tipsayId":tipsayId,"newTeacherId":teacherId},
			type:'post', 
			dataType:'json',
			success:function(data){
				var jsonO = data;
		 		if(!jsonO.success){
		 			isSave=false;
		 			autoTips(jsonO.msg)
		 		}else{
		 			isSave=false;
		 			autoTips('操作成功');
		 			//左边选中数据清空
		 			$("#allTable").find(".item").html("");
					//关闭
					$("#teacherTable1").find(".item").html("");
					$("#moveTeacherName").html("");
				}
				
			}
		});	
		
	}
	
</script>
<#else>
<div class="tab-item rightTeacherList">
	<div class="t-center mt-80">
		<img src="${request.contextPath}/static/images/public/nodata.png" alt="">
		<p class="c-999">暂无数据</p>
	</div>
</div>
</#if>