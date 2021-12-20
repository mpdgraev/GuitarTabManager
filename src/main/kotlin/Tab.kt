import com.github.ajalt.clikt.output.TermUi.echo
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.io.File

val FUZZYMIN = 90
@JvmInline
value class Song(val name: String){
    fun fuzEquals(other: Song) = FuzzySearch.weightedRatio(this.name, other.name) >= FUZZYMIN
}
@JvmInline
value class Tuning(val name: String){
    fun fuzEquals(other: Tuning) = FuzzySearch.weightedRatio(this.name, other.name) >= FUZZYMIN
}
@JvmInline
value class Artist(val name: String){
    fun fuzEquals(other: Artist) = FuzzySearch.weightedRatio(this.name, other.name) >= FUZZYMIN
}
@JvmInline
value class Version(val name: String) // validate to confirm 'ver' in there?

// todo: add File
data class Tab(val song: Song, val artist: Artist, val tuning: Tuning? = null, val version: Version? = null, val file: File): Comparable<Tab> {
    companion object {
        // naming schema as downloaded from ultimate-guitar
        // todo: use Tuxguitar libraries to parse the actual content instead of relying on filenames
        // todo: find a way to allow brackets in song name
//        val nameParser = Regex("""([^-\s]+(?:\s[^-\s]+)*)\s*-\s*([^(\s]+(?:\s[^(\s]+)*)\s*(?:\((.+)\))?\.[\d\w]+$""")
        val nameParser = Regex("""(.+)\s-\s([^(\s]+(?:\s[^(\s]+)*)(?:\s\((.+)\))?\.[\d\w]+$""")

        fun fromFileAndTuning(file: File, tuning: Tuning?): Tab? {
        if (file.isDirectory){
            throw Exception("This is not the tab you're looking for: ${file.absolutePath}")
        }
        val match = nameParser.find(file.name)
        if (match == null){
//            echo("could not parse filename ${file.name}", err = true) // probably shouldn't show these by default
            return null
        }
        val matchedGroups = match.groupValues
        if (matchedGroups[3] == ""){
            // no version
            return Tab(Song(matchedGroups[2]), Artist(matchedGroups[1]), tuning, file=file)
        }
        return Tab(Song(matchedGroups[2]), Artist(matchedGroups[1]), tuning, Version(matchedGroups[3]), file)
        }
    }

    fun getGoodFileName(currentName: File): String =
        version?.let {
            "${artist.name} - ${song.name} (${version.name}).${currentName.extension}"
        }?: "${artist.name} - ${song.name}.${currentName.extension}"

    override fun compareTo(other: Tab): Int {
        if (this.artist.name != other.artist.name){
            return this.artist.name.compareTo(other.artist.name)
        }
        if (this.song.name != other.song.name){
            return this.song.name.compareTo(other.song.name)
        }
        if (this.version != null && other.version != null && this.version.name != other.version.name){
            return this.version.name.compareTo(other.version.name)
        }
        return 0;
    }

    override fun toString() =
        version?.let {
            "${artist.name} - ${song.name} (${version.name}) (${tuning?.name}) ~ ${file.absolutePath}"
        }?: "${artist.name} - ${song.name} (${tuning?.name}) ~ ${file.absolutePath}"
}
