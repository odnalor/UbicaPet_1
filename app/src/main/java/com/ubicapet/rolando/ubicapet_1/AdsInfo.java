package com.ubicapet.rolando.ubicapet_1;

import android.app.Activity;
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
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class AdsInfo extends ListActivity{
    protected ParseUser mCurrentUser;
    protected String mAds;
    protected Button mDeleteButton;
    protected String []AdsInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_ads_info);

        mCurrentUser = ParseUser.getCurrentUser();
        Intent intent= getIntent();
        mAds = intent.getStringExtra("Titulo");


        setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Advertise");
        query.whereEqualTo("parent", mCurrentUser);
        query.whereEqualTo("Titulo", mAds);

        query.getFirstInBackground(new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject parseObject, ParseException e) {
                setProgressBarIndeterminateVisibility(false);
                if (e == null ){

                    AdsInfo = new String[3];

                    AdsInfo[0] = parseObject.getString("PetName");
                    AdsInfo[1] = parseObject.getString("advertice");
                    AdsInfo[2] = parseObject.getString("type");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1, AdsInfo);
                    setListAdapter(adapter);


                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdsInfo.this);
                    builder.setMessage(getString(R.string.consulta_vacia))
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });


        // borrar mascota
        mDeleteButton = (Button)findViewById(R.id.button_Eliminar);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery query = new ParseQuery("Advertise");
                query.whereEqualTo("parent", mCurrentUser);
                query.whereEqualTo("Titulo", mAds);
                query.getFirstInBackground(new GetCallback() {
                    @Override
                    public void done(ParseObject Ads, ParseException e) {
                        Ads.deleteInBackground();
                    }
                });

                Intent intent = new Intent(AdsInfo.this, MyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ads_info, menu);
        return true;






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   }
