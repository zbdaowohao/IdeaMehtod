<template>
    <div>
        <div id="printJS-html">
            <table class="table" id="table">
                <thead>
                    <tr>
                        <th colspan="7" style="font-size:18px;">礼宾员接待日统计表</th>
                    </tr>
                    <tr>
                        <th>商户</th>
                        <th>路局</th>
                        <th>车站</th>
                        <th>站厅</th>
                        <th>人员</th>
                        <th>接待类型</th>
                        <th>接待量(人次)</th>
                    </tr>
                </thead>
                <tbody v-if="tabsData.length > 0">
                    <tr v-for="item in tabsData" :key="item.id">
                        <!-- 商户 -->
                        <td width="6%">{{item.companyName}}</td>
                        <!-- 路局 -->
                        <td width="5%">{{item.burName}}</td>
                        <!-- 车站 -->
                        <td width="5%">{{item.stationName}}</td>
                        <!-- 站厅 -->
                        <td width="10%">{{item.roomName}}</td>
                        <!-- 人员 -->
                        <td width="5%">{{item.recistName}}</td>
                        <!-- 接待类型 -->
                        <td width="5%">{{item.receptionType}}</td>
                        <!-- 接待量(人次) -->
                        <td width="5%">{{item.recNum}}</td>
                    </tr>
                </tbody>
                <tbody v-else>
                    <tr>
                        <td colspan="7" rowspan="3" style="padding:20px 0;">暂无数据</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
<script>
import tableData from '@/static/oraginTableJson'
export default {
    data() {
        return {
            tabsDataList: tableData
        }
    },
    computed: {
        tabsData() {
            return this.tabsDataList;
        }
    }
}
</script>
<style scoped>
.inputStyle {
    width: 200px;
}

.table {
    text-align: center;
    width: 100%;
    border-collapse: collapse;
}

.table,
th,
td {
    border: 1px solid #ccc;
    height: 25px;
}

.hidden {
    display: none;
}

.none {
    display: none;
}
</style>