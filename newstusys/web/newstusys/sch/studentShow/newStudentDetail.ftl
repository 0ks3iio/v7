<script type="text/javascript" src="${request.contextPath}/static/ueditor/third-party/webuploader/webuploader.min.js" />
<script type="text/javascript" src="${request.contextPath}/newstusys/sch/student/studentValidate.js"/>
<script type="text/javascript">
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
    <input type="hidden" name="unitiveCode" value="${student.unitiveCode?default('')}">
    <input type="hidden" name="eventSource" value="${student.eventSource?default('0')}">
    <input type="hidden" id="filePath" name="filePath" value="${student.filePath?default('')}">
    <input type="hidden" name="creationTime" value="${(student.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
    <input type="hidden" name="modifyTime" value="${(student.modifyTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
    <input type="hidden"  id="hasAddPic" name="hasAddPic" value="false">
    <#if !noBack?exists || noBack?default(false)==false>
    <div class="base-bg-gray text-left">
	    <a class="btn btn-white" onclick="cancelOperate();" href="javascript:void(0)">返回</a>
	</div>
    <#else>
    <div class="base-bg-gray text-right">
        <a class="btn btn-blue" onclick="pdfExport();" href="javascript:void(0)">导出PDF</a>
    </div>
	</#if>
    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th colspan="4" class="text-center">学生信息
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="text-right">姓名:</td>
            <td>${student.studentName?default('')}</td>
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

                    </div>

                </div>
            </td>
        </tr>
        <tr>
            <td class="text-right">曾用名:</td>
            <td>${student.oldName?default('')}</td>
        </tr>
        <tr>
            <td class="text-right">班级:</td>
            <td>
            <#if classList?exists && (classList?size>0)>
                <#list classList as item>
                    <#if item.id == student.classId?default('')>
                    ${item.classNameDynamic!}
                    <#break />
                    </#if>
                </#list>
            </#if>
            </td>
        </tr>
        <tr>
            <td class="text-right">学号:</td>
            <td>${student.studentCode?default('')}</td>
        </tr>
        <tr>
            <td class="text-right">班内编号:</td>
            <td>${student.classInnerCode?default('')}</td>
            <td class="text-right">籍贯:</td>
            <td>
                <#if regionList?exists && (regionList?size>0)>
                    <#list regionList as region>
                        <#if region.fullCode == student.nativePlace?default('') >
                        ${region.fullName!}
                        <#break />
                        </#if>
                    </#list>
                </#if>
            </td>
        </tr>
        <tr>
            <td class="text-right">性别:</td>
            <td>
            ${mcodeSetting.getMcode("DM-XB", (student.sex?string)?default("0"))}
            </td>
            <td class="text-right">出生日期:</td>
            <td>
            ${(student.birthday?string('yyyy-MM-dd'))?default('')}
            </td>
        </tr>
        <tr>
            <td class="text-right">民族:</td>
            <td> ${mcodeSetting.getMcode("DM-MZ", student.nation?default(''))}

            </td>
            <td class="text-right">政治面貌:</td>
            <td>${mcodeSetting.getMcode("DM-ZZMM", student.background?default(''))}
            </td>
        </tr>
        <tr>
            <td class="text-right">国籍:</td>
            <td>
            ${mcodeSetting.getMcode("DM-COUNTRY", student.country?default(''))}
            </td>
            <td class="text-right">港澳台侨外:</td>
            <td>
            ${mcodeSetting.getMcode("DM-GATQ", (student.compatriots?string("number"))?default(''))}
            </td>
        </tr>
        <tr>
            <td class="text-right">证件类型:</td>
            <td>
            ${mcodeSetting.getMcode("DM-SFZJLX", student.identitycardType?default(''))}
            </td>
            <td class="text-right">证件号:</td>
            <td>${student.identityCard?default('')}</td>
        </tr>
        <tr>
            <td class="text-right">学生类别:</td>
            <td>  ${mcodeSetting.getMcode("DM-XSLB", student.studentType?default(''))}

            </td>
            <td class="text-right">一卡通卡号:</td>
            <td>${student.cardNumber?default('')}</td>
        </tr>
        <tr  >
            <td class="text-right">原毕业学校:</td>
            <td>${student.oldSchoolName?default('')}</td>
            <td class="text-right">入学年月:</td>
            <td >${(student.toSchoolDate?string('yyyy-MM'))?default('')}
            </td>
        </tr>
        <tr>
            <td class="text-right">户籍省县:</td>
            <td>
                <#if regionList?exists && (regionList?size>0)>
                    <#list regionList as region>
                        <#if region.fullCode == student.registerPlace?default('') >
                             ${region.fullName!}
                             <#break />
                        </#if>
                    </#list>
                </#if>

            </td>
            <td class="text-right">户籍镇/街:</td>
            <td>${student.registerStreet?default('')}</td>
        </tr>
        <tr>
            <td class="text-right">家庭地址:</td>
            <td>${student.homeAddress?default('')}</td>
            <td class="text-right">家庭邮编:</td>
            <td>${student.postalcode?default('')}</td>
        </tr>
        <tr>
            <td class="text-right">家庭电话:</td>
            <td>${student.familyMobile?default('')}</td>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td class="text-right" width="15%">特长爱好:</td>
            <td colspan="3"  class="table-print-textarea" >${student.strong?default('')}</td>
        </tr>
        <tr>
            <td class="text-right" width="15%">获奖情况:</td>
            <td colspan="3"  class="table-print-textarea" >${student.rewardRemark?default('')}</td>
        </tr>
        </tbody>
    </table>

    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th colspan="4" class="text-center">监护人信息</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="text-right" width="15%">监护人姓名:</td>
            <td width="32%">${(family1.realName)!}</td>
            <td class="text-right" width="15%">监护人与学生关系:</td>
            <td> ${mcodeSetting.getMcode("DM-GX", family1.relation?default(''))}
            </td>
        </tr>
        <tr>
            <td class="text-right">监护人联系电话:</td>
            <td>${(family1.mobilePhone)!}</td>
            <td colspan="2">&nbsp;</td>
        </tr>
        </tbody>
    </table>
    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th colspan="4" class="text-center">父亲信息</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="text-right" width="15%">父亲姓名:</td>
            <td width="32%">${(family2.realName)!}</td>
            <td class="text-right" width="15%">手机号码:</td>
            <td>${(family2.mobilePhone)!}</td>
        </tr>
        <tr>
            <td class="text-right">政治面貌:</td>
            <td>${mcodeSetting.getMcode("DM-ZZMM", family2.politicalStatus?default(''))}
            </td>
            <td class="text-right">文化程度:</td>
            <td>${mcodeSetting.getMcode("DM-WHCD", family2.culture?default(''))}
            </td>
        </tr>
        <tr>
            <td class="text-right">工作单位:</td>
            <td>${(family2.company)!}</td>
            <td class="text-right">职务:</td>
            <td>${(family2.duty)!}</td>
        </tr>
        <tr>
            <td class="text-right">证件类型:</td>
            <td>${mcodeSetting.getMcode("DM-SFZJLX", family2.identitycardType?default(''))}</td>
            <td class="text-right">身份证号:</td>
            <td>${(family2.identityCard)!}</td>
        </tr>
        <tr>
            <td class="text-right">国籍:</td>
            <td>${mcodeSetting.getMcode("DM-COUNTRY", family2.country?default(''))}</td>
            <td class="text-right">出生日期:</td>
            <td>${(family2.birthday?string('yyyy-MM-dd'))?default('')}</td>
        </tr>
        </tbody>
    </table>
    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th colspan="4" class="text-center">母亲信息</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="text-right" width="15%">母亲姓名:</td>
            <td width="32%">${(family3.realName)!}</td>
            <td class="text-right" width="15%">手机号码:</td>
            <td>
            ${(family3.mobilePhone)!}</td>
        </tr>
        <tr>
            <td class="text-right">政治面貌:</td>
            <td>${mcodeSetting.getMcode("DM-ZZMM", family3.politicalStatus?default(''))}

            </td>
            <td class="text-right">文化程度:</td>
            <td> ${mcodeSetting.getMcode("DM-WHCD", family3.culture?default(''))}
            </td>
        </tr>
        <tr>
            <td class="text-right">工作单位:</td>
            <td>${(family3.company)!}</td>
            <td class="text-right">职务:</td>
            <td>${(family3.duty)!}</td>
        </tr>
        <tr>
            <td class="text-right">证件类型:</td>
            <td>${mcodeSetting.getMcode("DM-SFZJLX", family3.identitycardType?default(''))}</td>
            <td class="text-right">身份证号:</td>
            <td>${(family3.identityCard)!}</td>
        </tr>
        <tr>
            <td class="text-right">国籍:</td>
            <td>${mcodeSetting.getMcode("DM-COUNTRY", family3.country?default(''))}</td>
            <td class="text-right">出生日期:</td>
            <td>${(family3.birthday?string('yyyy-MM-dd'))?default('')}</td>
        </tr>
        </tbody>
    </table>
    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th colspan="5" class="text-center">简历信息</th>
        </tr>
        </thead>
        <tbody>
        <#if studentResumeList?exists && studentResumeList?size gt 0>
            <#list studentResumeList as resume>
            <tr class="month"  id="resume${resume_index}" >

                <td class="text-right" width="15%">开始年月-结束年月:</td>
                <td width="32%">${(resume.startdate?string('yyyy-MM'))?if_exists}-${(resume.enddate?string('yyyy-MM'))?if_exists}

                </td>
                <td class="text-right" width="15%">所在学校:</td>
                <td>${resume.schoolname?default('')}</td>
            </tr>
            </#list>
        </#if>


        </tbody>
    </table>


