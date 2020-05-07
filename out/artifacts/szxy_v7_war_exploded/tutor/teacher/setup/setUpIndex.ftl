<#import "/fw/macro/htmlcomponent.ftl" as common />
<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="page-head-btns"><a class="btn btn-blue js-addNewProject">新建选导师项目</a></div>
				<#if listSetupDto?exists && listSetupDto?size gt 0>
				   <#list listSetupDto as listSetup>
					<div class="box box-default">
						<div class="box-header">
							<h4 class="box-title" id = "roundNameInput"><span id="${listSetup.tutorRound.id!}" title="${listSetup.tutorRound.roundName!}" >${listSetup.tutorRound.roundTitleName!}</span></h4>
							&nbsp; &nbsp; &nbsp; &nbsp; <span>时间：${listSetup.tutorRound.beginTime?string('yyyy-MM-dd HH:mm')} --- ${listSetup.tutorRound.endTime?string('yyyy-MM-dd HH:mm')}</span>
						</div>
						<div class="box-body">
							<div class="tutor-project">
								<a ><strong>${listSetup.canChooseTeaN!}</strong><span>可选导师数</span></a>
								<a ><strong>${listSetup.selectedStuN!}</strong><span>已选学生数</span></a>
								<a ><strong>${listSetup.noSelectedStuN!}</strong><span>未选学生数</span></a>
								<div class="tutor-project-state">
									<span  id = "tutorState" value= "${listSetup.state!}"></span>
									<p>创建时间：${listSetup.tutorRound.creatTime?string('yyyy-MM-dd HH:mm')}</p>
								</div>
							</div>
						</div>
						<div class="box-tools dropdown">
							<a class="box-tools-btn" href="javascript:void(0);" data-toggle="dropdown"><i class="wpfont icon-ellipsis"></i></a>
							<ul class="box-tools-menu dropdown-menu">
								<span class="box-tools-menu-angle"></span>
							<#-- 	<li><a class = "addTutor" onclick="openModel('99','添加导师','1','${request.contextPath}/tutor/teacher/setUp/showTutorIndex?tutorRoundId='+'${listSetup.tutorRound.id!}','','','','');">添加导师</a></li>-->
								<li><a class = "addTutor" href="javascript:void(0);" onclick="showTutorIndex('${listSetup.tutorRound.id!}')">添加导师</a></li>
								<li>
								 <#if listSetup.state != 1>
								    <a class="showProject" href="javascript:void(0);" value="${listSetup.tutorRound.id!}">查看选导设置</a>
								 <#else>
								    <a class="updateProject" href="javascript:void(0);" value="${listSetup.tutorRound.id!}">选导设置</a>
								 </#if>
								</li>
								<li><a class="js-del" href="javascript:void(0);" value="${listSetup.tutorRound.id!}">删除</a></li>
							</ul>
						</div>
					</div>
					</#list>
			    <#else>
			        <div class="no-data-container">
						<div class="no-data">
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
							</span>
							<div class="no-data-body">
								<h3>暂无项目</h3>
								<p class="no-data-txt">请点击左上角的“新建选导师项目”按钮新建</p>
							</div>
						</div>
				    </div>
				</#if>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->

<div class="layer layer-addNewProject">
	
</div>

