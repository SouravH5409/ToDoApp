package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.ModelTodo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "todoDatabase";
    private static final String TODO_TABLE = " todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = String.format("CREATE TABLE %s(%sINTEGER PRIMARY KEY AUTOINCREMENT, %sTEXT ,%sINTEGER)", TODO_TABLE, ID, TASK, STATUS);
    private SQLiteDatabase db;
    public DatabaseHandler(Context context){
        super(context,NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE todo (id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, status INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

        db.execSQL("DROP TABLE IF EXISTS'"+ TODO_TABLE +"'");
        onCreate(db);


    }
    public void openDatabase(){
        db = this.getWritableDatabase();
    }
    public void insertTask(ModelTodo task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS,0);
        db.insert(TODO_TABLE,null,cv);
    }
    @SuppressLint({"Recycle", "Range"})
    public List<ModelTodo> getAllTask(){
        List<ModelTodo> taskList = new ArrayList<>();
        Cursor cur = null;
        //In case we are moving out of app while a Transaction is being performed
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ModelTodo task = new ModelTodo();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    } while (cur.moveToNext());
                }
            }
        }finally{
                db.endTransaction();
                cur.close();
            }
            return taskList;
        }
    public void updateTask(int id,int task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv,ID+"+?",new String[]{String.valueOf(id)});
    }
    @SuppressLint("Range")
    public List<ModelTodo> getAllTasks(){
        List<ModelTodo> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ModelTodo task = new ModelTodo();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
