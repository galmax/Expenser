package com.github.sinapple.expenser;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.Wallet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter is used to generate views with transaction info for RecyclerView
 */
public class CustomRListAdapter extends RecyclerView.Adapter<CustomRListAdapter.ViewHolder>{
    private List<MoneyTransaction> mTransactions;

    //Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public TextView mCategory;
        public TextView mDescription;
        public TextView mAmount;
        public TextView mDate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setLongClickable(true);
            mTitle = (TextView)itemView.findViewById(R.id.title);
            mCategory = (TextView)itemView.findViewById(R.id.category);
            mDescription = (TextView)itemView.findViewById(R.id.description);
            mAmount = (TextView)itemView.findViewById(R.id.amount);
            mDate = (TextView)itemView.findViewById(R.id.date);
        }

    }

    //Suitable constructor
    public CustomRListAdapter(List<MoneyTransaction> transactions) {
        mTransactions = transactions;
    }

    //Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(itemLayout);
    }

    //Put specific data in the content of a view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MoneyTransaction item = mTransactions.get(position);
        holder.mTitle.setText(item.getName());
        holder.mCategory.setText(item.getCategory().toString());
        holder.mDescription.setText(item.getDescription());
        holder.mAmount.setText(String.format("%.2f %s", item.getAmount(), item.getWallet().getCurrency().toString()));
        String dateString;
        DateFormat dateFormat = new SimpleDateFormat("HH':'mm", Locale.getDefault());
        dateString = dateFormat.format(item.getDate());
        holder.mDate.setText(dateString);
    }

    //Return the size of your data set
    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

}
