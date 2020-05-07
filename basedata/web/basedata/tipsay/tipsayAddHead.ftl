<#--<a  href="javascript:void(0);" onclick="goTipsayList();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-body">
		<input type="hidden" id="acadyear" value="${semesterObj.acadyear!}"/>
		<input type="hidden" id="semester" value="${semesterObj.semester!}"/>
		<input type="hidden" id="oldWeek" value="${oldWeek!}"/>
		<input type="hidden" id="listType" value="${type!}"/>
		<div class="btn-group mb20">
            <button type="button" class="btn btn-default week-left"><i class="glyphicon glyphicon-menu-left"></i></button>
            <select class="form-control float-left" style="width: 100px;" id="searchWeek" onChange="searchByWeek()">
                <#if weekList?exists && (weekList?size>0)>
		              <#list weekList as item>
			               <option  value="${item!}" <#if item==oldWeek?default(0)>selected="selected"</#if>>第${item!}周</option>
		              </#list>
	             <#else>
		               <option value="">---请选择---</option>
	             </#if>
            </select>
            <button type="button" class="btn btn-default week-right"><i class="glyphicon glyphicon-menu-right"></i></button>
        </div>
		<div class="row" id="tipsaySchedule">

		</div>
	</div>
</div>



<script>
	var leftWeek=0;
	var rightWeek=0;
	$(function(){
		searchByWeek();
		showBreadBack(goTipsayList,false,"教务安排");
	})
	$(".week-left").on('click',function(){
		if(leftWeek>0){
			document.getElementById('searchWeek').value=leftWeek;
			<#--$("#searchWeek").find("option").each(function(){
				var e1=$(this).attr("value");
				console.log(e1);
				if(e1==""){
					return false;
				}
				var ee1=parseInt(e1);
				if(ee1==leftWeek){
					$(this).prop("selected",true);
				}else{
					$(this).attr("selected",false);
				}
			})-->
		}
		searchByWeek();
	})
	
	
	$(".week-right").on('click',function(){
		if(rightWeek>0){
			document.getElementById('searchWeek').value=rightWeek;
			<#--$("#searchWeek").find("option").each(function(){
				var e2=$(this).attr("value");
				if(e2==""){
					return false;
				}
				var ee2=parseInt(e2);
				if(ee2==rightWeek){
					$(this).attr("selected",true);
				}else{
					$(this).attr("selected",false);
				}
			})-->
		}
		searchByWeek();
	})
	
	function searchByWeek(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var week=$("#searchWeek").val();
		if(week==""){
			alert("请先选择周次");
			$("#tipsaySchedule").html("");
			return ;
		}
		makeLeftOrRight(week);
		var url =  '${request.contextPath}/basedata/tipsay/loadContent/page?acadyear='+acadyear+'&semester='+semester+'&week='+week;
		$("#tipsaySchedule").load(url);
	}
	
	function makeLeftOrRight(week){
		if(week==""){
			return;
		}
		var weekInt=parseInt(week);
		leftWeek=weekInt;
		rightWeek=weekInt;
		$("#searchWeek").find("option").each(function(){
			var eee=$(this).attr("value");
			if(eee==""){
				return false;
			}
			var eee1=parseInt(eee);
			if(eee1==weekInt){
				//现在相等的数据
			}else if(eee1>weekInt){
				if(rightWeek==weekInt || rightWeek<weekInt){
					rightWeek=eee1;
				}else{
					if(rightWeek>eee1){
						rightWeek=eee1;
					}
				}
				
			}else{
				if(leftWeek==weekInt || leftWeek>weekInt){
					leftWeek=eee1;
				}else{
					if(leftWeek<eee1){
						leftWeek=eee1;
					}
				}
			}
		})
	}
	
	function goTipsayList(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var week= $("#oldWeek").val();
		var type=$("#listType").val();
		var url =  '${request.contextPath}/basedata/tipsay/tab/page?acadyear='+acadyear+'&semester='+semester+'&week='+week+'&type='+type;
		$("#showList").load(url);
	}
</script>








