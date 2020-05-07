<script type="text/javascript" src="${request.contextPath}/static/ueditor/third-party/webuploader/webuploader.min.js" />
<script type="text/javascript" src="${request.contextPath}/static/js/validate.js"/>
<script type="text/javascript" src="${request.contextPath}/newstusys/sch/student/studentValidate.js"/>
<script type="text/javascript">

    $(function(){
        $("#searchDiv").hide();
        $("#backA").show();
        
        //初始化日期控件
        var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
        initCalendarData("#birthdayDiv",".date-picker",viewContent);
        initCalendarData("#birthdayDiv1",".date-picker",viewContent);
        initCalendarData("#birthdayDiv2",".date-picker",viewContent);
        var viewContent2={
            'format' : 'yyyy-mm',
            'startView' : '3',
            'minView' : '3'
        };
        initCalendarData(".month",".date-picker",viewContent2);
        initCalendarData("#toSchoolDate",".date-picker",viewContent2);
        
        //初始化单选控件
        var country = $('#country');
        $(country).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
        var nativePlace = $('#nativePlace');
        $(nativePlace).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
        var registerPlace = $('#registerPlace');
        $(registerPlace).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
        //初始化单选控件 registerPlace
        var classId = $('#classId');
        $(classId).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
        
        var culture1 = $('#familyTempList[1].culture');
        $(culture1).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
        
        var culture2 = $('#familyTempList[2].culture');
        $(culture2).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
        
        
    });

    // 校验
    function validator(){
        if(!checkValue('#stuForm')){
            return false;
        }
        var cid = $('#classId').val();
        if(cid == ''){
        	layerTipMsg(false,"","班级不能为空！");
            return false;
        }
        var flag1 = compare();
        if(!flag1){
            return false;
        }

		if(document.getElementById("postalcode")){
			if (checkPostalCode(document.getElementById("postalcode")) == false){
				return false;
			}
		}

        //电话号码校验
        if(document.getElementById("familyMobile").value != ''){
            if (!checkMobilePhone(document.getElementById("familyMobile"))){
                return false;
            }
        }
        
        //电话号码校验
        if(document.getElementById("mobilePhone0").value != ''){
            if (!checkMobilePhone(document.getElementById("mobilePhone0"))){
                return false;
            }
        }
        
        //电话号码校验
        if(document.getElementById("mobilePhone1").value != ''){
            if (!checkMobilePhone(document.getElementById("mobilePhone1"))){
                return false;
            }
        }
        
        //电话号码校验
        if(document.getElementById("mobilePhone2").value != ''){
            if (!checkMobilePhone(document.getElementById("mobilePhone2"))){
                return false;
            }
        }

        var idtype = document.getElementById("identitycardType");
        var idcard = document.getElementById("identityCard");
        if(!checkTypeIdentityCard(idcard,idtype)){
            return false;
        }
        if(!verifyLetterAndNum(document.getElementById("identityCard1"),'证件号')){
        	return false;
        }
        if(!verifyLetterAndNum(document.getElementById("identityCard2"),'证件号')){
        	return false;
        }

        return true;
    }

    // 保存
    var isSubmit = false;
    function save(){
        isSubmit = true;
        if (validator() == false){
            isSubmit = false;
            return ;
        }


        $("#btnSaveAll").attr("class", "abtn-unable");
        var options = {
            url : "${request.contextPath}/newstusys/sch/student/saveStudent.action",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"",jsonO.msg);
                    isSubmit=false;
                    return;
                }else{
                    layer.msg("保存成功");
                    cancelOperate();
                }
                isSubmit=false;
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        };
        $("#stuForm").ajaxSubmit(options);
    }
    
    function cancelOperate() {
    	goBack();
        <#--var url="${request.contextPath}/newstusys/sch/student/studentadmin.action?classId=${classId!}&studentId=${studentId!}";
        $("#backA").hide();
        $(".model-div").load(url);-->
    }
    <#if studentResumeList?exists >
    var size=${studentResumeList?size};
    <#else>
    var size=0;
    </#if>
    function addNewResume(){


        var html ='<tr class="month resume_tr"  id="resume' +size +'" >\
                <input type="hidden" name="studentResumeList[' +size +'].id" value="">\
                <input type="hidden" name="studentResumeList[' +size +'].schid" value="">\
                <input type="hidden" name="studentResumeList[' +size +'].stuid" value="">\
                <td class="text-right" width="15%"><span style="color:red;">* </span>开始年月-结束年月:</td>\
        <td width="32%">\
            <div class="input-group">\
                <input type="text" vtype="data" class="form-control date-picker" nullable="false" name="studentResumeList[' +size +'].startdate" id="resumeStart' +size +'"  placeholder="开始年月" value="" >\
                    <span class="input-group-addon">\
                        <i class="fa fa-calendar"></i>\
                    </span>\
                    <span class="input-group-addon"><i class="fa fa-minus"></i></span>\
                    <input type="text" vtype="data" class="form-control date-picker" nullable="false" name="studentResumeList[' +size +'].enddate" id="resumeEnd' +size +'" placeholder="结束年月" value="" >\
                        <span class="input-group-addon">\
                            <i class="fa fa-calendar"></i>\
                        </span>\
            </div>\
        </td>\
        <td class="text-right" width="15%"><span style="color:red;">* </span>所在学校:</td>\
        <td><input type="text" value="" class="form-control" id="schoolName' +size +'" nullable="false" name="studentResumeList[' +size +'].schoolname" nullable="false" notNull="true"   type="text" maxlength="60" value=""></td>\
        <td width="8%"><a class="table-btn" onclick="deleteNewResume(' +size +',\'\' );" href="javascript:void(0);">删除</a></td>\
    </tr>';
        size =size+1;
        $("#resumeNew").before(html);
        var viewContent2={
            'format' : 'yyyy-mm',
            'startView' : '3',
            'minView' : '3'
        };
        initCalendarData(".month",".date-picker",viewContent2);

    }
    function deleteNewResume(index ,id){
        showConfirmMsg('确认删除？','提示',function(){
            var ii = layer.load();
            if(id != "" ){
                $.ajax({
                    url:'${request.contextPath}/newstusys/sch/student/studentResumeDelete',
                    data: {'id':id},
                    type:'post',
                    success:function(data) {
                        var jsonO = JSON.parse(data);
                        if(jsonO.success){
                            layer.closeAll();
                            layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
                            $("#resume"+index).remove();
                        }
                        else{
                            layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                        }
                        layer.close(ii);
                    },
                    error:function(XMLHttpRequest, textStatus, errorThrown){}
                });
            }else{
                $("#resume"+index).remove();
                layer.closeAll();
                layer.close(ii);
            }

        });
    }

    /**
     *  9-1、验证日期的前后关系（传入提示信息）
     *  elem1 前面的日期
     *  elem2 后面的日期
     *  msg  验证通不过时的提示信息
     */
    function checkAfterDateWithMsg(id1,id2){
        var elem1 =document.getElementById(id1);
        var elem2 = document.getElementById(id2)
        if(elem1.value!="" && elem2.value!=""){
            var date1 ;
            var date2 ;
            try{
                date1 = elem1.value.split('-');
                date2 =elem2.value.split('-');
            }catch(e){
                date1 = elem1.split('-');
                date2 = elem2.split('-');
            }

            for(var i=0;i<Math.min(date1.length,date2.length);i++){
                if(date1[i].length==1)
                    date1[i]='0'+date1[i];
                if(date2[i].length==1)
                    date2[i]='0'+date2[i];
            }

            for(var i=0;i<date1.length;i++){
                if(date1[i]>date2[i]){
                    //alert(msg);
                    layer.tips("开始年月要小于结束年月，请修改！", "#"+id1, {
                        tipsMore: true,
                        tips:3
                    });
                    return false;
                }
                if(date1[i]<date2[i]){
                    i=date1.length;
                }
            }
        }
        return true;
    }
    
    function compare(){
        var flag = true;
        $("tr.month").each(function(){
            var ids = new Array();
            $(this).find('input[vtype="data"]').each(function(){
                var id= $(this).attr("id");
                ids.push(id);
            });
            if(ids.length > 0){
                if(!checkAfterDateWithMsg(ids[0],ids[1])){
                    flag=false;
                };
            }
        });
        return flag;

    }
