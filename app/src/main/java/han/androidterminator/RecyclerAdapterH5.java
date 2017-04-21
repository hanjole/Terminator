package han.androidterminator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import han.androidterminator.obj.CollectionObj;
import han.androidterminator.obj.WordObj;

/**
 * Created by hs on 2017/4/11.
 */

public class RecyclerAdapterH5 extends BaseRecyclerAdapter {


    boolean deleteShow = false;

    List<CollectionObj> list;
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

    public RecyclerAdapterH5(Context context, List list) {
        inflater = LayoutInflater.from(context);
        super.setList(list);
    }

    @Override
    public long getItemId(int position) {


        return super.getItemId(position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.recycler_url_item, parent, false);
        return new ItemViewHolders(view);
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final ItemViewHolders holders = (ItemViewHolders) holder;


        if(list.get(position)!=null&&list.get(position).name!=null){
            holders.name.setText(list.get(position).name);

        }

        if(list.get(position)!=null&&list.get(position).url!=null){
            holders.url.setText(list.get(position).url);

        }
        if(deleteShow){

            holders.delete.setVisibility(View.VISIBLE);
            holders.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.e("remove","getAdapterPosition"+holders.getAdapterPosition()+"-----"+position);
                    list.remove(holders.getAdapterPosition());
                    notifyItemRemoved(position);
//                    list.remove(position);
//                    notifyDataSetChanged();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long startTime =   System.currentTimeMillis();
                            new JsonUtils(Constant.SO_WORD).remove(holders.name.getText().toString());
                            System.out.println(System.currentTimeMillis()-startTime);
                        }
                    }).start();
                }
            });
        }else {
            holders.delete.setVisibility(View.GONE);
        }




    }


    @Override
    public int getItemCount() {
//        Log.e("getItemCount",super.getItemCount()+"");
        return super.getItemCount();
    }

    class ItemViewHolders extends ItemViewHolder {
        View itemView;
        TextView name;
        TextView url;
        ImageView delete;
        public ItemViewHolders(View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = (TextView) itemView.findViewById(R.id.recyler_item_name);
            url= (TextView) itemView.findViewById(R.id.recyler_item_url);
            delete = (ImageView) itemView.findViewById(R.id.recycler_item_delete);
        }

    }

}
