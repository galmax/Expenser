package com.github.sinapple.expenser;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;
import com.github.sinapple.expenser.model.Wallet;
import com.github.sinapple.expenser.statistic.StatisticActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, RecycleItemClickListener.OnItemClickListener, RecyclerViewItemCallback.ItemTouchHelperAdapter
{
    public static final String IS_EXPENSE_LIST = "com.github.sinapple.expenser.IS_EXPENSE_LIST";
    public static final String SETTINGS = "com.github.sinapple.expenser.SETTINGS";
    public static final String IS_FIRST_RUN = "com.github.sinapple.expenser.IS_FIRST_RUN";
    public static final int TRANSACTION_ADDED = 0;
    public static final int TRANSACTION_EDITED = 1;

    public static final int INCOME_LIST  = 2;
    public static final int EXPENSE_LIST = 3;

    //Model instances
    private List<MoneyTransaction> mTransactionList;
    private int mPassedTransactionIndex;
    private float mCurrentBalance;
    private CustomRListAdapter recyclerAdapter;
    private Wallet mWallet;

    //Date and date format tools
    private java.text.DateFormat mDateFormat;
    private Calendar mFirstDate;
    private Calendar mSecondDate;
    private boolean mIsExpenseActivity;

    //Views
    private FloatingActionButton mFab;
    private TextView mTvBalance;
    private TextView mNothingToShowView;
    private TextView mDateField;

    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to AddTransactionsActivity
                Intent intentToTransaction = new Intent(MainActivity.this, NewTransactionActivity.class);
                //get Transaction categories
                long transactionCategoriesCount = TransactionCategory.count(TransactionCategory.class, "m_expense_category=?", new String[]{mIsExpenseActivity ? "1" : "0"});
                if (transactionCategoriesCount != 0) {
                    //send key to NewTransactionActivity.class
                    intentToTransaction.putExtra(NewTransactionActivity.ACTION, mIsExpenseActivity ? NewTransactionActivity.ADD_EXPENSE : NewTransactionActivity.ADD_INCOME);
                    //Pass exact date and time for NewTransactionActivity
                    Calendar passedValue = Calendar.getInstance();
                    passedValue.set(mFirstDate.get(Calendar.YEAR), mFirstDate.get(Calendar.MONTH), mFirstDate.get(Calendar.DAY_OF_MONTH));
                    intentToTransaction.putExtra(NewTransactionActivity.TIME, passedValue.getTime());
                    startActivityForResult(intentToTransaction, TRANSACTION_ADDED);
                } else {
                    Snackbar.make(view, mIsExpenseActivity ? R.string.null_expense : R.string.null_income, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                mTvBalance.setText(Float.toString(mCurrentBalance));
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
        changeActivityAppearance(mIsExpenseActivity? EXPENSE_LIST: INCOME_LIST);

        mSettings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        //initializeDatabase();
        //Write balance in nav_header_main
        View header = navigationView.getHeaderView(0);
        mTvBalance = (TextView) header.findViewById(R.id.tv_balance);
        TextView tv_sign = (TextView) header.findViewById(R.id.tv_sign);
        mWallet = Wallet.getCurrentWallet();
        mCurrentBalance = mWallet.getBalance();
        mTvBalance.setText(Float.toString(mWallet.getBalance()));
        tv_sign.setText(mWallet.getCurrency().getSign());

        //Initialize date
        mFirstDate = Calendar.getInstance(Locale.getDefault());
        mDateField = (TextView)findViewById(R.id.date);
        setDateFieldValue();

        //Initialize RecyclerList
        addAllToList(MoneyTransaction.findTransactionByDate(mFirstDate, mIsExpenseActivity));
        RecyclerView list = (RecyclerView) findViewById(R.id.transaction_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new CustomRListAdapter(mTransactionList);
        list.setAdapter(recyclerAdapter);

        //Set click listener
        RecycleItemClickListener clickListener = new RecycleItemClickListener(this.getApplicationContext(), this);
        list.addOnItemTouchListener(clickListener);

        //Set swipe listener
        ItemTouchHelper.Callback callback = new RecyclerViewItemCallback(this, ContextCompat.getColor(this, R.color.colorPrimary));
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(list);
    }

    private static void initializeDatabase() {

        Wallet.getCurrentWallet();

        //Create income categories
        List<TransactionCategory> categoriesList= new ArrayList<TransactionCategory>();
        categoriesList.add(new TransactionCategory("Paycheck","Incomes on a monthly basis",false));
        categoriesList.add(new TransactionCategory("Work bonus","Incentive payments",false));
        categoriesList.add(new TransactionCategory("Expense reimbursements","Compensations for expenses",false));
        categoriesList.add(new TransactionCategory("Investment","Incomes from a range of investments",false));
        categoriesList.add(new TransactionCategory("Other","Unsorted incomes",false));

        //Create expense categories
        categoriesList.add(new TransactionCategory("Automobile","Expenses associated with vehicles",true));
        categoriesList.add(new TransactionCategory("Bank Charges","All charges for bank services",true));
        categoriesList.add(new TransactionCategory("Charity","Money given to those in need",true));
        categoriesList.add(new TransactionCategory("Clothing","Expenses on clothing, laundry, repairing",true));
        categoriesList.add(new TransactionCategory("Education","Money spent on education, trainings, courses",true));
        categoriesList.add(new TransactionCategory("Events","Expenses on celebrations",true));
        categoriesList.add(new TransactionCategory("Food","Expenses on food",true));
        categoriesList.add(new TransactionCategory("Gifts","Money spent on gifts",true));
        categoriesList.add(new TransactionCategory("Health care","Expenses on health insurance, treatment, diagnostic services",true));
        categoriesList.add(new TransactionCategory("Insurance","Insurance expenses",true));
        categoriesList.add(new TransactionCategory("Job expenses","Deductions and expenses related to job",true));
        categoriesList.add(new TransactionCategory("Leisure","Money spent on entertainment and partying",true));
        categoriesList.add(new TransactionCategory("Hobbies","Money spent on personal enjoyment",true));
        categoriesList.add(new TransactionCategory("Loans","Expenses on principal and interest payments",true));
        categoriesList.add(new TransactionCategory("Pet Care","Expenses on pet health care, toys, pet clothing",true));
        categoriesList.add(new TransactionCategory("Taxes","Financial charges imposed on various profits",true));
        categoriesList.add(new TransactionCategory("Utilities","Payments for electricity, water, gas, sewage",true));
        categoriesList.add(new TransactionCategory("Vacation","Money spent on recreation and tourism",true));

        //Save all in database
        for (int i=0;i<categoriesList.size();i++){
            categoriesList.get(i).save();
        }
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

    private void changeActivityAppearance(int flag){
        if (flag == EXPENSE_LIST){
            setTitle(getApplicationContext().getString(R.string.nav_expenses));
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.basic_expense_color)));
        }else if (flag == INCOME_LIST){
            setTitle(getApplicationContext().getString(R.string.nav_incomes));
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.basic_income_color)));
        }
    }

    //Display message if the transaction list is empty
    private void notifyIfEmptyList(){
        if (mNothingToShowView == null)
            mNothingToShowView = (TextView)findViewById(R.id.nothing_to_show);

        if (mTransactionList.size() == 0 || mTransactionList == null) {
            mNothingToShowView.setVisibility(View.VISIBLE);
        } else {
            mNothingToShowView.setVisibility(View.GONE);
        }
    }

    //Convenient methods to manage transaction list and reflect its changes on related views
    private void addItemToList(MoneyTransaction transaction){
        addItemToList(mTransactionList.size(), transaction);
    }

    private void addItemToList(int index, MoneyTransaction transaction){
        mTransactionList.add(index, transaction);
        recyclerAdapter.notifyItemInserted(index);
        notifyIfEmptyList();
    }

    private void addAllToList(List<MoneyTransaction> list){
        if (list == null) {
            notifyIfEmptyList();
            return;
        }
        if (mTransactionList == null) mTransactionList = new ArrayList<>();
        mTransactionList.addAll(list);
        if (recyclerAdapter != null) recyclerAdapter.notifyDataSetChanged();
        notifyIfEmptyList();
    }

    private MoneyTransaction removeItem(int index) {
        MoneyTransaction res = mTransactionList.remove(index);
        recyclerAdapter.notifyItemRemoved(index);
        notifyIfEmptyList();
        return res;
    }

    public void setDateFieldValue(){
        if (mDateFormat == null)
            mDateFormat = android.text.format.DateFormat.getMediumDateFormat(getApplicationContext());
        mDateField.setText(mDateFormat.format(mFirstDate.getTime()));
    }

    public void onDateBoxClick(View view) {
        new DatePickerDialog(this, this, mFirstDate.get(Calendar.YEAR), mFirstDate.get(Calendar.MONTH), mFirstDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check if application is on its first run
        if (mSettings.getBoolean(IS_FIRST_RUN, true)){
            initializeDatabase();
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(IS_FIRST_RUN, false);
            editor.apply();
        }
    }

    //Updates information has been changed or added in NewTransactionActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Update balance after adding new transaction
            mWallet = Wallet.findById(Wallet.class, mWallet.getId());
            mCurrentBalance = mWallet.getBalance();
            MoneyTransaction m;
            if (requestCode == TRANSACTION_ADDED){
                if (!data.hasExtra(NewTransactionActivity.TRANSACTION_ID)) throw new IllegalStateException("ID of added transaction hasn't been passed");
                //Attach added item to existing list
                m = MoneyTransaction.findById(MoneyTransaction.class, data.getLongExtra(NewTransactionActivity.TRANSACTION_ID, 0));
                if (mSecondDate == null ? m.isInDate(mFirstDate) : m.isInDateInterval(mFirstDate, mSecondDate)) {
                    addItemToList(m);
                }
            } else if (requestCode == TRANSACTION_EDITED) {
                if (mPassedTransactionIndex == -1 ) throw new IllegalStateException("Index of sent transaction in list hasn't been saved");
                //Update edited item
                m = MoneyTransaction.findById(MoneyTransaction.class, mTransactionList.get(mPassedTransactionIndex).getId());
                if (mSecondDate == null ? m.isInDate(mFirstDate) : m.isInDateInterval(mFirstDate, mSecondDate)) {
                    mTransactionList.set(mPassedTransactionIndex, m);
                    recyclerAdapter.notifyItemChanged(mPassedTransactionIndex);
                } else {
                    //If date has been changed
                    removeItem(mPassedTransactionIndex);
                }
                mPassedTransactionIndex = -1;
            }
        }
    }

    //Remove some item. Usually called when the swipe gesture has been performed.
    @Override
    public void onItemDismiss(View messageOutput, final int position) {
        final MoneyTransaction m = removeItem(position);
        mCurrentBalance -= m.getRawAmount();
        Snackbar.make(messageOutput, R.string.message_transaction_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.cancel, new View.OnClickListener() {
                    //Return transaction in the list when user have canceled removing and restore the amount of money in the wallet
                    @Override
                    public void onClick(View v) {
                        addItemToList(position, m);
                        mCurrentBalance += m.getRawAmount();
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    //Final removing the transaction and changing of money amount
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            mWallet.setBalance(mCurrentBalance);
                            mWallet.save();
                            m.delete();
                        }
                    }
                }).show();
    }

    //Called when the click has been performed, in this case, method will open edit activity
    @Override
    public void onItemClick(View view, int position) {
        Intent editIntent = new Intent(MainActivity.this, NewTransactionActivity.class);
        editIntent.putExtra(NewTransactionActivity.ACTION, mIsExpenseActivity ? NewTransactionActivity.EDIT_EXPENSE : NewTransactionActivity.EDIT_INCOME);
        editIntent.putExtra(NewTransactionActivity.TRANSACTION_ID, mTransactionList.get(position).getId());
        mPassedTransactionIndex = position;
        startActivityForResult(editIntent, TRANSACTION_EDITED);
    }

    //Called when user set date in specific window
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mFirstDate.get(Calendar.YEAR) != year || mFirstDate.get(Calendar.MONTH) != monthOfYear || mFirstDate.get(Calendar.DAY_OF_MONTH) != dayOfMonth) {
            mFirstDate.set(Calendar.YEAR, year);
            mFirstDate.set(Calendar.MONTH, monthOfYear);
            mFirstDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mTransactionList.clear();
            if (mSecondDate == null)
                addAllToList(MoneyTransaction.findTransactionByDate(mFirstDate, mIsExpenseActivity));
            else
                addAllToList(MoneyTransaction.findTransactionByDate(mFirstDate, mSecondDate, mIsExpenseActivity));

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
        } else if (id == R.id.nav_statistic) {
            startActivity(new Intent(this, StatisticActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
