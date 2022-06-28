package com.example.github.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.data.data.SearchData
import com.example.github.ui.navigation.Route
import com.example.github.ui.view.BackTopAppBar
import com.example.github.ui.view.CommonTextField
import com.example.github.ui.view.EllipsesText
import com.example.github.vm.SearchViewModel

@Composable
fun SearchScreen(navController: NavHostController, searchViewModel: SearchViewModel = viewModel()) {
    Scaffold(
        topBar = {
            BackTopAppBar(navController = navController, text = stringResource(R.string.search))
        },
        content = {
            Column(
                Modifier
                    .padding(it.calculateBottomPadding())
                    .fillMaxWidth()) {

                CommonTextField(
                    text = searchViewModel.searchText.collectAsState(),
                    label = stringResource(R.string.search),
                    onValueChange = searchViewModel::onSearchTextChanged,
                    onDone = searchViewModel::search,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                )

               UserItem(
                   navController = navController,
                   usersState = searchViewModel.data.collectAsState())
            }
        }
    )
}

@Composable
private fun UserItem(navController: NavHostController, usersState: State<SearchData?>) {
    val users = usersState.value?.items

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 6.dp), state = rememberLazyListState()) {
        if (!users.isNullOrEmpty()) {
            items(users) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Route.User.route)
                            navController.currentBackStackEntry?.savedStateHandle?.set(Route.USER_DATA, it)
                        }
                ) {
                    Box(Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                        EllipsesText(it.username)
                    }
                }
            }
        }
    }
}