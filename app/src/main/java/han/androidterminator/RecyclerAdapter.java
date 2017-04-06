package han.androidterminator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import han.androidterminator.obj.PhotoObj;
import han.androidterminator.utils.ImageLoaderUtils;
import han.androidterminator.utils.ImageUtils;

/**
 * Created by hs on 2017/3/23.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    List<PhotoObj> list;

    public void setList(List<PhotoObj> objList) {

        for (int i = 0; i < objList.size(); i++) {
            for (int j = i + 1; j < objList.size(); j++) {
                int intTemp = objList.get(i).getTime().compareToIgnoreCase(objList.get(j).getTime());
                PhotoObj strTemp;
                if (intTemp < 0) {

                    Log.e("sssss",objList.get(i).getTime().toString()+"---------"+objList.get(j).getTime());

                    strTemp = objList.get(j);
                    objList.set(j,objList.get(i));
                    objList.set(i,strTemp);
                }
            }
        }
        this.list = objList;
    }

    View view;
    LayoutInflater inflater;
    Context context;


    //点击监听事件

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public RecyclerAdapter(Context context,List<PhotoObj> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.recycler_photo_item,parent ,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getTitle());
        ImageLoaderUtils.displayImage(list.get(position).getImg(),holder.image,false);

        //多选状态
        //判断是否设置了监听器
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
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
        ImageView image;
        TextView name;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.recycler_item_photo);
            name = (TextView) itemView.findViewById(R.id.recycler_item_describe);
        }

    }


}
