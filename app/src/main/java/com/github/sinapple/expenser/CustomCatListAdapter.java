package com.github.sinapple.expenser;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.sinapple.expenser.model.Wallet;
import com.github.sinapple.expenser.model.TransactionCategory;
import com.github.sinapple.expenser.model.MoneyTransaction;
import java.util.List;

public class CustomCatListAdapter extends RecyclerView.Adapter<CustomCatListAdapter.ViewHolder> implements RecyclerViewItemCallback.ItemTouchHelperAdapter, RecycleItemClickListener.OnItemClickListener{
    List<TransactionCategory> mCategories;
    Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public TextView mDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.title_category);
            mDescription = (TextView)itemView.findViewById(R.id.description_category);
        }
    }
    public CustomCatListAdapter(Context activityContext,List<TransactionCategory> categories) {
        mContext = activityContext;
        mCategories = categories;
    }

    //Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view_item, parent, false);
        return new ViewHolder(itemLayout);
    }

    //Put specific data in the content of a view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TransactionCategory item = mCategories.get(position);
        holder.mTitle.setText(item.getName());
        holder.mDescription.setText(item.getDescription());
    }
    public int getItemCount() {
        return mCategories.size();
    }

    //Remove some item. Usually called when the swipe gesture has been performed.
    @Override
    public void onItemDismiss(View messageOutput, int position){
        final int p = position;
        final TransactionCategory x = mCategories.remove(position);
        final List<MoneyTransaction> mTransactions = MoneyTransaction.find(MoneyTransaction.class, "m_category = ?", x.getId().toString());
        final Wallet w = Wallet.findById(Wallet.class, 1);
        notifyDataSetChanged();
        Snackbar.make(messageOutput, R.string.message_category_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.cancel, new View.OnClickListener() {
                    //Return deleted category to the list if the user has canceled removing
                    @Override
                    public void onClick(View v) {
                        mCategories.add(p, x);
                        notifyItemInserted(p);
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    //Final removing of the category and all transactions in it
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            x.delete();
                            for (MoneyTransaction m : mTransactions) {
                                //change balance
                                if (m.isExpense()) {
                                    w.setBalance(w.getBalance() + m.getAmount());
                                } else w.setBalance(w.getBalance() - m.getAmount());
                                w.save();
                                m.delete();
                            }
                        }
                        super.onDismissed(snackbar, event);
                    }
                }).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent editIntent = new Intent(view.getContext(), NewCategoryActivity.class);
        editIntent.putExtra("edit_category", true);
        editIntent.putExtra("id", mCategories.get(position).getId());
        editIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(editIntent);
    }
}