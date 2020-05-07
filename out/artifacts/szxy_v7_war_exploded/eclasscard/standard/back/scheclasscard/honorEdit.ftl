<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="box box-default">
	<div class="box-body">
		<div class="tab-content">
			<div id="cc" class="tab-pane active" role="tabpanel">
				<div class="form-horizontal">
					<form id="honorForm">
					<input type="hidden" name="id" value="${eccHonor.id!}" >
					<input type="hidden" name="createTime" value="${eccHonor.createTime!}" >
					<input type="hidden" name="type" value="1" >
					<input type="hidden" name="status" value="1" >
					<div class="form-group">
						<label class="col-sm-2 control-title no-padding-right">颁布荣誉&nbsp;</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">荣誉名称：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<#if canEdit>
								<input type="text" name="title" id="title" autocomplete="off" class="form-control js-limit-word" maxlength="10" value="${eccHonor.title!}">
								<span class="input-group-addon">0/10</span>
								<#else>
								<label style="font-weight: normal;"><span class="lbl"></span>${eccHonor.title!}</label>
								</#if>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">班牌展示时间：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<#if canEdit>
								<input type="text" id="beginTime" name="beginTime" autocomplete="off" class="form-control datetimepicker" placeholder="开始时间" value="${eccHonor.beginTime!}">
								<span class="input-group-addon">
									<i class="fa fa-minus"></i>
								</span>
								<input type="text" id="endTime" name="endTime" autocomplete="off" class="form-control datetimepicker" placeholder="结束时间" value="${eccHonor.endTime!}">
								<#else>
								<label style="font-weight: normal;"><span class="lbl">${eccHonor.beginTime!}至${eccHonor.endTime!}</span></label>
								</#if>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">获奖日期：</label>
						<div class="col-sm-4">
							<div class="input-group">
								<#if canEdit>
								<input type="text" id="awardTime" name="awardTime" autocomplete="off" class="form-control awardtimepicker" placeholder="获奖日期" <#if eccHonor.awardTime?exists>value="${(eccHonor.awardTime?string("yyyy-MM-dd"))?if_exists}"</#if>>
								<#else>
								<label style="font-weight: normal;"><span class="lbl">${(eccHonor.awardTime?string("yyyy-MM-dd"))?if_exists}</span></label>
								</#if>
							</div>
						</div>
					</div>
					<#if canEdit>
					<div class="form-group">
						<@popup.selectMoreClass clickId="className" id="classId" name="className" handler="">
							<label class="col-sm-2 control-label no-padding-right">获奖班级：</label>
							<div class="col-sm-4">
								<input type="text" autocomplete="off" class="form-control" id="className" name="className" value="${eccHonor.className!}"/>
			         			<input type="hidden" id="classId" name="classId" value="${eccHonor.classId!}"/>
							</div>
						</@popup.selectMoreClass>
					</div>
					<#else>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">获奖班级：</label>
						<div class="col-sm-4">
							<label style="font-weight: normal;"><span class="lbl">${eccHonor.className!}</span></label>
						</div>
					</div>
					</#if>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">荣誉样式：</label>
						<div class="col-sm-8">
							<#if canEdit>
							<input type="hidden" id="style" name="style" value="${eccHonor.style!'1'}" >
							<ul class="honor-list clearfix" id="selectStyle">
								<li <#if !eccHonor.style?exists || (eccHonor.style?exists && eccHonor.style==1)> class="selected" </#if> data-action="select" value="1">
									<div >
										<a href="javascript:void(0);"><img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-flag.png" alt=""></a>
									</div>
									<p>锦旗</p>
								</li>
								<li <#if eccHonor.style?exists && eccHonor.style==2> class="selected" </#if> data-action="select" value="2">
									<div>
										<a href="javascript:void(0);"><img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-cup.png" alt=""></a>
									</div>
									<p>奖杯</p>
								</li>
								<li <#if eccHonor.style?exists && eccHonor.style==3> class="selected" </#if> data-action="select" value="3">
									<div>
										<a href="javascript:void(0);"><img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-medal.png" alt=""></a>
									</div>
									<p>勋章</p>
								</li>
							</ul>
							<#else>
								<ul class="honor-list clearfix" id="selectStyle">
								<li>
								<#if eccHonor.style==1>
									<div >
										<img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-flag.png" alt="">
									</div>
									<p>锦旗</p>
								<#elseif eccHonor.style==2>
									<div>
										<img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-cup.png" alt="">
									</div>
									<p>奖杯</p>
								<#else>
									<div>
										<img width="100" height="75" src="${request.contextPath}/static/eclasscard/standard/show/images/honor-medal.png" alt="">
									</div>
									<p>勋章</p>
								</#if>
								</li>
								</ul>
							</#if>
						</div>
					</div>
					</form>
					<div class="form-group">
						<#if canEdit>
						<div class="col-sm-8 col-sm-offset-2">
							<button class="btn btn-long btn-blue" onclick="honorSave()">确定</button>
							<button class="btn btn-long btn-white" onclick="gobacktoList()">取消</button>
						</div>
						<#else>
						<div class="col-sm-8 col-sm-offset-2">
							<button class="btn btn-long btn-blue" onclick="gobacktoList()">返回</button>
						</div>
						</#if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	function gobacktoList(){
		backFolderIndex('2');
	}
	function dateInit(){
		if(date_timepicker &&date_timepicker!=null){
			date_timepicker.remove();
		}
		setTimeout(function(){
			var minDate = "${((.now)?string('yyyy-MM-dd HH:mm'))?if_exists}";
			date_timepicker = $('.datetimepicker').datetimepicker({
				startDate:minDate,
				language: 'zh-CN',
	    		format: 'yyyy-mm-dd hh:ii',
	    		autoclose: true
	    	})
			date_timepicker = $('.awardtimepicker').datetimepicker({
				language: 'zh-CN',
				minView: 'month',
	    		format: 'yyyy-mm-dd',
	    		autoclose: true
	    	})
			<#--返回-->
			showBreadBack(gobacktoList,true,"荣誉列表");
		},100);
	}
	$(function(){
		dateInit();
		<#if canEdit>
		var titleLength = $('#title').val().length;
		$('#title').next().text(titleLength +'/'+10);
		$('.js-limit-word').on('keyup',function(){
			var max = $(this).attr('maxlength');
			if(this.value.length+1 > max){
				layer.tips('最好在'+max+'个字数内哦', this, {
					tips: [1, '#000'],
					time: 2000
				})
			}
			$(this).next().text(this.value.length +'/'+max);
		});
		</#if>	
		
		$('[data-action=select]').on('click',function(e){
			e.preventDefault();
			if($(this).hasClass('disabled')){
				return false;
			}else if($(this).parent().hasClass('multiselect')){
				$(this).toggleClass('selected');
			}else{
				$(this).addClass('selected').siblings().removeClass('selected');
			}
		});
		
		$("#selectStyle li").on("click", function() {
        	var i = $(this).val();
        	$("#style").val(i);
        })
	});
	
	var isSubmit=false;
	function honorSave(){
		if(isSubmit){
       		return;
    	}
    	
    	var title = $("#title").val();
    	if(!title||title==''){
    		layerTipMsgWarn("荣誉名称","不可为空");
			return;
    	}else{
    		if(title.length>10){
    			layerTipMsgWarn("荣誉名称","长度不可超过10字");
				return;
    		}
    	}
    	
    	var beginTime = $("#beginTime").val();
    	var endTime = $("#endTime").val();
    	if(!beginTime||beginTime==''||!endTime||endTime==''){
    		layerTipMsgWarn("展示时间","请选择完整的展示时间");
			return;
    	}else{
	    	if(beginTime>=endTime){
    			layerTipMsgWarn("展示时间","结束时间应大于开始时间");
				return;
			}
	   	}
	   	
	   	var awardTime = $("#awardTime").val();
	   	if(!awardTime||awardTime==''){
	   		layerTipMsgWarn("荣获时间","请选择完整的荣获时间");
			return;
	   	}
	   	
	    var classId = $("#classId").val();
	    if(!classId||classId==''){
	    	layerTipMsgWarn("颁布对象","不可为空");
			return;
	    }
	   	
		isSubmit = true;
		var options = {
			url : "${request.contextPath}/eclasscard/standard/honor/save",
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
		 			setTimeout(function(){
    					gobacktoList();
    				},500);
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#honorForm").ajaxSubmit(options);
	}
</script>