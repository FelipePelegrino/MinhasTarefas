package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class UtilsDao {
    private static final String TAG = UtilsDao.class.getSimpleName();

    public static String taskToJsonString(@NonNull Task task) {
        String output = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TaskFields.FIELD_NAME, task.getName());
            jsonObject.put(TaskFields.FIELD_DESCRIPTION, task.getDescription());
            jsonObject.put(TaskFields.FIELD_PRIORITY, task.getPriority());
            jsonObject.put(TaskFields.FIELD_DUE_DATE, task.getDueDate());
            jsonObject.put(TaskFields.FIELD_DONE, task.isDone());

            output = jsonObject.toString();
        } catch (JSONException jsonEx) {
            Log.e(TAG, jsonEx.getMessage());
            output = null;
        }
        return output;
    }

    public static Task jsonStringToTask(String string) {
        Task task = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(string);
            task = new Task(jsonObject.getString(TaskFields.FIELD_NAME),
                    jsonObject.getString(TaskFields.FIELD_DESCRIPTION),
                    (byte) jsonObject.getInt(TaskFields.FIELD_PRIORITY),
                    jsonObject.getString(TaskFields.FIELD_DUE_DATE),
                    jsonObject.getBoolean(TaskFields.FIELD_DONE));
        } catch (JSONException jsonEx) {
            Log.e(TAG, jsonEx.getMessage());
            task = null;
        }
        return task;
    }
}
