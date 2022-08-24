package AdjacencyList

class Graph {
    private var adjVertex :  MutableMap<Vertex, MutableList<Vertex>> = emptyMap<Vertex, MutableList<Vertex>>() as MutableMap<Vertex, MutableList<Vertex>>

    fun addVertex(id: String) {
        adjVertex.putIfAbsent(Vertex(id), emptyList<Vertex>() as MutableList)
    }

    fun addEdge(originId: String, destinationId: String ) {
        val v1 = Vertex(originId)
        val v2 = Vertex(destinationId)
        adjVertex[v1]?.add(v2)
    }

    fun getAdjVertexes(id: String): MutableList<Vertex>? {
        val ver = Vertex(id)
        return adjVertex[ver]
    }
}