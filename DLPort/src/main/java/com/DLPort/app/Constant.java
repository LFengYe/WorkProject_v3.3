package com.DLPort.app;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Constant {
    private static final String LocalHost = "http://app.dlnywl.com/";

    public static final String URL_UserLOGIN = LocalHost + "api/User/PostLogin";
    public static final String URL_UserGETIMAGE = LocalHost + "api/User/PostGetImage";
    public static final String URL_PostVehicleRegister = LocalHost + "api/User/PostVehicleRegister";
    public static final String URL_PostCargoRegister = LocalHost + "api/User/PostCargoRegister";
    public static final String URL_UserPostinviter = LocalHost + "api/User/Postinviter";
    public static final String URL_UserPostAboutUs = LocalHost + "api/User/PostAboutUs";
    public static final String URL_UserPostBack = LocalHost + "api/User/PostBack";
    public static final String URL_UserPostUpdateTelOrAdd = LocalHost + "api/User/PostUpdateTelOrAdd";
    public static final String URL_UserPostUpdatePwd = LocalHost + "api/User/PostUpdatePwd";
    public static final String URL_UserPostGetUser = LocalHost + "api/User/PostGetUser";


    public static final String URL_PostGetOrderDetail = LocalHost + "api/CarOwner/PostGetOrderDetail";
    public static final String URL_PostGrabOrder = LocalHost + "api/CarOwner/PostAttentionOrder";
    public static final String URL_PostAddVehicle = LocalHost + "api/CarOwner/PostAddVehicle";
    public static final String URL_PostGetMyCar = LocalHost + "api/CarOwner/PostGetMyCar";
    public static final String URL_PostRefresh = LocalHost + "api/CarOwner/PostRefresh";
    public static final String URL_PostGraspOrder = LocalHost + "api/CarOwner/PostGraspOrder";
    public static final String URL_PostOrderArrival = LocalHost + "api/CarOwner/PostOrderArrival";
    public static final String URL_PostCancelOrder = LocalHost + "api/CarOwner/PostCancelOrder";
    public static final String URL_CarOwnerPostGetMyAccount = LocalHost + "api/CarOwner/PostGetMyAccount";
    public static final String URL_CarOwnerPostStatistics = LocalHost + "api/CarOwner/PostStatistics";
    public static final String URL_PostGetMyOrder = LocalHost + "api/CarOwner/PostGetMyOrder";
    public static final String URL_CarOwnerPostVehPosition = LocalHost + "api/CarOwner/PostVehPosition";
    public static final String URL_CarOwnerPostDeleteCar = LocalHost + "api/CarOwner/PostDeleteCar";
    public static final String URL_CarOwnerPostEditCar = LocalHost + "api/CarOwner/PostEditCar";

    public static final String URL_GetOrderDetails = LocalHost + "api/CargoOwner/PostOrderDetails";
    public static final String URL_PostGetCarOrder = LocalHost + "api/CargoOwner/PostPostGetMyOrder";
    public static final String URL_PostPublishCargo = LocalHost + "api/CargoOwner/PostPublishCargo";
    public static final String URL_CargoOwnerPostReply = LocalHost + "api/CargoOwner/PostReply";/* 货主回复车主申请取消 */
    public static final String URL_CargoOwnerPostCharge = LocalHost + "api/CargoOwner/PostCharge";
    public static final String URL_CargoOwnerPostGetAccount = LocalHost + "api/CargoOwner/PostGetAccount";
    public static final String URL_CargoOwnerPostStatistics = LocalHost + "api/CargoOwner/PostStatistics";
    public static final String URL_CargoOwnerPostRemove = LocalHost + "api/CargoOwner/PostRemove";
    public static final String URL_CargoOwnerPostVehPosition = LocalHost + "api/CargoOwner/PostVehPosition";


    public static final String URL_InformationPostAllInform = LocalHost + "api/Information/PostAllInform";/* 获取全部信息(业务变更) */
    public static final String URL_InformationPostInform = LocalHost + "api/Information/PostInform";/* 获取未读信息(业务变更) */
    public static final String URL_InformationPostGetInformById = LocalHost + "api/Information/PostGetInformById";


    public static final String URL_PostGetzuoye = LocalHost + "api/Message/PostGetMessage";
    public static final String URL_PostGetyujing = LocalHost + "api/Message/PostGetEarlyWarning";
    public static final String URL_PostGetgaosu = LocalHost + "api/Message/PostRoadCondition";
    public static final String URL_PostGetguodao = LocalHost + "api/Message/PostNationalRoad";
    public static final String URL_PostGetguanyu = LocalHost + "api/Message/PostAboutUs";
    public static final String URL_MessagePostActivity = LocalHost + "api/Message/PostActivity";/* 平台活动 */
    public static final String URL_MessagePostInform = LocalHost + "api/Message/PostInform";/* 会员活动 */
    public static final String URL_MessageGetMessage = LocalHost + "api/Message/GetMessage";


    public static final String URL_OrderPostCarOrder = LocalHost + "api/Order/PostCarOrder";
    public static final String URL_OrderPostCargoOrder = LocalHost + "api/Order/PostCargoOrder";



    public static final String URL_CouponPrivilege = LocalHost + "api/Coupon/PostPrivilege";
    public static final String URL_HandbookPostHandbook = LocalHost + "api/Handbook/PostHandbook";
    public static final String URL_PostQuerySailSchedule = LocalHost + "api/SailSchedule/PostQuerySailSchedule";
    public static final String URL_PostQueryStorageYard = LocalHost + "api/StorageYard/PostQueryStorageYard";
    public static final String URL_PostAutomotiveRescue = LocalHost + "api/SeekHelp/PostAutomotiveRescue";
    public static final String URL_PostGetInsuranceList = LocalHost + "api/Insurance/PostGetInsuranceList";
    public static final String URL_PostAddInsurance = LocalHost + "api/Insurance/PostAddInsurance";
    public static final String URL_PostGetlicai = LocalHost + "api/ManageMmoney/PostGetMessage";
    public static final String URL_PostFinance = LocalHost + "api/ManageMmoney/PostFinance";
    public static final String URL_PostGetMessage = LocalHost + "api/OldCar/PostGetMessage";
    public static final String URL_PostMortgage = LocalHost + "api/Loan/PostMortgage";
    public static final String URL_EcshopPostGetMessage = LocalHost + "api/Ecshop/PostGetMessage";
    public static final String URL_EcshopPostExchange = LocalHost + "api/Ecshop/PostExchange";
    public static final String URL_PostInitShip = LocalHost + "api/SailSchedule/PostInitShip";
    public static final String URL_PostCarList = LocalHost +"api/Hold/PostCarList";
    public static final String URL_PostHoldBoxList = LocalHost + "api/Hold/PostHoldBoxList";


    public static final String URL_PaymentPostPay = LocalHost + "api/Payment/PostPay";
    public static final String URL_PaymentPostQueryPay = LocalHost + "api/Payment/PostQueryPay";
    public static final String URL_PaymentPostFinishPay = LocalHost + "api/Payment/PostFinishPay";
    public static final String URL_PaymentPaySuccess = LocalHost + "paysuccess.html";
    public static final String URL_PaymentPayFailure = LocalHost + "payfailure.html";
    public static final String URL_PaymentWithdrawCash = LocalHost + "api/Payment/WithdrawCash";
    public static final String URL_PaymentPostWithdrawCashList = LocalHost + "api/Payment/PostWithdrawCashList";
    public static final String URL_PaymentPostHistory = LocalHost + "api/Payment/PostHistory";
    public static final String URL_PaymentPostRecharge = LocalHost+"api/Payment/PostRecharge";
    public static final String URL_PaymentPostSuitcase = LocalHost+"api/Payment/PostSuitcase";
    public static final String URL_PaymentPostPutTheBox = LocalHost+"api/Payment/PostPutTheBox";
    public static final String URL_PaymentPostGetCount = LocalHost + "api/Payment/PostGetCount";

    public static final String FuYou_TestMchntCd = "0001000F0358674";
    public static final String FuYou_TestMchntKey = "d8n0dh23w2yzrnez52ocqb4ckzp7t0fs";
    public static final String FuYou_TestPayAction = "http://www-1.fuiou.com:18670/mobile_pay/h5pay/payAction.pay";
    public static final String FuYou_TestQueryOrder = "http://www-1.fuiou.com:18670/mobile_pay/findPay/queryOrderId.pay";
    public static final String FuYou_TestCardBinQuery = "http://www-1.fuiou.com:18670/mobile_pay/findPay/cardBinQuery.pay";
    public static final String Fuiou_MchntCd = "0002220F0286562";
    public static final String Fuiou_MchntKey = "o7ah35xpakqa29102bga38g3bf5ijd2n";
    public static final String Fuiou_PayAction = "https://mpay.fuiou.com:16128/h5pay/payAction.pay";
    public static final String Fuiou_QueryOrder = "https://mpay.fuiou.com:16128/findPay/queryOrderId.pay";
    public static final String Fuiou_CardBinQuery = "https://mpay.fuiou.com:16128/findPay/cardBinQuery.pay";
}
