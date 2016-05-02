package com.github.sinapple.expenser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.sinapple.expenser.model.TransactionCategory;
import java.util.List;

public class CustomCatListAdapter extends RecyclerView.Adapter<CustomCatListAdapter.ViewHolder>{
    List<TransactionCategory> mCategories;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public TextView mDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView)itemView.findViewById(R.id.title_category);
            mDescription = (TextView)itemView.findViewById(R.id.description_category);
        }
    }
    public CustomCatListAdapter(List<TransactionCategory> categories) {
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
}