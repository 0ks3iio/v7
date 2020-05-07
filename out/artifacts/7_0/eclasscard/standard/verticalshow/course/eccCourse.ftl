<#if showTeacher>
<div class="box-body no-padding">
	<div class="checkin-container" id="faceBox">
		<!-- S 课程预告 -->
		<div class="checkin-course">
			<ul id="courseUlDiv" class="course-list"></ul>
			<div id="noCourseDataDiv" class="no-data center" style="display:none">
			<div class="no-data-content">
				<img src="${request.contextPath}/static/eclasscard/standard/verticalshow/images/no-content.png" alt="">
				<p>今日没有课程安排哦~</p>
			</div>
			</div>
		</div><!-- E 课程预告 -->
		
	</div>
</div>
<#else>
<div class="box-body">
	<div class="course-notice-wrapper">
		<ul id="courseUlDiv" class="course-notice"></ul>
		<div id="noCourseDataDiv" class="no-data center" style="display:none">
			<div class="no-data-content">
				<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
				<p>今日没有课程安排哦~</p>
			</div>
		</div>
	</div>
</div>
</#if>
<script>
<#if !showTeacher>
// 课程预告高度
$('.course-notice-wrapper').css({
	overflow: 'auto',
	height: $('.same-height').height()-40
});
</#if>
<#if courseDtos?exists&&courseDtos?size gt 0>
var data = {
				monday: [
  	<#list courseDtos as item>
					{
						id: 'm1',
						avatar: <#if item.attend>'${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${item.teacherUserName!}&rnd='+Math.random()<#else>'${request.contextPath}/static/eclasscard/standard/verticalshow/images/male.png'</#if>,
						teacher: '${item.teacherName!}',
						attend: <#if item.attend>true<#else>false</#if>,
						period: '${item.periodName!}',
						name: '${item.name!}',
						time: '${item.time!}'
					}<#if item_index lt courseDtos?size>,</#if>
    </#list>
				]
			};

