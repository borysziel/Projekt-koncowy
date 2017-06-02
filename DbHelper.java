package com.example.bartu.to_do_list_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static android.R.attr.version;

/**
 * Created by Bartuś on 27.05.2017.
 */
//klasa dziedzicząca z SQLiteOpenHelper
public class DbHelper extends SQLiteOpenHelper {
    //deklarowanie zmiennych typu string , prywatnych i publicznych typu final
    private static final String DB_NAME="nazwa";
    private static final int DB_VER=1;
    public static final String DB_TABLE="zadanie";
    public static final String DB_COLUMN="nazwa_zadania";


   public DbHelper(Context context)
   {
       super(context,DB_NAME,null,DB_VER);
   }
    @Override
    //przesłonięta metoda inicjująca nasze activity , rozpoczyna nam cykl
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s(ID INTEGER PRIMARY KEY AUTOINCREMENT ,%s TEXT NOT NULL);",DB_TABLE,DB_COLUMN);
        //autoinkrementacja
        db.execSQL(query);//tworzy tabele w bazie sqlite
    }

    @Override
    //przeslonieta metoda aktualizująca database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query =String.format("DELETE TABLE IF EXISTS %s; ",DB_TABLE);
        db.execSQL(query);
        onCreate(db);//inicjacja aktywnosci

    }
//metoda dodaj zadanie z parametrem typu String zadanie
    public void dodajzadanie (String zadanie)
    {
        SQLiteDatabase db = this.getWritableDatabase();//Tworze database umozliwiajace wpisywanie zadan
        ContentValues values =new ContentValues();//Tworze pusty zestaw wartosci o domyslnym rozmiarze
        values.put(DB_COLUMN,zadanie);//dodaje zadanie do wczesniej stworzonego obiektu
        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        //Wstawia wiersz do bazy danych ktory jest priorytetowy
        db.close();//konczy ten cykl a tym samym łączenie z serwerem
    }
    //metoda usun zadanie z parametrem typu String zadanie
    public void usunzadanie(String zadanie)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN + "= ?" , new String[]{zadanie});//usuniecie zadania
        db.close();
    }
    //metoda usunWszystko
    public void usunWszystko()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DB_TABLE);//usuniecie wszystkich wierszy
        db.close();
    }
    //Tworze getter ArrayListy
    public ArrayList<String> getTaskList()
    {
        ArrayList<String> taskList = new ArrayList<>(); //implementacja tablicy , stworzenie obiektu
        SQLiteDatabase db = this.getReadableDatabase();//Odczytuje utworzoną tablicę
        Cursor cursor = db.query(DB_TABLE,new String[]{DB_COLUMN},null,null,null,null,null);
        while (cursor.moveToNext()) //Przesuwam kursor do następnego wiersza
        {
            int index = cursor.getColumnIndex(DB_COLUMN);
            // Tworze indexy danych kolumn/wierszy i je zapisuje
            taskList.add(cursor.getString(index));
            //Tworze tablice po nazwie kursora
        }
        cursor.close();//zamykam cursor
        db.close();//zamykam ten cykl
        return taskList;//wyrzucam na ekran Liste zadan
    }

}
