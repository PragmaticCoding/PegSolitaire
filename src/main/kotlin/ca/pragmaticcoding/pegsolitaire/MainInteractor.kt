package ca.pragmaticcoding.pegsolitaire

class MainInteractor(private val model: MainModel) {

    init {
        for (row in 1..5) {
            for (column in 1..row) {
                model.pegHoles += PegHole(Location(row, column))
            }
        }
        model.pegHoles.forEach { pegHole: PegHole ->
            pegHole.upperLeft = model.getPegHole(pegHole.row - 1, pegHole.column - 1)
            pegHole.upperRight = model.getPegHole(pegHole.row - 1, pegHole.column)
            pegHole.lowerLeft = model.getPegHole(pegHole.row + 1, pegHole.column)
            pegHole.lowerRight = model.getPegHole(pegHole.row + 1, pegHole.column + 1)
            pegHole.left = model.getPegHole(pegHole.row, pegHole.column - 1)
            pegHole.right = model.getPegHole(pegHole.row, pegHole.column + 1)
        }
    }

    fun resetGame() {
        model.pegHoles.forEach { pegHole -> pegHole.isOccupied.value = true }
        model.pegHoles.shuffled()[0].isOccupied.value = false
    }

    fun startMove(pegHole: PegHole) {
        resetMove()
        pegHole.candidateHoles().forEach { it.isCandidate.value = true }
        pegHole.isMoving.value = true
    }

    fun resetMove() {
        model.pegHoles.forEach {
            it.isCandidate.value = false
            it.isMoving.value = false
            it.isInvalid.value = false
        }
    }

    fun makeMove(destinationHole: PegHole) {
        model.pegHoles.firstOrNull { it.isMoving.value }?.let { movingPeg ->
            movingPeg.getNeighbours().intersect(destinationHole.getNeighbours().toSet()).firstOrNull()?.let {
                it.isOccupied.value = false;
            }
            movingPeg.isOccupied.value = false
            destinationHole.isOccupied.value = true
        }
    }
}