package com.ayo.lsp_client

import android.os.Build
import androidx.annotation.RequiresApi
import com.ExampleServer
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionList
import org.eclipse.lsp4j.CompletionParams
import org.eclipse.lsp4j.MessageActionItem
import org.eclipse.lsp4j.MessageParams
import org.eclipse.lsp4j.Position
import org.eclipse.lsp4j.PublishDiagnosticsParams
import org.eclipse.lsp4j.ShowMessageRequestParams
import org.eclipse.lsp4j.TextDocumentIdentifier
import org.eclipse.lsp4j.TraceValue.Messages
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.LanguageClient
import org.eclipse.lsp4j.services.LanguageServer
import java.util.concurrent.CompletableFuture


//单例模式
object ExampleClient {

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(Exception::class)
    fun init() {
        val client: LanguageClient = MyLanguageClient()

        val server: LanguageServer = ExampleServer()

//        // 建立与语言服务器的连接
//        val future: CompletableFuture<Void> = server
//
//        // 等待连接建立完成
//        future.get()

        // 创建一个补全请求
        val params: CompletionParams = CompletionParams(
            TextDocumentIdentifier("file:///path/to/your/file.txt"),  // 文件URI
            Position(0, 2) // 补全的位置：第1行（行号从0开始），第3个字符

        )

        // 发送补全请求
        val completions = server.textDocumentService.completion(params)

        // 处理补全结果
        completions.thenAccept { result: Either<List<CompletionItem>, CompletionList> ->
            if (result.isLeft) {
                val items = result.left
                for (item in items) {
                    if (item.label == "hello") {
                        // 执行补全操作（这里仅打印，实际应用中应将补全内容插入文本）
                        println("Code completion: " + item.label)
                    }
                }
            } else {
                val list = result.right
                // 类似处理...
            }
        }

        // 保持程序运行，直到手动停止
        Thread.sleep(Long.MAX_VALUE)
    }

    private class MyLanguageClient : LanguageClient {
        /**
         * The telemetry notification is sent from the server to the client to ask
         * the client to log a telemetry event.
         */
        override fun telemetryEvent(obj: Any) {

        }

        /**
         * Diagnostics notifications are sent from the server to the client to
         * signal results of validation runs.
         */
        override fun publishDiagnostics(diagnostics: PublishDiagnosticsParams?) {

        }

        /**
         * The show message notification is sent from a server to a client to ask
         * the client to display a particular message in the user interface.
         */
        override fun showMessage(messageParams: MessageParams?) {
            TODO("Not yet implemented")
        }

        // 实现必要的方法，这里为了简化示例，可以暂时留空或提供默认实现
        override fun showMessageRequest(params: ShowMessageRequestParams): CompletableFuture<MessageActionItem>? {
            return null
        }

        /**
         * The log message notification is send from the server to the client to ask
         * the client to log a particular message.
         */
        override fun logMessage(message: MessageParams?) {

        }
    }
}