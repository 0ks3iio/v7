<!--  -->
<template>
    <div class="main-container">
		<div v-if="cardNumber || !pswCheck">
			<div class="login-container">
				<div class="login-box">
					<div class="login-body">
						<div class="login-info">
							<div class="login-info-main">
								<img src="../assets/imgs/card-guide.png" alt="">
								<div class="login-info-tip">登录请先刷卡识别用户名</div>
							</div>
							
						</div>
						<div class="login-form">
							<div class="login-form-main">
								<h3>空间登录</h3>
								<div class="form-group">
									<span><i class="icon icon-user"></i></span>
									<p>请刷卡识别用户名</p>
								</div>
								<div class="form-group">
									<span><i class="icon icon-lock"></i></span>
									<input type="password" v-model="stuPsw" placeholder="请输入密码">
									<div class="login-form-tip"><i></i>密码错误，请重新输入</div>
								</div>
								<button class="btn btn-lg btn-block" @click.stop="stuCheckIn">登&emsp;录&emsp;<span class="icon icon-arrow-right"></span></button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<p class="copyleft">&copy;2017 万朋教育 版权所有</p>
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
							<li v-for="(nav,index) in sideNav" :key="index" :class="{active:nav.active}" @click="navClicked(index)"><a href="javascript:void(0)">{{nav.title}}</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="grid-cell space-main">
				<div class="box">
					<div class="box-header">
						<h4 class="box-title">{{title}}</h4>
					</div>
					<div class="box-body scroll-container" style="height:750px">
						<div class="space-content">
							<div class="tab-content">
								<e-courses-table v-if="sideNav[0].active" />
								<e-leave-paper v-if="sideNav[1].active" />
								<e-health-toolbar v-if="sideNav[2].active" />
								<e-honor v-if="sideNav[3].active" />
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
import ELeavePaper from '../components/ELeavePaper'
import EHealthToolbar from '../components/EHealthToolbar'
import EHonor from '../components/EHonor'
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
		ECoursesTable,
		ELeavePaper,
		EHealthToolbar,
		EHonor,
    },
    data () {
        return {
			title: '我的课表',
			stuPsw: '',
			sideNav: [{title: '我的课表', active:true}, {title: '我的请假', active:false},{title: '健康数据', active: false}, {title: '我的荣誉', active: false}]
        }
	},
	methods: {
		navClicked(index) {
			for (let i in this.sideNav) {
				this.sideNav[i].active = (i == index) ? true : false
			}
			this.title = this.sideNav[index].title
		},
		stuCheckIn() {
			if(!this.stuPsw){
				return
			}
			this.$store.dispatch('getStuLoginUser', this.stuPsw)
		}
	},
    computed: {
		...mapState({
			cardNumber: store => {return store.stuMsg.cardNumber},
			pswCheck: store => {return store.stuMsg.pswCheck}
		})
	},
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
	.scroll-container{
		overflow: auto
	}
</style>
