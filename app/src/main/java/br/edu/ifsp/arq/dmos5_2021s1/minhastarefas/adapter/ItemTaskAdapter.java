package br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.R;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.model.Task;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view.RecyclerItemTaskClickListener;
import br.edu.ifsp.arq.dmos5_2021s1.minhastarefas.view.UtilsView;

public class ItemTaskAdapter extends RecyclerView.Adapter<ItemTaskAdapter.ViewHolder> {

    private Context mContext;
    private List<Task> mTaskList;
    private static RecyclerItemTaskClickListener mImageCheckListener;
    private static RecyclerItemTaskClickListener mItemViewListener;

    public ItemTaskAdapter(Context context, List<Task> taskList) {
        this.mContext = context;
        this.mTaskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemTaskAdapter.ViewHolder holder, int position) {
        holder.mTextName.setText(mTaskList.get(position).getName());
        holder.mTextDate.setText(mTaskList.get(position).getDueDate());
        holder.mImagePriority.setColorFilter(UtilsView.getColorToImgPriority(mContext, mTaskList.get(position).getPriority()));
        holder.mImageCheck.setColorFilter(UtilsView.getColorToImgCheck(mContext, mTaskList.get(position).isDone()));
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public void setImageCheckClickListener(RecyclerItemTaskClickListener listener) {
        ItemTaskAdapter.mImageCheckListener = listener;
    }

    public void setItemViewClickListener(RecyclerItemTaskClickListener listener) {
        ItemTaskAdapter.mItemViewListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImagePriority;
        public ImageView mImageCheck;
        public TextView mTextDate;
        public TextView mTextName;
        public View mItemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImagePriority = itemView.findViewById(R.id.img_priority);
            mImageCheck = itemView.findViewById(R.id.img_check);
            mTextDate = itemView.findViewById(R.id.text_date);
            mTextName = itemView.findViewById(R.id.text_name);
            mItemView = itemView;
            mImageCheck.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == mImageCheck) {
                if (mImageCheckListener != null) {
                    mImageCheckListener.onItemClick(getAdapterPosition());
                }
            }
            else if (v == mItemView) {
                if(mItemViewListener != null) {
                    mItemViewListener.onItemClick(getAdapterPosition());
                }
            }
        }
    }

}
