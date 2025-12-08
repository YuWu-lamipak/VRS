package com.lemei.common.service;

import com.lemei.common.model.Text;
import com.lemei.common.model.TextMessage;
import com.lemei.common.utils.WeChatParamesUtil;
import com.lemei.common.utils.WeChatUtil;
import net.sf.json.JSONObject;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;


/**
 * @author yq
 * @CLassName WeChatService
 * @Description 发送消息
 * @date 2022/11/9 9:41
 **/
@Service
public class WeChatService {

    private static String sendMessage_url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";

    /**
     * @desc ：0.公共方法：发送消息
     * @param uid
     * @param content
     * void
     */
    public  String sendMessage(String uid, String content) {

        // 1.获取access_token:根据企业id和应用密钥获取access_token,并拼接请求url
        String accessToken = WeChatUtil.getAccessToken(WeChatParamesUtil.corpId, WeChatParamesUtil.corpsecret).getToken();
        // 2.获取发送对象，并转成json
        Gson gson = new Gson();
        TextMessage message = new TextMessage();
        // 1.1非必需,不区分大小写
        message.setTouser(uid);
        //message.setToparty("1");
        //message.getTouser(totag);
        // txtMsg.setSafe(0);
        // 1.2必需
        message.setMsgtype("text");
        message.setAgentid(WeChatParamesUtil.agentId);
        Text text = new Text();
        text.setContent(content);
        message.setText(text);
        String jsonMessage = gson.toJson(message);
        // 3.获取请求的url
        String url = sendMessage_url.replace("ACCESS_TOKEN", accessToken);

        // 4.调用接口，发送消息
        JSONObject jsonObject = WeChatUtil.httpRequest(url, "POST", jsonMessage);

        // 4.错误消息处理
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                System.out.println("消息发送失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
            }
        }
        return jsonObject.toString();
    }

//    public static void main(String[] args) {
//        // 0.设置消息内容
//        String content = "这是一条测试消息";
//        //userId为企业用户的id
//        String userId = "WeiLanDeXing";
//        // 3.发送消息：调用业务类，发送消息
//        WeChatService.sendMessage(userId, content);
//
//    }

}