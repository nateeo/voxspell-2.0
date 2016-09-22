package voxspell.scenes;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import voxspell.engine.*;
import voxspell.engine.Festival.Operations;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

// TODO: saveAll next unlocked level and voice data

/**
 * Controller for the spelling quiz scene
 * Created by nhur714 on 16/09/16.
 */
public class SpellingController implements Initializable {

    @FXML
    private Button submitButton;
    @FXML
    private Button listenAgainButton;
    @FXML
    private Text levelLabel;
    @FXML
    private TextField inputTextField;
    @FXML
    private TextArea outputTextArea;
    @FXML
    private Label rightLabel;
    @FXML
    private Label wrongLabel;
    @FXML
    private Label progressLabel;
    @FXML
    private Label outOfLabel;
    @FXML
    private VBox vBox;


    // plug in engine modules
    private DataIO data = new DataIO();
    private Festival festival = new Festival(new festivalListener());

    // current quiz
    private ArrayList<Word> words;
    private Word currentWord;
    private boolean currentFaulted = false;

    private boolean disableEnter = false;

    /**
     * listener to handle listen-again requests
     */
    class listenAgainHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            setLoading(true);
            festival.read(currentWord, Operations.LISTEN_AGAIN);
        }
    }

    /**
     * listener to handle festival completing a tts
     */
    class festivalListener implements EventHandler<WorkerStateEvent> {

        @Override
        public void handle(WorkerStateEvent event) {
            setLoading(false);
        }
    }

    /**
     * listener to handle enter key submits
     */
    class enterSubmitHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            if (!disableEnter) {
                submit();
            } else {
                // do nothing
            }
        }
    }

    /**
     * listener to handle submits with the "Go!" button
     */
    class submitHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            submit();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize words and level label using levelData
        vBox.setBackground(SceneManager.makeBackground());
        int level = LevelData.getLevel();
        WordList wordList;
        if (LevelData.isReview()) { // get faulted/wrong words from the level
            words = LevelData.getReviewWords(level);
        } else {
            wordList = new WordList(level);
            words = wordList.getWords();
        }


        LevelData.setCurrentWordList(words); // set global application state
        currentWord = words.get(0);

        levelLabel.setText("Level " + level + (LevelData.isReview() ? " review" : ""));

        // set up outputTextArea
        outputTextArea.setEditable(false);
        outputTextArea.setFocusTraversable(false);
        inputTextField.requestFocus();

        // reset labels and buttons
        reset();

        // add listeners
        submitButton.setOnMouseClicked(new submitHandler());
        inputTextField.setOnAction(new enterSubmitHandler());
        listenAgainButton.setOnMouseClicked(new listenAgainHandler());

        // start quiz
        listenAgainButton.setDisable(false);
        readWord(currentWord);
    }

    private void readWord(Word word) {
        Operations op = currentFaulted ? Operations.TRY_AGAIN : Operations.SPELL;
        // debug
        System.out.println(word);
        // debug end
        setLoading(true);
        festival.read(word, op);

    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            submitButton.setDisable(true);
            submitButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("assets/loading.gif"))));
            submitButton.setText("");
            disableEnter = true;
        } else {
            submitButton.setDisable(false);
            submitButton.setGraphic(null);
            submitButton.setText("Go!");
            disableEnter = false;
        }
    }

    private void submit() {
        String userInput = inputTextField.getText().trim();
        inputTextField.clear();
        inputTextField.requestFocus();
        boolean valid = checkInputValid(userInput);
        if (valid) {
            boolean correct = checkWord(userInput);
            if (correct && !currentFaulted) {
                currentWord.incrementMastered();
                output("Correct");
                incrementLabel(rightLabel);
                nextWord();
            } else if (correct && currentFaulted) {
                currentWord.incrementFaulted();
                output("That's right!");
                currentFaulted = false;
                nextWord();
            } else if (!correct && !currentFaulted) {
                currentFaulted = true;
                output("That's wrong. Try again");
                incrementLabel(wrongLabel); // wrong as soon as faulted
                readWord(currentWord);
            } else { // !correct && currentFaulted
                currentWord.incrementFailed();
                output("That's wrong. The word was " + "\"" + currentWord + "\"");
                currentFaulted = false;
                nextWord();
            }

        } else {
            // invalid input
        }
    }

    /**
     * checks userInput for apostrophes and valid characters, returning true if valid
     * @param userInput
     * @return
     */
    private boolean checkInputValid(String userInput) {
        if (!userInput.matches("[a-zA-Z]+")) {
            boolean wordHasApostrophe = currentWord.toString().contains("'");
            boolean userInputHasApostrophe = userInput.contains("'");
            if (wordHasApostrophe && !userInputHasApostrophe) {
                // word contains an apostrophe but userInput does not
                output("Oops, that's wrong. The word has an apostrophe (')");
                return false;
            } else if (!wordHasApostrophe && userInputHasApostrophe) {
                // word does not contain an apostrophe but userInput does
                output("Oops, that's wrong. The word does NOT have an apostrophe (')");
                return false;
            } else if (wordHasApostrophe && userInputHasApostrophe) {
                return true;
            } else if (currentWord.toString().contains(" ") && userInput.contains(" ")) {
                // valid if word and input both contain spaces
                return true;
            }
            else {
                // invalid input
                output("Oops, you entered something wrong. Try again.");
                return false;
            }
        }
        return true;
    }

    /**
     * check if user input is spelt correctly
     */
    private boolean checkWord(String userInput) {
        return userInput.trim().equalsIgnoreCase(currentWord.toString());
    }

    /**
     * get next word as currentWord
     */
    private void nextWord() {
        inputTextField.clear();
        inputTextField.requestFocus();
        int index = words.indexOf(currentWord);
        if (index < words.size() - 1) {
            incrementLabel(progressLabel);
            currentWord = words.get(index + 1);
            readWord(currentWord);
        } else {
            // TODO: finished, GO TO NEXT LEVEL
            // enable next level
            if (Integer.parseInt(rightLabel.getText()) >= 9 && LevelData.getLevel() < 10) {
                data.enableLevel(LevelData.getLevel() + 1);
            }
            // save level stats
            data.addWordList(words, LevelData.getLevel());
            goToEnd();
        }
    }

    /**
     * increments a label's value
     * @param label
     */
    private void incrementLabel(Label label) {
        int current = Integer.parseInt(label.getText());
        label.setText(Integer.toString(current + 1));
    }

    /**
     * reset labels and counts
     */
    private void reset() {
        listenAgainButton.setDisable(true);
        rightLabel.setText("0");
        wrongLabel.setText("0");
        progressLabel.setText("1");
        outOfLabel.setText("out of " + words.size());
        currentFaulted = false;
    }

    private void output(String message) {
        outputTextArea.setText("\n\n" + message);
    }

    private void goToEnd() {
        SceneManager.goTo("endSession.fxml");
    }
}
