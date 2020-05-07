<!-- 请假条 -->
<template>
    <div id="cc" role="tabpanel">
        <ul class="leave-paper-type clearfix">
            <li :class="{active: type.active}" v-for="(type, index) in leaveType" :key="index" @click="typeClicked(index)">
                <a href="javascript:void(0)">{{type.title}}</a>
            </li>
        </ul>

        <div class="leave-paper">
            <table class="table table-leave">
                <thead>
                    <tr>
                        <th class="text-center" colspan="6">请假单</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th width="14.28%">学生姓名：</th>
                        <td width="19.28%">韩雪</td>
                        <th width="14.28%">班级：</th>
                        <td width="19.28%">高一（3）班</td>
                        <th width="14.28%">寝室号：</th>
                        <td width="19.28%">3#333</td>
                    </tr>
                    <tr>
                        <th width="14.28%">开始时间：</th>
                        <td width="19.28%">
                            <input type="text" class="datatimepicker" placeholder="请选择">
                        </td>
                        <th width="14.28%">结束时间：</th>
                        <td width="19.28%">
                            <input type="text" class="datatimepicker" placeholder="请选择">
                        </td>
                        <th width="14.28%">天数：</th>
                        <td width="19.28%">2天</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="14.28%">事由：</th>
                        <td width="83.33%" colspan="5">因身体不适，请假去医院</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="14.28%">床位是否保留：</th>
                        <td width="83.33%" colspan="5">
                            <label><input type="radio" name="radio" class="wp"><span class="lbl"> 保留</span></label>
                            <label><input type="radio" name="radio" class="wp"><span class="lbl"> 不保留</span></label>
                        </td>
                    </tr>
                    <tr>
                        <th width="14.28%">通校居住地：</th>
                        <td width="19.28%">
                            <input type="text" placeholder="请输入">
                        </td>
                        <th width="14.28%">陪住人姓名：</th>
                        <td width="19.28%">
                            <input type="text" placeholder="请输入">
                        </td>
                        <th width="14.28%">与本人关系：</th>
                        <td width="19.28%">
                            <input type="text" placeholder="请输入">
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="leave-paper-btn"><button class="btn btn-lg">提交申请</button></div>
        </div>


        <div class="leave-paper">
            <span class="icon icon-passed">审核通过</span>
            <table class="table table-leave">
                <thead>
                    <tr>
                        <th class="text-center" colspan="4">杭州外国语学校（请假单）</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th width="16.67%">学生姓名：</th>
                        <td width="33.33%">韩雪</td>
                        <th width="16.67%">班级：</th>
                        <td width="33.33%">高一（3）班</td>
                    </tr>
                    <tr>
                        <th width="16.67%">寝室号：</th>
                        <td width="33.33%">3#333</td>
                        <th width="16.67%">起止时间：</th>
                        <td width="33.33%">2017-09-04 至 2017-09-06&emsp;共3天</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">事由：</th>
                        <td width="83.33%" colspan="3">因身体不适，请假去医院</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">班主任审核：</th>
                        <td width="33.33%">同意。廖明</td>
                        <th class="cell-big" width="16.67%">年级组长：</th>
                        <td width="33.33%">同意。廖明</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">学生处领导：</th>
                        <td width="33.33%">同意。廖明</td>
                        <th class="cell-big" width="16.67%">分管校领导：</th>
                        <td width="33.33%">同意。廖明</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="leave-paper">
            <p>申请时间：2017-09-04</p>
            <span class="icon icon-passed">审核通过</span>
            <table class="table table-leave">
                <thead>
                    <tr>
                        <th class="text-center" colspan="4">杭州外国语学校（临时出校申请单）</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th width="16.67%">学生姓名：</th>
                        <td width="33.33%">韩雪</td>
                        <th width="16.67%">班级：</th>
                        <td width="33.33%">高一（3）班</td>
                    </tr>
                    <tr>
                        <th width="16.67%">出校时间能联系到的号码：</th>
                        <td width="33.33%">18711112222</td>
                        <th width="16.67%">起止时间：</th>
                        <td width="33.33%">2017-09-04 13:00 至 2017-09-04 15:00</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">事由：</th>
                        <td width="83.33%" colspan="3">因身体不适，请假去医院</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">班主任审核：</th>
                        <td width="83.33%" colspan="3">同意。廖明</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="leave-paper">
            <p>申请时间：2017-09-04</p>
            <span class="icon icon-passed">审核通过</span>
            <table class="table table-leave">
                <thead>
                    <tr>
                        <th class="text-center" colspan="4">杭州外国语学校（暂时通校、住校申请单）</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th width="16.67%">学生姓名：</th>
                        <td width="33.33%">韩雪</td>
                        <th width="16.67%">班级：</th>
                        <td width="33.33%">高一（3）班</td>
                    </tr>
                    <tr>
                        <th width="16.67%">寝室号：</th>
                        <td width="33.33%">3#333</td>
                        <th width="16.67%">通住校选择：</th>
                        <td width="33.33%">通校</td>
                    </tr>
                    <tr>
                        <th width="16.67%">起止时间：</th>
                        <td width="83.33%" colspan="3">2017-09-04 至 2017-09-06&emsp;共3天</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">事由：</th>
                        <td width="83.33%" colspan="3">因身体不适，请假去医院</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">班主任审核：</th>
                        <td width="33.33%">同意。廖明</td>
                        <th class="cell-big" width="16.67%">年级组长：</th>
                        <td width="33.33%">同意。廖明</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="leave-paper">
            <p>申请时间：2017-09-04</p>
            <span class="icon icon-passed">审核通过</span>
            <table class="table table-leave">
                <thead>
                    <tr>
                        <th class="text-center" colspan="4">杭州外国语学校（长期通校申请单）</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th width="16.67%">学生姓名：</th>
                        <td width="33.33%">韩雪</td>
                        <th width="16.67%">班级：</th>
                        <td width="33.33%">高一（3）班</td>
                    </tr>
                    <tr>
                        <th width="16.67%">寝室号：</th>
                        <td width="33.33%">3#333</td>
                        <th width="16.67%">起止时间：</th>
                        <td width="33.33%">2017-09-04 至 2017-09-06&emsp;共3天</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">事由：</th>
                        <td width="83.33%" colspan="3">因身体不适，请假去医院</td>
                    </tr>
                    <tr>
                        <th width="16.67%">床位是否保留：</th>
                        <td width="33.33%">是</td>
                        <th width="16.67%">通校居住地：</th>
                        <td width="33.33%">大明湖畔</td>
                    </tr>
                    <tr>
                        <th width="16.67%">陪住人姓名：</th>
                        <td width="33.33%">弘历</td>
                        <th width="16.67%">与本人关系：</th>
                        <td width="33.33%">阿玛</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">家长签名：</th>
                        <td width="33.33%">弘历</td>
                        <th class="cell-big" width="16.67%">班主任审核：</th>
                        <td width="33.33%">同意。廖明</td>
                    </tr>
                    <tr>
                        <th class="cell-big" width="16.67%">年级组长：</th>
                        <td width="33.33%">同意。廖明</td>
                        <th class="cell-big" width="16.67%">学生处领导：</th>
                        <td width="33.33%">同意。廖明</td>
                    </tr>
                </tbody>
            </table>
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
            leaveType: [{title: '请假单', active: true}, {title: '临时出校申请单', active: false}, {title: '临时住校、通校申请单', active: false}, {title: '长期通校申请单', active: false}]
        }
    },
    methods: {
        typeClicked(index) {
            for (let i in this.leaveType) {
                this.leaveType[i].active = (i == index) ? true : false
            }
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
