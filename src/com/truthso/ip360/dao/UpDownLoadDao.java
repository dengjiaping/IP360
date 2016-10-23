package com.truthso.ip360.dao;

import java.io.File;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.db.MySQLiteOpenHelper;

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
    	  Cursor cursor = db.rawQuery("select position from updownloadlog where downloadfilepath=?",   
                  new String[]{url});  
          if(cursor.moveToFirst()){  
              return cursor.getString(0);  
          }  

          return null;  
    }
    
    public void saveDownLoadUrl(String url){
  	  SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
  	  db.execSQL("insert into updownloadlog(downloadfilepath) values(?)",  
            new Object[]{url});  
    }
   public void updateProgress(String url,int position){
	   SQLiteDatabase db = dbOpenHelper.getReadableDatabase();  
	  	  db.execSQL("update updownloadlog position=? where downloadfilepath =?",  
	            new Object[]{url,position});  
   }
}
