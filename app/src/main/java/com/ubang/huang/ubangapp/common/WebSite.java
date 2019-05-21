package com.ubang.huang.ubangapp.common;

/**
 * Created by huang on 2019/4/19.
 * @author huang
 */

public class WebSite {
    public static final String Service_Url = "http://openapi.tuling123.com/openapi/api/v2";
    public static final String News_Url = "http://api.jisuapi.com/news/search";
    public static final String Avatar = "http://img3.duitang.com/uploads/item/201408/28/20140828170248_QGGmh.jpeg";

//    private static final String IP_Adress = "10.0.2.2";
//    private static final String IP_Adress = "192.168.1.131";
   private static final String IP_Adress = "119.23.232.90";

    private static String IP = "http:"+IP_Adress+":8080/ubang";

    private static String User_IP = IP +"/User";
    public static String User_Regiest = User_IP+"/Regiest";
    public static String User_Login = User_IP+"/UserLogin";
    public static String User_ForgetPwd = User_IP+"/ForgetPwd";
    public static String GetUser = User_IP+"/GetUser";
    public static String PostFeedBack = User_IP+"/PostFeedBack";
    public static String PostAvatar = User_IP+"/PostAvatar";

    private static String SeekHelp_IP = IP +"/SeekHelp";
    public static String GetAlarmContent = SeekHelp_IP+"/GetAlarmContent";
    public static String ModifyAlarm = SeekHelp_IP+"/ModifyAlarm";
    public static String PublishHelp = SeekHelp_IP+"/PublishHelp";
    public static String PostPic = SeekHelp_IP+"/PostPic";
    public static String GetMySeekHelp = SeekHelp_IP+"/GetMySeekHelp";
    public static String GetSeekHelp = SeekHelp_IP+"/GetSeekHelp";
    public static String GetHelpInfo = SeekHelp_IP+"/GetHelpInfo";
    public static String PostRating = SeekHelp_IP+"/PostRating";
    public static String DeleteHelp = SeekHelp_IP+"/DeleteHelp";
    public static String ModifyHelpContent = SeekHelp_IP+"/ModifyHelpContent";
    public static String GetHelp = SeekHelp_IP+"/GetHelp";
    public static String PostCharMessage = SeekHelp_IP+"/PostCharMessage";
    public static String GetChatMessage = SeekHelp_IP+"/GetChatMessage";
    public static String GetRecentChatMessage = SeekHelp_IP+"/GetRecentChatMessage";
    public static String GetMyHelp = SeekHelp_IP+"/GetMyHelp";
    public static String GetHelpPic = SeekHelp_IP+"/GetHelpPic";
    public static String GetUnHelpInfo = SeekHelp_IP+"/GetUnHelpInfo";
    public static String PostCharMessagePic = SeekHelp_IP+"/PostCharMessagePic";
    public static String FinishHelp = SeekHelp_IP+"/FinishHelp";
    public static String CancelHelp = SeekHelp_IP+"/CancelHelp";
    public static String SaveHelp = SeekHelp_IP+"/saveHelp";
    public static String PersonInfo = SeekHelp_IP+"/personInfo";
    public static String GetHelpList = SeekHelp_IP+"/helpList";

    private static String JOB_IP = IP+"/Job";
    public static String Get_JOBINFO = JOB_IP+"/GetJobInfo";
    public static String Get_TAG = JOB_IP+"/GetTag";

    private static String News_IP = IP +"/News";
    public static String GetCampusSafeList = News_IP+"/GetCampusSafeList";

    private static String WS_IP = "ws://"+IP_Adress+":8080/ubang";
    public static String RemindOthers = WS_IP+"/RemindOthers/";

    public static final String Message_URL = "https://api.jisuapi.com/sms/send";
}
