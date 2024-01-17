package ca.pragmaticcoding.pegsolitaire

class MainController {

    val model = MainModel()
    val interactor = MainInteractor(model)
    val viewBuilder = MainViewBuilder(model, this::startDrag, this::doneDrag, this::stopDrag, this::reset)

    init {
        reset()
    }

    private fun reset() {
        interactor.resetGame()
    }

    private fun stopDrag() {
        interactor.resetMove()
    }

    private fun startDrag(pegHole: PegHole) {
        interactor.startMove(pegHole)
    }

    private fun doneDrag(pegHole: PegHole) {
        interactor.makeMove(pegHole)
    }

    fun getView() = viewBuilder.build()
}