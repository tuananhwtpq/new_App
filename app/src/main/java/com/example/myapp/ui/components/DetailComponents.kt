package com.example.myapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.theme.MyAppTheme

@Composable
fun InfoItem(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title, style = MaterialTheme.typography.bodySmall, color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun InfoItem2(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = title, style = MaterialTheme.typography.bodySmall, color = Color.Gray,
            fontWeight = FontWeight.Bold
        )

    }
}


@Composable
fun TagChip(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.LightGray.copy(alpha = 0.5f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@Preview
@Composable
fun InfoItemPreview() {

    MyAppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            InfoItem(title = "Camera", value = "Canon EOS 5D Mark IV")
        }
    }
}

@Preview
@Composable
fun TagChipPreview() {
    MyAppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            TagChip(text = "Cat")
        }
    }
}

@Preview
@Composable
fun InfoItemPreview2() {

    MyAppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            InfoItem2(title = "Camera", value = "Canon EOS 5D Mark IV")
        }
    }
}