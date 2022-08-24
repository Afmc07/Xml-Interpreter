import org.w3c.dom.Element
import java.io.*
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

fun copyFileUsingStream(sourceName: String, destinationName: String) {
    var inputS: InputStream? = null
    var outputS: OutputStream? = null
    try {
        inputS = FileInputStream(File(sourceName))
        outputS = FileOutputStream(File(destinationName))
        val buffer = ByteArray(1024)
        var length: Int
        while (inputS.read(buffer).also { length = it } > 0) {
            outputS.write(buffer, 0, length)
        }
    } finally {
        inputS!!.close()
        outputS!!.close()
    }
}

fun prepareDocument(fileName : String): Element {
    val xmlFile = File(fileName)
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val parsedDocument = builder.parse(xmlFile)
    return parsedDocument.documentElement
}

fun main (args : Array<String>) {
    val interpreter = Interpreter()
    copyFileUsingStream("Jflap.jff", "Jflap.xml")
    val document = prepareDocument("Jflap.xml")
    val graph = interpreter.createGraph(document)

    val data = Scanner(System.`in`)
    print("Provide a String: ")
    val input = data.next()

    if(interpreter.validateInput(input, graph)) {
        print("Input is a valid")
    }
}

