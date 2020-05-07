<#--临时演示-->
<style>
input {width:30px;height:30px;margin: 0 10px 10px 0;}
</style>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
	<div class="layer-content">
		<#if choiceSubjectDtoList?exists && (choiceSubjectDtoList?size>0)>
		<div class="publish-course publish-course-sm">
			<#assign count=4>
			<#if choiceSubjectDtoList?size<count>
				<#assign count = choiceSubjectDtoList?size>
			</#if>
			<#list 1..count as i>
			<span style="border:0px"><b>组合</b></span>
			<span style="width:32px;border:0px"><b>总数</b></span>
			<span style="width:32px;border:0px"><b>男</b></span>
			<span style="width:32px;border:0px"><b>女</b></span>
			</#list>
		</div>
		<div class="publish-course publish-course-sm">
				<#list choiceSubjectDtoList as item>
					<span class="subjectList" data-short="${item.shortNames!}">${item.shortNames!}</span>
					<span style="width:32px" class="allNum_${item.shortNames!}"><input type="text" vtype="int" maxlength="3" nullable="false" value="" placeholder=""></span>
					<span style="width:32px" class="boyNum_${item.shortNames!}"><input type="text" vtype="int" maxlength="3" nullable="false" value="" placeholder=""></span>
					<span style="width:32px" class="girlNum_${item.shortNames!}"><input type="text" vtype="int" maxlength="3" nullable="false" value="" placeholder=""></span>
				</#list>
		</div>
		</#if>
	</div>
	<div class="layer-footer">
   		<button class="btn btn-lightblue" id="arrange-commit">确定</button>
   		<button class="btn btn-grey" id="arrange-close">取消</button>
    </div>
</div>

<script>

$(function(){
	if($(".stuNum-list").text().trim()!="修改"){
		var stuArr = $(".stuNum-list").text().split("；");
		for(i=0;i<stuArr.length-1;i++){
			var numArr = stuArr[i].split("-");
			var comp = numArr[0].trim();
			$($(".subjectList[data-short='"+comp+"']")[0]).siblings(".allNum_"+comp).find("input").val(numArr[1].substring(1));
			$($(".subjectList[data-short='"+comp+"']")[0]).siblings(".boyNum_"+comp).find("input").val(numArr[2].substring(1));
			$($(".subjectList[data-short='"+comp+"']")[0]).siblings(".girlNum_"+comp).find("input").val(numArr[3].substring(1));
		}
	}

})

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 // 确定按钮操作功能
 $("#arrange-commit").on("click", function(){
 	var content = "";
 	var r = /^\+?[1-9][0-9]*$/;
 	var flag = false;　
 	$(".subjectList").each(function(){
 		var cla= "_"+$(this).text();
	 	var allNum = $(this).siblings(".allNum"+cla).find("input").val();
	 	var boyNum = $(this).siblings(".boyNum"+cla).find("input").val();	
	 	var girlNum = $(this).siblings(".girlNum"+cla).find("input").val();
	 	var str = $(this).text();
	 	if(!(allNum == "" && boyNum == "" && girlNum == "")){
		 	if(allNum!=""){
		 		if(!r.test(allNum)){
					layer.tips('请输入正整数', $(this).siblings(".allNum"+cla).find("input"), {
							tipsMore: true,
							tips:3				
					});
					flag=true;
					return;
				}
		 		str += ("-总"+allNum)
		 	}else{
		 		layer.tips('请输入值', $(this).siblings(".allNum"+cla).find("input"), {
						tipsMore: true,
						tips:3				
				});
				flag=true;
				return;
		 	}	
	 		
		 	if(boyNum!=""){
			 	if(!r.test(boyNum)){
					layer.tips('请输入正整数', $(this).siblings(".boyNum"+cla).find("input"), {
							tipsMore: true,
							tips:3				
					});
					flag=true;
					return;
				}
		 		str += ("-男"+boyNum)
	 		}else{
		 		layer.tips('请输入值', $(this).siblings(".boyNum"+cla).find("input"), {
						tipsMore: true,
						tips:3				
				});
				flag=true;
				return;
		 	}
		 		
		 	if(girlNum!=""){
			 	if(!r.test(girlNum)){
					layer.tips('请输入正整数', $(this).siblings(".girlNum"+cla).find("input"), {
							tipsMore: true,
							tips:3				
					});
					flag=true;
					return;
				}
		 		str += ("-女"+girlNum+"；")
		 	}else{
		 		layer.tips('请输入值', $(this).siblings(".girlNum"+cla).find("input"), {
						tipsMore: true,
						tips:3				
				});
				flag=true;
				return;
		 	}
		 	if(allNum-boyNum<girlNum){
		 		layer.tips('总数不能小于其余两者的和', $(this).siblings(".allNum"+cla).find("input"), {
						tipsMore: true,
						tips:3				
				});
				flag=true;
				return;
		 	}
	 	}
	 	if(str!=$(this).text()){
	 		content += str;
	 	}	
 	})
 	if(flag){
 		return;
 	}
 	if(content!=''){
   		content = "<span>"+content+"</span>";
 	}
 	content += '<a href="javascript:void(0)" onclick="alertList2()">修改</a>';
 	$(".stuNum-list").html(content);
	layer.closeAll();
});

</script>

