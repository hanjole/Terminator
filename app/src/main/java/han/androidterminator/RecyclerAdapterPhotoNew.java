package han.androidterminator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import han.androidterminator.obj.PhotoObj;
import han.androidterminator.utils.ImageLoaderUtils;

/**
 * Created by hs on 2017/4/11.
 */

public class RecyclerAdapterPhotoNew extends BaseRecyclerAdapter {

    List<PhotoObj> list;
    public void setList(List objList) {
        super.setList(objList);
        this.list = objList;
    }

    View view;
    LayoutInflater inflater;
    Context context;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        super.setOnItemClickListener(mOnItemClickListener);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        super.setOnItemLongClickListener(mOnItemLongClickListener);
    }

    public RecyclerAdapterPhotoNew(Context context, List list) {
        inflater = LayoutInflater.from(context);
        super.setList(list);
    }

    @Override
    public long getItemId(int position) {


        return super.getItemId(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.recycler_photo_item, parent, false);
        return new ItemViewHolders(view);
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemViewHolders holders = (ItemViewHolders) holder;

        holders.name.setText(list.get(position).getTitle());
        ImageLoaderUtils.displayImage(list.get(position).getImg(), holders.image, false);

    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class ItemViewHolders extends ItemViewHolder {
        View itemView;
        ImageView image;
        TextView name;

        public ItemViewHolders(View itemView) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.recycler_item_photo);
            name = (TextView) itemView.findViewById(R.id.recycler_item_describe);
        }

    }

}
