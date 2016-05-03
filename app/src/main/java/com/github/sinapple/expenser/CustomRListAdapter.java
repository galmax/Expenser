package com.github.sinapple.expenser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.Wallet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter is used to generate views with transaction info for RecyclerView
 */
public class CustomRListAdapter extends RecyclerView.Adapter<CustomRListAdapter.ViewHolder> implements RecyclerViewItemCallback.ItemTouchHelperAdapter, RecycleItemClickListener.OnItemClickListener{
    private List<MoneyTransaction> mTransactions;
    AppCompatActivity appC;
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
    public CustomRListAdapter(AppCompatActivity appC, List<MoneyTransaction> transactions) {
        mTransactions = transactions;
        this.appC=appC;
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
        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        if (dateFormat.format(item.getDate()) == dateFormat.format(new Date())){
            dateFormat = new SimpleDateFormat("d MMM", Locale.getDefault());
            dateString = dateFormat.format(item.getDate());
        } else {
            dateFormat = new SimpleDateFormat("HH':'mm", Locale.getDefault());
            dateString = dateFormat.format(item.getDate());
        }
        holder.mDate.setText(dateString);
    }

    //Return the size of your data set
    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    //Remove some item. Usually called when the swipe gesture has been performed.
    @Override
    public void onItemDismiss(View messageOutput, int position){
        final TextView txtView=(TextView) appC.findViewById(R.id.tv_balance);
        final int p = position;
        final MoneyTransaction x = mTransactions.remove(position);
        final Wallet w = Wallet.findById(Wallet.class, 1);
        //balance before
        final float balanceBefore=w.getBalance();
        //balance after
        final float balanceAfter;
        if (x.isExpense()) balanceAfter=balanceBefore+x.getAmount(); else balanceAfter=balanceBefore-x.getAmount();
        txtView.setText(Float.toString(balanceAfter));
        w.setBalance(balanceAfter);
        w.save();
        notifyDataSetChanged();
        Snackbar.make(messageOutput, R.string.message_transaction_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.cancel, new View.OnClickListener() {
                    //Return transaction in the list when user have canceled removing and restore the amount of money in the wallet
                    @Override
                    public void onClick(View v) {
                        txtView.setText(Float.toString(balanceBefore));
                        w.setBalance(balanceBefore);
                        w.save();
                        mTransactions.add(p, x);
                        notifyItemInserted(p);
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    //Final removing the transaction and changing of money amount
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            txtView.setText(Float.toString(balanceAfter));
                            w.setBalance(balanceAfter);
                            w.save();
                            x.delete();
                        }
                        super.onDismissed(snackbar, event);
                    }
                }).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent editIntent = new Intent(view.getContext(), AddTransactionActivity.class);
        editIntent.putExtra("whatDo", "editExpense");
        editIntent.putExtra("Id", mTransactions.get(position).getId());
        editIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appC.startActivity(editIntent);
    }
}
