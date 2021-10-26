package com.ecmaceda.sqldatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import static com.ecmaceda.sqldatabase.DatabaseHelper.*;

public class MainActivity extends AppCompatActivity {

    Button btn_viewAll, btn_Add;
    EditText et_name, et_age;
    SwitchCompat sw_activeCustomer;
    ListView lv_customerList;

    ArrayAdapter customerArrayAdapter;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        btn_Add = findViewById(R.id.btn_Add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_activeCustomer = findViewById(R.id.sw_activeCustomer);
        lv_customerList = findViewById(R.id.lv_customerList);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        showCustomersOnListView(databaseHelper);


        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerModel customerModel;

                try {
                    customerModel = new CustomerModel(-1, et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()), sw_activeCustomer.isChecked());
                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error Creating Customer Acct", Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "error", 0, false);
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                boolean success = databaseHelper.addOne(customerModel);
                Toast.makeText(MainActivity.this, "Success= " + success, Toast.LENGTH_SHORT).show();


                showCustomersOnListView(databaseHelper);
            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                    showCustomersOnListView(databaseHelper);
                }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                    databaseHelper.deleteOne(clickedCustomer);
                    showCustomersOnListView(databaseHelper);
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
        });



    }

    private void showCustomersOnListView(DatabaseHelper databaseHelper2) {
        customerArrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, databaseHelper2.getEveryone());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}