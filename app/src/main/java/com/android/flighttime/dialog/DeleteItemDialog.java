/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.android.flighttime.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.flighttime.R;
import com.android.flighttime.listener.deleteDialogClickListener;

public class DeleteItemDialog extends DialogFragment {
    private static final String KEY_GROUP_ITEM_POSITION = "mission_position";
    private static final String KEY_CHILD_ITEM_POSITION = "flight_position";
    private deleteDialogClickListener listener;

    public static DeleteItemDialog newInstance(int groupPosition, int childPosition) {
        final DeleteItemDialog frag = new DeleteItemDialog();
        final Bundle args = new Bundle();

        args.putInt(KEY_GROUP_ITEM_POSITION, groupPosition);
        args.putInt(KEY_CHILD_ITEM_POSITION, childPosition);

        frag.setArguments(args);
        return frag;
    }

    public DeleteItemDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);

        final int groupPosition = getArguments().getInt(KEY_GROUP_ITEM_POSITION, Integer.MIN_VALUE);
        final int childPosition = getArguments().getInt(KEY_CHILD_ITEM_POSITION, Integer.MIN_VALUE);
        Log.d("Delete", "groupPosition" + groupPosition + " " + childPosition);
        // for expandable list
        if (childPosition == RecyclerView.NO_POSITION) {
            builder.setMessage(getResources().getString(R.string.dialog_deleted_mission));
        } else {
            builder.setMessage(getResources().getString(R.string.dialog_deleted_flight));
        }
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (childPosition == RecyclerView.NO_POSITION)
                    listener.onClickToDeleteMission(groupPosition);
                    else
                    listener.onClickToDeleteFlight(groupPosition, childPosition);
                }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (deleteDialogClickListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }
}
