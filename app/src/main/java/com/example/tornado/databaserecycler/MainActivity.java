package com.example.tornado.databaserecycler;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SqliteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mDatabase = new SqliteDatabase(this);

        List<ModelProduct> allModelProducts = mDatabase.listProducts();
        if(allModelProducts.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            ProductAdapter mAdapter = new ProductAdapter(this, allModelProducts);
            recyclerView.setAdapter(mAdapter);
        }else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new quick task
                addTaskDialog();
            }
        });
    }


    private void addTaskDialog(){

        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_product_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText quantityField = (EditText)subView.findViewById(R.id.enter_quantity);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new product");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD PRODUCT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final int quantity = Integer.parseInt(quantityField.getText().toString());
                if(TextUtils.isEmpty(name) || quantity <= 0){
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    ModelProduct newModelProduct = new ModelProduct(name, quantity);
                    mDatabase.addProduct(newModelProduct);
                    //refresh the activity
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }
}
