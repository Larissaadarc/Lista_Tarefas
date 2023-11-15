package br.edu.ifsp.dmo.listatarefas.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import br.edu.ifsp.dmo.listatarefas.Constants;
import br.edu.ifsp.dmo.listatarefas.model.User;

public class UserDao {

    private final Context context;
    private List<User> dataset;

    public UserDao(Context context) {
        this.context = context;
        dataset = new LinkedList<>();
        readSharedPreferences();
    }

    public boolean create(User user){
        if (user != null){
            if (!dataset.contains(user)){
                dataset.add(user);
                writeSharedPreferences();
                readSharedPreferences();
                return true;
            }
        }
        return false;
    }

    public User recuperate(String username){
        return dataset.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst()
                .orElse(null);
    }
    private void readSharedPreferences() {
        SharedPreferences preferences;
        String json;
        User user;
        JSONObject jsonObject;
        JSONArray jsonArray;

        preferences = context.getSharedPreferences(Constants.FILE_USERS, Context.MODE_PRIVATE);
        json = preferences.getString(Constants.TABLE_USERS, "");

        if (json.isEmpty()){
            Log.i(Constants.TAG, "No data on table users from preferences.");
        }else {
            dataset.clear();
            try {
                jsonArray = new JSONArray(json);
                for (int i = 0; i != jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    user = new User(
                            jsonObject.getString(Constants.ATTR_USERNAME),
                            jsonObject.getInt(Constants.ATTR_PASSWORD));
                    dataset.add(user);
                }
            }catch (JSONException e){
                String str = "";
                str = e.getMessage();
                str += "\n on UserDao.readSharedPreferences().";
                Log.e(Constants.TAG, str);
            }
        }
    }

    private void writeSharedPreferences() {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();

        for (User u : dataset){
            jsonObject = new JSONObject();
            try {
                jsonObject.put(Constants.ATTR_USERNAME, u.getName());
                jsonObject.put(Constants.ATTR_PASSWORD, u.getPassword());
                jsonArray.put(jsonObject);
            }catch (JSONException e){
                Log.e(Constants.TAG, e.getMessage() + "\n on UserDao.readSharedPreferences().");
            }
        }

        preferences = context.getSharedPreferences(Constants.FILE_USERS, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(Constants.TABLE_USERS, jsonArray.toString());
        editor.commit();
    }
}
