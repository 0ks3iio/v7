
<script type="text/javascript" src="${request.contextPath}/static/webuploader/webuploader.js" />
<div id="myDiv">
    <form id="subForm" >
        <input type="hidden" name="createUserId" value="${famDearThreeInTwoStu.createUserId!}">
        <input type="hidden" name="id" id="id" value="${famDearThreeInTwoStu.id!}">
        <input type="hidden" name="unitId" value="${famDearThreeInTwoStu.unitId!}">
        <input type="hidden" name="createTime" value="${famDearThreeInTwoStu.createTime!}">
        <input type="hidden" name="teacherId" value="${famDearThreeInTwoStu.teacherId!}">
        
        <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
						<div class="box box-default">
							
							<div class="box-body">
								<h3 class="text-center mb20">“三进两联一交友”学生基本情况表</h3>
								<table class="table table-bordered no-margin text-center">
									<tbody>
										<tr>
											<td width="11%"><span style="color:red">*</span>姓名</td>
											<td width="11%">
												<input type="text" name="stuname" nullable="false" maxlength="200" id="stuname" class="form-control  " value="${famDearThreeInTwoStu.stuname!}" style="width:100%;">
											</td>
											<td width="11%"><span style="color:red">*</span>性别</td>
											<td width="11%">
												<select name="sex"  id="sex"  class="form-control" nullable="false" style="width:100%;">
                            						${mcodeSetting.getMcodeSelect("DM-XB", '${famDearThreeInTwoStu.sex!}', "1")}
                        						</select>
                        					</td>
											<td width="11%"><span style="color:red">*</span>民族</td>
											<td width="11%">
												<select name="nation"  id="nation" nullable="false" class="form-control" notnull="false" style="width:100%;">
                            						${mcodeSetting.getMcodeSelect("DM-MZ", '${famDearThreeInTwoStu.nation!}', "1")}
                        						</select>
											</td>
											<td width="11%">出生年月(岁)</td>
											<td width="11%">
												<input type="text" name="birthday" maxlength="20" id="birthday" class="form-control  " value="${famDearThreeInTwoStu.birthday!}" style="width:100%;">
											</td>
											<td width="12%" rowspan="3">
												<div id="ticketImages" class="js-layer-photos">
													<#if actDetails?exists&& (actDetails?size > 0)>
													    <#list actDetails as ad>
													        <span class="position-relative float-left mr10 mb10">
															<a class="pull-left">
													             <img id ="" style="width:100%;height: 94px"  data-img-action="adapte" layer-src="${request.contextPath}/familydear/threeInTwoStu/showPic?id=${ad.id!}&showOrigin=1" src="${request.contextPath}/familydear/threeInTwoStu/showPic?id=${ad.id!}&showOrigin=0" alt="">
													         </a>
													        <a class="pos-abs" style="top: -10px;right: -6px;" onclick="delPic1('${ad.id}')">
													            <i class="fa fa-times-circle color-red"></i>
													        </a>
													        </span>
													    </#list>
													</#if>
                                    			</div>
												<div><a class="btn btn-default btn-sm" id="ticket" onclick="addClick(this.id);">上传头像</a></div>
											</td>
										</tr>
										<tr>
											<td><span style="color:red">*</span>所在学校</td>
											<td>
												<input type="text" name="school" maxlength="50" id="school" nullable="false" class="form-control  " value="${famDearThreeInTwoStu.school!}" style="width:100%;">
											</td>
											<td><span style="color:red">*</span>所在系部</td>
											<td>
												<input type="text" name="sdept" maxlength="50" id="sdept" nullable="false" class="form-control  " value="${famDearThreeInTwoStu.sdept!}" style="width:100%;">
											</td>
											<td>宿舍号</td>
											<td>
												<input type="text" name="dormitoryNum" maxlength="20" id="dormitoryNum" class="form-control  " value="${famDearThreeInTwoStu.dormitoryNum!}" style="width:100%;">
											</td>
											<td>政治面貌</td>
											<td>
												<input type="text" name="politicCountenance" maxlength="20" id="politicCountenance" class="form-control  " value="${famDearThreeInTwoStu.politicCountenance!}" style="width:100%;">
											</td>
										</tr>
										<tr>
											<td>入学时间</td>
											<td>
												<input type="text" name="intake" maxlength="20" id="intake" class="form-control  " value="${famDearThreeInTwoStu.intake!}" style="width:100%;">
											</td>
											<td>学制</td>
											<td>
												<input type="text" name="schoolSystem" maxlength="10" id="schoolSystem" class="form-control  " value="${famDearThreeInTwoStu.schoolSystem!}" style="width:100%;">
											</td>
											<td><span style="color:red">*</span>所学专业</td>
											<td colspan="3">
												<input type="text" name="major" maxlength="50" id="major" nullable="false" class="form-control  " value="${famDearThreeInTwoStu.major!}" style="width:100%;">
											</td>
										</tr>
										<tr>
											<td>家庭住址</td>
											<td colspan="3">
												<input type="text" name="homeAddress" maxlength="300" id="homeAddress" class="form-control  " value="${famDearThreeInTwoStu.homeAddress!}" style="width:100%;">
											</td>
											<td colspan="2"><span style="color:red">*</span>身份证号码</td>
											<td colspan="3">
												<input type="text" name="identityCard" maxlength="18" nullable="false" id="identityCard" class="form-control  " value="${famDearThreeInTwoStu.identityCard!}" style="width:100%;">
												<div class="col-sm-4 control-tips tip-false"></div>
											</td>
										</tr>
										<tr>
											<td><span style="color:red">*</span>学生联系电话</td>
											<td colspan="3">
												<input type="text" name="linkPhone" maxlength="30" nullable="false" id="linkPhone" class="form-control  " value="${famDearThreeInTwoStu.linkPhone!}" style="width:100%;">
												<div class="col-sm-4 control-tips tip-false"></div>
											</td>
											<td colspan="2">所在系部负责人及联系电话</td>
											<td colspan="3">
												<input type="text" name="leaderPhone" maxlength="100" id="leaderPhone" class="form-control  " value="${famDearThreeInTwoStu.leaderPhone!}" style="width:100%;">
											</td>
										</tr>
										<tr>
											<td>班主任姓名<br>及联系电话</td>
											<td colspan="3">
												<input type="text" name="headmasterPhone" maxlength="100" id="headmasterPhone" class="form-control  " value="${famDearThreeInTwoStu.headmasterPhone!}" style="width:100%;">
											</td>
											<td colspan="2">电子邮箱</td>
											<td colspan="3">
												<input type="text" name="email" maxlength="20" id="email" class="form-control  " value="${famDearThreeInTwoStu.email!}" style="width:100%;">
											</td>
										</tr>
										<tr>
											<td rowspan="${memSize!}" class="family-infor">家庭情况</td>
											<td><span style="color:red">*</span>与本人关系</td>
											<td><span style="color:red">*</span>姓名</td>
											<td colspan="2">工作单位及职务</td>
											<td>出生年月(岁)</td>
											<td>政治面貌</td>
											<td>联系电话</td>
											<td>操作</td>
										</tr>
										
									<#if  memberList?exists && (memberList?size gt 0)>
                    					<#list memberList as item>
										<tr id="trTemp${item.id!}">
											<input type="hidden" name="stuTempList[${item_index}].id" value="${item.id!}" />
                            				<input type="hidden" name="stuTempList[${item_index!}].stuId" value="${item.stuId!}" />
											<td>
												<select name="stuTempList[${item_index!}].relation" nullable="false"   id="relation${item_index!}"  class="form-control">
                                    				${mcodeSetting.getMcodeSelect("DM-GX", '${item.relation!}', "1")}
                                				</select>
											</td>
											<td>
												<input type="text" name="stuTempList[${item_index!}].name" nullable="false" id="name${item_index!}"   maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.name!}" style="width:100%;"/>
											</td>
											<td colspan="2">
												<input type="text" name="stuTempList[${item_index!}].company"  id="company${item_index!}"   maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.company!}" style="width:100%;"/>
											</td>
											<td>
												<input type="text" name="stuTempList[${item_index!}].birthday"  id="birthday${item_index!}"   maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.birthday!}" style="width:100%;"/>
											</td>
											<td>
												<input type="text" name="stuTempList[${item_index!}].politicCountenance"  id="politicCountenance${item_index!}"   maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.politicCountenance!}" style="width:100%;"/>
											</td>
											<td>
												<input type="text" name="stuTempList[${item_index!}].linkPhone"  id="linkPhone${item_index!}"   maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${item.linkPhone!}" style="width:100%;"/>
											</td>
											<td><a class="table-btn color-blue js-del" href="#">删除</a></td>
										</tr>
										</#list>
									<#else>
										<tr id="trTemp0">
											<td>
												<select name="stuTempList[0].relation" nullable="false" id="relation0"  class="form-control">
                                					${mcodeSetting.getMcodeSelect("DM-GX", '', "1")}
                            					</select>
											</td>
											<td>
												<input type="text" name="stuTempList[0].name" nullable="false"  id="name0"  maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/>
											</td>
											<td colspan="2">
												<input type="text" name="stuTempList[0].company"   id="company0"  maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/>
											</td>
											<td>
												<input type="text" name="stuTempList[0].birthday"   id="birthday0"  maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/>
											</td>
											<td>
												<input type="text" name="stuTempList[0].politicCountenance"   id="politicCountenance0"  maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/>
											</td>
											<td>
												<input type="text" name="stuTempList[0].linkPhone"   id="linkPhone0"  maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/>
											</td>
											<td><a class="table-btn color-blue js-del" href="#">删除</a></td>
										</tr>
									</#if>
										
										<tr>
											<td colspan="8"><a class="table-btn color-blue js-add" href="#">+ 新增</a></td>
										</tr>
										<tr>
											<td>个人简历<br>(从小学起)</td>
											<td colspan="8" class="text-left">
												<textarea id="resume" name="resume" maxlength="2000" type="text/plain" style="width:100%;height:200px;">${famDearThreeInTwoStu.resume!}</textarea>
											</td>
										</tr>
										<tr>
											<td>主要表现<br>及奖惩情况</td>
											<td colspan="8" class="text-left">
												<div contenteditable="true">
													<textarea id="showContent" name="showContent" maxlength="2000" type="text/plain" style="width:100%;height:200px;">${famDearThreeInTwoStu.showContent!}</textarea>
												</div>
											</td>
										</tr>
										<tr>
											<td>直系亲属是否有被拘役、集中学习教育等情况。(如有，请注明具体情况)</td>
											<td colspan="8" class="text-left">
												<textarea id="situation" name="situation" maxlength="2000" type="text/plain" style="width:100%;height:200px;">${famDearThreeInTwoStu.situation!}</textarea>
											</td>
										</tr>
									</tbody>
							    </table>
							</div>
						</div>
					</div>
        </div>
    	</div>
        
        <div class="base-bg-gray text-center">
        	<a class="btn btn-blue" onclick="save();" href="javascript:;">保存</a>
        	<a class="btn btn-white" onclick="goBack();" href="javascript:;">返回</a>
    	</div>
    </form>
