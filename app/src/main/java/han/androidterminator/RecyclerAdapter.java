package han.filemanage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import han.filemanage.object.FileObj;

/**
 * Created by hs on 2017/3/23.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    public void setFileObjList(List<FileObj> fileObjList) {
        this.list = fileObjList;
    }
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemCheckBoxClickListener mOnItemCheckBoxClickListener;
    List<FileObj> list;
    View view;
    LayoutInflater inflater;
    Context context;
    boolean isDuoxuan = false;

    boolean isCheckBoxShow;

    //点击监听事件

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }


    public interface OnItemCheckBoxClickListener{
        void onItemCheckBoxClick(View view, int position,boolean f);
    }

    public void setOnItemCheckBoxClickListener(OnItemCheckBoxClickListener mOnItemCheckBoxClickListener){
        this.mOnItemCheckBoxClickListener = mOnItemCheckBoxClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public RecyclerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view  = inflater.inflate(R.layout.recycler_item,null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.fileName.setText(list.get(position).fileName);
        holder.fileCount.setText(list.get(position).fileCount==null?"":list.get(position).fileCount);
        holder.fileTime.setText(list.get(position).fileTime);
        //多选状态
        if(isCheckBoxShow){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
            if(list.get(position).isSelect){
                holder.checkBox.setChecked(true);
            }else {
                holder.checkBox.setChecked(false);
            }
        }else {
            holder.checkBox.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
        }
        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
        if(mOnItemCheckBoxClickListener != null){
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = holder.getLayoutPosition();
                    mOnItemCheckBoxClickListener.onItemCheckBoxClick(holder.checkBox,position,isChecked);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return null != list ? list.size() : 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView icon_image;
        TextView fileName;
        TextView fileCount;
        TextView fileTime;
        TextView right;
        CheckBox checkBox;
        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            icon_image = (ImageView)itemView.findViewById(R.id.file_ico_iv);
            fileName = (TextView)itemView.findViewById(R.id.file_name_tv);
            fileCount = (TextView)itemView.findViewById(R.id.file_count_tv);
            fileTime = (TextView)itemView.findViewById(R.id.file_time_tv);
            right = (TextView)itemView.findViewById(R.id.file_right_tv);
            checkBox = (CheckBox)itemView.findViewById(R.id.selects_rb);
        }
        public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (isVisible){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            }else{
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

    }
}
