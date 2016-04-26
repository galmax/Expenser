package com.github.sinapple.expenser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AddTransactionActivity extends AppCompatActivity {

    Button bt_setDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        //button for set Date
        bt_setDate = (Button) findViewById(R.id.bt_set_date);
        bt_setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker();
                datePicker.show(getFragmentManager(), "date_picker");
            }
        });
        //get intent
        Intent intentTransaction = getIntent();
        //get key of MainActivity.class
        String whatDo = intentTransaction.getStringExtra("whatDo");
        switch (whatDo) {
            case "addExpense":
                setTitle("Add Expense");
                break;
            case "editExpense":
                setTitle("Edit Expense");
                break;
            case "addIncome":
                setTitle("Add Income");
                break;
            case "editIncome":
                setTitle("Edit Income");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_transaction) {
            return true;
        } else if (id == R.id.clean_transaction) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
