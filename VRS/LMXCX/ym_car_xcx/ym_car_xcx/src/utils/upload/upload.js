import { baseURL } from '@/utils/luch/request'
import jwt from "../auth/jwt";
/*  上传 图片 / 视频 处理
 * **************************************************************************
 *                                                                          *
 *                                   _oo8oo_                                *
 *                                  o8888888o                               *
 *                                  88" . "88                               *
 *                                  (| -_- |)                               *
 *                                  0\  =  /0                               *
 *                                ___/'==='\___                             *
 *                              .' \\|     |// '.                           *
 *                             / \\|||  :  |||// \                          *
 *                            / _||||| -:- |||||_ \                         *
 *                           |   | \\\  -  /// |   |                        *
 *                           | \_|  ''\---/''  |_/ |                        *
 *                           \  .-\__  '-'  __/-.  /                        *
 *                         ___'. .'  /--.--\  '. .'___                      *
 *                      ."" '<  '.___\_<|>_/___.'  >' "".                   *
 *                     | | :  `- \`.:`\ _ /`:.`/ -`  : | |                  *
 *                     \  \ `-.   \_ __\ /__ _/   .-` /  /                  *
 *                 =====`-.____`.___ \_____/ ___.`____.-`=====              *
 *                                   `=---=`                                *
 * **************************************************************************
 */
function chooseImg(callback,configs = {}) {
    uni.chooseImage({
        count: configs && configs.count || 9, // 最多可以选择的图片张数，默认9
        sourceType: configs && configs.sourceType || ['album', 'camera '], //album 从相册选图，camera 使用相机，默认二者都有
        success: (chooseImageRes) => {
            console.log(chooseImageRes)
            let resources = []
            const tempFilePaths = chooseImageRes.tempFilePaths;
            uni.showLoading({
                title: "上传中...",
                mask: true
            });
            upload(tempFilePaths, resources, 0, callback)
            // callback(tempFilePaths)
        }
    });
}

// tempFilePaths 上传 图片/视频 的数组 resources 上传服务器后的url地址数组 index 上传图片下标 callback 回传
function upload(tempFilePaths, resources, index, callback) {
    if (index >= tempFilePaths.length) {
        uni.hideLoading()
        callback(resources)
        return
    }
    uni.uploadFile({
        url: baseURL + '/upload/uploadImg', //仅为示例，非真实的接口地址
        filePath: tempFilePaths[index],
        name: 'file',
        header: {
			'Authorization': jwt.getAccessToken()
		},
        // formData: {
        //     'user': 'test'
        // },
        complete: (uploadFileRes) => {
            if (uploadFileRes.statusCode == 200) {
                let resdata = uploadFileRes.data
                let resourcedata = JSON.parse(resdata) || {}
                let imgUrl = resourcedata.data && resourcedata.data.imgUrl || null
                // debugger
                if (index >= tempFilePaths.length) {
                    uni.hideLoading()
                    callback(resources)
                } else {
                    resources.push(imgUrl)
                    index = index + 1
                    upload(tempFilePaths, resources, index, callback)
                }
            } else {
                uni.hideLoading()
                uni.showToast({
                    title: uploadFileRes.error || '上传失败',
                    duration: 1000,
                    icon: 'none'
                });
            }
        },
        error: (err) => {
            uni.hideLoading()
            uni.showToast({
                title: err,
                duration: 1000,
                icon: 'none'
            });
        }
    });
}




module.exports = {
	chooseImg: chooseImg
}
