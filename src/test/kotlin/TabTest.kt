import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class TabTest {
    @Test
    fun testFileNameVersion(){
        val tab = Tab.fromFileAndTuning(File("~/Documents/tabs/B/Amon Amarth - The Pursuit Of Vikings (ver 6 by Lhauc).gp5"), Tuning("B"))
            ?: throw Exception("couldn't parse tab name")
        assertEquals(Artist("Amon Amarth"), tab.artist)
        assertEquals(Song("The Pursuit Of Vikings"), tab.song)
        assertEquals(Tuning("B"), tab.tuning)
        assertEquals(Version("ver 6 by Lhauc"), tab.version)
    }

    @Test
    fun testFileNameNoVersion(){
        val tab = Tab.fromFileAndTuning(File("~/Documents/tabs/E/Deafheaven - Sunbather.gpx"), Tuning("E"))
            ?: throw Exception("couldn't parse tab name")
        assertEquals(Artist("Deafheaven"), tab.artist)
        assertEquals(Song("Sunbather"), tab.song)
        assertEquals(Tuning("E"), tab.tuning)
        assertEquals(null, tab.version)
    }

    @Test
    fun testFileNameArtistDashes(){
        val tab = Tab.fromFileAndTuning(File("L'Arc-en-Ciel - Somesong.gpx"), Tuning("E"))
            ?: throw Exception("couldn't parse tab name")
        assertEquals(Artist("L'Arc-en-Ciel"), tab.artist)
        assertEquals(Song("Somesong"), tab.song)
        assertEquals(Tuning("E"), tab.tuning)
        assertEquals(null, tab.version)
    }

    @Test
    fun testFileNameSongPeriods(){
        val tab = Tab.fromFileAndTuning(File("L'Arc-en-Ciel - This.is.Somesong.gpx"), Tuning("E"))
            ?: throw Exception("couldn't parse tab name")
        assertEquals(Artist("L'Arc-en-Ciel"), tab.artist)
        assertEquals(Song("This.is.Somesong"), tab.song)
        assertEquals(Tuning("E"), tab.tuning)
        assertEquals(null, tab.version)
    }

    @Test
    fun toFileName(){
        val tab = Tab(Song("song name"), Artist("some band"))
        val versionedTab = Tab(Song("song name"), Artist("some band"), version = Version("1.0.0"))
        val orig = File("some_band_song_name.gp5")
        assertEquals("some band - song name.gp5", tab.getGoodFileName(orig))
        assertEquals("some band - song name (1.0.0).gp5", versionedTab.getGoodFileName(orig))
    }

    @Test
    fun testTabSorting(){
        val tab1 = Tab.fromFileAndTuning(File("Amon Amarth - The Pursuit Of Vikings (ver 6 by Lhauc).gp5"), Tuning("B"))
        val tab2 = Tab.fromFileAndTuning(File("Amon Amarth - The Pursuit Of Vikings (ver 7 by Lhauc).gp5"), Tuning("B"))
        val tab3 = Tab.fromFileAndTuning(File("Deafheaven - Sunbather.gpx"), Tuning("E"))

    }

    @Test
    fun a(){
//        getAllTabs(File("/home/mike/Documents/tabs"))
    }
}