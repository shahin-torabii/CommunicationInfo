package com.example.communicationinfo.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InFoAppBar(title: String, onButtonClicked : () -> Unit){

    TopAppBar(title = {
        Text(text = title)
    },
        navigationIcon = {
            IconButton(onClick = onButtonClicked) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back arrow icon")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )

        )
}