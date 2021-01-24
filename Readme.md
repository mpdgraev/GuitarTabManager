CLI application written in Kotlin/JVM to help searching for guitar tabs in a local directory

For now, requires the tabs to be named according to the current ultimate-guitar naming scheme:
`<artist - song (revision).xyz>`

Required directory structure is one folder per tuning, inside that tabs can be placed in the root or inside subdirs

Build with `gradle installDist`

Run with `build/install/TabManager/bin/TabManager`