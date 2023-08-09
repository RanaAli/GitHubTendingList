package com.rana.githubtrendinglist.ui.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rana.githubtendinglist.R

@Composable
fun DarkModeMenu(isDarkMode: Boolean, toggleDarkMode: (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true },
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = "More options"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        Row(
            Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.dark_theme),
                Modifier.padding(top = 10.dp, end = 10.dp)
            )
            Switch(checked = isDarkMode, onCheckedChange = toggleDarkMode)
        }
    }
}