package com.example.mypc.wakesandbakes;

import android.content.DialogInterface;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypc.wakesandbakes.Common.Common;
import com.example.mypc.wakesandbakes.Database.Database;
import com.example.mypc.wakesandbakes.Model.Order;
import com.example.mypc.wakesandbakes.Model.Request;
import com.example.mypc.wakesandbakes.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView txtTotalPrice;
    FButton btnPlace;

    FirebaseDatabase database;
    DatabaseReference requests;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your Cart is empty ..!", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step !");
        alertDialog.setMessage("Enter Delivery Address : ");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress); //Add edit text to alert Dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Create new Request
                Request request = new Request(
                        Common.currentUser.getName(),
                        Common.currentUser.getPhone(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
                //Submit to Firebase
                //Using System.CurrentMilli key
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //Delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank You, Order Placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //calculate total price
        int total = 0;

        for (Order order:cart)
        {
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        }
        Locale locale = new Locale("en","in");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    //ctrl+o


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.DELETE))
        {
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int position) {
        //we'll remove item at list<order> by position

        cart.remove(position);

        //After that we'll delete all old data from SQlite
        new Database(this).cleanCart();

        //After that we'll update new data from list<order> to SQlite
        for(Order item:cart)
        {
            new Database(this).addToCart(item);
        }

        //Refresh
        loadListFood();

    }
}
