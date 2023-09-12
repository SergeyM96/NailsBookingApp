package com.example.bayannails.Classes;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderService extends Service {
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public OrderService getService() {
            return OrderService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void getUserOrder(String userName, ValueEventListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userName);
        userRef.child("order").addListenerForSingleValueEvent(listener);
    }

    public void deleteOrder(String userName, DatabaseReference.CompletionListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userName);
        userRef.child("order").removeValue(listener);
    }

    public void updateOrder(String userName, Order newOrder, DatabaseReference.CompletionListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userName);
        userRef.child("order").setValue(newOrder, listener);
    }

    public void addOrder(Order order, DatabaseReference.CompletionListener listener) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
        String orderId = ordersRef.push().getKey();
        ordersRef.child(orderId).setValue(order, listener);
    }
}
