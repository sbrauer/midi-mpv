# midi-mpv

Control [MPV](https://mpv.io/) via midi.

Trigger playlist clips, (un)pause, seek to positions, change speed, etc.

Use the expressive power of Clojure to map midi events (note-on, control-change, etc) to any MPV commands you like.

You can control multiple MPV instances (by routing commands to distinct sockets).

Refer to sample config file [config.clj](config.clj) which routes by midi channel.

Routing by channel is just one possibility. Keyboard splits (routing by note-on note) would be a piece of cake.
Pretty much anything you might imagine doing based on midi event data should be feasible.

## Installation

FIXME

## References

https://mpv.io/manual/stable/#command-interface

https://mpv.io/manual/stable/#json-ipc
