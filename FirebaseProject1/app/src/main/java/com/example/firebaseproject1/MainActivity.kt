package com.example.firebaseproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    val notes = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arrayAdapter = ArrayAdapter(this, R.layout.item_row, R.id.lvNotes, notes)
        lvNotes.adapter = arrayAdapter

        database = Firebase.database.reference
        btnSave.setOnClickListener{
            val note = etNote.text.toString()
            // Upload the note to firebase realtime database

            //get a reference to root database
            database.push().setValue(note)
//            database.child("node").push().setValue(Note("Apoorv","Srivastava"))
        }

        database.child("node").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Called when a new data node is inserted to "note" node
                // New data(key- value pair) is sent to us in form of DataSnapshot
//                val data: Note? = snapshot.getValue(Note::class.java)
                val data = snapshot.getValue(String::class.java)// Extracting string from dataSnapshot
                notes.add(data!!)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Called when an existing data node is updated
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Called when data at a sub-node is removed
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Called when the position of a subnode changes
            }

            override fun onCancelled(error: DatabaseError) {
                // Called when the read operation failed. you are unable to read from the database
            }
        })

        database.child("node").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Whenever I add, delete or modify something in the database
                // I gets the entire database back
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}