package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao;

import android.content.Context;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.contants.Constants;

public class CurrentTask extends TaskDao{
    private static CurrentTask instance;

    private CurrentTask(Context context) {
        super(context);
        setJsonObjectName(Constants.KEY_JSON_OBJECT_CURRENT_TASKS);
        fillTaskList();
    }

    public static CurrentTask getInstance(Context context) {
        if(instance == null) {
            instance = new CurrentTask(context);
        }
        return instance;
    }
}
