package com.github.sinapple.expenser;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.github.sinapple.expenser.model.Currency;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;
import com.github.sinapple.expenser.model.Wallet;
import com.github.sinapple.expenser.statistic.StatisticActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener
{
    public static final String IS_EXPENSE_LIST = "com.github.sinapple.expenser.IS_EXPENSE_LIST";

    private List<MoneyTransaction> mTransactionList;
    private CustomRListAdapter recyclerAdapter;
    private Wallet mWallet;
    private Calendar mFirstDate;
    private Calendar mSecondDate;
    private boolean mIsExpenseActivity;

    private FloatingActionButton fab;
    private TextView tv_balance;
    private TextView dateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to AddTransactionsActivity
                Intent intentToTransaction = new Intent(MainActivity.this, AddTransactionActivity.class);
                //get Expense categories
                List<TransactionCategory> transactionCategories = TransactionCategory.find(TransactionCategory.class, "m_expense_category=?", "1");
                if (transactionCategories.size() != 0) {
                    //send key to AddTransactionActivity.class
                    intentToTransaction.putExtra("whatDo", mIsExpenseActivity?"addExpense":"addIncome");
                    intentToTransaction.putExtra("Id", 1);
                    startActivity(intentToTransaction);
                } else {
                    Snackbar.make(view, R.string.null_expense, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                tv_balance.setText(Float.toString(recyclerAdapter.getCurrentBalance()));
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Receive data have been sent to this activity and change UI
        mIsExpenseActivity = getIntent().getBooleanExtra(IS_EXPENSE_LIST, true);


        //initializeDB();
        //Write balance in nav_header_main
        View header = navigationView.getHeaderView(0);
        tv_balance = (TextView) header.findViewById(R.id.tv_balance);
        TextView tv_sign = (TextView) header.findViewById(R.id.tv_sign);
        mWallet = Wallet.getCurrentWallet();
        tv_balance.setText(Float.toString(mWallet.getBalance()));
        tv_sign.setText(mWallet.getCurrency().getSign());

        //Initialize date
        mFirstDate = Calendar.getInstance(Locale.getDefault());
        dateField = (TextView)findViewById(R.id.date);
        setDateFieldValue();

        //Initialize RecyclerList
        mTransactionList = MoneyTransaction.findTransactionByDate(mFirstDate, mIsExpenseActivity);
        RecyclerView list = (RecyclerView) findViewById(R.id.transaction_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new CustomRListAdapter(mTransactionList, Wallet.getCurrentWallet());
        list.setAdapter(recyclerAdapter);

        //Set click listener
        RecycleItemClickListener clickListener = new RecycleItemClickListener(this.getApplicationContext(), recyclerAdapter);
        list.addOnItemTouchListener(clickListener);

        //Set swipe listener
        ItemTouchHelper.Callback callback = new RecyclerViewItemCallback(recyclerAdapter, ContextCompat.getColor(this, R.color.colorAccent));
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(list);
    }

    private static void initializeDB() {

        Wallet mainWallet = Wallet.getCurrentWallet();

        //create TransactionCategory
        TransactionCategory transactionCategoryFood = new TransactionCategory("Food", "This category about food", true);
        transactionCategoryFood.save();
        //create TransactionCategory
        TransactionCategory transactionCategoryTransport = new TransactionCategory("Transport", "This category about transport", true);
        transactionCategoryTransport.save();
        //create TransactionCategory
        TransactionCategory transactionCategorySalary = new TransactionCategory("Salary", "This category about salary", false);
        transactionCategorySalary.save();
        //create TransactionCategory
        TransactionCategory transactionCategoryPremium = new TransactionCategory("Premium", "This category about premium", false);
        transactionCategoryPremium.save();
        //create TransactionCategory
        TransactionCategory transactionCategoryGym = new TransactionCategory("Gym", "This category about premium", true);
        transactionCategoryGym.save();

        //create Transactions
        MoneyTransaction moneyTransaction1 = new MoneyTransaction("Bought bread", transactionCategoryFood, mainWallet, "I bought some bread", -5);
        moneyTransaction1.save();
        MoneyTransaction moneyTransaction2 = new MoneyTransaction("Bought subscription", transactionCategoryGym, mainWallet, "I bought subscription on month", -50);
        moneyTransaction2.save();
        MoneyTransaction moneyTransaction3 = new MoneyTransaction("My salary", transactionCategorySalary, mainWallet, "I get salary", 270);
        moneyTransaction3.save();
        MoneyTransaction moneyTransaction4 = new MoneyTransaction("Banana", transactionCategoryFood, mainWallet, "Bought two kilogram of bananas in market at my home", -60);
        moneyTransaction4.save();
        MoneyTransaction moneyTransaction5 = new MoneyTransaction("Sport nutrition", transactionCategoryGym, mainWallet, null, -150);
        moneyTransaction5.save();
    }

    private static void addTestData() {
        Wallet mainWallet;
        TransactionCategory transactionCategoryFood;
        TransactionCategory transactionCategorySalary;
        TransactionCategory transactionCategoryGym;
        mainWallet = Wallet.getCurrentWallet();
        transactionCategoryFood = TransactionCategory.listAll(TransactionCategory.class).get(0);
        transactionCategorySalary = TransactionCategory.listAll(TransactionCategory.class).get(2);
        transactionCategoryGym = TransactionCategory.listAll(TransactionCategory.class).get(4);

        //create Transactions
        MoneyTransaction moneyTransaction1 = new MoneyTransaction("Bought bread", transactionCategoryFood, mainWallet, "I bought some bread", -5);
        moneyTransaction1.save();
        MoneyTransaction moneyTransaction2 = new MoneyTransaction("Bought subscription", transactionCategoryGym, mainWallet, "I bought subscription on month", -50);
        moneyTransaction2.save();
        MoneyTransaction moneyTransaction3 = new MoneyTransaction("My salary", transactionCategorySalary, mainWallet, "I get salary", 270);
        moneyTransaction3.save();
        MoneyTransaction moneyTransaction4 = new MoneyTransaction("Banana", transactionCategoryFood, mainWallet, "Bought two kilogram of bananas in market at my home", -60);
        moneyTransaction4.save();
        MoneyTransaction moneyTransaction5 = new MoneyTransaction("Sport nutrition", transactionCategoryGym, mainWallet, null, -150);
        moneyTransaction5.save();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_EXPENSE_LIST, mIsExpenseActivity);
        super.onSaveInstanceState(outState);
    }

    public void setDateFieldValue(){
        dateField.setText(DateUtils.formatDateTime(getApplicationContext(), mFirstDate.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    public void onDateBoxClick(View view) {
        new DatePickerDialog(this, this, mFirstDate.get(Calendar.YEAR), mFirstDate.get(Calendar.MONTH), mFirstDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mFirstDate.get(Calendar.YEAR) != year || mFirstDate.get(Calendar.MONTH) != monthOfYear || mFirstDate.get(Calendar.DAY_OF_MONTH) != dayOfMonth) {
            mFirstDate.set(Calendar.YEAR, year);
            mFirstDate.set(Calendar.MONTH, monthOfYear);
            mFirstDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mTransactionList.clear();
            mTransactionList.addAll(MoneyTransaction.findTransactionByDate(mFirstDate, mIsExpenseActivity));
            recyclerAdapter.notifyDataSetChanged();
            setDateFieldValue();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            addTestData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent in;
        if (id == R.id.nav_expenses) {
            in = new Intent(this, MainActivity.class);
            in.putExtra(IS_EXPENSE_LIST, true);
            startActivity(in);
        } else if (id == R.id.nav_incomes) {
            in = new Intent(this, MainActivity.class);
            in.putExtra(IS_EXPENSE_LIST, false);
            startActivity(in);
        } else if (id == R.id.nav_categories) {
            in = new Intent(this, CategoryActivity.class);
            startActivity(in);
        } else if(id == R.id.nav_statistic){
            startActivity(new Intent(this, StatisticActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
