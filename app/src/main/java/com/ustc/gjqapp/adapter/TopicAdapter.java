package com.ustc.gjqapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ustc.gjqapp.R;
import com.ustc.gjqapp.activity.TopicActivity;
import com.ustc.gjqapp.config.APIAddress;
import com.ustc.gjqapp.util.TimeUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by 灿斌 on 5/14/2015.
 */
public class TopicAdapter extends RecyclerView.Adapter{
    private Context context;
    private LayoutInflater layoutInflater;

    //回调接口
    public interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = TopicAdapter.class.getSimpleName();
    private List<Map<String,Object>> list;
    public TopicAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public void setData(List<Map<String,Object>> list) {
        this.list = list;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.item_topic_list, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new topicViewHolder(view);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final topicViewHolder holder = (topicViewHolder) viewHolder;
        holder.position = i;
        Map<String,Object> topic = list.get(i);
        holder.Title.setText(Html.fromHtml(topic.get("Topic").toString()).toString());
        holder.Description.setText(topic.get("UserName").toString() + " · " + topic.get("LastName").toString());
        holder.Time.setText(TimeUtil.formatTime(context, Long.parseLong(topic.get("LastTime").toString())));
        Glide.with(context).load(APIAddress.MIDDLE_AVATAR_URL(topic.get("UserID").toString(), "middle")).into(holder.Avatar);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class topicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public View rootView;
        ImageView Avatar;
        TextView Title;
        TextView Description;
        TextView Time;
        public int position;

        public topicViewHolder(View itemView) {
            super(itemView);

            Title = (TextView) itemView.findViewById(R.id.title);
            Description = (TextView) itemView.findViewById(R.id.description);
            Time = (TextView) itemView.findViewById(R.id.time);
            Avatar = (ImageView)itemView.findViewById(R.id.avatar);

            rootView = itemView.findViewById(R.id.topic_item);
            //rootView.setClickable(true);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }
        @Override
        //点击事件
        public void onClick(View v) {
            //Toast.makeText(context, "onItemClick" + list.get(position).get("Topic").toString(), Toast.LENGTH_SHORT).show();
            //v.getBackground().setColorFilter(Color.parseColor("#90A4AE"), PorterDuff.Mode.DARKEN);
            Intent intent = new Intent(context, TopicActivity.class);
            intent.putExtra("Topic", list.get(position).get("Topic").toString());
            intent.putExtra("TopicID", list.get(position).get("ID").toString());
            intent.putExtra("TargetPage", "1");
            context.startActivity(intent);
            //if (null != onRecyclerViewListener) {
                //onRecyclerViewListener.onItemClick(position);
            //}
        }


        @Override
        //长按事件
        public boolean onLongClick(View v) {
            if(null != onRecyclerViewListener){
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }
}
