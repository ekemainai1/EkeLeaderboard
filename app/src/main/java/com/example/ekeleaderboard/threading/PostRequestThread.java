package com.example.ekeleaderboard.threading;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.example.ekeleaderboard.models.PostModel;
import com.example.ekeleaderboard.networkcalls.Api;
import com.example.ekeleaderboard.utils.Constants;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;


public class PostRequestThread extends Thread {

    private static final String TAG = "PostRequestThread";
    private MyThreadHandler mMyThreadHandler = null;
    private Handler mMainThreadHandler;
    private boolean isRunning = false;

    public PostRequestThread(Handler mMainThreadHandler){
        this.mMainThreadHandler = mMainThreadHandler;
        isRunning = true;
    }

    @Override
    public void run() {
        if (isRunning){
            Looper.prepare();
            mMyThreadHandler = new MyThreadHandler(Looper.myLooper());
            Looper.loop();
        }
    }

    public void quitThread(){
        isRunning = false;
        mMainThreadHandler = null;
    }

    public void sendMessageToBackgroundThread(Message message){
        try {
            mMyThreadHandler.sendMessage(message);
        }catch (NullPointerException e){
            Log.e(TAG, "sendMessageToBackgroundThread: Null Pointer: " + e.getMessage());
            try {
                Thread.sleep(100);
            }catch (InterruptedException e1){
                e1.printStackTrace();
            }
        }

    }

    class MyThreadHandler extends Handler {

        public MyThreadHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.POST_INSERT_NEW: {
                    Log.d(TAG, "handleMessage: post project on thread: " + Thread.currentThread().getName());
                    String firstName;
                    String lastName;
                    String email;
                    String projectLink;

                    firstName = msg.getData().getString("firstName");
                    lastName = msg.getData().getString("lastName");
                    email = msg.getData().getString("email");
                    projectLink = msg.getData().getString("project");

                    // Call network request
                    postGooglePhaseIIProjectOnThread(firstName,lastName,email,projectLink);
                    break;
                }


            }
        }

        private void postGooglePhaseIIProjectOnThread(String firstName, String lastName, String email, String projectLink){


            Api.getClient().postProject(firstName, lastName, email, projectLink).clone().enqueue(
                    new retrofit2.Callback<PostModel>() {
                        @Override
                        public void onResponse(@NotNull Call<PostModel> call, @NotNull Response<PostModel> response) {
                            Message   message = Message.obtain(null, Constants.POST_INSERT_SUCCESS);
                            mMainThreadHandler.sendMessage(message);

                        }

                        @Override
                        public void onFailure(@NotNull Call<PostModel> call, @NotNull Throwable throwable) {
                            Message   message = Message.obtain(null, Constants.POST_INSERT_FAIL);
                            mMainThreadHandler.sendMessage(message);

                        }
                    });
        }
    }
}
