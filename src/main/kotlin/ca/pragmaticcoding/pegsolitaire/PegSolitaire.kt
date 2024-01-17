package ca.pragmaticcoding.pegsolitaire

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class PegSolitaire : Application() {
    override fun start(stage: Stage) {
        val scene = Scene(MainController().getView(), 500.0, 440.0).apply {
            PegSolitaire::class.java.getResource("pegsolitaire.css")?.toString()?.let { stylesheets += it }
        }
        stage.title = "Peg Solitaire!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(PegSolitaire::class.java)
}