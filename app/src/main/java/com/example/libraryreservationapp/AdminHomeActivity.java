package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminHomeActivity extends AppCompatActivity {

    //private member variables
    private Button btnLogout;
    private Button btnAddRoom;

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnLogout = findViewById(R.id.logout);
        btnAddRoom = findViewById(R.id.btnGoToAddRoom);
        fStore = FirebaseFirestore.getInstance();

        //calls the recycler view for it to be set up
        setUpRecyclerView();

        // Logout On Click Listener
        btnLogout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signs out user
                FirebaseAuth.getInstance().signOut();
                //Starts LoginActivity if this button is clicked
                Intent intToLogin = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(intToLogin);
            }
        }));

        // Add Room On Click Listener
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starts AddRoomActivity if the button is clicked
                Intent intToAddRoom = new Intent(AdminHomeActivity.this, AddRoomActivity.class);
                startActivity(intToAddRoom);
            }
        });


    }

    private void setUpRecyclerView() {
        // creates a query that uses the collection reference to get the buildings in ascending order
        Query query = fStore.collection("rooms").orderBy("building", Query.Direction.ASCENDING).orderBy("roomNumber", Query.Direction.ASCENDING);

        // creates configuratios for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<Room> options = new FirestoreRecyclerOptions.Builder<Room>().setQuery(query, Room.class).setLifecycleOwner(this).build();

        // sets the adapter with the configurations that were just made
        adapter = new RoomAdapter(options);

        // gets the recyclerView id for reference
        recyclerView = findViewById(R.id.recyclerViewRooms);
        // sets the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adds horizontal line between different items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // sets the adapter
        recyclerView.setAdapter(adapter);
    }
}