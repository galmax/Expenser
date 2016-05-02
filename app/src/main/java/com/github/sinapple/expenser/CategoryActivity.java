package com.github.sinapple.expenser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.github.sinapple.expenser.model.TransactionCategory;

public class CategoryActivity extends AppCompatActivity {
TextView incomeListLabel,expenseListLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("All categories");
        incomeListLabel=(TextView)findViewById(R.id.incomeListLabel);
        expenseListLabel=(TextView)findViewById(R.id.expenseListLabel);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CategoryActivity.this, NewCategoryActivity.class);
                in.putExtra("edit_category", false);
                startActivity(in);
            }
        });

        //set up adapters and listeners for income and expense lists
        CustomCatListAdapter expenseListAdapter = new CustomCatListAdapter(getApplicationContext(),TransactionCategory.find(TransactionCategory.class, "m_expense_category = ?", "1"));
        CustomCatListAdapter incomeListAdapter = new CustomCatListAdapter(getApplicationContext(),TransactionCategory.find(TransactionCategory.class,"m_expense_category = ?","0"));
        RecycleItemClickListener expClickListener = new RecycleItemClickListener(this.getApplicationContext(), expenseListAdapter);
        RecycleItemClickListener incClickListener = new RecycleItemClickListener(this.getApplicationContext(), incomeListAdapter);

        RecyclerView expenseCategoryList = (RecyclerView)findViewById(R.id.expenseList);
        expenseCategoryList.setLayoutManager(new LinearLayoutManager(this));
        expenseCategoryList.setAdapter(expenseListAdapter);
        RecyclerView incomeCategoryList = (RecyclerView)findViewById(R.id.incomeList);
        incomeCategoryList.setLayoutManager(new LinearLayoutManager(this));
        incomeCategoryList.setAdapter(incomeListAdapter);

        expenseCategoryList.addOnItemTouchListener(expClickListener);
        ItemTouchHelper.Callback callbackExp = new RecyclerViewItemCallback(expenseListAdapter, ContextCompat.getColor(this, R.color.colorAccent));
        ItemTouchHelper helperExp = new ItemTouchHelper(callbackExp);
        helperExp.attachToRecyclerView(expenseCategoryList);

        incomeCategoryList.addOnItemTouchListener(incClickListener);
        ItemTouchHelper.Callback callbackInc = new RecyclerViewItemCallback(incomeListAdapter, ContextCompat.getColor(this, R.color.colorAccent));
        ItemTouchHelper helperInc = new ItemTouchHelper(callbackInc);
        helperInc.attachToRecyclerView(incomeCategoryList);

        //check the total number of items in lists and change the textViews accordingly
        if (expenseListAdapter.getItemCount()==0 && incomeListAdapter.getItemCount()==0) {
            expenseListLabel.setText("There are no categories to display. Please press the \"+\" button to add a new category.");
            //don't display the label below
            incomeListLabel.setVisibility(View.GONE);
        } else if (expenseListAdapter.getItemCount()==0){
            expenseListLabel.setText("There are no expense categories to display. Please press the \"+\" button to add a new category.");
        } else if (incomeListAdapter.getItemCount()==0){
            incomeListLabel.setText("There are no income categories to display. Please press the \"+\" button to add a new category.");
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;}

}
