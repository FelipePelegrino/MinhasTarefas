package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.R;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.adapter.ItemTaskAdapter;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.contants.Constants;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.controller.CompletedTaskController;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.dao.UtilsDao;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;

public class CompletedTaskActivity extends AppCompatActivity {

    private List<Task> mTasks;
    private ItemTaskAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private CompletedTaskController completedTaskController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
        initVariables();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initVariables() {
        completedTaskController = CompletedTaskController.getInstance(this);
        mTasks = completedTaskController.getAllTasks();

        mAdapter = new ItemTaskAdapter(this, mTasks);
        mRecyclerView = findViewById(R.id.recycler_completed_task);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setImageCheckClickListener(this::checkAction);
        mAdapter.setItemViewClickListener(this::viewTask);
    }

    private void checkAction(int position) {
        Toast.makeText(this, R.string.msg_completed_task_button_check, Toast.LENGTH_SHORT).show();
    }

    private void viewTask(int position) {
        Intent intent = new Intent(this, TaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BUNDLE_CURRENT_TASK, UtilsDao.taskToJsonString(mTasks.get(position)));
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.CODE_VIEW_TASK);
    }
}