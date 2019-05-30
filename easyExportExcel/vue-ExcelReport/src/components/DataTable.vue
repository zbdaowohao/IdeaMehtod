<!-- dis是控制rowsoan合并隐藏的,dis1是控制colsoan合并隐藏的 -->
<template>
  <div class="sales-summary" style="padding-bottom:38px;">
    <div class="table-wrapper" ref="tableWrapper" style="padding-bottom:38px;">
      <div style="width:10%; position: absolute; right: 10px; top: 250px;">
        <Button type="primary" @click="exportData()">导出</Button>
      </div>
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
            <td width="6%" :rowspan="item.companyNamespan" :colspan="item.companyNamecols" :class="{hidden: item.companyNamedis,none:item.companyNamedis1}">{{item.companyName}}</td>
            <!-- 路局 -->
            <td width="5%" :rowspan="item.burNamespan" :colspan="item.burNamecols" :class="{hidden: item.burNamedis,none:item.burNamedis1}">{{item.burName}}</td>
            <!-- 车站 -->
            <td width="5%" :rowspan="item.stationNamespan" :colspan="item.stationNamecols" :class="{hidden: item.stationNamedis,none:item.stationNamedis1}">{{item.stationName}}</td>
            <!-- 站厅 -->
            <td width="10%" :rowspan="item.roomNamespan" :colspan="item.roomNamecols" :class="{hidden: item.roomNamedis,none:item.roomNamedis1}">{{item.roomName}}</td>
            <!-- 人员 -->
            <td width="5%" :rowspan="item.recistNamespan" :colspan="item.recistNamecols" :class="{hidden: item.recistNamedis,none:item.recistNamedis1}">{{item.recistName}}</td>
            <!-- 接待类型 -->
            <td width="5%" :class="{none:item.receptionTypedis1}">{{item.receptionType}}</td>
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
export default {
  data() {
    return {
      tabsDataList: [],
      saleTabsData: []
    }
  },
  created() {
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
        this.tableToExcel('tableId', '销售收入日统计表');
      } else {
        this.$Message.warning('数据为空，不能导出Excel');
      }
    },
    tableToExcel(tableId, fileName) {
      this.$http.post("http://localhost:8080/recServeReport/uploadTableDataApi", {
        tableData: JSON.stringify(this.saleTabsData)
      }, { emulateJSON: true }).then((response) => {
        console.log(response);
        //数据上传成功进行下载
        if (response.status == 200) {
          var tempForm = document.createElement("form");
          tempForm.id = "tempForm1";
          tempForm.method = "post";
          tempForm.action = 'http://localhost:8080/recServeReport/export';
          var hideInput = document.createElement("input");
          hideInput.type = "hidden";
          hideInput.name = "filePath";
          hideInput.value = response.data;
          tempForm.appendChild(hideInput);
          document.body.appendChild(tempForm);
          tempForm.submit();
          document.body.removeChild(tempForm);
        } else {
          this.$Message.warning("导出失败，请稍后再试");
        }
      }, (error) => {
        console.log(error);
      });
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
          var cc = 0;
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
          //礼宾员
          if (field == 'recNameMerge' && list[k][field] == 'true') {
            list[k]['recistName' + 'cols'] = 2;
            list[k]['receptionType' + 'dis1'] = true;
          }
          // 站厅
          if (field == 'roomMerge' && list[k][field] == 'true') {
            list[k]['roomName' + 'cols'] = 3;
            list[k]['recistName' + 'dis1'] = true;
            list[k]['receptionType' + 'dis1'] = true;
          }
          // 车站
          if (field == 'stationMerge' && list[k][field] == 'true') {
            list[k]['stationName' + 'cols'] = 4;
            list[k]['roomName' + 'dis1'] = true;
            list[k]['recistName' + 'dis1'] = true;
            list[k]['receptionType' + 'dis1'] = true;
          }
          // 路局
          if (field == 'burMerge' && list[k][field] == 'true') {
            list[k]['burName' + 'cols'] = 5;
            list[k]['stationName' + 'dis1'] = true;
            list[k]['roomName' + 'dis1'] = true;
            list[k]['recistName' + 'dis1'] = true;
            list[k]['receptionType' + 'dis1'] = true;
          }
          // 商户
          if (field == 'companyMerge' && list[k][field] == 'true') {
            list[k]['companyName' + 'cols'] = 6;
            list[k]['burName' + 'dis1'] = true;
            list[k]['stationName' + 'dis1'] = true;
            list[k]['roomName' + 'dis1'] = true;
            list[k]['recistName' + 'dis1'] = true;
            list[k]['receptionType' + 'dis1'] = true;
          }
          // 合计
          if (field == 'totalMerge' && list[k][field] == 'true') {
            list[k]['companyName' + 'cols'] = 6;
            list[k]['burName' + 'dis1'] = true;
            list[k]['stationName' + 'dis1'] = true;
            list[k]['roomName' + 'dis1'] = true;
            list[k]['recistName' + 'dis1'] = true;
            list[k]['receptionType' + 'dis1'] = true;
          }
          /*列合并结束*/
          k = i;
        }
      }
      this.saleTabsData = list;
      /*
      // thead若列存在不同列名则可在后台封装key时通过隐藏的p标签进行处理
      this.saleTabsData = JSON.parse(JSON.stringify(list));
      this.saleTabsData.forEach(row => {
          for(var element in row){
            if (element.indexOf("<p") > -1) {
              var start = element.indexOf("<p");
              var end = element.lastIndexOf("</p>")+4
              var removeStr = element.substring(start,end);
              var newKey = element.replace(removeStr, "");
              // 截取出列明拼接上列类型并将原key进行替换
              var typeNote = element.substring(element.indexOf('>')+1,end-4)
              row[newKey +"-"+ typeNote] = row[element];
              delete row[element];
            }
          }
      });
      // tbody若列存在款行合并某一列则可在后台封装value时通过隐藏的p标签进行处理
      this.saleTabsData.forEach(element => {
        有几列需要处理则添加几列
        if (element['receptionDay'].indexOf("<p")){
          var start = element['receptionDay'].indexOf("<p");
          var end = element['receptionDay'].lastIndexOf("</p >")+4
          var removeStr = element['receptionDay'].substring(start,end);
          element['receptionDay'] = element['receptionDay'].replace(removeStr, "");
        }
      });
      */
      return list;
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
