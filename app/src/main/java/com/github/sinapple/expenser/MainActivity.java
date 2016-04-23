package com.github.sinapple.expenser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.sinapple.expenser.model.Currency;
import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;
import com.github.sinapple.expenser.model.Wallet;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

/*
        //create Currency
        Currency mainCurrency = new Currency("Euro", "EUR", '\u20ac');
        mainCurrency.save();
        //create Wallet
        Wallet mainWallet = new Wallet("My Wallet","Sanya`s wallet",0, mainCurrency);
        mainWallet.save();
        //create TransactionCategory
        TransactionCategory transactionCategoryFood = new TransactionCategory("Food", "This category about food", false);
        transactionCategoryFood.save();
        //create TransactionCategory
        TransactionCategory transactionCategoryTransport = new TransactionCategory("Transport", "This category about transport", false);
        transactionCategoryTransport.save();
        //create TransactionCategory
        TransactionCategory transactionCategorySalary = new TransactionCategory("Salary", "This category about salary", true);
        transactionCategorySalary.save();
        //create TransactionCategory
        TransactionCategory transactionCategoryPremium = new TransactionCategory("Premium", "This category about premium", true);
        transactionCategoryPremium.save();
        //create TransactionCategory
        TransactionCategory transactionCategoryGym = new TransactionCategory("Gym", "This category about premium", false);
        transactionCategoryGym.save();

        //create Transactions
        MoneyTransaction moneyTransaction1 = new MoneyTransaction("Bought bread", transactionCategoryFood, mainWallet, "I bought some bread", 5);
        moneyTransaction1.save();
        MoneyTransaction moneyTransaction2 = new MoneyTransaction("Bought subscription", transactionCategoryGym, mainWallet, "I bought subscription on month", 50);
        moneyTransaction2.save();
        MoneyTransaction moneyTransaction3 = new MoneyTransaction("My salary", transactionCategorySalary, mainWallet, "I get salary", 270);
        moneyTransaction3.save();
*/

        //writeList
        List<Wallet> walletList = Wallet.listAll(Wallet.class);
        List<MoneyTransaction> moneyTransactions = MoneyTransaction.listAll(MoneyTransaction.class);
        List<TransactionCategory> transactionCategory = TransactionCategory.listAll(TransactionCategory.class);
        List<Currency> currency = Currency.listAll(Currency.class);

/*       //delete with db
        Wallet.deleteAll(Wallet.class);
        Currency.deleteAll(Currency.class);
        MoneyTransaction.deleteAll(MoneyTransaction.class);
        TransactionCategory.deleteAll(TransactionCategory.class);
*/


        TextView textView = (TextView) findViewById(R.id.textView);

      //  textView.setText(walletList.toString());
       textView.setText(moneyTransactions.toString());
       // textView.setText(transactionCategory.toString());
       // textView.setText(currency.toString());

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
