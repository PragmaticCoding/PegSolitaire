package ca.pragmaticcoding.pegsolitaire

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty

//@kotlinx.serialization.Serializable
class PegHole(val location: Location) {
    var upperLeft: PegHole? = null
    var upperRight: PegHole? = null
    var lowerLeft: PegHole? = null
    var lowerRight: PegHole? = null
    var left: PegHole? = null
    var right: PegHole? = null
    val isOccupied: BooleanProperty = SimpleBooleanProperty(false)
    val isTarget: BooleanProperty = SimpleBooleanProperty(false)
    val isCandidate: BooleanProperty = SimpleBooleanProperty(false)
    val isInvalid: BooleanProperty = SimpleBooleanProperty(false)
    val isMoving: BooleanProperty = SimpleBooleanProperty(false)
    val row
        get() = location.row
    val column
        get() = location.column

    fun canMoveTo(fromHole: PegHole): PegHole? = if (!isOccupied.value) null
    else when (fromHole) {
        upperLeft -> lowerRight?.takeIf { !it.isOccupied.value }
        upperRight -> lowerLeft?.takeIf { !it.isOccupied.value }
        lowerLeft -> upperRight?.takeIf { !it.isOccupied.value }
        lowerRight -> upperLeft?.takeIf { !it.isOccupied.value }
        right -> left?.takeIf { !it.isOccupied.value }
        left -> right?.takeIf { !it.isOccupied.value }
        else -> null
    }

    fun getNeighbours() = listOfNotNull(upperLeft, upperRight, lowerLeft, lowerRight, left, right)

    fun candidateHoles(): List<PegHole> = getNeighbours().mapNotNull { it.canMoveTo(this) }

    override fun toString() = "[${location.row},${location.column}] ${isCandidate.value}"
}

@kotlinx.serialization.Serializable
data class Location(val row: Int, val column: Int) {
    override fun toString(): String = "[$row,$column]"
}