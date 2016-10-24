package com.truthso.ip360.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.db.MySQLiteOpenHelper;
import com.truthso.ip360.updownload.UpLoadInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UpDownLoadDao {

	private MySQLiteOpenHelper dbOpenHelper;  
    private static UpDownLoadDao dao =new UpDownLoadDao(MyApplication.getApplication());
    private UpDownLoadDao(Context context){  
        this.dbOpenHelper = new MySQLiteOpenHelper(context);  
    }      
    public static UpDownLoadDao getDao(){
    	
    	return dao;
    }
    
    public void save(String sourceid, File uploadFile){  
    	  SQLiteDatabase db = dbOpenHelper.getWritableDatabase();  
        db.execSQL("insert into updownloadlog(uploadfilepath, sourceid) values(?,?)",  
                new Object[]{uploadFile.getAbsolutePath(),sourceid});  
    }  
    
    public void save(int sourceid, String uploadFilePath){  
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();  
        db.execSQL("insert into updownloadlog(uploadfilepath, sourceid) values(?,?)",  
                new Object[]{uploadFilePath,sourceid});  
    }  
        
    
    public void delete(File uploadFile){  
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();  
        db.execSQL("delete from updownloadlog where uploadfilepath=?", new Object[]{uploadFile.getAbsolutePath()});  
    }  
      
    public String getBindId(File uploadFile){  
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
        Cursor cursor = db.rawQuery("select sourceid from updownloadlog where uploadfilepath=?",   
                new String[]{uploadFile.getAbsolutePath()});  
        if(cursor.moveToFirst()){  
            return cursor.getString(0);  
        }  
        return null;  
    }  

    public String getPositionByUrl(String url){
    	  SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
    	  Cursor cursor = db.rawQuery("select position from updownloadlog where downloadurl=?",   
                  new String[]{url});  
          if(cursor.moveToFirst()){  
              return cursor.getString(0);  
          }  

          return null;  
    }
    
    public void saveDownLoadUrl(String url){
  	  SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
  	  db.execSQL("insert into updownloadlog(downloadurl) values(?)",  
            new Object[]{url});  
    }
    
    
   public void updateProgress(String url,int position){
	   SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
	  	  db.execSQL("update updownloadlog position=? where downloadurl =?",  
	            new Object[]{url,position});  
   }
   
   
   public List<DownLoadInfo> queryDownLoadList(){
	   SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
	   Cursor cursor= db.rawQuery("select * from updownloadlog where downorupload=?",  
	            new String[]{"0"}); 
	  List<DownLoadInfo> list=new ArrayList<DownLoadInfo>();
	   while(cursor.moveToFirst()){ 
		   DownLoadInfo info=new DownLoadInfo();
		   info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
		   info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		   info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
		   info.setDownLoadUrl(cursor.getString(cursor.getColumnIndex("downloadurl")));
		   info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
		   list.add(info);
           return list;  
       }  
	   return null;
   }
   
   
   public List<UpLoadInfo> queryUpLoadList(){
	   SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
	   Cursor cursor= db.rawQuery("select * from updownloadlog where downorupload=?",  
	            new String[]{"1"}); 
	  List<UpLoadInfo> list=new ArrayList<UpLoadInfo>();
	   while(cursor.moveToFirst()){ 
		   UpLoadInfo info=new UpLoadInfo();
		   info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
		   info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		   info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
		   info.setFilePath(cursor.getString(cursor.getColumnIndex("uploadfilepath")));
		   info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
		   list.add(info);
           return list;  
       }  
	   return null;
   }
   
   
   public DownLoadInfo queryDownLoadInfoByUrl(String url){
	   SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
	   Cursor cursor= db.rawQuery("select * from updownloadlog where downloadurl=?",  
	            new String[]{url}); 
	 
	   while(cursor.moveToFirst()){ 
		   DownLoadInfo info=new DownLoadInfo();
		   info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
		   info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		   info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
		   info.setDownLoadUrl(cursor.getString(cursor.getColumnIndex("downloadurl")));
		   info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
		  
           return info;  
       }  
	   return null;
   }
   
   public UpLoadInfo queryUpLoadInfoByUrl(String filePath){
	   SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
	   Cursor cursor= db.rawQuery("select * from updownloadlog where uploadfilepath=?",  
	            new String[]{filePath}); 
	 
	   while(cursor.moveToFirst()){ 
		   UpLoadInfo info=new UpLoadInfo();
		   info.setResourceId(cursor.getInt(cursor.getColumnIndex("sourceid")));
		   info.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
		   info.setFileSize(cursor.getString(cursor.getColumnIndex("filesize")));
		   info.setFilePath(cursor.getString(cursor.getColumnIndex("uploadfilepath")));
		   info.setPosition(cursor.getInt(cursor.getColumnIndex("position")));
		  
           return info;  
       }  
	   return null;
   }
   
   
   public void deleteByUrl(String url){
	   SQLiteDatabase db = dbOpenHelper.getWritableDatabase();  
       db.execSQL("delete from updownloadlog where downloadurl=?", new Object[]{url});  
   }
}
