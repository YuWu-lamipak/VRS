<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="150px">
    <el-form-item :label="$t('ua')" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30"/>
    </el-form-item>
    <el-form-item :label="$t('cjjj')" prop="phoneNumber">
      <el-input v-model="user.phoneNumber" maxlength="11" />
    </el-form-item>
<!--    <el-form-item label="邮箱" prop="email">-->
<!--      <el-input v-model="user.email" maxlength="50" />-->
<!--    </el-form-item>-->

<!--    <el-form-item label="简介" prop="personalProfile">-->
<!--      <el-input type="textarea" v-model="user.personalProfile" maxlength="100" />-->
<!--    </el-form-item>-->
<!--    <el-form-item label="地址" prop="place">-->
<!--      <el-input v-model="user.place" maxlength="100" />-->
<!--    </el-form-item>-->

<!--    <el-form-item label="身份证号" prop="idcard">-->
<!--      <el-input v-model="user.idcard" maxlength="50" />-->
<!--    </el-form-item>-->
<!--    <el-form-item label="性别">-->
<!--      <el-radio-group v-model="user.sex">-->
<!--        <el-radio label="0">男</el-radio>-->
<!--        <el-radio label="1">女</el-radio>-->
<!--      </el-radio-group>-->
<!--    </el-form-item>-->
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">{{ $t('bj') }}</el-button>
      <el-button type="danger" size="mini" @click="close">{{ $t('y4') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        // email: [
        //   { required: true, message: "邮箱地址不能为空", trigger: "blur" },
        //   {
        //     type: "email",
        //     message: "'请输入正确的邮箱地址",
        //     trigger: ["blur", "change"]
        //   }
        // ],
        phoneNumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        console.log(this.user,"this.user")
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.$modal.msgSuccess("修改成功");
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    }
  }
};
</script>
