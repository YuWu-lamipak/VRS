<template>
    <view>
        <headerbar title="乐美车辆预约" background="#2195F3" title_color="#FFFFFF" :noback="true"/>
        <!-- 背景 -->
        <img class="head" src="/static/login_bg.png"/>
		<view class="logoframe">
			<img class="logo" src="/static/logo.png"/>
		</view>
        <!-- 用户名密码输入  -->
        <view class="inputBox">
            <u-form :model="form">
                <u-form-item labelWidth="0">
                    <view class="input">
                        <view class="icon margin_right25">
                            <img class="icon" src="/static/username.png"/>
                        </view>
                        <u-input fontSize="36rpx" v-model="form.phone" placeholder-style="font-size: 36rpx;font-family: PingFangSC;font-weight: 400;color: #B4C3CE;" placeholder="输入账号" border="none" inputAlign="left"></u-input>
                    </view>
                </u-form-item>
                <u-form-item>
                    <view class="input">
                        <view class="icon margin_right25">
                            <img class="icon" src="/static/password.png"/>
                        </view>
                        <u-input fontSize="36rpx" v-model="form.password" placeholder-style="font-size: 36rpx;font-family: PingFangSC;font-weight: 400;color: #B4C3CE;" placeholder="输入密码" border="none" inputAlign="left" type="password"></u-input>
                    </view>
                </u-form-item>
            </u-form>
        </view>
        <!-- 登录按钮 -->
        <view class="button" @click="dologin">
            <view class="loginBtn">登 录</view>
        </view>
        <u-toast ref="uToast"></u-toast>
    </view>
</template>

<script>
import jwt from "@/utils/auth/jwt.js";
import { login } from "@/api/login.js";
export default {
    data() {
        return {
            imgUrl: "",
            //用户登录表单
            form: {
                phone: "",
                password: "",
            },
        };
    },
    onLoad() {
        this.imgUrl = this.globalUrl;
        let accountform = jwt.getAccount()
        if (accountform) {
            this.form = accountform
        }
    },
    methods: {
        //账号密码登录
        dologin() {
            let that = this;
            let form = this.form
            if (!form.phone) {
                uni.showToast({
                    icon: 'none',
                    title: "请输入账号",
                    duration: 1500
                })
                return
            } 
            if (!form.password) {
                uni.showToast({
                    icon: 'none',
                    title: "请输入密码",
                    duration: 1500
                })
                return
            }
            //登录
            login(form).then((res) => {
                // console.log(res)
                let dataRes = res.data;
                if (dataRes.code === 200) {
                    jwt.setAccessToken(dataRes.data && dataRes.data.token);
                    jwt.setAccount(form)
                    jwt.setUser(dataRes.data || null)
                    this.$refs.uToast.show({
                        type: "success",
                        message: "登录成功",
                        complete() {
                            uni.navigateTo({
                                url: "/pages/home/home",
                            });
                        },
                    });
                } else {
                    this.$refs.uToast.show({
                        type: "error",
                        message: dataRes.msg || '未知错误',
                    });
                }
            });
        },
    },
};
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';

.head {
    width: 100%;
    height: 442rpx;
    background-size: 100% 100%;
}

.logoframe {
	width: 150rpx;
	height: 150rpx;
	margin: 30rpx auto 0;
	
	.logo {
		width: 150rpx;
		height: 150rpx;
	}
}

.inputBox {
    padding: 0 56rpx;
    margin-top: 0rpx;
    width: 638rpx;

    .input {
        display: flex;
        width: 542rpx;
        align-items: center;
        margin-top: 24rpx;
        background: #e9eff3;
        padding: 26rpx 48rpx 25rpx 48rpx;
        min-height: 47rpx;
        border-radius: 12rpx;

        .icon {
            width: 46rpx;
            height: 46rpx;
        }
    }
}

.button {
    display: flex;
    justify-content: center;
    margin-top: 48rpx;
    padding: 0 56rpx;

    .loginBtn {
        width: 638rpx;
        // width: 100%;
        height: 98rpx;
        line-height: 98rpx;
        background: #2195f3;
        color: #ffffff;
        text-align: center;
        box-shadow: 0 8rpx 16rpx rgba($color: #000000, $alpha: 0.16);
        border-radius: 8rpx;
        font-size: 36rpx;
        font-family: PingFangSC;
        font-weight: 400;
        color: #FFFFFF;
    }
}
</style>
