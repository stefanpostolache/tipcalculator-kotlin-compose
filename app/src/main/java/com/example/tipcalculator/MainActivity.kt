 package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.Purple200
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

 class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                App()
            }
        }
    }
}

@Preview(name = "Default Preview")
@Composable
fun App(){

    var totalPerPerson by remember {
        mutableStateOf(0.00f)
    }

    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        color = MaterialTheme.colors.background) {
        Column {
            TipTotalPerPerson(modifier = Modifier
                .padding(
                    start = 24.dp,
                    top = 24.dp,
                    end = 24.dp,
                    bottom = 0.dp
                )
                .fillMaxWidth()
                .aspectRatio(16f / 7f), totalValue = totalPerPerson)
            BillCalculator {
                totalPerPerson = it
            }
        }

    }
}

@Composable
fun BillCalculator (onCalculationChange: (newValue: Float) -> Unit) {
    val focusManager = LocalFocusManager.current
    var bill by remember {
        mutableStateOf("")
    }
    var splits by remember {
        mutableStateOf(1)
    }
    var tipPercentage by remember { mutableStateOf(0f) }
    bill.toFloatOrNull()?.let {
        onCalculationChange((it + (it * tipPercentage / 100)) / splits)
    }
    Card(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp)) {
        Column (modifier = Modifier.fillMaxWidth()) {
            Surface(modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = bill,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Value in dollars"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {focusManager.clearFocus()}
                    ),
                    onValueChange = {
                        bill = it
                    },
                    singleLine = true,
                    label =  { Text(text = "Enter Bill") },
                    placeholder = { Text(text = "100" )}
                )
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            InlineFormGroup(label = "Split") {
                Row(modifier = Modifier
                    .padding(0.dp)
                    .padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    CircleIconButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Split"
                            )
                        }
                    ) {
                        splits += 1
                    }
                    Text(text = "$splits")
                    CircleIconButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove Split"
                            )
                        }
                    ) {
                        splits -= 1
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            InlineFormGroup(label = "Tip") {
                Surface(modifier = Modifier.padding(end=8.dp)) {
                    val convertedBill = bill.toFloatOrNull()
                    convertedBill?.let {
                        Text(text = "$${"%.2f".format(it*tipPercentage.toInt()/100f)}")
                    }
                }
            }
            Box(modifier = Modifier
                .padding(
                    end = 24.dp,
                    top = 16.dp,
                    start = 24.dp,
                )
                .fillMaxWidth()
                .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center) {
                Text(text = "${tipPercentage.toInt()}%", style = MaterialTheme.typography.h2)
            }
            Surface(modifier = Modifier
                .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
                .fillMaxWidth())
            {
                Slider(
                    value = tipPercentage,
                    valueRange = 0f..100f,
                    steps = 5,
                    onValueChange = {
                        tipPercentage = it
                    }
                )
            }

        }
    }
}

@Composable
fun InlineFormGroup(label: String, input: @Composable (() -> Unit)) {
    Row(modifier = Modifier
        .padding(horizontal = 24.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text=label, style = MaterialTheme.typography.caption)
        input()
    }
}

@Composable
fun CircleIconButton(icon: @Composable (() -> Unit), behaviour: () -> Unit) {
    Card(
        modifier = Modifier
            .width(50.dp)
            .aspectRatio(1f)
            .padding(8.dp)
            .clickable {
                behaviour()
            },
        elevation = 2.dp,
        shape = CircleShape) {
        icon()
    }
}


@Composable
fun TipTotalPerPerson(modifier: Modifier, totalValue: Float) {
        Surface(modifier = modifier,
            color = Purple200,
            shape = RoundedCornerShape(16.dp)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total Per Person",
                    style = MaterialTheme.typography.h2)
                Spacer(modifier = Modifier.padding(2.dp))
                Text("$${String.format("%.2f", totalValue)}",
                    style = MaterialTheme.typography.h1,
                )
        }
    }
}

