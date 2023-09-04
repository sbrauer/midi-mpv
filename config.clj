;; Sample config file
;; Config is the map at the end of the file.

(use 'midi-mpv.util)

(def midi-device-name "IAC Driver IAC Bus 1")
(def base-socket-path "/tmp/mpv-socket")

;; Note these depend on a custom MPV script (included in the repo).
;; The standard MPV playlist-play-index command would work
;; if it tolerated index out of bounds, but instead MPV crashes!
(defn playlist-commands
  [start num]
  (into {}
        (for [n (range num)]
          [(+ n start)
           (str "script-message-to sammy safe-playlist-play-index " n)])))

(defn seek-commands
  [start]
  {(+ start 0) "seek 0 absolute"
   (+ start 1) "seek 8 absolute-percent"
   (+ start 2) "seek 16 absolute-percent"
   (+ start 3) "seek 25 absolute-percent"
   (+ start 4) "seek 33 absolute-percent"
   (+ start 5) "seek 41 absolute-percent"
   (+ start 6) "seek 50 absolute-percent"
   (+ start 7) "seek 58 absolute-percent"
   (+ start 8) "seek 66 absolute-percent"
   (+ start 9) "seek 75 absolute-percent"
   (+ start 10) "seek 83 absolute-percent"
   (+ start 11) "seek 91 absolute-percent"})

(defn misc-commands
  [start]
  {(+ start C) "set pause yes"
   (+ start D) "set pause no"
   (+ start E) "set mute yes"
   (+ start F) "set mute no"
   (+ start G) "set sub-visibility no"
   (+ start A) "set sub-visibility yes"
   (+ start B) "ab-loop"

   (+ start C#) "set panscan 1.0"
   (+ start D#) "set panscan 0.0"
   ;; (+ start F#) "multiply speed 1/1.1"
   ;; (+ start G#) "set speed 1.0"
   ;; (+ start A#) "multiply speed 1.1"
   (+ start F#) "seek -5"
   (+ start G#) "cycle pause"
   (+ start A#) "seek 5"})

(defn speed-commands
  [start]
  ;; Absolute speed settings. Maybe tweak some of these...
  {(+ start 0) "set speed 0.1"
   (+ start 1) "set speed 0.2"
   (+ start 2) "set speed 0.35"
   (+ start 3) "set speed 0.5"
   (+ start 4) "set speed 0.7"
   (+ start 5) "set speed 0.85"
   (+ start 6) "set speed 1.0" ;; normal
   (+ start 7) "set speed 1.25"
   (+ start 8) "set speed 1.5"
   (+ start 9) "set speed 1.75"
   (+ start 10) "set speed 2.0"
   (+ start 11) "set speed 3.0"
   (+ start 12) "set speed 4.0"})

(def note-commands
  (merge (playlist-commands (octave 2) (octave 1))
         (seek-commands     (octave 3))
         (misc-commands     (octave 4))
         (speed-commands    (octave 5))))

(defn handle-cc
  [cc v]
  (when (= 1 cc) ;; mod wheel
    (let [percent (* 100 (/ v 127.0))]
      (format "seek %s absolute-percent" percent))))

(defn event->command
  [{:keys [command data1 data2] :as event}]
  (case command
    ;; FIXME: extend to support pitch-bend (for speed)
    :note-on (get note-commands data1)
    :control-change (handle-cc data1 data2)))

(defn event->socket-path
  [{:keys [channel] :as event}]
  (format "%s.%s" base-socket-path channel))

(defn midi-event->action
  [event]
  (when-let [command (event->command event)]
    {:socket-path (event->socket-path event)
     :command     command}))

;; This is it - the config map in all its glory...
{:midi-device-name   midi-device-name
 :midi-event->action midi-event->action}
