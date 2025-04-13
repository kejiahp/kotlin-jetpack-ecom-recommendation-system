package com.example.mobile.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.mobile.R

@Composable
fun CustomText(
    text: String, modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        textDecoration = textDecoration,
        textAlign = textAlign,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        fontSize = fontSize,
        overflow = overflow,
        lineHeight = lineHeight,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledOutlinedTextField(
    modifier: Modifier = Modifier,
    keyboardOpt: KeyboardOptions = KeyboardOptions.Default,
    value: String,
    onChange: (String) -> Unit,
    emptyFieldHandler: () -> Unit,
    imgVec: ImageVector,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    textFieldError: String? = null,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = { Icon(imgVec, contentDescription = "leading icon") },
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = emptyFieldHandler) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Text")
                }
            }
        },
        keyboardOptions = keyboardOpt,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        ),
        isError = textFieldError != null,
    )
    if (textFieldError != null) {
        Text(text = textFieldError, color = Color.Red, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomExposedDropdownMenu(
    selectedText: String,
    onSelectedText: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    label: @Composable (() -> Unit),
    textFieldError: String? = null,
) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            isError = textFieldError != null,
            value = selectedText,
            onValueChange = onSelectedText,
            readOnly = true, // Makes it behave like a dropdown
            label = label,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { CustomText(option) },
                    onClick = {
                        onSelectedText(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
    if (textFieldError != null) {
        Text(text = textFieldError, color = Color.Red, fontSize = 12.sp)
    }
}


@Composable
fun PasswordTextField(
    labelText: String,
    placeholderText: String,
    password: String,
    onChangePassword: (String) -> Unit,
    textFieldError: String? = null,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onChangePassword,
        label = { CustomText(labelText) },
        placeholder = { CustomText(placeholderText) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock icon") },
        trailingIcon = {
            val painterRes =
                if (passwordVisible) painterResource(R.drawable.visibility_icon) else painterResource(
                    R.drawable.visibility_off_icon
                )
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painterRes, contentDescription = "Toggle Password Visibility")
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
    if (textFieldError != null) {
        Text(text = textFieldError, color = Color.Red, fontSize = 12.sp)
    }
}

@Composable
fun BackButton(onBackClick: () -> Unit) {
    IconButton(onClick = onBackClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black
        )
    }
}

@Composable
fun BackButtonWithTitle(
    title: String,
    titleModifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    titleOverflow: TextOverflow = TextOverflow.Clip,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBackClick = onBackClick)
        Spacer(modifier = Modifier.width(10.dp))
        CustomText(
            modifier = titleModifier,
            text = title, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, maxLines = maxLines,
            overflow = titleOverflow,
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun Loader(size: Dp = 45.dp, color: Color = MaterialTheme.colorScheme.primary) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(size)
            .padding(10.dp), color = color
    )
}

@Composable
fun LoaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Loader()
    }
}

@Composable
fun LoaderScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Loader(size = 60.dp)
    }
}

@Composable
fun ErrorScreen(errorMsg: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText(errorMsg, color = Color.Red, fontSize = 24.sp)
    }
}

@Composable
fun CustomButton(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = MaterialTheme.shapes.small,
    onClickBtn: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        enabled = enabled,
        modifier = modifier,
        colors = colors,
        shape = shape,
        onClick = onClickBtn,
        content = content
    )
}

@Composable
fun LoaderButton(
    isLoading: Boolean,
    enabled: Boolean = true,
    onClickBtn: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        // disable the button if loading is true
        enabled = enabled && !isLoading,
        modifier = Modifier.size(width = 200.dp, height = 60.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = onClickBtn
    ) {
        if (isLoading) {
            Loader(color = ButtonDefaults.buttonColors().contentColor)
        } else {
            content()
        }
    }
}

@Composable
fun <T> SearchableDropdown(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T?) -> String,
    itemContent: @Composable (T) -> Unit = { Text(itemLabel(it)) },
    onChangeHandler: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(itemLabel(selectedItem)) }
    val filteredItems by remember(searchQuery, items) {
        derivedStateOf {
            items.filter { itemLabel(it).contains(searchQuery, ignoreCase = true) }
        }
    }

    val focusManager = LocalFocusManager.current

    // This keeps track of where to anchor the dropdown
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    var textFieldPosition by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = modifier.onGloballyPositioned {
        textFieldSize = it.size
        textFieldPosition = it.localToWindow(Offset.Zero)
    }) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                if (onChangeHandler != null) {
                    onChangeHandler(it)
                }
                searchQuery = it
                expanded = true
            },
            label = { CustomText("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    textFieldSize = it.size
                },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    }
                )
            },
            singleLine = true
        )

        DropdownContent(
            expanded = expanded,
            itemContent = {
                if (filteredItems.isEmpty()) {
                    Text("No results", modifier = Modifier.padding(16.dp))
                } else {
                    filteredItems.forEach { item ->
                        DropdownMenuItem(
                            text = { itemContent(item) },
                            onClick = {
                                searchQuery = itemLabel(item)
                                onItemSelected(item)
                                expanded = false
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            },
            width = textFieldSize.width,
            offsetY = textFieldSize.height, // Show directly below TextField
            onDismiss = { expanded = false }
        )
    }
}

@Composable
fun DropdownContent(
    expanded: Boolean,
    itemContent: @Composable ColumnScope.() -> Unit,
    width: Int,
    offsetY: Int,
    onDismiss: () -> Unit
) {
    if (expanded) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(x = 0, y = offsetY),
            onDismissRequest = onDismiss,
            properties = PopupProperties(focusable = false)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 4.dp,
                modifier = Modifier
                    .width(with(LocalDensity.current) { width.toDp() })
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    itemContent()
                }
            }
        }
    }
}


//@Preview()
//@Composable
//fun PreviewSomeStuff() {
//
//}
