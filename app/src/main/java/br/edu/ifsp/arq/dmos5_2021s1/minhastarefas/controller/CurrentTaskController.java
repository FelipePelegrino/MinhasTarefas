package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.controller;

import android.content.Context;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao.CurrentTask;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class CurrentTaskController {

    private static CurrentTaskController instance;
    private static List<Task> listTask;
    private static CurrentTask currentTaskDao;
    private static CompletedTaskController controllerCompletedTask;

    private CurrentTaskController(Context context) {
        currentTaskDao = CurrentTask.getInstance(context);
        controllerCompletedTask = CompletedTaskController.getInstance(context);
        listTask = currentTaskDao.getListTasks();
    }

    public static CurrentTaskController getInstance(Context context) {
        if (CurrentTaskController.instance == null) {
            instance = new CurrentTaskController(context);
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
                    isSuccess = currentTaskDao.saveTask(null, t);
                }
            }
        }
        listTask = currentTaskDao.getListTasks();
        return isSuccess;
    }

    public static boolean updateTask(Task oldTask, Task currentTask) {
        boolean isSuccess = false;
        if (instance != null) {
            if (oldTask != null && currentTask != null) {
                isSuccess = currentTaskDao.saveTask(oldTask, currentTask);
            }
        }
        listTask = currentTaskDao.getListTasks();
        return isSuccess;
    }

    public static boolean deleteTask(Task t) {
        boolean isSuccess = false;
        if (instance != null) {
            if (t != null) {
                if (listTask.contains(t)) {
                    isSuccess = currentTaskDao.deleteTask(t);
                }
            }
        }
        listTask = currentTaskDao.getListTasks();
        return isSuccess;
    }

    public static boolean setTaskDone(Task t) {
        boolean isSuccess = false;
        if (instance != null && controllerCompletedTask != null) {
            if (t != null) {
                if (listTask.contains(t)) {
                    Task myTask = listTask.get(listTask.indexOf(t));
                    myTask.setDone(true);
                    if(controllerCompletedTask.newTask(myTask)){
                        isSuccess = deleteTask(myTask);
                    }
                    else {
                        controllerCompletedTask.deleteTask(myTask);
                    }
                }
            }
        }
        listTask = currentTaskDao.getListTasks();
        return isSuccess;
    }
}
