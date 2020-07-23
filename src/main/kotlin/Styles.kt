
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val display by cssclass()
        val labelBold by cssclass()
    }

    init {
        button {
            fontWeight = FontWeight.BOLD
            fontFamily = "Monospace"
            backgroundColor += c("lightgrey")
            borderColor += box(
                top = c("#979797"),
                right = c("#979797"),
                left = c("#979797"),
                bottom = c("#979797")
            )
            borderWidth = multi(box(0.5.px))
           // prefWidth = 50.px
            //prefHeight = 50.px
            fontSize = 25.px
            backgroundInsets = multi(box(0.px))
            padding = box(7.px)
        }

        label {
            fontFamily = "Monospace"
            fontSize = 18.px
        }

        labelBold {
            label
            fontWeight = FontWeight.BOLD
            fontSize = 18.px
            padding = box(7.px)
        }

        form {
            backgroundColor += c("lightgrey")
        }

        display {
            fontWeight = FontWeight.BOLD
            fontFamily = "Monospace"
            backgroundColor += c("lightblue")
            fontSize = 30.px
            padding = box(7.px)
        }

    }
}