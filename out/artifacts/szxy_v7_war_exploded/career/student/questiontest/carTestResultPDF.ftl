<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<script src="${request.contextPath}/static/components/jquery/dist/jquery.min.js"></script>
<script src="${request.contextPath}/static/js/tool.js"></script>
<link rel="stylesheet" href="${resourceUrl}/css/pages.css" />
<link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css" />
<link rel="stylesheet" href="${resourceUrl}/css/components.css" />
<div class="page-content">
	<div class="row" id="printDiv">
		<div class="col-xs-12">
			<div class="box box-default">
				<div class="box-body clearfix">
					<div class="box-line text-center">
						<h3 class="mb20"><b>霍兰德职业兴趣量表评测报告</b></h3>
					</div>
					<div class="margin-bottom-40">
						<div class="public-title-blue-border">测评信息</div>
						<div class="font-16">
							<span class="mr150"><span class="color-999">测评人姓名：</span>${testResult.studentName!}</span>
							<span><span class="color-999">测评时间：</span>${(testResult.creationTime?string('yyyy-MM-dd HH:mm'))!}</span>
						</div>
					</div>
					<div class="margin-bottom-40">
						<div class="public-title-blue-border">答题结果</div>
						<div class="font-16">
							<div id="result1"></div>
							<div id="result2"></div>
						</div>
					</div>
					<div class="margin-bottom-40">
						<div class="public-title-blue-border">得分分布</div>
						<div id="echarts" style="width:250px;height:250px;"></div>
					</div>
					<div class="margin-bottom-40">
						<div class="public-title-blue-border">所属类型</div>
							<div class="font-16">
							<div><b>${testResult.resultType!}类型</b></div>
							<#if testResult.typelist?exists&&testResult.typelist?size gt 0>
								<#list testResult.typelist as typelist>
									<div>${typelist_index+1}、${typelist!}</div>
								</#list>
							</#if>
						</div>
					</div>
					<#if testResult.content?exists>
					<div class="margin-bottom-40">
						<div class="public-title-blue-border">典型职业</div>
						<div class="font-16">${testResult.resultType!}：${testResult.content!}</div>
					</div>
					</#if>
					<div>
						<div class="public-title-blue-border">量表介绍</div>
						<div>
							<div class="font-16 text-indent margin-bottom-40">
								<h4 class="font-18 no-text-indent"><b>霍兰德职业兴趣理论的定义</b></h4>
								<div>约翰.霍兰德于1959年提出了具有广泛社会影响的人业互择理论。这一理论首先根据劳动者的心理素质和择业倾向，将劳动者划分为6种基本类型，相应的职业也划分为6种类型：社会型（Social）、企业型（Enterprising）、现实型（Realistic）、常规型（Conventional）、研究型（Investigative）、艺术型（Artistic）。他认为，绝大多数人都可以被归于六种类型中的一种。</div>
								<div>霍兰德还认为，每一种职业的工作环境也是由六种不同的工作条件所组成，其中有一种占主导地位。一个人的职业是否成功，是否稳定，是否顺心如意，在很大程度上取决于其个性类型和工作条件之间的适应情况。</div>
								<div>霍兰德的职业选择理论，实质在于劳动者与职业的相互适应。霍兰德认为，同一类型的劳动和与职业互相结合，便是达到适应状态，结果，劳动者找到适宜的职业岗位，其才能与积极性会得以很好发挥。</div>
								<img src="${request.contextPath}/static/images/survey/survey1.jpg">
							</div>
							<div class="font-16 margin-bottom-40">
								<h4 class="font-18"><b>六种职业类型</b></h4>
								<div class="margin-bottom-40">
		   							<div><b>社会型：(S)</b></div>
		   							<div>● 共同特征：喜欢与人交往、不断结交新的朋友、善言谈、愿意教导别人。关心社会问题、渴望发挥自己的社会作用。寻求广泛的人际关系，比较看重社会义务和社会道德。</div>
		   							<div>● 典型职业：喜欢要求与人打交道的工作，能够不断结交新的朋友，从事提供信息、启迪、帮助、培训、开发或治疗等事务，并具备相应能力。如: 教育工作者（教师、教育行政人员），社会工作者（咨询人员、公关人员）。</div>
								</div>
								<div class="margin-bottom-40">
		   							<div><b>企业型：(E)</b></div>
		   							<div>● 共同特征：追求权力、权威和物质财富，具有领导才能。喜欢竞争、敢冒风险、有野心、抱负。为人务实，习惯以利益得失，权利、地位、金钱等来衡量做事的价值，做事有较强的目的性。</div>
		   							<div>● 典型职业：喜欢要求具备经营、管理、劝服、监督和领导才能，以实现机构、政治、社会及经济目标的工作，并具备相应的能力。如项目经理、销售人员，营销管理人员、政府官员、企业领导、法官、律师。</div>
								</div>
								<div class="margin-bottom-40">
		   							<div><b>常规型：(C)</b></div>
		   							<div>● 共同特征：尊重权威和规章制度，喜欢按计划办事，细心、有条理，习惯接受他人的指挥和领导，自己不谋求领导职务。喜欢关注实际和细节情况，通常较为谨慎和保守，缺乏创造性，不喜欢冒险和竞争，富有自我牺牲精神。</div>
		   							<div>● 典型职业：喜欢要求注意细节、精确度、有系统有条理，具有记录、归档、据特定要求或程序组织数据和文字信息的职业，并具备相应能力。如：秘书、办公室人员、记事员、会计、行政助理、图书馆管理员、出纳员、打字员、投资分析员。</div>
								</div>
								<div class="margin-bottom-40">
		   							<div><b>现实型：(R)</b></div>
		   							<div>● 共同特征：愿意使用工具从事操作性工作，动手能力强，做事手脚灵活，动作协调。偏好于具体任务，不善言辞，做事保守，较为谦虚。缺乏社交能力，通常喜欢独立做事。</div>
		   							<div>● 典型职业：喜欢使用工具、机器，需要基本操作技能的工作。对要求具备机械方面才能、体力或从事与物件、机器、工具、运动器材、植物、动物相关的职业有兴趣，并具备相应能力。如：技术性职业（计算机硬件人员、摄影师、机械装配工），技能性职业（木匠、厨师、技工、修理工、农民、一般劳动）。</div>
								</div>
								<div class="margin-bottom-40">
		   							<div><b>研究型：(I)</b></div>
		   							<div>● 共同特征：思想家而非实干家,抽象思维能力强，求知欲强，肯动脑，善思考，不愿动手。喜欢独立的和富有创造性的工作。知识渊博，有学识才能，不善于领导他人。考虑问题理性，做事喜欢精确，喜欢逻辑分析和推理，不断探讨未知的领域。</div>
		   							<div>● 典型职业：喜欢智力的、抽象的、分析的、独立的定向任务，要求具备智力或分析才能，并将其用于观察、估测、衡量、形成理论、最终解决问题的工作，并具备相应的能力。如科学研究人员、教师、工程师、电脑编程人员、医生、系统分析员。</div>
								</div>
								<div>
		   							<div><b>艺术型：(A)</b></div>
		   							<div>● 共同特征：有创造力，乐于创造新颖、与众不同的成果，渴望表现自己的个性，实现自身的价值。做事理想化，追求完美，不重实际。具有一定的艺术才能和个性。善于表达、怀旧、心态较为复杂。</div>
		   							<div>● 典型职业：喜欢的工作要求具备艺术修养、创造力、表达能力和直觉，并将其用于语言、行为、声音、颜色和形式的审美、思索和感受，具备相应的能力。如艺术方面（演员、导演、艺术设计师、雕刻家、建筑师、摄影家、广告制作人），音乐方面（歌唱家、作曲家、乐队指挥），文学方面（小说家、诗人、剧作家）。不善于事务性工作。</div>
								</div>
								<img style="max-width: 100%;" src="${request.contextPath}/static/images/survey/survey2.jpg">
							</div>
							<div class="font-16">
								<h4 class="font-18"><b>六种类型的内在关系</b></h4>
								<div class="margin-bottom-40">
		   							<div>霍兰德所划分的六大类型，并非是并列的、有着明晰的边界的。他以六边形标示出六大类型的关系。 </div>
		   							<div>● <b>相邻关系</b>，如 RI 、 IR 、 IA 、 AI 、 AS 、 SA 、 SE 、 ES 、 EC 、 CE 、 RC 及 CR 。属于这种关系的两种类型的个体之间共同点较多，现实型 R 、研究型 I 的人就都不太偏好人际交往，这两种职业环境中也都较少机会与人接触。 </div>
		   							<div>● <b>相隔关系</b>，如 RA 、 RE 、 IC 、 IS 、 AR 、 AE 、 SI 、 SC 、 EA 、 ER 、 CI 及 CS ，属于这种关系的两种类型个体之间共同点较相邻关系少。 </div>
		   							<div>● <b>相对关系</b>，在六边形上处于对角位置的类型之间即为相对关系，如 RS 、 IE 、 AC 、 SR 、 EI 、及 CA 即是，相对关系的人格类型共同点少，因此，一个共同人同时对处于相对关系的两种职业环境都兴趣很浓的情况较为少见。 </div>
								</div>
								<div class="margin-bottom-40">
		   							<div>人们通常倾向选择与自我兴趣类型匹配的职业环境，如具有现实型兴趣的人希望在现实型的职业环境中工作，可以最好地发挥个人的潜能。但职业选择中，个体并非一定要选择与自己兴趣完全对应的职业环境。一则因为个体本身常是多种兴趣类型的综合体，单一类型显著突出的情况不多，因此评价个体的兴趣类型时也时常以其在六大类型中得分居前三位的类型组合而成，组合时根据分数的高低依次排列字母，构成其兴趣组型，如 RCA 、 AIS 等；二则因为影响职业选择的因素是多方面的，不完全依据兴趣类型，还要参照社会的职业需求及获得职业的现实可能性。因此，职业选择时会不断妥协，寻求与相邻职业环境、甚至相隔职业环境，在这种环境中，个体需要逐渐适应工作环境。但如果个体寻找的是相对的职业环境，意味着所进入的是与自我兴趣完全不同的职业环境，则我们工作起来可能难以适应，或者难以做到工作时觉得很快乐，相反，甚至可能会每天工作得很痛苦。</div>
								</div>
							</div>
							<div class="font-16">
								<h4 class="font-18"><b>后记：</b></h4>
								<div>
		   							<div>(1)本报告书内容是依当事人的问卷回答产生的结果。其内容的真实性无法保证。而是需要与当事人进一步的沟通。</div>
		   						<div>(2)本内容运用的领域是针对个人的行为、能力、价值及管理发展上，组织管理者不能视此报告 作为唯一的评价依据，且本报告不负任何形式的法律问题。</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div><!-- /.col -->
	</div><!-- /.row -->
