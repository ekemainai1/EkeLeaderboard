package com.example.ekeleaderboard.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.example.ekeleaderboard.R;
import com.example.ekeleaderboard.dialogs.PostCheckDialogFragment;
import com.example.ekeleaderboard.dialogs.PostSuccessDialogFragment;
import com.example.ekeleaderboard.dialogs.PostUnsucessDialogFragment;
import com.example.ekeleaderboard.models.PostModel;
import com.example.ekeleaderboard.networkcalls.Api;
import com.example.ekeleaderboard.threading.PostRequestThread;
import com.example.ekeleaderboard.utils.Constants;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LeadersPostActivity extends AppCompatActivity implements
        Handler.Callback
    {

    private static final String TAG = "LeadersPostActivity";

    Toolbar toolbar;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RelativeLayout relativeLayout;
    public Button submitButton;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextProject;
    ProgressDialog progressDialog;

    String firstName;
    String lastName;
    String email;
    String projectLink;

    PostModel postModel;
    private PostRequestThread postRequestThread;
    private Handler mMainThreadHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders_post);

        mMainThreadHandler = new Handler(this);
        postRequestThread = new PostRequestThread(mMainThreadHandler);
        postRequestThread.start();

        // Get all layout components
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.mainScreenAppBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);
        submitButton = (Button) findViewById(R.id.submitButton);
        editTextFirstName = (EditText) findViewById(R.id.editTextFist);
        editTextLastName = (EditText) findViewById(R.id.editTextLast);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextProject = (EditText) findViewById(R.id.editTextProject);
        progressDialog  = new ProgressDialog(LeadersPostActivity.this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get text typed by user
        firstName = editTextFirstName.getText().toString().trim();
        lastName = editTextLastName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        projectLink = editTextProject.getText().toString().trim();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    public void sendPostCredentials(){
        Log.d(TAG, "sendPostCredentials: called.");
        Message message = Message.obtain(null, Constants.POST_INSERT_NEW);
        Bundle bundle = new Bundle();

        // verify text content
        // Validate the fields and call post method to implement the api
        if (validate(editTextFirstName) && validate(editTextLastName) && validateEmail()
                && validate(editTextProject)) {

            bundle.putString("firstName", firstName);
            bundle.putString("lastName",lastName);
            bundle.putString("email",email);
            bundle.putString("project",projectLink);
            message.setData(bundle);

            postRequestThread.sendMessageToBackgroundThread(message);
            // Clear all text after posting
            editTextFirstName.getText().clear();
            editTextLastName.getText().clear();
            editTextEmail.getText().clear();
            editTextProject.getText().clear();

            // Display a progress dialog
            progressDialog.setCancelable(false); // set cancelable to false
            progressDialog.setMessage("Please Wait ... "); // set message
            progressDialog.show(); // show progress dialog

        }


    }

    private void sendMessageToThread(){
        Log.d(TAG, "sendTestMessageToThread: sending message from thread:  " + Thread.currentThread().getName());
        Message message = Message.obtain(null, Constants.POST_INSERT_NEW);
        postRequestThread.sendMessageToBackgroundThread(message);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(postRequestThread != null) {
            postRequestThread.quitThread();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        postRequestThread.quitThread();

    }


    private boolean validate(EditText editText) {
        // Check the length of the enter data in EditText and give error if its empty
        if (editText.getText().toString().trim().length() > 0) {
            return true; // Returns true if field is not empty
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String email = editTextEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            editTextEmail.setError("Email is not valid.");
            editTextEmail.requestFocus();
            return false;
        }

        return true;
    }

    public void executePost(){
        // Validate the fields and call post method to implement the api
        if (validate(editTextFirstName) && validate(editTextLastName) && validateEmail()
                && validate(editTextProject)) {

            postGooglePhaseIIProject();
            // Clear all text after posting
            editTextFirstName.getText().clear();
            editTextLastName.getText().clear();
            editTextEmail.getText().clear();
            editTextProject.getText().clear();

        }
    }

    private void postGooglePhaseIIProject(){

        // Display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(LeadersPostActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait ... "); // set message
        progressDialog.show(); // show progress dialog

        Api.getClient().postProject(firstName, lastName, email, projectLink).clone().enqueue(
                new Callback<PostModel>() {
                    @Override
                    public void onResponse(@NotNull Call<PostModel> call, @NotNull Response<PostModel> response) {
                            // In this method we will get the response from API
                            progressDialog.dismiss(); //dismiss progress dialog
                            assert response.body() != null;
                            postModel = response.body();
                            // Display the message getting from web api
                            showSuccessAlertDialog();

                    }

                    @Override
                    public void onFailure(@NotNull Call<PostModel> call, @NotNull Throwable throwable) {
                        // If error occurs in network transaction then we can get the error in this method.
                        //Toast.makeText(LeadersPostActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss(); // Dismiss progress dialog
                        showUnsuccessfulAlertDialog();
                    }
                });
    }


    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PostCheckDialogFragment alertDialog = PostCheckDialogFragment.newInstance();
        alertDialog.show(fm, "fragment_alert");

    }

    private void showSuccessAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PostSuccessDialogFragment alertDialog = PostSuccessDialogFragment.newInstance();
        alertDialog.show(fm, "fragment_alert");

        Runnable dismissRunner = new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }

        };
        new Handler().postDelayed(dismissRunner, 120000);
    }

    private void showUnsuccessfulAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        PostUnsucessDialogFragment alertDialog = PostUnsucessDialogFragment.newInstance();
        alertDialog.show(fm, "fragment_alert");

        Runnable dismissRunner = new Runnable() {
            public void run() {
                alertDialog.dismiss();
            }

        };
        new Handler().postDelayed(dismissRunner, 120000);
    }


    @Override
     public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {

            case Constants.POST_INSERT_SUCCESS: {
                Log.d(TAG, "handleMessage: successfully posted project. This is from thread: "
                        + Thread.currentThread().getName());
                // In this method we will get the response from API
                progressDialog.dismiss(); //dismiss progress dialog
                // Display the message getting from web api
                showSuccessAlertDialog();

                break;
            }
            case Constants.POST_INSERT_FAIL: {
                Log.d(TAG, "handleMessage: project posting failed. This is from thread: "
                        + Thread.currentThread().getName());
                // If error occurs in network transaction then we can get the error in this method.
                //Toast.makeText(LeadersPostActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); // Dismiss progress dialog
                showUnsuccessfulAlertDialog();

                break;
            }


        }
         return false;
        }
    }