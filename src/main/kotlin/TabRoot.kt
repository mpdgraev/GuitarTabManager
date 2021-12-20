import com.github.ajalt.clikt.output.TermUi.echo
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.io.File

// todo: performance comparison of creating index(es) vs filtering on list of all
// probably want to index in interactive mode, filter in simple search
data class TabsBySong(val map: Map<Song, List<Tab>>)
data class TabsByArtist(val map: Map<Artist, List<Tab>>)
//data class TabsByTuning(val map: Map<Tuning, List<Tab>>)

/**
 * Tabs should be placed in subfolders, no tabs are allowed in the root as adding a null Tuning
 * makes stuff needlessly complicated, just stuff em all in an "unknown tuning" folder or whatever
 */
class TabRoot(private val root: File) {
    // todo: add cache functionality? Probably not worth it
    // todo: always start by indexing everything on start? would make everything much more trivial

//    var tabs = getAllTabs()
    fun getAllTabs(): List<Tab> {
        return root.listFiles()?.flatMap {
            when (it.isDirectory) {
                true -> getAllTabsOneDir(it, Tuning(it.name))
                false -> null
            }
        }?: throw Exception("listFiles returned null, does ${root.absolutePath} exist?")
    }

    // slightly more efficient than getting all tabs and then using associate() to map on tuning
    fun getTabsByTuning(): Map<Tuning, List<Tab>> {
        return root.listFiles()?.map {
            when (it.isDirectory) {
                true -> Tuning(it.name) to getAllTabsOneDir(it, Tuning(it.name))
                false -> null
            }
        }?.filterNotNull()?.toMap() ?: throw Exception("listFiles returned null, does ${root.absolutePath} exist?")
    }

    fun getAllTabsOneDir(dir: File, tuning: Tuning): List<Tab> {
        return dir.listFiles()?.flatMap {
            when (it.isDirectory) {
                true -> getAllTabsOneDir(it, tuning)
                false -> listOf(Tab.fromFileAndTuning(it, tuning))
            }
        }?.filterNotNull() ?: throw Exception("listFiles returned null, does ${dir.absolutePath} exist?")
    }

    fun getAllTunings(): List<Tuning>{
        return root.listFiles()?.filter{it.isDirectory}?.map{Tuning(it.name)}
            ?: throw Exception("listFiles returned null, does ${root.absolutePath} exist?")
    }

    fun searchTabs(tuning: Tuning?, song: Song?, artist: Artist?): List<Tab> {
        if (tuning == null && song == null && artist == null){
            echo("Please provide a song, artist or tuning to search for", err = true)
        }
        var filtered = getAllTabs()
        if (tuning != null) {
            echo("searching for tuning $tuning")
//            val tunings = getAllTunings().filter { tuning.fuzEquals(it) }
//            filtered = tunings.flatMap { getAllTabsOneDir(root.resolve(tuning.name), it) }
            filtered = filtered.filter{it.tuning?.let{tuning.fuzEquals(it)} ?: false}
        }
        if (artist != null){
            echo("searching for artist $artist")
            filtered = filtered.filter{artist.fuzEquals(it.artist)}
        }
        if (song != null){
            echo("searching for song $song")
            filtered = filtered.filter{song.fuzEquals(it.song)}
        }
        return filtered
    }
}