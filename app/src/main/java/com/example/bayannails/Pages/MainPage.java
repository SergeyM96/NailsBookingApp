package com.example.bayannails.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import android.content.DialogInterface;

import com.example.bayannails.Classes.OrderService;
import com.example.bayannails.MapsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Calendar;

import com.example.bayannails.Classes.Order;

import com.example.bayannails.R;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MainPage extends AppCompatActivity {
    String myNumber = "0523239955";
    List<Order> orderList = new ArrayList<>();
    private OrderService orderService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ImageView iv_gallery, iv_queue, iv_maps, iv_change, iv_cancel, iv_add, iv_instagram,
                iv_watssup, iv_call, iv_facebook;

        // Retrieve the user name from the Intent extras
        String userName = getIntent().getStringExtra("userName");

        // Set the user name as the title
        setTitle("Welcome " + userName);

        iv_gallery = findViewById(R.id.ivGallery);
        iv_queue = findViewById(R.id.ivQueue);
        iv_maps = findViewById(R.id.imageView_maps);
        iv_change = findViewById(R.id.ivChange);
        iv_cancel = findViewById(R.id.ivCancel);
        iv_add = findViewById(R.id.ivAdd);
        iv_instagram = findViewById(R.id.ivInstagram);
        iv_facebook = findViewById(R.id.ivFacebook);
        iv_watssup = findViewById(R.id.ivWatssup);
        iv_call = findViewById(R.id.ivCall);

        // Bind to the OrderService
        Intent serviceIntent = new Intent(this, OrderService.class);

        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);



        iv_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the user name from the Intent extras
                String userName = getIntent().getStringExtra("userName");

                if (userName != null) {
                    // Get a reference to the user node in the database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userName);

                    // Retrieve the user's order from the database
                    userRef.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Get the current order
                                Order currentOrder = dataSnapshot.getValue(Order.class);

                                // Display the order in an alert dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                                builder.setTitle("התור שלי");
                                builder.setMessage("Day: " + currentOrder.getDay() + "\nMonth: " + currentOrder.getMonth() + "\nYear: " + currentOrder.getYear() + "\nHour: " + currentOrder.getHour());
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            } else {
                                // User does not have an order
                                Toast.makeText(MainPage.this, "אין לך תור עד עכשיו", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled if needed
                        }
                    });
                }
            }
        });

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, Gallery_Activity.class);
                startActivity(intent);
            }
        });


        iv_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, MapsActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(MainPage.this,MapsActivity.class);
