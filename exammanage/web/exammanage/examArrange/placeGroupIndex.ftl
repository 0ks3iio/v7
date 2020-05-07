<!-- 手动分配 -->
<div class="layer layer-distribute">
    <div class="layer-content">
        <p>
            <span class="mr20"><span class="color-999">共可容纳考生：</span>${arrangeStuNum!}</span>
            <span class="mr20"><span class="color-999">参考人数：</span>${arrangeNum?default(0)}</span>
            <span class="mr20"><span class="color-999">已选考场：</span>${arrangePlaceNum!}</span>
            <span><span class="color-999">待容纳人数：</span>${noArrangeNum?default(0)}</span>
        </p>
        <form id="emplaceForm1">
            <input type="hidden" name="examId" value="${examId!}"/>
            <input type="hidden" name="groupId" value="${groupId!}"/>
            <div style="max-height: 350px;overflow-y:scroll;">
            <div class="table-container-body">
                <table class="table table-bordered table-striped table-hover no-margin">
                    <thead>
                    <tr>
                        <th style="width:5%">
                            <label class="pos-rel"><input type="checkbox" id="placeAll" class="wp"  value="" onchange="placeAllSelect()">
                                <span class="lbl" style="font-weight:bold;">全选</span></label>
                        </th>
                        <th class="">考试场地编号</th>
                        <th class="">考试场地</th>
                        <th class="">所属教学楼</th>
                        <th class="">可容纳人数</th>
                    </tr>
                    </thead>
                    <tbody>
					<#if (emPlaceList?exists && emPlaceList?size>0)>
						<#list emPlaceList as item>
                        <tr>
                            <td>
								<#if item.canCheck?default("0")=="1">
                                    <label class="pos-rel">
                                        <input name="placeId" type="checkbox" class="wp" value="${item.id!}" <#if item.hasCheck?default("0") == "1">checked</#if>>
                                        <span class="lbl"></span>
                                    </label>
								</#if>
                            </td>
                            <td class="">${item.examPlaceCode!}</td>
                            <td class="">${item.placeName!}</td>
                            <td class="">${item.buildName!}</td>
                            <td class="">${item.count?default(0)}</td>
                        </tr>
						</#list>
					</#if>
                    </tbody>
                </table>
                <div>
                </div>
        </form>
    </div>
</div>
<script>

function placeAllSelect(){
	if($("#placeAll").is(':checked')){
		$('input:checkbox[name=placeId]').each(function(i){
			$(this).prop('checked',true);
		});
	}else{
		$('input:checkbox[name=placeId]').each(function(i){
			$(this).prop('checked',false);
		});
	}
}

<#--var isSubmit=false;-->
<#--$("#js-save").on("click", function(){-->
	<#--if(isSubmit){-->
        <#--return;-->
	<#--}-->
	<#--isSubmit=true;-->
	<#--var ff=false;-->
	<#--$(this).addClass("disabled");-->
	<#--var options = {-->
		<#--url : "${request.contextPath}/exammanage/examArrange/placeArrange/groupPlaceSave",-->
		<#--dataType : 'json',-->
		<#--success : function(data){-->
 			<#--var jsonO = data;-->
	 		<#--if(!jsonO.success){-->
	 			<#--layerTipMsg(jsonO.success,"保存失败",jsonO.msg);-->
	 			<#--$("#js-save").removeClass("disabled");-->
	 			<#--isSubmit=false;-->
	 			<#--return;-->
	 		<#--}else{-->
	 			<#--layer.closeAll();-->
			  	<#--layer.msg(jsonO.msg, {-->
					<#--offset: 't',-->
					<#--time: 2000-->
				<#--});-->
				<#--itemShowList('8');-->
				<#--isSubmit=false;-->
				<#--$("#js-save").removeClass("disabled");-->
			<#--}-->
		<#--},-->
		<#--clearForm : false,-->
		<#--resetForm : false,-->
		<#--type : 'post',-->
		<#--error:function(XMLHttpRequest, textStatus, errorThrown){alert(111);}//请求出错-->
	<#--};-->

		<#--$("#emplaceForm1").ajaxSubmit(options);-->
 <#--});-->
</script>
													