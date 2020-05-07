var popupStrVar = '';
popupStrVar += '<div class="aui_state_box"><div class="aui_state_box_bg"></div>';
popupStrVar += '  <div class="aui_alert_zn aui_outer">';
popupStrVar += '    <table class="aui_border">';
popupStrVar += '      <tbody>';
popupStrVar += '        <tr>';
popupStrVar += '          <td class="aui_c">';
popupStrVar += '            <div class="aui_inner">';
popupStrVar += '              <table class="aui_dialog">';
popupStrVar += '                <tbody>';
popupStrVar += '                  <tr>';
popupStrVar += '                    <td class="aui_header" colspan="2"><div class="aui_titleBar">';
popupStrVar += '                      <div id="aui_title_id" class="aui_title" >请选择</div>';
popupStrVar += '                        <a href="javascript:void(0);" class="aui_close" onclick="popUp_Close();return false;">×</a>';
popupStrVar += '                      </div>';
popupStrVar += '                    </td>';
popupStrVar += '                  </tr>';
popupStrVar += '                  <tr>';
popupStrVar += '                  <tr>';
popupStrVar += '                    <td class="aui_icon" style="display: none;">';
popupStrVar += '                     <div class="aui_iconBg"></div></td>';
popupStrVar += '                       <td class="aui_main">';
popupStrVar += '                        <div class="aui_content">';
popupStrVar += '                          <div id="" >';
popupStrVar += '                            <div class="data-result"><em>最多选择 <strong>2000</strong> 项</em></div>';
popupStrVar += '                            <div class="data-error" style="display: none;">最多只能选择 3 项</div>';
popupStrVar += '                            <div class="data-search" id="searchRun"><input class="run" name="searcharea"/><div class="searchList run"></div></div>';
popupStrVar += '                            <div class="data-tabs">';
popupStrVar += '                              <ul>';
popupStrVar += '                                <li onclick="removenode_area(this)" data-selector="tab-all" class="active"><a href="javascript:;"><span>全部</span><em></em></a></li>';
popupStrVar += '                              </ul>';
popupStrVar += '                            </div>';
popupStrVar += '                            <div class="data-container data-container-item">';
popupStrVar += '                            </div>';
popupStrVar += '                          </div>';
popupStrVar += '                        </div>';
popupStrVar += '                      </div>';
popupStrVar += '                    </td>';
popupStrVar += '                  </tr>';
popupStrVar += '                  <tr>';
popupStrVar += '                    <td class="aui_footer" colspan="2">';
popupStrVar += '                      <div class="aui_buttons">';
popupStrVar += '                      <button class="aui-btn aui-btn-primary" onclick="popup_Save()" type="button">确定</button>';
popupStrVar += '                        <button class="aui-btn aui-btn-light" onclick="popUp_Close()" type="button">取消</button>';
popupStrVar += '                      </div>';
popupStrVar += '                    </td>';
popupStrVar += '                  </tr>';
popupStrVar += '                </tbody>';
popupStrVar += '              </table>';
popupStrVar += '            </div></td>';
popupStrVar += '        </tr>';
popupStrVar += '      </tbody>';
popupStrVar += '    </table>';
popupStrVar += '  </div>';
popupStrVar += '</div>';

// 全局变量
var datatype        = "";
var searchValue     =  [];
var dataarrary = "";
var recentDataarrary = "";
var dataLevel=0;
var selectAllName='全选';
var handler="";
var objectId;
var objectName;
var columnName;
var recentDataUrl;
function popupSlect(_objectId,_objectName,_dataType,_dataLevel,_handler,_columnName,_recentDataUrl,dataUrl,popupType) {
	objectId=_objectId;
	objectName=_objectName;
	datatype = _dataType;
	dataLevel=_dataLevel;
	handler=_handler;
	columnName=_columnName;
	recentDataUrl=_recentDataUrl;
	var extraParam=$("#popup-extra-param").val();
	
	if(extraParam && extraParam !=""){
		dataUrl=dataUrl+extraParam;
	}
	$.ajax({
		url:dataUrl,
		type:'post',
		success:function(data) {
			var resultarrary = JSON.parse(data);
			dataarrary = JSON.parse(resultarrary[0]);
			if(popupType=="all")
				recentDataarrary = JSON.parse(resultarrary[1]);
			searchValue = searchdata();
			appendElement(popupType);
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			
		}
	});
}

