package cn.com.caronwer.base;

public class Contants {
    //public static String userId;
    public static String Tel;
    public static double Balance;

    //位置支付app Id
    public final static String weChatAPPID = "wx1c2aa1319031558f";

    public final static String SHARED_NAME = "itheima";
//    public static String imagehost = "http://121.40.212.89:8009";
    public static String imagehost = "http://101.201.74.192:8018";
//    public static String imagehost = "http://110.53.162.172:7000/";
//    private static String localhost = "http://121.40.212.89:8009/";
    private static String localhost = "http://101.201.74.192:8018/";
//    private static String localhost = "http://110.53.162.172:7000/";
    public static String url_register = localhost + "Register/Register";
    public static String url_obtainCheckCode = localhost + "Visitors/ObtainCheckCode";
    public static String url_userLogin = localhost + "Register/UserLogin";
    public static String url_getUserAddress = localhost + "Consignor/GetUserAddress";
    public static String url_getUserVehicleTeam = localhost + "Consignor/GetUserVehicleTeam";
    public static String url_saveUserAddress = localhost + "Consignor/SaveUserAddress";
    public static String url_editUserAddress = localhost + "Consignor/EditUserAddress";
    public static String url_saveSuggest = localhost + "Users/SaveSuggest";

    public static String url_editUserInfo = localhost + "Users/EditUserInfo";
    public static String url_getUserInfo = localhost + "Users/GetUserInfo";

    public static String url_updateHeadPortrait = localhost + "Transporter/UpdateHeadPortrait";

    public static String url_getAdvertisings = localhost + "Visitors/GetAdvertisings";

    public static String url_getvehicletypes = localhost + "Visitors/GetVehicleTypes";
    public static String url_getNotInvoiceList = localhost + "Consignor/GetNotInvoiceList";
    public static String url_applayInvoice = localhost + "Consignor/ApplayInvoice";
    public static String url_getUserMessages = localhost + "Users/GetUserMessages";
    public static String url_obtainMessage = localhost + "Visitors/ObtainMessage";
    public static String url_getUserBalanceList = localhost + "Finance/GetUserBalanceList";
    public static String url_getUserBalance = localhost + "Finance/GetUserBalance";


    public static String url_editPassWord = localhost + "Users/EditPassWord";
    public static String url_editWorkstatus = localhost + "Transporter/EditWorkStatus";
    public static String url_gettransporterorderlist = localhost + "Transporter/GetTransporterOrderList";
    public static String url_getorders = localhost + "Visitors/GetOrders";
    public static String url_addbankcard = localhost + "Basic/AddBankCard";
    public static String url_graborder = localhost + "Transporter/GrabOrder";
    public static String url_gettransporterorderList = localhost + "Transporter/GetTransporterOrderList";
    public static String url_savevehicleinfo = localhost + "Transporter/SaveVehicleInfo";
    public static String url_getuserbankcard = localhost + "Basic/GetUserBankCard";
    public static String url_commentsender = localhost + "Transporter/CommentSender";
    public static String url_payAmount = localhost + "Finance/PayAmount";
    public static String url_updatevehicleLocation = localhost + "Basic/UpdateVehicleLocation";
    public static String url_transporteroffer = localhost + "Transporter/TransporterOffer";
    public static String url_OrdersConfirmCancel = localhost + "Orders/ConfirmCancel";
    public static String url_cancelorder = localhost + "Orders/CancelOrder";
    public static String url_updateOrder = localhost + "Orders/UpdateOrder";
    public static String url_getvehMsg = localhost + "Transporter/GetVehMsg";
    public static String url_applaywithdrawals = localhost + "Finance/ApplayWithdrawals";

    public static String url_TransporterGetOrderDetails = localhost + "Transporter/GetOrderDetails";


    public static String url_AlreadyExistingOrder = localhost + "Transporter/AlreadyExistingOrder";
    public static String url_TransporterVehicleCheck1 = localhost + "Transporter/VehicleCheck1";
    public static String url_TransporterVehicleCheck2 = localhost + "Transporter/VehicleCheck2";
    public static String url_TransporterVehicleCheck3 = localhost + "Transporter/VehicleCheck3";
    public static String url_TransporterGetVehicleCheck1 = localhost + "Transporter/GetVehicleCheck1";
    public static String url_TransporterGetVehicleCheck2 = localhost + "Transporter/GetVehicleCheck2";
}
