package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.contants.Constants;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class TaskDao {

    private final String TAG = this.getClass().getSimpleName();
    private String jsonObjectName;
    private Context context;
    private List<Task> listTasks;

    public TaskDao(Context context) {
        this.context = context;
        if (listTasks == null) {
            listTasks = new LinkedList<>();
        }
    }

    public List<Task> getListTasks() {
        return listTasks;
    }

    public boolean deleteTask(Task task) {
        boolean isSuccess = false;
        if (task != null) {
            if (listTasks.contains(task)) {
                isSuccess = listTasks.remove(task);
                commitAll();
            }
        }
        return isSuccess;
    }

    public boolean saveTask(Task oldTask, Task currentTask) {
        boolean isSuccess = false;
        if (currentTask != null) {
            Task task = null;
            if (oldTask != null) {
                for (int i = 0; i < listTasks.size(); i++) {
                    task = listTasks.get(i);
                    if (task.equals(oldTask)) {
                        i = listTasks.size();
                    } else {
                        task = null;
                    }
                }
            }

            if (task == null) {
                isSuccess = addInOrder(listTasks, currentTask);
            } else {
                task.setName(currentTask.getName());
                task.setDescription(currentTask.getDescription());
                task.setPriority(currentTask.getPriority());
                task.setDueDate(currentTask.getDueDate());
                isSuccess = true;
            }
            commitAll();
        }
        return isSuccess;
    }

    protected void setJsonObjectName(String name) {
        this.jsonObjectName = name;
    }

    protected void fillTaskList() {
        SharedPreferences sharedPreferences;
        JSONArray jsonArray;
        String jsonString;
        Task task;

        sharedPreferences = this.context.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        jsonString = sharedPreferences.getString(jsonObjectName, "");

        if (!jsonString.isEmpty()) {
            try {
                jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    task = UtilsDao.jsonStringToTask(jsonArray.getJSONObject(i).toString());
                    if (task != null) {
                        addInOrder(listTasks, task);
                    } else {
                        Log.e(TAG, jsonArray.getJSONObject(i).toString());
                    }
                }
            } catch (JSONException jsonEx) {
                Log.e(TAG, jsonEx.getMessage());
                Log.e(TAG, jsonEx.getStackTrace().toString());
            }
        } else {
            Log.i(TAG, "Sem dados armazenados");
        }
    }

    protected boolean addInOrder(List<Task> list, Task task) {
        boolean isSuccess = false;
        Task aux = null;

        if (list != null) {
            if (list.size() == 0) {
                isSuccess = list.add(task);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    aux = list.get(i);
                    if (isValidPriorityDateName(aux, task)) {
                        list.add(i, task);
                        i = list.size();
                        isSuccess = true;
                    } else if (isValidPriorityDate(aux, task)) {
                        list.add(i, task);
                        i = list.size();
                        isSuccess = true;
                    } else if (aux.getPriority() < task.getPriority()) {
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

    protected int convertAndCompareToDates(String date1, String date2) {
        int output = 0;
        Date compareDate1;
        Date compareDate2;

        try {
            compareDate1 = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
            compareDate2 = new SimpleDateFormat("dd/MM/yyyy").parse(date2);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            compareDate1 = compareDate2 = null;
        }
        if (compareDate1 != null) {
            output = compareDate1.compareTo(compareDate2);
        }
        return output;
    }

    private void commitAll() {
        boolean isSuccess = true;
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        String jsonString;
        JSONObject jsonObject;

        for (Task c : listTasks) {
            jsonString = UtilsDao.taskToJsonString(c);
            if (jsonString != null) {
                try {
                    jsonObject = new JSONObject(jsonString);
                    jsonArray.put(jsonObject);
                } catch (JSONException jsonException) {
                    Log.e(TAG, "Falha ao criar JSONObject a partir de jsonString ou ao adicionar o object no array");
                    Log.e(TAG, jsonException.getMessage());
                }
            }
        }

        if (isSuccess) {
            editor.putString(this.jsonObjectName, jsonArray.toString());
            editor.commit();
        }
    }

    private boolean isValidPriorityDateName(Task task1, Task task2) {
        return ((task1.getPriority() < task2.getPriority() || task1.getPriority() == task2.getPriority()) &&
                (convertAndCompareToDates(task1.getDueDate(), task2.getDueDate()) > 0 || convertAndCompareToDates(task1.getDueDate(), task2.getDueDate()) == 0) &&
                task1.getName().compareToIgnoreCase(task2.getName()) > 0);
    }

    private boolean isValidPriorityDate(Task task1, Task task2) {
        return ((task1.getPriority() < task2.getPriority() || task1.getPriority() == task2.getPriority()) &&
                (convertAndCompareToDates(task1.getDueDate(), task2.getDueDate()) > 0 || convertAndCompareToDates(task1.getDueDate(), task2.getDueDate()) == 0));
    }
}