function selectAll(objThis) {
	if (datatype == "duoxuan") {
		if ($(objThis).hasClass("d-item-active")) {
			$(".data-result").html("");
			for (var i=0;i<searchValue.length;i++) {
				$('a[data-code=' + searchValue[i].code + ']').removeClass('d-item-active');
			}
			$(objThis).removeClass("d-item-active");
		} else {
			if (searchValue.length > 2000) {
				$('.data-error').slideDown();
           		setTimeout("$('.data-error').hide()", 1000);
            	return false;
			} else {
				for (var i=0;i<searchValue.length;i++) {
					if ($('span[data-code=' + searchValue[i].code + ']').length > 0) {
						continue;
					}
					$('.data-result').append('<span class="svae_box aui-titlespan"  data-parent="' + searchValue[i].parent + '"data-type="' + searchValue[i].type + '"data-id="' + searchValue[i].id + '"data-level="' + searchValue[i].level + '"data-code="' + searchValue[i].code + '" data-name="' + searchValue[i].name + '" onclick="removespan_area(this)">' + searchValue[i].name + '<i>×</i></span>');
            		$('a[data-code=' + searchValue[i].code + ']').addClass('d-item-active');
				}
				$(objThis).addClass("d-item-active");
			}
		}
	} else {
		return false;
	}
}

function appendElement(popupType) {
   
    $('body').append(popupStrVar);
    $("#aui_title_id").html("请选择"+columnName);
    if (datatype == "danxuan") {
        $('.data-result').find('strong').text('1');
    } else {
        $('.data-result').html('<em>可选择多项</em>');
    }
    
    
    selectData(popupType, null, '',true);
    auto_search.run();
}

