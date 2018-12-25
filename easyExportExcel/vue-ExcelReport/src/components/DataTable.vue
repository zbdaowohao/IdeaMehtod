<!-- dis是控制rowsoan合并隐藏的,dis1是控制colsoan合并隐藏的 -->
<template>
  <div ref="tableWrapper" style="overflow: hidden;display: inline-block;">
    <div style="width:10%; position: absolute; right: 0; top: 50px;">
        <Button type="ghost" @click="exportData()">导出</Button>
    </div>
        <table class="table" id="tableId">
            <thead>
                <tr>
                    <th>商户</th>
                    <th>路局</th>
                    <th>车站</th>
                    <th>站厅</th>
                    <th>日期</th>
                    <th>销售额(元)</th>
                    <th>支付宝(元)</th>
                    <th>微信(元)</th>
                    <th>POS(元)</th>
                    <th>现金(元)</th>
                </tr>
            </thead>
            <tbody v-if="tabsData.length > 0">
                <tr v-for="item in tabsData" :key="item.id">
                    <!-- 商户 -->
                    <td width="10%" :rowspan="item.companyAbbreviationspan" :colspan="item.companyAbbreviationcols" :class="{hidden: item.companyAbbreviationdis,none:item.companyAbbreviationdis1,rig:item.companyAbbreviationcols>1?true:false}">{{item.companyAbbreviation}}</td>
                    <!-- 路局 -->
                    <td width="10%" :rowspan="item.bureauNamespan" :colspan="item.bureauNamecols" :class="{hidden: item.bureauNamedis,none:item.bureauNamedis1,rig:item.bureauNamecols>1?true:false}">{{item.bureauName}}</td>
                    <!-- 车站 -->
                    <td width="10%" :rowspan="item.stationNamespan" :colspan="item.stationNamecols"   :class="{hidden: item.stationNamedis,none:item.stationNamedis1,rig:item.stationNamecols>1?true:false}">{{item.stationName}}</td>
                    <!-- 站厅 -->
                    <td width="10%" :rowspan="item.roomNamespan" :colspan="item.roomNamecols"  :class="{hidden: item.roomNamedis,none:item.roomNamedis1,rig:item.roomNamecols>1?true:false}">{{item.roomName}}</td>
                    <!-- 日期 -->
                    <td width="5%" :colspan="item.orderDaycols" :class="{none:item.orderDaydis1,rig:item.orderDaycols>1?true:false}">{{item.orderDay}}</td>
                    <!-- 销售额(元) -->
                    <td width="5%">{{item.amount/100}}</td>
                    <!-- 支付宝(元) -->
                    <td width="5%">{{item.zfb/100}}</td>
                    <!-- 微信(元) -->
                    <td width="5%">{{item.wx/100}}</td>
                    <!-- POS(元) -->
                    <td width="5%">{{item.pos/100}}</td>
                    <!-- 现金(元) -->
                    <td width="5%">{{item.xj/100}}</td>
                </tr>
            </tbody>
            <tbody v-else>
                <tr><td colspan="10" rowspan="3" style="padding:20px 0;">暂无数据</td></tr>
            </tbody>
        </table>
    </div>
</template>

<script>

