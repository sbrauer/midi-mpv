# midi-mpv

Control [MPV](https://mpv.io/) via midi.

Trigger playlist clips, (un)pause, seek to positions, change speed, etc.

Use the expressive power of Clojure to map midi events (note-on, control-change, etc) to any MPV commands you like.

You can control multiple MPV instances (by routing commands to distinct sockets).

Refer to [example mappers](example-mappers) to get an idea of the midi mapping possibilities.

## Usage

You'll need to [install Clojure](https://clojure.org/guides/install_clojure) if you don't already have it.

Many of the example mappers depend on a custom MPV script that provides an alternative command to play a specific playlist item.
Copy [sammy.lua](mpv/scripts/sammy.lua) to your MPV scripts folder (`~/.mpv/scripts`).

Start up one or more instances of MPV such that each instance creates its own socket for receiving commands.
For example, see the included script [mpv-for-vj](scripts/mpv-for-vj) which starts one instance using a socket at `/tmp/mpv-socket.0` and enables several options suitable for playing videos "VJ-style" (fullscreen, no OSD, infinite looping, etc). Additional instances could use `/tmp/mpv-socket.1`, etc.

Startup this app. You'll need to:
- set an enviroment variable specifying the desired midi device
- specify a Clojure script that provides a function mapping midi events to MPV commands

As an example, see the included script [start](scripts/start).
You could use one of the included [example mappers](example-mappers), or use one of them as a starting point for your own mapper.

## References

https://mpv.io/manual/stable/#command-interface

https://mpv.io/manual/stable/#json-ipc