</script>
<form id="stuForm" >
    <input type="hidden" name="specId" value="${student.specId!}">
    <input type="hidden" name="specpointId" value="${student.specpointId!}">
    <input type="hidden" name="id" value="${student.id!}">
    <input type="hidden" name="schoolId" value="${student.schoolId!}">
    <input type="hidden" name="dirId" value="${student.dirId!}">
    <input type="hidden" name="oldFilePath" value="${student.filePath!}">
    <input type="hidden" name="isDeleted" value="${student.isDeleted?default('0')}">
    <input type="hidden" name="isLeaveSchool" value="${student.isLeaveSchool?default('0')}">
    <#if hw>
    <input type="hidden" name="unitiveCode" value="${student.unitiveCode?default('')}">
    </#if>
    <input type="hidden" name="eventSource" value="${student.eventSource?default('0')}">
    <input type="hidden" id="filePath" name="filePath" value="${student.filePath?default('')}">
    <input type="hidden" name="creationTime" value="${(student.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
    <input type="hidden" name="modifyTime" value="${(student.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
    <input type="hidden"  id="hasAddPic" name="hasAddPic" value="false">
	<table class="table table-bordered table-striped no-margin">

    <thead>
    <tr>
        <th colspan="4" class="text-center">学生信息</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="text-right"><span style="color:red;">* </span>姓名:</td>
        <td><input  name="studentName" id="studentName1" nullable="false" notNull="true" type="text" maxlength="30" value="${student.studentName?default('')}" class="form-control" ></td>
        <td rowspan="4" class="text-right">照片:</td>
        <td rowspan="4">
            <div class="form-horizontal">
                <div class="col-sm-10">
                    <div class="upload-img-container clearfix">
                        <div class="upload-img-item file-img-wrap  upload-img-new <#if !hasPic?default(false)>open<#else>hidden</#if> ">
                            <label class="upload-img" for="filePicker2"><span></span></label>
                        </div>
                        <div class="upload-img-item uploader-list <#if hasPic?default(false)>open<#else>hidden</#if> " style="width:130px;height:130px;">
                            <div id="file_id_0" class="file-item" style="width:130px;">
                                <div class="file-img-wrap">
                                <#if hasPic?default(false)><img width="130px;" height="130px;" src="${request.contextPath}/newstusys/sch/student/studentpic/show?studentId=${student.id!}&${random!}" alt="" layer-index="0"></#if>
                                </div>
                            </div>
                        </div>
                    </div>
                    <a id="filePicker2" class="btn btn-blue  <#if !hasPic?default(false)>open<#else>hidden</#if>  ">选择图片</a>

                    <a id="filePicker3" class="btn btn-blue  <#if hasPic?default(false)>open<#else>hidden</#if>  ">重新上传</a>
                    <p class="tip tip-grey">支持JPG、GIF、PNG格式，每张图片不超过5M</p>
                </div>

            </div>
        </td>
    </tr>
    <tr>
        <td class="text-right">曾用名:</td>
        <td><input type="text" name="oldName" id="oldName" maxlength="30" value="${student.oldName?default('')}" value="" class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right"><span style="color:red;">* </span>班级:</td>
        <td>
            <select name="classId" id="classId" notNull="true" nullable="false" class="form-control">
            <#if classList?exists && (classList?size>0)>
                <option value="">---请选择---</option>
                <#list classList as item>
                    <option value="${item.id!}" <#if item.id == student.classId?default('')>selected</#if> >${item.classNameDynamic!}</option>
                </#list>
            <#else>
                <option value="">---请选择---</option>
            </#if>
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right">学号:</td>
        <td><input type="text" name="studentCode" id="studentCode" nullable="true" maxlength="30" value="${student.studentCode?default('')}"  class="form-control"></td>
    </tr>
    <#if !hw>
    <tr>
        <td class="text-right">学籍号:</td>
        <td><input type="text" name="unitiveCode" id="unitiveCode"  maxlength="30" value="${student.unitiveCode?default('')}"  class="form-control"></td>
        <td class="text-right"></td>
        <td></td>
    </tr>
    </#if>
    <tr>
        <td class="text-right">班内编号:</td>
        <td><input type="text" name="classInnerCode" id="classInnerCode" maxlength="3" vtype="int" max="999" min="1"  value="${student.classInnerCode?default('')}" class="form-control"></td>
        <td class="text-right">籍贯:</td>
        <td>
            <select name="nativePlace" id="nativePlace" class="form-control">
            <#if regionList?exists && (regionList?size>0)>
                <option value="">---请选择---</option>
                <#list regionList as region>
                    <option value="${region.fullCode!}" <#if region.fullCode == student.nativePlace?default('') >selected</#if>>${region.fullName!}</option>
                </#list>
            <#else>
                <option value="">---请选择---</option>
            </#if>
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right">性别:</td>
        <td>
            <select name="sex" id="sex" nullable="true" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-XB", (student.sex?string("number"))?default("1"), "")}
            </select>
        </td>
        <td class="text-right">出生日期:</td>
        <td>
            <div id="birthdayDiv"  class="input-group">
                <input type="text" vtype="data"  class="form-control date-picker" nullable="true" name="birthday" id="birthday" placeholder="出生日期" value="${(student.birthday?string('yyyy-MM-dd'))?default('')}">
            </div>
        </td>
    </tr>
    <tr>
        <td class="text-right">民族:</td>
        <td>
            <select name="nation"  id="nation" class="form-control" >
            ${mcodeSetting.getMcodeSelect("DM-MZ", student.nation?default(''), "")}
            </select>
        </td>
        <td class="text-right">政治面貌:</td>
        <td>
            <select name="background"  id="background" class="form-control" >
            ${mcodeSetting.getMcodeSelect("DM-ZZMM", student.background?default(''), "")}
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right">国籍:</td>
        <td>
            <select name="country"  id="country" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-COUNTRY", student.country?default(''), "")}
            </select>
        </td>
        <td class="text-right">港澳台侨外:</td>
        <td>
            <select name="compatriots"  id="compatriots" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-GATQ", (student.compatriots?string("number"))?default(''), "")}
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right"><span style="color:red;">* </span>证件类型:</td>
        <td>
            <select name="identitycardType" nullable="false" notNull="true"   id="identitycardType" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-SFZJLX", student.identitycardType?default(''), "")}
            </select>
        </td>
        <td class="text-right"><span style="color:red;">* </span>证件号:</td>
        <td><input type="text" name="identityCard" id="identityCard" nullable="false" notNull="true" maxlength="30" value="${student.identityCard?default('')}" class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">学生类别:</td>
        <td>
            <select name="studentType"  id="studentType"  notnull="false" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-XSLB", student.studentType?default(''), "1")}
            </select>
        </td>
        <td class="text-right">一卡通卡号:</td>
        <td><input type="text" name="cardNumber" id="cardNumber" maxlength="30" value="${student.cardNumber?default('')}" class="form-control"></td>
    </tr>
    <tr  >
        <td class="text-right">原毕业学校:</td>
        <td><input type="text" name="oldSchoolName" id="oldSchoolName" maxlength="30" value="${student.oldSchoolName?default('')}"  class="form-control"></td>
        <td class="text-right">入学年月:</td>
        <td >
            <div id="toSchoolDate" class="input-group">
                <input type="text" vtype="data"  class="form-control date-picker" name="toSchoolDate"  placeholder="入学年月" value="${(student.toSchoolDate?string('yyyy-MM'))?default('')}" >
													<span class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</span>
            </div>
        </td>
    </tr>
    <tr>
        <td class="text-right">户籍省县:</td>
        <td>
            <select name="registerPlace" id="registerPlace" class="form-control">
            <#if regionList?exists && (regionList?size>0)>
                <option value="">---请选择---</option>
                <#list regionList as region>
                    <option value="${region.fullCode!}" <#if region.fullCode == student.registerPlace?default('') >selected</#if>>  ${region.fullName!}</option>
                </#list>
            <#else>
                <option value="">---请选择---</option>
            </#if>
            </select>
        </td>
        <td class="text-right">户籍镇/街:</td>
        <td><input name="registerStreet" id="registerStreet"  type="text" maxlength="100" value="${student.registerStreet?default('')}" class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">家庭地址:</td>
        <td><input name="homeAddress" id="homeAddress"   type="text" maxlength="60" value="${student.homeAddress?default('')}" class="form-control"></td>
        <td class="text-right">家庭邮编:</td>
        <td><input name="postalcode" id="postalcode"  type="text" maxlength="6" value="${student.postalcode?default('')}" class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">家庭电话:</td>
        <td><input name="familyMobile" id="familyMobile" type="text" maxlength="20" value="${student.familyMobile?default('')}"class="form-control"></td>
        <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
        <td class="text-right">特长爱好:</td>
        <td colspan="3"><textarea name="strong" id="strong"  maxlength="1000" value="${student.strong?default('')}" rows="5" class="form-control">${student.strong?default('')}</textarea></td>
    </tr>
    <tr>
        <td class="text-right">获奖情况:</td>
        <td colspan="3"><textarea name="rewardRemark" id="rewardRemark" maxlength="1000" value="${student.rewardRemark?default('')}"  rows="5" class="form-control">${student.rewardRemark?default('')}</textarea></td>
    </tr>
    </tbody>
