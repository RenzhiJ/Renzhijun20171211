package rxjava.bwei.com.renzhijun20171211.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rxjava.bwei.com.renzhijun20171211.R;
import rxjava.bwei.com.renzhijun20171211.bean.CartBean;
import rxjava.bwei.com.renzhijun20171211.bean.PriceAndCountEvent;

/**
 * Created by Zhijun on 2017/12/11.
 */

public class MyAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<CartBean.DataBean> groupList;
    private List<List<CartBean.DataBean.ListBean>> childList;
    public MyAdapter(Context context, List<CartBean.DataBean> groupList, List<List<CartBean.DataBean.ListBean>> childList) {
        this.context =context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        final GroupViewHolder holder;
        if (view == null) {
            holder = new GroupViewHolder();
            view = view.inflate(context, R.layout.item_parent_market, null);
            holder.cbGroup = view.findViewById(R.id.cb_parent);
            holder.tv_number = view.findViewById(R.id.tv_number);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        final CartBean.DataBean dataBean = groupList.get(i);
//        holder.cbGroup.setChecked(dataBean.isChecked());
//        holder.tv_number.setText(dataBean.getTitle());
        holder.tv_number.setText(dataBean.getSellerName());
        //一级checkbox
        holder.cbGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dataBean.setChecked(holder.cbGroup.isChecked());
                changeChildCbState(i, holder.cbGroup.isChecked());
                EventBus.getDefault().post(compute());
                changeAllCbState(isAllGroupCbSelected());
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final ChildViewHolder holder;
        if (view == null) {
            holder = new ChildViewHolder();
            view = view.inflate(context,R.layout.item_child_market, null);
            holder.cbChild = view.findViewById(R.id.cb_child);
            holder.tv_tel = view.findViewById(R.id.tv_tel);
//            holder.tv_content = view.findViewById(R.id.tv_content);
//            holder.tv_time = view.findViewById(R.id.tv_time);
            holder.imgIcon = view.findViewById(R.id.img_icon);
            holder.tv_price = view.findViewById(R.id.tv_pri);
            holder.tv_del = view.findViewById(R.id.tv_del);
            holder.iv_add = view.findViewById(R.id.iv_add);
            holder.iv_del = view.findViewById(R.id.iv_del);
            holder.tv_num = view.findViewById(R.id.tv_num);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }
        final CartBean.DataBean.ListBean datasBean = childList.get(i).get(i1);

//        holder.cbChild.setChecked(datasBean.isChecked());
//        holder.tv_tel.setText(datasBean.getType_name());
        holder.tv_tel.setText(datasBean.getTitle());
//        holder.tv_content.setText(datasBean.getMsg());
//        holder.tv_time.setText(datasBean.getAdd_time());
        holder.tv_price.setText("￥"+datasBean.getPrice() );
        holder.tv_num.setText(datasBean.getNum() + "");
        String images = datasBean.getImages().trim();
        String[] split = images.split("[|]");
        Glide.with(context).load(split[0]).into(holder.imgIcon);

        //二级checkbox
        holder.cbChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置该条目对象里的checked属性值
//                datasBean.setChecked(holder.cbChild.isChecked());
                PriceAndCountEvent priceAndCountEvent = compute();
                EventBus.getDefault().post(priceAndCountEvent);

                if (holder.cbChild.isChecked()) {
                    //当前checkbox是选中状态
                    if (isAllChildCbSelected(i)) {
                        changGroupCbState(i, true);
                        changeAllCbState(isAllGroupCbSelected());
                    }
                } else {
                    changGroupCbState(i, false);
                    changeAllCbState(isAllGroupCbSelected());
                }
                notifyDataSetChanged();
            }


        });
        //加号
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = datasBean.getNum();
                holder.tv_num.setText(++num + "");
                datasBean.setNum(num);
                if (holder.cbChild.isChecked()) {
                    EventBus.getDefault().post(compute());
                }
            }
        });
        //减号
        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = datasBean.getNum();
                if (num == 1) {
                    return;
                }
                holder.tv_num.setText(--num + "");
                datasBean.setNum(num);
                if (holder.cbChild.isChecked()) {

                    EventBus.getDefault().post(compute());
                }
            }
        });
        //删除
        holder.tv_del.setOnClickListener(new View.OnClickListener() {

            private AlertDialog dialog;

            @Override
            public void onClick(View v) {
                final List<CartBean.DataBean.ListBean> datasBeen = childList.get(i);


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("确认是否删除？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        CartBean.DataBean.ListBean remove = datasBeen.remove(i1);
                        if (datasBeen.size() == 0) {
                            childList.remove(i);
                            groupList.remove(i);
                        }
                        EventBus.getDefault().post(compute());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();

            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    class GroupViewHolder {
        CheckBox cbGroup;
        TextView tv_number;
    }

    class ChildViewHolder {
        CheckBox cbChild;
        TextView tv_tel;
        TextView tv_content;
        TextView tv_time;
        ImageView imgIcon;
        TextView tv_price;
        TextView tv_del;
        ImageView iv_del;
        ImageView iv_add;
        TextView tv_num;
    }
    /**
     * 改变全选的状态
     *
     * @param flag
     */
    private void changeAllCbState(boolean flag) {
//        MessageEvent messageEvent = new MessageEvent();
//        messageEvent.setChecked(flag);
//        EventBus.getDefault().post(messageEvent);
    }
    /**
     * 改变一级列表checkbox状态
     *
     * @param groupPosition
     */
    private void changGroupCbState(int groupPosition, boolean flag) {
//        GoosBean.DataBean dataBean = groupList.get(groupPosition);
        CartBean.DataBean dataBean = groupList.get(groupPosition);

//        dataBean.setChecked(flag);
    }

    /**
     * 改变二级列表checkbox状态
     *
     * @param groupPosition
     * @param flag
     */
    private void changeChildCbState(int groupPosition, boolean flag) {
        List<CartBean.DataBean.ListBean> datasBeen = childList.get(groupPosition);

        for (int i = 0; i < datasBeen.size(); i++) {
            CartBean.DataBean.ListBean datasBean = datasBeen.get(i);
//            datasBean.setChecked(flag);
        }
    }
    /**
     * 判断一级列表是否全部选中
     *
     * @return
     */
    private boolean isAllGroupCbSelected() {
        for (int i = 0; i < groupList.size(); i++) {
            CartBean.DataBean dataBean = groupList.get(i);

//            if (!dataBean.isChecked()) {
//                return false;
//            }
        }
        return true;
    }

    /**
     * 判断二级列表是否全部选中
     *
     * @param groupPosition
     * @return
     */
    private boolean isAllChildCbSelected(int groupPosition) {
        List<CartBean.DataBean.ListBean> datasBeen = childList.get(groupPosition);

        for (int i = 0; i < datasBeen.size(); i++) {
            CartBean.DataBean.ListBean datasBean = datasBeen.get(i);

//            if (!datasBean.isChecked()) {
//                return false;
//            }
        }
        return true;
    }
    /**
     * 计算列表中，选中的钱和数量
     */
    private PriceAndCountEvent compute() {
        int count = 0;
        int price = 0;
        for (int i = 0; i < childList.size(); i++) {
            List<CartBean.DataBean.ListBean> datasBeen = childList.get(i);

            for (int j = 0; j < datasBeen.size(); j++) {
                CartBean.DataBean.ListBean datasBean = datasBeen.get(j);

//                if (datasBean.isChecked()) {
//                    price += datasBean.getNum() * datasBean.getPrice();
//                    count += datasBean.getNum();
//                }
            }
        }
        PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
        priceAndCountEvent.setCount(count);
        priceAndCountEvent.setPrice(price);
        return priceAndCountEvent;
    }
    /**
     * 设置全选、反选
     *
     * @param flag
     */
    public void changeAllListCbState(boolean flag) {
        for (int i = 0; i < groupList.size(); i++) {
            changGroupCbState(i, flag);
            changeChildCbState(i, flag);
        }
        EventBus.getDefault().post(compute());
        notifyDataSetChanged();
    }
}