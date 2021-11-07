package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.R;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageCurrentTask;
    private ImageView mImageCompletedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageCurrentTask = findViewById(R.id.img_list_current);
        mImageCompletedTask = findViewById(R.id.img_list_completed);

        mImageCurrentTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, CurrentTaskActivity.class);
            startActivity(intent);
        });
        mImageCompletedTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, CompletedTaskActivity.class);
            startActivity(intent);
        });
    }
}