</table>

<table class="table table-bordered table-striped no-margin">
    <thead>
    <tr>
        <input type="hidden" name="familyTempList[0].id" value="${(family1.id)!}">
        <input type="hidden" name="familyTempList[0].schoolId" value="${(family1.schoolId)!}">
        <input type="hidden" name="familyTempList[0].studentId" value="${(family1.studentId)!}">
        <input type="hidden" name="familyTempList[0].isGuardian" value="1">
        <input type="hidden" name="familyTempList[0].eventSource" value="${(family1.eventSource)!}">
        <input type="hidden" name="familyTempList[0].isDeleted" value="${(family1.isDeleted)?default('0')}">
        <input type="hidden" name="familyTempList[0].openUserStatus" value="${(family1.openUserStatus)?default(1)}">
        <input type="hidden" name="familyTempList[0].creationTime" value="${(family1.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
        <input type="hidden" name="familyTempList[0].modifyTime" value="${(family1.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
        <th colspan="4" class="text-center">监护人信息</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="text-right" width="15%">监护人姓名:</td>
        <td width="32%"><input type="text" msgName="监护人姓名" name="familyTempList[0].realName" placeholder="监护人若是父母，可只维护关系"  id="realName0" maxlength="30" value="${(family1.realName)!}"   class="form-control"></td>
        <td class="text-right" width="15%">监护人与学生关系:</td>
        <td>
            <select name="familyTempList[0].relation" id="relation0" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-GX", family1.relation?default(''), "1")}
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right">监护人联系电话:</td>
        <td><input type="text" msgName="监护人联系电话" maxlength="20" id="mobilePhone0" name="familyTempList[0].mobilePhone" value="${(family1.mobilePhone)!}"  class="form-control"></td>
        <td colspan="2">&nbsp;</td>
    </tr>
    </tbody>
