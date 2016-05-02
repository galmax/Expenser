package com.github.sinapple.expenser;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.sinapple.expenser.model.Currency;
import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;
import com.github.sinapple.expenser.model.Wallet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to AddTransactionsActivity
                Intent intentToTransaction = new Intent(MainActivity.this, AddTransactionActivity.class);
                //send key to AddTransactionActivity.class
                intentToTransaction.putExtra("whatDo", "editExpense");
                intentToTransaction.putExtra("Id", 1);
                startActivity(intentToTransaction);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addTestData();
        //Initialize RecyclerList
        RecyclerView list = (RecyclerView) findViewById(R.id.transaction_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        CustomRListAdapter adapter = new CustomRListAdapter(getApplicationContext(), MoneyTransaction.find(MoneyTransaction.class, "m_amount < ?", "0"));
        RecycleItemClickListener clickListener = new RecycleItemClickListener(this.getApplicationContext(), adapter);
        list.setAdapter(adapter);
        list.addOnItemTouchListener(clickListener);
        ItemTouchHelper.Callback callback = new RecyclerViewItemCallback(adapter, ContextCompat.getColor(this, R.color.colorAccent));
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(list);
    }

    private static void initializeDB() {
        //create Currency
        Currency mainCurrency = new Currency("Euro", "EUR", "\u20ac");
        mainCurrency.save();

        Wallet mainWallet = new Wallet("DefaultWallet", null, 0, mainCurrency);
        mainWallet.save();

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

    private static void addTestData(){
        Wallet mainWallet;
        TransactionCategory transactionCategoryFood;
        TransactionCategory transactionCategorySalary;
        TransactionCategory transactionCategoryGym;
        try {
            mainWallet = Wallet.listAll(Wallet.class).get(0);
            transactionCategoryFood = TransactionCategory.listAll(TransactionCategory.class).get(0);
            transactionCategorySalary = TransactionCategory.listAll(TransactionCategory.class).get(2);
            transactionCategoryGym = TransactionCategory.listAll(TransactionCategory.class).get(4);
        }catch (Exception x){
            return;
        }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_expenses) {
            // Handle the camera action
        } else if (id == R.id.nav_incomes) {

        } else if (id == R.id.nav_categories) {
            Intent in=new Intent(this,CategoryActivity.class);
            startActivity(in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
