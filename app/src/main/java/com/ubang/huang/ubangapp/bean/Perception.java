package com.ubang.huang.ubangapp.bean;

/**
 * Created by huang on 2019/4/10.
 */

public class Perception {
    private InputText inputText;
    private InputImage inputImage;
    private SelfInfo selfInfo;

    public InputImage getInputImage() {
        return inputImage;
    }

    public void setInputImage(InputImage inputImage) {
        this.inputImage = inputImage;
    }

    public SelfInfo getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(SelfInfo selfInfo) {
        this.selfInfo = selfInfo;
    }

    public InputText getInputText() {
        return inputText;
    }

    public void setInputText(InputText inputText) {
        this.inputText = inputText;
    }
}
