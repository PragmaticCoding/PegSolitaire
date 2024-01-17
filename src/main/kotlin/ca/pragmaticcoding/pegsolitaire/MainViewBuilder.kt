package ca.pragmaticcoding.pegsolitaire

import javafx.beans.InvalidationListener
import javafx.beans.value.ObservableBooleanValue
import javafx.css.PseudoClass
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle
import javafx.util.Builder

class MainViewBuilder(
    private val model: MainModel,
    private val dragStarter: (pegHole: PegHole) -> Unit,
    private val dragDropper: (pegHole: PegHole) -> Unit,
    private val dragStopper: () -> Unit,
    private val gameStarter: () -> Unit
) : Builder<Region> {
    override fun build(): Region = BorderPane().apply {
        center = createCentre()
        bottom = HBox(Button("New Game").apply { onAction = EventHandler { gameStarter.invoke() } }).apply {
            alignment = Pos.CENTER
            padding = Insets(6.0)
        }
        sceneProperty().addListener(InvalidationListener {
            scene?.let {
                it.onMouseDragReleased = EventHandler { dragStopper.invoke() }
            }
        })
    }

    companion object {
        val HOLE_OCCUPIED_PC: PseudoClass = PseudoClass.getPseudoClass("occupied")
        val HOLE_TARGET_PC: PseudoClass = PseudoClass.getPseudoClass("target")
        val HOLE_CANDIDATE_PC: PseudoClass = PseudoClass.getPseudoClass("candidate")
        val HOLE_INVALID_PC: PseudoClass = PseudoClass.getPseudoClass("invalid")
        val HOLE_MOVING_PC: PseudoClass = PseudoClass.getPseudoClass("moving")
    }

    private fun createCentre(): Region = VBox().apply {
        for (row in 1..5) {
            children += HBox(3.0).apply {
                children += model.pegHoles.filtered { pegHole -> (pegHole.row == row) }
                    .sortedWith(compareBy { it.column }).map { createCircle(it) }
                alignment = Pos.CENTER
            }
        }
        alignment = Pos.CENTER
    }

    private fun createCircle(pegHole: PegHole): Node = Circle(30.0).apply {
        styleClass += "peg-hole"
        bindPseudoCode(HOLE_OCCUPIED_PC, pegHole.isOccupied)
        bindPseudoCode(HOLE_TARGET_PC, pegHole.isTarget)
        bindPseudoCode(HOLE_CANDIDATE_PC, pegHole.isCandidate)
        bindPseudoCode(HOLE_INVALID_PC, pegHole.isInvalid)
        bindPseudoCode(HOLE_MOVING_PC, pegHole.isMoving)
        onDragDetected = EventHandler {
            if (pegHole.isOccupied.value) {
                startFullDrag()
                dragStarter.invoke(pegHole)
            }
        }
        onMouseDragOver = EventHandler {
            if (pegHole.isCandidate.value) {
                pegHole.isTarget.value = true
            } else pegHole.isInvalid.value = true
        }
        onMouseDragExited = EventHandler {
            pegHole.isInvalid.value = false
            pegHole.isTarget.value = false
        }
        onMouseDragReleased = EventHandler {
            println("Drag released: $pegHole")
            if (pegHole.isCandidate.value == true) {
                dragDropper.invoke(pegHole)
            }
        }
    }
}

fun <T : Node> T.bindPseudoCode(pseudoClass: PseudoClass, property: ObservableBooleanValue) = apply {
    pseudoClassStateChanged(pseudoClass, property.value)
    property.addListener(InvalidationListener {
        pseudoClassStateChanged(pseudoClass, property.value)
    })
}
