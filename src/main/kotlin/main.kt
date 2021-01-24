import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

val defaultRoot = File("/home/mike/Documents/tabs")

class Search: CliktCommand(
    help = """
        Run non-interactive query
    """.trimIndent(),
    printHelpOnEmptyArgs = true
) {
    val tuning by option("-t", "--tuning", help="Tuning name")
    val artist by option("-a", "--artist", "--band", help="Artist name")
    val song by option("-s", "--song", help="Song name")
    val dir by option("-d", "--directory", help="Tab root directory").file().default(defaultRoot)
    override fun run() {
        val root = TabRoot(dir)
        val tabs = root.searchTabs(tuning?.let{Tuning(it)}, song?.let{Song(it)}, artist?.let{Artist(it)})
        tabs.forEach{
            echo(it)
        }
    }
}

class TabManager: CliktCommand(
    help = """
        When ran without arguments, starts an interactive query session.
        
        Pass 'search' as an argument to run a non-interactive query, see 'search --help' for more info.
        
        Required tab directory structure:
        
        ./<tuning>/<optional subdir>/<artist - song (revision).xyz>
    """.trimIndent()
) {
    val dir by option("-d", "--directory", help="Tab root directory").file().default(defaultRoot)
    override fun run() {
//        echo("entering interactive mode, do something here")
        // todo: open tabs in tuxguitar
        // todo: fix tab names that failed to match
    }
}

fun main(args: Array<String>) = TabManager().subcommands(Search()).main(args)
