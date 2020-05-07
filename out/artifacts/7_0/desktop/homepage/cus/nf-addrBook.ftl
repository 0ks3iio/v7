<#import  "/desktop/homepage/ap/addrBook/addrBook-showTemplate.ftl" as s />

<a class="js-dropbox-mailList js-dropbox-toggle" href="#"  title="通讯录">
		<i class="wpfont icon-directory"></i>
</a>
<div class="dropbox dropbox-mailList">
	<div class="dropbox-container">
		<!-- Tab panes -->
		<div class="mail-search">
			<div class="input-icon">
				<i class="fa fa-search" onclick = "findUserName()"></i>
				<input type="text" id="userName" class="form-control typeahead addressBookUserName" placeholder="输入姓名搜索">
				<a href="javascript:void(0);" class="mail-search-close"><i class="fa fa-times-circle"></i></a>
			</div>
		</div>
		<div class="mail-search-result">
		 <ul class="user-list user-list-hover">
			<#-- 搜索结果-->
		 </ul> 
		</div>
		<div class="mail-list-wrap">
			<ul class="nav nav-tabs nav-justified nav-mail-list" role="tablist">
			<#--<li role="presentation" class="active">
					<a href="#a" role="tab" data-toggle="tab">
						<i class="fa fa-commenting"></i>
					</a>
				</li> -->
				<li role="presentation" class="active" >
					<a href="#b" role="tab" data-toggle="tab"  >
						<i class="fa fa-user"></i>
					</a>
				</li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content no-padding">
			<#--	<div role="tabpanel" class="tab-pane active" id="a">	--> 			
				  <#-- 会话列表-->
			<#--	</div> -->
				<div role="tabpanel" class="tab-pane active" id="b">
					<div class="panel-group" role="tablist" aria-multiselectable="true">
							<#if adList?exists && adList?size gt 0>
								<#list adList as adGroup>
									<#assign groupId=UuidUtils.generateUuid() />
									<div class="panel panel01">
							     	 	<div class="panel-heading" role="tab">
											<h4 class="panel-title">
												<a data-toggle="collapse" href="#${groupId!}" aria-expanded="true" aria-controls="${groupId!}"><i class="fa fa-caret-right"></i>${adGroup.groupName}</a>
											</h4>
										</div> 
										<div id="${groupId!}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
										  	<div class="panel-body"    style= "width:280px;  overflow:auto;">
										  		<@s.showTempla2 userLists=adGroup.userDetails />	
								        	</div> 
								       	</div>
								    </div>			  
								</#list>
							</#if>						   	
					</div>
				</div>  <#--tabpanel -->
			</div>
		</div>
	</div>			
</div>
<script>
	$(document).ready(function(){
        $(".user-avatar-template").each(function(){
            var iconUrl = $(this).attr("src");
            $(this).attr("src",iconUrl+"?"+new Date().getTime());
		});
        $(".addressBookUserName").keypress(function () {
			displayResult();
        });
   	 });
	// 下拉弹出框
	$('.js-dropbox-toggle').on('click',function(){
		$(this).next().toggle()
		.closest('li').toggleClass('open')
		.siblings().removeClass('open').find('.dropbox').hide();
		scrollMailList();
	})
	
	// 通讯录内容部分滚动
	function scrollMailList(){
		var $userContainer=$('.mail-list-wrap .tab-content');
		var $maxHeight=$(window).height()-$userContainer.offset().top;
		var $resultContainer=$('.mail-search-result .user-list');
		var $resultHeight=$(window).height()-$resultContainer.offset().top;
		
		wpScroll($userContainer,$maxHeight);
		wpScroll($resultContainer,$resultHeight);
	}
		
	$(window).on('resize',function(){
		scrollMailList();
	}).trigger('resize')
	
	function wpScroll(container, height){
        container.slimscroll({
            height: height
        })
    }
	
