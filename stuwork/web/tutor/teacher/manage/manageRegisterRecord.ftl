<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="layer-content">
	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">学年：</label>
			<div class="col-sm-9">
				<select name="searchAcadyear" id="searchAcadyear" class="form-control" <#if isSee?default(false)> disabled="true" </#if>>
					<#if acadyearList?exists && (acadyearList?size>0)>
	                    <#list acadyearList as item>
		                     <option value="${item!}"   <#if tutorRecordDetailed ?exists &&tutorRecordDetailed.acadyear==item> selected <#elseif semester.acadyear?default('a')==item?default('b')>selected</#if>  >${item!}  </option>
	                    </#list>
                    </#if>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">学期：</label>
			<div class="col-sm-9">
				<select id="searchSemester" name="searchSemester" class="form-control" <#if isSee?default(false)> disabled="true" </#if> >
				    <#if tutorRecordDetailed?exists &&tutorRecordDetailed.acadyear== (semester.semester?string) >
				     <option value="${semester.semester!}" selected  >${semester.semester!}  </option>
				    <#else>
					 ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
				    </#if>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">类型：</label>
			<div class="col-sm-9">
				<select name="layerRecordType" id="layerRecordType" class="form-control" onChange="doChange();"  <#if isSee?default(false) || isEdit?default(false)> disabled="true" </#if>>
				  <#if recordTypes?exists && (recordTypes?size>0)>
	                    <#list recordTypes as recordType>
		                     <option value="${recordType.thisId!}" <#if tutorRecordDetailed?exists &&tutorRecordDetailed.recordType==recordType.thisId> selected</#if> >${recordType.mcodeContent!}</option>
	                    </#list>
                  </#if>
				</select>
			</div>
		</div>
		<div class="form-group layerStudentIds" >
			<@popup.popup_div clickId="studentName" columnName="学生（多选）" dataUrl="${request.contextPath}/tutor/record/manage/popupData" id="studentIds" name="studentName" dataLevel="1" type="duoxuan" recentDataUrl="${request.contextPath}/tutor/record/manage/recentData" resourceUrl="${resourceUrl}" handler="" popupType="one">
				<label class="col-sm-3 control-label no-padding-right">选择对象：</label>
				<div class="col-sm-9">
					<input type="hidden" id="studentIds" name="studentIds"  class="form-control" value="<#if stuIds?exists>${stuIds!}</#if>"  >
					<input type="text" id="studentName" class="form-control" value="<#if stuNames?exists>${stuNames!}</#if>"  <#if isSee?default(false)> disabled="true" </#if> >
				</div>
			</@popup.popup_div>
		
		</div>
		<div class="form-group layerClassIds" style="display:none">   
			<label class="col-sm-3 control-label no-padding-right">行政班级：</label>
			<div class="col-sm-9">
				<select name="clazz" id="clazz" class="form-control" onChange="doChange();" <#if isSee?default(false)> disabled="true" </#if>>
				  <#if classList?exists && (classList?size>0)>
	                    <#list classList as clazz>
		                     <option value="${clazz.id!}"   <#if tutorRecordDetailed?exists && tutorRecordDetailed.classId?default('') != "" && tutorRecordDetailed.classId == clazz.id   >   selected  </#if> > ${clazz.classNameDynamic!}</option>
	                    </#list>
                  </#if>
				</select>
			</div>
		</div>
		<div class="form-group layerStuSee">
			<label class="col-sm-3 control-label no-padding-right">学生可见：</label>
			<div class="col-sm-9" id="stuShow">
				<label><input type="radio"  class="wp isShow" <#if isSee?default(false)> disabled="true" </#if>  value="${isStuShow!}" <#if tutorRecordDetailed?exists &&tutorRecordDetailed.isStudentShow==isStuShow>   checked="checked" name="stuShow"</#if>><span class="lbl"> 是</span></label>
				<label><input type="radio"  class="wp isShow" <#if isSee?default(false)> disabled="true" </#if>  value="${isNStuShow!}" <#if tutorRecordDetailed?exists &&tutorRecordDetailed.isStudentShow==isNStuShow>   checked="checked" name="stuShow"</#if>><span class="lbl" id="stuShowError"> 否</span></label>
			</div>
		</div>
		<div class="form-group layerFamSee">
			<label class="col-sm-3 control-label no-padding-right">家长可见：</label>
			<div class="col-sm-9" id= "famShow">
				<label><input type="radio"  class="wp isShow" <#if isSee?default(false)> disabled="true" </#if>  value="${isFamShow!}" <#if tutorRecordDetailed?exists &&tutorRecordDetailed.isFamilyShow==isFamShow>   checked="checked" name="famShow"</#if>><span class="lbl"> 是</span></label>
				<label><input type="radio"  class="wp isShow" <#if isSee?default(false)> disabled="true" </#if>  value="${isNFamShow!}" <#if tutorRecordDetailed?exists &&tutorRecordDetailed.isFamilyShow==isNFamShow>   checked="checked" name="famShow"</#if>><span class="lbl" id="famShowError"> 否</span></label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">内容：</label>
			<div class="col-sm-9">
				<div class="textarea-container">
				    <#if tutorRecordDetailed?exists>
	    			<textarea name="recordResult" id="recordResult" <#if isSee?default(false)> disabled="true" </#if> cols="30" rows="10" class="form-control js-limit-word" value = "${tutorRecordDetailed.recordResult!}">${tutorRecordDetailed.recordResult!} </textarea>
	    			<#else>
	    			<textarea name="recordResult" id="recordResult" <#if isSee?default(false)> disabled="true" </#if> cols="30" rows="10" class="form-control js-limit-word" ></textarea>
	    			</#if>
	    			<#if !isSee?default(false)>
	    			   <span style="right:15px;">1000</span>
	    			</#if>
	    		</div>
			</div>
		</div>
	</div>
	<script>
	   
	   $(function(){
	      <#if !isSee?default(false)>
			   var titleLength = $('#recordResult').val().length;
				$('#recordResult').next().text(titleLength +'/'+1000);
				$('.js-limit-word').on('keyup',function(){
					var max = 1000;
					$(this).next().text(this.value.length +'/'+max);
				})
		  </#if>
	        $("#recordResult").blur(function(){
	           isShow();
	        });
	        $("textarea").on("onpropertychange",function(){
	           isShow();
	        });
	        
	        doChange();
	   });
	    //进行条件检验
	    function isShow(){
	        var studentIds = $('#studentIds').val();
	        var stu=$('input:radio[name="stuShow"]:checked').val();
		    var fam=$('input:radio[name="famShow"]:checked').val();
		    var recordResult = $("textarea[name='recordResult']").val();
		    //去掉空格
		    recordResult=recordResult.replace(/\ +/g,"");
		    var recordType = $("#layerRecordType").val();
		    if(recordType == '06'){
		        if(clazz=='' || clazz== null){
		         layerError("#clazz","班级不能为空");
		         return isOk=false;
		        };
		    }
		    if(!(recordType == '02' || recordType == '08' || recordType == '06')){
				if(studentIds=='' || studentIds== null){
				     layerError("#studentName","学生不能为空");
				     return isOk=false;
				};
			    if(stu==null){
			         layerError("#stuShowError","学生是否可见不能为空");
			         return isOk=false;
			    };
			    if(fam==null){
			         layerError("#famShowError","家长是否可见不能为空");
			         return isOk=false;
			    };
			}
	        if(recordResult=='' || recordResult== null){
	          layerError("#recordResult","记录内容不能为空");
	          return isOk=false;
	        };
	        if(recordResult && recordResult !='' && recordResult.length >=1001){
	             layerError("#recordResult","内容 长度过长,不能超过1000！");
	             return isOk=false;
	        };
	        return isOk= true; 
	    }
	    
	    $(document).ready(function() {
			$("#stuShow .isShow").change(function() {
			    var checked = $(this).attr("checked")=="checked"?true:false;
	            if(checked){
	                $(this).attr("checked",false);
	                $(this).removeAttr("name");
	            }else{
	                $(this).attr("checked",true);
	                $(this).attr("name","stuShow");
	                $(this).parent().siblings().find('input').removeAttr("name");
	                $(this).parent().siblings().find('input').attr("checked",false);
	            }
	        });
	        $("#famShow .isShow").change(function() {
			    var checked = $(this).attr("checked")=="checked"?true:false;
	            if(checked){
	                $(this).attr("checked",false);
	                $(this).removeAttr("name");
	            }else{
	                $(this).attr("checked",true);
	                $(this).attr("name","famShow");
	                $(this).parent().siblings().find('input').removeAttr("name");
	                $(this).parent().siblings().find('input').attr("checked",false);
	            }
	        });
	     });
	    
	    function doChange(){
	       var recordType = $("#layerRecordType").val();
	       if(recordType == '02' || recordType == '08'){
	         $('.layerStudentIds').hide();
	         $('.layerClassIds').hide(); 
	         $('.layerStuSee').hide();
	         $('.layerFamSee').hide();
	       }else if(recordType == '06'){
	         $('.layerStudentIds').hide();
	         $('.layerClassIds').show(); 
	         $('.layerStuSee').hide();
	         $('.layerFamSee').hide(); 
	       }else{
	         $('.layerStudentIds').show();
	         $('.layerClassIds').hide(); 
	         $('.layerFamSee').show();
	         $('.layerStuSee').show();
	       }
	    }
	
	</script>
</div>



