package com.example.hassan.facebook_app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView txtname, txtemail;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtname = (TextView) findViewById(R.id.txtname);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "circleImageView"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }
            @Override
            protected void onActivityResult(int requestcode,int resultcode,@Nullable Intent data){
            callbackManager.onActivityResult(requestcode,resultcode,data);
            super.onActivityResult(requestcode,resultcode,data);
        }
     AccessTokenTracker tokenTracker= new AccessTokenTracker() {
         @Override
         protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

             if (currentAccessToken==null){

                 txtname.setText("");
                 txtemail.setText("");
                 Toast.makeText(MainActivity.this,"user logined out",Toast.LENGTH_LONG).show();

             }
             else loadUserprofile(currentAccessToken);
         }
     };
    private void loadUserprofile(AccessToken newaccessToken ){
        GraphRequest request=GraphRequest.newMeRequest(newaccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try{
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");

                    String email=object.getString("email");
                    String id=object.getString("id");
                    String image_url="http://graph.facebook.com/"+id+"/picture?type=normal";

                    txtname.setText(first_name+" "+last_name);
                    RequestOptions requestOptions= new RequestOptions();
                    requestOptions.dontAnimate();
                    Glide.with(MainActivity.this).load(image_url).into(circleImageView);

                }catch(JSONException e){
                e.printStackTrace();

                }

            }
        });

        Bundle parameters= new Bundle();
        parameters.putString("fields","first_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

}