</form>
<#if !noBack?exists || noBack?default(false)==false>
<div class="base-bg-gray text-center">
    <a class="btn btn-white" onclick="cancelOperate();" href="javascript:void(0)">返回</a>
</div>
</#if>
<iframe style="display:none;" id="hiddenFrame" name="hiddenFrame"></iframe>
<script type="text/javascript" >
    function cancelOperate(){
		var tabIndex = $("#roleStuShow").find("li.active").attr("val");
		if(tabIndex == '3'){
			searchList();
		} else {
	        var classId = $('#classIdSearch').val();
	        var field = $("#field").val();
	        var keyWord = $("#keyWord").val();
	        var url="${request.contextPath}/newstusys/sch/student/studentShowList.action?classId="+classId+"&tabType=" + tabIndex + "&field="+field+"&keyWord="+keyWord;
	        $("#showList").load(url);
        }
    }

    function pdfExport(){
        var ii = layer.load();
        var downId=new Date().getTime();//以时间戳来区分每次下载
        var url="${request.contextPath}/newstusys/sch/student/stuPdfExport?stuId=${studentId!}&downId="+downId;
        document.getElementById('hiddenFrame').src=url;

        var interval=setInterval(function(){
            // var down=$.cookies.get("DS"+downId);
            // if(down==downId){
                layer.close(ii);
                // $.cookies.del("DS"+downId);
            // }
        },5000);
    }
</script>