//                startActivity(intent);
            }
        });


        iv_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform navigation to Instagram here
                Uri uri = Uri.parse("https://instagram.com/bayan_nails.5.2?igshid=MzRlODBiNWFlZA==");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform navigation to Facebook here
                Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100067890501451&mibextid=ZbWKwL");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        iv_watssup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform navigation to WhatsApp here
                String message = "Hello, this is a message from my app BayanNails!";
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + myNumber + "&text=" + Uri.encode(message));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform phone call here
                Uri uri = Uri.parse("tel:" + myNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the user name from the Intent extras
                String userName = getIntent().getStringExtra("userName");

                if (userName != null) {
                    // Get a reference to the user node in the database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userName);

                    // Retrieve the user's order from the database
                    userRef.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Get the current order
                                Order currentOrder = dataSnapshot.getValue(Order.class);

                                // Display the order and provide an option to delete it
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                                builder.setTitle("התור הנוכחי");
                                builder.setMessage("Day: " + currentOrder.getDay() + "\nMonth: " + currentOrder.getMonth() + "\nYear: " + currentOrder.getYear() + "\nHour: " + currentOrder.getHour());
                                builder.setPositiveButton("מחק תור", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Delete the order from the database
                                        userRef.child("order").removeValue();
                                        Toast.makeText(MainPage.this, "התור שלך נמחק ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setNegativeButton("ביטול", null);
                                builder.show();
                            } else {
                                // User does not have an order
                                Toast.makeText(MainPage.this, "אין לך תורים", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled if needed
                        }
                    });
                }
            }
        });

        iv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the user name from the Intent extras
                String userName = getIntent().getStringExtra("userName");

                if (userName != null) {
                    // Get a reference to the user node in the database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userName);

                    // Retrieve the user's order from the database
                    userRef.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Get the current order
                                Order currentOrder = dataSnapshot.getValue(Order.class);

                                // Display the current order
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                                builder.setTitle("התור הנוכחי");
                                builder.setMessage("Day: " + currentOrder.getDay() + "\nMonth: " + currentOrder.getMonth() + "\nYear: " + currentOrder.getYear() + "\nHour: " + currentOrder.getHour());
                                builder.setPositiveButton("שינוי תור", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Delete the current order from the database
                                        userRef.child("order").removeValue();

                                        // Call the method to show the date and time picker
                                        showDateTimePicker();
                                    }
                                });
                                builder.setNegativeButton("ביטול", null);
                                builder.show();
                            } else {
                                // User does not have an order, directly call the method to show the date and time picker
                                showDateTimePicker();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled if needed
                        }
                    });
                }
            }
        });
    }

    private void showDateTimePicker() {
        // Retrieve the user name from the Intent extras
        String userName = getIntent().getStringExtra("userName");

        if (userName != null) {
            // Get a reference to the user node in the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userName);

            // Check if the user already has an order
            userRef.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User already has an order
                        Toast.makeText(MainPage.this, "יש לך כבר תור במערכת", Toast.LENGTH_SHORT).show();
                    } else {
                        // Get the current date and time
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = (calendar.get(Calendar.MONTH))+1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);

                        // Show date picker dialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainPage.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                        final int selectedYearFinal = selectedYear;
                                        final int selectedMonthFinal = selectedMonth;
                                        final int selectedDayFinal = selectedDayOfMonth;

                                        // Show time picker dialog
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainPage.this,
                                                new TimePickerDialog.OnTimeSetListener() {
                                                    @Override
                                                    public void onTimeSet(TimePicker view, int selectedHourOfDay, int selectedMinute) {
                                                        // Create an Order object with the selected hour
                                                        Order selectedOrder = new Order(selectedDayFinal, selectedMonthFinal, selectedYearFinal, selectedHourOfDay);

                                                        // Update the database with the selected order
                                                        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
                                                        String orderId = ordersRef.push().getKey();
                                                        ordersRef.child(orderId).setValue(selectedOrder);

                                                        // Update the user's order in the database
                                                        userRef.child("order").setValue(selectedOrder);

                                                        Toast.makeText(MainPage.this, "הוספתה תור אל " + userName, Toast.LENGTH_SHORT).show();
                                                    }
                                                }, hour, 0, true); // Set is24HourView to true and remove the minute parameter
                                        timePickerDialog.show();
                                    }
                                }, year, month, day);
                        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()); // Set minimum date to the current date
                        datePickerDialog.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled if needed
                }
            });
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OrderService.LocalBinder binder = (OrderService.LocalBinder) service;
            orderService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            orderService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the OrderService
        unbindService(serviceConnection);
    }

    private void getUserOrder(String userName) {
        orderService.getUserOrder(userName, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order currentOrder = dataSnapshot.getValue(Order.class);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                    builder.setTitle("התור שלי");
                    builder.setMessage("Day: " + currentOrder.getDay() + "\nMonth: " + currentOrder.getMonth() + "\nYear: " + currentOrder.getYear() + "\nHour: " + currentOrder.getHour());
                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else {
                    Toast.makeText(MainPage.this, "אין לך תור עד עכשיו", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled if needed
            }
        });
    }
    private void deleteOrder(String userName) {
        orderService.deleteOrder(userName, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(MainPage.this, "התור נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainPage.this, "נכשל במחיקת התור", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateOrder(String userName, Order newOrder) {
        orderService.updateOrder(userName, newOrder, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(MainPage.this, "התור עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainPage.this, "נכשל בעדכון התור", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addOrder(Order order) {
        orderService.addOrder(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(MainPage.this, "התור נוסף בהצלחה", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainPage.this, "נכשל בהוספת התור", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



