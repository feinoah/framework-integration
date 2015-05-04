package com.chttl.jee.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.LoggerContext;

public class XLog {

	public static final String DEFAULT_SYSLOG_CATEGORY = "SYSTEM" ;
	public static final String DEFAULT_ERRLOG_CATEGORY = "ERROR" ;
	
	private static ReadWriteLockHashMap<String, Logger> loggerMap = new ReadWriteLockHashMap<String, Logger> ();
	
	static Logger defaultLogger = null;
    static Logger defaultErrorLogger = null;
    
    /**
     * 目前Log的初始化狀態
     * 0: 未初始化, 初始化失敗 (以System.out 輸出至 Console)
     * 1: 一般 console 狀態, 有基本的 Log 功能, 但僅輸出至 console
     * 2: 有正常透過 logback.xml 設定
     */
    public static int initialStatus =0 ;
    
    private static boolean isInitialized = false;
    
    public static void debug (String category, String msg, Object... params)
    {
        log(LogLevel.DEBUG, category, msg, true, params);
    }

    public static void info (String category, String msg, Object... params)
    {
        log(LogLevel.INFO, category, msg, true, params);
    }
    
    public static void warn (String category, String msg, Object... params)
    {
        log(LogLevel.WARN, category, msg, true, params);
    }
    
    public static void error (String category, String msg, Object... params)
    {
        log(LogLevel.ERROR, category, msg, true, params);
    }

    public static void simplelog (LogLevel level, String category, String msg, Object... params)
    {
        log (level, category, msg, false, params);
    }
    
    public static void log (LogLevel level, String category, String msg, boolean withMethodInfo, Object... params)
    {
        log(level, category, msg, withMethodInfo ? 0 : null, params);
    }
    
