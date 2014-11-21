package com.ubicapet.rolando.ubicapet_1;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class PetInfo extends ListActivity {
    public static final String TAG = PetInfo.class.getSimpleName();
    protected ParseUser mCurrentUser;
    protected String mName;
    protected Button mDeleteButton;
    protected String[] PetsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_pet_info);

        mCurrentUser =ParseUser.getCurrentUser();

        Intent intent = getIntent();
        mName= intent.getStringExtra("Pet");

        setProgressBarIndeterminateVisibility(true);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Pets");
        query.whereEqualTo("parent", mCurrentUser);
        query.whereEqualTo("name", mName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null ){

                    PetsNames = new String[3];

                    PetsNames[0] = parseObject.getString("name");
                    PetsNames[1] = parseObject.getString("Race");
                    PetsNames[2] = parseObject.getString("Char");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1, PetsNames);
                    setListAdapter(adapter);


                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PetInfo.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

// borrar mascota
        mDeleteButton = (Button)findViewById(R.id.button_delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery query = new ParseQuery("Pets");
                query.whereEqualTo("parent", mCurrentUser);
                query.whereEqualTo("name", mName);
                query.getFirstInBackground(new GetCallback() {
                    @Override
                    public void done(ParseObject pet, ParseException e) {
                        pet.deleteInBackground();
                    }
                });

                Intent intent = new Intent(PetInfo.this, MyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        });


    }

    @Override
    public void onResume(){
        super.onResume();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pet_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