</table>
<table class="table table-bordered table-striped no-margin">
    <thead>
    <tr>
        <input type="hidden" name="familyTempList[1].id" value="${(family2.id)!}">
        <input type="hidden" name="familyTempList[1].schoolId" value="${(family2.schoolId)!}">
        <input type="hidden" name="familyTempList[1].studentId" value="${(family2.studentId)!}">
        <input type="hidden" name="familyTempList[1].eventSource" value="${(family2.eventSource)!}">
        <input type="hidden" name="familyTempList[1].isDeleted" value="${(family2.isDeleted)?default(0)}">
        <input type="hidden" name="familyTempList[1].openUserStatus" value="${(family2.openUserStatus)?default(1)}">
        <input type="hidden" name="familyTempList[1].relation" value="51">
        <input type="hidden" name="familyTempList[1].isGuardian" value="0">
        <input type="hidden" name="familyTempList[1].creationTime" value="${(family3.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
        <input type="hidden" name="familyTempList[1].modifyTime" value="${(family3.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
        <th colspan="4" class="text-center">父亲信息</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="text-right" width="15%">父亲姓名:</td>
        <td width="32%"><input type="text" name="familyTempList[1].realName" id="realName1" msgName="家庭成员或监护人姓名" maxlength="30" value="${(family2.realName)!}" class="form-control"></td>
        <td class="text-right" width="15%">手机号码:</td>
        <td><input type="text" id="mobilePhone1" name="familyTempList[1].mobilePhone" maxlength="20" value="${(family2.mobilePhone)!}" class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">政治面貌:</td>
        <td>
            <select name="familyTempList[1].politicalStatus"  id="familyTempList[1].politicalStatus" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-ZZMM", family2.politicalStatus?default(''), "1")}
            </select>
        </td>
        <td class="text-right">文化程度:</td>
        <td>
            <select name="familyTempList[1].culture" id="familyTempList[1].culture" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-WHCD", family2.culture?default(''), "1")}
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right">工作单位:</td>
        <td><input type="text" name="familyTempList[1].company" id="company1" notNull="false" maxlength="60" value="${(family2.company)!}"  class="form-control"></td>
        <td class="text-right">职务:</td>
        <td><input type="text" name="familyTempList[1].duty" id="duty1" maxlength="20" value="${(family2.duty)!}"  class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">证件类型:</td>
        <td>
            <select name="familyTempList[1].identitycardType" id="identitycardType1" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-SFZJLX", family2.identitycardType?default(''), "")}
            </select>
        </td>
        <td class="text-right">证件号:</td>
        <td><input type="text" name="familyTempList[1].identityCard" id="identityCard1" notNull="false" maxlength="30" value="${(family2.identityCard)!}"  class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">国籍:</td>
        <td>
            <select name="familyTempList[1].country"  id="country1" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-COUNTRY", family2.country?default(''), "")}
            </select>
        </td>
        <td class="text-right">出生日期:</td>
        <td>
           <div id="birthdayDiv1"  class="input-group">
              <input type="text" vtype="data"  class="form-control date-picker" nullable="true" name="familyTempList[1].birthday" id="birthday1" placeholder="出生日期" value="${(family2.birthday?string('yyyy-MM-dd'))?default('')}">
           </div>
        </td>
    </tr>
    </tbody>
