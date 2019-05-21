package com.ubang.huang.ubangapp.util;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.ubang.huang.ubangapp.activity.Regiest;
import com.ubang.huang.ubangapp.common.DialogModel;
import com.ubang.huang.ubangapp.common.Signal;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by huang on 2019/4/20.
 * @author huang
 */

public class Dialog {

    private int model;
    private Context context;
    private SweetAlertDialog sweetAlertDialog;

    public Dialog(int model,Context context){
        this.model = model;
        this.context = context;
    }

    public void createDialog(){
        switch (model){
            case DialogModel.loading:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog .setTitleText("请耐心等待...")
                                  .setCancelable(true);
                break;
            case DialogModel.Regiest_Suc:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("注册账号成功")
                                .setContentText("跳转回登录界面");
                break;
            case DialogModel.Regiest_Fail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("注册账号失败")
                        .setContentText(Signal.Result);
                break;
            case DialogModel.Modefy_Suc:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("修改密码成功")
                        .setContentText("跳转到登录界面");
                break;
            case DialogModel.Modefy_Fail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("修改密码失败");
                break;
            case DialogModel.Login_Succ:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("登录成功")
                        .setContentText("跳转到U帮首页");
                break;
            case DialogModel.Login_Fail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("登录失败");
                break;
            case DialogModel.Vertify_Fail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("验证码不正确");
                break;
            case DialogModel.DeleHelpSuc:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("取消求助成功");
                break;
            case DialogModel.DeleHelpFail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("不可取消，已经有人接收求助");
                break;
            case DialogModel.ModefyHelpFail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("修改失败，已经有人接收求助");
                break;
            case DialogModel.ModefyHelpSuc:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("修改内容成功");
                break;

            case DialogModel.SendFeedBackSuc:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("发送成功");
                break;
            case DialogModel.SendFeedBackFail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("出了点小问题，发送失败");
                break;
            case DialogModel.GetHelpSuc:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("接收成功");
                break;
            case DialogModel.GetHelpFail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("求助者取消了或者被别人抢先了");
                break;
            case DialogModel.AnalyzeQRCodeFail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("失败");
                break;
            case DialogModel.GiveQRCode:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("请给对方扫码");
                break;
            case DialogModel.FinishHelp:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("结束帮助");
                break;
            case DialogModel.CancelHelp:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("取消帮助");
                break;
            case DialogModel.CancelHelpFail:
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("帮助的状态不处于进行中");
                break;

        }
    }

    public void show(){
        sweetAlertDialog.show();
    }
    public void dismiss(){
        sweetAlertDialog.dismiss();
    }


}
