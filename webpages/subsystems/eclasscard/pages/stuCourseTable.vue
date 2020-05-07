<!--  -->
<template>
    <div class="main-container">
		<div class="login-stu-table center"  v-if="!cardNumber">
			<div class="login-info-main">
				<img src="../assets/imgs/card-guide.png" alt="">
				<div class="login-info-tip">查看课表请先刷卡登录</div>
			</div>
		</div>
		<div class="grid" v-else>
			<div class="grid-cell space-side">
				<div class="box">
					<div class="box-body scroll-container" style="height:810px">
						<div class="role">
							<span class="role-img"><img src="../assets/imgs/male.png" alt=""></span>
							<h4 class="role-name">小李子</h4>
						</div>
						
						<ul class="side-nav">
							<li class="active"><a href="#aa" data-action="tab">我的课表</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="grid-cell space-main">
				<div class="box">
					<div class="box-header">
						<h4 class="box-title">我的课表</h4>
					</div>
					<div class="box-body scroll-container" style="height:750px">
						<div class="space-content">
							<div class="tab-content">
								<e-courses-table />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import ECoursesTable from '../components/ECoursesTable'
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
		ECoursesTable,
    },
    data () {
        return {

        }
    },
    computed: {
		...mapState({
			cardNumber: store => {return store.stuMsg.cardNumber}
		})
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>