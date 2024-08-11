package com.ayo.lsp_client.ui.editor

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ayo.lsp_client.editor.EditorViewModel
import com.ayo.lsp_client.editor.SemanticToken
import com.ayo.lsp_client.server.ConnViewModel
import com.ayo.lsp_client.server.initializeLspWebSocket
import com.ayo.lsp_client.ui.editor.files.FilePane
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import lsp_proxy_tools.FileNode
import lsp_proxy_tools.getRootUri
import org.eclipse.lsp4j.Diagnostic

/**
 * 设计并实现应用的启动屏幕，用于输入代理服务器的IP地址并建立连接。
 *
 * 该函数使用了Jetpack Compose进行UI构建，并利用Coroutines处理异步连接操作。
 * 当用户输入有效的IP地址并点击连接按钮时，尝试与代理服务器建立连接，并根据连接结果更新UI。
 */
@Preview
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun StartupScreen() {
    // 提示用户输入的内容
    val prompt = "Address"
    // 用于存储用户输入的IP地址
    var ipAddress by remember { mutableStateOf("10.0.2.2:8001") }
    // 标记是否正在尝试连接
    var connecting by remember { mutableStateOf(false) }
    // 使用ViewModel来处理连接逻辑
    val connectionViewModel: ConnViewModel = viewModel()
    // 创建一个CoroutineScope用于launch异步操作
    val coroutineScope = rememberCoroutineScope()
    // 用于存储连接响应结果
    var response by remember { mutableStateOf("") }
    // 用于存储根URI，连接成功后会使用
    var rootUri by remember { mutableStateOf("") }

    // 主UI布局，填充整个宽度，水平居中
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        // 上半部分布局，垂直居中
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            // 显示输入提示
            Text("Enter proxy IP address:", color = Color.White)
            // 输入框，用于用户输入IP地址
            OutlinedTextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
                label = { Text(prompt) })
            // 连接按钮，只有在输入非空时才显示
            Row(modifier = Modifier.padding(24.dp)) {
                if (ipAddress.isNotEmpty()) {
                    Button(onClick = {
                        coroutineScope.launch {
                            // 设置正在连接状态
                            connecting = true
                            // 尝试连接并获取结果
                            response = connectionViewModel.getResult(ipAddress)
                            // 根据连接结果获取根URI
                            rootUri = getRootUri(ipAddress)
                            // 更新连接状态为完成
                            connecting = false
                        }
                    })
                    {
                        Text(text = "Connect")
                    }
                }
            }
        }
        // 下半部分布局
        Row {
            // 当有有效的响应、IP地址、根URI且当前未处于连接状态时，显示响应或进行相应UI操作
            if (response.isNotEmpty() && ipAddress.isNotEmpty() && rootUri.isNotEmpty() && !connecting) {
                Text(response)
                // 根据响应结果执行相应操作，如显示编辑器或连接动画
                if (response == "OK ✅") {
                    Editor(ipAddress, rootUri)
                } else if (connecting) {
                    // 连接中显示动画
                    CircularProgressIndicator(modifier = Modifier.scale(0.7F))
                }

            }

        }
    }
}

