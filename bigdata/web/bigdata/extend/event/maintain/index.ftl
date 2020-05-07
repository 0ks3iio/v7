<div class="overview-set bg-fff metadata clearfix">
	<input type="hidden" id="type" value=""/>
	<input type="hidden" id="dimCode" value=""/>
	<div id="event-type-div" class="filter-item active" onclick="changeTab1('eventType')">
        <span>事件组</span>
    </div>
    <div id="event-div" class="filter-item" onclick="changeTab1('event')">
        <span>事件</span>
    </div>
    <div id="event-property-div" class="filter-item" onclick="changeTab1('eventProperty')">
        <span>事件属性</span>
    </div>
    <div id="event-index-div" class="filter-item" onclick="changeTab1('eventIndex')">
        <span>指标</span>
    </div>
</div>
<div id="mainDIv" class="box box-default no-margin clearfix js-height">
</div>	
<div class="layer layer-event">
    <div id="eventDiv">
    </div>
</div>
<script>
    $(function () {
        changeTab1('eventType');
    });

    function editEventType(id) {
        $.ajax({
            url: '${request.contextPath}/bigdata/eventMaintain/eventTypeEdit',
            type: 'POST',
            data: {id: id},
            dataType: 'html',
            success: function (response) {
                $('#eventDiv').empty().append(response);
            }
        });
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增事件组' : '修改事件组',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#typeName').val() == "") {
                    layer.tips("不能为空", "#typeName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#orderId').val() == "") {
                    layer.tips("不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/eventMaintain/saveEventType",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                           showLayerTips('error',data.message,'t');
                            isSubmit = false;
                        } else {
                        	showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            $('#eventDiv').empty();
                            changeTab('eventType');
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#eventTypeForm").ajaxSubmit(options);
            },
            area: ['600px', '350px'],
            content: $('.layer-event')
        });
    }

    function deleteEventType(id) {
		showConfirmTips('prompt',"提示","您确定要删除事件组吗？",function(){
				 $.ajax({
			            url: '${request.contextPath}/bigdata/eventMaintain/deleteEventType',
			            data:{
			              id: id
			            },
			            type:"post",
			            clearForm : false,
						resetForm : false,
			            dataType: "json",
			            success:function(data){
			          	    layer.closeAll();
					 		if(!data.success){
					 			showLayerTips4Confirm('error',data.message);
					 		}else{
					 		     showLayerTips('success','删除成功','t');
					 		     changeTab('eventType');
			    			}
			          },
			          error:function(XMLHttpRequest, textStatus, errorThrown){}
			    });
			});
    }

    function editEvent(id) {
        $.ajax({
            url: '${request.contextPath}/bigdata/eventMaintain/eventEdit',
            type: 'POST',
            data: {id: id},
            dataType: 'html',
            success: function (response) {
                $('#eventDiv').empty().append(response);
                if ($('#eventTypeFilter').val() != '') {
                    $('#eventTypeSelect').val($('#eventTypeFilter').val());
                }
            }
        });
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增事件' : '修改事件',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#eventTypeSelect').val() == null || $('#eventTypeSelect').val() == "") {
                    layer.tips("不能为空", "#eventTypeSelect", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#eventName').val() == "") {
                    layer.tips("不能为空", "#eventName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#eventCode').val() == "") {
                    layer.tips("不能为空", "#eventCode", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#tableName').val() == "") {
                    layer.tips("不能为空", "#tableName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#topicName').val() == "") {
                    layer.tips("不能为空", "#topicName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#orderId').val() == "") {
                    layer.tips("不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/eventMaintain/saveEvent",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                        	showLayerTips4Confirm('error',data.message);
                            isSubmit = false;
                        } else {
                        	showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            $('#eventDiv').empty();
                            changeTab('event', $('#eventTypeFilter').val());
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#eventForm").ajaxSubmit(options);
            },
            area: ['600px', '730px'],
            content: $('.layer-event')
        });
    }
    
    function deleteEvent(id) {
		showConfirmTips('prompt',"提示","您确定要删除事件吗？",function(){
			 $.ajax({
	             url: '${request.contextPath}/bigdata/eventMaintain/deleteEvent',
		            data:{
		              id: id
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(data){
		          	    layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.message);
				 		}else{
				 		     showLayerTips('success','删除成功','t');
				 		      changeTab('event', $('#eventTypeFilter').val());
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
    }

    function editEventProperty(id) {
        $.ajax({
            url: '${request.contextPath}/bigdata/eventMaintain/eventPropertyEdit',
            type: 'POST',
            data: {id: id},
            dataType: 'html',
            success: function (response) {
                $('#eventDiv').empty().append(response);
                if ($('#eventFilter').val() != '') {
                    $('#eventSelect').val($('#eventFilter').val());
                }
            }
        });
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增事件属性' : '修改事件属性',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#eventSelect').val() == null || $('#eventSelect').val() == "") {
                    layer.tips("不能为空", "#eventSelect", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#propertyName').val() == "") {
                    layer.tips("不能为空", "#propertyName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#fieldName').val() == "") {
                    layer.tips("不能为空", "#fieldName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#orderId').val() == "") {
                    layer.tips("不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/eventMaintain/saveEventProperty",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error',data.message);
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            $('#eventDiv').empty();
                            changeTab('eventProperty', $('#eventFilter').val());
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#eventForm").ajaxSubmit(options);
            },
            area: ['600px', '600px'],
            content: $('.layer-event')
        });
    }

    function deleteEventProperty(id) {
    	showConfirmTips('prompt',"提示","您确定要删除事件属性吗？",function(){
			 $.ajax({
	             url: '${request.contextPath}/bigdata/eventMaintain/deleteEventProperty',
		            data:{
		              id: id
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(data){
		          	    layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.message);
				 		}else{
				 		     showLayerTips('success','删除成功','t');
				 		     changeTab('eventProperty', $('#eventFilter').val());
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
    }

    function editEventIndex(id) {
        $.ajax({
            url: '${request.contextPath}/bigdata/eventMaintain/eventIndexEdit',
            type: 'POST',
            data: {id: id},
            dataType: 'html',
            success: function (response) {
                $('#eventDiv').empty().append(response);
                if ($('#eventFilter').val() != '') {
                    $('#eventSelect').val($('#eventFilter').val());
                }
            }
        });
        var isSubmit = false;
        layer.open({
            type: 1,
            shade: .6,
            title: id == '' ? '新增事件指标' : '修改事件指标',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }
                isSubmit = true;

                if ($('#eventSelect').val() == null || $('#eventSelect').val() == "") {
                    layer.tips("不能为空", "#eventSelect", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#indicatorName').val() == "") {
                    layer.tips("不能为空", "#indicatorName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#aggTypeSelect').val() != "count") {
                    if ($('#aggField').val() == "") {
                        layer.tips("不能为空", "#aggField", {
                            tipsMore: true,
                            tips: 3
                        });
                        isSubmit = false;
                        return;
                    }
                }

                if ($('#aggOutputName').val() == "") {
                    layer.tips("不能为空", "#aggOutputName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#orderId').val() == "") {
                    layer.tips("不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/eventMaintain/saveEventIndex",
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error',data.message);
                            isSubmit = false;
                        } else {
                            showLayerTips('success','保存成功!','t');
                            layer.close(index);
                            $('#eventDiv').empty();
                            changeTab('eventIndex', $('#eventFilter').val());
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#eventIndexForm").ajaxSubmit(options);
            },
            area: ['600px', '500px'],
            content: $('.layer-event')
        });
    }

    function deleteEventIndex(id) {
		showConfirmTips('prompt',"提示","您确定要删除事件指标吗？",function(){
			 $.ajax({
	             url: '${request.contextPath}/bigdata/eventMaintain/deleteEventIndex',
		            data:{
		              id: id
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(data){
		          	    layer.closeAll();
				 		if(!data.success){
				 			showLayerTips4Confirm('error',data.message);
				 		}else{
				 		     showLayerTips('success','删除成功','t');
				 		     changeTab('eventIndex', $('#eventFilter').val());
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){}
		    });
		});
    }

	function eventDataImport(eventId){
		router.go({
	        path: '/bigdata/event/import/index?eventId='+eventId,
	        name:'导入',
	        level: 3
	    }, function () {
		    var url = '${request.contextPath}/bigdata/event/import/index?eventId='+eventId;
			$("#mainDIv").load(url);
	    });
	}
	
	function configure(eventId){
		router.go({
	        path: 'bigdata/eventMaintain/configure?eventId='+eventId,
	        name:'配置',
	        level: 3
	    }, function () {
		   var url = '${request.contextPath}/bigdata/eventMaintain/configure?eventId='+eventId;
			$("#mainDIv").load(url);
	    });
	}

    function changeTab(type, typeId) {
   		var url ;
   		if(!type)
			type=$("#type").val();
			
    	if(type =='eventType'){
    		$('#event-type-div').addClass('active').siblings().removeClass('active');	
    		url = '${request.contextPath}/bigdata/eventMaintain/eventTypeList';
        }
        if (type == 'event') {
			$('#event-div').addClass('active').siblings().removeClass('active');	
            url = '${request.contextPath}/bigdata/eventMaintain/eventList';
        }

        if (type == 'eventProperty') {
			$('#event-property-div').addClass('active').siblings().removeClass('active');	
            url = '${request.contextPath}/bigdata/eventMaintain/eventPropertyList';
        }

        if (type == 'eventIndex') {
			$('#event-index-div').addClass('active').siblings().removeClass('active');	
            url = '${request.contextPath}/bigdata/eventMaintain/eventIndexList';
        }        
		$("#type").val(type);
        $.ajax({
            url: url,
            type: 'POST',
            data: {typeId:typeId},
            dataType: 'html',
            success: function (response) {
                $('#mainDIv').empty().html(response);
            }
        });
        $(this).parent().addClass('active');
    }

	function changeTab1(type, typeId) {
   		var url ;
   		var path;
   		var titleName;
   		if(!type)
			type=$("#type").val();
			
    	if(type =='eventType'){
    		$('#event-type-div').addClass('active').siblings().removeClass('active');	
    		url = '${request.contextPath}/bigdata/eventMaintain/eventTypeList';
    		path='/bigdata/eventMaintain/eventTypeList';
    		titleName='事件组';
        }
        if (type == 'event') {
			$('#event-div').addClass('active').siblings().removeClass('active');	
            url = '${request.contextPath}/bigdata/eventMaintain/eventList';
			path='/bigdata/eventMaintain/eventList';
    		titleName='事件';
        }

        if (type == 'eventProperty') {
			$('#event-property-div').addClass('active').siblings().removeClass('active');	
            url = '${request.contextPath}/bigdata/eventMaintain/eventPropertyList';
			path='/bigdata/eventMaintain/eventPropertyList';
    		titleName='事件属性';
        }

        if (type == 'eventIndex') {
			$('#event-index-div').addClass('active').siblings().removeClass('active');	
            url = '${request.contextPath}/bigdata/eventMaintain/eventIndexList';
            path='/bigdata/eventMaintain/eventIndexList';
    		titleName='指标';
        }        
		$("#type").val(type);
		
		router.go({
	        path: path,
	        name:titleName,
	        level: 2
	    }, function () {
		   $.ajax({
            url: url,
            type: 'POST',
	            data: {typeId:typeId},
	            dataType: 'html',
	            success: function (response) {
	                $('#mainDIv').empty().html(response);
	            }
	        });
	        $(this).parent().addClass('active');
	    });
       
    }

    function changeEvent(e) {
        changeTab('eventProperty', $(e).val());
    }

    function changeEventForIndex(e) {
        changeTab('eventIndex', $(e).val());
    }

    function changeEventType(e) {
        changeTab('event', $(e).val());
    }
    
    function stopEventProperty(id) {
        $.ajax({
            url: '${request.contextPath}/bigdata/eventMaintain/changeEventPropertyStatu',
            type: 'POST',
            data: {
                id: id,
                status: 0
            },
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
					showLayerTips('error',val.message,'t');
                }
                else {
                    changeTab('eventProperty', $('#eventFilter').val());
                }
            }
        });
    }
    
    function startEventProperty(id) {
        $.ajax({
            url: '${request.contextPath}/bigdata/eventMaintain/changeEventPropertyStatu',
            type: 'POST',
            data: {
                id: id,
                status: 1
            },
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showLayerTips('error',val.message,'t');
                }
                else {
                    changeTab('eventProperty', $('#eventFilter').val());
                }
            }
        });
    }
</script>