// 通讯录分组下拉展开
	$('.panel-collapse').on('shown.bs.collapse',function(){
		$(this).parent().find('.panel-title i')
		.removeClass('fa-caret-right').addClass('fa-caret-down');
	}).on('hidden.bs.collapse',function(){
		$(this).parent().find('.panel-title i')
		.removeClass('fa-caret-down').addClass('fa-caret-right');
	})

	// 通讯录搜索
	$('.mail-search input').on('focus',function(){
		$(this).closest('.input-icon').addClass('focus');
		$('.mail-search-result').removeClass('hidden').addClass('show');
		$('.mail-list-wrap').removeClass('show').addClass('hidden');
		scrollMailList();
	})
	$('.mail-search-close').on('click',function(){
		findName();
		$(this).closest('.input-icon').removeClass('focus').find('input').val('');
		$('.mail-search-result').removeClass('show').addClass('hidden');
		$('.mail-list-wrap').removeClass('hidden').addClass('show');
		scrollMailList();
	})
	$('.input-icon>i').on('click', function(){
		$(this).next().find('input').focus();
	})

	// 查看通讯录好友信息
	$('#c[data-toggle=show-user-detail]').on('click',function(){
		if($(this).parent().hasClass('open')){
			$(this).parent().removeClass('open');
		}else{
			$(this).parent().addClass('open').siblings().removeClass('open');
		}
	});
    
    // 模糊查询
	var substringMatcher = function(strs) {
	    
	    return function findMatches(q, cb) {
	        
	    	var matches, substringRegex;

	    	// an array that will be populated with substring matches
	    	matches = [];

	    	// regex used to determine if a string contains the substring `q`  \b[a-z]*\b
	    	if(q == '*' || q =='+' || q == '?' || q == '\\' || q.indexOf ('*') != -1 || q.indexOf ('+') != -1 || q.indexOf ('(') != -1 || q.indexOf (')') != -1){
	    	  substrRegex = new RegExp('/q/', 'i');
	    	}else{
	    	  substrRegex = new RegExp(q, 'i');
	    	}

	    	// iterate through the pool of strings and for any string that
	    	// contains the substring `q`, add it to the `matches` array
	    	$.each(strs, function(i, str) {
		        if (substrRegex.test(str)) {
		        	matches.push(str);
		        }
	    	});

	    	cb(matches);
	    };
	};
   var states = new Array();
   <#if userNames ?? && userNames?size gt 0>     
   <#list userNames as userName >           
     states[${userName_index}] =  "${userName}"; 
   </#list>  
   </#if>
    
     
     
	 $('.typeahead').typeahead({
	    hint: true,
	    highlight: true,
	    minLength: 1
	},{ 
	 //   items:5, 
	    name: 'states',
	    source: substringMatcher(states),
	  //  afterSelect:findUserName(),
	 //   highlighter: findUserName ($("#userName").val()),  
         updater: findUserName () ,  
         afterSelect: function () {
                    //选择项之后的事件 ，item是当前选中的。
          },
                   // 最多显示项目数量
    //    itemSelected: findUserName (),     // 当选中一个项目的时候，回调函数

	}); 
	
	// 点击查找通讯录信息
	$('.tt-menu').on('click',function(){
		findUserName();
	});
    
	
	
    
    function findUserName(){ 
      var userName = $("#userName").val();
      userName =  encodeURI(userName);      
      $(".mail-search-result .user-list-hover").load("${request.contextPath}/desktop/mailList/findUserName?userName="+userName);      
    }
    function findName(){  
      var userName=" ";  
      $(".mail-search-result .user-list-hover").load("${request.contextPath}/desktop/mailList/findUserName?userName="+userName);      
    }
    
    function displayResult(){	
		var x;
        if(window.event){
        	 // IE8 以及更早版本
        	x=event.keyCode;
        }else if(event.which){
        	// IE9/Firefox/Chrome/Opera/Safari
            x=event.which;
        }
        if(13==x){
            findUserName();
        }
    }
    

    
</script>
