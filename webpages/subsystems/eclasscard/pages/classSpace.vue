<!--  -->
<template>
    <div class="main-container">
		<div class="grid">
			<div class="grid-cell space-side">
				<div class="box">
					<div class="box-body scroll-container" style="height:810px">
						<div class="role">
							<span class="role-img"><img src="../assets/imgs/male.png" alt=""></span>
							<span class="label label-fill label-fill-bluePurple">班主任</span>
							<h4 class="role-name">{{classInfo.teacherName}}</h4>
						</div>

						<ul class="side-nav">
							<li v-for="(nav,index) in sideNav" :key="index" :class="{active: nav.active}" @click="navClicked(index)">
								<a href="javascript: void(0)" data-action="tab">
									{{nav.title}}
								</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="grid-cell space-main">
				<div class="box">
					<div class="box-header">
						<ul class="tabs">
							<li :class="{active: !tabIndex}"><a href="#honor-class" data-action="tab" @click="tabClick(0)">{{tabTitle}}</a></li>
							<li :class="{active: tabIndex}" v-if="sideNavIndex===2"><a href="#honor-personal" data-action="tab" @click="tabClick(1)">个人荣誉</a></li>
						</ul>
					</div>
					<div class="box-body scroll-container" style="height:750px">
						<div class="space-content">
							<div class="tab-content">
								<e-student v-show="sideNav[0].active" height="710px" />
								<e-photos v-show="sideNav[1].active" />
								<e-honor v-show="sideNav[2].active" :tabIndex="tabIndex"/>
								<e-introduction v-show="sideNav[3].active"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import EHonor from '../components/EHonor'
import EPhotos from '../components/EPhotos'
import EStudent from '../components/EStudent'
import EIntroduction from '../components/EIntroduction'
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
		EHonor,
		EPhotos,
		EStudent,
		EIntroduction,
    },
    data () {
        return {
			tabIndex: 0,
			sideNavIndex: 0,
			sideNav: [{title:'班内学生',active:true},{title:'班级相册',active:false},{title:'荣誉',active:false},{title:'班级简介',active:false}],
        }
	},
	methods: {
		navClicked(index) {
			for (let i of this.sideNav) {
				i.active = false
			}
			this.sideNav[index].active = true
			this.sideNavIndex = index
			this.tabIndex = 0
		},
		tabClick(index) {
			this.tabIndex = index
		}
	},
    computed: {
		...mapState({
			classInfo: store => {return store.classMsg.classInfo}
		}),
		tabTitle() {
			if(this.sideNavIndex === 2) {
				return '班级荣誉'
			}
			return this.sideNav[this.sideNavIndex].title
		}
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
