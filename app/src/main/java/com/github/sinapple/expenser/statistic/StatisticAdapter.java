package com.github.sinapple.expenser.statistic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.sinapple.expenser.R;

import java.util.List;

/**
 * Created by Jarvis on 04.05.2016.
 */
public class StatisticAdapter extends ArrayAdapter {
    Context context;
    LayoutInflater mInflater;
    List<StatisticItem> statisticList;

    //Constructor
    public StatisticAdapter(Context context, int resource, List<StatisticItem> statisticList) {
        super(context, resource, statisticList);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.statisticList = statisticList;
    }

    //Put specific data in the content of a view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row=convertView;

        if(row==null){
            row = mInflater.inflate(R.layout.statistic_item, parent, false);
            holder = new ViewHolder();
            holder.categoryName = (TextView) row.findViewById(R.id.statistic_item_categoryName);
            holder.categoryAmount = (TextView) row.findViewById(R.id.statistic_item_amount);
            row.setTag(holder);
        }
        else{

            holder = (ViewHolder)row.getTag();
        }
        StatisticItem item = statisticList.get(position);
        holder.categoryName.setText(item.getCategory().getName());
        holder.categoryAmount.setText(String.valueOf(item.getAmount()) + " \u20ac");
        holder.categoryName.setTextColor(item.getColor());
        holder.categoryAmount.setTextColor(item.getColor());

        return row;
    }

    private class ViewHolder{
        public TextView categoryName, categoryAmount;
    }
}
