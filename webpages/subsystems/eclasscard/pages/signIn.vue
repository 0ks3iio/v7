<!-- 上课签到 --> 
<template>
	<div class="main-container">
		<div class="grid">
			<div class="grid-cell space-side">
                <e-courses />
			</div>
			<div class="grid-cell space-main">
				<div class="box">
					<div class="box-body">
						<div class="checkin-container">
							<!-- S 考勤数据 -->
							<ul class="checkin-info">
								<li>应到人数：<em>55</em>人</li>
								<li>实到人数：<em>51</em>人</li>
								<li>请假人数：<em>1</em>人</li>
								<li>未到人数：<em>3</em>人</li>
								<li>迟到人数：<em>1</em>人</li>
							</ul><!-- E 考勤数据 -->
							<!-- S 学生列表 -->
							<e-student />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import EStudent from '../components/EStudent'
import ECourses from '../components/ECourses'
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
		ECourses,
		EStudent
    },
    data () {
        return {
        }
    },
    computed: {
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
</style>