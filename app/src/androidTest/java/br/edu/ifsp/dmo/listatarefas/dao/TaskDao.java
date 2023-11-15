package br.edu.ifsp.dmo.listatarefas.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifsp.dmo.listatarefas.Constants;
import br.edu.ifsp.dmo.listatarefas.model.Task;
import br.edu.ifsp.dmo.listatarefas.model.User;

public class TaskDao {

    private final Context context;
    private final User user;

    public TaskDao(Context context, User user) {
        this.context = context;
        this.user = user;
        readSharedPreferences();
    }

    public void updateTasks(){
        writeSharedPreferences();
    }

    private void writeSharedPreferences() {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();

        for (Task t : user.getTaskList()){
            jsonObject = new JSONObject();
            try {
                jsonObject.put(Constants.ATTR_TASK_TITLE, t.getTitle());
                jsonObject.put(Constants.ATTR_TASK_DESCRIPTION, t.getDescription());
                jsonArray.put(jsonObject);
            }catch (JSONException e){
                Log.e(Constants.TAG, e.getMessage() + "\n on TaskDao.readSharedPreferences().");
            }
        }

        String filaName = Constants.FILE_TASK_PREFIX + user.getName();
        preferences = context.getSharedPreferences(filaName, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(Constants.TABLE_TASK, jsonArray.toString());
        editor.commit();
    }

    private void readSharedPreferences() {
        SharedPreferences preferences;
        String json;
        JSONObject jsonObject;
        JSONArray jsonArray;

        String filaName = Constants.FILE_TASK_PREFIX + user.getName();
        preferences = context.getSharedPreferences(filaName, Context.MODE_PRIVATE);
        json = preferences.getString(Constants.TABLE_TASK, "");

        if (json.isEmpty()){
            Log.i(Constants.TAG, "No data on table users from preferences.");
        }else {
            user.getTaskList().clear();
            try {
                jsonArray = new JSONArray(json);
                for (int i = 0; i != jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    user.addTask(new Task(
                            jsonObject.getString(Constants.ATTR_TASK_TITLE),
                            jsonObject.getString(Constants.ATTR_TASK_DESCRIPTION)
                    ));
                }
            }catch (JSONException e){
                String str = "";
                str = e.getMessage();
                str += "\n on TaskDao.readSharedPreferences().";
                Log.e(Constants.TAG, str);
            }
        }
    }
}
