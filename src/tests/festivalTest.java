package tests;

import org.junit.Before;
import org.junit.Test;
import voxspell.Festival;
import voxspell.Word;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the voxspell.Festival module
 * Created by nateeo on 6/09/16.
 */
public class festivalTest {
    private Festival festival;

    @Before
    public void setUp() {
        festival = new Festival();
    }
    @Test
    public void testValidVoices() {
        assertEquals("Expect correct user-friendly type to be returned", festival.getVoiceType(), "English");
        festival.setVoiceType(Festival.WELSH);
        assertEquals("Expect setting type to update correctly", festival.getVoiceType(), "Welsh");
    }

    @Test public void testInvalidVoices() {
        festival.setVoiceType("");
        assertEquals("Expect incorrect voice type set to default to English", festival.getVoiceType(), "English");
    }

    @Test
    public void readNonTryAgain() {
        Word word = new Word("word");
        String command = festival.commandBuilder(word, false);
        assertEquals("Expect a non-try-again read to be correct",
                "echo \"Please spell word.\" | festival --tts --language english",
                command);
    }

    @Test
    public void readTryAgain() {
        Word word = new Word("word");
        String command = festival.commandBuilder(word, true);
        assertEquals("Expect a try-again read to be correct",
                "echo \"Try again. word. word.\" | festival --tts --language english",
                command);
    }

}