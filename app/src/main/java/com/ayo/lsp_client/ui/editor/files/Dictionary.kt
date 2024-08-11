package com.ayo.lsp_client.ui.editor.files

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ayo.lsp_client.editor.EditorViewModel
import lsp_proxy_tools.FileNode

@ExperimentalAnimationApi
@Composable
fun FilePane(rootFileNode: FileNode, editorViewModel: EditorViewModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .animateContentSize()
    ) {
        DrawFileNode(rootFileNode, rootFileNode, editorViewModel, onClick)
    }
}

@ExperimentalAnimationApi
@Composable
fun DrawFileNode(
    root: FileNode,
    fileNode: FileNode,
    editorViewModel: EditorViewModel,
    onClick: () -> Unit
) {
    var childrenVisible by remember { mutableStateOf(false) }
    FileItem(root, fileNode, editorViewModel, onClick, { childrenVisible = !childrenVisible })
    if (childrenVisible || !fileNode.isDirectory()) {
        if (fileNode.children.isNotEmpty()) {
            for (child in fileNode.children) {
                child.parent = fileNode
                DrawFileNode(root, child, editorViewModel, onClick)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun FileItem(
    root: FileNode,
    fileNode: FileNode,
    editorViewModel: EditorViewModel,
    onClick: () -> Unit,
    onOpen: () -> Unit
) {
    val padding = 8.dp
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .padding(padding)
            .clickable(onClick = {
                if (!fileNode.isDirectory()) {
                    editorViewModel.currentPath = fileNode.getPath(root)
                    editorViewModel.getCurrentFile()
                    onClick()
                }
            })
            .fillMaxWidth()
    ) {
        Column {
            var newFileDialog by remember { mutableStateOf(false) }
            //val dismissDialog = { newFileDialog = false }
            var name by remember { mutableStateOf("") }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (fileNode.isDirectory()) {
//                    if(!fileNode.isRoot()) {
//                        Spacer(Modifier.padding(4.dp))
//                    }
                    Column(horizontalAlignment = Alignment.Start) {
                        IconButton(onClick = { onOpen() }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                "Directory burger menu",
                                tint = Color.White
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = fileNode.name,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(onClick = { newFileDialog = !newFileDialog }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                "Create new file",
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    Text(text = fileNode.name, color = Color.White)
                }
            }
            AnimatedVisibility(visible = newFileDialog) {
                Column {
                    TextField(
                        modifier = Modifier,
                        value = name,
                        onValueChange = { name = it })
                    Row(horizontalArrangement = Arrangement.End) {
                        IconButton(
                            onClick = {
                                if (name.isNotEmpty()) {
                                    editorViewModel.createNewFile(
                                        directoryPath = fileNode.getPath(root),
                                        fileName = name
                                    )
                                    editorViewModel.getFileDirectory()
                                    newFileDialog = false
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Create file",
                            )
                        }
                        IconButton(
                            onClick = {
                                newFileDialog = false
                            }
                        ) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Cancel creation of file",
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFileDialog(
    onDismiss: () -> Unit,
    editorViewModel: EditorViewModel,
    fileNode: FileNode,
    rootFileNode: FileNode
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            TextField(
                modifier = Modifier,
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "File Name") },
                textStyle = TextStyle(Color.White),
                singleLine = true,
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        editorViewModel.createNewFile(
                            directoryPath = fileNode.getPath(rootFileNode),
                            fileName = name
                        )
                        onDismiss()
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}