package com.example.bazededate;

import android.os.AsyncTask;
import android.widget.Toast;

public class GetUserOperation  extends AsyncTask<String, Object, Boolean> {
    UserOperations listener;
    GetUserOperation(UserOperations listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String...strings) {
        String password = "";
        try {
            password = MyApplication.getAppDatabase().userDao().getPassword(strings[0]);
        } catch (Exception e) {
            System.out.println(e);
            return false;

        }

        if (password == ""){
            System.out.println("Nu a gasit!");
            return false;
        }
        ;
        //verifica daca parola este egalaa
        return password.equals(strings[1]);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }



    @Override
    protected void onPostExecute(Boolean result) {
        if (result == true)
            System.out.println("LOGGED IN!!!!!!!");
        else System.out.println("NOT GOOD!!!!!!!");
    }
}