</table>
<table class="table table-bordered table-striped no-margin">
    <thead>
    <tr>
        <input type="hidden" name="familyTempList[2].id" value="${(family3.id)!}">
        <input type="hidden" name="familyTempList[2].schoolId" value="${(family3.schoolId)!}">
        <input type="hidden" name="familyTempList[2].studentId" value="${(family3.studentId)!}">
        <input type="hidden" name="familyTempList[2].eventSource" value="${(family3.eventSource)!}">
        <input type="hidden" name="familyTempList[2].isDeleted" value="${(family3.isDeleted)?default(0)}">
        <input type="hidden" name="familyTempList[2].openUserStatus" value="${(family3.openUserStatus)?default(1)}">
        <input type="hidden" name="familyTempList[2].relation" value="52">
        <input type="hidden" name="familyTempList[2].isGuardian" value="0">
        <input type="hidden" name="familyTempList[2].creationTime" value="${(family3.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
        <input type="hidden" name="familyTempList[2].modifyTime" value="${(family3.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
        <th colspan="4" class="text-center">母亲信息</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="text-right" width="15%">母亲姓名:</td>
        <td width="32%"><input type="text" name="familyTempList[2].realName" id="realName2"  msgName="家庭成员或监护人姓名" maxlength="30" value="${(family3.realName)!}"  class="form-control"></td>
        <td class="text-right" width="15%">手机号码:</td>
        <td>
            <input type="text"  class="form-control" maxlength="20" id="mobilePhone2" name="familyTempList[2].mobilePhone" value="${(family3.mobilePhone)!}" ></td>
    </tr>
    <tr>
        <td class="text-right">政治面貌:</td>
        <td>
            <select name="familyTempList[2].politicalStatus"  id="politicalStatus2" class="form-control" notnull="false" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-ZZMM", family3.politicalStatus?default(''), "1")}
            </select>
        </td>
        <td class="text-right">文化程度:</td>
        <td>
            <select name="familyTempList[2].culture"  id="culture2" class="form-control" notnull="false"  class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-WHCD", family3.culture?default(''), "1")}
            </select>
        </td>
    </tr>
    <tr>
        <td class="text-right">工作单位:</td>
        <td><input type="text" name="familyTempList[2].company" id="company2" notNull="false" maxlength="60" value="${(family3.company)!}" class="form-control"></td>
        <td class="text-right">职务:</td>
        <td><input type="text" name="familyTempList[2].duty" id="duty2" maxlength="20" value="${(family3.duty)!}"  class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">证件类型:</td>
        <td>
            <select name="familyTempList[2].identitycardType" id="identitycardType2" class="form-control">
            ${mcodeSetting.getMcodeSelect("DM-SFZJLX", family3.identitycardType?default(''), "")}
            </select>
        </td>
        <td class="text-right">证件号:</td>
        <td><input type="text" name="familyTempList[2].identityCard" id="identityCard2" notNull="false" maxlength="30" value="${(family3.identityCard)!}"  class="form-control"></td>
    </tr>
    <tr>
        <td class="text-right">国籍:</td>
        <td>
            <select name="familyTempList[2].country"  id="country2" class="form-control" notnull="false" >
            ${mcodeSetting.getMcodeSelect("DM-COUNTRY", family3.country?default(''), "")}
            </select>
        </td>
        <td class="text-right">出生日期:</td>
        <td>
            <div id="birthdayDiv2"  class="input-group">
             <input type="text" vtype="data"  class="form-control date-picker" nullable="true" name="familyTempList[2].birthday" id="birthday2" placeholder="出生日期" value="${(family3.birthday?string('yyyy-MM-dd'))?default('')}">
            </div>
        </td>
    </tr>
    </tbody>
