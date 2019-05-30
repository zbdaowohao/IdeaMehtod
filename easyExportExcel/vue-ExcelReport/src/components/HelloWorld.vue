<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <DataTable />
    <h2>--------------------Excel导入导出-----------------------</h2>
    <br>
    <ImportAndExport />
  </div>
</template>
<script>
import DataTable from './DataTable'
import ImportAndExport from './ImportAndExport'
export default {
  name: 'HelloWorld',
  components: {
    DataTable,
    ImportAndExport
  },
  data() {
    return {
      msg: 'Welcome to The Table display .'
    }
  }
}

</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

</style>
