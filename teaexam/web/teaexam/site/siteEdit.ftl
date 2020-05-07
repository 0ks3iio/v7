<form id="subForm">
			<div class="layer-content">
				<table width="100%">
				    <input type="hidden" name="id" class="form-control" value="${site.id!}">
				    <input type="hidden" name="creationTime" class="form-control" value="${site.creationTime!}">
					<tr>
						<td width="120"><p class="mb10">考点名称：</p></td>
						<td>
							<select vtype="selectOne" name="schoolId" id="schoolId" class="form-control mb10" nullable="false">
							<option value="">--请选择--</option>
							<#if units?exists && units?size gt 0>
								<#list units as uint>	
								    <option value="${uint.id!}-${uint.unionCode!}" <#if '${site.schoolId!}'=='${uint.id!}'>selected="selected"</#if>>${uint.unitName!}</option>
								</#list>						     
							</#if>
							</select>
						</td>
					</tr>					
					<tr>
						<td><p class="mb10">考场数量：</p></td>
						<td>
							<div class="form-number form-number-lg mb10">
					    		<input type="text" name="siteNum" id="siteNum" class="form-control" value="${site.siteNum!}" nullable="false">
					    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
					    		<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
					    	</div>
						</td>
					</tr>
					<tr>
						<td><p class="mb10">考场可容纳人数：</p></td>
						<td>
							<div class="form-number form-number-lg mb10">
					    		<input type="text" name="capacity" id="capacity" class="form-control" value="${site.capacity!}" nullable="false">
					    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
					    		<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
					    	</div>
						</td>
					</tr>
				</table>
			</div>
		</form>
<script>
$(function(){
    var classIdSearch = $('#schoolId');
	$(classIdSearch).chosen({
	width:'350px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
	});
	//数字输入框
	$('.form-number .form-control').keyup(function(){
		var num = /^\d{1,3}$/;
		if(!num.test($(this).val())){
			layer.msg('请输入不超过4位的数字！', {
				icon: 2,
				time: 1500,
				shade: 0.2
			});
			$(this).val('');
		};
	});
	$('.form-number > button').click(function(e){
		e.preventDefault();
		var $num = $(this).siblings('.form-control');
		var val = $num.val();
		if (!val ) val = 0;
		var num = parseInt(val);
		var step = $num.parent('.form-number').attr('data-step');
		if (step === undefined) {
			step = 1;
		} else{
			step = parseInt(step);
		}
		if ($(this).hasClass('form-number-add')) {
			num += step;
		} else{
			num -= step;
			if (num <= 0) num = 0;
		}
		var num2 = /^\d{1,3}$/;
		if(!num2.test(num)){
			layer.msg('请输入不超过4位的数字！', {
				icon: 2,
				time: 1500,
				shade: 0.2
			});
			num = "";
		};
		$num.val(num);
	});
})
</script>