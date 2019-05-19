package com.supadata.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DateUtil {
	
	/**
	 * 获取指定日期的指定天数后的日期
	 * @param date 指定日期
	 * @param days 指定推后的天数
	 * @return 
	 */
	public static String getAfterDate(java.util.Date date,int days) {
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.add(Calendar.DAY_OF_MONTH, days);  //设置为前n天
		date = calendar.getTime();   //得到前n天的时间

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
		String beforeDate = sdf.format(date);    //格式化前n天
		return beforeDate;
	}
	/**
	 * 获取"yyyy-mm-dd"格式的当前日期
	 * @return
	 */
	public static String getCurrentDate(){
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd");
		return tempDate.format(new java.util.Date());
	} 
	/**
	 * 获取"yyyy-mm-dd"格式的当前日期
	 * @return
	 */
	public static String getCurrentDateTime(){
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return tempDate.format(new java.util.Date());
	} 
	
	/**
	* 
	* @Title: getCurDate    
	* @Description: TODO(当前日期对应的timestamp值)    
	* @param @return    设定文件    
	* @return Timestamp    返回类型    
	* @throws
	*/
	public static String getTimestamp(){
		java.util.Date date=new java.util.Date();
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
	
	/**
	 * 生成当前时间的字符串，添加两位随机数
	 * @return
	 */
	public static String getOutTradeNo(){
		java.util.Date date=new java.util.Date();
		String str = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		int a = (int)(Math.random()*1000);
		if (a < 10) {
			a = (int)(Math.random()*1000);
		}
		return str + "_" + a;
	}
	/**
	* 
	* @Title: changeDate    
	* @Description: TODO(字符串转换成sql中的date)    
	* @param @param str
	* @param @return    设定文件    
	* @return Date    返回类型    
	* @throws
	*/
	public static Date changeDate(String str){
		try {
			java.util.Date date= new SimpleDateFormat("yyyy-MM-dd").parse(str);
			return new Date(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* 
	* @Title: changeDateByStr    
	* @Description: TODO(将字符串指定格式转换成sql中的timestamp)    
	* @param @param dateStr  2016-10-21 03:59:00
	* @param @param format
	* @param @return    设定文件    
	* @return Timestamp    返回类型    
	* @throws
	*/
	public static Timestamp changeDateByStr(String dateStr){
		try {
			java.util.Date date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* 
	* @Title: getCurDate    
	* @Description: TODO(当前日期对应的timestamp值)    
	* @param @return    设定文件    
	* @return Timestamp    返回类型    
	* @throws
	*/
	public static Timestamp getCurDate(){
		java.util.Date date=new java.util.Date();
		String str= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		System.out.println(str);
		return Timestamp.valueOf(str);
	}
	
	/**
	* 
	* @Title: getDate    
	* @Description: TODO(当前日期获取年月日)    
	* @param @return    设定文件    
	* @return String    返回类型    
	* @throws
	*/
	public static String getDate(){
		java.util.Date date=new java.util.Date();
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
		
	}
	
	/**
	* 
	* @Title: fromTimeToStr    
	* @Description: TODO(timestamp转成字符串)    
	* @param @param timestamp
	* @param @return    设定文件    
	* @return String    返回类型    
	* @throws
	*/
	public static String fromTimeToStr(Timestamp timestamp){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
	}
	/**
	* 
	* @Title: Date时间转换为String    
	* @Description: TODO(timestamp转成字符串)    
	* @param @param timestamp
	* @param @return    设定文件    
	* @return String    返回类型    
	* @throws
	*/
	public static String fromDateToStr(java.util.Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	/**
	 * 字符串类型日期转化
	 * @param strDate
	 * yyyyMMddHHmmss ---> yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String fromStrintDate(String strDate){
		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DateUtil.fromDateToStr(date);
	}
	/**
	* 
	* @Title: timestampToStr    
	* @Description: TODO(timestamp转成无间隔的字符串)    
	* @param @param timestamp
	* @param @return    设定文件    
	* @return String    返回类型    
	* @throws
	*/
	public static String timestampToStr(String timestamp){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义第一个模板
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss"); // 定义第一个模板
		java.util.Date date = null;
		try {
			date = sdf1.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf2.format(date);
	}
	
	

	 /** 
    * 两个时间相差多少分钟
    * @param str1 较小时间 格式：1990-01-01 12:00:00 
    * @param str2 较大时间 2 格式：2009-01-01 12:00:00 
    * @return String 返回值为：分钟数
    * 		  t1<t2为正值,t1>t2为负值
    */  
   public static String getDistanceTimes(String str1, String str2) {  
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
       java.util.Date one;  
       java.util.Date two;  
       long day = 0;  
       long hour = 0;  
       long min = 0;  
       try {  
           one = df.parse(str1);  
           two = df.parse(str2);  
           long time1 = one.getTime();  
           long time2 = two.getTime();  
           long diff = time2 - time1;  
           day = diff / (24 * 60 * 60 * 1000);  
           hour = (diff / (60 * 60 * 1000) - day * 24);  
           min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
           min=hour*60+min;
       } catch (Exception e) {  
           e.printStackTrace();  
       }  
       return Long.toString(min);  
   }  
   /**
    * 计算两个日期相差年数  
    * @param startDate
    * @param endDate
    * @return
    */
   public static int yearDateDiff(String startDate,String endDate){  
      Calendar calBegin = Calendar.getInstance(); //获取日历实例  
      Calendar calEnd = Calendar.getInstance();  
      calBegin.setTime(stringTodate(startDate,"yyyy")); //字符串按照指定格式转化为日期  
      calEnd.setTime(stringTodate(endDate,"yyyy"));  
      return calEnd.get(Calendar.YEAR) - calBegin.get(Calendar.YEAR);  
   }  
   /**
    * 字符串按照指定格式转化为日期  
    * @param dateStr
    * @param formatStr
    * @return
    */
   public static java.util.Date stringTodate(String dateStr, String formatStr) {  
      // 如果时间为空则默认当前时间  
	   java.util.Date date = null;  
      SimpleDateFormat format = new SimpleDateFormat(formatStr);  
      if (dateStr != null && !dateStr.equals("")) {    
   String time = "";  
   try {  
	   java.util.Date dateTwo = format.parse(dateStr);  
      time = format.format(dateTwo);  
      date = format.parse(time);  
  } catch (ParseException e) {  
      e.printStackTrace();  
         }  
    
      } else {  
    String timeTwo = format.format(new java.util.Date());  
    try {  
      date = format.parse(timeTwo);  
    } catch (ParseException e) {  
      e.printStackTrace();  
    }  
     }  
     return date;  
  }  
   /**
    * 计算出勤时间
    * @param t1 第一次打卡时间
    * @param t2 第二次打卡时间
    * @param t3 课表开始时间
    * @param t4 课表结束时间
    * @return Map: 
    * 		  Late 迟到时间, Early  早退时间
    * 		  Amount  实际出勤总时间, Must  应该出勤总时间
    */
	public static HashMap<String,String> CalculatedAttendanceTime(String t1,
			String t2, String t3, String t4) {
		//计算应出勤时间
		String must=getDistanceTimes(t3,t4);
		String amount="";
		String late="";
		String early="";
		if ("0".equals(must)) {
			must="480";
			//课表起始时间也要修改以计算迟到时间
		}
		/**case1:只打一次卡、只有上班打卡(可能多次)、只有下班打卡*/
		//只打一次卡，按一天算
		String isSame=getDistanceTimes(t1,t2);
		//第一次打卡时间晚于下课时间，按忘记打卡算
		String isTooLate=getDistanceTimes(t1,t4);
		//最后一次打卡时间早于上课时间，按忘记打卡算
		String isTooEarly=getDistanceTimes(t3,t2);
		if ("0".equals(isSame) || isTooLate.startsWith("-") || isTooEarly.startsWith("-")) {
			amount=must;
			late="0";
			early="0";
		}
		
		//计算迟到时间late,计算早退时间，计算总出勤时间，计算应该出勤总时间
		String lateMinute=getDistanceTimes(t1,t3);
		String earlyMinute=DateUtil.getDistanceTimes(t4,t2);
		/**case2:正常打卡(2次) 但有迟到、早退*/
		if (lateMinute.startsWith("-") || earlyMinute.startsWith("-")) {
			if (lateMinute.startsWith("-")) {//迟到
				late=lateMinute.substring(1);
			}else {
				late="0";
			}
			if (earlyMinute.startsWith("-")) {//早退
				early=earlyMinute.substring(1);
			}else {
				early="0";
			}
			if (!"0".equals(late) || !"0".equals(early)) {
				long tmp=Long.parseLong(must)-Long.parseLong(late)-Long.parseLong(early);
				amount=Long.toString(tmp);
			}
		} else {/**case3:正常打卡(2次) 无迟到、早退*/
			late="0";
			early="0";
			amount=must;
		}
		HashMap<String,String> timeMap=new HashMap<String, String>();
		timeMap.put("Late", late);
		timeMap.put("Early", early);
		timeMap.put("Amount", amount);
		timeMap.put("Must", must);
		return timeMap;
	}
	
	/**
	 * 字符串类型日期转换为date类型
	 * @param inTime "2017-11-27"
	 * @return
	 */
	public static java.util.Date StringToDate(String inTime) {
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = fmt.parse(inTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取"yyyy-MM-dd HH:mm:ss"格式的当前日期
	 * @return
	 */
	public static String getCurrentTime(String format){
		SimpleDateFormat tempDate = new SimpleDateFormat(format);
		return tempDate.format(new java.util.Date());
	} 
	
	/**
	 * 获取指定时间与当前时间的时间间隔
	 * @param datetime
	 * @return 间隔秒数
	 */
	public static int getIntervalTime(String datetime) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = getCurrentTime("yyyy-MM-dd HH:mm:ss");
		int time = 0;
		try {
			java.util.Date date = myFormatter.parse(datetime);
			java.util.Date cdate = myFormatter.parse(currentTime);

			time = (int) ((cdate.getTime() - date.getTime()) / (1000));
			if (time == 0) {
				time = 1;
			}
			// 这里精确到了秒，我们可以在做差的时候将时间精确到天
		} catch (Exception e) {
			e.printStackTrace();
			return time;
		}
		return time;
	}

	public static long dateToLong(java.util.Date date) {
		return date.getTime();
	}

	// string类型转换为date类型
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	public static java.util.Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		java.util.Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	public static long stringToLong(String strTime, String formatType)
			throws ParseException {
		java.util.Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}
	public static void main(String[] args) {
		
//		String time1="2016-10-21";
//		String time2="2016-10-21 03:59:00";
//		System.out.println(getTimestamp());
//		System.out.println(getDistanceTimes("2018-07-02 03:59:00","2018-07-02 03:57:10"));

		System.out.println(dateToLong(new java.util.Date()));
		System.out.println(System.currentTimeMillis());
	}
}
