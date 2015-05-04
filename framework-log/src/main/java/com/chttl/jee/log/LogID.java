package com.chttl.jee.log;

import java.io.Serializable;
import java.util.Calendar;

public class LogID {

	/** LogID 實際上儲存的 thread local 變數 */
	private static final ThreadLocal<String> currentLogId = new ThreadLocal<String>(){
		@Override
		protected String initialValue(){ return null; }
	};
	
	private static final ThreadLocal<LogData> currentLogData = new ThreadLocal<LogData>(){
		@Override
		protected LogData initialValue(){ return new LogData(); }
	};
	
	
	/** 取得目前的 Log ID */
	public static String getCurrentLogID(){
		return currentLogId.get();
	}
	
	/** 取得目前的 Log Data */
	public static LogData getCurrentLogData(){
		return currentLogData.get();
	}
	
	/** 初始化 Log ID (每一request只需處理一次) */
	public static void startLogId(){
		currentLogId.set(newLogId(null));
	}

	/** 初始化 Log ID(使用session id) (每一request只需處理一次) */
	public static void startLogId(String sessionId){
		currentLogId.set(newLogId(sessionId));
	}
	
	/**
	 * 初始化 Log ID (每一request只需處理一次).
	 * 
	 * @return 若是由本次呼叫設定LogID則回傳true; 否則回傳false.
	 */
	public static boolean startLogidWithCheck(String prevLogId){
		boolean prevId_valid = (prevLogId != null);
		boolean logid_exists = (currentLogId.get() != null);

		// log id 如果已存在, 就不做任何事
		if (logid_exists) return false;
		else{ // 假如不在, 就看是否需要產生 (帶入的ID有效則以該ID為主)
			if (prevId_valid) currentLogId.set(prevLogId);
			else startLogId();
			return true;
		}
	}
	
	/** 結束 Log ID */
	public static void endLogId(){
		currentLogId.remove();
		currentLogData.remove();
	}
	
	/**
	 * Log ID 實際產生
	 * 
	 * 2013-10-08 logId產生規則修正
	 *     由於session create可能發生在開始處理request之後, 故將此介面調整為可多次呼叫
	 *     可以後續再補入 session id part
	 * 
	 * @return 隨機產生的一組Log ID
	 */
	private static String newLogId(String sessionId){
	    // 2014-04-07 logid 長度調整
	    // 1. server_name 把 0 排除
	    // 2. sessionid 與 server id 接起來, thread id 和 timestamp接起來
	    LogData logData = getCurrentLogData();
	    if (logData.logid_prefix == null){
	        String server_name = System.getProperty("server.name");
	        if (server_name == null) server_name = "L";
	        server_name = server_name.replaceAll("0", "");

	        logData.logid_prefix = "{" + server_name;
	    }

	    if (logData.logid_postfix == null){
	        String thread_id = genTextSerial(Thread.currentThread().getId());
	        String timestamp = genTextSerial(System.currentTimeMillis());
	        if (timestamp.length() > 4)
	            timestamp = timestamp.substring(timestamp.length() - 4); // 取後4碼就好 縮短長度

	        logData.logid_postfix = thread_id + timestamp + "}";
	    }

	    if (logData.logid_sessionid == null && sessionId != null){
	        logData.logid_sessionid = sessionId;
	    }
	    
        //加日期，加在 session part
	    Calendar cal = Calendar.getInstance();
	    String daymark = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
	    
	    String sessionid_part = daymark + "_";
	    if (logData.logid_sessionid != null){
	        sessionid_part = 
                // sessionid 取後4碼就好,縮短長度
                logData.logid_sessionid.substring(logData.logid_sessionid.length() - 4) + daymark + "_"; 
	    }

		return logData.logid_prefix + sessionid_part + logData.logid_postfix;
	}
	
	
	/** 仿聯單序號編碼: 34 進位，10+26-2 (去除'I' and 'O') */
    private static String genTextSerial (long serial)
    {
        final String encodeChars = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ"; // remove character 'I' and 'O'
        final int encodeChars_length = encodeChars.length();

        StringBuilder sb = new StringBuilder();
        long st = serial;
        while (st > 0)
        {
            int offset = (int) (st % encodeChars_length);
            sb.append(encodeChars.charAt(offset));
            st = (st - offset) / encodeChars_length;
        }

        //sb.append(String.format("%" + max_serial_length + "s", "").replaceAll(" ", "0"));
        sb.reverse();
        return sb.toString();
    }
    
    /** 清除所有相關資源 */
    public static synchronized void clean(){
        // 原本應將 currentLogId , currentLogData 全部清除
        // 但為避免在 shutdown 時, 仍有執行中 request , 可能會引發 log 失敗問題, 故不做清理
        // 理論上各 request 結束會自行清理, 故只會留下二個內容為 0 的物件
    }
    
    
    public static class LogData implements Serializable{
        private static final long serialVersionUID = 1L;

        public StringBuilder sb = new StringBuilder();
		public int level = 1;
		public String logid_prefix = null, logid_postfix = null, logid_sessionid = null;

	}
    
    
	
}