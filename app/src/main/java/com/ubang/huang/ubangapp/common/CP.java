package com.ubang.huang.ubangapp.common;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.orhanobut.dialogplus.DialogPlus;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.ubang.huang.ubangapp.activity.InputPlaceActivity;
import com.ubang.huang.ubangapp.bean.AlarmContent;
import com.ubang.huang.ubangapp.bean.CampusSafe;
import com.ubang.huang.ubangapp.bean.ChatMessage;
import com.ubang.huang.ubangapp.bean.GetNews;
import com.ubang.huang.ubangapp.bean.Help;
import com.ubang.huang.ubangapp.bean.HelpInfo;
import com.ubang.huang.ubangapp.bean.HelpInfoUpdate;
import com.ubang.huang.ubangapp.bean.Job;
import com.ubang.huang.ubangapp.bean.Person;
import com.ubang.huang.ubangapp.bean.PersonInfo;
import com.ubang.huang.ubangapp.bean.SeekHelpInfo;
import com.ubang.huang.ubangapp.bean.ServiceMessage;
import com.ubang.huang.ubangapp.bean.User;
import com.ubang.huang.ubangapp.service.LocationService;
import com.ubang.huang.ubangapp.util.GetParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2019/1/29.
 * 通用的参数
 * @author = huangyouxin
 */

public class CP {

    public static final float UNSELECTSIZE = 15;
    public static final float SELECTSIZE = UNSELECTSIZE * 1.3f;
    public static final String[] SEEK_HELP_TAB = {"全部","未帮助","帮助中","已帮助"};
    public static final String[] Help_Others_Tab = {"时间最近","距离最近"};
    public static final String[] type = {"紧急求助","普通求助"};
    public static final String[] Help_Status = {"帮助中","已帮助"};
    public static final String[] header = {"城市","公司","要求"};

    public static final int SEEKHELP = 0;
    public static final int HELPOTHERS = 1;
    public static final int NEWS = 2;

    public static final int Center = 0;
    public static final int Me = 1;
    public static final int You = 2;
    public static final int Text = 3;
    public static final int Pic = 4;
    public static final int CAMERA = 5;
    public static final int ALBUM = 6;
    public static final int GetURLPic = 10018;

    public static final int Send_Message = 10;
    public static final int HelpSort =11;
    public static final int Service_Notice = 1012;
    public static final int Service_Online = 1013;
    public static final int Service_Offline = 1014;
    public static final int DIP = 15;
    public static final int MyHelpList =16;


    public static final int Get_Position = 0;
    public static final int setAlarmInfo = 2;
    public static final int GetNews = 3;
    public static final int GetTag = 4;
    public static final int GetJobInfo = 5;
    public static final int GetLocalPosition = 6;
    public static final int GetEndpostion = 7;
    public static final int GetHelpListFilter = 9;
    public static final int VeritifuSuc = 10;

    public static List<Help> HelpInfoList = new ArrayList<>();

    public static final String ChatOnlineStatus = "对方在线，可以进行沟通";
    public static final String FinshHelp = "结束帮助";
    public static final String CancelHelp = "帮助取消成功";
    public static final String ChatOfflineStatus = "对方不在线，请尝试其他方式联系";
    public static final String SendChatFail= "由于种种原因，发送失败";
    public static final String UnHelpStatus = "无人问津";
    public static final String HelpingStatus = "帮助中";
    public static final String HelpedStatus = "已完成";
    public static final String vertify_code_fist = "【U帮】您的验证码";
    public static final String vertify_code_latter = "，请在页面中提交验证，5分钟内输入有效【U帮】您的验证码";

    public static final String Message_APPKEY = "8009cb57ac35ed61";
    public static final String Message_content_first = "您的手机验证码为";
    public static final String Message_content_latter = "，5分钟内有效。请不要把此验证码泄露给任何人。【Ubang】";

    public static final String Linear = "Linear";
    public static final String Gallery = "Gallery";
    public static final String Grid = "Grid";

    public static Boolean MyHelp = false;
    public static int ChatByWay= 0;
    public static final int ChatByNotice = 0;
    public static final int ChatByHelp = 1;
    public static final int ChatBySeekHelp = 2;

    public static int DisPlay = 0;

    public static final int RefreshUserAndNotice = 0;
    public static final int RefreshUser = 1;

    public static int SeekHelpListPageNUM = 0;
    public static Boolean getEndPosition = false;

    public static String currentCity = "北京";
    public static String currentPosition = "北京";
    public static String currentDetailPosition = "北京";
    public static String endCurrentPosition = "北京";
    public static String endCurrentDetailPosition = "北京";
    public static LatLng currentLatLng = new LatLng(0,0);
    public static LatLng endCurrentLatLng = new LatLng(0,0);
    public static LatLng CurrentLatLng = new LatLng(0,0);

    public static InputPlaceActivity inputPlaceActivity =null;

