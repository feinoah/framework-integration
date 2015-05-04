package com.chttl.jee.dao.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.chttl.jee.util.io.ObjectSerializer;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;


public class DaoUtil {

	public String hexToString(String strHex,String encoding){
        int tmpHexLen = strHex.length();
        int cursorPos = 0;
        byte [] strBytes = new byte[ (int) (tmpHexLen/2)];
        int i = 0;
        while(cursorPos < tmpHexLen){
            int val = Integer.parseInt(strHex.substring(cursorPos,cursorPos+2),16);
            strBytes[i] = (byte) (val & 0xff );
            cursorPos += 2;
            i++;
        }
        try {
            return new String(strBytes,encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(strBytes);
        }
    }

	/**
	 * 對 String 欄位編碼
	 * @param to
	 * @return
	 */
	public <T> T encodeToDBString(T to){
		if(to==null) return to ;
		
        // 2014-10-13 [cory] 針對array type 處理
        if (to.getClass().isArray()){
            Object[] toarray = (Object[]) to;
            for (int i = 0; i < toarray.length; ++ i){
                if (toarray[i] instanceof String)
                    toarray[i] = encodeToDBString((String) toarray[i]);
            }
            return to;
        }

        // 一般物件的處理
		try{
			Field[] fields = to.getClass().getDeclaredFields();
			for(Field f : fields){
				if(f.getType() == String.class){
					String first = f.getName().substring(0,1).toUpperCase() ;
					String other = f.getName().substring(1);
					Method getter = to.getClass().getMethod("get"+first+other, (Class[]) null) ;
					Method setter = to.getClass().getMethod("set"+first+other, String.class) ;
					setter.invoke(to, encodeToDBString((String)getter.invoke(to, (Object[]) null)));
				}
			}
		}catch(Exception e){
			return to ;
		}
		
		return to ;
	}
	
	/**
	 * 對 String 欄位編碼
	 * 
	 * @param to
	 * @return
	 */
	public <T> T encodeToAPString(T to){
		if(to==null) return to ;
		
		// 2014-10-13 [cory] 針對array type 處理
        if (to.getClass().isArray()){
            Object[] toarray = (Object[]) to;
            for (int i = 0; i < toarray.length; ++ i){
                if (toarray[i] instanceof String)
                    toarray[i] = encodeToAPString((String) toarray[i]);
            }
            return to;
        }

        // 一般物件的處理
		try{
			Field[] fields = to.getClass().getDeclaredFields();
			for(Field f : fields){
			    if (Modifier.isStatic(f.getModifiers()))
			        continue;

				if(f.getType() == String.class){
					String first = f.getName().substring(0,1).toUpperCase() ;
					String other = f.getName().substring(1);
					Method getter = to.getClass().getMethod("get"+first+other, (Class[]) null) ;
					Method setter = to.getClass().getMethod("set"+first+other, String.class) ;
					setter.invoke(to, encodeToAPString((String)getter.invoke(to, (Object[])null)));
				}
			}
		}catch(Exception e){
			return to ;
		}
		
		return to ;
	}
	
	public String encodeToAPString(String str){
	    if(str == null){
	        return null;
	    }else{
	        return str.trim() ;
	    }
	}
	
	public String encodeToDBString (String str){
	    return str ;
    }


    public <T> List<T> resultList(List<T> list)
    {
        List<T> tlist = 
            FluentIterable
                .from(list)
                .transform(new Function<T, T>() {
                    @Override
                    public T apply (T input)
                    {
                        return encodeToAPString(ObjectSerializer.cloneBySerialize(input));
                    }
                })
                .toList();
        return new ArrayList<T>(tlist);
    }
    
    public <T> T singleResult(List<T> objectList)
    {
        if (objectList != null && objectList.size() > 0)
            return encodeToAPString(ObjectSerializer.cloneBySerialize(objectList.get(0)));
        return null;
    }
    
    public <T> T result(T object)
    {
        return encodeToAPString(ObjectSerializer.cloneBySerialize(object));
    }
    
    public String transConditionWithIn(String data){
        String datas [] = data.split(",");
        for (int i = 0; i < datas.length; i++) {
            datas[i] = "'" + datas[i] + "'";
        } 
        Joiner joiner = Joiner.on(",");
        return joiner.join(datas);
    }
	
}