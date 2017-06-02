package com.example.bartu.to_do_list_4;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Tworze obiekty
    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter ;//służy do „adaptowania” danych będących tablicami
    ListView lstTask;
    //Tworze ArrayListe przechowywującą obiekty typu String
    ArrayList<String> taskList;
    @Override
    //metoda wywoływana podczas startu programu typu protected
    protected void onCreate(Bundle savedInstanceState) {
        //Odwoluje sie do onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);//tworze obiekt
        lstTask = (ListView) findViewById(R.id.lstTask);//do łączenia się z nasyzmi widgetami
        //rejestruje wywolania zwrotnego która bedzie wywolana jak AdapterView bedzie nacisniete
        lstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usunzadanie(taskList.get(position));//usuwanie zadania
            }
        });
        loadTaskList();
    }
        //ponownie laduje liste zadan bo dokonaniu zmian
//metoda klasy MainActivity ladujaca liste zadan
    private void loadTaskList() {
        taskList = dbHelper.getTaskList(); //pobiera mi tablice z klasy DbHelper
        if (mAdapter==null)
        {
            mAdapter = new ArrayAdapter<String>(this,R.layout.row, R.id.task_title,taskList);//Tworzy wiersz z nazwa zadania
            lstTask.setAdapter(mAdapter);//Dodaje mi zadanie

        }
        else {
            mAdapter.clear();//usuwa wszystkie elementy z listy
            mAdapter.addAll(taskList);//Dodaje jeszcze raz taskList
            mAdapter.notifyDataSetChanged();//dostaje sygnał ze zmieniły się dane i trzeba je uaktualnić


        }
     }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //Tworze Menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())//pobiera mi Id obiektu
            {//dodawanie zadania ustawiam podpisy i okreslam dzialanie przycisku
            case R.id.action_add_task:
                    final EditText taskEditText= new EditText(this);
                    AlertDialog dialog = new AlertDialog.Builder(this)//tworzymy okno do dodania obiektu
                            .setTitle("Dodaj nowe zadanie:")
                            .setView(taskEditText)
                            .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {//dodaje nam zadanie
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String zadanie = String.valueOf(taskEditText.getText());
                                    dbHelper.dodajzadanie(zadanie);
                                    loadTaskList();
                                }
                            })

                            .setNegativeButton("Rezygnuj",null)//usuwa zadanie
                            .create();
                dialog.show();
                 return true ;
            case R.id.usun_wszystko:
                usunWszystko();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    //metoda ktora sluzy do usuwania zadania
    public void usunzadanie(final String zadanie)
    {   //okreslam czym zajmują się przyciski i tworze male okienko "dialog"
        //w ktorym decydujemy co chcemy zrobic
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Usuń", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.usunzadanie(zadanie);
                loadTaskList();//po wykonaniu akcji ponownie wczytuję listę
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
      dialog.setMessage("Czy chcesz usunąć to zadanie?");
        dialog.setTitle("Usuń zadanie");
        dialog.show();
    }
    //metoda słuzaca do usuwania wszystkich zadan
    public void usunWszystko()
    {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Usuń", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.usunWszystko();
                loadTaskList();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setMessage("Czy chcesz usunąć wszystkie zadania?");
        dialog.setTitle("Usuń wszystko");
        dialog.show();
    }
}
