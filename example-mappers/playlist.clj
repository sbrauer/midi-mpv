;; Example midi mapper file
;; This one has a bunch of playlist triggers for a range of note-on events.
;; Also includes modwheel -> seek and pitchbend -> speed.

(use 'midi-mpv.util)

(def base-socket-path "/tmp/mpv-socket")

;; Prefs for pitch-bend -> speed
(def top-speed 4.0)
(def bottom-speed 0.25)

;; Note these depend on a custom MPV script (included in the repo).
;; The standard MPV playlist-play-index command would work
;; if it tolerated index out of bounds, but instead MPV crashes!
(defn playlist-commands
  [start num]
  (into {}
        (for [n (range num)]
          [(+ n start)
           (str "script-message-to sammy safe-playlist-play-index " n)])))

(def note-commands
  ;; These start at the low C of my 49 key midi keyboard (with default octave shift).
  (playlist-commands (octave 3) (inc (octave 4))))

(defn handle-cc
  [cc v]
  (when (= 1 cc) ;; mod wheel
    (let [percent (* 100 (/ v 127.0))]
      (format "seek %s absolute-percent" percent))))

(defn handle-pb
  [v1 v2]
  (let [pb (interpret-pitch-bend-data v1 v2)
        normal-speed 1.0
        speed (cond
                (pos? pb) (+ normal-speed (* pb (- top-speed    normal-speed)))
                (neg? pb) (+ normal-speed (* pb (- normal-speed bottom-speed)))
                :default  normal-speed)]
    (format "set speed %s" speed)))

(defn event->command
  [{:keys [command data1 data2] :as event}]
  (case command
    :note-on (get note-commands data1)
    :control-change (handle-cc data1 data2)
    :pitch-bend (handle-pb data1 data2)
    nil))

(defn event->socket-path
  [{:keys [channel] :as event}]
  (format "%s.%s" base-socket-path channel))

(fn midi-event->action
  [event]
  (when-let [command (event->command event)]
    {:socket-path (event->socket-path event)
     :command     command}))
