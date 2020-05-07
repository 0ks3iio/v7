<!-- 班级信息组件 -->
<template>
    <div class="grid-cell grid-3of10">
        <div class="box">
        <div class="box-body">
            <div class="role">
            <span class="role-img"><img src="../assets/imgs/female-teacher.png" alt=""></span>
            <span class="label label-fill label-fill-bluePurple">班主任</span>
            <h4 class="role-name">{{classInfo.teacherName}}</h4>
            </div>
            <ul class="data-list">
            <li><span>班级人数</span><span><em>{{classInfo.classStuNum}}</em></span></li>
            <li><span>请假人数</span><span><em>{{classInfo.leaveStus}}</em></span></li>
            <li>
                <span>请假名单</span>
                <span>{{classInfo.leaveStusName}}<a class="js-show-leave" @click.stop="getMore" v-if="classInfo.leaveStus>2"> 更多</a></span>
                <div class="leave-list">
                <h4>请假名单</h4>
                <div class="leave-list-content"></div>
                </div>
            </li>
            </ul>
            <div class="box-horizontal">
            <div class="box-horizontal-header">
                <h3>班级荣誉</h3>
            </div>
            <div class="box-horizontal-body">
                <swipe class="honor-list honor-list-h js-honor-slick" :auto="0" :showIndicators="false" :prevent="true" style="height:100%" v-if="honor.classHonor.length">
                    <swipe-item class="honor-item" v-for="(honor,index) in honor.classHonor" :key="index">
                        <div class="honor-photo-frame">
                        <div class="honor-photo-wrap"><img width="116" height="86" src="../assets/imgs/img.png" alt=""></div>
                        </div>
                        <div class="honor-info">
                        <h4 class="honor-winner"></h4>
                        <p class="honor-name">{{honor.name}}</p>
                        <p class="honor-time">{{honor.time}}</p>
                        </div>
                    </swipe-item>
                </swipe>
                <div class="no-data no-data-small center" v-else>
                    <div class="no-data-content">
                        <img src="../assets/imgs/no-content.png" alt="">
                        <p>当前暂无内容，请至后台添加</p>
                    </div>
                </div>
            </div>
            </div>
            <div class="box-horizontal">
            <div class="box-horizontal-header">
                <h3>个人荣誉</h3>
            </div>
            <div class="box-horizontal-body">
                <ul class="personal-honor" v-if="honor.stuHonor.length">
                    <li v-for="(honor,index) in honor.stuHonor" :key="index">
                        <div class="personal-honor-img">
                        <img src="../assets/imgs/img.png" alt="">
                        <span class="personal-honor-winner">{{honor.teacher}}</span>
                        </div>
                        <h5 class="personal-honor-title">{{honor.name}}</h5>
                    </li>
                </ul>
                <div class="no-data no-data-small center" v-else>
                    <div class="no-data-content">
                        <img src="../assets/imgs/no-content.png" alt="">
                        <p>当前暂无内容，请至后台添加</p>
                    </div>
                </div>
            </div>
            </div>
        </div>
        </div>
    </div>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
    },
    data () {
        return {

        }
    },
    methods: {
        getMore() {
            console.log(1)
        }
    },
    computed: {
        ...mapState({
            honor: store => {return {...store.honor}},
            classInfo: store => {return {...store.classMsg.classInfo}},
        }),
        ...mapGetters([
            'getLeaveList'
        ])
    },
    mounted() {
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
