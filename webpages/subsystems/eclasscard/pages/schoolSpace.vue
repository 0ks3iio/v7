<!--  -->
<template>
    <div class="main-container">
		<div class="grid">
			<div class="grid-cell index-main">
				<e-slide height="810px" />
			</div>
			<div class="grid-cell index-side">
                <e-notify height="710px"/>
			</div>
		</div>
	</div>
</template>

<script>
import ESlide from '../components/ESlide'
import ENotify from '../components/ENotify'
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
        ESlide,
        ENotify
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
