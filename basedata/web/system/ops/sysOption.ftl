<div <#if fromDesktop?default(false)>role="tabpanel"<#else>class="box box-default"</#if>>
    <#if fromDesktop?default(false)><div class="box-body"></#if>
        <ul id="breadcrumb" class="clearfix" <#if fromDesktop?default(false)>style="display: none"</#if>>
            <li id="passwd" class="active"><span>重设密码</span></li>
            <li id="option" class="active"><span>参数设置</span></li>
            <li id="license"><span>系统激活</span></li>
        </ul>
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">OptionCode：</span>
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
			<p id="serverDescription">
				☞ 在搜索框可输入optionCode进行搜索(不区分大小写),输入code回车即可
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
            <#if options?exists && options?size gt 0> <#list options as option>
            <tr id="${option.optionCode?default("noCode")}" class="deployTr">
                <td class="text-right">${option.name!}</td>
                <td class="text-right">${option.optionCode!}</td>
                <#if (option.valueType)?? && option.valueType==0>
                    <td class="text-center td-lock">
                    <span class="input-icon">
                      <input type="text" value="" class="form-control" data-id="${option.id}" data-code="${option.optionCode}">
                      <span class="txt-val">${option.nowValue!}</span>
                      <i class="fa fa-pencil"></i>
                    </span>
                    </td>
                <#elseif (option.valueType)?? && option.valueType==1>
                    <td class="text-center">
                        <label class="switch-label">
                            <input name="switch-field-1" class="wp wp-switch" type="checkbox" data-id="${option.id}" data-code="${option.optionCode}" <#if option.nowValue == "1">checked</#if>>
                            <span class="lbl"></span>
                        </label>
                    </td>
                </#if>
                <td class="text-left">${option.description!}</td>
            </tr>
            </#list> </#if>
            </tbody>
        </table>

        <div class="text-right" <#if fromDesktop?default(false)>style="display: none"</#if>>
            <a class="btn btn-white" href="${request.contextPath}/system/ops/resetPassword">上一步</a> <a
                class="btn btn-blue" id="nextBtn">下一步</a>
        </div>
    <#if fromDesktop?default(false)></div></#if>
</div>
  <script>
      $(function() {
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
        $('#nextBtn').click(function() {
          layer.confirm('请确保参数修改正确后再激活系统！', {
            btn : [ '确定', '再改下' ]
          //按钮
          }, function() {
              $("#ops-container").load('${request.contextPath}/system/ops/init');
          });
        });
          $("#breadcrumb #passwd").click(function(){
              $(this).addClass("active");
              $("#breadcrumb #option").removeClass("active");
              $("#breadcrumb #license").removeClass("active");
              $("#ops-container").load("${request.contextPath}/system/ops/resetPassword");
          });
          $("#breadcrumb #option").click(function () {
              $(this).addClass("active");
              $("#breadcrumb #passwd").addClass("active");
              $("#breadcrumb #license").removeClass("active");
              $("#ops-container").load("${request.contextPath}/system/ops/sysOption");
          });
          $("#breadcrumb #license").click(function () {
              $(this).addClass("active");
              $("#breadcrumb #passwd").addClass("active");
              $("#breadcrumb #option").addClass("active");
              $("#ops-container").load("${request.contextPath}/system/ops/init");
          });
      });
      
      function updateValue(event){
          var condition = event.data.condition;
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
            'ini':0,
            'code':code,
            'value':val
          },
          dataType: "json",
          success:function(result){
            layer.alert(result.msg);
          }
        });
      }
      $('#deployInputCode').on("change",function () {
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
    </script>