export default {
    data () {
        return {
            tabsDataList :[],
            saleTabsData :[]
        }
    },
    created(){
         this.$http.get('./static/oraginTableJson').then(res => {
           this.tabsDataList = res.body;
        })
    },
    computed: {
        // 处理数据合并单元格
        tabsData() {
            return this.combineCell(this.tabsDataList);
        }
    },
    methods: {
        // 导出Excel
        exportData() {
            if (this.tabsData.length > 0) {
                this.tableToExcel('tableId','销售收入日统计表');
            } else {
                this.$Message.warning('数据为空，不能导出Excel');
            }
        },
        tableToExcel(tableId,fileName){
            this.$http.post("http://localhost:8080/productSaleIncomeReport/uploadSaleDayReportByDayTableDataApi", {
                tableData:JSON.stringify(this.saleTabsData)
                },{emulateJSON: true}).then ((response)=>{
                    console.log(response); 
                    //数据上传成功进行下载
                    if(response.status == 200){
                        var tempForm = document.createElement("form");
                        tempForm.id="tempForm1";
                        tempForm.method="post";
                        tempForm.action='http://localhost:8080/productSaleIncomeReport/export';
                        var hideInput = document.createElement("input");
                        hideInput.type="hidden";
                        hideInput.name= "filePath";
                        hideInput.value= response.data;
                        tempForm.appendChild(hideInput);
                        document.body.appendChild(tempForm);
                        tempForm.submit();
                        document.body.removeChild(tempForm);
                    } else {
                        this.$Message.warning("导出失败，请稍后再试");
                    }
                }, (error)=>{
                    console.log(error);
                }
            );
            /*var hiddens = document.querySelectorAll("td.hidden");
            for(var i=0;i<hiddens.length;i++) {
              hiddens[i].parentNode.removeChild(hiddens[i]);
            }
            var nones = document.querySelectorAll("td.none");
            for(var j=0;j<nones.length;j++) {
              nones[j].parentNode.removeChild(nones[j]);
            }
            var table = document.getElementById(tableId);
            var excelContent = table.innerHTML;

            var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>";
            excelFile += "<head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head>";
            excelFile += "<body><table>";
            excelFile += excelContent;
            excelFile += "</table></body>";
            excelFile += "</html>";
            var link = "data:application/vnd.ms-excel;base64," + window.btoa(unescape(encodeURIComponent(excelFile)));
            var a = document.createElement("a");
            a.download = fileName + ".xls";
            a.href = link;
            a.click();*/
        },
        // 合并单元格  K:行，i:列，span:row，cols:col
        combineCell(list) {
            for (let field in list[0]) {
                var k = 0;
                while (k < list.length) {
                    list[k][field + 'span'] = 1;
                    /**行合并开始**/
                    var cc=0;
                    for (var i = k + 1; i <= list.length - 1; i++) {
                        // 自己是自己则上下合并
                        if (list[k][field] == list[i][field] && list[k][field] != '') {
                            cc++
                            list[k][field + 'span']++;
                            list[i][field + 'dis'] = true;
                        } else {
                            break;
                        }
                    }
                    /**行合并结束**/
                    /**列合并开始**/
                    // 站厅 - 标识:zt
                    if(field=='ztxj' && list[k][field]== true){
                        list[k]['roomName' + 'cols'] = 2;
                        list[k]['orderDay' + 'dis1'] = true;
                    }
                    // 车站 - 标识:cz
                    if(field=='czxj' && list[k][field]== true){
                        list[k]['stationName' + 'cols'] = 3;
                        list[k]['roomName' + 'dis1'] = true;
                        list[k]['orderDay' + 'dis1'] = true;
                    }
                    // 路局 - 标识:lj
                    if(field=='ljhj' && list[k][field]== true){
                        list[k]['bureauName' + 'cols'] = 4;
                        list[k]['stationName' + 'dis1'] = true;
                        list[k]['roomName' + 'dis1'] = true;
                        list[k]['orderDay' + 'dis1'] = true;
                    }
                    // 总合计 - 标识:hj
                    if(field=='zj' && list[k][field]== true){
                        list[k]['companyAbbreviation' + 'cols'] = 5;
                        list[k]['bureauName' + 'dis1'] = true;
                        list[k]['stationName' + 'dis1'] = true;
                        list[k]['roomName' + 'dis1'] = true;
                        list[k]['orderDay' + 'dis1'] = true;
                    }
                    /**列合并结束**/
                    k = i;
                }
            }
            this.saleTabsData = list;
            //若列存在不同列下同名则可通过隐藏的p标签进行处理
            /*this.saleTabsData = JSON.parse(JSON.stringify(list));
            this.saleTabsData.forEach(row => {
                for(var element in row){
                    if (element.indexOf("<p") > -1) {
                        var start = element.indexOf("<p");
                        var end = element.lastIndexOf("</p >")+4
                        var removeStr = element.substring(start,end);
                        var newKey = element.replace(removeStr, "");
                        // 截取出列明拼接上列类型并将原key进行替换
                        var typeNote = element.substring(element.indexOf('>')+1,end-4)
                        row[newKey +"-"+ typeNote] = row[element];
                        delete row[element];
                    }
                }
            });*/
            return list;
        }
    }
}
</script>
<style scoped>
  .inputStyle{
    width:200px;
  }
  .table{
    text-align: center;
    width: 100%;
    border-collapse:collapse;
  }
  .table, th, td {
    border: 1px solid #ccc;
    height: 25px;
  }
  .hidden{
    display: none;
  }
  .none{
    display: none;
  }
</style>