/*
2015-12-09
基于jquery的评分功能
用法：
1、html：ui-rating  ---->   .ui-rating>p>span.rating-tt+(span.rating-star>a.star1+a.star2+a.star3+a.star4+a.star5)+span.rating-info
*/

//评分
$('.rating-star a').click(function(){
	$(this).addClass('current').siblings('a').removeClass('current');
	var myIndex=$(this).index()+1;
	switch (myIndex){
	case 1:
		myText='失望';
		break;
	case 2:
		myText='不满';
		break;
	case 3:
		myText='一般';
		break;
	case 4:
		myText='满意';
		break;
	case 5:
		myText='惊喜';
		break;
	}
	$(this).parent('.rating-star').siblings('.rating-info').show().text(myText);
});