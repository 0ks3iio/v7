<#if studentList?exists && (studentList?size gt 0) >
    <#list studentList as student>
    <div class="card-item">
        <div class="card-stu">
            <label class="card-checkbox">
                <input type="checkbox"  class="wp detail-box" value="${student.id!}">
                <span class="lbl"></span>
            </label>
            <#assign stuRel = 0 />
            <#if stuIntroductionMap?exists && stuIntroductionMap?size gt 0  >
                <#assign isHas = (stuIntroductionMap[student.id!])?exists />
            	<#if (stuIntroductionMap[student.id!])?exists>
            	<#assign intro = stuIntroductionMap[student.id!] />
            	</#if>
            	<#if isHas && intro?exists>
            		<#assign stuRel = intro.hasRelease?default(0) />	
            	</#if>
            </#if>
            <div class="card-stu-tools tools-btn">
                <a href="#" class="tools-btn" onclick="exportDoc('${student.id!}');">导出HTML</a>
                <a href="#" class="tools-btn" onclick="releaseDoc('${student.id!}',${stuRel});"><#if stuRel==0>发布给家长<#elseif stuRel==1>取消发布</#if></a>
            </div>
            <a href="javascript:;" class="item-detail">
                <img class="card-stu-img"
                     <#if isHas?default(false) &&  intro?exists && intro.imgPath?default('') != ''>
                             src="${intro.imgPath!}"
                     <#elseif student.sex?default(0) == 2 >
                     src="../images/user-female.png"
                     <#else>
                     src="../images/user-male.png"
                     </#if>
                      alt="">
                <span class="card-stu-name">${student.studentName!}</span>
                <input id="studentId" type="hidden" value="${student.id!}" >
            </a>
        </div>
    </div>
    </#list>
</#if>
<script type="text/javascript" >
    jQuery(document).ready(function () {
        $("#allSelect").click(function () {
                $("input.detail-box").each(function () {
                    $(this).prop("checked" ,true);
                })
            $(this).hide();
            $("#allCancel").show();
        });
        $("#allCancel").click(function () {
            $("input.detail-box").each(function () {
                $(this).prop("checked" ,false);
            })
            $(this).hide();
            $("#allSelect").show();
        })
        $(".item-detail").click(function () {
            var studentId = $(this).find("#studentId").val();
            var acadyear = $("#acadyear").val();
            var semester = $("#semester").val();
            <#--window.open("${request.contextPath}/mobile/open/studevelop/handbook/index?studentId=" + studentId + "&acadyear=" +acadyear + "&semester="+semester );-->
            window.open("${request.contextPath}/studevelop/devdoc/studentDetail?studentId=" + studentId + "&acadyear=" +acadyear + "&semester="+semester );
        })
        
    })

var tosIds='';
var release='';   
    function batchRelease(){
    	tosIds='';
    	release='1';
    	$('.detail-box').each(function(){
			if($(this).is(":checked")){
				if(tosIds != ''){
					tosIds+=',';
				}
				tosIds+=($(this).val());
			}
		});
		if(tosIds == ''){
			layerTipMsg(false,"提示","没有选择要操作的手册记录！");
			return;
		}
		showConfirmSuccess('确定要发布选中的手册吗？','确认',releaseAct);
    }
    
    function releaseDoc(sid,nowRelease){
    	tosIds=sid;
    	if(nowRelease==0){
    		release=1;
    	} else {
    		release=0
    	}
    	var msg = '发布';
    	if(release == 0){
    		msg = '取消发布';
    	}
    	showConfirmSuccess('确定要'+msg+'选中的手册吗？','确认',releaseAct);
    }
    
    function releaseAct(){
    	$.ajax({
    		url:"${request.contextPath}/studevelop/devdoc/release",
    		dataType:"json",
            type:"post",
            data:{"acadyear":"${acadyear!}","semester":"${semester!}","stuIds":tosIds ,"release":release},
            success:function(data){
                layer.closeAll();
                var jsonO = data;
                if(!jsonO.success){
                    showWarnMsg(jsonO.msg);
                    return;
                }else{
                    doSearch();
                }
            },
            error:function(XMLHttpRequest ,textStatus,errorThrown){}
    	});
    }
    
    function exportDoc(sid){
    	var sids = '';
    	if(!sid){
	    	$('.detail-box').each(function(){
				if($(this).is(":checked")){
					if(sids != ''){
						sids+=',';
					}
					sids+=($(this).val());
				}
			});
			if(sids == ''){
				layerTipMsg(false,"提示","没有选择要导出的学生手册记录！");
				return;
			}
    	} else {
    		sids = sid;
    	}
    	window.open("${request.contextPath}/studevelop/devdoc/export?acadyear=${acadyear!}&semester=${semester!}&stuIds="+sids);
    }
</script>
