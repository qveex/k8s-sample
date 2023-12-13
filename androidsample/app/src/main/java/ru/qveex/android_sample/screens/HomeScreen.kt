package ru.qveex.android_sample.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.qveex.android_sample.models.User

@Composable
fun HomeScreen() {
    val viewModel = viewModel<HomeViewModel>()

    val users by viewModel.users.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                error?.let { Text(modifier = Modifier.padding(12.dp), text = it) }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            CreateUserForm(createUser = viewModel::createUsers)
            UserList(users = users)
        }
    }
}

@Composable
fun CreateUserForm(
    createUser: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    ExpandableContent(
        icon = Icons.Outlined.Create,
        title = "Create new user"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
        ) {
            Spacer(modifier = Modifier.padding(0.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") }
            )
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp))
            Button(
                onClick = {
                    runCatching {
                        createUser(
                            name,
                            age.toIntOrNull() ?: return@runCatching
                        )
                    }
                }
            ) {
                Text(text = "Create user", style = MaterialTheme.typography.labelMedium)
            }
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserList(users: List<User>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    ) {
        items(users, key = { it.id }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .animateItemPlacement()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.height(32.dp),
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = it.name)
                            Text(text = "id: ${it.id}")
                        }
                        Text(text = it.age.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableContent(
    icon: ImageVector,
    title: String,
    animationDuration: Int = 600,
    clickableContent: Boolean = false,
    content: @Composable () -> Unit
) {
    var expendedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expendedState) 180f else 0f, label = "rotationState")
    Card(
        modifier = Modifier.padding(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { expendedState = !expendedState },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(32.dp),
                    imageVector = icon,
                    contentDescription = "Expand icon"
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                IconButton(
                    modifier = Modifier
                        .alpha(0.75f)
                        .rotate(rotationState),
                    onClick = { expendedState = !expendedState },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier.run {
                    if (clickableContent) clickable { expendedState = !expendedState }
                    else this
                },
                visible = expendedState,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        easing = FastOutLinearInEasing
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = animationDuration,
                        easing = LinearOutSlowInEasing
                    )
                )
            ) {
                content()
            }
        }
    }

}