</div><!-- /.page-content -->
<script>
	$(function(){
		var result = "${testResult.answer!}";
		var result1 = result.substring(0,59).replaceAll(',','、').replaceAll('1','是').replaceAll('2','否');
		var result2 = result.substring(60).replaceAll(',','、').replaceAll('1','是').replaceAll('2','否');
		$("#result1").text("1-30："+result1);
		$("#result2").text("31-60："+result2);
		
		if(document.getElementById("echarts")) {
				// 基于准备好的dom，初始化echarts实例
				var myChart = echarts.init(document.getElementById("echarts"));
				window.addEventListener('resize',function(){myChart.resize();},false);
				// 指定图表的配置项和数据
				var option = {
				    tooltip: {
				        trigger: 'axis'
				    },
				    radar: [
				        {
				            indicator: [
				                {text: 'A艺术', max: 10},
				                {text: 'S社会', max: 10},
				                {text: 'E企业', max: 10},
				                {text: 'C常规', max: 10},
				                {text: 'R实际', max: 10},
				                {text: 'I调研', max: 10}
				            ]
				        }
				    ],
				    series: [
				        {
				            type: 'radar',
				             tooltip: {
				                trigger: 'item'
				            },
				            itemStyle: {normal: {color: '#7fa9fb',areaStyle: {type: 'default'}}},
				            areaStyle: {normal: {color: '#7fa9fb'}},
				            data: [
				                {
				                    value: [${testResult.scoreA!},${testResult.scoreS!},${testResult.scoreE!},${testResult.scoreC!},${testResult.scoreR!},${testResult.scoreI!}],
				                    name: '得分分布'
				                }
				            ]
				        }
				    ]
				};
			// 使用刚指定的配置项和数据显示图表
			myChart.setOption(option);
		}
	});
</script>