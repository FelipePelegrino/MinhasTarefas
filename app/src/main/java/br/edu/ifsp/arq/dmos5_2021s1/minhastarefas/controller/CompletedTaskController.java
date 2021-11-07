package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.controller;

import android.content.Context;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao.CompletedTask;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class CompletedTaskController {

    private static CompletedTaskController instance;
    private static List<Task> listTask;
    private static CompletedTask completedTaskDao;

    private CompletedTaskController(Context context) {
        completedTaskDao = CompletedTask.getInstance(context);
        listTask = completedTaskDao.getListTasks();
    }

    public static CompletedTaskController getInstance(Context context) {
        if (CompletedTaskController.instance == null) {
            instance = new CompletedTaskController(context);
        }
        return instance;
    }

    public static List<Task> getAllTasks() {
        List<Task> allTasks = null;
        if (instance != null) {
            allTasks = listTask;
        }
        return allTasks;
    }

    public static Task getTask(int position) {
        Task myTask = null;
        if (instance != null) {
            if (position < listTask.size()) {
                myTask = listTask.get(position);
            }
        }
        return myTask;
    }

    public static boolean newTask(Task t) {
        boolean isSuccess = false;
        if (instance != null) {
            if (t != null) {
                if (!listTask.contains(t)) {
                    isSuccess = completedTaskDao.saveTask(null, t);
                }
            }
        }
        listTask = completedTaskDao.getListTasks();
        return isSuccess;
    }

    public static boolean deleteTask(Task t) {
        boolean isSuccess = false;
        if (instance != null) {
            if (t != null) {
                if (listTask.contains(t)) {
                    isSuccess = completedTaskDao.deleteTask(t);
                }
            }
        }
        listTask = completedTaskDao.getListTasks();
        return isSuccess;
    }
}
