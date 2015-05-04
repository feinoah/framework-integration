package com.chttl.jee.log;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

public class XLogConfiguration {

	public static void configure(String configureFilePath,Hashtable<String,String> properties) throws Exception{
		
		File configFile = new File(configureFilePath) ;
		
		System.out.println(configFile.getPath());
		System.out.println(configFile.isFile());
		System.out.println(configFile.canRead());
		
		if (! (configFile != null && configFile.isFile() && configFile.canRead())){
            //TODO BLog.error(LogCategory.SYSTEM, "無法自組態設定目錄中取得 log 設定: {}", foLogConfigFull);
            throw new Exception("無法自組態設定目錄中取得 log 設定");
        }
		
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();

        
        if(properties!=null){
        	Iterator<Entry<String,String>> itr = properties.entrySet().iterator();
        	while(itr.hasNext()){
        		Entry<String,String> e = itr.next() ;
        		lc.putProperty(e.getKey(), e.getValue()); //TODO put deployname
        	}
        }
        
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        configurator.doConfigure(configFile);
		
	}
	
	public static void defaultConfigure()throws Exception{
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();
        
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<ILoggingEvent>();
        consoleAppender.setContext(lc);
        consoleAppender.setName("ConsoleAppendar");

        PatternLayoutEncoder outPattern = new PatternLayoutEncoder ();
        outPattern.setContext(lc);
        // 2014-03-03 以前的原本格式為:
        // outPattern.setPattern("%d{HH:mm:ss.SSS} [%-6logger{10}-%-5level] %-80msg %X{methodinfo}  %X{logid}\n");
        outPattern.setPattern("%d{HH:mm:ss} %-5level[%-6logger{10}] %-40msg  %X{methodinfo}  %X{logid}\n");
        outPattern.start();

        consoleAppender.setEncoder(outPattern);
        consoleAppender.start();
        
        // hc Log setting
        ch.qos.logback.classic.Logger l1 = lc.getLogger("net.sf.ehcache");
        l1.setLevel(Level.ERROR);
        ch.qos.logback.classic.Logger l2 = lc.getLogger("org.apache.myfaces");
        l2.setLevel(Level.ERROR);
        
        ch.qos.logback.classic.Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);
        rootLogger.addAppender(consoleAppender);
		
	}
	
	/** 修改指定 Log categories 的 level */
    public static void setLoggerLevel (String category, LogLevel newLevel){
        LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(category);
        logger.setLevel(toLogbackLevel(newLevel));
    }

    /** log level 互換: logutil.loglevel to logback log level */
    private static Level toLogbackLevel (LogLevel logutilLevel){
        switch (logutilLevel){
        	case ERROR: return Level.ERROR;
        	case WARN: return Level.WARN;
        	case INFO: return Level.INFO;
        	case DEBUG:
        	default: return Level.DEBUG;
        }
    }
    
    /** log level 互換: logback log level to logutil.loglevel  */
    @SuppressWarnings("unused")
	private static LogLevel toLogLevel (Level logbackLevel){
        if (logbackLevel == null)
            return LogLevel.DEBUG;
        
        switch (logbackLevel.toInt()){
        	case Level.ERROR_INT: return LogLevel.ERROR;
        	case Level.WARN_INT: return LogLevel.WARN;
        	case Level.INFO_INT: return LogLevel.INFO;
        	case Level.DEBUG_INT: 
        	default: return LogLevel.DEBUG;
        }
    }
	
}