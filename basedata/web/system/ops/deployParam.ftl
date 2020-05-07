<!--运维部署必须参数页面 from common/tabHome.ftl-->
<div role="tabpanel">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name"><#if !fromSystemIni?default(false)>OptionCode或者</#if>iniid：</span>
				<div class="filter-content">
					<input class="form-control" type="text" id="deployInputCode" value=""/>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" id="searchBtn">查找</button>
			</div>
		</div>
		<div class="box box-graybg">
			<div class="box-graybg-title"><b>注意事项</b></div>
			<#if !fromSystemIni?default(false)>
			<p>
				☞ Deploy必填参数（不一定必须填写，重要的参数）包含base_sys_option的参数和sys_option参数
			</p>
			<p>
				☞ 填写passport_second_url之前应当确保index_second_url等eis相关的内网地址已经输入（初始化passportClient需要用到）
			</p>
			<p>
				☞ 填写member_url（个人中心地址之前），应当确保passport_url已经填写完成（调用passport_service初始化member和sysCooperator需要）
			</p>
			</#if>
			<p>
				☞ 数据更新完毕请及时刷新redis和memcache (redis可输入 http://xx.xx.xx:80/system/ops/refresh/command 其中command可为【'flushAll'或'flushDb'】)
			</p>
			<p>
				☞ 搜索框可输入optionCode或者iniId进行搜索(不区分大小写),输入code回车即可
			</p>
		</div>
		<table class="table table-outline">
			<thead>
			<tr>
				<th class="text-right" width="10%">名称</th>
				<th class="text-right" width="20%">参数</th>
				<th class="text-center" width="40%">设置</th>
				<th class="text-left" width="30%">说明</th>
			</tr>
			</thead>
			<tbody>
            <#if optionVos?exists && optionVos?size gt 0> <#list optionVos as option>
            <tr id="${option.code?default("noCode")}" class="deployTr">
				<td class="text-right">${option.name!}<#if option.ini == 1>(sys_option)</#if></td>
				<td class="text-right">${option.code!}</td>
                <#if (option.valueType)?? && option.valueType==0>
                    <td class="text-center td-lock">
                    <span class="input-icon">
                      <input type="text" value="" class="form-control" data-ini="${option.ini}" data-code="${option.code}">
                      <span class="txt-val">${option.value!}</span>
                      <i class="fa fa-pencil"></i>
                    </span>
					</td>
				<#elseif (option.valueType)?? && option.valueType==1>
                    <td class="text-center">
						${option.value!}
						<label class="switch-label">
							<input name="switch-field-1" class="wp wp-switch" type="checkbox" data-ini="${option.ini}" data-code="${option.code}" <#if option.value?default("") == "1">checked</#if>>
							<span class="lbl"></span>
						</label>
					</td>
				</#if>
				<td class="text-left">${option.description!}</td>
				<td style="display: none;">${option.ini}</td>
			</tr>
			</#list> </#if>
			</tbody>
		</table>
</div>
<script>
	$(document).ready(function () {
		function lockSave() {
			$('.td-edit').find('.txt-val').text($('.td-edit').find('.form-control').val());
			$('.td-lock').removeClass('td-edit');
		}
		;
		$('.td-lock i.fa').click(function(e) {
			e.stopPropagation();
			lockSave();
			var txt = $(this).siblings('.txt-val').text();
			$(this).parents('.td-lock').addClass('td-edit').find('.form-control').val(txt);
		});
		$(document).click(function(event) {
			var eo = $(event.target);
			if (!eo.hasClass('form-control') && !eo.parents('.td-lock').length) {
				lockSave();
			}
		});
		$('.td-lock input.form-control').on("blur",{condition:"inputType"},updateValue);
		$('.wp-switch').on("click",{condition:"checkType"},updateValue);
		$('#searchBtn').bind("click",searchCodeOrIniid);

		function updateValue(event){
			var condition = event.data.condition;
			var ini = $(this).data("ini");
			var code = $(this).data("code");
			var val="";
			if(condition=="inputType"){
				val = $(this).val();
			}
			if(condition=="checkType"){
				val =$(this).prop('checked')?"1":"0";
			}
			$.ajax({
				url:"${request.contextPath}/system/ops/modifyOption",
				data:{
					'ini':ini,
					'code':code,
					'value':val
				},
				dataType: "json",
				success:function(result){
					layer.alert(result.msg);
				}
			});
		}

		$('#deployInputCode').bind('change',function () {
			searchCodeOrIniid();
		})

		function searchCodeOrIniid() {
		    var code = $('#deployInputCode').val();
			$('.deployTr').each(function () {
				var id = $(this).attr('id')
				var s = id.toLowerCase().search(code.toLowerCase());
				if (s>=0) {
				    $(this).css("display","")
				} else {
					$(this).css("display","none")
				}
			})
		}
	})
</script>