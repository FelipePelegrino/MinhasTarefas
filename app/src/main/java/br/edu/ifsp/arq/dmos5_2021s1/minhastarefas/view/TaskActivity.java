package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.R;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.contants.Constants;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao.UtilsDao;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class TaskActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private Button mBtnSave;
    private Button mBtnDelete;
    private EditText mEditName;
    private EditText mEditDueDate;
    private EditText mEditDescription;
    private ImageView mImgCheck;
    private ImageView mImgLowPriority;
    private ImageView mImgMediumPriority;
    private ImageView mImgHighPriority;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initVariables();
        configureFields();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initVariables() {
        mBtnSave = findViewById(R.id.btn_save);
        mBtnDelete = findViewById(R.id.btn_delete);
        mImgLowPriority = findViewById(R.id.img_low_priority);
        mImgMediumPriority = findViewById(R.id.img_medium_priority);
        mImgHighPriority = findViewById(R.id.img_high_priority);
        mImgCheck = findViewById(R.id.img_task_check);
        mEditName = findViewById(R.id.edit_name);
        mEditDueDate = findViewById(R.id.edit_date);
        mEditDescription = findViewById(R.id.edit_description);

        mBtnSave.setOnClickListener(c -> saveTask());
        mBtnDelete.setOnClickListener(c -> deleteTask());
        mImgLowPriority.setOnClickListener(c -> lowPriorityAction());
        mImgMediumPriority.setOnClickListener(c -> mediumPriorityAction());
        mImgHighPriority.setOnClickListener(c -> highPriorityAction());
        mEditDueDate.setOnTouchListener((v, event) -> {
            if(mEditDueDate.getText() == null || mEditDueDate.getText().toString().isEmpty()) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();
                mEditDueDate.setText(dtf.format(now));
            }
            return TaskActivity.super.onTouchEvent(event);
        });
        mEditDueDate.addTextChangedListener(Mask.insert("##/##/####", mEditDueDate));
        mBundle = getIntent().getExtras();
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        mBundle.putString(Constants.KEY_BUNDLE_OLD_TASK, "");
    }

    private void configureFields() {
        disableOrEnableViews(false, mBtnDelete);
        configureImgPriority((byte) 0);
        mImgCheck.setColorFilter(UtilsView.getColorToImgCheck(this, false));
        if (mBundle != null) {
            Task myTask = UtilsDao.jsonStringToTask(mBundle.getString(Constants.KEY_BUNDLE_CURRENT_TASK, ""));
            if (myTask != null) {
                configureImgPriority(myTask.getPriority());
                mImgCheck.setColorFilter(UtilsView.getColorToImgCheck(this, myTask.isDone()));
                mEditName.setText(myTask.getName());
                mEditDueDate.setText(myTask.getDueDate());
                mEditDescription.setText(myTask.getDescription().isEmpty() ? "" : myTask.getDescription());
                if (myTask.isDone()) {
                    disableOrEnableViews(false, mBtnDelete, mBtnSave, mEditName, mEditDueDate, mEditDescription, mImgCheck, mImgLowPriority, mImgMediumPriority, mImgHighPriority);
                } else {
                    mBundle.putString(Constants.KEY_BUNDLE_OLD_TASK, UtilsDao.taskToJsonString(myTask));
                    disableOrEnableViews(true, mBtnDelete);
                }
            }
        }
    }

    private void disableOrEnableViews(boolean isEnable, View... fields) {
        for (View v : fields) {
            v.setEnabled(isEnable);
        }
    }

    private void configureImgPriority(byte priority) {
        switch (priority) {
            case 1:
                mImgLowPriority.setColorFilter(this.getResources().getColor(R.color.gold, this.getTheme()));
                break;

            case 2:
                mImgMediumPriority.setColorFilter(this.getResources().getColor(R.color.orange, this.getTheme()));
                break;

            case 3:
                mImgHighPriority.setColorFilter(this.getResources().getColor(R.color.red, this.getTheme()));
                break;

            default:
                mImgLowPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
                mImgMediumPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
                mImgHighPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
                break;
        }
    }

    private void saveTask() {
        if (mEditName.getText() == null || (mEditName.getText() == null ? true : mEditName.getText().toString().isEmpty())) {
            Toast.makeText(this, R.string.msg_name_fail_save_task, Toast.LENGTH_SHORT).show();
        } else if(!isValidDate()) {
            Toast.makeText(this, R.string.msg_date_fail_save_task, Toast.LENGTH_SHORT).show();
        }
        else {
            String myName = mEditName.getText().toString();
            String myDescription = (mEditDescription.getText() == null ? "" : mEditDescription.getText().toString());
            byte myPriority = getPriorityByImg();
            String myDueDate = (mEditDueDate.getText() == null ? "" : mEditDueDate.getText().toString());
            boolean isDone = getCheckByImg();
            Task myTask = new Task(myName, myDescription, myPriority, myDueDate, isDone);
            Intent intent = new Intent();
            mBundle.putString(Constants.KEY_BUNDLE_CURRENT_TASK, UtilsDao.taskToJsonString(myTask));
            intent.putExtras(mBundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void deleteTask() {
        Intent intent = new Intent();
        mBundle.putString(Constants.KEY_BUNDLE_CURRENT_TASK, "");
        intent.putExtras(mBundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isValidDate() {
        boolean isValid = false;
        if(mEditDueDate.getText() == null) {
            isValid = true;
        } else if(mEditDueDate.getText().toString().isEmpty()) {
            isValid = true;
        } else {
            String editTextDate = mEditDueDate.getText().toString();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
            try {
                LocalDate date = LocalDate.parse(editTextDate, dateTimeFormatter);
                isValid = true;
            } catch (DateTimeParseException e) {
                Log.e(TAG, "Erro ao converter data " + editTextDate);
            }
        }

        return isValid;
    }

    private byte getPriorityByImg() {
        byte priority = 0;
        if (mImgLowPriority.getColorFilter().equals(new PorterDuffColorFilter(this.getResources().getColor(R.color.gold, this.getTheme()), PorterDuff.Mode.SRC_ATOP))) {
            priority = 1;
        } else if (mImgMediumPriority.getColorFilter().equals(new PorterDuffColorFilter(this.getResources().getColor(R.color.orange, this.getTheme()), PorterDuff.Mode.SRC_ATOP))) {
            priority = 2;
        } else if (mImgHighPriority.getColorFilter().equals(new PorterDuffColorFilter(this.getResources().getColor(R.color.red, this.getTheme()), PorterDuff.Mode.SRC_ATOP))) {
            priority = 3;
        }

        return priority;
    }

    private boolean getCheckByImg() {
        return mImgCheck.getColorFilter().equals(new PorterDuffColorFilter(this.getResources().getColor(R.color.green, this.getTheme()), PorterDuff.Mode.SRC_ATOP));
    }

    private void lowPriorityAction() {
        if (mImgLowPriority.getColorFilter().equals(new PorterDuffColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()), PorterDuff.Mode.SRC_ATOP))) {
            mImgLowPriority.setColorFilter(this.getResources().getColor(R.color.gold, this.getTheme()));
        } else {
            mImgLowPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
        }
        mImgMediumPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
        mImgHighPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
    }

    private void mediumPriorityAction() {
        if (mImgMediumPriority.getColorFilter().equals(new PorterDuffColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()), PorterDuff.Mode.SRC_ATOP))) {
            mImgMediumPriority.setColorFilter(this.getResources().getColor(R.color.orange, this.getTheme()));
        } else {
            mImgMediumPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
        }
        mImgLowPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
        mImgHighPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
    }

    private void highPriorityAction() {
        if (mImgHighPriority.getColorFilter().equals(new PorterDuffColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()), PorterDuff.Mode.SRC_ATOP))) {
            mImgHighPriority.setColorFilter(this.getResources().getColor(R.color.red, this.getTheme()));
        } else {
            mImgHighPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
        }
        mImgLowPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
        mImgMediumPriority.setColorFilter(this.getResources().getColor(R.color.gray, this.getTheme()));
    }
}