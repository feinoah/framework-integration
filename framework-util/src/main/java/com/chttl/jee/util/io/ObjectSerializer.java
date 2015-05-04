package com.chttl.jee.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class ObjectSerializer {
	
	/**
     * 將物件陣列轉換為byte array.
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serializeToByteArray (Object[] obj)throws IOException{
        return serializeToByteArray((Object) obj);
    }
    
    /**
     * 將byte array回轉成為物件陣列. 
     * 
     * @param bytes
     * @return 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object[] deserializeToObjectArray (byte[] bytes)throws IOException, ClassNotFoundException{
        return (Object[]) deserializeToObject(bytes);
    }
    
    /**
     * 將物件陣列轉換為byte array.
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] serializeToByteArray (Object obj) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null)
                out.close();
            if (bos != null)
                bos.close();
        }
    }
	
    
    /**
     * 將byte array回轉成為物件陣列. 
     * 
     * @param bytes
     * @return 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserializeToObject (byte[] bytes)
        throws IOException, ClassNotFoundException
    {
        ObjectInputStream is = null;
        ObjectInput in = null;
        try {
            is = new ContextLoaderObjectInputStream(new ByteArrayInputStream(bytes));
            Object o = is.readObject();
            return o;
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        } finally {
            try { if (is != null) is.close(); } catch (IOException e) {}
            try { if (in != null) in.close(); } catch (IOException e) {}
        }
    }
    
    
    /**
     * 替代版 clone. 
     * 
     * <p>請注意：利用 Serialization 複製物件之效能與單純 clone 可能會差到千倍以上，請謹慎使用
     * 
     * @param obj 欲複製之物件
     * @return obj之複製完成之物件; 若複製過程發生任何問題則回傳 <tt>null</tt>
     */
    @SuppressWarnings("unchecked")
	public static  <T> T cloneBySerialize (T obj)
    {
        ObjectInputStream is = null;
        
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream (); 
            ObjectOutputStream oos = new ObjectOutputStream (bs);
            oos.writeObject(obj);
            oos.flush();
            oos.close ();
            
            is = new ContextLoaderObjectInputStream(new ByteArrayInputStream (bs.toByteArray()));
            return (T) is.readObject();

        } catch (Exception e) {
            // NOTE: 原本會將 exception 吞掉並 return null 有潛在發生問題卻被忽略的風險, 
            // 在不改變 function signature 的前提下, 改成由 RuntimeException 包裹丟出
            throw new RuntimeException("cloneBySerialize()", e);
        } finally {
            if (is != null) { try { is.close(); } catch (Exception e) { ignoreException(e); } }
        }
    }
    
    /** 壓縮 byte[] */
    public static byte[] compress (byte[] data) throws IOException{
        Deflater deflater = new Deflater();
        deflater.setLevel(9);
        deflater.setInput(data);
        deflater.finish();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        return output;
    }
    
    
    /** 解壓縮 byte[] */
    public static byte[] decompress (byte[] data)throws IOException, DataFormatException{
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            if (count == 0 && inflater.needsInput())
                throw new IOException("Data unexpected ended");

            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        return output;
    }
    
    /** 將物件編碼成 base64 字串 */
    public static String serializeToBase64 (Object obj, boolean withCompression)
        throws IOException
    {
        byte[] arr = serializeToByteArray(obj);
        
        byte[] arr2 = arr;
        if (withCompression)
            arr2 = compress(arr);
        
        String base64 = new String(Base64.getEncoder().encode(arr2));
        return base64;
    }
    
    /** 將 base64 字串轉換回物件 
     * @throws DataFormatException 
     * @throws ClassNotFoundException */
    public static Object deserializeFromBase64 (String base64ObjectString, boolean withCompression)
        throws IOException, DataFormatException, ClassNotFoundException
    {
        if (base64ObjectString == null || base64ObjectString.length() == 0)
            return null;
        
        
        byte[] decodeArr = Base64.getDecoder().decode(base64ObjectString.getBytes());

        byte[] decodeArr2 = decodeArr;
        if (withCompression)
            decodeArr2 = decompress(decodeArr);

        Object out = deserializeToObject(decodeArr2);
        
        return out;
    }

    private static void ignoreException(Exception e){}
    
    private static class ContextLoaderObjectInputStream extends ObjectInputStream{
    	@Override
    	public Class<?> resolveClass (ObjectStreamClass desc)
    			throws IOException,
    			ClassNotFoundException
    			{
    		ClassLoader currentTccl = null;
    		try {
    			currentTccl = Thread.currentThread().getContextClassLoader();
    			return currentTccl.loadClass(desc.getName());
    		} catch (Exception e) {}

    		return super.resolveClass(desc);
    			}

    	public ContextLoaderObjectInputStream (InputStream in) throws IOException
    	{
    		super(in);
    	}
    }
    
}