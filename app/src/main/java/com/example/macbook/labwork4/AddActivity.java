package com.example.macbook.labwork4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by macbook on 22.04.16.
 */
public class AddActivity extends Activity {
    Button vCancelButton, vAddButton;
    EditText vName, vSurname, vAge, vGrade;
    private long personID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        vCancelButton=(Button)findViewById(R.id.button_cancel);
        vAddButton=(Button)findViewById(R.id.button_add);
        vName=(EditText)findViewById(R.id.text_name);
        vSurname=(EditText)findViewById(R.id.text_surname);
        vAge=(EditText)findViewById(R.id.text_age);
        vGrade=(EditText)findViewById(R.id.text_grade);


        if(getIntent().hasExtra("PersonData")){
            Person curPerson = (Person)getIntent().getExtras().getSerializable("PersonData");
            vName.setText(curPerson.getName());
            vSurname.setText(curPerson.getSurname());
            vAge.setText(Integer.toString(curPerson.getAge()));
            vGrade.setText(Integer.toString(curPerson.getGrade()));
            personID=curPerson.getId();
        }
        else {
            personID=-1;
        }

        vCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!corrector())
                    return;
                Person person = new Person(vName.getText().toString(),
                        vSurname.getText().toString(),
                        Integer.parseInt(vAge.getText().toString()),
                        Integer.parseInt(vGrade.getText().toString()));
                if(personID!=-1)
                    person.setId(personID);

                Intent intent = getIntent();
                intent.putExtra("PersonData", person);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
    Boolean corrector(){
        if(vName.getText().toString()==null){
            Toast.makeText(getApplicationContext(),"Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(vName.getText().toString()==null) {
            Toast.makeText(getApplicationContext(), "Surname is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        try{
            Integer.parseInt(vAge.getText().toString());
        }
        catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(),"Put down correct age", Toast.LENGTH_SHORT).show();
            return false;
        }
        try{
            Integer.parseInt(vGrade.getText().toString());
        }
        catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(),"Put down correct Grade", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
