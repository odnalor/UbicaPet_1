package com.ubicapet.rolando.ubicapet_1;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by rolando on 04/09/2014.
 */
public class AdsFragment extends ListFragment{
    public static final String TAG = PetFragment.class.getSimpleName();
    public Button mCreate;
    public ParseGeoPoint mLocation;
    protected ParseUser mCurrentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ads, container, false);

        mCreate= (Button)rootView.findViewById(R.id.button_create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), CreateAds.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void onResume() {
        super.onResume();
        getActivity().setProgressBarIndeterminateVisibility(true);
        mCurrentUser = ParseUser.getCurrentUser();

        //Lista de mis mascotas.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Advertise");
        query.whereEqualTo("parent", mCurrentUser);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> mAds, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (e == null ){
                    String[] Advertices = new String[mAds.size()];
                    int i=0;
                    for (ParseObject Ads : mAds){
                        Advertices[i] = Ads.getString("Titulo");
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1, Advertices);
                    setListAdapter(adapter);

                    }

                else{

                    Log.e(TAG, e.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            }
        });



    }

}