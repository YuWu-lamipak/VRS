<template>
    <view>
        <headerbar title="修改密码" background="#2195F3" title_color="#FFFFFF" :noback="false"/>
        <view class="page"> 
            <!-- <view v-if="status === 0">
                <u-form :model="form" :borderBottom="true" :rules="formRules" errorType="message" ref="uForm">
                    <u-form-item labelWidth="0" prop="" borderBottom>
                        <view class="formitemframe">
                            <view class="formitem_normal flex">
                                <view class="formitem_leftkey">手机号</view>
                                <view class="leftauto maxwidth400">
                                    <formelement v-model="form.password" placeholder="请输入手机号"/>
                                </view>
                            </view>
                        </view>
                    </u-form-item>
                    <u-form-item labelWidth="0" prop="" borderBottom>
                        <view class="formitemframe">
                            <view class="formitem_normal flex">
                                <u-input v-model="form.password" placeholder-style="font-size: 28rpx;font-family: PingFangSC;font-weight: 400;color: #999999;"
                                    placeholder="输入验证码" border="none" inputAlign="left">
                                </u-input>
                                <view class="captcha margin_left40" @click="getcode()" v-if="!iscodetime">
                                    获取验证码
                                </view>
                                <view class="captcha margin_left40" @click="getcode()" v-else>
                                    重新获取验证码  {{codetime_show}}
                                </view>
                            </view>
                        </view>
                    </u-form-item>
                </u-form>
            </view> -->
            <view>
                <u-form :model="form" :borderBottom="true" :rules="formRules" errorType="message" ref="uForm">
                    <u-form-item labelWidth="0" prop="prePassword" borderBottom>
                        <view class="formitemframe">
                            <view class="formitem_normal flex">
                                <view class="formitem_leftkey">旧密码</view>
                                <view class="leftauto maxwidth400">
                                    <formelement v-model="form.prePassword" placeholder="请输入旧密码" type="password"/>
                                </view>
                            </view>
                        </view>
                    </u-form-item>
                    <u-form-item labelWidth="0" prop="password" borderBottom>
                        <view class="formitemframe">
                            <view class="formitem_normal flex">
                                <view class="formitem_leftkey">新密码</view>
                                <view class="leftauto maxwidth400">
                                    <formelement v-model="form.password" placeholder="请输入新密码" type="password"/>
                                </view>
                            </view>
                        </view>
                    </u-form-item>
                    <u-form-item labelWidth="0" prop="passwordagain" borderBottom>
                        <view class="formitemframe">
                            <view class="formitem_normal flex">
                                <view class="formitem_leftkey">再次确认新密码</view>
                                <view class="leftauto maxwidth400">
                                    <formelement v-model="form.passwordagain" placeholder="请再次输入新密码" type="password"/>
                                </view>
                            </view>
                        </view>
                    </u-form-item>
                </u-form>
            </view>
            <view class="butframe flex_center">
                <view class="but flex_center" @click="confirm()">
                    <view class="buttext">确  认</view>
                </view>
            </view>
            <!-- <view class="butframe flex_center" v-if="status === 0">
                <view class="but flex_center" @click="next()">
                    <view class="buttext">下一步</view>
                </view>
            </view>
            <view class="butframe flex_center" v-if="status === 1">
                <view class="but flex_center" @click="confirm()">
                    <view class="buttext">确  认</view>
                </view>
            </view> -->
        </view>
    </view>
</template>

<script>
import {
    chooseImg
} from '@/utils/upload/upload.js'
import {
    updatePassword
} from '@/api/user.js'
export default {
    data() {
        return {
            imgUrl: "",
            // 0 为 验证码 1 为确认密码
            status: 0,
            // 验证码时间
            iscodetime: false,
            codetime: 60,
            codetime_show: '60',
            //表单
            form: {
                prePassword: "",
                password: "",
                passwordagain: ""
            },
            formRules: {
                prePassword: {
                    required: true,
                    message: "请输入原始密码",
                    trigger: ["blur", "change"],
                },
                password: {
                    required: true,
                    message: "请输入新密码",
                    trigger: ["blur", "change"],
                },
                passwordagain: [
                    {
                        type: "string",
                        required: "true",
                        message: "请再次输入新密码",
                        trigger: ["blur", "change"],
                    },
                    {
                        // 自定义验证函数，见上说明
                        validator: (rule, value, callback) => {
                            // 上面有说，返回true表示校验通过，返回false表示不通过
                            let result = false
                            console.log(value)
                            console.log(this.form.password)
                            if (value && value === this.form.password) {
                                result = true
                            } else {
                                result = false
                            }
                            return result;
                        },
                        message: "两次输入新密码不一致",
                        // 触发器可以同时用blur和change
                        trigger: ["blur", "change"],
                    },
                ],
            },
        };
    },
    onLoad(e) {
        this.imgUrl = this.globalUrl;
        // #ifndef H5
        console.log('非H5执行代码')
        this.$refs.uForm.setRules(this.formRules);
        // #endif
    },
    methods: {
        // 获取验证码
        getcode() {
            if (this.iscodetime) {
                return
            }
            this.iscodetime = true
            this.codetime = this.codetime - 1
            let interval = setInterval(() => {
                if (this.codetime <= 0) {
                    this.codetime = 60
                    this.codetime_show = '60'
                    this.iscodetime = false
                    clearInterval(interval)
                } else {
                    this.codetime = this.codetime - 1
                    this.codetime_show = this.codetime < 10 ? '0' + this.codetime : this.codetime //小于10秒补 0
                }
                
            }, 1000)
        },
        // 确认 提交表单
        confirm() {
            let that = this
            this.$refs.uForm.validate().then((res) => {
                // console.log(res)
                if (res) {
                    that.updatePasswordapi()
                }
            }).catch((errors) => {

            });
        },
        // 修改密码
        updatePasswordapi() {
            let that = this
            uni.showLoading({
                title: "修改密码中...",
                mask: true
            });
            updatePassword(this.form).then(res => {
                uni.hideLoading()
                let dataRes = res.data || {}
                if (dataRes.code === 200) {
                    uni.showToast({
                        title: '修改密码成功',
                        icon: 'success'
                    })
                    that.tominepasswordresult()
                } else {
                    uni.showToast({
                        title: dataRes.msg || '修改密码失败',
                        icon: 'error',
                        duration: 2000
                    })
                }
                this.deleteitem = {}
                this.deletemodal = false
            })
        },
        // 下一步
        next() {
            this.status = 1
        },
        // 跳转修改密码成功页面
        tominepasswordresult() {
            uni.redirectTo({
                url: '/pages/mine/minepasswordresult'
            })
        }
    },
};
</script>

<style lang="scss" scoped>
@import '@/styles/common.scss';
@import '@/styles/formlist.scss';
.page {
    padding: 12rpx 24rpx 40rpx;
}

.captcha {
    padding: 10rpx 20rpx;
    border: 1rpx solid #2196F3;
    font-size: 24rpx;
    font-family: PingFangSC;
    font-weight: 400;
    color: #2196F3;
    border-radius: 8rpx;

}

.butframe {
    width: 100%;
    position: fixed;
    bottom: 90rpx;
    left: 0;

    .but {
        width: 638rpx;
        height: 98rpx;
        background: #2196F3;
        box-shadow: 0rpx 8rpx 16rpx rgba(0,0,0,0.16);
        opacity: 1;
        border-radius: 8rpx;
    }

    .buttext {
        font-size: 36rpx;
        font-family: PingFangSC;
        font-weight: 400;
        color: #FFFFFF;
    }
}
</style>
