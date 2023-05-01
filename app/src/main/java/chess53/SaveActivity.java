package chess53;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.chessandroid.R;

public class SaveActivity extends Activity {
    EditText gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.save_layout);
        Button confirmSaveButton = findViewById(R.id.confirmSave);
        Button cancelSaveButton = findViewById(R.id.cancelAdd);
        gameName=findViewById(R.id.newGameName);

        confirmSaveButton.setOnClickListener(v -> {
            Intent saveIntent = new Intent();
            saveIntent.putExtra("Name", gameName.getText().toString());
            Log.i("Z", gameName.getText().toString());
            setResult(RESULT_OK, saveIntent);
            finish();
        });

        cancelSaveButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
