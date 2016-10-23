package voxspell.scenes.controllers;

import javafx.scene.Node;
import javafx.scene.Scene;
import voxspell.Voxspell;

import java.util.ArrayList;

/**
 * Class to hold constants for css classnames and helper function to set style
 * Created by nhur714 on 11/10/16.
 */
public final class classNames {

    enum Style {

        // buttons

        LEVEL_BUTTON("levelButton"),
        MAIN_MENU_BUTTON("mainMenuButton"),
        DROP_DOWN_BUTTON("dropDownButton"),
        BUTTON("button"),

        PRIMARY("primary"),
        SECONDARY("secondary"),
        TERTIARY("tertiary"),
        NEUTRAL("neutral"),
        CANCEL("cancel"),

        // text

        TITLE("title"),
        SUBTITLE("subtitle"),
        DISPLAY_TEXT("displayText"),
        TEXT("text");

        private String style;

        Style(String style) {
            this.style = style;
        }
        public String toString() {
            return this.style;
        }
    }

    public static void linkStyleSheet(Scene scene) {
        scene.getStylesheets().add(Voxspell.class.getResource("scenes/main.css").toExternalForm());
    }
    public static void setStyle(Node node, Style ... styles) {
        ArrayList<String> styleStrings = new ArrayList<String>();
        for (Style style : styles) {
            styleStrings.add(style.toString());
        }
        node.getStyleClass().addAll(styleStrings);
    }

    public static void setStyle(Node node, Style style) {
        node.getStyleClass().add(style.toString());
    }
}
