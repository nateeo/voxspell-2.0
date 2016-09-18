# voxspell
```
src
    ├── tests ** test suite for selected classes **
    │   ├── FestivalTest.java
    │   ├── VoxspellTestSuite.java
    │   └── WordListTest.java
    └── voxspell
        ├── Voxspell.java ** entry point for application **
        ├── engine ** package for back-end / functionality **
        │   ├── DataIO.java                                         // deals with file IO and saving
        │   ├── Festival.java                                       // deals with festival tts and voice changing
        │   ├── LevelData.java                                      // static link class to store global application state (levels, etc.)
        │   ├── Word.java                                           // class to represent a word
        │   └── WordList.java                                       // class to parse file and get lists of words
        └── scenes ** package for front-end and controllers **
            ├── MainController.java                                 // controller for main/welcome scene
            ├── SpellingController.java                             // controller for spelling scene
            ├── main.fxml                                           // main/welcome scene for level selection
            └── spelling.fxml                                       // spelling scene for quizzes
```