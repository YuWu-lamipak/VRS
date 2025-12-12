<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ $t('theme') }}</h3>
      <el-form-item prop="username" :rules="[{ required: true, message: $t('jbbb'), trigger: ['blur', 'change'] }]">
        <el-input v-model="loginForm.username" type="text" auto-complete="off" :placeholder="$t('jbb')">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password" :rules="[{ required: true, message: $t('jbbbb'), trigger: ['blur', 'change'] }]">
        <el-input v-model="loginForm.password" type="password" auto-complete="off" :placeholder="$t('jb')"
          @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaOnOff">
        <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
          @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">{{ $t('rempassword') }}</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
          @click.native.prevent="handleLogin">
          <span v-if="!loading">{{ $t('loginText') }}</span>
          <span v-else>{{ $t('lmm6') }}</span>
        </el-button>
        <!--        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>-->
        <!-- <el-button style="position: relative;left: 122px;top: 10px;width: 90px; height: 40px; color:cornflowerblue;"
          round @click="languageChange">{{ $t('language') }}</el-button> -->
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
    </div>
  </div>
</template>
<script>

</script>

<script>
// import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        // username: [
        //   { required: true, trigger: "blur", message: "请输入您的账号" }
        // ],
        // password: [
        //   { required: true, trigger: "blur", message: "请输入您的密码" }
        // ],
       // code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaOnOff: false,
      // 注册开关
      register: true,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    // this.getCode();
    this.getCookie();
  },
  methods: {
    languageChange () {
      if (this.$i18n.locale ==='en'){
        this.$i18n.locale ='zh'
      }
      else {
        this.$i18n.locale ='en'
      }
    this.$refs.loginForm.clearValidate();

    // 重新设置验证规则以更新提示信息
    this.$nextTick(() => {
      const usernameRules = [
        { required: true, message: this.$t('jbbb'), trigger: ['blur', 'change'] },
      ];
      const passwordRules = [
        { required: true, message: this.$t('jbbbb'), trigger: ['blur', 'change'] },
      ];

      this.loginRules = {
        username: usernameRules,
        password: passwordRules,
          };
      });
    },

    // getCode() {
    //   getCodeImg().then(res => {
    //     this.captchaOnOff = res.captchaOnOff === undefined ? true : res.captchaOnOff;
    //     if (this.captchaOnOff) {
    //       this.codeUrl = "data:image/gif;base64," + res.img;
    //       this.loginForm.uuid = res.uuid;
    //     }
    //   });
    // },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }

          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: /*this.redirect ||*/ "/car/application" }).catch(()=>{});
            // 获取 redirect 参数，默认跳转到 /car/application
          // const redirect = this.redirect || '/car/application';            
          // 只有在 redirect 和当前路径不相同时才进行跳转
          // if (this.$route.path !== redirect) {
          //   this.$router.push({ path: redirect }).catch((err) => {
          //     console.error("Navigation failed: ", err);
          //   });
          // }
          }).catch(() => {
            this.loading = false;
            if (this.captchaOnOff) {
             // this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 60px;
  line-height: 50px;
  position: fixed;
  bottom: 0;
  width: 200%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 24px;
  letter-spacing: 10px;
}

.login-code-img {
  height: 38px;
}
</style>
