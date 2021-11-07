package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Trecho de código retirado do link
 * https://pt.stackoverflow.com/questions/207341/mascara-de-data-no-edittext/207415
 * */

public abstract class Mask {

    public static TextWatcher insert(final String mask, final EditText et) {
        return new TextWatcher() {
            boolean isUpdating;
            String oldTxt = "";

            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                String str = Mask.unmask(s.toString());
                String maskCurrent = "";
                if (isUpdating) {
                    oldTxt = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > oldTxt.length()) {
                        maskCurrent += m;
                        continue;
                    }
                    try {
                        maskCurrent += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                et.setText(maskCurrent);
                et.setSelection(maskCurrent.length());
            }

            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    private static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }
}