</div>
<script>
	var strOption = '<option value="">--- 请选择 ---</option>';
    <#if mcodelList?exists && mcodelList?size gt 0>
        <#list mcodelList as mcode>
        	strOption += ' <option title="${mcode.mcodeContent!}" value="${mcode.thisId!}">${mcode.mcodeContent!}</option>'
        </#list>
    </#if>
    
    var index = 1;
    <#if  memberList?exists && (memberList?size gt 0)>
    	index = ${memberList?size};
    </#if>

	$(function(){
		$(".js-add").click(function(){
			var html = '<tr>' +
                '<td >' +
            '                    <select name="stuTempList[' + index +'].relation" nullable="false" id="relation' +index+  '" class="form-control">'+strOption +
                ' </select></td>'+
                '   <td ><input type="text" name="stuTempList['+ index +
            '].name" id="name' +index+   '" nullable="false" maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/></td>'+
                '<td colspan="2"><input type="text" name="stuTempList['+index+'].company" id="company' + index+ '"  maxLength="200"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/></td>'+
                '<td ><input type="text" name="stuTempList['+index+'].birthday"  id="birthday' +index+ '"  maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/></td>' +
                '<td ><input type="text" name="stuTempList['+index+'].politicCountenance"  id="politicCountenance' +index+ '"  maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/></td>' +
                '<td ><input type="text" name="stuTempList['+index+'].linkPhone"  id="linkPhone' +index+ '"  maxLength="20"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:100%;"/></td>' +
                '<td><a class="table-btn color-blue js-del" href="#">删除</a></td>'+
                '</tr>';
            index++;
			$(this).parents("tr").before(html);
			var rowspan = parseInt($(".family-infor").attr("rowspan")) + 1;
			$(".family-infor").attr("rowspan",rowspan);
		})
		$(".table").on('click','.js-del',function(){
			$(this).parents("tr").remove();
			var rowspan = parseInt($(".family-infor").attr("rowspan")) - 1;
			$(".family-infor").attr("rowspan",rowspan);
		})
	})

	function goBack(){
		url = "${request.contextPath}/familydear/threeInTwoStu/edu/stuManage/page?currentPageIndex="+'${currentPageIndex!}'+"&currentPageSize="+'${currentPageSize!}'+"&stuName="+'${stuName!}'+"&ganbName="+'${ganbName!}'+"&stuPhone="+'${stuPhone!}';
		$(".model-div-show").load(url);
	}
	
	function checkIdentityCard(){
        var identityCard = $("#identityCard").val();
        var identityCardReg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
        if(identityCardReg.test(identityCard)){
            addSuccess("identityCard");
            return true;
        }else{
            addError("identityCard","身份证件号不符合身份证规则");
            return false;
        }
    }

    function checkMobilePhone(){
        var linkPhone = $("#linkPhone").val();
        var mobilePhoneReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        var phoneNumReg = /^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/;
        if(mobilePhoneReg.test(linkPhone)){
            addSuccess("linkPhone");
            return true;
        }else{
            debugger;
            if(phoneNumReg.test(linkPhone)){
                addSuccess("linkPhone");
                return true;
            }else {
                addError("linkPhone","联系电话不符合规则");
                return false;
            }
        }
    }

    function addError(id,errormsg){
        if(!errormsg){
            errormsg='错误';
        }
        $("#"+id).siblings(".control-tips").addClass("tip-false");
        $("#"+id).siblings(".control-tips").html('<span class="has-error"><i class="fa fa-times-circle"></i>'+errormsg+'</span>');
    }

    function addSuccess(id,msg){
        if(!msg){
            msg='正确';
        }
        $("#"+id).siblings(".control-tips").removeClass("tip-false");
        $("#"+id).siblings(".control-tips").html('<span class="has-success"><i class="fa fa-check-circle"></i>&nbsp;'+msg+'</span>');
    }
	
	var isSubmit=false;
    function save() {
    	if(isSubmit){
    		isSubmit = true;
			return;
		}
		var check = checkValue('#subForm');
		if(!check){
			isSubmit=false;
			return;
		}
		var flag1 = true;
        var flag2 = true;
        if ($("#identityCard").val() != '') {
            flag1 = checkIdentityCard();
        }
        if($("#linkPhone").val() != ''){
            flag2 = checkMobilePhone();
        }
        if (!flag1 || !flag2) {
            return;
        }
		
        var options = {
            url : "${request.contextPath}/familydear/threeInTwoStu/edu/stuManage/save",
            dataType : 'json',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success){
                    layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
                    $("#arrange-commit").removeClass("disabled");
                    isSubmit=false;
                    return;
                }else{
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                    isSubmit=false;
                    goBack();
                }
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    }
    
    var ticketNum=1;
    var index;
    function addClick(id){
        index = id;
    }
    
    $(function(){
        var $wrap = $('#uploader');
        var $queue = $("#filelist");
        /*init webuploader*/
        var $btn =$("#ctlBtn");   
        var thumbnailWidth = 100;   //缩略图高度和宽度 （单位是像素），当宽高度是0~1的时候，是按照百分比计算，具体可以看api文档
        var thumbnailHeight = 100;


        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: '${request.contextPath}/static/webuploader/Uploader.swf',
            // 文件接收服务端。
            server: '${request.contextPath}/familydear/threeInTwoStu/saveAttachment',

            formData: {
                'objId':'${famDearThreeInTwoStu.id!}',
                'objType':'famDearThreeInTwoStu',
            },

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#ticket',
            duplicate:true,

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            method:'POST',
        });
        
        // 当有文件添加进来的时候
        uploader.on( 'fileQueued', function( file ) {  // webuploader事件.当选择文件后，文件被加载到文件队列中，触发该事件。等效于 uploader.onFileueued = function(file){...} ，类似js的事件定义。
            var $li = $( '<li id="' + file.id + '">' +
                    '<p class="imgWrap">' +
                    '<img>' +
                    '</p>'+
                    '</li>' ),
                    $img = $li.find('img'),

                    $btns = $('<div class="file-panel">' +
                            '<span class="cancel">删除</span></div>').appendTo( $li );


            // $list为容器jQuery实例
            // $queue.append( $li );
            // refreshPic();
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            uploader.makeThumb( file, function( error, src ) {   //webuploader方法
                if ( error ) {
                    $img.replaceWith('<span>不能预览</span>');
                    return;
                }

                $img.attr( 'src', src );
            }, thumbnailWidth, thumbnailHeight );

            // uploader.upload();
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
        uploader.on( 'uploadSuccess', function( file ) {
            refreshPic();
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

            console.log("上传成功");
        });

        // 完成上传完了，成功或者失败，先删除进度条。
        uploader.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').remove();
        });
        
        $btn.on( 'click', function() {
            console.log("上传...");
            uploader.upload();
            console.log("上传成功");
        });
    });
    
    function refreshPic(){
    	var id=$("#id").val();
        $("#ticketImages").load("${request.contextPath}/familydear/threeInTwoStu/showAllpic?id=" + id);

    }
    
<#if actDetails?exists&& (actDetails?size > 0)> 
    $(function () {
        layer.photos({
            shade: .6,
            photos:'.js-layer-photos',
            shift: 5
        });
    });
    function delPic1(id){
        var picIds;
        picIds = $("#picIds").val()+","+id;
        $("#picIds").val(picIds)
        $.ajax({
            url:'${request.contextPath}/familydear/threeInTwoStu/delPic',
            data: {"id":id},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    refreshPic(index);
                }
                else{
                    
                }
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
</#if>   
</script>