;; Sample mapper file with keyboard split
;; where left side triggers clips for first MPV
;; and right side triggers clips for second MPV.

(use 'midi-mpv.util)

(def base-socket-path "/tmp/mpv-socket")

;; Note values for keyboard split
;; These match my 49 key midi keyboard (with default octave shift).
;; Tweak to match your keyboard...
(def start1 (octave 3))
(def start2 (octave 5))
(def end    (octave 7))

;; Note this depend on a custom MPV script (included in the repo).
;; The standard MPV playlist-play-index command would work
;; if it tolerated index out of bounds, but instead MPV crashes!
(defn playlist-command
  [idx]
  (str "script-message-to sammy safe-playlist-play-index " idx))

(defn note->idx
  "Return 0 when note is in first split
  1 when note is in second split
  or nil when note is out of range."
  [note]
  (when (>= note start1)
    (if (< note start2)
      0
      (when (<= note end)
        1))))

(defn socket-path
  [idx]
  (format "%s.%s" base-socket-path idx))

(fn midi-event->action
  [{:keys [command note] :as event}]
  (when (= :note-on command)
    (when-let [idx (note->idx note)]
      {:socket-path (socket-path idx)
       :command (let [start (if (zero? idx) start1 start2)]
                  (playlist-command (- note start)))})))