function selectData(type, con, isremove,init) {
    //显示第一级目录
    var popupStrVar = "";
    //var dataId=$('#'+key+'-id').val();
	var busienssIds =$("#"+objectId).val();
	if(type=="one"){
		 // 加载历史搜索和第一级目录
        popupStrVar += '<div class="view-all" id="">';
        popupStrVar += '    <p class="data-title"></p>';
        popupStrVar += '   <div class="data-list data-list-items">';
        popupStrVar += '  <ul class="clearfix">';
        for (var i in dataarrary) {
        	var className="d-item";
        	//已有的数据
        	if(busienssIds !="" && busienssIds.indexOf(dataarrary[i][0]) != -1 && dataarrary[i][5]=="data"){
    			$('.data-result').append('<span class="svae_box aui-titlespan" data-parent="' + dataarrary[i][6] + '"data-type="' + dataarrary[i][5] + '"data-level="' + dataarrary[i][7] + '"data-id="' + dataarrary[i][0] + '"data-code="' + dataarrary[i][8] + '" data-name="' + dataarrary[i][1] + '" onclick="removespan_area(this)">' + dataarrary[i][1] + '<i>×</i></span>');
    			className="d-item d-item-active";
        	}
        	
        	popupStrVar += '<li><a href="javascript:;" data-parent="' + dataarrary[i][6] + '"data-type="' + dataarrary[i][5] + '"data-level="' + dataarrary[i][7] + '"data-id="' + dataarrary[i][0] + '"data-code="' + dataarrary[i][8] + '" data-name="' + dataarrary[i][1] + '" class="'+className+'"  onclick="selectData(\'sub\',this,\'\')">' + dataarrary[i][1] + '</a><label>0</label></li>';
        }
        popupStrVar += ' </ul>';
        popupStrVar += '</div>';
        $('.data-container-item').html(popupStrVar);
	}
	else if (type == "all") {
        // 加载历史搜索和第一级目录
        popupStrVar += '<div class="view-all" id="">';
        popupStrVar += '  <p class="data-title">最近</p>';
        popupStrVar += '    <div class="data-list data-list-hot">';
        popupStrVar += '      <ul class="clearfix">';
        
        for(var j in recentDataarrary){
        	for (var i in dataarrary) {
        		if(dataarrary[i][5] !="data"){
        			continue;
        		}
            	var className="d-item";
            	if(init){
            		//已有的数据
            		if(busienssIds !="" && busienssIds.indexOf(dataarrary[i][0]) !=-1){
                		className="d-item d-item-active";
                	}
            	}
            	if(recentDataarrary[j] == dataarrary[i][0]){
            		popupStrVar += '<li><a href="javascript:;" title="' + dataarrary[i][1] + '" data-parent="' + dataarrary[i][6] + '"data-type="' + dataarrary[i][5] + '"data-level="' + dataarrary[i][7] + '"data-id="' + dataarrary[i][0] + '"data-code="' + dataarrary[i][8] + '" data-name="' + dataarrary[i][1] + '" class="'+className+'"  onclick="selectData(\'recent\',this,\'\')">' + dataarrary[i][1] + '</a><label>0</label></li>';
            		break;
            	}
        	}
        }
     
        popupStrVar += '      </ul>';
        popupStrVar += '    </div>';
        popupStrVar += '    <a href="javascript:;" class="data-title" onClick="selectAll(this)">全部</a>';
        popupStrVar += '   <div class="data-list data-list-items">';
        popupStrVar += '  <ul class="clearfix">';
        var existsBusinessIds="";
        for (var i in dataarrary) {
        	if(dataarrary[i][5]=="top")
        		popupStrVar += '<li><a href="javascript:;" data-parent="' + dataarrary[i][6] + '"data-type="' + dataarrary[i][5] + '"data-level="' + dataarrary[i][7] + '"data-id="' + dataarrary[i][0] + '"data-code="' + dataarrary[i][8] + '" data-name="' + dataarrary[i][1] + '" class="d-item"  onclick="selectData(\'sub\',this,\'\')">' + dataarrary[i][1] + '</a><label>0</label></li>';
        	if(init){
        		//已有的数据
            	if(busienssIds !="" && busienssIds.indexOf(dataarrary[i][0]) != -1 && dataarrary[i][5]=="data"){
            		if(existsBusinessIds.indexOf(dataarrary[i][0]) == -1){
            			$('.data-result').append('<span class="svae_box aui-titlespan" data-parent="' + dataarrary[i][6] + '"data-type="' + dataarrary[i][5] + '"data-level="' + dataarrary[i][7] + '"data-id="' + dataarrary[i][0] + '"data-code="' + dataarrary[i][8] + '" data-name="' + dataarrary[i][1] + '" onclick="removespan_area(this)">' + dataarrary[i][1] + '<i>×</i></span>');
            			existsBusinessIds+=dataarrary[i][0]+","
            		}
            		
            	}
        	}
        }
        popupStrVar += ' </ul>';
        popupStrVar += '</div>';
        $('.data-container-item').html(popupStrVar);
        //在标签上显示已经选中的数字
        $('.data-result span').each(function (index) {
            if ($('a[data-code=' + $(this).data("code") + ']').length > 0) {
                $('a[data-code=' + $(this).data("code") + ']').addClass('d-item-active');
                if ($('a[data-code=' + $(this).data("code") + ']').attr("class").indexOf('data-all') > 0) {
                    $('a[data-code=' + $(this).data("code") + ']').parent('li').nextAll('li').find('a').css({ 'color': '#ccc', 'cursor': 'not-allowed' });
                    $('a[data-code=' + $(this).data("code") + ']').parent('li').nextAll("li").find('a').attr("onclick", "");
                } else {
                    if ($('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').length > 0) {
                        var numlabel = $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').next('label').text();
                        $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').next('label').text(parseInt(numlabel) + 1).show();
                    }
                }
            } else {
                var numlabel = $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').next('label').text();
                $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').next('label').text(parseInt(numlabel) + 1).show();
            }
        });
    }
    //显示下一级
    else {
        var current_level = parseInt($(con).data("level"));
        if ($(con).data("type") != "data" && $(con).data("type") != "data-history") {
            //添加标题
            if (isremove != "remove") {
                $('.data-tabs li').each(function () {
                    $(this).removeClass('active');
                });
                $('.data-tabs ul').append('<li data-type=' + $(con).data("type") + ' data-parent=' + $(con).data("parent") + ' data-level=' + $(con).data("level") + ' data-id=' + $(con).data("id") + ' data-code=' + $(con).data("code") + ' data-name=' + $(con).data("name") + ' class="active" onclick="removenode_area(this)"><a href="javascript:;"><span>' + $(con).data("name") + '</span><em></em></a></li>');
            }
            //添加内容
            popupStrVar += '<div class="data-list data-list-items"><ul class="clearfix">';
            
            //怎么判断下一级是数据
            if (datatype == "duoxuan" && dataLevel ==(current_level+1)) {
               popupStrVar += '<li class="" style="width:100%"><a href="javascript:;" class="d-item data-all"  data-level="' + $(con).data("level") + '" data-id="' + $(con).data("id") + '"data-code="' + $(con).data("code") + '"  data-name="' + $(con).data("name") + '"  onclick="selectitem_all(this)">'+ selectAllName +'</a><label>0</label></li>';
            }
            for (var i in dataarrary) {
            	if(dataarrary[i][6]==$(con).data("id") && dataarrary[i][5] !="data-history")
            		popupStrVar += '<li><a href="javascript:;" class="d-item" data-parent="' + dataarrary[i][6] + '"data-type="' + dataarrary[i][5] + '"data-level="' + dataarrary[i][7] + '"data-id="' + dataarrary[i][0] + '"data-code="' + dataarrary[i][8] + '" data-name="' + dataarrary[i][1] + '" onclick="selectData(\'sub\',this,\'\')">' + dataarrary[i][1] + '</a><label>0</label></li>';
            }
            popupStrVar += '</ul></div>';
            $('.data-container-item').html(popupStrVar);
        } else {
            if (datatype == "duoxuan") {
                if (typeof $(con).data('flag') != 'undefined') {
                    if($('.data-result span[data-code="' + $(con).data("code") + '"]').length > 0) {
                        return false;
                    }
                }
                if ($(con).attr("class").indexOf('d-item-active') > 0) {
                    $('.data-result span[data-code="' + $(con).data("code") + '"]').remove();
                    $(con).removeClass('d-item-active');
                    if ($('.data-list-items a[data-code=' + $(con).data("code").toString().substring(0, 5*(current_level+1)) + ']').length > 0) {
                        var numlabel = $('.data-list-items a[data-code=' + $(con).data("code").toString().substring(0, 5*(current_level+1)) + ']').next('label').text();
                        if (parseInt(numlabel) == 1) {
                            $('.data-list-items a[data-code=' + $(con).data("code").toString().substring(0, 5*(current_level+1)) + ']').next('label').text(0).hide();
                        } else {
                            $('.data-list-items a[data-code=' + $(con).data("code").toString().substring(0, 5*(current_level+1)) + ']').next('label').text(parseInt(numlabel) - 1);
                        }
                    }
                    return false;
                }
                if ($('.data-result span').length > 2000) {
                    $('.data-error').slideDown();
                    setTimeout("$('.data-error').hide()", 1000);
                    return false;
                } else {
                    $('.data-result').append('<span class="svae_box aui-titlespan"  data-parent="' + $(con).data("parent") + '"data-type="' + $(con).data("type") + '"data-id="' + $(con).data("id") + '"data-level="' + $(con).data("level") + '"data-code="' + $(con).data("code") + '" data-name="' + $(con).data("name") + '" onclick="removespan_area(this)">' + $(con).data("name") + '<i>×</i></span>');
                    $(con).addClass('d-item-active');
                }
            } else {
                //单选 
                //$('.data-result span').remove();
                // // 消除搜索影响
                //$('.data-list-hot li').siblings('li').find('a').removeClass('d-item-active');
                //$('.data-container-item li').siblings('li').find('a').removeClass('d-item-active');

                //$('.data-result').append('<span class="svae_box aui-titlespan" data-id="' + $(con).data("id") + '"data-level="' + $(con).data("level") + '"data-code="' + $(con).data("code") + '" data-name="' + $(con).data("name") + '" onclick="removespan_area(this)">' + $(con).data("name") + '<i>×</i></span>');
                // $(con).parent('li').siblings('li').find('a').removeClass('d-item-active')
                //$(con).addClass('d-item-active');

                //$('.data-list-items a[data-code=' + $(con).data("code").toString().substring(0, 5) + ']').removeClass('d-item-active');
                //$('.data-list-items a[data-code=' + $(con).data("code").toString().substring(0, 5) + ']').find('label').text(0).hide();
            	//直接关闭
        	    popup_Save(con);
        	    return false;
            }
        }

        //对于最近搜索的特殊处理
        if(type == "recent"){
        	$('.data-list-items a[data-code=' + $(con).data("code").toString().substring(0, 5) + ']').next('label').text(0).hide();
        }else{
        	$('.data-result span').each(function () {
            	$('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5*(current_level+1)) + ']').next('label').text(0).hide();
            });
        }
        $('.data-result span').each(function () {
            if ($('a[data-code=' + $(this).data("code") + ']').length > 0) {
                $('a[data-code=' + $(this).data("code") + ']').addClass('d-item-active');
                if ($('a[data-code=' + $(this).data("code") + ']').attr("class").indexOf('data-all') > 0) {
                    if (datatype == "duoxuan") {
                        $('a[data-code=' + $(this).data("code") + ']').parent('li').nextAll('li').find('a').css({ 'color': '#ccc', 'cursor': 'not-allowed' });
                        $('a[data-code=' + $(this).data("code") + ']').parent('li').nextAll("li").find('a').attr("onclick", "");
                    }
                } else {
	                    if (datatype == "danxuan") {
	                        $('.data-list-items a').each(function () {
	                            $(this).find('label').text(0).hide();
	                        });
	                    }
	                    if(type == "recent"){
	                    	if ($('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').length > 0) {
		                    	var numlabel = $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').next('label').text();
		                        $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5) + ']').next('label').text(parseInt(numlabel) + 1).show();
		                    }
	                    }
                }
            } else {
                var numlabel = $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5*(current_level+1)) + ']').next('label').text();
                $('.data-list-items a[data-code=' + $(this).data("code").toString().substring(0, 5*(current_level+1)) + ']').next('label').text(parseInt(numlabel) + 1).show();
            }
        });
    }
}

