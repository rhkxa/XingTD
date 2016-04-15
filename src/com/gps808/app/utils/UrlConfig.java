package com.gps808.app.utils;

public class UrlConfig {

	public static String SERVER = "";
	
	
	
	// private static final String SERVER = "http://139.129.23.31/shop-api/";

	/**
	 * 登录
	 * 
	 * post json {"username":zss,"password":4297f44b13955235245b2497399d7a93"
	 * }密码需要md5加密
	 * 
	 * @return
	 */
	public static final String getLogin() {
		return SERVER + "login/login";
	}

	// 注册
	public static final String getRegister() {
		return SERVER + "register/submit";
	}

	/**
	 * 用户退出登录 get
	 * 
	 * @return
	 */
	public static final String getLoginOut() {
		return SERVER + "login/logout";
	}

	/**
	 * 发送消息推送信息
	 * 
	 * post json {"channelId":"4923859573096872165","appId":"0696110321",
	 * "userId":"0696110321" }
	 * 
	 * @return
	 */
	public static final String getPush() {
		return SERVER + "user/msgPush";
	}

	/**
	 * 版本信息 get
	 * 
	 * @return
	 */
	public static final String getAppVersion() {
		return SERVER + "info/appVersion?mobileType=3";
	}

	/**
	 * 根据查询条件查询用户所有车辆位置信息，并将位置信息在地图中显示 post
	 * json{"plateNo":"889","simNo":"131123" }
	 * 
	 * @return
	 */
	public static final String getVehicleLocations() {
		return SERVER + "vehicle/locations";
	}

	/**
	 * 根据车辆标识查询车辆的详细信息 get
	 * 
	 * @param vid
	 * @return
	 */
	public static final String getVehicleVehInfo(String vid) {
		return SERVER + "vehicle/vehInfo?vId=" + vid;
	}

	/**
	 * 用户可以自定义设备名称 post {"vId":1109,"tmnlName":"张三" }
	 * 
	 * @param vid
	 *            车辆ID
	 * @return
	 */
	public static final String getSetTmnlName() {
		return SERVER + "vehicle/setTmnlName";
	}

	/**
	 * 根据车辆ID查询车辆的位置信息，车辆跟踪功能会通过定时调用该方法，刷新车辆位置 get
	 * 
	 * @param vid
	 *            车辆ID
	 * @return
	 */
	public static final String getLocationById(String vid) {
		return SERVER + "vehicle/locationById?vId=" + vid;
	}

	/**
	 * 根据查询条件 ，查询车辆的历史行驶信息 post json
	 * {"vId":1109,"start":"2015-11-26 16:14:10","end":"2015-11-26 16:14:10"}
	 * 
	 * @return
	 */
	public static final String getVehicleGPSHistory() {
		return SERVER + "vehicle/gpsHistory";
	}

	/**
	 * 查询车辆列表信息 post
	 * json{"plateNo":"112","simNo":"123","status":0,"startPage":0,"pageNo":10 }
	 * 
	 * @return
	 */

	public static final String getVehicleVehicleByPage() {
		return SERVER + "vehicle/vehicleByPage";
	}

	/**
	 * 查询车队车辆列表 post json{ "placeName":"北京","startPage":0,"pageNum":10 }
	 * 
	 * @return
	 */

	public static final String getVehicleRoutes() {
		return SERVER + "vehicle/routesByGroup";
	}

	/**
	 * 获取当前用户各类消息的未读消息数量 get
	 * 
	 * @param rid
	 *            路线ID
	 * @return
	 */
	public static final String getVehicleRoutesInfo(String rid) {
		return SERVER + "vehicle/routeInfo?rId=" + rid;
	}

	/**
	 * 报警信息列表用于分页获取所有分类的消息
	 * 
	 * @param startPage
	 *            开始页
	 * @param pageNo
	 *            多少行
	 * @return
	 */
	public static final String getVehicleAlarms(int startPage, int pageNum) {
		return SERVER + "vehicle/alarms?startPage=" + startPage + "&pageNum="
				+ pageNum;
	}

