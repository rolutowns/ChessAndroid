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
            String name = gameName.getText().toString().trim();
            boolean contains = false;
            for (Game g: MainActivity.games)
                if (g.getName().equals(name)) {
                    contains = true;
                    break;
                }
            if (name.isEmpty() || contains){
                setResult(RESULT_CANCELED);
            }
            else{
                saveIntent.putExtra("Name", name);
                Log.i("Z", name);
                setResult(RESULT_OK, saveIntent);
            }
            finish();
        });

        cancelSaveButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
