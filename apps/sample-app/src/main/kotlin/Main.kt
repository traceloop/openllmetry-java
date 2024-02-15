
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import com.traceloop.sdk.Client
import java.time.LocalDate
import java.util.Date

val traceloopApiKey = System.getenv("TRACELOOP_API_KEY")
val openaiKey: String? = System.getenv("OPENAI_API_KEY")

fun main(args: Array<String>) {
    val traceloop = Client.newBuilder()
        .baseUrl("https://api-staging.traceloop.com")
        .apiKey(traceloopApiKey)
        .build()

    traceloop.start()

    val service = OpenAiService(openaiKey)
    val date = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd"))
    val request = traceloop.getPrompt("example-prompt", mapOf("date" to date))
    val response = service.createChatCompletion(request)

    println(response.choices[0].message.content)

    traceloop.stop()
}
