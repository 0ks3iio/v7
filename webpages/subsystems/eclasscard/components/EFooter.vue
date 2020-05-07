<!-- 底部路由组件 -->
<template>
    <footer class="footer">
		<ul class="menu-list">
			<li v-for="(nav,index) in navList"  :class="{active: fullPath === nav.routerPath}" :key="index">
				<router-link :to="nav.routerPath">
					<span :class="`icon ${nav.icon}`"></span>
					<h5 class="menu-name">{{nav.title}}</h5>
				</router-link>
			</li>
		</ul>
	</footer>
</template>

<script>
export default {
    name: 'efooter',
    components: {
    },
    data () {
        return {
			navList: [
				{routerPath: '/', icon: 'icon-home', title: '首页'},
				{routerPath: '/signin', icon: 'icon-sign', title: '上课签到'},
				{routerPath: '/classSpace', icon: 'icon-classSpace', title: '班级空间'},
				// {routerPath: '/schoolSpace', icon: 'icon-schoolSpace', title: '校园空间'},
				// {routerPath: '/activity', icon: 'icon-activity', title: '活动'},
				{routerPath: '/stuCourseTable', icon: 'icon-stuCourseTable', title: '学生课表'},
				{routerPath: '/stuSpace', icon: 'icon-stuSpace', title: '学生空间'},
			]
        }
    },
    computed: {
		fullPath() {
			return this.$route.fullPath
		}
	},
	methods: {
	
	},
    mounted() {
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