// 课程预告
(function(){
	var activeTab = $(".menu-list li[class='active']").attr('id');
	if (isActivate == "true" && activeTab == "footTabClsClock") {
		<#if showTeacher>
			$("#faceBox").append("<div class='text-center'><button class='btn btn-lg btn-made' onClick='openFaceView()'>刷 脸 签 到</button></div>");
		</#if>
	}
	var container = $('#courseUlDiv');
	// 判断课程状态
	var now_second_time = 0;
	function judgeState(time,current,isNeed){
		var temp = time.split("~");
		var startminute = temp[0].split(":")[1];
		if(current){
			startminute = startminute-10;
		}
		var start = temp[0].split(":")[0]*3600 + startminute*60;
		var end = temp[1].split(":")[0]*3600 + temp[1].split(":")[1]*60;
		var date = new Date(timestampnow);
		var now = date.getHours()*3600 + date.getMinutes()*60;
		if(isNeed){
			now_second_time = now;
		}else{
			now = now_second_time;
		}
	    <#if showTeacher>
			if(now > end){
				return 'pass';
			}else if(now >= start && now <= end){
				return 'current';
			}else{
				return 'future';
			}
		<#else>
			if(now > end){
				return 'pass-side';
			}else if(now >= start && now <= end){
				return 'current-side';
			}else{
				return 'future-side';
			}
		</#if>
		
	}

	// 渲染内容
	function rendar(courses){
		var content = document.createDocumentFragment();
		var itemColor;

		for(var i=0; i<courses.length; i++){
			var sign,item,teastr;

			if(courses[i].attend){
				sign = '<span id="teacherClockState" class="label label-fill">未签到</span>'
			}else{
				sign = '';
			}

			<#if showTeacher>
			switch(i % 5){
				case 0: itemColor = 'course-item-violetRed'; break;
				case 1: itemColor = 'course-item-yellow'; break;
				case 2: itemColor = 'course-item-blue'; break;
				case 3: itemColor = 'course-item-green'; break;
				case 4: itemColor = 'course-item-orange'; break;
			};
			teastr = '<div class="course-teacher">\
						<div class="role">\
							<span class="role-img"><img src=' + courses[i].avatar +' alt=""></span>\
							' + sign + '\
						</div>\
					</div>';
			item = '<li class="course-item ' + itemColor + ' ' + judgeState(courses[i].time,courses[i].attend,i==0) + '">\
						<div class="course-card">\
							' + teastr + '\
							<ul class="course-info">\
								<li><h4>' + courses[i].name + '<em>'+courses[i].period+'</em></h4></li>\
								<li>\
									<span><i class="icon icon-teacher"></i>' + courses[i].teacher + '</span>\
									<span><i class="icon icon-time"></i>' + courses[i].time + '</span>\
								</li>\
							</ul>\
						</div>\
					</li>';
			<#else>
			switch(i % 5){
				case 0: itemColor = 'course-item-violetRed-side'; break;
				case 1: itemColor = 'course-item-yellow-side'; break;
				case 2: itemColor = 'course-item-blue-side'; break;
				case 3: itemColor = 'course-item-green-side'; break;
				case 4: itemColor = 'course-item-orange-side'; break;
			};
			item = '<li class="course-item-side ' + itemColor + ' ' + judgeState(courses[i].time,courses[i].attend,i==0) + '">\
						<div class="course-card-side">\
							<ul class="course-info-side">\
								<li><h4>' + courses[i].name + '<em>'+courses[i].period+'</em></h4></li>\
								<li>\
									<span><i class="icon icon-teacher"></i>' + courses[i].teacher + '</span>\
									<span><i class="icon icon-time"></i>' + courses[i].time + '</span>\
								</li>\
							</ul>\
						</div>\
					</li>';
			</#if>
			$(content).append(item);
		}

		container.html('');
		container.append(content);
	}

	// 居中当前课程
	function centerCurrent(){
		var cur = $('.course-item.current');
		var container = $('.checkin-course');
		var list = $('.course-list');
		var sum_width = 0;
		var center_left = $(window).innerWidth() / 2 - $('.course-item').outerWidth() / 2 - container.offset().left;
		var cur_left = 0;

		$('.course-item').each(function(){
			sum_width = sum_width + $(this).outerWidth();
		});
		var pass = $('.course-item.pass');
		var p_length = pass.length;
		if(cur.length <= 0){
			p_length = p_length - 1; 
		}
		for(var i=0; i<p_length; i++){
			cur_left = cur_left + pass.eq(i).outerWidth();
		}
		
		list.outerWidth(sum_width);

		var s_max = sum_width - container.outerWidth();

		if(cur_left > center_left && cur_left - center_left > s_max){
			container.css({
				'padding-right': cur_left - center_left - s_max +'px'
			})
			container.scrollLeft(cur_left - center_left);
		}else if(cur_left < center_left){
			container.scrollLeft(0);
		}else{
			container.scrollLeft(cur_left - center_left);
		}
	}


	// 居中当前课程2
	function centerCurrent2(){
		var cur = $('.course-item-side.current');
		var container = $('.course-notice-wrapper');
		var list = $('.course-notice');
		var sum_height = 0;
		var center_top = container.innerHeight() / 2 - cur.outerHeight() / 2;
		var cur_top = 0;

		$('.course-item-side').each(function(){
			sum_height = sum_height + $(this).outerHeight();
		});
		var pass = $('.course-item-side.pass-side');
		var p_length = pass.length;
		if(cur.length <= 0){
			p_length = p_length - 1;
		}
		for(var i=0; i<p_length; i++){
			cur_top = cur_top + pass.eq(i).outerHeight();
		}
		
		list.outerHeight(sum_height);

		var s_max = sum_height - container.innerHeight();

		if(cur_top > center_top && cur_top - center_top > s_max){
			container.scrollTop(s_max);
		}else if(cur_top < center_top){
			container.scrollTop(0);
		}else{
			container.scrollTop(cur_top - center_top);
		}
	}
		function initCourse(){
			rendar(data.monday);
			<#if showTeacher>
				centerCurrent();
			<#else>
				centerCurrent2();
			</#if>
		}
		initCourse();
		<#if showTeacher>
			<#if status ==2>
				$("#teacherClockState").addClass('label-fill-red').text('迟到');
			<#elseif status ==4>
				$("#teacherClockState").addClass('label-fill-bluePurple').text('已签到');
			</#if>
		</#if>

		// 每分钟刷一次
		interval_init_course = setInterval(initCourse, 60000);
	})()
	
<#else>
$("#noCourseDataDiv").show();
$("#courseUlDiv").hide();
</#if>
</script>