/**
 * Editor函数用于创建一个编辑器界面，支持代码编辑、诊断和 semantic token 功能。
 *
 * @param ipAddress 服务器的IP地址，用于LSP WebSocket连接。
 * @param rootUri 根目录的URI，用于标识代码文件的根目录。
 * @param editorViewModel 编辑器视图模型，负责管理编辑器的状态和数据，如目录结构、当前文件、诊断信息等。
 */
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun Editor(ipAddress: String, rootUri: String, editorViewModel: EditorViewModel = viewModel()) {
    // 记录Scaffold的状态，包括抽屉的状态。
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    // 观察并记录根目录的文件节点状态。
    val rootDirectory: FileNode by editorViewModel.directory.observeAsState(FileNode())
    // 观察并记录当前文件名。
    val currentFile: String by editorViewModel.currentFile.observeAsState("")
    // 从编辑器视图模型中获取诊断信息和语义标记。
    val diagnostics = editorViewModel.diagnostics
    val semanticTokens = editorViewModel.semanticTokens
    // 观察并记录最高级别的诊断信息颜色。
    val highestDiagnostic: Color by editorViewModel.highestDiagnosticSeverity.observeAsState(Color.White)
    // 创建不同作用域以供协程使用。
    val webSocketScope = rememberCoroutineScope()
    val listenerScope = rememberCoroutineScope()
    val drawerScope = rememberCoroutineScope()
    // 记录抽屉的状态。
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Open)
    // 创建并管理消息的流动和列表。
    val messageFlow by remember { mutableStateOf(MutableSharedFlow<String>()) }
    val messageList = remember { mutableStateListOf<String>() }
    val codeOutputList = remember { mutableStateListOf<String>() }
    // 状态管理：会话是否已开始、诊断信息是否可见、当前的标签页索引。
    var sessionStarted by remember { mutableStateOf(false) }
    var diagnosticsVisible by remember { mutableStateOf(false) }
    var tabIndex by remember { mutableStateOf(0) }

    // 会话开始前的初始化工作，包括LSP WebSocket的设置。
    if (!sessionStarted) {
        initializeLspWebSocket(
            onSessionStart = { sessionStarted = true },
            editorViewModel = editorViewModel,
            ipAddress = ipAddress,
            webSocketSendingScope = webSocketScope,
            webSocketListeningScope = listenerScope,
            rootUri = rootUri,
            messageFlow = messageFlow,
            messageOutputList = messageList
        )
    }

    // 构建Scaffold框架，包括顶部栏、主体内容和抽屉内容。
    Scaffold(
        topBar = {
            // 顶部栏的设置，包括应用标题、信息按钮和导航图标。
            TopAppBar(title = {
                EditorAppBar(
                    ipAddress = ipAddress,
                    onInfoPressed = { diagnosticsVisible = !diagnosticsVisible },
                    editorViewModel = editorViewModel,
                    highestDiagnostic = highestDiagnostic,
                    drawerState = drawerState,
                    onRun = {
                        codeOutputList.clear()
                        tabIndex = 0
                    },
                    codeOutputList = codeOutputList,
                )
            }, navigationIcon = {
                // 导航图标的点击事件，用于打开抽屉并刷新诊断信息。
                Icon(
                    Icons.Default.Menu,
                    "Burger menu icon",
                    modifier = Modifier.clickable(onClick = {
                        editorViewModel.getFileDirectory()
                        drawerScope.launch {
                            scaffoldState.drawerState.open()
                            editorViewModel.refreshDiagnostics()
                        }
                    })
                )
            })
        },
        drawerContent = {
            // 抽屉内容，显示文件目录和服务器文件。
            Text(text = "$ipAddress's files", modifier = Modifier.padding(16.dp))
            FilePane(
                rootFileNode = rootDirectory,
                editorViewModel,
                onClick = {
                    editorViewModel.getFile()
                    diagnosticsVisible = false
                    diagnosticsVisible = true
                })
        },
    ) {
        // 主体内容，包括编辑器主体和底部抽屉。
        BottomDrawer(
            modifier = Modifier.padding(it),
            drawerState = drawerState,
            drawerContent = {
                // 底部抽屉内容，包括代码输出、消息列表和标签页切换。
                BottomDrawerContent(
                    drawerState,
                    codeOutputList,
                    messageList,
                    tabIndex,
                    editorViewModel
                ) {
                    tabIndex = it
                }
            }
        ) {
            // 编辑器主体内容，显示代码编辑器、诊断信息和语义标记。
            MainEditorBody(
                diagnosticsVisible = diagnosticsVisible,
                diagnostics = diagnostics,
                semanticTokens = semanticTokens,
                editorViewModel = editorViewModel,
                currentFile = currentFile
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EditorAppBar(
    ipAddress: String,
    onInfoPressed: () -> Unit,
    editorViewModel: EditorViewModel,
    highestDiagnostic: Color,
    drawerState: BottomDrawerState,
    onRun: () -> Unit,
    codeOutputList: SnapshotStateList<String>
) {
    val drawerScope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = ipAddress,
                textAlign = TextAlign.Center
            )
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = {
                onInfoPressed()
                editorViewModel.refreshDiagnostics()
            }) {
                Icon(
                    Icons.Default.Info,
                    "Code Diagnostics",
                    tint = highestDiagnostic
                )
            }
            IconButton(onClick = {
                onRun()
                editorViewModel.runUserCode(codeOutputList)
                drawerScope.launch {
                    drawerState.open()
                }
            }) {
                Icon(Icons.Default.PlayArrow, "Run code")
            }
            IconButton(onClick = {
                editorViewModel.killRunningCode()
                drawerScope.launch {
                    drawerState.open()
                }
            }) {
                Icon(Icons.Default.Clear, "Stop code")
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
/**
 * 主编辑器体，负责渲染编辑器的主要内容。
 *
 * @param diagnosticsVisible 是否显示诊断信息。
 * @param diagnostics 诊断信息列表，用于显示错误或警告。
 * @param semanticTokens 语义标记列表，用于给代码着色等。
 * @param editorViewModel 编辑器视图模型，提供编辑器的状态和行为。
 * @param currentFile 当前编辑的文件名。
 */
private fun MainEditorBody(
    diagnosticsVisible: Boolean,
    diagnostics: List<Diagnostic>,
    semanticTokens: List<SemanticToken>,
    editorViewModel: EditorViewModel,
    currentFile: String
) {
    Column {
        // 当诊断信息可见时，展示诊断信息列表
        Row(Modifier.animateContentSize()) {
            if (diagnosticsVisible) {
                Column(
                    Modifier
                        .padding(8.dp)
                        .animateContentSize()
                ) {
                    diagnostics.forEach {
                        Text(
                            text = it.message,
                            fontSize = 14.sp,
                            color = editorViewModel.colorFromDiagnosticSeverity(it.severity),
                        )
                    }
                }
            }
        }
        // 展示编辑器文本字段
        Row {
            EditorTextField(currentFile, editorViewModel, diagnostics, semanticTokens)
        }
    }
}


/**
 * 创建一个编辑器文本字段，用于显示和编辑当前文件的内容。
 *
 * @param currentFile 当前文件的内容。
 * @param editorViewModel 编辑器视图模型，用于处理文件编辑操作。
 * @param diagnostics 诊断信息列表，用于在文本中高亮显示错误或警告。
 * @param semanticTokens 语义标记列表，用于给文本添加语法高亮。
 */
@Composable
private fun EditorTextField(
    currentFile: String,
    editorViewModel: EditorViewModel,
    diagnostics: List<Diagnostic>,
    semanticTokens: List<SemanticToken>,
) {
    // 使用TextField组件显示当前文件内容，并允许编辑。
    TextField(
        value = currentFile,
        onValueChange = { editorViewModel.editCurrentFile(it) },
        textStyle = TextStyle(color = Color.White, fontSize = 15.sp), // 文本样式设置为白色，字体大小为15sp。
        modifier = Modifier
            .fillMaxWidth() // 设置文本字段填充父容器的宽度。
            .verticalScroll(rememberScrollState()), // 允许垂直滚动，并记住滚动状态。
        visualTransformation = { text ->
            // 计算并应用文本的视觉转换，以支持语法高亮和错误标记。
            val lineIndexes = text.indices.filter { text[it] == '\n' || it == 0 }.map {
                if (it == 0) {
                    0
                } else {
                    it + 1
                }
            }
            TransformedText(
                AnnotatedString(
                    text = text.text,
                    spanStyles = if (text.isNotEmpty()) {
                        // 应用语义标记和诊断信息来设置文本的不同部分的样式。
                        styleSemanticTokens(
                            semanticTokens,
                            editorViewModel,
                            lineIndexes
                        ) + styleDiagnostics(diagnostics, editorViewModel, lineIndexes)
                    } else {
                        emptyList()
                    }
                ), OffsetMapping.Identity
            )
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomDrawerContent(
    drawerState: BottomDrawerState,
    codeOutputList: SnapshotStateList<String>,
    messageList: SnapshotStateList<String>,
    tabIndex: Int,
    editorViewModel: EditorViewModel,
    onTabIndexChange: (Int) -> Unit
) {
    val drawerScope = rememberCoroutineScope()
    val titles = listOf("Output", "LSP")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.padding(8.dp)) {
            Button(
                onClick = { drawerScope.launch { drawerState.close() } },
                content = { Text("Close Drawer") })
        }

        TabRow(selectedTabIndex = tabIndex) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { onTabIndexChange(index) }
                )
            }
        }
        when (tabIndex) {
            0 -> {
                CodeOutputTab(codeOutputList, editorViewModel)
            }

            1 -> {
                DebugLspTab(messageList = messageList)
            }
        }

    }
}

@Composable
private fun DebugLspTab(messageList: SnapshotStateList<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(messageList.reversed()) {
                Text(text = it, fontSize = 14.sp, color = Color.Black)
            }
        }
    }
}

