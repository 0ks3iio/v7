<div id="faceBox" class="box-body">
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
<script>
<#if courseDtos?exists&&courseDtos?size gt 0>
var data = {
				monday: [
  	<#list courseDtos as item>
					{
						id: 'm1',
						avatar: '${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${item.teacherUserName!}&rnd='+Math.random(),
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
		$("#faceBox").append("<a class='btn btn-lg btn-block margin-t-20' onClick='openFaceView()'>刷 脸 签 到</a>");
	}

	var list = $('.course-notice');
	// 判断课程状态
	function judgeState(time,current){
		var temp = time.split("~");
		var startminute = temp[0].split(":")[1];
		if(current){
			startminute = startminute-10;
		}
		var start = temp[0].split(":")[0]*3600 + startminute*60;
		var end = temp[1].split(":")[0]*3600 + temp[1].split(":")[1]*60;
		var date = new Date(timestampnow);
		var now = date.getHours()*3600 + date.getMinutes()*60;
		if(now > end){
			return 'pass';
		}else if(now >= start && now <= end){
			return 'current';
		}else{
			return 'future';
		}
		
		
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

			switch(i % 5){
				case 0: itemColor = 'course-item-violetRed'; break;
				case 1: itemColor = 'course-item-yellow'; break;
				case 2: itemColor = 'course-item-blue'; break;
				case 3: itemColor = 'course-item-green'; break;
				case 4: itemColor = 'course-item-orange'; break;
			};
			<#if showTeacher>
			teastr = '<div class="course-teacher">\
						<div class="role">\
							<span class="role-img"><img src=' + courses[i].avatar +' alt=""></span>\
							' + sign + '\
						</div>\
					</div>';
			<#else>
			teastr = '';
			</#if>
			item = '<li class="course-item ' + itemColor + ' ' + judgeState(courses[i].time,courses[i].attend) + '">\
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
			$(content).append(item);
		}

		list.html('');
		list.append(content);
	}

	// 居中当前课程
	function centerCurrent(){
		var cur = $('.course-item.current');
		var container = $('.course-notice-wrapper');
		var list = $('.course-notice');
		var sum_height = 0;
		var center_top = container.innerHeight() / 2 - cur.outerHeight() / 2;
		var cur_top = 0;

		$('.course-item').each(function(){
			sum_height = sum_height + $(this).outerHeight();
		});
		var pass = $('.course-item.pass');
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
			centerCurrent();
			<#if status ==2>
				$("#teacherClockState").addClass('label-fill-red').text('迟到');
			<#elseif status ==4>
				$("#teacherClockState").addClass('label-fill-bluePurple').text('已签到');
			</#if>
		}
		initCourse();

		// 每分钟刷一次
		interval_init_course = setInterval(initCourse, 60000);
	})()
<#else>
$("#noCourseDataDiv").show();
$("#courseUlDiv").hide();
</#if>
<#if showHeight>
// 课程预告高度
if ($('.course-notice-wrapper').next().is('a')) {
	$('.course-notice-wrapper').css({
		overflow: 'auto',
		height: $(window).height() - $('.course-notice-wrapper').offset().top - 180 - 80
	});
} else {
	$('.course-notice-wrapper').css({
		overflow: 'auto',
		height: $(window).height() - $('.course-notice-wrapper').offset().top - 180
	});
}
</#if>
</script>