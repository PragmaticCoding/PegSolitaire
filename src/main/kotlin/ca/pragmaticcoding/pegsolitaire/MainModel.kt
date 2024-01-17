package ca.pragmaticcoding.pegsolitaire

import javafx.collections.FXCollections
import javafx.collections.ObservableList

class MainModel {

    val pegHoles: ObservableList<PegHole> = FXCollections.observableArrayList()

    fun getPegHole(row: Int, column: Int): PegHole? =
        pegHoles.firstOrNull { pegHole -> ((pegHole.row == row) && (pegHole.column == column)) }
}