<#assign zeroSubId = stack.findValue('@net.zdsoft.basedata.constant.BaseConstants@ZERO_GUID')/>
<#if index == '2'>
<div class="filter" id="searchType">
	<div class="filter-item">
		<span class="filter-name">年份：</span>
        <div class="filter-content">
            <select name="year" id="year" class="form-control" onchange="searchEndList()" style="width:135px">
                <#list minYear..maxYear as item>
                    <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
                </#list>			                       
            </select>
        </div>
	</div>
	<div class="filter-item">
		<span class="filter-name">类型：</span>
        <div class="filter-content">
            <select name="type" id="type" class="form-control" onchange="searchEndList()" style="width:135px">
                <option value="0" <#if '${type!}'=='0'>selected="selected"</#if>>考试</option>	
	            <option value="1" <#if '${type!}'=='1'>selected="selected"</#if>>培训</option>		                       
            </select>
        </div>
	</div>		
</div>	
</#if>	
<div id="a1" class="tab-pane active">
<#if teaexamInfoList?exists && teaexamInfoList?size gt 0>
    <#list teaexamInfoList as exam>
	<div class="<#if !(exam_index==0)>mt50</#if><#if exam.infoType==1> pos-rel</#if>">
	    <#if index=='1'>
		<p>
			<span class="font-16">${exam.examName!}</span>
			<span class="color-grey ml20 mr20">|</span>
			<span class="font-16 color-grey">报名时间：${exam.registerBegin?string('yyyy-MM-dd')!}  至  ${exam.registerEnd?string('yyyy-MM-dd')!}</span>
			<#if exam.infoType==1>
				<span class="color-grey ml20 mr20">|</span>
				<#assign a = 0>
				<#if registerInfoList?exists && registerInfoList?size gt 0>
				    <#list registerInfoList as register>
				        <#if register.examId == exam.id>
				            <#if register.status == 1>
				               <#if index=='1'>    
				                  <span><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${exam.id!}','${zeroSubId!}','${register.id!}','0');">取消报名</a></span>						               
				                  <span class="label-extra label-extra-green">审核中</span>
                               <#else>
                                  <span><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></span>
                               </#if>
				     
				            <#elseif register.status == 2>
				                <span class="color-blue mt16 font-16">报名成功</span>
				            <#elseif register.status == -1>		
				                <#if index=='1'>				                
				                <span><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${exam.id!}','${zeroSubId!}','${register.id!}','1');">重新报名</a><span class="color-red ml10"><i class="fa fa-exclamation-circle color-yellow"></i> ${register.remark!}</span></span>
								<span class="label-extra label-extra-red">未通过</span>
				                <#else>
				                      <span><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></span>
								      <span class="label-extra label-extra-red">未通过</span>
				                </#if>
				            </#if>
				            <#assign a = a + 1>
				        </#if>						        
				    </#list>
				</#if>
				<#if a == 0>
				     <#if index=='1'>   
				     <span><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${exam.id!}','${zeroSubId!}','','1');">我要报名</a></span>
				     <#else>
				     <span><a class="btn btn-white btn-sm disabled" href="#">已结束</a></span>
				     </#if>
				</#if>
			</#if>
		</p>
		<#else>
		    <#if exam.infoType==0>
		    <div class="filter">
				<div class="filter-item">
					<a class="btn btn-white" href="javascript:void(0);" onClick="examCardEdit('${exam.id!}');">预览准考证</a>
				</div>
			</div>
			</#if>
		    <p class="text-center font-20">${exam.examName!}</p>
			<p class="text-center mb20"><span class="mr20">报名时间：${exam.registerBegin?string('yyyy-MM-dd')!}  至  ${exam.registerEnd?string('yyyy-MM-dd')!}</span>
			<#if exam.infoType==1>
				<#assign a = 0>
				<#if registerInfoList?exists && registerInfoList?size gt 0>
				    <#list registerInfoList as register>
				        <#if register.examId == exam.id>
				            <#if register.status == 1>
                                  <span><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></span>
				            <#elseif register.status == 2>
				                <span class="color-blue mt16 font-16">报名成功</span>
				            <#elseif register.status == -1>		
			                      <span><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></span>
							      <span class="label-extra label-extra-red">未通过</span>
				            </#if>
				            <#assign a = a + 1>
				        </#if>						        
				    </#list>
				</#if>
				<#if a == 0>
				     <span><a class="btn btn-white btn-sm disabled" href="#">已结束</a></span>
				</#if>
			</#if>
			</p>
		</#if>
		<div class="row">
		<#if subjectList?exists && subjectList?size gt 0>
		    <#list subjectList as sub>
		    <#if sub.examId == exam.id>
			<div class="col-sm-4"> 
				<div class="box-default box-primary mb10">
					<div class="box-body pos-rel">
						<p class="font-16"><b class="mr20">${sub.subjectName!}</b>
						<#if sub.section == 1>
						    小学
						<#elseif sub.section == 0>
						    学前
						<#elseif sub.section == 2>
						    初中
						<#else>
						    高中
						</#if>
						</p>
						<p class="font-16 color-grey"><i class="fa fa-calendar"></i> ${sub.startTime?string('yyyy-MM-dd HH:mm')}-${sub.endTime?string('HH:mm')}</p>
						
						<#assign a = 0>
						<#if registerInfoList?exists && registerInfoList?size gt 0>
						    <#list registerInfoList as register>
						        <#if register.examId == exam.id && register.subjectInfoId == sub.id>
						            <#if register.status == 1>
						               <#if index=='1'>    
						                  <div><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${exam.id!}','${sub.id!}','${register.id!}','0');">取消报名</a></div>						               
						                  <span class="label-extra label-extra-green">审核中</span>
                                       <#else>
                                          <div><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></div>
                                       </#if>
						     
						            <#elseif register.status == 2>
						                <div class="color-blue mt16 font-16">报名成功</div>
						            <#elseif register.status == -1>		
						                <#if index=='1'>				                
						                <div><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${exam.id!}','${sub.id!}','${register.id!}','1');">重新报名</a><span class="color-red ml10"><i class="fa fa-exclamation-circle color-yellow"></i> ${register.remark!}</span></div>
										<span class="label-extra label-extra-red">未通过</span>
						                <#else>
						                      <div><a class="btn btn-white btn-sm disabled" href="#">审核中(已结束)</a></div>
										      <span class="label-extra label-extra-red">未通过</span>
						                </#if>
						            </#if>
						            <#assign a = a + 1>
						        </#if>						        
						    </#list>
						</#if>
						<#if a == 0>
						     <#if index=='1'>   
						     <div><a class="btn btn-blue btn-sm" href="#" onClick="doRegister('${exam.id!}','${sub.id!}','','1');">我要报名</a></div>
						     <#else>
						     <div><a class="btn btn-white btn-sm disabled" href="#">已结束</a></div>
						     </#if>
						</#if>
						
					</div>
				</div>
		   </div>		
		   </#if>
		   </#list>
		</#if>   
		</div>
		<#if exam.infoType == 1>
		<div class="explain-default explain-blue color-grey no-margin">
			<div>培训项目：</div>
			<div>${exam.trainItems!}</div>
		</div>
		</#if>
		<div class="explain-default explain-blue color-grey no-margin">
			<div><#if exam.infoType==0>考试说明<#else>培训要求</#if>：</div>
			<div>${exam.description!}</div>
		</div>
	</div>
	</#list>
	<#else>
			<tr>
				<td  align="center">暂无数据</td>
			</tr>
	</#if>
