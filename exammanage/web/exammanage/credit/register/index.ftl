<div class="row">
  <div class="col-xs-12">
    <div class="box box-default">
      <div class="box-body clearfix">
        <div class="credit-classify">
          <div class="credit-classify-item">
            <span>学年：</span>
            <select class="form-control"  id="acadyear" name="acadyear" style="width:150px;display: inline-block;" onchange="doSearch();">
            <#if acadyearList?exists && (acadyearList?size>0)>
                <#list acadyearList as item>
                     <option value="${item!}" <#if acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                </#list>
             </#if>
            </select>
          </div>
          <div class="credit-classify-item">
            <span>学期：</span>
            <select class="form-control" id="semester" name="semester" style="width:150px;display: inline-block;" onchange="doSearch();">
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
            </select>
          </div>
        </div>
		<#if grades?exists&& (grades?size > 0)>
		<#list grades as grade>
        <div class="credit-item">
          <div class="credit-item-title">${grade.gradeName!}</div>
          <div class="row">
            <div class="col-sm-6">
              <div class="credit-item-body">
                <div class="credit-minitem-title">
                  <div class="row">
                    <div class="col-sm-6">日常表现登记</div>
                    <div class="col-sm-6 text-right">
                      <a href="javascript:" class="btn btn-link btn-info credit-text" onclick="doAllImport('${grade.id!}','1')">导入</a>
                      <i class="credit-minitem-line"></i>
                      <button class="btn btn-link btn-info credit-text" onclick="toDailyPage('${grade.id!}')">详情</button>
                    </div>
                  </div>
                </div>
                <div class="progress credit-bar">
                  <div id="dailybar_${grade.id!}" class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>
                </div>
                <div class="row">
                  <div class="col-sm-6">登记率</div>
                  <div class="col-sm-6 text-right"><span id="daily_${grade.id!}">0</span>%</div>
                </div>
              </div>
            </div>
            <div class="col-sm-6">
              <div class="credit-item-body">
                <div class="credit-minitem-title">
                  <div class="row">
                    <div class="col-sm-6">成绩登记</div>
                    <div class="col-sm-6 text-right">
                      <button class="btn btn-link btn-info credit-text js-new" onclick="moduleSet('${grade.id!}')">设置模块考试</button>
                      <i class="credit-minitem-line"></i>
                      <a href="javascript:" class="btn btn-link btn-info credit-text" onclick="doAllImport('${grade.id!}','2')">导入</a>
                      <i class="credit-minitem-line"></i>
                      <button class="btn btn-link btn-info credit-text"  onclick="toExamPage('${grade.id!}')">详情 </button>
                    </div>
                  </div>
                </div>
                <div class="progress credit-bar">
                  <div id="exambar_${grade.id!}" class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>
                </div>
                <div class="row">
                  <div class="col-sm-6">登记率</div>
                  <div class="col-sm-6 text-right"><span id="exam_${grade.id!}">0</span>%</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- tab切换结束 -->
        </#list>
        <#else>
		<div class="credit-nocontent">暂无数据...</div>
		</#if>
      </div>
    </div>
  </div>
  <!-- /.col -->
</div>
<!-- /.row -->
<script>

function doAllImport(gradeId,type){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var str = '?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&type="+type;
	var url='${request.contextPath}/exammanage/credit/import/register/head'+str;
	url=encodeURI(encodeURI(url));
	$('#model-div-37097').load(url);
}

function getNum(gradeId,type){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	$.ajax({
		url:"${request.contextPath}/exammanage/credit/getNum?acadyear="+acadyear+"&semester="+semester,
		data:{'gradeId':gradeId,'type':type},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
			if(type == '1'){
				$("#daily_"+gradeId).html(jsonO.num);
				$("#dailybar_"+gradeId).attr('style','width: '+jsonO.num+'%;');
			}else{
				$("#exam_"+gradeId).html(jsonO.num);
				$("#exambar_"+gradeId).attr('style','width: '+jsonO.num+'%;');
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function doSearch(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/exammanage/credit/register?acadyear='+acadyear+'&semester='+semester;
	$('#model-div-37097').load(url);
}
function toDailyPage(gradeId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/exammanage/credit/register/toDailyPage?acadyear='+acadyear+'&semester='+semester+'&gradeId='+gradeId;
	$('#model-div-37097').load(url);
}
function toExamPage(gradeId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/exammanage/credit/register/toExamPage?acadyear='+acadyear+'&semester='+semester+'&gradeId='+gradeId;
	$('#model-div-37097').load(url);
}

function moduleSet(gradeId){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
   	var url = '${request.contextPath}/exammanage/credit/register/exam/moduleSet?acadyear='+acadyear+'&semester='+semester+'&gradeId='+gradeId;
    indexDiv = layerDivUrl(url,{title: "模块成绩设置",width:450,height:200});
}
<#if grades?exists&& (grades?size > 0)>
	<#list grades as grade>
	<#list 1..2 as i>
		getNum('${grade.id!}','${i}');
                  </#list>
	</#list>
</#if>
</script>
          