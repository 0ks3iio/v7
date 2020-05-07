<!--  -->
<template>
    <div class="tab-content" >
        <div id="honor-class" v-show="!tabIndex">
            <ul class="honor-list honor-list-v">
                <li class="honor-item" v-for="(honor,index) in classSpaceHonor" :key="index">
                    <div class="honor-photo-frame">
                        <div class="honor-photo-wrap"><img width="272" height="204" src="../assets/imgs/honor-cup.png" alt=""></div>
                    </div>
                    <div class="honor-info">
                        <p class="honor-name">{{honor.title}}</p>
                        <p class="honor-time">获得于{{honor.beginTime}}</p>
                    </div>
                </li>
            </ul>
        </div>
        <div id="honor-personal" v-show="tabIndex">
            <ul class="honor-list honor-list-v">
                <li class="honor-item" v-for="(honor,index) in classStuHonor" :key="index">
                    <div class="honor-photo-frame">
                        <div class="honor-photo-wrap"><img width="272" height="204" src="../assets/imgs/img.png" alt=""></div>
                    </div>
                    <div class="honor-info">
                        <h4 class="honor-winner">{{honor.studentName}}</h4>
                        <p class="honor-name">{{honor.title}}</p>
                        <p class="honor-time">获得于{{honor.beginTime}}</p>
                    </div>
                </li>
            </ul>
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
    props: {
        tabIndex: null
    },
    computed: {
        ...mapState({
            classSpaceHonor: store => {return store.classMsg.classSpaceHonor},
            classStuHonor:   store => {return store.classMsg.classStuHonor}
        })
    },
    mounted() {
        this.$store.dispatch('getClassSpaceHonor');
        this.$store.dispatch('getClassStuHonor')
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