    public static void log (LogLevel level, String category, String msg, Integer withMethodInfoLevel, Object... params){
    	if (initialStatus == 0){
    		logSystemOut (level, category, msg, params);
    		return;
    	}

    	if (withMethodInfoLevel != null)
    		MDC.put("methodinfo", getMethodInfo(withMethodInfoLevel.intValue()));
    	else
    		MDC.remove("methodinfo");

    	//TODO 增加LogID
//    	MDC.put("logid", LogID.getCurrentLogID());

    	Logger logger = getLogger (category);
    	switch (level){
	    	case DEBUG: logger.debug(msg, params); break;
	    	case INFO:  logger.info (msg, params); break;
	    	case WARN:  logger.warn (msg, params); break;
	    	case ERROR: logger.error(msg, params); break;
    	}

    	// 若有正常初始化 error logger, 則記錄
    	if (defaultErrorLogger != null && level == LogLevel.ERROR){
    		defaultErrorLogger.error(msg, params);
    	}
    }
    
    
    /** BLog 成功初始化之前的記錄 */
    public static void logSystemOut (LogLevel level, String category, String msg, Object... params)
    {
        StringBuilder out_str = new StringBuilder ();
        out_str.append ((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(new Date ()));
        out_str.append (" [");
        out_str.append (category);
        out_str.append ("-");
        out_str.append (level);
        out_str.append ("]");
        //TODO 增加LogID
//        if (LogID.getCurrentLogID() != null)
//            out_str.append (LogID.getCurrentLogID());
        out_str.append ("(").append(getMethodInfo(0)).append (") ");
        out_str.append (msg).append("  ");
        
        for (int i = 0; i < params.length; ++ i){
            out_str.append ("[").append(i).append("]");
            if (params[i] instanceof Throwable)
                out_str.append(getDetailMsg((Throwable) params[i]));
            else
                out_str.append (params[i]);
            out_str.append(" ");
        }
        
        System.out.println (out_str.toString());
    }
    
    
    /**
     * 自設定檔中讀取 Log 相關設定. 
     * @param checkInitialized 是否要檢查已被初始化過 (可能發生在多個thread都在等初始化, 
     *         第一個做完後, 下一個再進 critial section 時其實 DomainLogger 已被初始化完成)
     * @return 是否成功，成功回傳<tt>true</tt>, 否則回傳<tt>false</tt>
     */
    public synchronized static void reload(String configureFilePath,Hashtable<String,String> properties){
    	// 若已被初始化則不再重覆進行
        if (isInitialized) return ;
        isInitialized = true;
        
        try {
			XLogConfiguration.configure(configureFilePath,properties) ;
			initialStatus = 2;
		} catch (Exception e) {
			XLog.error(DEFAULT_SYSLOG_CATEGORY, "Log 初始化: logback.xml 初始化失敗，無法取得 log 檔案位置，或檔案無法被讀取", e);
			try {
				XLogConfiguration.defaultConfigure();
				initialStatus = 1;
			} catch (Exception e1) {
				XLog.error(DEFAULT_SYSLOG_CATEGORY, "BLog 初始化: 預設 Log 初始化失敗", e);
			}
		}
        initialDefaultLogger();

    }
    
    /**
     * 取得指定 Log 類別之 Logger.
     * @param category
     * @return
     */
    private static Logger getLogger (String category){

        // 先檢查此類別是否已被暫存
        Logger logger = loggerMap.getData(category);
        if (logger != null)
            return logger;

        // 若初始化失敗或是設定檔沒有此類別則log通通寫到 DEFAULT_LOG_CATEGORY 去 (若是 console mode 則一律輸出，不做此一檢查)
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger lg = lc.getLogger(category);
        if(initialStatus > 1 && (lg == null || lg.getLevel() == null) && defaultLogger != null) 
        {
            logger = defaultLogger;
        }
        else
        {
            logger = LoggerFactory.getLogger(category);
        }
        
        // 完成後將 logger cache 至 loggerMap
        loggerMap.addData(category, logger);
        return logger;
    }
    
    /** 初始化 defaultLogger */
    private static void initialDefaultLogger(){
        // default logger
        try {
            defaultLogger = getLogger(DEFAULT_SYSLOG_CATEGORY);
        } catch (Exception e) {
            XLog.error(DEFAULT_SYSLOG_CATEGORY, "初始化 DEFAULT_LOGGER 失敗", e);
        }
        
        // error logger
        try {
            if (initialStatus >= 2) // 有透過檔案初始化
                defaultErrorLogger = getLogger(DEFAULT_ERRLOG_CATEGORY);

        } catch (Exception e) {
            XLog.error(DEFAULT_SYSLOG_CATEGORY, "初始化 DEFAULT_LOGGER 失敗", e);
        }
    }
    
    /**
     * 取得 Exception 簡短之描述字串 (只有 exception class name 及 message)
     * @return 回傳該exception之描述字串
     */
     public static String getShortMsg (Throwable e){
         return getExceptionDetailMessage(e, false, false);
     }
     
     /**
     * 取得加長版 Exception 簡短之描述字串 (只有 exception class name 及 message，及所有 cause 的 shortMsg)
     */
     public static String getAllShortMsg (Throwable e){
         return getExceptionDetailMessage(e, false, true);
     }
     
     /**
     * 取得 Exception 完整之描述字串 (回傳類似 Exception.printStackTrace () )，參考 getExceptionDetailMessage(Throwable,boolean)
     * @return 回傳該exception之描述字串
     */
     public static String getDetailMsg (Throwable e){
         return getExceptionDetailMessage(e, true, true);
     }
    
    
    /**
     * 取得 Exception 完整之描述字串 (回傳類似 Exception.printStackTrace ())
     * @param e Exception what is printed
     * @param detailedCallStack If a detailed call stack message is printed, or only Exception's class name and message (if any) will be printed.
     * @return 回傳該exception之描述字串
     */
    public static String getExceptionDetailMessage (Throwable e, boolean detailedCallStack, boolean withCause){
        StringBuffer ret = new StringBuffer("Exception: ");
        ret.append(e.getClass().getName());
        if (e.getMessage() != null)
            ret.append(": ").append(e.getMessage());

        if (detailedCallStack) {
            if (ret.charAt(ret.length() - 1) != '\n') ret.append("\n"); // only to next line on detailed call stack is outputed

            StackTraceElement[] em = e.getStackTrace();
            for (int i = 0; i < em.length; i++)
                ret.append("    at ").append(em[i].toString()).append("\n");
        }
        
        if (withCause && e.getCause() != null)
        {
            if (ret.charAt(ret.length() - 1) != '\n') ret.append("\n"); // only to next line on detailed call stack is outputed

            ret.append("Caused by \n").append(getExceptionDetailMessage(e.getCause(), detailedCallStack, withCause));
        }

        return ret.toString();
    }
    
    
    public static String toStringSafely (Object obj)
    {
        if (obj == null)
            return "null";
        else
            return obj.toString();
    }
    
    /**
     * 取得 Exception Cause (一併同時忽略其他錯誤)
     */
	@SuppressWarnings("unchecked")
	public static Throwable getCause(Throwable e, Class<? extends Throwable>... ignores)
    {
        Throwable ex = e;
        
        while(ex != null)
        {
            for(Class<? extends Throwable> cth : ignores)
            {
                if (cth.isInstance(ex))
                {
                    ex = ex.getCause();
                    continue;
                }
            }
            
            break;
        }
        
        if (ex == null)
            return e;

        return ex;
    }
    
    /**
     * 取得 Exception Root Cause (最後面的一個Exception)
     */
    public static Throwable getRootCause(Throwable e)
    {
        if (e == null)
            return e;
        
        Throwable ex = e;
        while (ex.getCause() != null)
            ex = ex.getCause();
        
        return ex;            
    }
    

    /**
     * 取得呼叫此 function 的 class 及 functionname 回傳呼叫者資訊. 
     * @param startFrom
     * @param caller
     * @return
     */
    private static String getMethodInfo (int startFrom){
    	
    	StackTraceElement[] em = new Throwable().getStackTrace();

    	// 針對 XLog 所做之客製化，取 className 時，加判斷一項取到 XLog 以外，避免 log 到無意義之檔名與行數
    	// ProtentialBUG: 效能?
    	int _startFrom = 2; // 第0層一定是 getMethodInfo(), 第1層一定是XLog.log, 故從2開始找
    	for (; _startFrom < em.length && em[_startFrom].getClassName().equals(XLog.class.getName()); _startFrom++)
    		;

    	if (_startFrom + startFrom < em.length)
    		_startFrom += startFrom;

    	// class 部份只取 className
    	String className = em[_startFrom].getClassName();
    	int dotPos = className.lastIndexOf(".");
    	if (dotPos == -1)
    		dotPos = 0;
    	else
    		dotPos++;

    	// format成 ClassSimpleName.someMethod:line
    	// ex: XLog.getMethodInfo:252
    	return className.substring(dotPos, className.length()) + "." + em[_startFrom].getMethodName() + ":" + em[_startFrom].getLineNumber() + "";


    }
    
    static{
    	reload("D:\\developenv\\workingspace\\test\\framework-business\\src\\main\\java\\META-INF\\logback.xml",null);
    }

}