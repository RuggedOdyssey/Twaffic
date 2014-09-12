package net.ruggedodyssey.twaffic.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import net.ruggedodyssey.twaffic.R;

public class GPlusSignIn extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        ResultCallback<People.LoadPeopleResult>, GoogleApiClient.OnConnectionFailedListener {

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "android-plus-quickstart";
    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    private GoogleApiClient mGoogleApiClient;
    // We use mSignInProgress to track whether user has clicked sign in.
// mSignInProgress can be one of three values:
//
// STATE_DEFAULT: The default state of the application before the user
// has clicked 'sign in', or after they have clicked
// 'sign out'. In this state we will not attempt to
// resolve sign in errors and so will display our
// Activity in a signed out state.
// STATE_SIGN_IN: This state indicates that the user has clicked 'sign
// in', so resolve successive errors preventing sign in
// until the user has successfully authorized an account
// for our app.
// STATE_IN_PROGRESS: This state indicates that we have started an intent to
// resolve an error, and so we should not start further
// intents until the current intent completes.
    private int mSignInProgress;
    // Used to store the PendingIntent most recently returned by Google Play
// services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;
    // Used to store the error code most recently returned by Google Play services
// until the user clicks 'sign in'.
    private int mSignInError;
    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;
    private static final String SAVED_PROGRESS = "sign_in_progress";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gplus_sign_in);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
        mStatus = (TextView) findViewById(R.id.sign_in_status);
        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }
        mGoogleApiClient = buildGoogleApiClient();
    }

    private GoogleApiClient buildGoogleApiClient() {
// When we build the GoogleApiClient we specify where connected and
// connection failed callbacks should be returned, which Google APIs our
// app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
// We only process button clicks when GoogleApiClient is not transitioning
// between connected and not connected.
            switch (v.getId()) {
                case R.id.sign_in_button:
                    mStatus.setText(R.string.status_signing_in);
                    resolveSignInError();
                    break;
                case R.id.sign_out_button:
// We clear the default account on sign out so that Google Play
// services will not return an onConnected callback without user
// interaction.
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    break;
                case R.id.revoke_access_button:
// After we revoke permissions for the user with a GoogleApiClient
// instance, we must discard it and create a new one.
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
// Our sample has caches no user data from Google+, however we
// would normally register a callback on revokeAccessAndDisconnect
// to delete user data so that we comply with Google developer
// policies.
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient = buildGoogleApiClient();
                    mGoogleApiClient.connect();
                    break;
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
// Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");
// Update the user interface to reflect that the user is signed in.
        mSignInButton.setEnabled(false);
        mSignOutButton.setEnabled(true);
        mRevokeButton.setEnabled(true);
// Retrieve some profile information to personalize our app for the user.
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        mStatus.setText(String.format(
                getResources().getString(R.string.signed_in_as),
                currentUser.getDisplayName()));
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(this);
// Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
// Refer to the javadoc for ConnectionResult to see what error codes might
// be returned in onConnectionFailed.
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        if (mSignInProgress != STATE_IN_PROGRESS) {
// We do not have an intent in progress so we should store the latest
// error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();
            if (mSignInProgress == STATE_SIGN_IN) {
// STATE_SIGN_IN indicates the user already clicked the sign in button
// so we should continue processing errors until the user is signed in
// or they click cancel.
                resolveSignInError();
            }
        }
// In this sample we consider the user signed out whenever they do not have
// a connection to Google Play services.
        onSignedOut();
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
// We have an intent which will allow our user to sign in or
// resolve an error. For example if the user needs to
// select an account to sign in with, or if they need to consent
// to the permissions your app is requesting.
            try {
// Send the pending intent that we stored on the most recent
// OnConnectionFailed callback. This will allow the user to
// resolve the error currently preventing our connection to
// Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
// The intent was canceled before it was sent. Attempt to connect to
// get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
// Google Play services wasn't able to provide an intent for some
// error types, so we show the default Google Play services error
// dialog which may still start an intent on our behalf if the
// user can resolve the issue.
            showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
// If the error resolution was successful we should continue
// processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
// If the error resolution was not successful or the user canceled,
// we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }
                if (!mGoogleApiClient.isConnecting()) {
// If Google Play services resolved the issue with a dialog then
// onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }
    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
        }
    }
    private void onSignedOut() {
// Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);
        mSignOutButton.setEnabled(false);
        mRevokeButton.setEnabled(false);
        mStatus.setText(R.string.status_signed_out);
    }
    @Override
    public void onConnectionSuspended(int cause) {
// The connection to Google Play services was lost for some reason.
// We call connect() to attempt to re-establish the connection or get a
// ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DIALOG_PLAY_SERVICES_ERROR:
                if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                    return GooglePlayServicesUtil.getErrorDialog(
                            mSignInError,
                            this,
                            RC_SIGN_IN,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Log.e(TAG, "Google Play services resolution cancelled");
                                    mSignInProgress = STATE_DEFAULT;
                                    mStatus.setText(R.string.status_signed_out);
                                }
                            });
                } else {
                    return new AlertDialog.Builder(this)
                            .setMessage(R.string.play_services_error)
                            .setPositiveButton(R.string.close,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e(TAG, "Google Play services error could not be "
                                                    + "resolved: " + mSignInError);
                                            mSignInProgress = STATE_DEFAULT;
                                            mStatus.setText(R.string.status_signed_out);
                                        }
                                    }).create();
                }
            default:
                return super.onCreateDialog(id);
        }
    }

}
//
//    /* Track whether the sign-in button has been clicked so that we know to resolve
// * all issues preventing sign-in without waiting.
// */
//    private boolean mSignInClicked;
//
//    /* Store the connection result from onConnectionFailed callbacks so that we can
//     * resolve them when the user clicks sign-in.
//     */
//    private ConnectionResult mConnectionResult;
//
//    /* Request code used to invoke sign in user interactions. */
//    private static final int RC_SIGN_IN = 0;
//
//    /* Client used to interact with Google APIs. */
//    private GoogleApiClient mGoogleApiClient;
//
//    /* A flag indicating that a PendingIntent is in progress and prevents
//     * us from starting further intents.
//     */
//    private boolean mIntentInProgress;
//
//    private SignInButton mSignInButton;
//    private Button mSignOutButton;
//    private Button mRevokeButton;
//    private TextView mStatus;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gplus_sign_in);
//
//        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
//        mRevokeButton = (Button) findViewById(R.id.revoke_button);
//        mStatus = (TextView) findViewById(R.id.sign_in_status);
//
//        mSignInButton.setOnClickListener(this);
//        mSignOutButton.setOnClickListener(this);
//        mRevokeButton.setOnClickListener(this);
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .build();
//    }
//
//    public void onClick(View view) {
//        if (view.getId() == R.id.sign_in_button
//                && !mGoogleApiClient.isConnecting()) {
//            mSignInClicked = true;
//            resolveSignInError();
//        }
//        else if (view.getId() == R.id.sign_out_button) {
//            if (mGoogleApiClient.isConnected()) {
//                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                mGoogleApiClient.disconnect();
//                mGoogleApiClient.connect();
//            }
//        }
//        else if (view.getId() == R.id.revoke_button){
//            // Prior to disconnecting, run clearDefaultAccount().
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
//                    .setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(Status status) {
//                        }
//                    });
//        }
//    }
//
//    private void onSignedOut() {
//    // Update the UI to reflect that the user is signed out.
//        mSignInButton.setEnabled(true);
//        mSignOutButton.setEnabled(false);
//        mRevokeButton.setEnabled(false);
//        mStatus.setText(R.string.status_signed_out);
//    }
//
//    protected void  onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    protected void onStop() {
//        super.onStop();
//
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//    /* A helper method to resolve the current ConnectionResult error. */
//    private void resolveSignInError() {
//        if (mConnectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
//                        RC_SIGN_IN, null, 0, 0, 0);
//            } catch (IntentSender.SendIntentException e) {
//                // The intent was canceled before it was sent.  Return to the default
//                // state and attempt to connect to get an updated ConnectionResult.
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//    }
//
//
//    public void onConnectionFailed(ConnectionResult result) {
//        if (!mIntentInProgress) {
//            // Store the ConnectionResult so that we can use it later when the user clicks
//            // 'sign-in'.
//            mConnectionResult = result;
//
//            if (mSignInClicked) {
//                // The user has already clicked 'sign-in' so we attempt to resolve all
//                // errors until the user is signed in, or they cancel.
//                resolveSignInError();
//            }
//        }
//        onSignedOut();
//    }
//
//    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
//        if (requestCode == RC_SIGN_IN) {
//            if (responseCode != RESULT_OK) {
//                mSignInClicked = false;
//            }
//
//            mIntentInProgress = false;
//
//            if (!mGoogleApiClient.isConnecting()) {
//                mGoogleApiClient.connect();
//            }
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle connectionHint) {
//        mSignInClicked = false;
//        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
//        mSignInButton.setEnabled(false);
//        mSignOutButton.setEnabled(true);
//        mRevokeButton.setEnabled(true);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//}
