package com.example.macbook.labwork4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by macbook on 22.04.16.
 */
public class DBPerson {
    private static final String DATABASE_NAME = "person.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tablePersons";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GRADE = "grade";

    private static final int NUM_ID=0;
    private static final int NUM_NAME=1;
    private static final int NUM_SURNAME=2;
    private static final int NUM_AGE=3;
    private static final int NUM_GRADE=4;

    private SQLiteDatabase mDataBase;

    public DBPerson(Context context){
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }
    private ContentValues encapsulateCV(Person person){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, person.getName());
        cv.put(COLUMN_SURNAME, person.getSurname());
        cv.put(COLUMN_AGE, person.getAge());
        cv.put(COLUMN_GRADE, person.getGrade());
        return cv;
    }

    public long insert(Person person){
        return mDataBase.insert(TABLE_NAME, null, encapsulateCV(person));
    }

    public int update(Person person){
        return mDataBase.update(TABLE_NAME, encapsulateCV(person), COLUMN_ID + " = ?",
                new String[]{String.valueOf(person.getId())});
    }

    public void deleteAll(){
        mDataBase.delete(TABLE_NAME, null,null);
    }

    public void delete(long id){
        mDataBase.delete(TABLE_NAME,COLUMN_ID + " = ?",
                new String[]{ String.valueOf(id)});
    }

    private Person collectData(Cursor cursor, long id){
        String name = cursor.getString(NUM_NAME);
        String surname = cursor.getString(NUM_SURNAME);
        int age = cursor.getInt(NUM_AGE);
        int grade = cursor.getInt(NUM_GRADE);
        Person result = new Person(name,surname,age, grade);
        result.setId(id);
        return result;
    }

    public Person select(long id){
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?",
                new String[]{ String.valueOf(id)}, null, null, null);
        mCursor.moveToFirst();
        return collectData(mCursor, id);
    }

    public ArrayList<Person> selectAll(){
        Cursor mCursor = mDataBase.query(TABLE_NAME, null,null,null,null,null,null);
        ArrayList<Person> result = new ArrayList<>();
        mCursor.moveToFirst();
        if(!mCursor.isAfterLast()){
            do{
                long id = mCursor.getLong(NUM_ID);
                result.add(collectData(mCursor, id));
            }
            while (mCursor.moveToNext());
        }
        return result;
    }

    private class OpenHelper extends SQLiteOpenHelper{
        public OpenHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " ("+
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_NAME + " TEXT, "+
                    COLUMN_SURNAME + " TEXT, "+
                    COLUMN_AGE + " INT, "+
                    COLUMN_GRADE+ " INT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
