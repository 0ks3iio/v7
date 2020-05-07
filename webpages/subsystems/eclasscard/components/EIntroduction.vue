<!--  -->
<template>
    <div class="class-introduction">
        <div class="class-introduction-imgs"><img src="../assets/imgs/img.png" alt=""></div>
        <div class="class-introduction-main">
            <div class="class-introduction-item">
                <h3>班级概况</h3>
                <p>{{classDes.introduction}}</p>
            </div>
            <div class="class-introduction-item">
                <h3>班级荣誉</h3>
                <p v-for="(honor,index) in honors" :key="index" v-if="honor">{{honor}}</p>
            </div>
            <div class="class-introduction-item">
                <h3>班级简介</h3>
                <div v-html="classDes.culture" />
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
    computed: {
        ...mapState({
            classDes: store => {return store.classMsg.classDescription}
        }),
        honors() {
            if(this.classDes.honorNames) return this.classDes.honorNames.split(",")
        }
    },
    mounted() {
        this.$store.dispatch('getDescription')
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
</style>
