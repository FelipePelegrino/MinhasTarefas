package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view;

import android.content.Context;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.R;

public class UtilsView {

    public static int getColorToImgCheck(Context context, boolean isChecked) {
        if (isChecked) {
            return context.getResources().getColor(R.color.green, context.getTheme());
        } else {
            return context.getResources().getColor(R.color.gray, context.getTheme());
        }
    }

    public static int getColorToImgPriority(Context context, byte priority) {
        int resource;
        switch(priority) {
            case 1:
                resource = context.getResources().getColor(R.color.gold, context.getTheme());
                break;

            case 2:
                resource = context.getResources().getColor(R.color.orange, context.getTheme());
                break;

            case 3:
                resource = context.getResources().getColor(R.color.red, context.getTheme());
                break;

            default:
                resource = context.getResources().getColor(R.color.gray, context.getTheme());
                break;
        }
        return resource;
    }
}
