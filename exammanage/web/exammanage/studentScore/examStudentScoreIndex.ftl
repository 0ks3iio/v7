<div class="box box-default" id="itemShowDivId">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="" id="acadyear" class="form-control" onChange="doSearch('');">
			        <#if acadyearList?exists && acadyearList?size gt 0>
			            <#list acadyearList as item>
						    <option value="${item!}" <#if '${acadyear!}' == '${item!}'>selected</#if>>${item!}</option>
						</#list>
				    </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="" id="semester" class="form-control" onChange="doSearch('');">
						<option value="1" <#if '${semester!}' == '1'>selected</#if>>第一学期</option>
			            <option value="2" <#if '${semester!}' == '2'>selected</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">考试名称：</span>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="examId" onChange="doSearch()" style="width: 200px;">
				     <#if examList?exists && examList?size gt 0>
				        <#list examList as item>
				            <option value="${item.id!}" <#if '${item.id!}' == '${examId!}'>selected</#if>>${item.examName!}</option>
				        </#list>
				     <#else>
					    <option value="">---请选择---</option>
					 </#if>
				     </select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="" id="classId" class="form-control" onChange="doSearch('');">
					<#if clsList?exists && clsList?size gt 0>
			           <#list clsList as item>
			              <option value="${item.id!}" <#if '${classId!}' == '${item.id!}'>selected</#if>>${item.classNameDynamic!}</option>
			           </#list>
			        <#else>
			              <option value="">---请选择---</option>
			        </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<button class="btn btn-blue" onclick="doPrint()">打印</button>
				<button class="btn btn-white" onclick="doExport();">导出</button>
			</div>
		</div>
		<div class="clearfix">
			<div class="tree-wrap">
				<div class="tree-list" style="height:600px;margin-top: 0;">
					<#if studentList?exists && studentList?size gt 0>
						<#list studentList as item>
							<a href="javascript:doSearch('${item.id!}')" class="list-group-item <#if studentId?default("")==item.id>active</#if>">
								${item.studentName!}
							</a>
						</#list>
					<#else>
					该班级没有学生
					</#if>
				</div>
			</div>
			<div class="box-boder box" id="print">
			    <p class="text-center" style="font-size: 28px;">
					   <#--<img src="${request.contextPath}/scoremanage/images/logo1.png"/>-->${unitName!}${acadyear!}学年${semester!}学期学生成绩单
				</p>
               <table class="table table-print no-margin" style="height:800px;">
                  <tbody>
                     <tr>
                         <td width="25%" style="line-height: 1.4;border: 2px solid #333;"><b>班级：${className!}</b></td>
                         <td width="25%" style="line-height: 1.4;border: 2px solid #333;"><b>姓名：${studentName!}</b></td>
                         <td width="25%" style="line-height: 1.4;border: 2px solid #333;"><b>学号：${studentCode!}</b></td>
                         <td width="25%" style="line-height: 1.4;border: 2px solid #333;"><b>操行等第：${optionName!}</b></td>
                     </tr>
                     <tr>
                         <td colspan="4" style="line-height: 1.4;border: 2px solid #333;"><b>
                                                                                体检信息：
				             <#if dyStuHealthResultList?exists && dyStuHealthResultList?size gt 0>
				                <#list dyStuHealthResultList as item>
				                    ${item.itemName!}&nbsp;${item.itemResult!}${item.itemUnit!}；
				                </#list>
				             </#if>
                         </b></td>
                     </tr>
                     <tr>
                         <td style="line-height: 1.4;border: 2px solid #333;"><b>事假：天</b></td>
                         <td style="line-height: 1.4;border: 2px solid #333;"><b>病假：天</b></td>
                         <td style="line-height: 1.4;border: 2px solid #333;"><b>旷课：天</b></td>
                         <td style="line-height: 1.4;border: 2px solid #333;"><b></b></td>
                     </tr>
                     <tr style="vertical-align: top !important;">
                         <td colspan="2" style="height: 400px;vertical-align: top !important;line-height: 1.4;border: 2px solid #333;"><b>
                            <#if scoreInfoList?exists && scoreInfoList?size gt 0>
                                                                                                必修课：<br>
				                  <#list scoreInfoList as item>
				                      <#if item.inputType=='S'>
				                          ${item.subjectName!}：${item.score!}（考试），${item.toScore!}（总评）<br>
				                      <#else>
				                          <#if item.score=="A">
				                             ${item.subjectName!}：优秀（考试），${item.toScore!}（总评）<br>
				                          <#elseif item.score=="B">
				                             ${item.subjectName!}： 良好（考试），${item.toScore!}（总评）<br>
				                          <#elseif item.score=="C">
				                             ${item.subjectName!}： 中等（考试），${item.toScore!}（总评）<br>
				                          <#elseif item.score=="D">
				                             ${item.subjectName!}：合格（考试），${item.toScore!}（总评）<br>
				                          <#else>
				                              ${item.subjectName!}：不合格（考试），${item.toScore!}（总评）<br>
				                          </#if>
				                      </#if>
				                  </#list>
				             </#if>                                                      
                         </b></td>
                         <td colspan="2" style="height: 400px;vertical-align: top !important;line-height: 1.4;border: 2px solid #333;"><b>
     						<#if optionalScoreInfoList?exists && optionalScoreInfoList?size gt 0>
				                                               选修课：<br>
				                  <#list optionalScoreInfoList as item>
				                      <#if item.inputType=='S'>
				                          ${item.subjectName!}（${item.courseTypeName!}）：${item.toScore!}学分<br>
				                      <#else>
				                          <#if item.score=="A">
				                             ${item.subjectName!}：优秀（考试），${item.toScore!}学分<br>
				                          <#elseif item.score=="B">
				                             ${item.subjectName!}： 良好（考试），${item.toScore!}学分<br>
				                          <#elseif item.score=="C">
				                             ${item.subjectName!}： 中等（考试），${item.toScore!}学分<br>
				                          <#elseif item.score=="D">
				                             ${item.subjectName!}：合格（考试），${item.toScore!}学分<br>
				                          <#else>
				                              ${item.subjectName!}：不合格（考试），${item.toScore!}学分<br>
				                          </#if>
				                      </#if>
				                  </#list>
				             </#if>
                         </b></td>
                     </tr>
                     <tr>
                         <td class="text-center" style="height: 100px;line-height: 1.4;border: 2px solid #333;"><b>期末评语<br></b></td>
                         <td colspan="3" style="line-height: 1.4;border: 2px solid #333;"><b>${remark!}</b></td>
                     </tr>
                     <tr>
                         <td colspan="3" style="line-height: 1.4;border: 2px solid #333;"><b>备注：</b></td>
                         <td class="text-center" style="line-height: 1.4;border: 2px solid #333;"><b>${unitName!}教务处<br>${nowDate?string('yyyy-MM-dd')!}</b></td>
                     </tr>
                   </tbody>
                </table>
			</div>
		</div>
	</div>
