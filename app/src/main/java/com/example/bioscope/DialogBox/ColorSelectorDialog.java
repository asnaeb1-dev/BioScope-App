package com.example.bioscope.DialogBox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.example.bioscope.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorSelectorDialog  {

    private Context context;
    private View view;
    private int color;

    public ColorSelectorDialog(Context context) {
        this.context = context;

    }

    public void generateColorSelectorDialog(){
        view = ((Activity)context).getLayoutInflater().inflate(R.layout.color_selector_ui, null);

        CircleImageView civ1 = view.findViewById(R.id.c1),
                civ2 = view.findViewById(R.id.c2),
                civ3 = view.findViewById(R.id.c3),
                civ4 = view.findViewById(R.id.c4),
                civ5 = view.findViewById(R.id.c5),
                civ6 = view.findViewById(R.id.c6),
                civ7 = view.findViewById(R.id.c7),
                civ8 = view.findViewById(R.id.c8),
                civ9 = view.findViewById(R.id.c9);

        engageClickListener(civ1, civ2, civ3, civ4, civ5,civ6, civ7, civ8, civ9);
        new AlertDialog.Builder(context)
                .setTitle("Select color")
                .setView(view)
                .setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, String.valueOf(color), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    private void engageClickListener(final CircleImageView c1, CircleImageView c2, CircleImageView c3, CircleImageView c4, CircleImageView c5, CircleImageView c6, CircleImageView c7, CircleImageView c8, CircleImageView c9){
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_2);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.primary);

            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_7);

            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_8);

            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_12);

            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_11);

            }
        });
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_14);

            }
        });
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_13);

            }
        });
        c9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = context.getResources().getColor(R.color.color_1);
            }
        });
    }
}
