<template>
  <div class="sales-summary" style="padding-bottom:38px;">
    <Button type="primary" @click="exportData()">导出</Button>
    <Upload ref="upload" name="upfile" :format="['xls', 'xlsx']" :action="uploadAction" :show-upload-list="false" :on-success="handleSuccess" :on-format-error="handleFormatError" style="display: inline-block;">
      <Button type="primary" style="margin-left: 5px">导入</Button>
    </Upload>
    <div style="padding:16px 0px;">
      <Table border :columns="columns" :data="data" ref="table"></Table>
    </div>
  </div>
</template>
<script>
import employJsonData from '@/../static/employJsonData'
import { formatStrDate, formatDate } from '@/utils/date';
export default {
  data() {
    return {
      uploadAction: 'http://localhost:8080/excelData/uploadExcel',
      data: employJsonData,
      employee: {
        total: 0,
        current: 1,
        size: 10
      },
      columns: [{
          type: 'index',
          align: 'center'
        },
        {
          title: '姓名',
          align: 'center',
          key: 'employeeName'
        },
        {
          title: '性别',
          align: 'center',
          key: 'gender'
        },
        {
          title: '身份证',
          align: 'center',
          key: 'idCard'
        },
        {
          title: '出生日期',
          align: 'center',
          key: 'birthDay',
          render: (h, params) => {
            return h('p', formatStrDate(params.row.birthDay));
          }
        },
        {
          title: '电话号码',
          align: 'center',
          key: 'phone'
        },
        {
          title: '创建日期',
          align: 'center',
          key: 'createDate',
          render: (h, params) => {
            return h('p', formatDate(new Date(params.row.createDate), 'yyyy-MM-dd hh:mm:ss'));
          }
        }
      ],
    }
  },
  methods: {
    exportData() {
      if (this.data.length > 0) {
        var tempForm = document.createElement("form");
        tempForm.id = "tempForm1";
        tempForm.method = "post";
        tempForm.action = 'http://localhost:8080/excelData/exportExcel';
        var hideInput = document.createElement("input");
        hideInput = document.createElement("input");
        hideInput.type = "hidden";
        hideInput.name = "tableData";
        hideInput.value = JSON.stringify(this.data);
        tempForm.appendChild(hideInput);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
      } else {
        this.$Message.warning("暂无数据，无法导出");
      }
    },
    handleSuccess(res, file) {
      if (res.code === 200) {
        this.employee.current = 1;
        this.data.push(...res.result);
        this.$Message.info({
          content: '导入乘务员资料成功',
          top: 50,
          duration: 5
        });
        const fileList = this.$refs.upload.fileList
        this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
      } else {
        this.$Message.error({
          content: res.message,
          top: 50,
          duration: 5
        });
      }
    },
    handleFormatError() {
      this.$Notice.warning({
        title: '文件仅支持.xls.xlsx格式的Excel文件'
      })
    },
  }
}

</script>
