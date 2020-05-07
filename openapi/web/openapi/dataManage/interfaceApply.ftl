<div class="page-content">
	<div class="row">
		<div class="col-xs-12">
			<!-- PAGE CONTENT BEGINS -->
			<div class="box box-default">
				<div class="box-body">
					<h4>
						<b>申请接口</b>
					</h4>
					<hr />
					<!-- LIST BEGINS -->
					<div class="clearfix">
						<div class="col-md-4 ml10">
						    <div class="base-border-line">
						    	<h3 class="box-title"><b>选择接口</b></h3>
							</div>
							<table class="table table-bordered table-striped table-hover no-margin">
								<thead>
								    <tr>
										<th width="20">
											<label class="pos-rel">
												<input name="course-checkbox" type="checkbox" class="wp" onclick="checkInterfaceAll(this)">
												<span class="lbl"></span>
											</label>
										</th>
										<th>接口名称</th>
										<th width="19"></th>
									</tr>
								</thead>
							</table>
							<div style="height: 685px;overflow-y: scroll;border: 1px solid #ddd;">
								<table class="table table-bordered table-striped table-hover select-port" style="margin: -1px 0 0 -1px;">
									<tbody class="interList">
									    <#if openInterfaceDtos?exists&&openInterfaceDtos?size gt 0>
          	                               <#list openInterfaceDtos as dto>
									            <tr onclick="chooseInter('${dto.type!}',this)" data-action="select">
									                <td width="20">
														<label class="pos-rel">
															<input name="course-checkbox" type="checkbox" class="wp" id = "chooseInte_${dto.type!}" value="${dto.type!}">
															<span class="lbl"></span>
														</label>
													</td>
											        <td value="${dto.type!}">${dto.interfaceName!}</td>
										        </tr>
									       </#list>
									    </#if>
									</tbody>
								</table>
							</div>	
						</div>
						<div class="col-md-4" style="margin-left: -21px;">
						    <div class="base-border-line">
						    	<h3 class="box-title"><b>选择属性</b></h3>
							</div>
							<table class="table table-bordered table-striped table-hover no-margin">
								<thead>
									<tr>
										<th width="45">
											<label class="pos-rel">
												<input name="entity-checkbox" type="checkbox" class="wp"  id = "chooseEntity" onclick="checkEntityAll(this)">
												<span class="lbl"> </span>
											</label>
										</th>
										<th width="85">属性名称</th>
										<th>参数名</th>
										<th width="85">是否可为空</th>
										<th width="19"></th>
									</tr>
								</thead>
							</table>
							<div style="height: 685px;overflow-y: scroll;border: 1px solid #ddd;">
								<table class="table table-bordered table-striped table-hover" style="margin: -1px 0 0 -1px;">
									<tbody id = "showEntity">
									
									</tbody>
								</table>
							</div>
						</div>
						<div class="col-md-4">
						    <div class="base-border-line">
								<h3 class="box-title"><b>已选</b></h3>
								<a class="btn btn-blue btn-sm btn-apply-entity pull-right" href="javascript:void(0);">提交申请</a>
							</div>
							<table class="table table-bordered table-striped table-hover no-margin">
								<thead>
									<tr>
										<th width="85">已选接口</th>
										<th>已选属性</th>
										<th width="19"></th>
									</tr>
								</thead>
							</table>
							<div style="height: 634px;overflow-y: scroll;border: 1px solid #ddd;">
								<table class="table table-bordered table-striped table-hover" style="margin: -1px 0 0 -1px;" id = "showResult">
								    
								   
								</table>
							</div>
						</div>
					</div>
					<!-- LIST ENDS -->
					
				</div>
			</div>
			
			<!-- PAGE CONTENT ENDS -->
		</div><!-- /.col -->
	</div><!-- /.row -->
</div><!-- /.page-content -->

<script>

var inter;
var selType;
var map_=getMap();
var entityIds;
function chooseInter(type,event){
	selType = type;
	doUpdateInter(type,event);
	inter = $(event).children('td').last().html();
}

function doUpdateInter(type,event){
	var checked = $('#chooseInte_'+type).prop("checked");
	doUpdateEntity();
	if(checked){
		$('#chooseInte_'+type).prop('checked',false);
		map_.put(type+"_","");
		showEndResult();
		getEntityList();
	}else{
		$('#chooseInte_'+type).prop('checked',true);
		getEntityList(type);
	}
	$('td').removeClass('active');
	$(event).children('td').toggleClass('active');
}

function doUpdateEntity(){
	var checked = $('#chooseEntity').prop("checked");
	if(checked){
		$('#chooseEntity').prop('checked',false);
	}
}

