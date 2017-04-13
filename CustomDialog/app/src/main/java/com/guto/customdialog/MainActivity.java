package com.guto.customdialog;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btDialog = (Button)findViewById(R.id.btDialog);
        btDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDetailsDialog();
            }
        });
    }

    private void createDetailsDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_details);
        dialog.setTitle("Titulo");

        // set the custom dialog components - text, image and button
        LinearLayout llCusto = (LinearLayout) dialog.findViewById(R.id.llCusto);
        ImageView ivIcon = (ImageView) dialog.findViewById(R.id.ivIcon);
        TextView tvCategoria = (TextView) dialog.findViewById(R.id.tvCategoria);
        TextView tvSetor = (TextView) dialog.findViewById(R.id.tvSetor);
        TextView tvStatus = (TextView) dialog.findViewById(R.id.tvStatus);
        TextView tvData = (TextView) dialog.findViewById(R.id.tvData);
        TextView tvCreator = (TextView) dialog.findViewById(R.id.tvCreator);
        TextView tvObservacao = (TextView) dialog.findViewById(R.id.tvObservacao);
        TextView tvHideDetails = (TextView) dialog.findViewById(R.id.tvHideDetails);

        tvHideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
