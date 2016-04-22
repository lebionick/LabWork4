package com.example.macbook.labwork4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView vListView;
    DBPerson mDBPerson;
    Context mContext;
    PersonListViewAdapter mPersonAdapter;

    private final int ADD_ACTIVITY = 0;
    private final int EDIT_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;
        mDBPerson = new DBPerson(this);

        vListView = (ListView)findViewById(R.id.listView);
        mPersonAdapter = new PersonListViewAdapter(mContext,mDBPerson.selectAll());
        vListView.setAdapter(mPersonAdapter);
        registerForContextMenu(vListView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_ACTIVITY);
                updateList();
            }
        });
    }

    private void updateList(){
        mPersonAdapter.setArrayMyData(mDBPerson.selectAll());
        mPersonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.action_delete:
                mDBPerson.delete(info.id);
                updateList();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(mContext,AddActivity.class);
                Person person = mDBPerson.select(info.id);
                intent.putExtra("PersonData", person);
                startActivityForResult(intent, EDIT_ACTIVITY);
                updateList();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case (R.id.action_settings):
                return true;
            case (R.id.action_delete_all):
                mDBPerson.deleteAll();
                updateList();
                return true;
            case (R.id.action_quit):
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Person person = (Person) data.getExtras().getSerializable("PersonData");
            if(requestCode == EDIT_ACTIVITY){
                mDBPerson.update(person);
            }
            if(requestCode == ADD_ACTIVITY){
                mDBPerson.insert(person);
            }
            updateList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class PersonListViewAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private ArrayList<Person> mArrayList;

        public PersonListViewAdapter(Context context, ArrayList<Person> arrayList) {
            mInflater=LayoutInflater.from(context);
            setArrayMyData(arrayList);
        }


        public void setArrayMyData(ArrayList<Person> arrayList){
            mArrayList=arrayList;
        }

        public ArrayList<Person> getArrayList(){
            return mArrayList;
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            Person person = mArrayList.get(position);
            if(person!=null){
                return person.getId();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
                convertView = mInflater.inflate(R.layout.item_in_list,null);
            TextView vName = (TextView)convertView.findViewById(R.id.item_name);
            TextView vSurname = (TextView)convertView.findViewById(R.id.item_surname);
            TextView vAge = (TextView)convertView.findViewById(R.id.item_age);
            TextView vGrade = (TextView)convertView.findViewById(R.id.item_grade);

            Person person = mArrayList.get(position);

                vName.setText(person.getName());
                vSurname.setText(person.getSurname());
                vAge.setText(person.getAge().toString());
                vGrade.setText(person.getGrade().toString());

            return convertView;
        }
    }

}