function selectitem_all(con) {
	 var current_Level=parseInt($(con).data("level"));
    if (datatype == "duoxuan") {
        //多选
        if ($('.data-result span').length > 2000) {
            $('.data-error').slideDown();
            setTimeout("$('.data-error').hide()", 1000);
            return false;
        } else {
            $('.data-result span').each(function () {
                if ($(this).data("code").toString().substring(0, 5*current_Level) == $(con).data("code").toString()) {
                    $(this).remove();
                }
            })
            $(con).parent('li').siblings('li').find("a").removeClass("d-item-active");

            //if ($(con).attr("class").indexOf("d-item-active") == -1) {
               // $(con).parent('li').nextAll("li").find('a').css({ 'color': '#ccc', 'cursor': 'not-allowed' })
               // $(con).parent('li').nextAll("li").find('a').attr("onclick", "");
            //} else {
               // $(con).parent('li').nextAll("li").find('a').css({ 'color': '#0077b3', 'a.d-item-active:hover': '#fff', 'cursor': 'pointer' })
               // $(con).parent('li').nextAll("li").find('a').attr("onclick", 'selectData("sub",this,"")');
            //}
            if ($(con).attr("class").indexOf('d-item-active') > 0) {
                $('.data-result span[data-code="' + $(con).data("code") + '"]').remove();
                $(con).removeClass('d-item-active');
                return false;
            }
            
            for (var i in dataarrary) {
            	if(dataarrary[i][6]==$(con).data("id") && dataarrary[i][5]=="data"){
            		$('.data-result').append('<span class="svae_box aui-titlespan" data-parent="' + dataarrary[i][6] + '"data-type="' + dataarrary[i][5] + '"data-level="' + dataarrary[i][7] + '"data-id="' + dataarrary[i][0] + '"data-level="' + dataarrary[i][5] + '"data-code="' + dataarrary[i][8] + '" data-name="' + dataarrary[i][1] + '" onclick="removespan_area(this)">' + dataarrary[i][1] + '<i>×</i></span>');
            	}
            }
            //$(con).parent('li').siblings('li').find("a").addClass("d-item-active");
            $('.d-item').each(function () {
                if ($(this).data("code").toString().substring(0, 5*(current_Level)) == $(con).data("code").toString()) {
                    $(this).addClass('d-item-active');
                }
            })
            
            $(con).addClass('d-item-active');
        }
    } else {
        //单选
       // $('.data-result span').remove();
       // $('.data-result').append('<span class="svae_box aui-titlespan" data-level="' + $(con).data("level") + '"data-code="' + $(con).data("code") + '" data-name="' + $(con).data("name") + '" onclick="removespan_area(this)">' + $(con).data("name") + '<i>×</i></span>');
       // $(con).parent('li').siblings('li').find('a').removeClass('d-item-active')
       // $(con).addClass('d-item-active');
    }
}

