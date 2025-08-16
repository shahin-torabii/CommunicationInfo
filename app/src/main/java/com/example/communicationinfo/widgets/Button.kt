package com.example.communicationinfo.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Button(modifier: Modifier = Modifier, text:String, onClick:() -> Unit){

    Button(modifier = modifier, onClick = onClick,
        shape = RoundedCornerShape(20.dp)
            , colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF021A86)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}