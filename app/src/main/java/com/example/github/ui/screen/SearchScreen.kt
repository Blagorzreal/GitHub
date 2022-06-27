package com.example.github.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.github.R
import com.example.github.ui.view.BackTopAppBar
import com.example.github.ui.view.CommonTextField
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
            }
        }
    )
}