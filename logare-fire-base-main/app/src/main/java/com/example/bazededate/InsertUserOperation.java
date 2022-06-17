package com.example.bazededate;

import android.os.AsyncTask;

public class InsertUserOperation extends AsyncTask<User, Object, String> {
    UserOperations listener;
    InsertUserOperation(UserOperations listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(User...users) {
        try {
            MyApplication.getAppDatabase().userDao().insertAll(users);
        } catch (Exception e) {
            System.out.println(e);
            return "error";

        }
        return "success";
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