</table>
<table class="table table-bordered table-striped no-margin table-resume">
    <thead>
    <tr>
        <th colspan="5" class="text-center">简历信息</th>
    </tr>
    </thead>
    <tbody>
    <#if studentResumeList?exists && studentResumeList?size gt 0>
        <#list studentResumeList as resume>
        <tr class="month resume_tr"  id="resume${resume_index}" >
            <input type="hidden" name="studentResumeList[${resume_index}].id" value="${(resume.id)!}">
            <input type="hidden" name="studentResumeList[${resume_index}].schid" value="${(resume.schid)!}">
            <input type="hidden" name="studentResumeList[${resume_index}].stuid" value="${(resume.stuid)!}">
            <td class="text-right" width="15%"><span style="color:red;">* </span>开始年月-结束年月:</td>
            <td width="32%">
                <div class="input-group">
                    <input type="text" vtype="data" class="form-control date-picker" nullable="false" name="studentResumeList[${resume_index}].startdate" id="resumeStart${resume_index}" placeholder="开始年月" value="${(resume.startdate?string('yyyy-MM'))?if_exists}" >
													<span class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</span>
                    <span class="input-group-addon"><i class="fa fa-minus"></i></span>
                    <input type="text" vtype="data" class="form-control date-picker" nullable="false" name="studentResumeList[${resume_index}].enddate" id="resumeEnd${resume_index}"  placeholder="结束年月" value="${(resume.enddate?string('yyyy-MM'))?if_exists}" >
													<span class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</span>
                </div>
            </td>
            <td class="text-right" width="15%"><span style="color:red;">* </span>所在学校:</td>
            <td><input type="text" id="schoolName${resume_index}"  class="form-control" name="studentResumeList[${resume_index}].schoolname" nullable="false" notNull="true" type="text" maxlength="60" value="${resume.schoolname?default('')}"></td>
            <td width="8%"><a class="table-btn" onclick="deleteNewResume('${resume_index}' , '${resume.id!}');" href="javascript:void(0);">删除</a></td>
        </tr>
        </#list>
    </#if>

    <tr id="resumeNew">
        <td class="text-center" colspan="5"><a class="table-btn" onclick="addNewResume();"  href="javascript:void(0);">+新增简历</a></td>
    </tr>
    </tbody>
