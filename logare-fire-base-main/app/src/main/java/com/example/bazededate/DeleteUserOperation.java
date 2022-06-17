package com.example.bazededate;

import android.os.AsyncTask;

public class DeleteUserOperation extends AsyncTask<Integer, Object, String> {
    UserOperations listener;
    DeleteUserOperation(UserOperations listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        try {
            MyApplication.getAppDatabase().userDao().deleteAllId(integers[0]);
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
