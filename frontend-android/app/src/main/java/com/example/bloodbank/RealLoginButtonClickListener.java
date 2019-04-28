package com.example.bloodbank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

public class RealLoginButtonClickListener implements RequestCallback, View.OnClickListener {
    Context ctx;
    View mParentView;
    public RealLoginButtonClickListener(Context c, View parent){
        mParentView = parent;
        ctx = c;
    }

    public void onClick(View mView){
        try{
            Log.d("login", "onClick");
            View v = mParentView;
            APICaller c = new APICaller("http://nj.kuroa.me:8080/", ctx);
            String username = ((EditText) v.findViewById(R.id.log_user)).getText().toString();
            String password = ((EditText) v.findViewById(R.id.log_pass)).getText().toString();
            c.userAuth(username, password, this);
        }catch(Exception e){
            Log.d("login",e.toString());
        }
    }

    public void process(JSONObject obj) {
        try {
            Boolean ok = obj.getBoolean("ok");
            if (ok) {
                Log.d("login", "ok");
                String token = obj.getString("token");
                SharedPreferences prefs = ctx.getSharedPreferences("com.example.bloodbank", Context.MODE_PRIVATE);
                prefs.edit().putString("token", token).apply();
                Log.d("token", token);
            }else {
                Log.d("misc", "not ok");
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(ctx).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }
        }catch (Exception e){
            Log.d("token", e.toString());
        }
    }
}