function removenode_area(lithis) {
    $(lithis).siblings().removeClass('active');
    $(lithis).addClass('active');
    if ($(lithis).nextAll('li').length == 0) {
        return false;
    }
    $(lithis).nextAll('li').remove();
    if ($(lithis).data("selector") == "tab-all") {
        selectData('all', null, '');
    } else {
        selectData('sub', lithis, 'remove');
    }
}

function removespan_area(spanthis) {
    $('a[data-id=' + $(spanthis).data("id") + ']').removeClass('d-item-active');
    if ($('a[data-id=' + $(spanthis).data("id") + ']').length > 0) {
        if ($('a[data-id=' + $(spanthis).data("id") + ']').attr("class").indexOf('data-all') > 0) {
            $('a[data-id=' + $(spanthis).data("id") + ']').parent('li').nextAll('li').find('a').css({ 'color': '#0077b3', 'a.d-item-active:hover': '#fff', 'cursor': 'pointer' });
            $('a[data-id=' + $(spanthis).data("id") + ']').parent('li').nextAll("li").find('a').attr("onclick", 'selectData("sub",this,"")');
        }
    }
    var current_level=parseInt($(spanthis).data("level"));
    for(var i=1;i<=current_level-1;i++){
	    if ($('.data-list-items a[data-code=' + $(spanthis).data("code").toString().substring(0, 5*i) + ']').length > 0) {
	        var numlabel = $('.data-list-items a[data-code=' + $(spanthis).data("code").toString().substring(0, 5*i) + ']').next('label').text();
	        if (parseInt(numlabel) == 1) {
	            $('.data-list-items a[data-code=' + $(spanthis).data("code").toString().substring(0, 5*i) + ']').next('label').text(0).hide();
	        } else {
	            $('.data-list-items a[data-code=' + $(spanthis).data("code").toString().substring(0, 5*i) + ']').next('label').text(parseInt(numlabel) - 1);
	        }
	    }
	}
    $(spanthis).remove();
}