</div>

<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
    function doExport(){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var classId=$("#classId").val();
        var examId=$("#examId").val();
        if(classId == ''){
            layerTipMsg(false,"提示!","请选择一个班级!");
            return;
        }
        var url="${request.contextPath}/exammanage/studentScore/doExport?acadyear="+acadyear+"&semester="+semester+"&classId="+classId+"&examId="+examId;
        document.location.href=url;

    }



    function doSearch(studentId){
        var acadyear=$("#acadyear").val();
        var semester=$("#semester").val();
        var classId=$("#classId").val();
        var examId=$("#examId").val();
        if(studentId==undefined && '${studentId!}'!=''){
            studentId = '${studentId!}';
        }
        var url="${request.contextPath}/exammanage/studentScore/searchStuScore?acadyear="+acadyear+"&semester="+semester+"&classId="+classId+"&studentId="+studentId+"&examId="+examId;
        $("#itemShowDivId").load(url);
    }
    //打印
    function doPrint(){
        var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
        if (LODOP==undefined || LODOP==null) {
            return;
        }
        //设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
        LODOP.ADD_PRINT_HTM("10mm","10mm","RightMargin:5mm","BottomMargin:0mm",getPrintContent($("#print")));
        LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//纵向打印
        LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
        LODOP.PREVIEW();//打印预览
    }
</script>
