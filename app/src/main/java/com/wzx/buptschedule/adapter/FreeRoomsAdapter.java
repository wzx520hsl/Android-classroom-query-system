package com.wzx.buptschedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzx.buptschedule.R;
import com.wzx.buptschedule.bean.FreeRoom;
import com.wzx.buptschedule.bean.FreeRooms;
import com.wzx.buptschedule.util.LogTrace;

import java.util.List;


public class FreeRoomsAdapter extends BaseExpandableListAdapter {
    @SuppressWarnings("unused")
    private Context context;
    private LayoutInflater inflater;
    private List<FreeRooms> mFreeRoomsList;

    public FreeRoomsAdapter(Context context, List<FreeRooms> freeRoomsList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mFreeRoomsList = freeRoomsList;
        LogTrace.d("FreeRoomsAdapter","FreeRoomsAdapter",""+mFreeRoomsList.size());
    }

    // 返回父列表个数
    @Override
    public int getGroupCount() {
        return mFreeRoomsList.size();
    }

    // 返回子列表个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mFreeRoomsList.get(groupPosition).getRooms().size();
    }

    @Override
    public FreeRooms getGroup(int groupPosition) {
        return mFreeRoomsList.get(groupPosition);
    }

    @Override
    public FreeRoom getChild(int groupPosition, int childPosition) {
        return mFreeRoomsList.get(groupPosition).getRooms().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.exlistview_freerooms_group,
                    null);
            groupHolder.txName = (TextView) convertView
                    .findViewById(R.id.freerooms_time);
            groupHolder.imgHint = (ImageView) convertView
                    .findViewById(R.id.freerooms_hint);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        FreeRooms info = getGroup(groupPosition);
        groupHolder.txName.setText(info.getTime());

        if (isExpanded) {
            groupHolder.imgHint.setImageResource(R.drawable.expanded);
        } else {
            groupHolder.imgHint.setImageResource(R.drawable.collapse);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = inflater.inflate(R.layout.exlistview_freerooms_child,
                    null);
            childHolder.txBuilding = (TextView) convertView
                    .findViewById(R.id.room_building);
            childHolder.txRoomName = (TextView) convertView
                    .findViewById(R.id.room_name);

            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        FreeRoom info = getChild(groupPosition, childPosition);

        childHolder.txBuilding.setText(info.getBuilding());
        childHolder.txRoomName.setText(info.getRoom());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

class GroupHolder {
    TextView txName;
    ImageView imgHint;
}

class ChildHolder {
    TextView txBuilding;
    TextView txRoomName;
}