//确定选择
function popup_Save(con) {
	
    var val = '';
    var SelVal = '';
    if(con){
    	val=$(con).data("id");
    	//$(popupDataInput).data("value", $(con).data("id"));
        $("#"+objectName).val( $(con).data("name"));
        //var dataId=$('#'+key+'-id').val();
        $("#"+objectId).val($(con).data("id"));
    }else{
    	if ($('.svae_box').length > 0) {
            $('.svae_box').each(function () {
                val += $(this).data("id") + ',';
                SelVal += $(this).data("name") + ',';
            });
        }
        if (val != '') {
            val = val.substring(0, val.lastIndexOf(','));
        }
        if (SelVal != '') {
            SelVal = SelVal.substring(0, SelVal.lastIndexOf(','));
        }
        
        //$(popupDataInput).data("value", val);
        $("#"+objectName).val(SelVal);
        //var dataId=$('#'+key+'-id').val();
        $("#"+objectId).val(val);
    }
    
    if(recentDataUrl !=""){
    	 $.ajax({
			url:recentDataUrl,
			data: {'ids':val},  
			type:'post',
			success:function(data) {
				popUp_Close();
				if(handler !=""){
					if (handler instanceof Function) {
						eval(handler)();
					} else {
						eval(handler);
					}
				}
			},
	 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
	 			
			}
		});
    }else{
    	popUp_Close();
    	if(handler !=""){
			if (handler instanceof Function) {
				eval(handler)();
			} else {
				eval(handler);
			}
		}
    }
   
}

function popUp_Close() {
    $('.aui_state_box').remove();
}

function searchdata() {
    var dataArr = [];
    for (var i in dataarrary) {
        if (dataarrary[i][5] !=　"data") {
            continue;
        }
        var temp = {};
        temp.code   = dataarrary[i][8];
        temp.name   = dataarrary[i][1];
        temp.tip   = dataarrary[i][2];
        temp.pinyin = dataarrary[i][3];
        temp.py     = dataarrary[i][4];
        temp.parent     = dataarrary[i][6];
        temp.type     = dataarrary[i][5];
        temp.level     = dataarrary[i][7];
        temp.id     = dataarrary[i][0];
        
        dataArr.push(temp);
    }
    return dataArr;
}