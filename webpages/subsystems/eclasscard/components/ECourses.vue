<!-- 课程表组件 -->
<template>
    <div class="box">
        <div class="box-body">
            <div class="course-notice-wrapper" ref="course" style="height:770px;overflow:auto" >
            <ul class="course-notice" ref="courseList">
                <li :class="`course-item ${itemColor(index)} ${course.status}`" v-for="(course,index) in coursesForm" :key="index" ref="class">
                <div class="course-card">
                    <div class="course-teacher" v-if="showTeacher">
                        <div class="role">
                            <span class="role-img"><img src='../assets/imgs/male.png' alt=""></span>
                            <span class="label label-fill">未签到</span>
                        </div>
                    </div>
                    <ul class="course-info">
                    <li><h4>{{course.name}}<em>{{course.periodName}}</em></h4></li>
                    <li>
                        <span><i class="icon icon-teacher"></i>{{course.teacherName}}</span>
                        <span><i class="icon icon-time"></i>{{course.time}}</span>
                    </li>
                    </ul>
                </div>
                </li>
            </ul>
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
            num: ['一','二','三','四','五','六','七','八','九','十','十一','十二'],
            timOut: null,
        }
    },
    computed: {
        ...mapState({
            coursesForm: store => {return store.courses.coursesForm},
            curIndex: store => {return store.courses.curIndex},
        }),
        showTeacher() {
            return this.$route.fullPath === '/signin'
        },
    },
    methods: {
        itemColor(index) {
            switch(index % 5){
                case 0: return 'course-item-violetRed'; break;
                case 1: return 'course-item-yellow'; break;
                case 2: return 'course-item-blue'; break;
                case 3: return 'course-item-green'; break;
                case 4: return 'course-item-orange'; break;
            };
        },
        scrollTo(y, time, easing="ease-in-out") {
            let maxY = this.$refs.courseList.clientHeight - this.$refs.course.clientHeight
            y = y > maxY ? maxY : y
            let course = this.$refs.course
            let dir = ( y - course.scrollTop ) > 0
            let round = 4       //速度
            let scrollEvent = () => {
                if( course.scrollTop <= y - round || course.scrollTop >= y + round ) {
                    if( dir ) {
                        course.scrollTop += round
                    }else{
                        course.scrollTop -= round
                    }
                    this.timOut = setTimeout(scrollEvent, 16)
                }else{
                    clearTimeout(this.timOut)
                }
            }
            this.timOut = setTimeout(scrollEvent, 16)
        },
        end() {
            this.timeOut = setTimeout( () => {
                this.scrollTo(this.$refs.class[this.curIndex].offsetTop)
            },1000)
        },
    },
    watch: {
        curIndex(newVal) {
            this.timeOut = setTimeout(() => {
                this.scrollTo(this.$refs.class[newVal].offsetTop)
            }, 1000);
        }
    },
    mounted() {
        this.end()
        this.$refs.course.addEventListener('touchstart', (e) => { clearTimeout(this.timeOut) }, false)
        this.$refs.course.addEventListener('touchend', (e) => { this.end() }, false)
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
