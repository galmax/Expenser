package com.github.sinapple.expenser;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;

public class NewCategoryActivity extends AppCompatActivity {
    boolean EDIT_CATEGORY;
    long id;
    EditText new_category_name,new_category_description;
    Spinner new_category_type;
    LinearLayout category_elements;
    TransactionCategory editedTransactionCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        Intent intent=getIntent();
        EDIT_CATEGORY=intent.getExtras().getBoolean("edit_category");
        //initialize views
        new_category_name=(EditText)findViewById(R.id.new_category_name);
        new_category_description=(EditText)findViewById(R.id.new_category_description);
        category_elements=(LinearLayout)findViewById(R.id.category_elements);
        new_category_type=(Spinner)findViewById(R.id.new_category_type);

        if(EDIT_CATEGORY) {
            setTitle("Edit Category");
            //it's impossible to change category type once it has been created, so we hide the spinner and its surrounding elements
            category_elements.setVisibility(View.GONE);
            //get edited category using its id
            id=intent.getExtras().getLong("id");
            editedTransactionCategory = TransactionCategory.findById(TransactionCategory.class, id);
            //fill editTexts with data
            new_category_name.setText(editedTransactionCategory.getName());
            new_category_description.setText(editedTransactionCategory.getDescription());
        } else {
            setTitle("Add Category");
            //set up spinner for choosing category type
            ArrayAdapter categoryTypeAdapter = ArrayAdapter.createFromResource(this,R.array.category_type,R.layout.spinner);
            new_category_type.setAdapter(categoryTypeAdapter);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.submit_category_data) {
            if(isInputValid()) {
                if (EDIT_CATEGORY) {
                    editCategory();
                } else {
                    saveCategory();
                }
                //return to CategoryActivity
                Intent in = new Intent(NewCategoryActivity.this, CategoryActivity.class);
                startActivity(in);
            }
            return true;
        } else if (id == R.id.clear_category_data) {
            //set views' values to default
            new_category_name.setText("");
            new_category_description.setText("");
            new_category_type.setSelection(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void editCategory(){
            editedTransactionCategory.setName(new_category_name.getText().toString());
            editedTransactionCategory.setDescription(new_category_description.getText().toString());
            editedTransactionCategory.save();

    }
    private void saveCategory(){
            //get chosen category type
            String new_category_type_position = String.valueOf(new_category_type.getSelectedItem());
            boolean expenseCategory;
            if (new_category_type_position.equals("Expense")) expenseCategory = true;
            else expenseCategory = false;
            //create new category and save it
            TransactionCategory transactionCategory = new TransactionCategory(new_category_name.getText().toString(), new_category_description.getText().toString(), expenseCategory);
            transactionCategory.save();

    }
    private boolean isInputValid(){
        // check if all inputs are filled out
        if(isEmpty(new_category_name) || isEmpty(new_category_description)) {
            Snackbar.make(findViewById(R.id.new_category_layout), "All fields must be filled out!", Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
    //check if EditText is empty
    private boolean isEmpty(EditText t){
        return t.getText().toString().trim().length()==0;
    }
}
