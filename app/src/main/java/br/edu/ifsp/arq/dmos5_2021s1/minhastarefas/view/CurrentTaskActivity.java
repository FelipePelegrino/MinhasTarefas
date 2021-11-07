package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.R;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.adapter.ItemTaskAdapter;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.contants.Constants;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.controller.CurrentTaskController;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao.UtilsDao;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class CurrentTaskActivity extends AppCompatActivity {

    private List<Task> mTasks;
    private ItemTaskAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private CurrentTaskController currentTaskController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_task);
        initVariables();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CODE_NEW_TASK:
                    newTask(data.getExtras());
                    break;
                case Constants.CODE_SAVE_TASK:
                    editTask(data.getExtras());
                    break;
            }
            updateAdapter();
        }
    }

    private void initVariables() {
        currentTaskController = CurrentTaskController.getInstance(this);
        mTasks = currentTaskController.getAllTasks();

        mAdapter = new ItemTaskAdapter(this, mTasks);
        mRecyclerView = findViewById(R.id.recycler_current_task);
        mFab = findViewById(R.id.fab_add_task);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setImageCheckClickListener(this::finishTask);
        mAdapter.setItemViewClickListener(this::actionEditTask);
        mFab.setOnClickListener(v -> actionNewTask());
    }

    private void finishTask(int position) {
        if (currentTaskController.setTaskDone(mTasks.get(position))) {
            Toast.makeText(this, "Tarefa concluída!", Toast.LENGTH_SHORT).show();
            updateAdapter();
        } else {
            Toast.makeText(this, "Ocorreu algum erro ao finalizar a tarefa.", Toast.LENGTH_SHORT).show();
        }
    }

    private void actionNewTask() {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivityForResult(intent, Constants.CODE_NEW_TASK);
    }

    private void actionEditTask(int position) {
        Intent intent = new Intent(this, TaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BUNDLE_CURRENT_TASK, UtilsDao.taskToJsonString(mTasks.get(position)));
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.CODE_SAVE_TASK);
    }

    private void updateAdapter() {
        mTasks = currentTaskController.getAllTasks();
        mAdapter.notifyDataSetChanged();
    }

    private void newTask(Bundle b) {
        int resourceMsg;
        String jsonReturn = b.getString(Constants.KEY_BUNDLE_CURRENT_TASK, "");
        resourceMsg = CurrentTaskController.newTask(UtilsDao.jsonStringToTask(jsonReturn)) ? R.string.msg_succes_new_task : R.string.msg_fail_new_task;
        Toast.makeText(this, resourceMsg, Toast.LENGTH_SHORT).show();
    }

    private void editTask(Bundle b) {
        int resourceMsg = R.string.msg_update_fail;
        String jsonCurrentTask = b.getString(Constants.KEY_BUNDLE_CURRENT_TASK, "");
        String jsonOldTask = b.getString(Constants.KEY_BUNDLE_OLD_TASK, "");
        if (jsonCurrentTask.isEmpty()) {
            Task myOldTask = UtilsDao.jsonStringToTask(jsonOldTask);
            if (CurrentTaskController.deleteTask(myOldTask)) {
                resourceMsg = R.string.msg_delete_succes;
            } else {
                resourceMsg = R.string.msg_delete_fail;
            }
        } else if (!jsonCurrentTask.isEmpty() && !jsonOldTask.isEmpty()) {
            Task myOldTask = UtilsDao.jsonStringToTask(jsonOldTask);
            Task myCurrentTask = UtilsDao.jsonStringToTask(jsonCurrentTask);
            if (CurrentTaskController.updateTask(myOldTask, myCurrentTask)) {
                resourceMsg = R.string.msg_update_succes;
            } else {
                resourceMsg = R.string.msg_update_fail;
            }
        }

        Toast.makeText(this, resourceMsg, Toast.LENGTH_SHORT).show();
    }

}