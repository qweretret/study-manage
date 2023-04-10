<template>
  <div>
    <el-drawer
      title="我是标题"
      size="50%"
      :visible.sync="drawer"
      :with-header="false">



        <el-button @click="data">默认按钮</el-button>

    </el-drawer>
  </div>
</template>

<script>
export default {
  name: 'DevView',
  data () {
    return {

    }
  },
  methods: {
    open (row) {
      this.drawer = true
      this.userDdata = [row]
    },
    data (row) {
      console.log(this.value1)
    }
  }
}
</script>
