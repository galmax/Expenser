package com.github.sinapple.expenser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;
import com.github.sinapple.expenser.model.Wallet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    EditText et_nameTransaction, et_amountTransaction, et_descriptionTransaction;
    TextView tv_dateTransaction;
    Spinner spinner_CategoryTransaction;
    int isExpense;
    Date date;
    List<TransactionCategory> transactionCategories;
    TransactionCategory transactionCategory;
    MoneyTransaction moneyTransactionForEdit;
    String whatDo;
    SimpleDateFormat sdf;
    //get object wallet
    Wallet wallet = Wallet.findById(Wallet.class, 1);
    private boolean isEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        //initialize edit,text,spinner
        et_nameTransaction = (EditText) findViewById(R.id.et_name_transaction);
        et_amountTransaction = (EditText) findViewById(R.id.et_amount_transaction);
        et_descriptionTransaction = (EditText) findViewById(R.id.et_description_transaction);
        tv_dateTransaction = (TextView) findViewById(R.id.tv_date);
        spinner_CategoryTransaction = (Spinner) findViewById(R.id.spinner_category);

        //click on button for set Date
        tv_dateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker();
                datePicker.show(getFragmentManager(), "date_picker");
            }
        });

        //get intent
        Intent intentTransaction = getIntent();
        //get key of MainActivity.class
        whatDo = intentTransaction.getStringExtra("whatDo");
        //get format date
        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        //get object that will change
        moneyTransactionForEdit = MoneyTransaction.findById(MoneyTransaction.class, intentTransaction.getLongExtra("Id", 1));

        switch (whatDo) {
            //if add Expense
            case "addExpense":
                setTitle(getString(R.string.add_expense));
                //get expense`s categories
                transactionCategories = TransactionCategory.find(TransactionCategory.class, "m_expense_category=?", "1");
                isExpense = -1;
                showSpinner(transactionCategories);
                break;
            //if edit Expense
            case "editExpense":
                setTitle(getString(R.string.edit_expense));
                //get expense`s categories
                transactionCategories = TransactionCategory.find(TransactionCategory.class, "m_expense_category=?", "1");
                isExpense = -1;
                showSpinner(transactionCategories);
                dataFillingDb();
                break;
            //if add Income
            case "addIncome":
                setTitle(getString(R.string.add_income));
                //get income`s categories
                transactionCategories = TransactionCategory.find(TransactionCategory.class, "m_expense_category=?", "0");
                isExpense = 1;
                showSpinner(transactionCategories);
                break;
            //if edit Income
            case "editIncome":
                setTitle(getString(R.string.edit_income));
                //get income`s categories
                transactionCategories = TransactionCategory.find(TransactionCategory.class, "m_expense_category=?", "0");
                isExpense = 1;
                showSpinner(transactionCategories);
                dataFillingDb();
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
            isEmpty();
            //checking empty fields
            if (!isEmpty) {
                if (whatDo.equals("addExpense") || whatDo.equals("addIncome")) {
                    saveTransaction();
                } else if (whatDo.equals("editExpense") || whatDo.equals("editIncome")) {
                    editTransaction();
                }
                //return to MainActivity
                Intent intentToMain = new Intent(AddTransactionActivity.this, MainActivity.class);
                startActivity(intentToMain);
            }
            return true;
        } else if (id == R.id.clean_transaction) {
            //clean all fields
            et_nameTransaction.setText("");
            et_amountTransaction.setText("");
            et_descriptionTransaction.setText("");
            tv_dateTransaction.setText("");
            spinner_CategoryTransaction.setSelection(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //fill all fields of the object
    private void dataFillingDb() {
        et_nameTransaction.setText(moneyTransactionForEdit.getName());
        et_amountTransaction.setText(String.valueOf(moneyTransactionForEdit.getAmount()));
        //add money to the balance
        wallet.setBalance(wallet.getBalance() - (Float.valueOf(et_amountTransaction.getText().toString()) * isExpense));

        et_descriptionTransaction.setText(moneyTransactionForEdit.getDescription());
        tv_dateTransaction.setText(sdf.format(moneyTransactionForEdit.getDate()));
        spinner_CategoryTransaction.setSelection(getIndex(moneyTransactionForEdit.getCategory().getName()));
    }

    //save the changed data in transaction
    private void editTransaction() {
        try {
            date = sdf.parse(tv_dateTransaction.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        transactionCategory = transactionCategories.get(spinner_CategoryTransaction.getSelectedItemPosition());
        //add money to the balance
        wallet.setBalance(wallet.getBalance() + (Float.valueOf(et_amountTransaction.getText().toString()) * isExpense));
        wallet.save();
        moneyTransactionForEdit.setAmount((Float.valueOf(et_amountTransaction.getText().toString()) * isExpense));
        moneyTransactionForEdit.setName(et_nameTransaction.getText().toString());
        moneyTransactionForEdit.setDescription(et_descriptionTransaction.getText().toString());
        moneyTransactionForEdit.setCategory(transactionCategory);
        moneyTransactionForEdit.setDate(date);
        moneyTransactionForEdit.save();
    }

    //show categories in the spinner
    private void showSpinner(List<TransactionCategory> categories) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter categoryAdapter = new ArrayAdapter(this, R.layout.spinner, categories);
        spinner_CategoryTransaction.setAdapter(categoryAdapter);
    }

    //save transaction
    private void saveTransaction() {

        try {
            date = sdf.parse(tv_dateTransaction.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        transactionCategory = transactionCategories.get(spinner_CategoryTransaction.getSelectedItemPosition());
        MoneyTransaction moneyTransaction = new MoneyTransaction(et_nameTransaction.getText().toString(), transactionCategory, wallet, et_descriptionTransaction.getText().toString(), (Float.valueOf(et_amountTransaction.getText().toString()) * isExpense), date);
        //add amount money to the balance
        wallet.setBalance(wallet.getBalance() + (Float.valueOf(et_amountTransaction.getText().toString()) * isExpense));
        //save
        wallet.save();
        moneyTransaction.save();
    }

    //get index for edit transaction
    private int getIndex(String category) {
        int index = 0;
        for (int i = 0; i < spinner_CategoryTransaction.getCount(); i++) {
            if (spinner_CategoryTransaction.getItemAtPosition(i).toString().equals(category)) {
                index = i;
            }
        }
        return index;
    }

    //check is empty fields
    private void isEmpty() {
        isEmpty = false;
        if (et_nameTransaction.getText().toString().equals("")) {
            et_nameTransaction.setError(getString(R.string.enter_name));
            isEmpty = true;
        }
        if (et_amountTransaction.getText().toString().equals("")) {
            et_amountTransaction.setError(getString(R.string.enter_amount));
            isEmpty = true;
        }
       if (Float.parseFloat(et_amountTransaction.getText().toString()) == 0) {
            et_amountTransaction.setError(getString(R.string.if_zero_amount));
            isEmpty = true;
        }
        if ((tv_dateTransaction.getText().toString().equals("") && !isEmpty)) {
            String sDate = sdf.format(new Date());
            tv_dateTransaction.setText(sDate);
        }
    }
}