@Composable
private fun CodeOutputTab(
    codeOutputList: SnapshotStateList<String>,
    editorViewModel: EditorViewModel
) {
    var input by remember { mutableStateOf("") }
    val isCodeLoading: Boolean by editorViewModel.isCodeLoading.observeAsState(false)
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 1.dp, max = 150.dp)
                .background(Color.DarkGray)
        ) {
            if (isCodeLoading) {
                codeOutputList.clear()
                CircularProgressIndicator(modifier = Modifier.scale(0.7F))
                codeOutputList.clear()
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .animateContentSize()
                    .verticalScroll(rememberScrollState())
            ) {
                codeOutputList.forEach {
                    Text(
                        text = it, fontSize = 17.sp, color = if (it == "Program exit") {
                            Color.Red
                        } else {
                            Color.White
                        }
                    )
                }
            }
        }
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray),
            textStyle = TextStyle(Color.White),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                editorViewModel.sendInput(input)
            })
        )
    }
}

/**
 * 根据编辑器视图模型和行索引列表，为给定的诊断列表添加样式。
 *
 * @param diagnostics 诊断列表，包含代码中的错误和警告。
 * @param editorViewModel 编辑器视图模型，用于控制界面显示。
 * @param lineIndexes 行索引列表，映射文件中的行号到编辑器中的行索引。
 * @return 返回一个注释字符串列表，每个字符串都对应一个诊断，并且包含了该诊断的样式信息。
 */
