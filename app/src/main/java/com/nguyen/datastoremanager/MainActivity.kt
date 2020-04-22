package com.nguyen.datastoremanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nguyen.datastore.KeyStoreManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: KeyRecyclerAdapter

    var list = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = KeyStoreManager.getListKeyAliases()
        adapter = KeyRecyclerAdapter(list)
        listView.adapter = adapter
    }
}
