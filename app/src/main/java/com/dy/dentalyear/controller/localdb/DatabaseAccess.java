package com.dy.dentalyear.controller.localdb;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dy.dentalyear.model.local.NotesModel;

import java.util.ArrayList;

public class DatabaseAccess {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static DatabaseAccess instance;
    Cursor cursor=null;
    private DatabaseAccess(Context context) {
        this.databaseHelper=new DatabaseHelper(context);

    }
    public static DatabaseAccess getInstance(Context context) {
        if (instance==null) {
            instance=new DatabaseAccess(context);
        }
        return instance;
    }
    public void open() {
        this.sqLiteDatabase=databaseHelper.getWritableDatabase();
    }
    public void close() {
        if (sqLiteDatabase!=null) {
            this.sqLiteDatabase.close();
        }
    }
    public boolean createNote(NotesModel notesModel) {
        try {
            String sql="INSERT INTO notes (title,desc,date,type) VALUES ('" + notesModel.getTitle()+"','"+notesModel.getDesc()+"','"+notesModel.getDate()+"',"+notesModel.getType()
                    + ")";
            Log.d("sqlCheck",sql);
            sqLiteDatabase.execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }
    public ArrayList<NotesModel> getAllNotes(int type) {
        String sql="SELECT * FROM notes WHERE type="+type;
        cursor = sqLiteDatabase.rawQuery(sql, new String[]{});
        ArrayList<NotesModel> data = new ArrayList<>();

        while (cursor.moveToNext()) {
            NotesModel notesModel = new NotesModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            // SkinsModel skinsModel=new SkinsModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));
            data.add(notesModel);
        }
        return data;
    }

    public boolean updateNote(NotesModel notesModel) {
        String sql = "UPDATE notes SET title='" + notesModel.getTitle() + "'," + "desc='" + notesModel.getDesc() + "'," + "date='" + notesModel.getDate() + "' WHERE id=" + notesModel.getId();
        try {
            Log.d("sqlCheck", sql);
            sqLiteDatabase.execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNote(int id) {
        String sql = "DELETE FROM notes WHERE id=" + id;
        try {
            Log.d("sqlCheck", sql);
            sqLiteDatabase.execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//    public Boolean makeFavourite(int id,int favourite) {
//        try {
//            String sql="UPDATE skins SET favorite="+favourite+" WHERE _id="+id;
//            Log.d("sql",sql);
//            sqLiteDatabase.execSQL(sql);
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    public ArrayList<SkinsModel> getAllFavourite() {
//        cursor=sqLiteDatabase.rawQuery("select * from skins where favorite=1",new String[]{});
//        ArrayList<SkinsModel> data=new ArrayList<>();
//
//        while (cursor.moveToNext()) {
//            SkinsModel skinsModel=new SkinsModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));
//            data.add(skinsModel);
//        }
//        return data;
//    }
}