<script>
    var isOk= true;
     //判断当前轮次的状态
	$(document).ready(function(){
	  $('.tutor-project-state #tutorState').each(function(){
	     if($(this).attr("value") == 1){
	         $(this).text("未开始");
	         $(this).addClass('color-red');
	         $(this).parents('.box-default').find(".addTutor").css('display','');
	     }else if($(this).attr("value") == 0){
	         $(this).text("进行中");
	         $(this).addClass('color-green');
	         $(this).parents('.box-default').find(".addTutor").css('display','none'); 
//	         $(this).parents('.box-default').find(".js-del").css('display','none'); 
	     }else{
	         $(this).text("已结束");
	         $(this).addClass('color-grey');
//	         $(this).parents('.box-default').find(".addTutor").css('display','none');
	     }
	  })
	});
    
    // 添加选导师项目
    $('.page-head-btns .js-addNewProject').on('click',function(){
		$('.layer-addNewProject').load("${request.contextPath}/tutor/teacher/setUp/save",function(){			
		layer.open({
					type: 1,
					shade: .5,
					title: '新建选导师项目',
					area: '350px',
					btn: ['确定','取消'],
					zIndex: 1000,
					yes:function(index,layero){
					    datetimepicker();
					    if(isOk){
					    saveTutorRound("${request.contextPath}/tutor/teacher/setUp/create");
					    }
		            },
					content: $('.layer-addNewProject'),
		           })
        });
	})
    function saveTutorRound(contextPath){
      $.ajax({
            url:contextPath,
            data:dealDValue(".layer-addNewProject"),
            clearForm : false,
            resetForm : false,
            dataType:'json',
            contentType: "application/json",
            type:'post',
            success:function (data) {
                if(data.success){
                    showSuccessMsgWithCall(data.msg,goSetUpIndex);
               //     showSuccessMsg(data.msg);
                }else{
                    showErrorMsg(data.msg);
                }
            }
      })
    }
 //更改名字 	 
 var roundName1 ='';
	$('.js-editTutorProjectName').on('click', function(e){
		e.preventDefault();
		roundName1 = $(this).prev().html();
		$(this).prev().attr('contenteditable','true').focus();
	})
	
	$('.js-editTutorProjectName').prev().blur(function(e){
				var tutorRoundId=$(this).next('a').attr("value");
				$(this).removeAttr('contenteditable');
				var roundName=$(this).html();
				roundName = roundName.replace(/<br>/g,"");
				if(roundName==''){
		              layerError("#"+tutorRoundId,"轮次名字不能为空");
		              $(this).html(roundName1);
		              return ;
		        }else{
		            if(roundName.length > 50){
						layerError("#"+tutorRoundId,"轮次名字长度过长,不能超过50！");
					}else{
						roundName =  encodeURI(roundName);
						$.ajax({
				                    url:"${request.contextPath}/tutor/teacher/setUp/update?tutorRoundId="+tutorRoundId+"&roundName="+roundName,
				                    data:JSON.stringify({}),
				                    dataType:'json',
				                    contentType:'application/json',
				                    type:'post',
				                    success:function (data) {
				                        if(data.success){
				                            showSuccessMsgWithCall(data.msg,goSetUpIndex);
				                        }else{
				                            showErrorMsg(data.msg);
				                        }
				                    }
				       });
					}
			     }
			});	 


	// 删除
	$('.js-del').on('click', function(e){
	  var tutorRoundId=$(this).attr("value");
			    	e.preventDefault();
			    	var that = $(this);
			    	var index = layer.confirm("是否删除关于这个轮次的所有信息？", {
			    	 btn: ["确定", "取消"]
			    	}, 
			    	function(){
			    		$.ajax({
	                        url:"${request.contextPath}/tutor/teacher/setUp/delete?tutorRoundId="+tutorRoundId,
	                        data:{},
	                        dataType:'json',
	                        contentType:'application/json',
	                        type:'GET',
	                        success:function (data) {
	                            if(data.success){
	                                that.closest('tr').remove();
	                                showSuccessMsgWithCall(data.msg,goSetUpIndex);
	                            }else{
	                                showErrorMsg(data.msg);
	                            }
	                        }
                        });
			    	  layer.close(index);
			    	})
	});

    //修改项目
    $('.updateProject').on('click',function(){
       var tutorRoundId=$(this).attr("value");
		$('.layer-addNewProject').load("${request.contextPath}/tutor/teacher/setUp/save?tutorRoundId="+tutorRoundId,function(){			
		layer.open({
					type: 1,
					shade: .5,
					title: '修改选导师设置',
					area: '350px',
					btn: ['确定','取消'],
					zIndex: 1000,
					yes:function(index,layero){
					   datetimepicker();
					   if(isOk){
					    saveTutorRound("${request.contextPath}/tutor/teacher/setUp/update?tutorRoundId="+tutorRoundId);
					   }
		            },
					content: $('.layer-addNewProject')
		           })
         });
	})
	
	//查看项目  showProject
	$('.showProject').on('click',function(){
       var tutorRoundId=$(this).attr("value");
       var isSee = "true";
		$('.layer-addNewProject').load("${request.contextPath}/tutor/teacher/setUp/save?tutorRoundId="+tutorRoundId+"&isSee="+isSee,function(){			
		layer.open({
					type: 1,
					shade: .5,
					title: '查看导师项目',
					area: '350px',
					btn: [],
					disabled:"true",
					zIndex: 1000,
					content: $('.layer-addNewProject')
		           })
         });
	})
	
	//重新加载页面
	function goSetUpIndex(){
	  setUpIndex();
	<#--   openModel('super1','导师设置','1','${request.contextPath}/tutor/teacher/setUp/index?fromDesktop=true','','','','');--> 
	}
	
    
    
    //从desktop.js中引用的方法
    function dealDValue(container){
    var tags = ["input","select","textarea"];
    var os ;
    var obj = new Object();
    for(var i=0; i<tags.length;i++) {
        if (typeof(container) == "string") {
            os = jQuery(container + " " + tags[i]);
        }
        else {
            return;
        }
        os.each(function () {
            $this = $(this);
            var value = $this.val();
            var type = $this.attr("type");
            if ((type == 'number' || type=='int') && value!='' && value!=null) {
                value = parseInt(value);
            }
            var name = $this.attr("name");
            name = name || $this.attr("id");
            var exclude = $this.attr("exclude");
            if (!exclude) {
                if (obj[name] && !(obj[name] instanceof Array)) {
                    var array = new Array();
                    array.push(obj[name]);
                    array.push(value);
                    obj[name] = array;
                } else if (obj[name] && obj[name] instanceof Array){
                    obj[name].push(value);
                }else{
                    obj[name] = value;
                }
            }
        });
    }
    return JSON.stringify(obj);
}

   function showSuccessMsgWithCall(msg, call){
	    if (!(call == null || !(call instanceof Function))) {
	        showMsg("成功", msg, true, call);
	    } else {
	        showSuccessMsg(msg)
	    }
	}
	function showSuccessMsg(msg) {
	    showMsg("成功",msg,true);
	}
	function showErrorMsg(msg) {
	    showMsg("失败",msg,false);
	}
    function showMsg(title,msg,success,call){
    layer.alert(msg,{
        icon:success?1:2,
        title:title,
        btn:['确定'],
        end:function () {
            if (call!=null && call instanceof Function) {
                call();
            }
        },
        yes:function (index) {
            if (success){
                layer.closeAll();
            } else {
                layer.close(index);
            }
        }
    });
}
 function layerError(key,msg){
    layer.tips(msg, key, {
        tipsMore: true,
        tips:2,
        time:3000
    });
}

</script>


