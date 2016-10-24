# voxspell 2.0
#### A spelling application targeted at native English learners aged 7-10, developed by Nathan Hur
voxspell 1.0 (prototype) was developed by Nathan Hur and Harry Lim.

*Some code written by Harry Lim remains in the application with written permission.*

This version is for evaluation purposes and has a Evaluation-only unlock all level mode (see usage guide)


##quick start
Download/clone as .zip, extract and run the run.sh script
(requires Oracle Java 1.8 and festival text-to-speech)

##brief usage guide

###levels
Only level 1 is unlocked on first launch. The next level is automatically and permanently unlocked when
at least 9 out of 10 words are spelt correctly on the previous level. You can pick any unlocked level
to start the quiz on. The developer only button temporarily enables all levels. You can reset all
of your unlocked levels with the reset button.

###spelling
You can listen to the word again without penalty by clicking the "Hear again" button. Entering invalid
characters will not count as a wrong spelling and you will be allowed to try again without penalty.
Some words contain apostrophes - if your spelling does not contain it while the word does, you will
be told and asked to retry without penalty (and vice versa).

Scoring 9 or more on a level enables you to watch a video reward.

You can change the spelling list to a custom one in Settings.

###reviewing
You can review all the words you got wrong at the end of each level you have completed. You can also review
the words you got wrong in the previous review quiz. Unless you were reviewing after scoring 9/10, you cannot
access the next level by simply reviewing.
If you qualify for the next level (getting 9 out of 10 on the quiz) and then review the one word you got wrong,
you still have access to the video reward and next level.

###achievements
You can unlock achievements by completing levels and doing other things! Basically improving your spelling and
advancing through the levels will net you achievements. For full information, see the manual.

###statistics
The stats are intended for parents/teachers to review the words that the user has gotten wrong. A brief
display of the words spelt during the current level will appear at the end. 

Full statistics of all sessions are available. These are resettable.

Statistics are stored separately with each spelling list.

##project structure

```
src
    ├── tests ** test suite for selected classes **
    └── voxspell
        ├── Voxspell.java                           // entry point for application
        ├── engine ** package for back-end / functionality **
        │   ├── Achievement.java                    // represents an achievement
        │   ├── DataIO.java                         // deals with file IO and saving
        │   ├── Festival.java                       // deals with festival tts and voice changing
        │   ├── LevelData.java                      // static link class to store global application state (levels, etc.)
        │   ├── QueuedEvent                         // interface to represent queued FX thread event
        │   ├── SceneManager.java                   // class that handles scene transfers
        │   ├── Word.java                           // class to represent a word
        │   └── WordList.java                       // class to parse file and get lists of words
        └── scenes ** package for front-end and controllers **
            ├── assets ** folder for images, sounds **
            ├── controllers
            │   ├── MainController.java             // controller for main/welcome scene
            │   ├── AchievementsPopup.java          // popup to show unlocked achievement
            │   ├── SettingsController.java         // =voice/level settings
            │   ├── EndSessionController.java       // end of level controller
            │   ├── SpellingController.java         // controller for spelling scene
            │   ├── StatsController.java            // controller for statistic scene
            │   ├── VideoController.java            // controller for video reward scene
            ├── achievements.fxml                   // scene to show all achievements
            ├── endSession.fxml                     // scene on reaching the end of a level
            ├── main.fxml                           // main/welcome scene for level selection
            ├── settings.fxml                       // settings for lists/voices/stats
            ├── spelling.fxml                       // spelling scene for quizzes
            ├── stats.fxml                          // statistics scene
            └── video.fxml                          // video reward scene
```

## assets
All assets are from royalty-free sources
Loading gif: ajaxload.info
Background (all backgrounds), achievement images: openclipart.org
Welcome music: freemusicarchive.org
Ta-da sound: Mike Koenig (Attribution 3.0)
Bubble click sound: Self-recorded
Music icons: self-made
