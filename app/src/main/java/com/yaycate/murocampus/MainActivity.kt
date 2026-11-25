package com.yaycate.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.yaycate.murocampus.data.PostRepositoryRemote
import com.yaycate.murocampus.data.remote.RetrofitClient
import com.yaycate.murocampus.ui.PantallaCrear
import com.yaycate.murocampus.ui.PantallaDetalle
import com.yaycate.murocampus.ui.PantallaLista
import com.yaycate.murocampus.ui.theme.AzulMuro
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

                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }

                BackHandler(enabled = idSeleccionado != null || creando) {
                    idSeleccionado = null
                    creando = false
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = AzulMuro,
                                titleContentColor = Color.White
                            )
                        )
                    },
                    floatingActionButton = {
                        if (idSeleccionado == null && !creando) {
                            FloatingActionButton(
                                onClick = {
                                    vm.limpiarPublicado()
                                    creando = true
                                },
                                containerColor = AzulMuro,
                                contentColor = Color.White
                            ) {
                                Text("+")
                            }
                        }
                    }
                ) { innerPadding ->
                    when {
                        creando -> PantallaCrear(
                            vm = vm,
                            modifier = Modifier.padding(innerPadding)
                        )
                        idSeleccionado == null -> PantallaLista(
                            vm = vm,
                            onAbrir = { idSeleccionado = it },
                            modifier = Modifier.padding(innerPadding)
                        )
                        else -> PantallaDetalle(
                            vm = vm,
                            id = idSeleccionado!!,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
