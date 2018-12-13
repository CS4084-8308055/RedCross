package ie.ul.davidbeck.redcross;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mStationEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mStationEditText = findViewById(R.id.station_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        }
    }

    public void handleSignIn(View view){
        final String station = mStationEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (station.length() < 2 ){
            mStationEditText.setError(getString(R.string.invalid_email));
        } else if (password.length() < 6) {
            mPasswordEditText.setError(getString(R.string.invalid_password));
        } else{
            String email = station + Constants.EMAIL_DOMAIN;
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra(Constants.EXTRA_CALLSIGN, station);
                                startActivity(intent);
                            } else{
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    @Override
    public void onBackPressed() {
    }
}
