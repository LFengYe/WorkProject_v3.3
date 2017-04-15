package com.gpw.app.base;

/**
 * Created by gpw on 2016/10/12.
 * --加油
 */

public class Contants {
    public static String userId;
    public static String Tel;
    public static double Balance;
    public final static String SHARED_NAME = "gpwsp";
    public static String imagehost = "http://121.40.212.89:8009";
    private static String localhost = "http://121.40.212.89:8009/";
    public static String url_register = localhost + "Register/Register";
    public static String url_editPassWord= localhost + "Users/EditPassWord";
    public static String url_updatePassWord= localhost + "Users/UpdatePassWord";
    public static String url_obtainCheckCode = localhost + "Visitors/ObtainCheckCode";
    public static String url_userLogin = localhost + "Register/UserLogin";
    public static String url_getUserAddress = localhost + "Consignor/GetUserAddress";
    public static String url_getUserVehicleTeam = localhost + "Consignor/GetUserVehicleTeam";
    public static String url_saveUserAddress = localhost + "Consignor/SaveUserAddress";
    public static String url_editUserAddress = localhost + "Consignor/EditUserAddress";
    public static String url_saveSuggest = localhost + "Users/SaveSuggest";
    public static String url_editUserInfo = localhost + "Users/EditUserInfo";
    public static String url_updateHeadPortrait = localhost + "Consignor/UpdateHeadPortrait";
    public static String url_getAdvertisings = localhost + "Visitors/GetAdvertisings";
    public static String url_getVehicleTypes = localhost + "Visitors/GetVehicleTypes";
    public static String url_getNotInvoiceList = localhost + "Consignor/GetNotInvoiceList";
    public static String url_applayInvoice = localhost + "Consignor/ApplayInvoice";
    public static String url_getUserMessages = localhost + "Users/GetUserMessages";
    public static String url_obtainMessage = localhost + "Visitors/ObtainMessage";
    public static String url_getUserBalanceList = localhost + "Finance/GetUserBalanceList";
    public static String url_getUserBalance = localhost + "Finance/GetUserBalance";
    public static String url_calculationFreight = localhost + "Basic/CalculationFreight";
    public static String url_lingDanFreight = localhost + "Basic/LingDanFreight";
    public static String url_calculatedPremium = localhost + "Basic/CalculatedPremium";
    public static String url_sendOrder = localhost + "Consignor/SendOrder";
    public static String url_publishCarpool = localhost + "Consignor/PublishCarpool";
    public static String url_deleteUserAddress = localhost + "Consignor/DeleteUserAddress";
    public static String url_getSendOrderList = localhost + "Consignor/GetSendOrderList";
    public static String url_confirmCancel = localhost + "Orders/ConfirmCancel";
    public static String url_cancelOrder = localhost + "Orders/CancelOrder";
    public static String url_getOrderOffers = localhost + "Consignor/GetOrderOffers";
    public static String url_getSendOrderDetail = localhost + "Consignor/GetSendOrderDetail";
    public static String url_getReceiptOrderDetail = localhost + "Consignor/GetReceiptOrderDetail";
    public static String url_getReceiptList = localhost + "Consignor/GetReceiptList";
    public static String url_payAmount = localhost + "Finance/PayAmount";
    public static String url_keepTransporter = localhost + "Consignor/KeepTransporter";
    public static String url_getVehicleLocation = localhost + "Basic/GetVehicleLocation";
    public static String url_updateOrder = localhost + "Orders/UpdateOrder";
    public static String url_deleteTransportTeam = localhost + "Consignor/DeleteTransportTeam";
    public static String url_confirmTransporter = localhost + "Consignor/ConfirmTransporter";
    public static String url_commentTransporter = localhost + "Consignor/CommentTransporter";
}
