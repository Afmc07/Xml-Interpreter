import com.google.common.graph.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

class Interpreter {

    private lateinit var alphabet : ArrayList<String>
    fun createGraph(document : Element): MutableValueGraph<Int, String> {
        alphabet = arrayListOf()
        val stateNodes : NodeList =  document.getElementsByTagName("state")
        val transitionNodes : NodeList = document.getElementsByTagName("transition")
        val Automaton : MutableValueGraph<Int, String> = ValueGraphBuilder
            .directed()
            .allowsSelfLoops(true)
            .expectedNodeCount(stateNodes.length)
            .build()

        for (index in 0 until stateNodes.length){
           val stateNode = stateNodes.item(index)
            if (stateNode.nodeType == Node.ELEMENT_NODE) {
                val stateElement : Element = stateNode as Element
                val stateId = stateElement.getAttribute("id")
                Automaton.addNode(stateId.toInt())
            }
        }

        for (index in 0 until transitionNodes.length){
            val transitionNode = transitionNodes.item(index)
            if (transitionNode.nodeType == Node.ELEMENT_NODE) {
                val transitionElement : Element = transitionNode as Element
                val transitionOrigin = transitionElement.getElementsByTagName("from").item(0).textContent.toInt()
                val transitionDestination = transitionElement.getElementsByTagName("to").item(0).textContent.toInt()
                val transitionValue = transitionElement.getElementsByTagName("read").item(0).textContent
                val existingEdgeValue = Automaton.edgeValue(transitionOrigin, transitionDestination)

                if (existingEdgeValue.isPresent) {
                    val test = "${existingEdgeValue.get()} $transitionValue"
                    Automaton.putEdgeValue(transitionOrigin, transitionDestination, test)
                } else {
                    Automaton.putEdgeValue(transitionOrigin, transitionDestination, transitionValue)
                }

                alphabet.add(transitionValue)
            }

        }

        return Automaton
    }

    fun validateInput(input: String, graph: MutableValueGraph<Int, String>): Boolean {
        var currentNode = 0

        for (char in input) {
            if (!alphabet.contains(char.toString())) {
                print("Error: ($char) not in alphabet")
                return false
            }
            else {
                val successors = graph.successors(currentNode)
                for (index in 0 until successors.size) {
                    val successor = successors.elementAt(index)
                    val currentRule = graph.edgeValue(currentNode, successor).get()
                    if (char in currentRule) {
                        currentNode = successor
                    } else if (index == successors.size - 1) {
                        print("Error: ($char) misplaced")
                        return false
                    }
                }
            }
        }

        return true
    }
}