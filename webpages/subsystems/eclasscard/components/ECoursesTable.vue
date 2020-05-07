<!--  -->
<template>
    <div id="aa" role="tabpanel">
        <table class="table table-course">
            <thead>
                <tr>
                    <th width="12.5%"></th>
                    <th class="active" width="12.5%">周一</th>
                    <th width="12.5%">周二</th>
                    <th width="12.5%">周三</th>
                    <th width="12.5%">周四</th>
                    <th width="12.5%">周五</th>
                    <th width="12.5%">周六</th>
                    <th width="12.5%">周日</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td rowspan="4">上午</td>
                    <td class="active">
                        语文
                        <p>小李子</p>
                    </td>
                    <td>
                        语文
                        <p>小李子</p>
                    </td>
                    <td>
                        语文
                        <p>小李子</p>
                    </td>
                    <td>
                        语文
                        <p>小李子</p>
                    </td>
                    <td>
                        语文
                        <p>小李子</p>
                    </td>
                    <td>
                        语文
                        <p>小李子</p>
                    </td>
                    <td>
                        语文
                        <p>小李子</p>
                    </td>
                </tr>
                <tr>
                    <td class="active">语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                </tr>
                <tr>
                    <td class="active">语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                </tr>
                <tr>
                    <td class="active">语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                </tr>
                <tr><td colspan="8"></td></tr>
                <tr>
                    <td rowspan="4">下午</td>
                    <td class="active">语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                </tr>
                <tr>
                    <td class="active">语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                </tr>
                <tr>
                    <td class="active">语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                </tr>
                <tr>
                    <td class="active">语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                    <td>语文</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import ECoursesTable from '../components/ECoursesTable'
import { mapState,mapGetters } from 'vuex'
export default {
    name: '',
    components: {
        ECoursesTable
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
