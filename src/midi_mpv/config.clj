(ns midi-mpv.config
  (:require [midi-mpv.util :as util]))

(def midi-device-name "IAC Driver IAC Bus 1")
(def socket-path "/tmp/mpvsocket")

(def commands
  ;; FIXME: Add some CC commands at least as examples; say pitchbend to speed or modwheel for seek.
  ;; FIXME: Perhaps also add support for multiple sockets and figure out how to route events to sockets.
  {0 {:note-on
      ;; DRY this up ofc
      {(+ (util/octave 2) 0) "script-message-to sammy safe-playlist-play-index 0"
       (+ (util/octave 2) 1) "script-message-to sammy safe-playlist-play-index 1"
       (+ (util/octave 2) 2) "script-message-to sammy safe-playlist-play-index 2"
       (+ (util/octave 2) 3) "script-message-to sammy safe-playlist-play-index 3"
       (+ (util/octave 2) 4) "script-message-to sammy safe-playlist-play-index 4"
       (+ (util/octave 2) 5) "script-message-to sammy safe-playlist-play-index 5"
       (+ (util/octave 2) 6) "script-message-to sammy safe-playlist-play-index 6"
       (+ (util/octave 2) 7) "script-message-to sammy safe-playlist-play-index 7"
       (+ (util/octave 2) 8) "script-message-to sammy safe-playlist-play-index 8"
       (+ (util/octave 2) 9) "script-message-to sammy safe-playlist-play-index 9"
       (+ (util/octave 2) 10) "script-message-to sammy safe-playlist-play-index 10"
       (+ (util/octave 2) 11) "script-message-to sammy safe-playlist-play-index 11"

       (+ (util/octave 3) 0) "seek 0 absolute"
       (+ (util/octave 3) 1) "seek 8 absolute-percent"
       (+ (util/octave 3) 2) "seek 16 absolute-percent"
       (+ (util/octave 3) 3) "seek 25 absolute-percent"
       (+ (util/octave 3) 4) "seek 33 absolute-percent"
       (+ (util/octave 3) 5) "seek 41 absolute-percent"
       (+ (util/octave 3) 6) "seek 50 absolute-percent"
       (+ (util/octave 3) 7) "seek 58 absolute-percent"
       (+ (util/octave 3) 8) "seek 66 absolute-percent"
       (+ (util/octave 3) 9) "seek 75 absolute-percent"
       (+ (util/octave 3) 10) "seek 83 absolute-percent"
       (+ (util/octave 3) 11) "seek 91 absolute-percent"

       ;; FIXME: Are these misc mappings good?  Maybe tweak. Used to have a few cycle commands, but replaced with sets.

       (+ (util/octave 4) util/C) "set pause yes"          ;; pause
       (+ (util/octave 4) util/D) "set pause no"           ;; play
       (+ (util/octave 4) util/C#) "set panscan 1.0"       ;; panscan on
       (+ (util/octave 4) util/D#) "set panscan 0.0"       ;; panscan off
       (+ (util/octave 4) util/E) "set mute yes"           ;; mute
       (+ (util/octave 4) util/F) "set mute no"            ;; unmute
       (+ (util/octave 4) util/G) "set sub-visibility no"  ;; subs off
       (+ (util/octave 4) util/A) "set sub-visibility yes" ;; subs on

       ;; (+ (util/octave 4) util/F#) "multiply speed 1/1.1" ;; slower
       ;; (+ (util/octave 4) util/G#) "set speed 1.0"        ;; reset to normal
       ;; (+ (util/octave 4) util/A#) "multiply speed 1.1"   ;; faster

       (+ (util/octave 4) util/F#) "seek -5" ;; jump back 5 secs like left arrow key
       (+ (util/octave 4) util/G#) "cycle pause" ;; toggle pause
       (+ (util/octave 4) util/A#) "seek 5" ;; jump forward 5 secs like right arrow key

       (+ (util/octave 4) util/B) "ab-loop" ;; ab-loop start/end/off

       ;; Absolute speed settings. Maybe tweak some of these...
       (+ (util/octave 5) 0) "set speed 0.1"
       (+ (util/octave 5) 1) "set speed 0.2"
       (+ (util/octave 5) 2) "set speed 0.35"
       (+ (util/octave 5) 3) "set speed 0.5"
       (+ (util/octave 5) 4) "set speed 0.7"
       (+ (util/octave 5) 5) "set speed 0.85"
       (+ (util/octave 5) 6) "set speed 1.0" ;; normal
       (+ (util/octave 5) 7) "set speed 1.25"
       (+ (util/octave 5) 8) "set speed 1.5"
       (+ (util/octave 5) 9) "set speed 1.75"
       (+ (util/octave 5) 10) "set speed 2.0"
       (+ (util/octave 5) 11) "set speed 3.0"
       (+ (util/octave 5) 12) "set speed 4.0"}}})

;; FIXME: extend to support pitch-bend (for speed) and mod wheel CC #1 for seek.
(defn event->command
  [{:keys [channel command data1] :as event}]
  (get-in commands [channel command data1]))

(defn midi-event->action
  [event]
  (when-let [command (event->command event)]
    {:socket-path socket-path
     :command     command}))

(def config
  {:midi-device-name   midi-device-name
   :midi-event->action midi-event->action})
