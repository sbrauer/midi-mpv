;; Sample mapper file with keyboard split
;; where left side triggers clips for first MPV
;; and right side triggers clips for second MPV.

(use 'midi-mpv.util)

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
  [start num]
  (into {}
        (for [n (range num)]
          [(+ n start)
           (if (zero? n)
             "seek 0 absolute"
             (format "seek %s absolute-percent" (* 100 (/ (float n) num))))])))

(defn socket-path
  [idx]
  (format "%s.%s" base-socket-path idx))

(defn add-socket-path
  [idx note-commands]
  (into {}
        (map (fn [[note cmd]]
               [note
                {:command cmd
                 :socket-path (socket-path idx)}])
             note-commands)))

(def note->action
  ;; These settings match my 49 key midi keyboard (with default octave shift).
  ;; Tweak to match your keyboard...
  (merge (add-socket-path 0 (playlist-commands (octave 3) (octave 1)))
         (add-socket-path 0 (seek-commands     (octave 4) (octave 1)))
         (add-socket-path 1 (playlist-commands (octave 5) (octave 1)))
         (add-socket-path 1 (seek-commands     (octave 6) (octave 1)))))

(fn midi-event->action
  [{:keys [command note] :as event}]
  (when (= :note-on command)
    (note->action note)))
