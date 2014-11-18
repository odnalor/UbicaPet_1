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
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by rolando on 03/09/2014.
 */
public class PetFragment extends ListFragment {

    public static final String TAG = PetFragment.class.getSimpleName();
    protected ParseUser mCurrentUser;
    protected Button mAddPet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet, container, false);
        mCurrentUser = ParseUser.getCurrentUser();

        mAddPet = (Button) rootView.findViewById(R.id.button_edit_pet);
        mAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterPetActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();


            getActivity().setProgressBarIndeterminateVisibility(true);


            //Lista de mis mascotas.
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Pets");
            query.whereEqualTo("parent", mCurrentUser);
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> mPet, ParseException e) {
                    getActivity().setProgressBarIndeterminateVisibility(false);

                    if (e == null) {
                        String[] PetsNames = new String[mPet.size()];
                        int i = 0;
                        for (ParseObject pet : mPet) {
                            PetsNames[i] = pet.getString("name");
                            i++;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getListView().getContext(),
                                android.R.layout.simple_list_item_1, PetsNames);
                        setListAdapter(adapter);


                    } else {

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



        @Override
        public void onListItemClick (ListView l, View v,int position, long id){
            super.onListItemClick(l, v, position, id);


            String selct = l.getItemAtPosition(position).toString();

            Intent intent = new Intent(getActivity(), PetInfo.class);
            intent.putExtra("Pet", selct);
            startActivity(intent);


        }


    }

