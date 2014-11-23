package com.ubicapet.rolando.ubicapet_1;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterPetActivity extends Activity {

    public static final String TAG = RegisterPetActivity.class.getSimpleName();

    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseObject> mPetRelation;
    protected EditText mName;
    protected EditText mRace;
    protected EditText mSex;
    protected Spinner spinner;
    protected EditText mColor;
    protected EditText mSize;
    protected EditText mCharacteristic;
    protected Button mSignUpButton;
    protected String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_register_pet);

        mCurrentUser = ParseUser.getCurrentUser();

        mName = (EditText)findViewById(R.id.nameField);
        mRace = (EditText)findViewById(R.id.edit_race);
        mSex = (EditText)findViewById(R.id.edit_sex);
        spinner = (Spinner) findViewById(R.id.spinner_select_sex);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sexo, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sex=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mColor = (EditText)findViewById(R.id.edit_color);
        mSize = (EditText)findViewById(R.id.edit_size);
        mCharacteristic = (EditText)findViewById(R.id.edit_characteristic);
        mSignUpButton = (Button)findViewById(R.id.button_register);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String race = mRace.getText().toString();
                String color = mColor.getText().toString();
                String size = mSize.getText().toString();
                String characteristic = mCharacteristic.getText().toString();


                name = name.trim();
                characteristic = characteristic.trim();
                race = race.trim();
                sex = sex.trim();
                color = color.trim();
                size = size.trim();

                if (name.isEmpty() || characteristic.isEmpty() || race.isEmpty() || sex.isEmpty() || color.isEmpty() || size.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPetActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // crear Mascota
                    setProgressBarIndeterminateVisibility(true);
                    ParseObject newPet = new ParseObject("Pets");
                    newPet.put("name", name);
                    newPet.put("Char", characteristic);
                    newPet.put("Race", race);
                    newPet.put("Sex", sex);
                    newPet.put("Color", color);
                    newPet.put("Size", size);
                    newPet.put("parent", mCurrentUser);
                    newPet.put("Estado","En casa" );

                    newPet.saveInBackground();



                    setProgressBarIndeterminateVisibility(false);


                    Intent intent = new Intent(RegisterPetActivity.this, MyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }


            }
        });



    }
    public void onResume (){
        super.onResume();



    }

}