	/**
	 * 车辆报警详细用于删除消息
	 * 
	 * @param aId
	 * @return
	 */
	public static final String getVehicleAlarms(String aId) {
		return SERVER + "vehicle/alarmInfo?aId=" + aId;
	}

	/**
	 * 查询用户报警设置项信息 get
	 * 
	 * @return
	 */
	public static final String getVehicleAlarmsOtions() {
		return SERVER + "vehicle/alarmOptions";
	}

	/**
	 * 对报警消息接收进行设置 post json
	 * {"isAcceptAlarm":false,"isSound":true,"isVibration":false,
	 * "emergency":false,"overSpeed":true,"inArea":true,"outArea":true}
	 * 
	 * @return
	 */
	public static final String getVehicleSetAlarms() {
		return SERVER + "vehicle/setAlarm";
	}

	/**
	 * 查询用户的系统设置信息
	 * 
	 * 
	 * @return
	 */
	public static final String getUserOptions() {
		return SERVER + "user/options";
	}

	/**
	 * 设置用户的配置信息post json {"monitorInterval":10,"trackInterval":10}
	 * 
	 * @return
	 */

	public static final String getUserSetOptions() {
		return SERVER + "user/setOptions";
	}

	/**
	 * 用户重置密码post json
	 * 
	 * {"oldPassword":"1231","newPassword":"123"} 原始密码和新密码必须进行MD5加密后传输
	 * 
	 * @return
	 */

	public static final String getUserResetPassword() {
		return SERVER + "user/resetPassword";
	}

	/**
	 * 分页查询消息列表。
	 * 
	 * @param startPage
	 * @param pageNum
	 * @return
	 */
	public static final String getMessages(String startPage, String pageNum) {
		return SERVER + "user/messages?startPage=" + startPage + "&pageNum="
				+ pageNum;
	}

	/**
	 * 查询消息详细信息
	 * 
	 * @param mId
	 * 
	 * @return
	 */
	public static final String getMessageInfo(String mId) {
		return SERVER + "user/messageInfo?mId=" + mId;
	}

	/**
	 * 意见反馈 post json{"content":"意见反馈内容"}
	 * 
	 * @return
	 */
	public static final String getUserQuestion() {
		return SERVER + "user/querstion";
	}

	/**
	 * 查询用户下的所有司机
	 * 
	 * @param startPage
	 * @param pageNum
	 * @return
	 */
	public static final String getDrivers(int startPage, int pageNum) {
		return SERVER + "user/drivers?startPage=" + startPage + "&pageNum="
				+ pageNum;
	}

	/**
	 * 查询司机详细信息
	 * 
	 * @param driverId
	 * @return
	 */
	public static final String getDriverInfo(int driverId) {
		return SERVER + "user/driverInfo?driverId=" + driverId;
	}

	/**
	 * 增加、编辑司机信息 post
	 * json{"driverId":1211,"driverName":"司机名称","phone":"13112311231"
	 * ,"loginName":"登录名称","status":1,"password":"123123"} 1.driverId：司机唯一编号>
	 * 0时，表示修改操作，否则表示新增加司机； 2.password：密码必须以MD5加密后传输； 3.loginName:司机登录名称，唯一性约束；
	 * 
	 * @return
	 */
	public static final String getAddDriver() {
		return SERVER + "user/addDriver";
	}

	/**
	 * 天气
	 * 
	 * @param lng
	 * @param lat
	 * @return
	 */
	public static final String getWeather(double lng, double lat) {
		return SERVER + "info/weather?lng=" + lng + "&lat=" + lat;
	}

	/**
	 * 当前天气
	 * 
	 * @param lng
	 * @param lat
	 * @return
	 */
	public static final String getNowWeather(double lng, double lat) {
		return SERVER + "info/nowWeather?lng=" + lng + "&lat=" + lat;
	}

	public static final String getMatchVichcle(String loc, String eId) {
		return SERVER + "vehicle/matchingVehicle?location=" + loc + "&eId="
				+ eId;
	}

}
