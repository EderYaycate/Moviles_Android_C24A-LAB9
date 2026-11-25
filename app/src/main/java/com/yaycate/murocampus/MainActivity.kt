package com.yaycate.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.yaycate.murocampus.data.PostRepositoryRemote
import com.yaycate.murocampus.data.remote.RetrofitClient
import com.yaycate.murocampus.ui.PantallaLista
import com.yaycate.murocampus.ui.theme.MuroCampusTheme
import com.yaycate.murocampus.viewmodel.PostsViewModel
import com.yaycate.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                val repo = PostRepositoryRemote(RetrofitClient.api)
                val vm: PostsViewModel = ViewModelProvider(this@MainActivity, PostsViewModelFactory(repo))[PostsViewModel::class.java]
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") }
                        )
                    }
                ) { innerPadding ->
                    PantallaLista(
                        vm = vm,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
