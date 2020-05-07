<!-- 弹出层 -->
<template>
	<transition name="opacity">
		<div class="shadow" v-if="show" @click.stop="close">
			<div class="layer-post" @click.stop="">
				<div class="article">
					<h2 class="article-title">2017级初一新生报到暨入学测试通知</h2>
					<p class="article-time">发布时间：2017-06-13 03:55:35</p>
					<div class="article-content">
						<p>欢迎杭州外国语学校2017级初一新生来校报到！现将学生信息录入、报到及入学摸底测试有关事项通告如下：</p>
						<p>一、学生信息录入（6月15—16日）</p>
						<p>为完善学生信息，请全体初一新生在家长的指导下，登录我校教学教务管理系统，在线填写《杭州外国语学校学生信息登记表》，填写信息时请确保信息准确，其中身高、体重填写近期数据，便于校服制作。填写完成后，保存、打印签字。打印好的表格，请学生于6月17日下午参加入学测试时交给监考老师。</p>
						<p>登录网址：http://www.chinahw.net/chinahw_stud/ （系统将于6月15日开放）</p>
						<p>用户类型：登录时，用户选“家长”</p>
						<p>用户名：新生身份证号码</p>
						<p>初始密码：身份证号码后6位</p>
						<p>登录后，请点左边菜单，进入“学生基本信息”模块。学生基本信息->学生信息维护->按要求完成完善信息->保存->打印（要求横向打印）。</p>
						<p>欢迎杭州外国语学校2017级初一新生来校报到！现将学生信息录入、报到及入学摸底测试有关事项通告如下：</p>
						<p>一、学生信息录入（6月15—16日）</p>
						<p>为完善学生信息，请全体初一新生在家长的指导下，登录我校教学教务管理系统，在线填写《杭州外国语学校学生信息登记表》，填写信息时请确保信息准确，其中身高、体重填写近期数据，便于校服制作。填写完成后，保存、打印签字。打印好的表格，请学生于6月17日下午参加入学测试时交给监考老师。</p>
						<p>登录网址：http://www.chinahw.net/chinahw_stud/ （系统将于6月15日开放）</p>
						<p>用户类型：登录时，用户选“家长”</p>
						<p>用户名：新生身份证号码</p>
						<p>初始密码：身份证号码后6位</p>
						<p>登录后，请点左边菜单，进入“学生基本信息”模块。学生基本信息->学生信息维护->按要求完成完善信息->保存->打印（要求横向打印）。</p>
						<p>欢迎杭州外国语学校2017级初一新生来校报到！现将学生信息录入、报到及入学摸底测试有关事项通告如下：</p>
						<p>一、学生信息录入（6月15—16日）</p>
						<p>为完善学生信息，请全体初一新生在家长的指导下，登录我校教学教务管理系统，在线填写《杭州外国语学校学生信息登记表》，填写信息时请确保信息准确，其中身高、体重填写近期数据，便于校服制作。填写完成后，保存、打印签字。打印好的表格，请学生于6月17日下午参加入学测试时交给监考老师。</p>
					</div>
				</div>
			</div><!-- E 通知公告弹出框 -->
		</div>
	</transition>
</template>

<script>
export default {
    name: '',
    components: {
    },
    data () {
        return {

        }
    },
    computed: {
	},
	methods: {
		close() {
			this.$emit('closeDetail')
		},
	},
	props: {
		show: {
			type: Boolean,
			default: false,
		}
	},
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
	.shadow{
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		background: rgba(0, 0, 0, .3);
		z-index: 9;
		.layer-post{
			width: 1000px;
			margin-left: 460px;
			background: #fff;
			height: 100%;
			overflow: scroll;
		}
	}
</style>