</table>

<div class="base-bg-gray text-center">
    <a class="btn btn-blue" onclick="save();" href="javascript:;">保存</a>
    <a class="btn btn-white" onclick="cancelOperate();" href="javascript:;">返回</a>
</div>
</form>
<script type="text/javascript">
    <#if nowSection?default('') != ''>
    $('#sec-${nowSection!}').click();
    </#if>
    <#if msg?default('') != ''>
    layerTipMsg(false,"提示","${msg!}");
    </#if>

    <#-- ==========================pic start====================================================-->
    var uploader = WebUploader.create({

        // 选完文件后，是否自动上传。
        auto: true,
        // swf文件路径
        swf: '${request.contextPath}/static/webuploader/Uploader.swf',
        // 文件接收服务端。
        server: '${request.contextPath}/newstusys/sch/student/stuPicSave.action',

        // 文件数量
        fileNumLimit : 1,
        // 单个文件大小 5M
        fileSingleSizeLimit : 5*1024*1024,

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: {
            id:'#filePicker2',
            innerHTML : '选择图片',
            multiple:false
        },

        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,png',
            mimeTypes: 'image/gif,image/jpg,image/jpeg,image/png'
        },

        // 缩略图
        thumb : {
            width: 120,
            height: 120,
            // 图片质量，只有type为`image/jpeg`的时候才有效。
            quality: 70,

            // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
            allowMagnify: false,

            // 是否允许裁剪。
            crop: true
        }
    });

    <#if hasPic?default(false)>
    uploader.addButton({
        id: '#filePicker3',
        innerHTML : '重新上传'
    });
    </#if>

    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        var $li = $(
                        '<div id="' + file.id + '" class="file-item">' +
                        '<div class="file-img-wrap">\
                            <div class="file-panel">\
        		</div>\
        		<img>\
        	</div>' +
                        '</div>'
                ),
                $img = $li.find('img');

        // $list为容器jQuery实例
        var $list = $('.uploader-list');
        var fileList = $list.find('.file-item');
        if(fileList && fileList.length>0){
            fileList.each(function(){
                $(this).remove();
            });
        }

        $list.addClass('open');
        $list.append( $li );
        $list.removeClass('hidden').addClass('open');
        $('.upload-img-new').removeClass('open').addClass('hidden');
        $('#filePicker2').removeClass('open').addClass('hidden');
        $('#filePicker3').removeClass('hidden').addClass('open');
    <#if !hasPic?default(false)>
        // 添加“添加文件”的按钮，
        uploader.addButton({
            id: '#filePicker3',
            innerHTML : '重新上传'
        });
    </#if>

        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // thumbnailWidth x thumbnailHeight 为 100 x 100
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }

            $img.attr( 'src', src );
        }, 120, 120 );
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
                $percent = $li.find('.progress span');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<p class="progress"><span></span></p>')
                    .appendTo( $li )
                    .find('span');
        }

        $percent.css( 'width', percentage * 100 + '%' );
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on( 'uploadSuccess', function( file ,data ) {
        if(data.success){
            $("#filePath").val(data.businessValue);
            $("#hasAddPic").val("true");
        }
        $( '#'+file.id ).addClass('upload-state-done');
    });

    // 文件上传失败，显示上传出错。
    uploader.on( 'uploadError', function( file ) {
        var $li = $( '#'+file.id ),
                $error = $li.find('div.error');

        // 避免重复创建
        if ( !$error.length ) {
            $error = $('<div class="error"></div>').appendTo( $li );
        }

        $error.text('上传失败');
    });

    // 完成上传完了，成功或者失败，先删除进度条。
    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').remove();
        uploader.reset();
    });

    <#-- ==========================pic end====================================================-->
</script>