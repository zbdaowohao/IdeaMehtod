<template>
    <div>
        <img src="../assets/logo.png">
        <div style="width:10%; margin: 0 auto">
            <button type="ghost" @click="printJSFun()">
                Print Web
            </button>
        </div>
        <div id="printJS-html" style="margin-top: 20px;">
            <TableData />
            <br>
            <ImagePic />
        </div>
    </div>
</template>
<script>
import printJS from 'print-js';
// @ is an alias to /src
import tableData from '@/static/oraginTableJson'
import TableData from '@/components/TableData'
import ImagePic from '@/components/ImagePic'
export default {
    name: 'PrintPlugin',
    components: {
        TableData,
        ImagePic
    },
    data() {
        return {

        };
    },
    mounted() {

    },
    methods: {
        printJSFun() {
            let printData = [];
            tableData.forEach(item => {
                printData.push({
                    '商户': item.companyName,
                    '路局': item.burName,
                    '车站': item.stationName,
                    '站厅': item.roomName,
                    '人员': item.recistName,
                    '接待类型': item.receptionType,
                    '接待量(人次)': item.recNum
                })
            })
            printJS({
                printable: printData,
                type: 'json',
                properties: ['商户', '路局', '车站', '站厅', '人员', '接待类型', '接待量(人次)'],
                header: '<h2 class="header-style">礼宾员接待日统计表</h2>',
                style: '.header-style { text-align: center; }'
            })
        }
    }
}
</script>