function checkInterfaceAll(obj){
	var checked = $(obj).prop("checked");
	var all = $(obj).parents(".col-md-4").find("tbody input:checkbox[name='course-checkbox']");
	if(checked){
		all.prop('checked',true);
	}else{
		all.prop('checked',false);
		map_ = getMap();
		$("#showResult").html('');
		getEntityList();
		doUpdateEntity();
	}
	$(event).children('td').toggleClass('active');
}

function checkEntityAll(obj){
	var checked = $(obj).prop("checked");
	var all = $(obj).parents("div").find("tbody  input:checkbox[name='entity-checkbox']");
	if(checked){
		all.prop('checked',true);
	}else{
		all.prop('checked',false);
	}
	getChooseResult();
}

function getEntityList(type){
	$.ajax({
        url:"${request.contextPath}/data/manage/showEntityList?type="+type,
        data:{},
        async: false,
        dataType:'json',
        contentType:'application/json',
        type:'GET',
        success: function(data) {
			var array = data;
			var htmlStr = '';
			if(array.length > 0){
				$.each(array, function(index, json){
					htmlStr += '<tr><td width="45"><label><input name="entity-checkbox" type="checkbox" class="wp" value="'+json.id+'"  onclick="getChooseResult()"><span class="lbl"></span></label></td>';
					htmlStr += '<td width="85"  value="'+json.id+'">' + json.entityName + '</td>'
					htmlStr += '<td name="entity-displayName">' + json.displayName + '</td>';
					htmlStr += '<td width="85">' + json.mandatory + '</td></tr>';
					htmlStr += '<input type="hidden" id="entityId_type" value="'+json.id+'"></input>';
				});
			}
			$("#showEntity").html(htmlStr);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
		}
    });
}

function getChooseResult(){
   getEndResult(selType);
   showEndResult();
}

//返回最后的结果
function showEndResult(){
	var endResult = '';
	var keys=map_.keyset();
	for(var i=0;i<keys.length;i++){      
	    endResult += map_.get(keys[i])
	}  
	$("#showResult").html(endResult);
}

function getEndResult(type){
   var selEle = $('#showEntity :checkbox:checked');
   var rowspan = selEle.length +1;
   var htmlResult = "";
   if(selEle.length > 0) {
	   htmlResult += '<tbody id = "inter-'+type+'"><tr><td rowspan="'+rowspan+'" width="85">' + inter + '</td></tr>';
	   for(var i=0;i< selEle.length;i++){
		    var event = selEle.eq(i);
		    var entityName = event.parents('td').nextAll("td[name='entity-displayName']").html();
		    var entityId = event.parents('tr').next("#entityId_type").val();
		    htmlResult += '<tr><td>' + entityName + '<input type="hidden" id="entityId" value="'+entityId+'"></td></tr>';
	   } 
	   htmlResult += '</tbody>';
   }
   map_.put(type+"_",htmlResult);  
}

//提交申请属性
var isSubmit=false;
$('.btn-apply-entity').on('click',function(){
	if(isSubmit){
	   return;
    }
    isSubmit = true;
    var selEle = $('#showResult').find('input');
    if(selEle.length <= 0) {
        showMsgError('请先选择接口和属性','');
        isSubmit = false;
    }
    if(isSubmit){
       isSubmit = false;
 	   var entityIds = new Array();
 	   for(var i=0;i<selEle.length;i++){
 		  entityIds.push(selEle.eq(i).val());
 	   } 
 	   var types = new Array();
 	   var selInter = $('.interList :checkbox:checked');
 	   for(var i=0;i<selInter.length;i++){
 		  types.push(selInter.eq(i).val());
 	   }
	    var data={'entityIds':entityIds,'types':types}
        $.ajax({
          url:'${request.contextPath}/data/manage/apply',
          data:data,
          type:'post',  
          cache:false,  
          success:function(result){
        	var json =  JSON.parse(result);
            ajaxSessionVai(result);
            if(json.code != -1){
              showMsgSuccess(json.msg,'成功');
            }else{
              showMsgError(json.msg,"失败");
            }
          }
        });
    }
})

function getMap(){//初始化map_，给map_对象增加方法，使map_像个Map  
    var map_=new Object();  
    //属性加个特殊字符，以区别方法名，统一加下划线_  
    map_.put=function(key,value){    map_[key]=value;}   
    map_.get=function(key){    return map_[key];}  
    map_.remove=function(key){    delete map_[key];}      
    map_.keyset=function(){  
        var ret="";  
        for(var p in map_){      
            if(typeof p =='string' && p.substring(p.length-1)=="_"){   
                ret+=",";  
                ret+=p;  
            }  
        }             
        if(ret==""){  
            return ret.split(","); //empty array  
        }else{  
            return ret.substring(1).split(",");   
        }  
    }     
    return map_;  
}  
</script>
    