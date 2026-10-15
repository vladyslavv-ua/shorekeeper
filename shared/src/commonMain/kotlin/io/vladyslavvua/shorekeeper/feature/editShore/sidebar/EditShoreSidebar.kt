package io.vladyslavvua.shorekeeper.feature.editShore.sidebar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.vladyslavvua.shorekeeper.feature.editShore.dto.TabElement
import io.vladyslavvua.shorekeeper.navigation.EditShore
import org.jetbrains.compose.resources.painterResource

@Composable
fun EditShoreSidebar(tabs: List<TabElement>, selectedTab: EditShore.Tab, navigate: (EditShore.Tab) -> Unit) {
    NavigationRail(modifier = Modifier.width(64.dp)) {
      tabs.forEach { element->
          NavigationRailItem(
              selected = selectedTab == element.destination,
              label = {
                  Text(element.name, textAlign = TextAlign.Center)
              },
              icon = {
                  Icon(painterResource(element.icon), contentDescription = null, modifier = Modifier.size(36.dp))
              },
              onClick = { navigate(element.destination) },
              modifier = Modifier.padding(horizontal = 8.dp)
          )

      }
    }
}