    public static Handler dataHandler;
    public static HandlerThread handlerThread ;

    public static List<SuggestionResult.SuggestionInfo> suggestList = new ArrayList<>();
    public static List<String> suggestInfoList = new ArrayList<>();
    public static List<String> picList = new ArrayList<>();
    public static List<ServiceMessage> ServiceList = new ArrayList<>();
    public static List<GetNews> getNewsList = new ArrayList<>();

    public static final String Uid = "小小浪一下";
    public static final String Key = "d41d8cd98f00b204e980";
    public static final String smsMob = "15880272131";
    public static String smsText = "验证码：8888";

    public static String Notice = "有人决定帮助你，请查看";


    public static int help_id = 0;

    public static int currentPage = 0;
    public static final int LearnPage = 0;
    public static final int AccompanyPage = 1;
    public static final int ReplacePage = 2;
    public static final int LostPage = 3;

    public static int Distant = 0;

    public static String HelpContent = "你好，请问有什么我可以帮你的吗？";

    public static String Service_Greeting = "你好，请问有什么我可以帮你的吗？";

    public static String currentPageType = "陪同";

    public static Boolean isEmegency = false;

    public static List<AlarmContent> contentList = new ArrayList<>();
    public static PersonInfo personInfo = new PersonInfo();

    public static List<HelpInfoUpdate> personSeekHelp = new ArrayList<>();

    public static User user = new User();
    public static int alarm_content_num = 3;

    public static DialogPlus dialogPlus;

    public static LocationService alarmLocationService;
    public static int firstLocal = 0;
    public static  Message message;
    public static Handler alarmHeplHandler;
    public static HandlerThread alarmHeplHandlerThread;
    public static Handler LearnHandler;
    public static Handler AccompanyHandler;
    public static Handler LostHandler;
    public static HandlerThread ReplaceHandlerThread;
    public static Handler ReplaceHandler;
    public static Handler HelpInfoHandler;

    public static BDAbstractLocationListener bdaListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError && firstLocal == 0) {
                CP.CurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                CP.personInfo.setLatLon(location.getLatitude()+","+location.getLongitude());
                CP.personInfo.setAddress(location.getAddrStr());
                CP.personInfo.setCity(location.getCity());
                CP.personInfo.setDetailAdress(location.getLocationDescribe());

                GetParam.SendMessage(alarmHeplHandler,setAlarmInfo);
                firstLocal++;
            }
        }
    };

    public static  final int  Service = 0;
    public static  final int  User = 1;

    public static  int  isUrgent = 0;

    public static  final String  ServiceApiKey = "96909b704ab1451f89b68ade2d646d87";
    public static  final String  ServiceUserId = "90c1d1d471e6a55d";
    public static  final String  NewsApiId = "8009cb57ac35ed61";
    public static  final String  NewsKey = "校园诈骗";
    public static  final int  NewsNum = 10;

    public static  GetNews  getNews = new GetNews();
    public static CampusSafe campusSafe = new CampusSafe();

    public static ArrayList<String> MOUDLE_JOB_CITY_TAG = new ArrayList<>();
    public static ArrayList<String> MOUDLE_JOB_PEOPLE_TAG = new ArrayList<>();
    public static ArrayList<String> MOUDLE_JOB_EXP_TAG = new ArrayList<>();
    public static ArrayList<String> MOUDLE_JOB_DEGREE_TAG = new ArrayList<>();
    public static ArrayList<String> MOUDLE_JOB_SALARY_TAG = new ArrayList<>();
    public static ArrayList<String> MOUDLE_JOB_COMPANY_TYPE_TAG = new ArrayList<>();

    public static final int CITY_INFO = 0;
    public static final int COMPANY_SUM_INFO = 1;
    public static final int COMPANY_TYPE_INFO = 2;
    public static final int EXP_INFO = 3;
    public static final int DEGREE_INFO = 4;
    public static Job jobInfo = new Job();
    public static List<Job> jobInfoList = new ArrayList<>();
    public static Help helpInfo = new Help();
    public static SeekHelpInfo seekHelpInfo = new SeekHelpInfo();

    public static ChatMessage chatMessage;
    public static Bitmap bitmap;

    public static final ArrayList<String> help_type = new ArrayList<String>(){
        {
            add("紧急求助");
            add("普通求助");
        }
    };

    public static final ArrayList<String> help_sort = new ArrayList<String>(){
        {
            add("学习");
            add("陪同");
            add("代取");
            add("失物");
            add("其他");
        }
    };

    public static final ArrayList<String> emegency_help_sort = new ArrayList<String>(){
        {
            add("陪同");
            add("代取");
            add("失物");
            add("其他");
        }
    };

    public static final ArrayList<String> feedBack_type = new ArrayList<String>(){
        {
            add("U友求助功能");
            add("帮助U友功能 ");
            add("资讯功能");
            add("其他");
        }
    };
}
