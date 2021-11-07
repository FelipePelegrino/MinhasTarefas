package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao;

import android.content.Context;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.contants.Constants;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class CompletedTask extends TaskDao {

    private static CompletedTask instance;

    private CompletedTask(Context context) {
        super(context);
        setJsonObjectName(Constants.KEY_JSON_OBJECT_COMPLETED_TASKS);
        fillTaskList();
    }

    public static CompletedTask getInstance(Context context) {
        if(instance == null) {
            instance = new CompletedTask(context);
        }
        return instance;
    }

    @Override
    protected boolean addInOrder(List<Task> list, Task task) {
        boolean isSuccess = false;
        Task aux = null;

        if (list != null) {
            if (list.size() == 0) {
                isSuccess = list.add(task);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    aux = list.get(i);
                    if (convertAndCompareToDates(aux.getDueDate(), task.getDueDate()) > 0) {
                        list.add(i, task);
                        i = list.size();
                        isSuccess = true;
                    }
                }
                if (!isSuccess) {
                    isSuccess = list.add(task);
                }
            }
        }
        return isSuccess;
    }
}
