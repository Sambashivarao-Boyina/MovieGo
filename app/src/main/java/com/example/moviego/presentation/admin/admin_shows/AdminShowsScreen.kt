package com.example.moviego.presentation.admin.admin_shows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.domain.model.Show
import com.example.moviego.presentation.admin.components.ShowCard
import com.example.moviego.presentation.navgraph.Route

@Composable
fun AdminShowsScreen(
    shows:List<Show>,
    navController: NavHostController
) {
   Scaffold {
      Column(
          modifier = Modifier.padding(it)
      ) {
          LazyColumn(
              modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(20.dp)
          ) {
              items(shows) { show ->
                  ShowCard(show = show, onClick = {
                      navController.navigate(Route.AdminShowDetails.passShowId(showId =  show._id))
                  })
              }
          }
      }
   }
}