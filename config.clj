(use 'midi-mpv.util)

(def midi-device-name "IAC Driver IAC Bus 1")
(def base-socket-path "/tmp/mpv-socket")

(def commands
  {:note-on
   ;; DRY this up ofc
   {(+ (octave 2) 0) "script-message-to sammy safe-playlist-play-index 0"
    (+ (octave 2) 1) "script-message-to sammy safe-playlist-play-index 1"
    (+ (octave 2) 2) "script-message-to sammy safe-playlist-play-index 2"
    (+ (octave 2) 3) "script-message-to sammy safe-playlist-play-index 3"
    (+ (octave 2) 4) "script-message-to sammy safe-playlist-play-index 4"
    (+ (octave 2) 5) "script-message-to sammy safe-playlist-play-index 5"
    (+ (octave 2) 6) "script-message-to sammy safe-playlist-play-index 6"
    (+ (octave 2) 7) "script-message-to sammy safe-playlist-play-index 7"
    (+ (octave 2) 8) "script-message-to sammy safe-playlist-play-index 8"
    (+ (octave 2) 9) "script-message-to sammy safe-playlist-play-index 9"
    (+ (octave 2) 10) "script-message-to sammy safe-playlist-play-index 10"
    (+ (octave 2) 11) "script-message-to sammy safe-playlist-play-index 11"

    (+ (octave 3) 0) "seek 0 absolute"
    (+ (octave 3) 1) "seek 8 absolute-percent"
    (+ (octave 3) 2) "seek 16 absolute-percent"
    (+ (octave 3) 3) "seek 25 absolute-percent"
    (+ (octave 3) 4) "seek 33 absolute-percent"
    (+ (octave 3) 5) "seek 41 absolute-percent"
    (+ (octave 3) 6) "seek 50 absolute-percent"
    (+ (octave 3) 7) "seek 58 absolute-percent"
    (+ (octave 3) 8) "seek 66 absolute-percent"
    (+ (octave 3) 9) "seek 75 absolute-percent"
    (+ (octave 3) 10) "seek 83 absolute-percent"
    (+ (octave 3) 11) "seek 91 absolute-percent"

    (+ (octave 4) C) "set pause yes"          ;; pause
    (+ (octave 4) D) "set pause no"           ;; play
    (+ (octave 4) E) "set mute yes"           ;; mute
    (+ (octave 4) F) "set mute no"            ;; unmute
    (+ (octave 4) G) "set sub-visibility no"  ;; subs off
    (+ (octave 4) A) "set sub-visibility yes" ;; subs on
    (+ (octave 4) B) "ab-loop" ;; ab-loop start/end/off

    (+ (octave 4) C#) "set panscan 1.0"       ;; panscan on
    (+ (octave 4) D#) "set panscan 0.0"       ;; panscan off
    ;; (+ (octave 4) F#) "multiply speed 1/1.1" ;; slower
    ;; (+ (octave 4) G#) "set speed 1.0"        ;; reset to normal
    ;; (+ (octave 4) A#) "multiply speed 1.1"   ;; faster
    (+ (octave 4) F#) "seek -5" ;; jump back 5 secs like left arrow key
    (+ (octave 4) G#) "cycle pause" ;; toggle pause
    (+ (octave 4) A#) "seek 5" ;; jump forward 5 secs like right arrow key

    ;; Absolute speed settings. Maybe tweak some of these...
    (+ (octave 5) 0) "set speed 0.1"
    (+ (octave 5) 1) "set speed 0.2"
    (+ (octave 5) 2) "set speed 0.35"
    (+ (octave 5) 3) "set speed 0.5"
    (+ (octave 5) 4) "set speed 0.7"
    (+ (octave 5) 5) "set speed 0.85"
    (+ (octave 5) 6) "set speed 1.0" ;; normal
    (+ (octave 5) 7) "set speed 1.25"
    (+ (octave 5) 8) "set speed 1.5"
    (+ (octave 5) 9) "set speed 1.75"
    (+ (octave 5) 10) "set speed 2.0"
    (+ (octave 5) 11) "set speed 3.0"
    (+ (octave 5) 12) "set speed 4.0"}})

;; FIXME: extend to support pitch-bend (for speed) and mod wheel CC #1 for seek.
(defn event->command
  [{:keys [command data1] :as event}]
  (get-in commands [command data1]))

(defn event->socket-path
  [{:keys [channel] :as event}]
  (format "%s.%s" base-socket-path channel))

(defn midi-event->action
  [event]
  (when-let [command (event->command event)]
    {:socket-path (event->socket-path event)
     :command     command}))

{:midi-device-name   midi-device-name
 :midi-event->action midi-event->action}
