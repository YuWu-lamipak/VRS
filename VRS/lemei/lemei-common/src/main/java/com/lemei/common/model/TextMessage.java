package com.lemei.common.model;

/**
 * @author yq
 * @CLassName TextMessage
 * @Description 文本消息
 * @date 2022/11/9 9:06
 **/

public class TextMessage extends BaseMessage {
    /** 文本*/
    private Text text;
    /** 否 表示是否是保密消息，0表示否，1表示是，默认0*/
    private int safe;

    public Text getText() {
        return text;
    }

    public void setText(Text text2) {
        this.text = text2;
    }

    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

}