</div>
<div class="layer layer-see"></div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
var isSubmit=false;
function doRegister(examId,subId,id,status){
    if(isSubmit){
    	isSubmit = true;
		return;
	}
    var msg = "";
    if(status == '0'){
        msg = "取消报名";
    }else if(status == '1'){
        msg = "报名";
    }
    var index = layer.confirm("确定"+msg+"吗？", {
			btn: ["确定", "取消"]
		}, function(){
		    $.ajax({
		        url:"${request.contextPath}/teaexam/registerInfo/doRegister",
		        data:{examId:examId,subId:subId,id:id,status:status},
		        dataType : 'json',
		        success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"报名失败！",data.msg);
		 		      layer.closeAll();
		 		      layerTipMsg(data.success,"报名失败！",data.msg);
		 		      return;
		 	       }else{
		 		      layer.closeAll();
				      layerTipMsg(data.success,msg+"成功！",data.msg);
                      var url = "${request.contextPath}/teaexam/registerInfo/beginRegister?index=1";
                      $('#bmTblDiv').load(url);
    		       }
		        }
	        });
	})

}

function searchEndList(){
    var acadyear = $('#year').val();
    var semester = $('#type').val();
    var url = "${request.contextPath}/teaexam/registerInfo/beginRegister?index=2&year="+acadyear+"&type="+semester;
    $('#bmTblDiv').load(url);
}

function examCardEdit(examId){
   var url = "${request.contextPath}/teaexam/siteSet/query/examCardEdit?examId="+examId;
   $(".layer-see").load(url,function() {
		layerShow();
	});
}

function layerShow(){
    layer.open({
		type: 1,
		shadow: 0.5,
		title: false,
		area: '620px',
		btn: ['打印准考证'],
		yes: function(index){
			printExamCard();
		},
		content: $('.layer-see')
	});
}

function printExamCard(){
    var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
    if (LODOP==undefined || LODOP==null) {
			return;
		}
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_HTM("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".layer-see")));
	LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
	LODOP.PREVIEW();//打印预览
}
</script>