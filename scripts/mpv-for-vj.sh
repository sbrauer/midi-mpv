# Just an example; tweak to taste.
# The most critical detail is that you give each MPV instance its own dedicated socket file.
# The sample config.clj assumes a naming convention like `/tmp/mpv-socket.0`, `/tmp/mpv-socket.1`, etc.
# Pass a dir or playlist argument to this script.
MPV=/Applications/mpv.app/Contents/MacOS/mpv
$MPV --input-ipc-server=/tmp/mpv-socket.0 --keep-open=yes --keep-open-pause=no --loop-playlist --fullscreen --loop-file=inf --video-osd=no $*
