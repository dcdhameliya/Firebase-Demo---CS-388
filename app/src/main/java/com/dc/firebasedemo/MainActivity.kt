package com.dc.firebasedemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.Arrays


class MainActivity : AppCompatActivity() {

    lateinit var btnSubmit: Button
    lateinit var tvFirstName: EditText
    lateinit var tvLastName: EditText
    lateinit var rvMain: RecyclerView
    lateinit var tvLogout: TextView

    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference

    val nameList = ArrayList<ListModel?>()

    lateinit var listAdapter: ListAdapter

    fun init() {
        btnSubmit = findViewById(R.id.btnSubmit)
        tvFirstName = findViewById(R.id.tvFirstName)
        tvLastName = findViewById(R.id.tvLastName)
        rvMain = findViewById(R.id.rvMain)
        tvLogout = findViewById(R.id.tvLogout)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference(auth.currentUser!!.uid)


        btnSubmit.setOnClickListener {
            uploadData()
        }

        tvLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        init()

        listAdapter = ListAdapter(nameList, object : ListAdapter.OnItemDeleteClickListener {
            override fun onItemDeleteClick(position: Int) {
                deleteData(position)
            }
        })
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = listAdapter


        getList()


    }

    private fun deleteData(position: Int) {
        nameList.removeAt(position)
        val newJson = Gson().toJson(nameList)
        databaseReference.child("NameList").setValue(newJson)
    }

    private fun uploadData() {
        val firstName = tvFirstName.text.toString()
        val lastName = tvLastName.text.toString()

        nameList.add(ListModel(firstName, lastName))
        val newJson = Gson().toJson(nameList)

        databaseReference.child("NameList").setValue(newJson)
        databaseReference.child("Name").setValue(firstName + " " + lastName)

    }

    fun getList() {

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val namelistString = dataSnapshot.child("NameList").value.toString()

                if (namelistString.isEmpty() || namelistString == "" || namelistString == "null") {
                    return
                }
                nameList.clear()
                val playerArray = Gson().fromJson(namelistString, Array<ListModel>::class.java)
                nameList.addAll(Arrays.asList(*playerArray))

                listAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CUSTOM---->", "Failed to read value.", error.toException())
            }
        })
    }
}