private fun styleDiagnostics(
    diagnostics: List<Diagnostic>,
    editorViewModel: EditorViewModel,
    lineIndexes: List<Int>
) = diagnostics
    // 筛选出当前文件范围内的诊断信息
    .filter { diagnostic ->
        editorViewModel.diagnosticInFileRange(
            diagnostic,
            lineIndexes
        )
    }
    // 为每个筛选后的诊断信息添加样式
    .map { diagnostic ->
        AnnotatedString.Range(
            SpanStyle(
                // 根据诊断的严重程度设置颜色，黄色警告使用白色底色
                color = if (editorViewModel.colorFromDiagnosticSeverity(
                        diagnostic.severity
                    ) == Color.Yellow
                ) {
                    Color.White.copy(alpha = 0.8f)
                } else {
                    editorViewModel.colorFromDiagnosticSeverity(
                        diagnostic.severity
                    )
                },
                textDecoration = TextDecoration.Underline
            ),
            // 计算诊断信息在编辑器中的起始和结束位置
            lineIndexes.getOrElse(diagnostic.range.start.line) { 0 } + diagnostic.range.start.character,
            lineIndexes.getOrElse(diagnostic.range.end.line) { 0 } + diagnostic.range.end.character
        )
    }

/**
 * 使用语义标记对文本进行样式设置。
 *
 * 此函数接收一个语义标记列表、编辑器视图模型和行索引列表，然后过滤出位于指定行范围内的语义标记，
 * 并为这些标记生成对应的样式化的文本范围（AnnotatedString.Range）。
 *
 * @param semanticTokens 语义标记列表，代表代码中的不同元素或属性。
 * @param editorViewModel 编辑器视图模型，用于提供UI相关的数据和功能，如颜色映射和范围检查。
 * @param lineIndexes 行索引列表，用于映射原始行号到视图中的行号，以便处理行号的偏移。
 * @return 返回一个样式化文本范围的列表，每个范围对应一个语义标记，并应用了相应的颜色样式。
 */
private fun styleSemanticTokens(
    semanticTokens: List<SemanticToken>,
    editorViewModel: EditorViewModel,
    lineIndexes: List<Int>
) = semanticTokens
    // 过滤出位于指定行范围内的语义标记
    .filter { semanticToken -> editorViewModel.inRange(semanticToken.range, lineIndexes) }
    // 为每个符合条件的语义标记生成样式化的文本范围
    .map { semanticToken ->
        AnnotatedString.Range(
            SpanStyle(
                color = editorViewModel.colorFromSemanticToken(semanticToken) // 从语义标记获取颜色
            ),
            // 计算标记的起始和结束字符位置，考虑行索引的偏移
            lineIndexes.getOrElse(semanticToken.range.start.line) { 0 } + semanticToken.range.start.character,
            lineIndexes.getOrElse(semanticToken.range.end.line) { 0 } + semanticToken.range.end.character
        )
    }
