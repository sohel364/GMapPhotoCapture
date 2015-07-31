package com.example.user.googlemaptest.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.Utils;
import com.example.user.googlemaptest.fragments.containers.FragmentContainerBase;
import com.example.user.googlemaptest.model.AddressBase;

import java.util.List;

/**
 * Created by user on 7/5/2015.
 */
public class FragmentUserInfo extends BaseFragment{

    private Button btnEdit;
    private Button btnUpdate;
    private Button btnChangeCode;
    private EditText editUserId;
    private EditText editUserName;
    private EditText editUserAddress;
    private SharedPreferences mSharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        initView(view);
        initListeners();

        return view;
    }

    private void initView(View view) {
        btnEdit = (Button) view.findViewById(R.id.btn_edit_user);
        btnUpdate = (Button) view.findViewById(R.id.btn_update_user);
        btnChangeCode = (Button) view.findViewById(R.id.btn_change_code);
        editUserId = (EditText) view.findViewById(R.id.edit_user_id);
        editUserName = (EditText) view.findViewById(R.id.edit_user_name);
        editUserAddress = (EditText) view.findViewById(R.id.edit_user_address);

        mSharedPreferences = getActivity().getSharedPreferences(Utils.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editUserId.setText(mSharedPreferences.getString(Utils.SHARED_KEY_USER_ID, ""));
        editUserName.setText(mSharedPreferences.getString(Utils.SHARED_KEY_USER_NAME, ""));
        editUserAddress.setText(mSharedPreferences.getString(Utils.SHARED_KEY_USER_ADDRESS, ""));

        setEditable(false);
    }

    private void initListeners() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEditButton();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateButton();
            }
        });

        btnChangeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChangeCodeButton();
            }
        });

    }

    private void onClickChangeCodeButton() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialog_change_code, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        final EditText editTextPrevCode = (EditText) promptsView
                .findViewById(R.id.edit_prev_code);
        final EditText editTextNewCode = (EditText) promptsView
                .findViewById(R.id.edit_new_code);
        final EditText editTextNewCodeRe = (EditText) promptsView
                .findViewById(R.id.edit_new_code_re);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Enter Code")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSaved = changeCode(editTextPrevCode.getText().toString(),
                        editTextNewCode.getText().toString(),
                        editTextNewCodeRe.getText().toString());
                if (isSaved) {
                    Toast.makeText(getActivity(), "Saved Successfully!!!", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            }
        });
    }


    private void onClickUpdateButton() {
        String userId = editUserId.getText().toString();
        if(userId == null || userId.length() <= 0) {
            Toast.makeText(getActivity(),"Please enter user id", Toast.LENGTH_LONG).show();
            return;
        }
        String userName = editUserName.getText().toString();
        String userAddress = editUserAddress.getText().toString();

        mSharedPreferences.edit().putString(Utils.SHARED_KEY_USER_ID, userId).
                putString(Utils.SHARED_KEY_USER_NAME, userName).
                putString(Utils.SHARED_KEY_USER_ADDRESS, userAddress).commit();

        Toast.makeText(getActivity(), "Updated Successfully!!!", Toast.LENGTH_LONG).show();

        setEditable(false);


    }

    private void clearFields() {
        editUserId.setText("");
        editUserName.setText("");
        editUserAddress.setText("");
    }

    private void onClickEditButton() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialog_code, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        final EditText editTextCode = (EditText) promptsView
                .findViewById(R.id.edit_code);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Enter Code")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                validateCode(editTextCode.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void setEditable(boolean editable) {
        /*editUserId.setFocusable(editable);
        editUserId.setClickable(editable);

        editUserName.setFocusable(editable);
        editUserName.setClickable(editable);

        editUserAddress.setFocusable(editable);
        editUserAddress.setClickable(editable);*/
        editUserId.setEnabled(editable);
        editUserName.setEnabled(editable);
        editUserAddress.setEnabled(editable);

        if(editable) {
            btnUpdate.setVisibility(View.VISIBLE);
        } else {
            btnUpdate.setVisibility(View.GONE);
        }
    }

    private void validateCode(String code) {
        String sharedCode = mSharedPreferences.getString(Utils.SHARED_KEY_CODE, "");
        if(code.equals(sharedCode)) {
            setEditable(true);
        } else {
            Toast.makeText(getActivity(), "The code is incorrect!!!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean changeCode(String prevCode, String newCode, String newCodeRe) {
        String sharedCode = mSharedPreferences.getString(Utils.SHARED_KEY_CODE, "");
        if(!prevCode.equals(sharedCode)) {
            Toast.makeText(getActivity(), "Prev code is incorrect!!!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!newCode.equals(newCodeRe)) {
            Toast.makeText(getActivity(), "Re entry of the code doesn't match!!!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            mSharedPreferences.edit().putString(Utils.SHARED_KEY_CODE, newCode).commit();
            return true;
        }
    }

    @Override
    public void executeAsyncTaskCallBack(List<AddressBase> addressLatLonList) {

    }
}
